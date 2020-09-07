package peacefulotter.engine.rendering.GUI;

import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.core.transfomations.STransform;
import peacefulotter.engine.rendering.graphics.Texture;

public class GUITexture extends Texture
{
    private static final String GUI_FOLDER = "gui/";
    private final STransform transform;

    public GUITexture( String fileName, Vector2f position, Vector2f scale )
    {
        super( GUI_FOLDER, fileName );
        this.transform = new STransform();
                //.translate( new Vector3f( position.getX(), position.getY(), 0 ) )
                //.scale( new Vector3f( scale.getX(), scale.getY(), 0 ) );
    }

    public STransform getTransform()
    {
        return transform;
    }
}
