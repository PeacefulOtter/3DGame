package peacefulotter.game.Utils;

import peacefulotter.game.Display.Graphics.Mesh;
import peacefulotter.game.Display.Graphics.Vertex;
import peacefulotter.game.Maths.Vector3f;

import java.io.*;
import java.util.*;

public class ResourceLoader
{
    private static final String SHADER_PATH = "/shaders/";
    private static final String MODELS_PATH = "/models/";

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

    private List<Integer> indicesAddAt( List<Integer> indices, int index, String[] split )
    {
        indices.add( Integer.parseInt( split[ index ].split( "/" )[ 0 ] ) - 1 );
        return indices;
    }

    public Mesh loadMesh( String fileName )
    {
        String[] splitArray = fileName.split( "\\." );
        String extension = splitArray[ splitArray.length - 1 ];

        if ( !extension.equals( "obj" ) )
        {
            System.err.println( "File format not supported" );
            new Exception().printStackTrace();
            System.exit( 1 );
        }

        List<Vertex> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( resourceStream( MODELS_PATH + fileName ) ) ) )
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

        Mesh mesh = new Mesh();
        mesh.addVertices(
                Arrays.copyOf( vertices.toArray(), vertices.size(), Vertex[].class ),
                Util.toIntArray( indices ) );
        return mesh;
    }
}
