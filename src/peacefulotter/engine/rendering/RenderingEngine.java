package peacefulotter.engine.rendering;

import org.lwjgl.opengl.GL;
import peacefulotter.engine.components.*;
import peacefulotter.engine.components.lights.BaseLight;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.ShaderTypes;
import peacefulotter.engine.utils.MappedValues;
import peacefulotter.engine.utils.ProfileTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class RenderingEngine extends MappedValues
{
    private final ProfileTimer profiler;
    private final Window window;
    private Camera camera;

    private final List<BaseLight> lights;
    private final Shader ambient;
    private BaseLight activeLight;

    private final Map<String, Integer>  samplerMap;

    public RenderingEngine()
    {
        this.profiler = new ProfileTimer();
        this.lights = new ArrayList<>();
        this.window = new Window();
        this.samplerMap = new HashMap<>();

        samplerMap.put( "diffuse", 0 );
        samplerMap.put( "normalMap", 1 );
        samplerMap.put( "dispMap", 2 );

        GL.createCapabilities();
        glClearColor( 0.6f, 0.6f, 0.6f, 0.8f );
        glFrontFace( GL_CW );
        glCullFace( GL_BACK );
        glEnable( GL_CULL_FACE );
        glEnable( GL_DEPTH_TEST );
        // glEnable( GL_DEPTH_CLAMP );
        glEnable( GL_TEXTURE_2D );

        addVector3f( "ambient", new Vector3f( 0.4f, 0.4f, 0.4f ) );
        ambient = ShaderTypes.AMBIENT.getShader();

        // Window.bindAsRenderTarget(); Use for Render to Texture (not finished)
    }


    public void render( GameObject object )
    {
        if ( camera == null )
            throw new NullPointerException( "Main camera not found." );

        profiler.startInvocation();

        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

        object.renderAll( ambient, this );

        glEnable( GL_BLEND );
        glBlendFunc( GL_ONE, GL_ONE );
        glDepthMask( false );
        glDepthFunc( GL_EQUAL );

        for ( BaseLight light : lights )
        {
            activeLight = light;
            object.renderAll( light.getShader(), this );
        }

        glDepthFunc( GL_LESS );
        glDepthMask( true );
        glDisable( GL_BLEND );

        profiler.stopInvocation();
    }

    public void addLight( BaseLight light ) { lights.add( light ); }

    public BaseLight getActiveLight() { return activeLight; }

    public long getCurrentWindow() { return window.getWindow(); }

    public Camera getCamera() { return camera; }
    public void setCamera( Camera camera ) { this.camera = camera; }

    public int getSamplerSlot( String samplerName )
    {
        return samplerMap.get( samplerName );
    }

    public double displayRenderTime( double dividend ) { return profiler.displayAndReset( "Render time", dividend ); }
}
