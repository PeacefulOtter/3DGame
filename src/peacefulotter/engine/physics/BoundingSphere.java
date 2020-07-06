package peacefulotter.engine.physics;

import peacefulotter.engine.core.maths.Vector3f;

public class BoundingSphere
{
    private final Vector3f center;
    private final float radius;

    public BoundingSphere( Vector3f center, float radius )
    {
        this.center = center;
        this.radius = radius;
    }

    public IntersectData intersectBoundingSphere( BoundingSphere other )
    {
        float radiusDistance = radius + other.getRadius();
        float centerDistance = other.getCenter().sub( center ).length();
        float distance = centerDistance - radiusDistance;

        return new IntersectData( distance < 0, distance );
    }

    public Vector3f getCenter() { return center; }
    public float getRadius() { return radius; }
}
