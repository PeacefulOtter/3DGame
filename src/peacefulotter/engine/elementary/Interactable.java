package peacefulotter.engine.elementary;

import peacefulotter.engine.physics.InteractionHandler;

public interface Interactable
{
    void acceptInteraction( InteractionHandler other );
}
