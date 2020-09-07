package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.rendering.BufferUtil;
import peacefulotter.engine.rendering.resourceManagement.MeshResource;
import peacefulotter.engine.utils.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh
{
    private static final Map<String, MeshResource> loadedModels = new HashMap<>();

    private MeshResource resource;

    public Mesh( String fileName ) { this( "", fileName ); }

    public Mesh( String subfolder, String fileName )
    {
        String path = subfolder + fileName;
        MeshResource res = checkLoadedModels( path );
        if ( res == null )
        {
            Vertices v = new ResourceLoader().loadMesh( subfolder, fileName );
            addVertices( v.vertices, v.indices );
            loadedModels.put( path, resource );
        }
        else
            resource = res;
    }

    public Mesh( String identifier, Vertices vertices )
    {
        this( identifier, vertices.getVertices(), vertices.getIndices() );
    }

    public Mesh( String identifier, Vertex[] vertices, int[] indices )
    {
        MeshResource res = checkLoadedModels( identifier );
        if ( res == null )
        {
            addVertices( vertices, indices );
            loadedModels.put( identifier, resource );
        }
        else
            resource = res;
    }

    public MeshResource checkLoadedModels( String id )
    {
        return loadedModels.get( id );
    }


    private void addVertices( Vertex[] vertices, int[] indices )
    {
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
        glEnableVertexAttribArray( 3 );

        glBindBuffer( GL_ARRAY_BUFFER,  resource.getVbo() );
        glVertexAttribPointer( 0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0 );
        glVertexAttribPointer( 1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12 ); // 12 = FLOAT_BYTES (4) * NB_FLOAT_BEFORE (3)
        glVertexAttribPointer( 2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20 );
        glVertexAttribPointer( 3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32 );

        glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, resource.getIbo() );
        glDrawElements( GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0 );

        glDisableVertexAttribArray( 0 );
        glDisableVertexAttribArray( 1 );
        glDisableVertexAttribArray( 2 );
        glDisableVertexAttribArray( 3 );
    }

    public MeshResource getResource() { return resource; }

    public static class Vertices
    {
        private final Vertex[] vertices;
        private final int[] indices;

        public Vertices( Vertex[] vertices, int[] indices )
        {
            this.vertices = vertices;
            this.indices = indices;
        }

        public Vertex[] getVertices() { return vertices; }
        public int[] getIndices() { return indices; }
    }
}
