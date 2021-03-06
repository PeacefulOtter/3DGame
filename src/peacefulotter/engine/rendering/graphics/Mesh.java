package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.graphics.meshes.IndexedModel;
import peacefulotter.engine.rendering.graphics.meshes.OBJModel;
import peacefulotter.engine.utils.BufferUtil;
import peacefulotter.engine.rendering.resourceManagement.MeshResource;
import peacefulotter.engine.utils.Logger;
import peacefulotter.engine.utils.ResourceLoader;
import peacefulotter.engine.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh
{
    public static final String MODELS_PATH = "/models/";

    private static final Map<String, MeshResource> loadedModels = new HashMap<>();

    private MeshResource resource;

    public Mesh( String fileName ) { this( "", fileName ); }

    public Mesh( String subfolder, String fileName )
    {
        String path = subfolder + fileName;
        MeshResource res = checkLoadedModels( path );
        if ( res == null )
        {
            Vertices v = loadMesh( subfolder, fileName );
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

    /**
     * Creates an OBJModel from and OBJ file and load its vertices
     * @param subFolder
     * @param fileName
     * @return
     */
    private Mesh.Vertices loadMesh( String subFolder, String fileName )
    {
        Logger.log( getClass(), "Loading mesh at : " + subFolder + fileName );
        String[] splitArray = fileName.split( "\\." );
        String extension = splitArray[ splitArray.length - 1 ];

        if ( !extension.equals( "obj" ) )
        {
            System.err.println( "File format not supported" );
            new Exception().printStackTrace();
            System.exit( 1 );
        }

        OBJModel model = new OBJModel();

        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( new ResourceLoader().resourceStream( MODELS_PATH + subFolder + fileName ) ) ) )
        {
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                String[] split = line.split( " " );
                split = Utils.removeEmptyStrings( split );
                if ( split.length == 0 ) continue;
                String prefix = split[ 0 ];

                if ( prefix.equals( "v" ) )
                {
                    model.addPosition( new Vector3f(
                            Float.parseFloat( split[ 1 ] ),
                            Float.parseFloat( split[ 2 ] ),
                            Float.parseFloat( split[ 3 ] ) ) );
                }
                else if ( prefix.equals( "vt" ) )
                {
                    model.addTexCoord( new Vector2f(
                            Float.parseFloat( split[ 1 ] ),
                            1.0f - Float.parseFloat( split[ 2 ] ) ) );
                }
                else if ( prefix.equals( "vn" ) )
                {
                    model.addNormal( new Vector3f(
                            Float.parseFloat( split[ 1 ] ),
                            Float.parseFloat( split[ 2 ] ),
                            Float.parseFloat( split[ 3 ] ) ) );
                }
                else if ( split[ 0 ].equals( "f" ) )
                {
                    for ( int i = 0; i < split.length - 3; i++ )
                    {
                        model.addIndices( split[ 1 ] );
                        model.addIndices( split[ 2 + i ] );
                        model.addIndices( split[ 3 + i ] );
                    }
                }
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            System.exit( 1 );
        }

        IndexedModel indexedModel = model.toIndexedModel();

        return ResourceLoader.loadVertices( indexedModel );
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
