package peacefulotter.engine.utils;

import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.BufferUtil;
import peacefulotter.engine.rendering.graphics.Mesh;
import peacefulotter.engine.rendering.graphics.SimpleMaterial;
import peacefulotter.engine.rendering.graphics.Texture;
import peacefulotter.engine.rendering.graphics.Vertex;
import peacefulotter.engine.rendering.graphics.meshes.IndexedModel;
import peacefulotter.engine.rendering.graphics.meshes.OBJModel;
import peacefulotter.engine.rendering.resourceManagement.TextureResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class ResourceLoader
{
    private static final String SHADER_PATH = "/shaders/";
    private static final String MODELS_PATH = "/models/";
    private static final String TEXTURES_PATH = "/textures/";

    private static final String INCLUDE_DIRECTIVE = "#include";
    private static final int INCLUDE_DIRECTIVE_LENGTH = INCLUDE_DIRECTIVE.length();

    private InputStream resourceStream( String resourceName )
    {
        return getClass().getResourceAsStream( resourceName );
    }

    public String loadShader( String fileName )
    {
        System.out.println("Loading shader : " + fileName );
        StringJoiner sj = new StringJoiner( "\n" );

        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( resourceStream( SHADER_PATH + fileName ) ) ) )
        {
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                if ( line.startsWith( INCLUDE_DIRECTIVE ) )
                    sj.add( loadShader( line.substring( INCLUDE_DIRECTIVE_LENGTH + 2, line.length() - 2 ) ) );
                else
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


    public Mesh.Vertices loadMesh( String fileName )
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

        return loadVertices( indexedModel );
    }

    public static Mesh.Vertices loadVertices( IndexedModel indexedModel )
    {
        List<Vector3f> positions = indexedModel.getPositions();
        List<Vector2f> texCoords = indexedModel.getTexCoords();
        List<Vector3f> normals = indexedModel.getNormals();
        List<Vector3f> tangents = indexedModel.getTangents();
        List<Integer>  indices = indexedModel.getIndices();

        int modelSize = indexedModel.getPositions().size();
        Vertex[] vertices = new Vertex[ modelSize ];
        for ( int i = 0; i < modelSize; i++ )
        {
            vertices[ i ] = new Vertex(
                    positions.get( i ),
                    texCoords.get( i ),
                    normals.get( i ),
                    tangents.get( i ) );
        }

        return new Mesh.Vertices( vertices, Util.toIntArray( indices ) );
    }


    public TextureResource loadTexture( String fileName )
    {
        TextureResource resource = null;

        try
        {
            BufferedImage image = ImageIO.read( resourceStream( TEXTURES_PATH + fileName ) );

            int imageWidth = image.getWidth(); int imageHeight = image.getHeight();
            int[] pixels = image.getRGB( 0, 0, imageWidth, imageHeight, null, 0, imageWidth );

            ByteBuffer buffer = BufferUtil.createByteBuffer( imageWidth * imageHeight * 4 );

            for ( int y = 0; y < imageHeight; y++ )
            {
                for ( int x = 0; x < imageWidth; x++ )
                {
                    int pixel = pixels[ y * imageWidth + x ];
                    buffer.put( (byte) ( ( pixel >> 16 ) & 0xff ) );
                    buffer.put( (byte) ( ( pixel >> 8 )  & 0xff ) );
                    buffer.put( (byte) ( ( pixel )       & 0xff ) );
                    if ( image.getColorModel().hasAlpha() )
                        buffer.put( (byte) ( ( pixel >> 24 ) & 0xff ) );
                    else
                        buffer.put( (byte) 0xff );
                }
            }

            buffer.flip();

            resource = new TextureResource();
            glBindTexture( GL_TEXTURE_2D, resource.getId() );

            glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT );
            glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT );

            glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
            glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );

            glTexImage2D( GL_TEXTURE_2D, 0, GL_RGBA8, imageWidth, imageHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer );
        }
        catch( IOException e )
        {
            e.printStackTrace();
            System.exit( 1 );
        }

        return resource;
    }


    public List<SimpleMaterial> loadMaterial( String fileName )
    {
        List<SimpleMaterial> simpleMaterials = new ArrayList<>();
        int index = -1;

        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( resourceStream( MODELS_PATH + fileName ) ) ) )
        {
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                if ( line.startsWith( "newmtl" ) )
                {
                    simpleMaterials.add( new SimpleMaterial() );
                    index++;
                }
                else if ( line.startsWith( "Ni" ) )
                {
                    simpleMaterials.get( index ).setSpecularIntensity( Float.parseFloat( line.split( " " )[ 1 ] ) );
                }
                else if ( line.startsWith( "Ns" ) )
                {
                    simpleMaterials.get( index ).setSpecularPower( Float.parseFloat( line.split( " " )[ 1 ] ) );
                }
                else if ( line.startsWith( "map_" ) )
                {
                    simpleMaterials.get( index ).setTexture( new Texture( line.split( " " )[ 1 ] ) );
                }
                System.out.println(line);
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            System.exit( 1 );
        }
        System.out.println(simpleMaterials);
        return simpleMaterials;
    }
}
