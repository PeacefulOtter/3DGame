package peacefulotter.engine.core.transfomations;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector3f;

public class SScale
{
    private Vector3f scale;

    public SScale()
    {
        this.scale = new Vector3f( 1, 1, 1 );
    }

    public SScale( SScale scale )
    {
        this.scale = new Vector3f( scale.getScaleVector() );
    }

    public Vector3f getScaleVector() { return scale; }

    public void setScale( Vector3f scale ) { this.scale = scale; }

    public Matrix4f getScaleMatrix()
    {
        return new Matrix4f().initScale(
            scale.getX(), scale.getY(), scale.getZ()
        );
    }
}
