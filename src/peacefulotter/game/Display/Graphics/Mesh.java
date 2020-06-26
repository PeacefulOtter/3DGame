package peacefulotter.game.Display.Graphics;

import peacefulotter.game.Utils.RenderUtil;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh
{
    private int vbo; // VertexBufferObject - array of vertices on the graphics card
    private int ibo; // IndexBufferObject - array of indices on the graphics card
    private int size;

    public Mesh()
    {
        vbo = glGenBuffers();
        ibo = glGenBuffers();
        size = 0;
    }

    public void addVertices(Vertex[] vertices, int[] indices )
    {
        size = indices.length;

        glBindBuffer( GL_ARRAY_BUFFER, vbo );
        glBufferData( GL_ARRAY_BUFFER, RenderUtil.createFlippedBuffer( vertices ), GL_STATIC_DRAW );

        glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, ibo );
        glBufferData( GL_ELEMENT_ARRAY_BUFFER, RenderUtil.createFlippedBuffer( indices ), GL_STATIC_DRAW );
    }

    public void draw()
    {
        glEnableVertexAttribArray( 0 );

        glBindBuffer( GL_ARRAY_BUFFER, vbo );
        glVertexAttribPointer( 0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0 );

        glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, ibo );
        glDrawElements( GL_TRIANGLES, size, GL_UNSIGNED_INT, 0 );

        glDisableVertexAttribArray( 0 );
    }
}