package peacefulotter.engine.physics.colliders;

import peacefulotter.engine.core.maths.Vector3f;

public class PlanePhysicsObject extends PhysicsObject
{
    private Vector3f normal;
    private float distance;

    public PlanePhysicsObject( Vector3f normal, float distance  )
    {
        this( Vector3f.getZero(), Vector3f.getZero(), normal, distance );
    }

    public PlanePhysicsObject( Vector3f position, Vector3f velocity, Vector3f normal, float distance )
    {
        super( position, velocity );
        this.normal = normal;
        this.distance = distance;
    }

    @Override
    public Plane getCollider()
    {
        return new Plane( normal, distance );
    }
}
