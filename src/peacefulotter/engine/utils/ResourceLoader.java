package peacefulotter.engine.utils;

import org.lwjgl.opengl.GL11;
import org.lwjglx.Sys;
import peacefulotter.engine.components.renderer.MultiMeshRenderer;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.BufferUtil;
import peacefulotter.engine.rendering.GUI.GUITexture;
import peacefulotter.engine.rendering.graphics.*;
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
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class ResourceLoader
{
    private static final String SHADER_PATH = "/shaders/";
    private static final String MODELS_PATH = "/models/";
    private static final String TEXTURES_PATH = "/textures/";
    private static final String MATERIALS_PATH = "/materials/";

    private static final String INCLUDE_DIRECTIVE = "#include";
    private static final int INCLUDE_DIRECTIVE_LENGTH = INCLUDE_DIRECTIVE.length();

    private InputStream resourceStream( String resourceName )
    {
        return getClass().getResourceAsStream( resourceName );
    }

    public String loadShader( String fileName )
    {
        Logger.log( getClass(), "Loading shader at : " + fileName );
        StringJoiner sj = new StringJoiner( "\n" );

        try ( BufferedReader reader = new BufferedReader(
                new InputStreamReader( resourceStream( SHADER_PATH + fileName ) ) ) )
        {
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                if ( line.startsWith( INCLUDE_DIRECTIVE ) )
                    sj.add( loadShader( line.substring( INCLUDE_DIRECTIVE_LENGTH + 2, line.length() - 2 ) ) );
                else if ( !line.startsWith( "//" ) )
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


    public Mesh.Vertices loadMesh( String subFolder, String fileName )
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

        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( resourceStream( MODELS_PATH + subFolder + fileName ) ) ) )
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

        return loadVertices( indexedModel );
    }

    public MultiTextureMesh loadMultiMesh( String subFolder, String fileName )
    {
        MultiTextureMesh mtm = new MultiTextureMesh();
        String path = subFolder + fileName;
        Logger.log( getClass(), " = Loading MultiMesh at : " + path );
        String[] splitArray = fileName.split( "\\." );
        String extension = splitArray[ splitArray.length - 1 ];

        if ( !extension.equals( "obj" ) )
        {
            System.err.println( "File format not supported" );
            new Exception().printStackTrace();
            System.exit( 1 );
        }

        OBJModel currentModel = new OBJModel();
        Map<String, SimpleMaterial> materialMap = null; // associate material name to the material
        Map<Integer, SimpleMaterial> integerSimpleMaterialMap = new HashMap<>(); // associate material index to the material
        boolean isFirstMaterial = true;

        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( resourceStream( MODELS_PATH + subFolder + fileName ) ) ) )
        {
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                String[] split = line.split( " " );
                split = Utils.removeEmptyStrings( split );
                if ( split.length == 0 ) continue;
                String prefix = split[ 0 ];

                if ( prefix.equals( "mtllib" ) )
                {
                    materialMap = loadMaterial( subFolder, split[ 1 ] );
                }
                else if ( prefix.equals( "usemtl" ) && materialMap != null )
                {
                    String[] splitFileName = split[ 1 ].split( "\\." );
                    String materialFileName = splitFileName[ 0 ];
                    String materialExtension = splitFileName[ 1 ];

                    if ( materialMap.containsKey( materialFileName ) )
                    {
                        Logger.log( getClass(), "   Adding Material to MTM : " + materialFileName + " at " + currentModel.getIndicesSize() );
                        Material mat = new Material( materialMap.get( materialFileName ) );
                        mat.setNormalMap( new Texture( subFolder, materialFileName + "_normal." + materialExtension ) );
                        mat.setDispMap( new Texture( subFolder, materialFileName + "_height." + materialExtension ) );
                        mtm.addMaterial( mat );
                    }
                    if ( isFirstMaterial )
                        isFirstMaterial = false;
                    else
                        addMeshToMTM( currentModel, mtm, path + mtm.getSize() );
                }
                else if ( prefix.equals( "v" ) )
                {
                    currentModel.addPosition( new Vector3f(
                            Float.parseFloat( split[ 1 ] ),
                            Float.parseFloat( split[ 2 ] ),
                            Float.parseFloat( split[ 3 ] ) ) );
                }
                else if ( prefix.equals( "vt" ) )
                {
                    currentModel.addTexCoord( new Vector2f(
                            Float.parseFloat( split[ 1 ] ),
                            1.0f - Float.parseFloat( split[ 2 ] ) ) );
                }
                else if ( prefix.equals( "vn" ) )
                {
                    currentModel.addNormal( new Vector3f(
                            Float.parseFloat( split[ 1 ] ),
                            Float.parseFloat( split[ 2 ] ),
                            Float.parseFloat( split[ 3 ] ) ) );
                }
                else if ( split[ 0 ].equals( "f" ) )
                {
                    for ( int i = 0; i < split.length - 3; i++ )
                    {
                        currentModel.addIndices( split[ 1 ] );
                        currentModel.addIndices( split[ 2 + i ] );
                        currentModel.addIndices( split[ 3 + i ] );
                    }
                }
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            System.exit( 1 );
        }

        addMeshToMTM( currentModel, mtm, path + mtm.getSize() );

        Logger.log( "" );
        return mtm;
    }

    private void addMeshToMTM( OBJModel currentModel, MultiTextureMesh mtm, String path )
    {
        Logger.log( getClass(), "  Adding Mesh to MTM : " + path );
        OBJModel newModel = new OBJModel();
        currentModel.getIndices().forEach( indexObj -> {
            newModel.addPosition( currentModel.getPosition( indexObj.vertexIndex ) );
            newModel.addTexCoord( currentModel.getTexCoord( indexObj.texCoordIndex ) );
            newModel.addNormal( currentModel.getNormal( indexObj.normalIndex ) );
            newModel.addIndices( indexObj );
        } );
        currentModel.getIndices().clear();
        // treat all the faces and add the meshes of all the faces
        IndexedModel indexedModel = newModel.toIndexedModel( true );
        Mesh mesh = new Mesh( path, loadVertices( indexedModel ) );
        mtm.addMesh( mesh );
    }

    public static Mesh.Vertices loadVertices( IndexedModel indexedModel )
    {
        List<Vector3f> positions = indexedModel.getPositions();
        List<Vector2f> texCoords = indexedModel.getTexCoords();
        List<Vector3f> normals   = indexedModel.getNormals();
        List<Vector3f> tangents  = indexedModel.getTangents();
        List<Integer>  indices   = indexedModel.getIndices();

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

        return new Mesh.Vertices( vertices, Utils.toIntArray( indices ) );
    }


    public TextureResource loadTexture( String subFolder, String fileName )
    {
        Logger.log( getClass(), "Loading texture at : " + subFolder + fileName );
        TextureResource resource = null;

        try
        {
            BufferedImage image = ImageIO.read( resourceStream( TEXTURES_PATH + subFolder + fileName ) );

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

            // MipMapping
            glGenerateMipmap( GL_TEXTURE_2D );
            glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR );
            glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.5f );
        }
        catch ( IllegalArgumentException e )
        {
            if ( fileName.contains( "_normal" ) )
                return Texture.getDefaultNormal().getResource();
            else if ( fileName.contains( "_height" ) )
                return Texture.getDefaultHeight().getResource();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }

        return resource;
    }

    public Map<String, SimpleMaterial> loadMaterial( String subFolder, String fileName )
    {
        Logger.log( getClass(), "Loading material at : " + subFolder + fileName );
        Map<String, SimpleMaterial> materialMap = new HashMap<>();

        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( resourceStream( MATERIALS_PATH + subFolder + fileName ) ) ) )
        {
            String line;
            SimpleMaterial.MaterialBuilder builder = new SimpleMaterial.MaterialBuilder();
            while ( ( line = reader.readLine() ) != null )
            {
                if ( line.startsWith( "newmtl" ) )
                {
                    builder = new SimpleMaterial.MaterialBuilder();
                }
                else if ( line.startsWith( "Ni" ) )
                {
                    builder.setSpecularIntensity( Float.parseFloat( line.split( " " )[ 1 ] ) );
                }
                else if ( line.startsWith( "Ns" ) )
                {
                    builder.setSpecularPower( Float.parseFloat( line.split( " " )[ 1 ] ) );
                }
                else if ( line.startsWith( "map_" ) )
                {
                    String[] s = line.split( " " )[ 1 ].split("\\.");
                    builder.setTexture( new Texture( subFolder, line.split( " " )[ 1 ] ) );
                    // builder.setNormalTexture( new Texture( subFolder, s[ 0 ] + "_normal." + s[ 1 ] ) );
                    materialMap.put( s[ 0 ], builder.build() );
                }
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            System.exit( 1 );
        }

        return materialMap;
    }
}
