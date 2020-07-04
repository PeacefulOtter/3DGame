package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.BufferUtil;
import peacefulotter.engine.rendering.resourceManagement.MeshResource;
import peacefulotter.engine.utils.ResourceLoader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh
{
    private static final Map<String, MeshResource> loadedModels = new HashMap<>();

    private MeshResource resource;

    public Mesh( String fileName )
    {
        MeshResource res = loadedModels.get( fileName );
        if ( res == null )
        {
            Object[] o = new ResourceLoader().loadMesh( fileName );
            addVertices( (Vertex[]) o[ 0 ], (int[]) o[ 1 ], (Boolean)o[ 2 ] );
            loadedModels.put( fileName, resource );
        }
        else
            resource = res;
    }

    public Mesh( Vertex[] vertices, int[] indices )
    {
        this( vertices, indices, false );
    }

    public Mesh( Vertex[] vertices, int[] indices, boolean calcNormals )
    {
        addVertices( vertices, indices, calcNormals );
    }

    private void addVertices( Vertex[] vertices, int[] indices, boolean calcNormals )
    {
        if ( calcNormals )
            calcNormals( vertices, indices );

        resource = new MeshResource( indices.length );

        glBindBuffer( GL_ARRAY_BUFFER, resource.getVbo() );
        glBufferData( GL_ARRAY_BUFFER, BufferUtil.createFlippedBuffer( vertices ), GL_STATIC_DRAW );


        glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, resource.getIbo() );
        glBufferData( GL_ELEMENT_ARRAY_BUFFER, BufferUtil.createFlippedBuffer( indices ), GL_STATIC_DRAW );
    }

    public void draw()
    {
        glEnableVertexAttribArray( 0 );
        glEnableVertexAttribArray( 1 );
        glEnableVertexAttribArray( 2 );

        glBindBuffer( GL_ARRAY_BUFFER,  resource.getVbo() );
        glVertexAttribPointer( 0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0 );
        glVertexAttribPointer( 1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12 ); // 12 = FLOAT_BYTES (4) * NB_FLOAT_BEFORE (3)
        glVertexAttribPointer( 2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20 );

        glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, resource.getIbo() );
        glDrawElements( GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0 );

        glDisableVertexAttribArray( 0 );
        glDisableVertexAttribArray( 1 );
        glDisableVertexAttribArray( 2 );
    }

    private void calcNormals( Vertex[] vertices, int[] indices )
    {
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

        for ( Vertex vertex : vertices )
            vertex.setNormal( vertex.getNormal().normalize() );
    }
}
