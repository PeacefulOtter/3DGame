package peacefulotter.engine.rendering.GUI;

import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.transfomations.STransform;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.SimpleMaterial;
import peacefulotter.engine.rendering.graphics.Texture;

public class GUIMaterial extends Material
{
    private static final String GUI_FOLDER = "gui/";
    private final STransform transform;

    public GUIMaterial( String fileName, Vector2f position, Vector2f scale )
    {
        super( new SimpleMaterial( new Texture( GUI_FOLDER, fileName ), 1, 1 ) );
        this.transform = new STransform();
                // .translate( new Vector3f( position.getX(), position.getY(), 0 ) )
    }

    public STransform getTransform()
    {
        return transform;
    }
}
