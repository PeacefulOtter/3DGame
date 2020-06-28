package peacefulotter.engine.rendering;

import org.lwjgl.opengl.GL;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.core.GameObject;
import peacefulotter.engine.rendering.shaders.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

/*

    Move camera inside Game class ?
 */


public class RenderingEngine
{
    private final Window window;
    private Camera camera;
    private Vector3f ambientLight;
    private DirectionalLight directionalLight;
    private PointLight pointLight;
    private SpotLight spotLight;

    public RenderingEngine( String winName, int winWidth, int winHeight )
    {
        window = new Window( winName, winWidth, winHeight );
        camera = new Camera(
                (float) Math.toRadians( 70.0f ),
                (float) window.getWidth() / (float) window.getHeight(),
                0.01f, 1000f );

        GL.createCapabilities();
        glClearColor( 0, 0, 0, 0 );
        glFrontFace( GL_CW );
        glCullFace( GL_BACK );
        glEnable( GL_CULL_FACE );
        glEnable( GL_DEPTH_TEST );
        glEnable( GL_DEPTH_CLAMP );
        glEnable( GL_TEXTURE_2D );

        ambientLight = new Vector3f( 0.2f, 0.2f, 0.2f );
        directionalLight = new DirectionalLight(
                new BaseLight( new Vector3f( 0.6f, 0.3f,  0.6f ), 0.5f ),
                new Vector3f( 1, 1, 1 ) );
        pointLight = new PointLight(
                new BaseLight( new Vector3f( 0.4f, 1,  0.4f ), 0.4f  ),
                new Attenuation( 1, 0, 0 ),
                new Vector3f( 5, 0, 5 ),
                20 );
        spotLight = new SpotLight(
                new PointLight(
                        new BaseLight( new Vector3f( 1f, 0,  0f ), 1f  ),
                        new Attenuation( 0, 0.05f, 0 ),
                        new Vector3f( 5, 0, 5 ),
                        20 ),
                new Vector3f( 1, 0, 0 ),
                0.7f
        );
    }

    public Vector3f getAmbientLight() { return ambientLight; }

    public DirectionalLight getDirectionalLight() { return directionalLight; }

    public PointLight getPointLight() { return pointLight; }

    public SpotLight getSpotLight() { return spotLight; }

    public void render( GameObject object )
    {
        clearScreen();

        Shader forwardAmbient = ForwardAmbient.getInstance();
        forwardAmbient.setRenderingEngine( this );

        Shader forwardDirectional = ForwardDirectional.getInstance();
        forwardDirectional.setRenderingEngine( this );

        Shader forwardPoint = ForwardPoint.getInstance();
        forwardPoint.setRenderingEngine( this );

        Shader forwardSpot = ForwardSpot.getInstance();
        forwardSpot.setRenderingEngine( this );

        object.render( forwardAmbient );

        glEnable( GL_BLEND );
        glBlendFunc( GL_ONE, GL_ONE );
        glDepthMask( false );
        glDepthFunc( GL_EQUAL );

        object.render( forwardDirectional );
        object.render( forwardPoint );
        object.render( forwardSpot );

        glDepthFunc( GL_LESS );
        glDepthMask( true );
        glDisable( GL_BLEND );

        // Shader shader = BasicShader.getInstance();
        // shader.setRenderingEngine( this );

        // object.render( shader );
    }

    public void initCamera() { camera.init(); }

    public void updateCamera( float deltaTime ) { camera.update(  deltaTime  ); }

    private static void clearScreen()
    {
        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
    }

    private static void setTextures( boolean enable )
    {
        if ( enable ) { glEnable( GL_TEXTURE_2D ); }
        else { glDisable( GL_TEXTURE_2D ); }
    }

    private static void unbindTextures()
    {
        glBindTexture( GL_TEXTURE_2D, 0 );
    }

    private static void setClearColor( Vector3f color )
    {
        glClearColor( color.getX(), color.getY(), color.getZ(), 1 );
    }

    public long getCurrentWindow() { return window.getWindow(); }

    public Camera getCamera() { return camera; }
    public void setCamera( Camera camera ) { this.camera = camera; }
}
