package peacefulotter.engine.utils;

import org.lwjgl.BufferUtils;
import peacefulotter.engine.rendering.graphics.Vertex;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector3f;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtil
{
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
            Vector3f tangent = v.getTangent();

            buffer.put( pos.getX() );
            buffer.put( pos.getY() );
            buffer.put( pos.getZ() );
            buffer.put( v.getTextureCoordinates().getX() );
            buffer.put( v.getTextureCoordinates().getY() );
            buffer.put( normal.getX() );
            buffer.put( normal.getY() );
            buffer.put( normal.getZ() );
            buffer.put( tangent.getX() );
            buffer.put( tangent.getY() );
            buffer.put( tangent.getZ() );
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


    public static FloatBuffer createSimpleFlippedBuffer( float[] positions )
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer( positions.length );
        buffer.put( positions );
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



    /*
                BYTE BUFFER
     */
    public static ByteBuffer createByteBuffer( int size ) { return BufferUtils.createByteBuffer( size ); }
}
