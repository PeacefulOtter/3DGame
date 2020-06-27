package peacefulotter.game.Utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import peacefulotter.game.Display.Graphics.Vertex;
import peacefulotter.game.Maths.Matrix4f;
import peacefulotter.game.Maths.Vector3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
        glEnable( GL_TEXTURE_2D );
        glEnable( GL_FRAMEBUFFER_SRGB );
    }

    /*
            FLOAT BUFFER
     */
    public static FloatBuffer createFloatBuffer( int size )
    {
        return BufferUtils.createFloatBuffer( size );
    }

    public static FloatBuffer createFlippedBuffer( Vertex[] vertices )
    {
        int verticesLength = vertices.length;
        FloatBuffer buffer = createFloatBuffer( verticesLength * Vertex.SIZE );

        for ( int i = 0; i < verticesLength; i++ )
        {
            Vertex v = vertices[ i ];
            Vector3f pos = v.getPos();
            Vector3f normal = v.getNormal();

            buffer.put( pos.getX() );
            buffer.put( pos.getY() );
            buffer.put( pos.getZ() );
            buffer.put( v.getTextureCoordinates().getX() );
            buffer.put( v.getTextureCoordinates().getY() );
            buffer.put( normal.getX() );
            buffer.put( normal.getY() );
            buffer.put( normal.getZ() );
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


    /*
                INT BUFFER
     */
    public static IntBuffer createIntBuffer( int size )
    {
        return BufferUtils.createIntBuffer( size );
    }

    public static IntBuffer createFlippedBuffer( int... indices )
    {
        IntBuffer buffer = createIntBuffer( indices.length );
        buffer.put( indices );
        buffer.flip();
        return buffer;
    }


    public static void setTextures( boolean enable )
    {
        if ( enable ) { glEnable( GL_TEXTURE_2D ); }
        else { glDisable( GL_TEXTURE_2D ); }
    }

    public static void unbindTextures()
    {
        glBindTexture( GL_TEXTURE_2D, 0 );
    }

    public static void setClearColor( Vector3f color )
    {
        glClearColor( color.getX(), color.getY(), color.getZ(), 1 );
    }
}
