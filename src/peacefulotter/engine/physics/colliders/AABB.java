package peacefulotter.engine.physics.colliders;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.physics.IntersectData;

// Axis Aligned Bounding Box
public class AABB extends Collider
{
    private Vector3f minExtents, maxExtents; // two extreme corners of the box

    public AABB( Vector3f minExtents, Vector3f maxExtents )
    {
        super( ColliderTypes.AABB );
        this.minExtents = minExtents;
        this.maxExtents = maxExtents;
    }

    public IntersectData intersectAABB(AABB other )
    {
        Vector3f distances1 = other.getMinExtents().sub( maxExtents );
        Vector3f distances2 = minExtents.sub( other.getMaxExtents() );
        Vector3f distances = distances1.max( distances2 );
        float maxDistance = distances.maxValue();
        return new IntersectData( maxDistance < 0, maxDistance );
    }

    public Vector3f getMinExtents() { return minExtents; }
    public void setMinExtents( Vector3f minExtents ) { this.minExtents = minExtents; }

    public Vector3f getMaxExtents() { return maxExtents; }
    public void setMaxExtents( Vector3f maxExtents ) { this.maxExtents = maxExtents; }

    @Override
    public IntersectData intersect(Collider other)
    {
        return null;
    }
}
