package peacefulotter.engine.physics.colliders;

import peacefulotter.engine.components.PhysicsObject;
import peacefulotter.engine.core.maths.Vector3f;

public class AABBPhysicsObject extends PhysicsObject
{
    private Vector3f minExtents, maxExtents;

    public AABBPhysicsObject( Vector3f minExtents, Vector3f maxExtents )
    {
        this( Vector3f.getZero(), Vector3f.getZero(), minExtents, maxExtents );
    }

    public AABBPhysicsObject( Vector3f position, Vector3f velocity, Vector3f minExtents, Vector3f maxExtents )
    {
        super( position, velocity, false );
        this.minExtents = minExtents;
        this.maxExtents = maxExtents;
    }

    @Override
    public AABB getCollider()
    {
        return new AABB( Vector3f.getZero(), Vector3f.getZero() );
    }
}
