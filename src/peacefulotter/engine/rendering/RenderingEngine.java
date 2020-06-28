package peacefulotter.engine.rendering;

import org.lwjgl.opengl.GL;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.core.GameObject;
import peacefulotter.engine.rendering.shaders.BasicShader;
import peacefulotter.engine.rendering.shaders.Shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

/*

    Move camera inside Game class ?
 */


public class RenderingEngine
{
    private Window window;
    private Camera camera;

    public RenderingEngine( String winName, int winWidth, int winHeight )
    {
        window = new Window( winName, winWidth, winHeight );
        camera = new Camera( (float) Math.toRadians( 70.0f ), (float) ( window.getWidth() / window.getHeight() ), 0.01f, 1000f );

        GL.createCapabilities();
        glClearColor( 0, 0, 0, 0 );
        glFrontFace( GL_CW );
        glCullFace( GL_BACK );
        glEnable( GL_CULL_FACE );
        glEnable( GL_DEPTH_TEST );
        glEnable( GL_DEPTH_CLAMP );
        glEnable( GL_TEXTURE_2D );
    }

    public void render( GameObject object )
    {
        clearScreen();

        Shader shader = BasicShader.getInstance();
        shader.setRenderingEngine( this );

        object.render( shader );
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
