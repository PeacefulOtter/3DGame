package peacefulotter.engine.physics.colliders;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.physics.IntersectData;

import static peacefulotter.engine.physics.colliders.Collider.ColliderTypes.*;

public class BoundingSphere extends Collider
{
    private final Vector3f center;
    private final float radius;

    public BoundingSphere( Vector3f center, float radius )
    {
        super( BOUNDING_SPHERE );
        this.center = center;
        this.radius = radius;
    }

    public Vector3f getCenter() { return center; }
    public float getRadius() { return radius; }
}
