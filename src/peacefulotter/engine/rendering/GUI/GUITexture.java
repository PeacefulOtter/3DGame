package peacefulotter.engine.rendering.GUI;

import org.w3c.dom.Text;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.rendering.graphics.Texture;

public class GUITexture
{
    private final Texture texture;
    private final Vector2f position, scale;

    public GUITexture( String subFolder, String fileName, Vector2f position, Vector2f scale )
    {
        this.texture = new Texture( subFolder, fileName );
        this.position = position;
        this.scale = scale;
    }

    public Texture getTexture()
    {
        return texture;
    }

    public Vector2f getPosition()
    {
        return position;
    }

    public Vector2f getScale()
    {
        return scale;
    }
}
