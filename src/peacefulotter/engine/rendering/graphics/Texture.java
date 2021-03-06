package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.rendering.resourceManagement.TextureResource;
import peacefulotter.engine.utils.BufferUtil;
import peacefulotter.engine.utils.Logger;
import peacefulotter.engine.utils.ResourceLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;


public class Texture
{
    private static final String TEXTURES_PATH = "/textures/";
    private static final String DEFAULT_FOLDER = "default/";

    private static final Map<String, TextureResource> loadedTextures = new HashMap<>();

    private final TextureResource resource;
    private final String subFolder, fileName;

    public enum TextureTypes
    {
        BASIC(),
        CUBE_MAP();
    }

    public Texture( String fileName ) { this( fileName, TextureTypes.BASIC ); }

    public Texture( String fileName, TextureTypes type )
    {
        this( "", fileName, type );
    }

    public Texture( String subFolder, String fileName )
    {
        this( subFolder, fileName, TextureTypes.BASIC );
    }

    public Texture( String subFolder, String fileName, TextureTypes type )
    {
        String path = subFolder + fileName;
        if ( loadedTextures.containsKey( path ) )
            resource = loadedTextures.get( path );
        else
        {
            switch ( type )
            {
                case BASIC:
                    resource = loadTexture( subFolder, fileName );
                    break;
                case CUBE_MAP:
                    resource = ResourceLoader.loadCubeMap();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            loadedTextures.put( path, resource );
        }

        this.subFolder = subFolder;
        this.fileName = fileName;
    }

    /**
     * Creates a TextureResource from an image file
     * @param subFolder
     * @param fileName
     * @return
     */
    public TextureResource loadTexture( String subFolder, String fileName )
    {
        Logger.log( getClass(), "Loading texture at : " + subFolder + fileName );
        TextureResource resource = null;

        try
        {
            BufferedImage image = ImageIO.read( new ResourceLoader().resourceStream( TEXTURES_PATH + subFolder + fileName ) );

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

    public static Texture getDefaultNormal()
    {
        return new Texture( DEFAULT_FOLDER, "default_normal.jpg" );
    }

    public static Texture getDefaultHeight()
    {
        return new Texture( DEFAULT_FOLDER, "default_height.jpg" );
    }

    public void bind( int samplerSlot )
    {
        bind( samplerSlot, GL_TEXTURE_2D );
    }

    public void bind( int samplerSlot, int target )
    {
        assert ( samplerSlot >= 0 && samplerSlot <= 31 );
        glActiveTexture( GL_TEXTURE0 + samplerSlot );
        glBindTexture( target, resource.getId() );
    }

    public void bindAsRenderTarget()
    {
        //
    }

    public TextureResource getResource() { return resource; }

    @Override
    public String toString()
    {
        return "[Texture] " + subFolder + fileName;
    }
}
