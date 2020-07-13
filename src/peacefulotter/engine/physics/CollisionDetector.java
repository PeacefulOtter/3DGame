package peacefulotter.engine.physics;

import peacefulotter.engine.core.maths.Matrix3f;
import peacefulotter.engine.core.maths.Vector3f;

public class CollisionDetector
{
    private Vector3f ellipsoidPositionIR, ellipsoidPositionIE;
    private Vector3f triangleP1R, triangleP2R, triangleP3R;
    private Vector3f triangleP1E, triangleP2E, triangleP3E;
    private Vector3f P2P1E, P2P3E;
    private Vector3f planeIntersectionPointR, planeIntersectionPointE;
    private Vector3f planeNormal, planeUnitNormal;
    private float distance, t0, t1;
    private Vector3f velocityR, velocityE;

    private Matrix3f toESpaceMatrix, fromESpaceMatrix;
    private CollisionPacket collisionPacketVertex,
            collisionPacketSurface,
            collisionPacketEdge;

    public CollisionDetector(
            Vector3f position, Vector3f velocity,
            Vector3f P1, Vector3f P2, Vector3f P3,
            float ellipsoidMaxX, float ellipsoidMaxY, float ellipsoidMaxZ  )
    {
        this.ellipsoidPositionIR = position;
        this.velocityR = velocity;
        this.triangleP1R = P1;
        this.triangleP2R = P2;
        this.triangleP3R = P3;

        this.toESpaceMatrix = new Matrix3f().initCollision( ellipsoidMaxX, ellipsoidMaxY, ellipsoidMaxZ );
    }
}
