package peacefulotter.engine.physics;

import peacefulotter.engine.core.maths.Vector3f;

public class IntersectData
{
    private final boolean doesIntersect;
    private final Vector3f direction;

    public IntersectData( boolean doesIntersect, Vector3f direction )
    {
        this.doesIntersect = doesIntersect;
        this.direction = direction;
    }

    public boolean getDoesIntersect() { return doesIntersect; }
    public Vector3f getDirection() { return direction; }
    public float getDistance() { return direction.length(); }
}
