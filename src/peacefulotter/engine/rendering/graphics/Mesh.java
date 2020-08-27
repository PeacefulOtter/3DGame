package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.BufferUtil;
import peacefulotter.engine.rendering.graphics.meshes.IndexedModel;
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
            Vertices v = new ResourceLoader().loadMesh( fileName );
            addVertices( v.vertices, v.indices );
            loadedModels.put( fileName, resource );
        }
        else
            resource = res;
    }

    public Mesh( Vertex[] vertices, int[] indices )
    {
        addVertices( vertices, indices );
    }

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
}
