package peacefulotter.engine.physics;

public class IntersectData
{
    private final boolean doesIntersect;
    private final float distance;

    public IntersectData( boolean doesIntersect, float distance )
    {
        this.doesIntersect = doesIntersect;
        this.distance = distance;
    }

    public boolean getDoesIntersect() { return doesIntersect; }
    public float getDistance() { return distance; }
}
