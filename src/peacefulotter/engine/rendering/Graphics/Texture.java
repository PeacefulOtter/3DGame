package peacefulotter.engine.rendering.Graphics;

import peacefulotter.engine.Utils.ResourceLoader;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_BINDING_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class Texture
{
    private final int id;

    public Texture( int id ) { this.id = id; }

    public Texture( String textureName ) { this.id = new ResourceLoader().loadTexture( textureName ); }

    public void bind()
    {
        glBindTexture( GL_TEXTURE_BINDING_2D, id );
    }

    public int getId() { return id; }

    // public void setId( int id ) { this.id = id; }
}
