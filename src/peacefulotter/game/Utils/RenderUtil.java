package peacefulotter.game.Utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import peacefulotter.game.Display.Vertex;
import peacefulotter.game.Maths.Matrix4f;
import peacefulotter.game.Maths.Vector3f;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class RenderUtil
{
    public static void clearScreen()
    {
        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
    }

    public static void initGraphics()
    {
        GL.createCapabilities();
        glClearColor( 0, 0, 0, 0 );
        glFrontFace( GL_CW );
        glCullFace( GL_BACK );
        glEnable( GL_CULL_FACE );
        glEnable( GL_DEPTH_TEST );

        glEnable( GL_FRAMEBUFFER_SRGB );
    }

    public static FloatBuffer createFloatBuffer( int size )
    {
        return BufferUtils.createFloatBuffer( size );
    }

    public static FloatBuffer createFlippedBuffer( Vertex[] vertices )
    {
        FloatBuffer buffer = createFloatBuffer( vertices.length * Vertex.SIZE );
        for ( int i = 0; i < vertices.length; i++ )
        {
            Vector3f pos = vertices[ i ].getPos();
            buffer.put( pos.getX() );
            buffer.put( pos.getY() );
            buffer.put( pos.getZ() );
        }
        buffer.flip();
        return buffer;
    }

    public static FloatBuffer createFlippedBuffer( Matrix4f matrix )
    {
        FloatBuffer buffer = createFloatBuffer( 4 * 4 );
        for ( int i = 0; i < 4; i++ )
        {
            for ( int j = 0; j < 4; j++ )
            {
                buffer.put( matrix.getAt( i, j ) );
            }
        }
        buffer.flip();
        return buffer;
    }
}
