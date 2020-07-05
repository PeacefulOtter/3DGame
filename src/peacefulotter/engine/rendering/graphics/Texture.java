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
    private static final Map<String, TextureResource> loadedTextures = new HashMap<>();

    private final TextureResource resource;

    public Texture( String textureName )
    {
        if ( loadedTextures.containsKey( textureName ) )
            resource = loadedTextures.get( textureName );
        else
        {
            resource = new ResourceLoader().loadTexture( textureName );
            loadedTextures.put( textureName, resource );
        }
    }

    public void bind() { bind(0); }

    public void bind( int samplerSlot )
    {
        assert ( samplerSlot >= 0 && samplerSlot <= 31 );
        glActiveTexture( GL_TEXTURE0 + samplerSlot );
        glBindTexture( GL_TEXTURE_2D, resource.getId() );
    }
}
