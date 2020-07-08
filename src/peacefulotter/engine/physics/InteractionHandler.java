package peacefulotter.engine.physics;

import peacefulotter.engine.components.PhysicsObject;

public interface InteractionHandler
{
    void interactWith(PhysicsObject object, IntersectData intersectData);
    // void interactWith( PhysicsObjectHerited other );
}
