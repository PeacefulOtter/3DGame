package peacefulotter.engine.physics.colliders;

public abstract class Collider
{
    private final ColliderTypes type;

    public Collider( ColliderTypes type )
    {
        this.type = type;
    }

    public enum ColliderTypes
    {
        BOUNDING_SPHERE( BoundingSphere.class ),
        AABB( AABB.class ),
        PLANE( Plane.class );

        private final Class<? extends Collider> colliderClass;

        ColliderTypes( Class<? extends Collider> a )
        {
            this.colliderClass = a;
        }

        public Class<? extends Collider> getColliderClass() { return colliderClass; }
    }

    public ColliderTypes getType() { return type; }
}
