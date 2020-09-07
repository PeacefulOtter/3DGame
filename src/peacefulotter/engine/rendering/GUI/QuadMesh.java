package peacefulotter.engine.rendering.GUI;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class QuadMesh
{
    private static final float[] POSITIONS = new float[] { -1, 1, -1, -1, 1, 1, 1, -1 };
    private final int vaoId;

    public QuadMesh()
    {
        // Create a new Vertex Array Object in memory and select it (bind)
        // A VAO can have up to 16 attributes (VBO's) assigned to it by default
        vaoId = glGenVertexArrays();
        glBindVertexArray( vaoId );

        // Create a new Vertex Buffer Object in memory and select it (bind)
        // A VBO is a collection of Vectors which in this case resemble the location of each vertex.
        int vboId = glGenBuffers();
        glBindBuffer( GL_ARRAY_BUFFER, vboId );
        FloatBuffer verticesBuffer = createSimpleFlippedBuffer();
        glBufferData( GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW );
        // Put the VBO in the attributes list at index 0
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0 );
        // Deselect (bind to 0) the VBO
        glBindBuffer( GL_ARRAY_BUFFER, 0 );

        // Deselect (bind to 0) the VAO
        glBindVertexArray( 0 );
    }

    private FloatBuffer createSimpleFlippedBuffer()
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer( POSITIONS.length );
        buffer.put( POSITIONS );
        buffer.flip();
        return buffer;
    }


    public int getVaoId() { return vaoId; }
}
