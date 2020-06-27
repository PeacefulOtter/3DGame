package peacefulotter.engine.rendering;

import org.lwjgl.opengl.GL;
import peacefulotter.engine.core.Maths.Vector3f;
import peacefulotter.engine.core.elementary.GameObject;
import peacefulotter.engine.rendering.Shaders.BasicShader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine
{
    public RenderingEngine()
    {
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
        object.render( BasicShader.getInstance() );
    }

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

}
