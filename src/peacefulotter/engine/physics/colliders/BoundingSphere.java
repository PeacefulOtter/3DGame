package peacefulotter.engine.physics.colliders;

import peacefulotter.engine.core.maths.Vector3f;

import static peacefulotter.engine.physics.colliders.Collider.ColliderTypes.*;

public class BoundingSphere extends Collider
{
    private final float radius;

    public BoundingSphere( Vector3f center, float radius )
    {
        super( BOUNDING_SPHERE, center );
        this.radius = radius;
    }

    public Vector3f getCenter() { return getPosition(); }
    public float getRadius() { return radius; }
}
