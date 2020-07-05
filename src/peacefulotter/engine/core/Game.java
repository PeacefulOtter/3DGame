package peacefulotter.engine.core;

import peacefulotter.engine.components.GameObject;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.Window;
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

    public void init() { root.initAll(); }
    public void updateAll(float deltaTime ) { root.updateAll( deltaTime ); }
    public void render( RenderingEngine renderingEngine ) { renderingEngine.render( root ); }

    public void addObject( GameObject object ) { root.addChild( object ); }
    public void addObjects( GameObject ...objects )
    {
        for ( GameObject object : objects )
            addObject( object );
    }
}
