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

    public Texture( String fileName ) { this( "", fileName ); }

    public Texture( String subFolder, String fileName )
    {
        String path = subFolder + fileName;
        if ( loadedTextures.containsKey( path ) )
            resource = loadedTextures.get( path );
        else
        {
            resource = new ResourceLoader().loadTexture( subFolder, fileName );
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
        assert ( samplerSlot >= 0 && samplerSlot <= 31 );
        glActiveTexture( GL_TEXTURE0 + samplerSlot );
        glBindTexture( GL_TEXTURE_2D,   resource.getId() );
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
