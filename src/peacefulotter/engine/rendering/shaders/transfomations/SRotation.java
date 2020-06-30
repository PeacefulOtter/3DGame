package peacefulotter.engine.rendering.shaders.transfomations;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Quaternion;

public class SRotation
{
    private Quaternion rotation = new Quaternion( 0, 0, 0, 1 );

    public Matrix4f getRotationMatrix()
    {
        /*return new Matrix4f().initRotation(
                rotation.getX(), rotation.getY(), rotation.getZ()
        );*/
        return rotation.toRotationMatrix();
    }

    public Quaternion getRotation()
    {
        return rotation;
    }

    public void setRotation( Quaternion rotation )
    {
        this.rotation = rotation;
    }

    public void setRotation( float x, float y, float z, float w )
    {
        rotation = new Quaternion( x, y, z, w );
    }
}
