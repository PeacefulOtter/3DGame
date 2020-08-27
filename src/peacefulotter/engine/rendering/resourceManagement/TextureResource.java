package peacefulotter.engine.rendering.resourceManagement;

import peacefulotter.engine.elementary.Disposable;
import peacefulotter.engine.utils.Logger;
import peacefulotter.engine.utils.ResourceLoader;

import static org.lwjgl.opengl.GL11.glGenTextures;

public class TextureResource extends Disposable
{
    private final int id;

    public TextureResource()
    {
        this.id = glGenTextures();
    }

    public void dispose()
    {
        Logger.log( getClass(), "Dispose called");
        // glDeleteBuffers( id );
    }

    public int getId()  { return id; }
}
