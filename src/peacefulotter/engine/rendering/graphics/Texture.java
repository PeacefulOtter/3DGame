package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.rendering.resourceManagement.TextureResource;
import peacefulotter.engine.utils.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;


public class Texture
{
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
                    resource = new ResourceLoader().loadTexture( subFolder, fileName );
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
