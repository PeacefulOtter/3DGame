package peacefulotter.engine.core;

import peacefulotter.engine.components.GameObject;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.utils.IO.Input;
import peacefulotter.engine.elementary.Initializable;
import peacefulotter.engine.elementary.Updatable;

public class Game implements Initializable, Updatable
{
    private final CoreEngine engine;
    private final GameObject root;

    protected Game( String winName, int winWidth, int winHeight )
    {
        this.engine = new CoreEngine( this, winName, winWidth, winHeight );
        this.root = new GameObject();
        startEngine();
    }

    public void startEngine() { engine.start(); }

    public void init() { root.init(); }
    public void update( float deltaTime ) { root.update( deltaTime ); Input.execInputs( deltaTime );  }
    public void render( RenderingEngine renderingEngine ) { renderingEngine.render( root ); }

    public void addObject( GameObject object ) { root.addChild( object ); }

    public GameObject getRootObject() { return root; }
}
