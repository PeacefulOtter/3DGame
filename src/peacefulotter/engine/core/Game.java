package peacefulotter.engine.core;

import peacefulotter.engine.core.elementary.Initializable;
import peacefulotter.engine.core.elementary.Updatable;

abstract public class Game implements Initializable, Updatable
{
    abstract public void render();
    abstract public void startEngine();
}
