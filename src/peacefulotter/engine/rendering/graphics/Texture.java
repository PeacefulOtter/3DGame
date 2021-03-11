package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.rendering.resourceManagement.TextureBuffer;
import peacefulotter.engine.rendering.resourceManagement.TextureResource;
import peacefulotter.engine.rendering.resourceManagement.TexturesHandler;
import peacefulotter.engine.utils.Logger;
import peacefulotter.engine.utils.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

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

    private final String subFolder, fileName;
    private TextureResource resource;

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
                    loadTexture( path );
                    break;
                case CUBE_MAP:
                    resource = ResourceLoader.loadCubeMap();
                    loadedTextures.put( path, resource );
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        this.subFolder = subFolder;
        this.fileName = fileName;
    }

    /**
     * Creates a TextureResource from an image file, operation done in another Thread using the TextureHandler
     */
    private void loadTexture( String path )
    {
        Logger.log( getClass(), "Loading texture at : " + path );
        TexturesHandler.create( this, TEXTURES_PATH + path );
    }

    public void setResource( TextureBuffer tb )
    {
        Logger.log( getClass(), "Creating the texture of " + subFolder + fileName );
        resource = new TextureResource();
        loadedTextures.put( subFolder + fileName, resource );
        glBindTexture( GL_TEXTURE_2D, resource.getId() );

        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT );
        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT );

        glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
        glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );

        glTexImage2D( GL_TEXTURE_2D, 0, GL_RGBA8, tb.imageWidth, tb.imageHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, tb.buffer );

        // MipMapping
        glGenerateMipmap( GL_TEXTURE_2D );
        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR );
        glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.5f );
    }

    public static Texture getDefaultNormal()
    {
        return new Texture( DEFAULT_FOLDER, "default_normal.jpg", TextureTypes.BASIC );
    }

    public static Texture getDefaultHeight()
    {
        return new Texture( DEFAULT_FOLDER, "default_height.jpg", TextureTypes.BASIC  );
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

    @Override
    public String toString()
    {
        return subFolder + fileName;
    }
}
