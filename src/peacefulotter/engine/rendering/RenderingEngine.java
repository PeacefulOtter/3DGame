package peacefulotter.engine.rendering;

import org.lwjgl.opengl.GL;
import peacefulotter.engine.components.*;
import peacefulotter.engine.components.lights.BaseLight;
import peacefulotter.engine.components.renderer.Renderer;
import peacefulotter.engine.components.renderer.SkyBoxRenderer;
import peacefulotter.engine.components.renderer.TerrainRenderer;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.GUI.GUIRenderer;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.ShaderTypes;
import peacefulotter.engine.utils.Logger;
import peacefulotter.engine.utils.MappedValues;
import peacefulotter.engine.utils.ProfileTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine extends MappedValues
{
    // NIGHT private static final Vector3f skyColor = new Vector3f( 0.1f, 0.13f, 0.12f );
    private static final Vector3f skyColor = new Vector3f( 1f, 1f, 1f );

    private final ProfileTimer profiler;
    private final Window window;
    private final List<BaseLight> lights;
    private final List<Renderer> renderers;
    private final Shader ambient;

    private final Map<String, Integer>  samplerMap;

    private World world;

    private BaseLight activeLight;
    private Camera camera;


    public RenderingEngine()
    {
        this.profiler = new ProfileTimer();
        this.lights = new ArrayList<>();
        this.renderers = new ArrayList<>();
        this.window = new Window();
        this.samplerMap = new HashMap<>();

        samplerMap.put( "diffuse", 0 );
        samplerMap.put( "normalMap", 1 );
        samplerMap.put( "dispMap", 2 );
        samplerMap.put( "aTexture", 3 );
        samplerMap.put( "rTexture", 4 );
        samplerMap.put( "gTexture", 5 );
        samplerMap.put( "bTexture", 6 );
        samplerMap.put( "blendMap", 7 );
        samplerMap.put( "guiTexture", 8 );
        samplerMap.put( "cubeMap", 9 );

        GL.createCapabilities();
        glClearColor( skyColor.getX(), skyColor.getY(), skyColor.getZ(), 1f );
        glFrontFace( GL_CW );
        glCullFace( GL_BACK );
        glEnable( GL_CULL_FACE );
        glEnable( GL_DEPTH_TEST );
        //glEnable( GL_DEPTH_CLAMP );
        glEnable( GL_TEXTURE_2D );

        // NIGHT addVector3f( "ambient", new Vector3f( 0.00001f, 0.00001f, 0.00001f ) );
        addVector3f( "ambient", new Vector3f( 0.1f, 0.1f, 0.1f ) );
        // NIGHT addVector3f( "skyColor", skyColor.mul( 0.3f ) );
        addVector3f( "skyColor", skyColor );
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

        // will need to move this at the bottom of the renders
        if ( world != null )
            world.renderWorld( this );

        renderers.forEach( renderer -> renderer.render( this ) );

        glDepthFunc( GL_LESS );
        glDepthMask( true );
        glDisable( GL_BLEND );

        profiler.stopInvocation();
    }

    public void addLight( BaseLight light )
    {
        Logger.log( getClass(), "Adding " + light.getClass().getSimpleName() );
        lights.add( light );
    }

    public void addRenderer( Renderer renderer )
    {
        Logger.log( getClass(), "Adding a " + renderer.getClass().getSimpleName() );
        renderers.add( renderer );
    }

    public void setWorld( World world )
    {
        this.world = world;
    }

    public BaseLight getActiveLight() { return activeLight; }
    public long getCurrentWindow() { return window.getWindow(); }

    public Camera getCamera() { return camera; }
    public void setCamera( Camera camera )
    {
        Logger.log( getClass(), "Setting the Camera" );
        this.camera = camera;
    }

    public int getSamplerSlot( String samplerName )
    {
        return samplerMap.get( samplerName );
    }

    public double displayRenderTime( double dividend ) { return profiler.displayAndReset( "Render time", dividend ); }
}
