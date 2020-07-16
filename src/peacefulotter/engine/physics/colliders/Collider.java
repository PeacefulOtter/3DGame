package peacefulotter.engine.physics.colliders;

import peacefulotter.engine.core.maths.Vector3f;

public abstract class Collider
{
    private final ColliderTypes type;
    private Vector3f position;

    public Collider( ColliderTypes type, Vector3f position )
    {
        this.type = type;
        this.position = position;
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

    public Vector3f getPosition() { return position; }
    public void setPosition( Vector3f position ) { this.position = position; }
}
