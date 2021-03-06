package peacefulotter.engine.rendering;

import org.lwjgl.opengl.GL;
import peacefulotter.engine.components.Camera;
import peacefulotter.engine.components.GameObject;
import peacefulotter.engine.components.World;
import peacefulotter.engine.components.lights.BaseLight;
import peacefulotter.engine.components.renderer.GUIRenderer;
import peacefulotter.engine.components.renderer.SkyBoxRenderer;
import peacefulotter.engine.components.renderer.TerrainRenderer;
import peacefulotter.engine.components.renderer.WaterRenderer;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.GUI.GUIMaterial;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.ShaderTypes;
import peacefulotter.engine.rendering.terrain.Terrain;
import peacefulotter.engine.rendering.terrain.WaterTile;
import peacefulotter.engine.utils.Logger;
import peacefulotter.engine.utils.MappedValues;
import peacefulotter.engine.utils.ProfileTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class RenderingEngine extends MappedValues
{
    // NIGHT private static final Vector3f skyColor = new Vector3f( 0.1f, 0.13f, 0.12f );
    private static final Vector3f skyColor = new Vector3f( 0.5444f, 0.62f, 0.69f );

    private final ProfileTimer profiler;
    private final Map<String, Integer>  samplerMap;
    private final List<BaseLight> lights;
    private final Window window;
    private final Shader ambient;

    private final TerrainRenderer tr;
    private final WaterRenderer wr;
    private final GUIRenderer gr;
    private final SkyBoxRenderer sbr;

    private Camera camera;


    public RenderingEngine()
    {
        this.profiler = new ProfileTimer();
        this.lights = new ArrayList<>();
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

        GL.createCapabilities();
        glClearColor( 0, 0, 0, 1f );
        glFrontFace( GL_CW );
        enableCulling();
        glEnable( GL_DEPTH_TEST );
        //glEnable( GL_DEPTH_CLAMP );
        glEnable( GL_TEXTURE_2D );

        // NIGHT addVector3f( "ambient", new Vector3f( 0.00001f, 0.00001f, 0.00001f ) );
        addVector3f( "ambient", new Vector3f( 0.1f, 0.1f, 0.1f ) );
        addVector3f( "skyColor", skyColor.mul( 0.25f ) );
        addVector3f( "fogColor", skyColor );
        ambient = ShaderTypes.AMBIENT.getShader();

        /* TERRAIN */
        this.tr = new TerrainRenderer();
        /* WATER */
        this.wr = new WaterRenderer();
        wr.addWaterTile( new WaterTile( Terrain.SIZE, Terrain.SIZE ) );
        /* SKYBOX */
        this.sbr = new SkyBoxRenderer();
        /* GUI */
        this.gr = new GUIRenderer();
        GUIMaterial material = new GUIMaterial( "crosshair_hit.png", new Vector2f( 0.5f, 0.5f ), new Vector2f( 5f, 5f ) );
        gr.addGUIMaterial( material );

        // Window.bindAsRenderTarget(); Use for Render to Texture (not finished)
    }

    public static void enableCulling()
    {
        glEnable( GL_CULL_FACE );
        glCullFace( GL_BACK );
    }

    public static void disableCulling()
    {
        glDisable( GL_CULL_FACE );
    }


    public void render( GameObject object )
    {
        if ( camera == null )
            throw new NullPointerException( "Main camera not found." );

        profiler.startInvocation();

        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

        object.renderAll( ambient, this );
        sbr.render( ambient, this );
        gr.render( ambient, this );

        glEnable( GL_BLEND );
        glBlendFunc( GL_ONE, GL_ONE );
        glDepthMask( false );
        glDepthFunc( GL_EQUAL );

        for ( BaseLight light : lights )
        {
            BaseLight.setActiveLight( light );
            object.renderAll( light.getShader(), this );
        }

        // Render them with their personal shader
        tr.renderTerrain( this );
        wr.renderWater( this );

        sbr.renderSkyBox( this );
        gr.renderGUI( this );

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

    public void setWorld( World world )
    {
        this.tr.addComponent( world.getTerrain() );
    }
    public void setRoot( GameObject object )
    {
        object
                .addComponent( tr )
                .addComponent( wr );
    }

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
