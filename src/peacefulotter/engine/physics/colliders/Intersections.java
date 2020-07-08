package peacefulotter.engine.physics.colliders;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.physics.IntersectData;

public class Intersections
{
    private Intersections() {}

    public static IntersectData intersect( AABB collider1, AABB collider2 )
    {
        Vector3f distances1 = collider2.getMinExtents().sub( collider1.getMaxExtents() );
        Vector3f distances2 = collider1.getMinExtents().sub( collider2.getMaxExtents() );
        Vector3f distances  = distances1.max( distances2 );
        float maxDistance   = distances.maxValue();

        return new IntersectData( maxDistance < 0, maxDistance );
    }

    public static IntersectData intersect( AABB collider1, BoundingSphere collider2 )
    {
        return null;
    }

    public static IntersectData intersect( BoundingSphere collider1, AABB collider2 )
    {
        return intersect( collider2, collider1 );
    }

    public static IntersectData intersect( AABB collider1, Plane collider2 )
    {
        return null;
    }

    public static IntersectData intersect( Plane collider1, AABB collider2 )
    {
        return intersect( collider2, collider1 );
    }

    public static IntersectData intersect( BoundingSphere collider1, BoundingSphere collider2 )
    {
        float radiusDistance = collider1.getRadius() + collider2.getRadius();
        float centerDistance = collider2.getCenter().sub( collider1.getCenter() ).length();
        float distance = centerDistance - radiusDistance;

        return new IntersectData( distance < 0, distance );
    }

    public static IntersectData intersect( Plane collider1, BoundingSphere collider2 )
    {
        float distanceFromCenterSphere = Math.abs(
                collider1.getNormal().dot( collider2.getCenter() ) + collider1.getDistance() );
        float distanceFromSphere = distanceFromCenterSphere - collider2.getRadius();

        return new IntersectData( distanceFromSphere < 0, distanceFromSphere );
    }

    public static IntersectData intersect( BoundingSphere collider1, Plane collider2 )
    {
        return intersect( collider2, collider1 );
    }

    public static IntersectData intersect( Plane collider1, Plane collider2 )
    {
        return null;
    }

    public static IntersectData intersect( Collider collider1, Collider collider2 )
    {
        if ( collider1 instanceof AABB )
        {
            if ( collider2 instanceof AABB )
                return intersect( (AABB) collider1, (AABB) collider2 );
            else if ( collider2 instanceof BoundingSphere )
                return intersect( (AABB) collider1, (BoundingSphere) collider2 );
            else if ( collider2 instanceof Plane )
                return intersect( (AABB) collider1, (Plane) collider2 );
        }
        else if ( collider1 instanceof BoundingSphere )
        {
            if ( collider2 instanceof AABB )
                return intersect( (BoundingSphere) collider1, (AABB) collider2 );
            else if ( collider2 instanceof BoundingSphere )
                return intersect( (BoundingSphere) collider1, (BoundingSphere) collider2 );
            else if ( collider2 instanceof Plane )
                return intersect( (BoundingSphere) collider1, (Plane) collider2 );
        }
        else if ( collider1 instanceof Plane )
        {
            if ( collider2 instanceof AABB )
                return intersect( (Plane) collider1, (AABB) collider2 );
            else if ( collider2 instanceof BoundingSphere )
                return intersect( (Plane) collider1, (BoundingSphere) collider2 );
            else if ( collider2 instanceof Plane )
                return intersect( (Plane) collider1, (Plane) collider2 );
        }

        throw new IllegalArgumentException( "Could not intersect " + collider1 + " and " + collider2 );
    }
}
