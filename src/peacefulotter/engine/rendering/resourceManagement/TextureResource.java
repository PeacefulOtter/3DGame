package peacefulotter.engine.rendering.resourceManagement;

import static org.lwjgl.opengl.GL11.glGenTextures;

public class TextureResource
{
    private final int id;

    public TextureResource()
    {
        this.id = glGenTextures();
        // Used to free memory and thus avoid memory leak
        Resources.addBuffer( this.id );
    }

    public int getId()  { return id; }
}
