package peacefulotter.engine.core;

import peacefulotter.engine.Utils.IO.Input;
import peacefulotter.engine.core.elementary.GameComponent;
import peacefulotter.engine.core.elementary.GameObject;
import peacefulotter.engine.core.elementary.Initializable;
import peacefulotter.engine.core.elementary.Updatable;
import peacefulotter.engine.rendering.Camera;

abstract public class Game implements Updatable
{
    private final GameObject root;

    public Game( GameObject root )
    {
        this.root = root;
    }
    public void init() { root.init(); }
    public void update() { root.update(); Input.execInputs();  }

    abstract public void startEngine();

    public GameObject getRootObject() { return root; }
}
