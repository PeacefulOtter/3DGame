package peacefulotter.engine.elementary;

import peacefulotter.engine.physics.InteractionHandler;
import peacefulotter.engine.physics.IntersectData;

public interface Interactable
{
    void acceptInteraction(InteractionHandler other, IntersectData intersectData);
}
