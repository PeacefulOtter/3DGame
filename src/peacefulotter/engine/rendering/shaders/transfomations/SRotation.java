package peacefulotter.engine.rendering.shaders.transfomations;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector3f;

public class SRotation
{
    private Quaternion rotation;

    public SRotation()
    {
       rotation = new Quaternion( 0, 0, 0, 1 );
    }

    public SRotation( SRotation rotation )
    {
        this.rotation = new Quaternion( rotation.getRotationQuaternion() );
    }

    public Quaternion getRotationQuaternion()
    {
        return rotation;
    }

    public void setRotation( Quaternion rotation ) { this.rotation = rotation.normalize(); }

    public Matrix4f getRotationMatrix()
    {
        return rotation.toRotationMatrix();
    }

    public void rotate( Vector3f axis, float angleDeg )
    {
        rotate( new Quaternion( axis, angleDeg ) );
    }

    public void rotate( Quaternion q )
    {
        setRotation( q.mul( rotation ) );
    }
}
