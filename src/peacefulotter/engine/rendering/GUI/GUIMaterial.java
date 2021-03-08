package peacefulotter.engine.rendering.GUI;

import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.core.transfomations.STransform;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.SimpleMaterial;
import peacefulotter.engine.rendering.graphics.Texture;

public class GUIMaterial extends Material
{
    public enum ANCHOR_X {
        LEFT(),
        RIGHT(),
        CENTER();
    }
    public enum ANCHOR_Y {
        TOP(),
        BOTTOM(),
        CENTER();
    }

    private static final String GUI_FOLDER = "gui/";
    private final STransform transform;
    private ANCHOR_X anchorX;
    private ANCHOR_Y anchorY;

    public GUIMaterial( String fileName, Vector2f position, Vector2f scale, ANCHOR_X anchorX, ANCHOR_Y anchorY )
    {
        super( new SimpleMaterial( new Texture( GUI_FOLDER, fileName ), 1, 1 ) );
        this.transform = new STransform()
                .translate( new Vector3f( position.getX(), position.getY(), 0f ) )
                .scale( new Vector3f( scale.getX(), scale.getY(), 1f ) );
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    public GUIMaterial( String fileName, Vector2f position, Vector2f scale )
    {
        this(fileName, position, scale, ANCHOR_X.CENTER, ANCHOR_Y.CENTER);
    }

    public void setWidth( float width )
    {
        float ancientWidth = transform.getScale().getX();
        float padding = (ancientWidth - width);

        transform.scale( new Vector3f( width, transform.getScale().getY(), 1 ) );

        switch ( anchorX )
        {
            case LEFT:
                transform.translate( new Vector3f( -padding, 0, 0 ) );
                break;
            case RIGHT:
                transform.translate( new Vector3f( padding, 0, 0 ) );
                break;
            case CENTER:
            default:
                break;
        }
    }

    // dont use this method to set the width or height of a guimaterial
    public STransform getTransform()
    {
        return transform;
    }
}
