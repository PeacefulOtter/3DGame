package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.BufferUtil;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh
{
    private int vbo; // VertexBufferObject - array of vertices on the graphics card
    private int ibo; // IndexBufferObject - array of indices on the graphics card
    private int size;

    public Mesh( Vertex[] vertices, int[] indices )
    {
        this( vertices, indices, false );
    }

    public Mesh( Vertex[] vertices, int[] indices, boolean calcNormals )
    {
        vbo = glGenBuffers();
        ibo = glGenBuffers();
        size = 0;
        addVertices( vertices, indices, calcNormals );
    }

    private void addVertices( Vertex[] vertices, int[] indices, boolean calcNormals )
    {
        if ( calcNormals )
            calcNormals( vertices, indices );

        size = indices.length;

        glBindBuffer( GL_ARRAY_BUFFER, vbo );
        glBufferData( GL_ARRAY_BUFFER, BufferUtil.createFlippedBuffer( vertices ), GL_STATIC_DRAW );

        glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, ibo );
        glBufferData( GL_ELEMENT_ARRAY_BUFFER, BufferUtil.createFlippedBuffer( indices ), GL_STATIC_DRAW );
    }

    public void draw()
    {
        glEnableVertexAttribArray( 0 );
        glEnableVertexAttribArray( 1 );
        glEnableVertexAttribArray( 2 );

        glBindBuffer( GL_ARRAY_BUFFER, vbo );
        glVertexAttribPointer( 0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0 );
        glVertexAttribPointer( 1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12 ); // 12 = FLOAT_BYTES (4) * NB_FLOAT_BEFORE (3)
        glVertexAttribPointer( 2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20 );

        glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, ibo );
        glDrawElements( GL_TRIANGLES, size, GL_UNSIGNED_INT, 0 );

        glDisableVertexAttribArray( 0 );
        glDisableVertexAttribArray( 1 );
        glDisableVertexAttribArray( 2 );
    }

    private void calcNormals( Vertex[] vertices, int[] indices )
    {
        int verticesLength = vertices.length;
        int indicesLength = indices.length;

        for ( int i = 0; i < indicesLength; i += 3 )
        {
            int i0 = indices[ i ];
            int i1 = indices[ i + 1 ];
            int i2 = indices[ i + 2 ];

            Vertex vertex0 = vertices[ i0 ];
            Vertex vertex1 = vertices[ i1 ];
            Vertex vertex2 = vertices[ i2 ];

            Vector3f v0 = vertex0.getPos();
            Vector3f v1 = vertex1.getPos().sub( v0 );
            Vector3f v2 = vertex2.getPos().sub( v0 );

            Vector3f normal = v1.cross( v2 ).normalize();

            vertex0.setNormal( vertex0.getNormal().add( normal ) );
            vertex1.setNormal( vertex1.getNormal().add( normal ) );
            vertex2.setNormal( vertex2.getNormal().add( normal ) );
        }

        for ( int i = 0; i < verticesLength; i++ )
        {
            vertices[ i ].setNormal( vertices[ i ].getNormal().normalize() );
        }
    }
}
