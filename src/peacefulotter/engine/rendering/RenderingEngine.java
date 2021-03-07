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
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine extends MappedValues
{
    private static final Vector3f SKY_COLOR = new Vector3f( 0.02f, 0.08f, 0.15f );
    // private static final Vector3f SKY_COLOR = new Vector3f( 0.5444f, 0.62f, 0.69f );
    private static final Vector3f AMBIENT_COLOR = new Vector3f( 0.003f, 0.015f, 0.030f );

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
        samplerMap.put( "aNormalMap", 7 );
        samplerMap.put( "rNormalMap", 8 );
        samplerMap.put( "gNormalMap", 9 );
        samplerMap.put( "bNormalMap", 10 );
        samplerMap.put( "aDispMap", 11 );
        samplerMap.put( "rDispMap", 12 );
        samplerMap.put( "gDispMap", 13 );
        samplerMap.put( "bDispMap", 14 );
        samplerMap.put( "blendMap", 15 );
        samplerMap.put( "guiTexture", 16 );

        GL.createCapabilities();
        glClearColor( 0, 0, 0, 1f );
        glFrontFace( GL_CW );
        enableCulling();
        glEnable( GL_DEPTH_TEST );
        glEnable( GL_DEPTH_CLAMP );
        glEnable( GL_TEXTURE_2D );

        addVector3f( "ambient", AMBIENT_COLOR );
        addVector3f( "skyColor", SKY_COLOR.mul( 0.25f ) );
        addVector3f( "fogColor", SKY_COLOR );
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
        GUIMaterial material1 = new GUIMaterial( "rainbow.png", new Vector2f( 0.5f, 0.5f ), new Vector2f( 0.25f, 0.25f ) );
        GUIMaterial material2 = new GUIMaterial( "crosshair.png", new Vector2f( -0.5f, -0.5f ), new Vector2f( 0.5f, 0.5f ) );
        gr.addGUIMaterials( material1, material2 );

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
        // gr.renderGUI( this );

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
        object.addComponent( tr ).addComponent( wr );
    }

    public long getCurrentWindow() { return window.getWindow(); }

    public Camera getCamera() { return camera; }
    public void setCamera( Camera camera )
    {
        Logger.log( getClass(), "Setting the Camera" );
        this.camera = camera;
        gr.setCamera( camera );
    }

    public int getSamplerSlot( String samplerName )
    {
        return samplerMap.get( samplerName );
    }

    public double displayRenderTime( double dividend ) { return profiler.displayAndReset( "Render time", dividend ); }
}
