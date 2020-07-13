package peacefulotter.engine.physics;

import peacefulotter.engine.core.maths.Vector3f;

public class CollisionPacket
{
    private Vector3f collisionPosition;
    private float collisionDistance;
    private int type;

    public CollisionPacket(Vector3f collisionPosition, float collisionDistance, int type)
    {
        this.collisionPosition = collisionPosition;
        this.collisionDistance = collisionDistance;
        this.type = type;
    }

    public Vector3f getCollisionPosition()
    {
        return collisionPosition;
    }

    public void setCollisionPosition(Vector3f collisionPosition)
    {
        this.collisionPosition = collisionPosition;
    }

    public float getCollisionDistance()
    {
        return collisionDistance;
    }

    public void setCollisionDistance(float collisionDistance)
    {
        this.collisionDistance = collisionDistance;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
}
