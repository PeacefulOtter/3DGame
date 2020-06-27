package peacefulotter.game.Display.Shaders.Transfomations;

import peacefulotter.game.Maths.Matrix4f;
import peacefulotter.game.Maths.Vector3f;

public class SScale
{
    private Vector3f scale;

    public SScale()
    {
        scale = new Vector3f( 1, 1, 1 );
    }

    public Vector3f getScale() { return scale; }

    public void setScale( Vector3f scale ) { this.scale = scale; }

    public void setScale( float x, float y, float z )
    {
        scale = new Vector3f( x, y, z );
    }

    public Matrix4f getScaleMatrix()
    {
        Matrix4f scaleMatrix = new Matrix4f().initScale(
            scale.getX(), scale.getY(), scale.getZ()
        );
        return scaleMatrix;
    }
}
