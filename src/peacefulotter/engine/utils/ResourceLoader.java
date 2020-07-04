package peacefulotter.engine.utils;

import peacefulotter.engine.rendering.graphics.Mesh;
import peacefulotter.engine.rendering.graphics.Vertex;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.graphics.meshes.IndexedModel;
import peacefulotter.engine.rendering.graphics.meshes.OBJModel;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class ResourceLoader
{
    private static final String SHADER_PATH = "/shaders/";
    private static final String MODELS_PATH = "/models/";
    private static final String TEXTURES_PATH = "/textures/";

    private InputStream resourceStream( String resourceName )
    {
        return getClass().getResourceAsStream( resourceName );
    }

    public String loadShader( String fileName )
    {
        StringJoiner sj = new StringJoiner( "\n" );

        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( resourceStream( SHADER_PATH + fileName ) ) ) )
        {
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                sj.add( line );
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            System.exit( 1 );
        }
        return sj.toString();
    }

    public static void indicesAddAt( List<Integer> indices, int index, String[] split )
    {
        indices.add( Integer.parseInt( split[ index ].split( "/" )[ 0 ] ) - 1 );
    }

    public Mesh loadMesh( String fileName, boolean calcNormals )
    {
        String[] splitArray = fileName.split( "\\." );
        String extension = splitArray[ splitArray.length - 1 ];

        if ( !extension.equals( "obj" ) )
        {
            System.err.println( "File format not supported" );
            new Exception().printStackTrace();
            System.exit( 1 );
        }

        OBJModel objModel = new OBJModel( MODELS_PATH + fileName );
        IndexedModel indexedModel = objModel.toIndexedModel();
        indexedModel.calcNormals();
        int modelSize = indexedModel.getPositions().size();
        
        List<Vertex> vertices = new ArrayList<>();
        for ( int i = 0; i < modelSize; i++ )
        {
            vertices.add( new Vertex(
                    indexedModel.getPositions().get( i ),
                    indexedModel.getTexCoords().get( i ),
                    indexedModel.getNormals().get( i ) ) );
        }

        return new Mesh(
                Arrays.copyOf( vertices.toArray(), vertices.size(), Vertex[].class ),
                Util.toIntArray( indexedModel.getIndices() ),
                calcNormals
        );


        /*try ( BufferedReader reader = new BufferedReader( new InputStreamReader( resourceStream( MODELS_PATH + fileName ) ) ) )
        {
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                String[] split = line.split( " " );
                split = Util.removeEmptyStrings( split );

                if ( split[ 0 ].equals( "v" ) )
                {
                    vertices.add( new Vertex( new Vector3f(
                            Float.parseFloat( split[ 1 ] ),
                            Float.parseFloat( split[ 2 ] ),
                            Float.parseFloat( split[ 3 ] ) ) ) );
                }
                else if ( split[ 0 ].equals( "f" ) )
                {
                    for ( int i = 1; i <= 3; i++ )
                    {
                        indicesAddAt( indices, i, split );
                    }
                    if ( split.length > 4 )
                    {
                        indicesAddAt( indices, 1, split );
                        indicesAddAt( indices, 3, split );
                        indicesAddAt( indices, 4, split );
                    }
                }
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            System.exit( 1 );
        }

        return new Mesh(
                Arrays.copyOf( vertices.toArray(), vertices.size(), Vertex[].class ),
                Util.toIntArray( indices ),
                calcNormals
        );*/
    }

    public int loadTexture( String fileName )
    {
        int id = 0;

        try ( InputStream in = resourceStream( TEXTURES_PATH + fileName ) ) {
            PNGDecoder decoder = new PNGDecoder( in );
            // assuming RGB here but should allow for RGB and RGBA (changing wall.png to RGBA will crash this!)
            ByteBuffer buffer = ByteBuffer.allocateDirect( 3 * decoder.getWidth() * decoder.getHeight() );
            decoder.decode( buffer, decoder.getWidth() * 3, PNGDecoder.Format.RGB );
            buffer.flip();

            id = glGenTextures();

            glBindTexture( GL_TEXTURE_2D, id );
            glTexImage2D( GL_TEXTURE_2D, 0, GL_RGB, decoder.getWidth(), decoder.getHeight(), 0,
                    GL_RGB, GL_UNSIGNED_BYTE, buffer );
            glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST );
            glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST );
        }
        catch( IOException e )
        {
            e.printStackTrace();
            System.exit( 1 );
        }

        return id;
    }
}
