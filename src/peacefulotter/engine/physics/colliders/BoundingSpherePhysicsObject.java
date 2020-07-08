package peacefulotter.engine.physics.colliders;

import peacefulotter.engine.core.maths.Vector3f;

public class BoundingSpherePhysicsObject extends PhysicsObject
{
    private float radius;

    public BoundingSpherePhysicsObject( float radius )
    {
        this( Vector3f.getZero(), Vector3f.getZero(), radius );
    }

    public BoundingSpherePhysicsObject( Vector3f position, Vector3f velocity, float radius )
    {
        super( position, velocity );
        this.radius = radius;
    }

    @Override
    public BoundingSphere getCollider()
    {
        return new BoundingSphere( getPosition(), radius );
    }
}
