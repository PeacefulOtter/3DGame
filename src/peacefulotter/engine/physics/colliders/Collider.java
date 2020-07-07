package peacefulotter.engine.physics.colliders;

import peacefulotter.engine.physics.IntersectData;

public abstract class Collider
{
    private final ColliderTypes type;

    public Collider( ColliderTypes type )
    {
        this.type = type;
    }

    abstract public IntersectData intersect( Collider other );

    public enum ColliderTypes
    {
        BOUNDING_SPHERE( BoundingSphere.class ),
        AABB( AABB.class ),
        PLANE( Plane.class );


        ColliderTypes( Class<? extends Collider> a )
        {
        }
    }

    public ColliderTypes getType() { return type; }
}
