package peacefulotter.engine.physics.colliders;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.physics.IntersectData;

public class Plane extends Collider
{
    private final Vector3f normal;
    private final float distance;

    public Plane( Vector3f normal, float distance )
    {
        super( ColliderTypes.PLANE );
        this.normal = normal;
        this.distance = distance;
    }

    public Plane normalize()
    {
        return new Plane( normal.normalize(), distance / normal.length() );
    }

    public IntersectData intersectBoundingSphere( BoundingSphere other )
    {
        float distanceFromCenterSphere = Math.abs( normal.dot( other.getCenter() ) + distance );
        float distanceFromSphere = distanceFromCenterSphere - other.getRadius();

        return new IntersectData( distanceFromSphere < 0, distanceFromSphere );
    }

    public Vector3f getNormal() { return normal; }
    public float getDistance() { return distance; }

    @Override
    public IntersectData intersect(Collider other)
    {
        return null;
    }
}
