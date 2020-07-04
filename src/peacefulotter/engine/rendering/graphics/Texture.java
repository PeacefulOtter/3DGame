package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.rendering.resourceManagement.MeshResource;
import peacefulotter.engine.rendering.resourceManagement.TextureResource;
import peacefulotter.engine.utils.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_BINDING_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.*;


public class Texture
{
    private static final Map<String, TextureResource> loadedTextures = new HashMap<>();

    private final TextureResource resource;

    public Texture( String textureName )
    {
        if ( loadedTextures.containsKey( textureName ) )
            resource = loadedTextures.get( textureName );
        else
            resource = new TextureResource( new ResourceLoader().loadTexture( textureName ) );
    }

    public void bind()
    {
        glBindTexture( GL_TEXTURE_2D, resource.getId() );
    }
}
