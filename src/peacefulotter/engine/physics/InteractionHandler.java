package peacefulotter.engine.physics;

import peacefulotter.engine.physics.colliders.PhysicsObject;

public interface InteractionHandler
{
    void interactWith( PhysicsObject object );
}
