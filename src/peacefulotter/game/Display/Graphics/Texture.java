package peacefulotter.game.Display.Graphics;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_BINDING_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class Texture
{
    private int id;

    public Texture( int id ) { this.id = id; }

    public void bind()
    {
        glBindTexture( GL_TEXTURE_BINDING_2D, id );
    }

    public int getId() { return id; }

    // public void setId( int id ) { this.id = id; }
}
