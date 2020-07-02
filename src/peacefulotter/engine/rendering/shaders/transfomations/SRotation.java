package peacefulotter.engine.rendering.shaders.transfomations;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector3f;

public class SRotation
{
    private Quaternion rotation = new Quaternion( 0, 0, 0, 1 );

    public Quaternion getRotation()
    {
        return rotation;
    }

    public void setRotation( Quaternion rotation ) { this.rotation = rotation; }

    public Matrix4f getRotationMatrix()
    {
        return rotation.toRotationMatrix();
    }

    public void rotate( Vector3f axis, float angleDeg )
    {
        setRotation( rotation.mul( new Quaternion( axis, angleDeg ) ) );
    }
}
