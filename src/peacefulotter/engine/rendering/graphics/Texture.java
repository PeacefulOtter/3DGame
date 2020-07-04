package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.utils.ResourceLoader;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_BINDING_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.*;


public class Texture
{
    private final int id;

    public Texture( String textureName )
    {
        this.id = new ResourceLoader().loadTexture( textureName );
    }

    public void bind()
    {
        glBindTexture( GL_TEXTURE_BINDING_2D, id );
    }
}
