package peacefulotter.engine.rendering;

import org.lwjgl.opengl.GL;
import peacefulotter.engine.components.*;
import peacefulotter.engine.components.lights.BaseLight;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.shaders.ShaderTypes;
import peacefulotter.engine.utils.MappedValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

/*

    Move camera inside Game class ?
 */


public class RenderingEngine extends MappedValues
{
    private final Window window;
    private Camera camera;

    private final List<BaseLight> lights;
    private BaseLight activeLight;

    private final Map<String, Integer>  samplerMap;

    public RenderingEngine()
    {
        this.lights = new ArrayList<>();
        this.window = new Window();
        this.samplerMap = new HashMap<>();
        samplerMap.put( "diffuse", 0 );
        addVector3f( "ambient", new Vector3f( 0.2f, 0.2f, 0.2f ) );

        GL.createCapabilities();
        glClearColor( 0.3f, 0.3f, 0.9f, 0.8f );
        glFrontFace( GL_CW );
        glCullFace( GL_BACK );
        glEnable( GL_CULL_FACE );
        glEnable( GL_DEPTH_TEST );
        glEnable( GL_DEPTH_CLAMP );
        glEnable( GL_TEXTURE_2D );
    }


    public void render( GameObject object )
    {
        if ( camera == null )
            System.err.println( "Main camera not found." );

        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

        object.render( ShaderTypes.AMBIENT.getShader(), this );

        glEnable( GL_BLEND );
        glBlendFunc( GL_ONE, GL_ONE );
        glDepthMask( false );
        glDepthFunc( GL_EQUAL );

        for ( BaseLight light : lights )
        {
            // light.getShader().setRenderingEngine( this ); // move this to init
            activeLight = light;
            object.render( light.getShader(), this );
        }

        glDepthFunc( GL_LESS );
        glDepthMask( true );
        glDisable( GL_BLEND );
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
}
