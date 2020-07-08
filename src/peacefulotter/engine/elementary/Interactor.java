package peacefulotter.engine.elementary;

import peacefulotter.engine.physics.IntersectData;

/*
 * Represents an Interactor object (i.e. it can interact with some Interactable)
 */
public interface Interactor
{
    void interactWith(Interactable other, IntersectData intersectData);
}
