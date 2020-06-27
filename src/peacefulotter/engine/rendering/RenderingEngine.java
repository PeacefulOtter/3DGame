package peacefulotter.engine.rendering;

import org.lwjgl.opengl.GL;
import peacefulotter.engine.core.Maths.Vector3f;
import peacefulotter.engine.core.elementary.GameObject;
import peacefulotter.engine.rendering.Shaders.BasicShader;
import peacefulotter.engine.rendering.Shaders.Shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine
{
    private Camera camera;

    public RenderingEngine( float aspectRatio )
    {
        GL.createCapabilities();
        glClearColor( 0, 0, 0, 0 );
        glFrontFace( GL_CW );
        glCullFace( GL_BACK );
        glEnable( GL_CULL_FACE );
        glEnable( GL_DEPTH_TEST );
        glEnable( GL_DEPTH_CLAMP );
        glEnable( GL_TEXTURE_2D );

        camera = new Camera(  (float) Math.toRadians( 70.0f ), aspectRatio, 0.1f, 1000f );
    }

    public void render( GameObject object )
    {
        clearScreen();
        Shader shader = BasicShader.getInstance();
        shader.setRenderingEngine( this );
        object.render( shader );
    }

    public void initCamera() { camera.init(); }

    public void updateCamera() { camera.update(); }

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

    public Camera getCamera() { return camera; }
    public void setCamera( Camera camera ) { this.camera = camera; }
}
