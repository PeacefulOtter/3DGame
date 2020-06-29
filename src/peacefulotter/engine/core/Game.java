package peacefulotter.engine.core;

import peacefulotter.engine.components.Camera;
import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.components.GameObject;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.Window;
import peacefulotter.engine.utils.IO.Input;
import peacefulotter.engine.elementary.Updatable;

public abstract class Game implements Updatable
{
    private final CoreEngine engine;
    private final GameObject root;

    protected Game( String winName, int winWidth, int winHeight )
    {
        Window.setAttributes( winName, winWidth, winHeight );
        this.engine = new CoreEngine( this );
        this.root = new GameObject();
        startEngine();
    }

    public void startEngine() { engine.start(); }
    public void setEngine( CoreEngine engine ) { root.setEngine( engine ); }

    public void init() { root.init(); }
    public void update( float deltaTime ) { root.update( deltaTime ); }
    public void render( RenderingEngine renderingEngine ) { renderingEngine.render( root ); }

    public void addObject( GameObject object ) { root.addChild( object ); }
    public void addComponent( GameComponent component ) { root.addComponent( component ); }

    // public GameObject getRootObject() { return root; }

    public void setCamera( Camera camera ) { engine.getRenderingEngine().setCamera( camera ); }
}
