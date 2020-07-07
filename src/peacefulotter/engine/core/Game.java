package peacefulotter.engine.core;

import peacefulotter.engine.components.GameObject;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.elementary.Updatable;
import peacefulotter.engine.physics.PhysicsEngine;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.Window;
import peacefulotter.engine.utils.ProfileTimer;

public abstract class Game implements Updatable
{
    private final ProfileTimer updateProfiler, physicsProfiler;
    private final CoreEngine engine;
    private final GameObject root;

    protected Game( String winName, int winWidth, int winHeight )
    {
        Window.setAttributes( winName, winWidth, winHeight );
        this.updateProfiler = new ProfileTimer();
        this.physicsProfiler = new ProfileTimer();
        this.engine = new CoreEngine( this );
        this.root = new GameObject();
        startEngine();
    }

    public void startEngine() { engine.start(); }
    public void setEngine( CoreEngine engine ) { root.setEngine( engine ); }

    public void init() { root.initAll(); }
    public void update( float deltaTime )
    {
        updateProfiler.startInvocation();
        root.updateAll( deltaTime );
        updateProfiler.stopInvocation();
    }
    public void simulate( float deltaTime, PhysicsEngine physicsEngine )
    {
        physicsProfiler.startInvocation();
        physicsEngine.simulate( root, deltaTime );
        physicsProfiler.stopInvocation();
    }
    public void render( RenderingEngine renderingEngine ) { renderingEngine.render( root ); }

    public void addObject( GameObject object ) { root.addChild( object ); }
    public void addObjects( GameObject ...objects )
    {
        for ( GameObject object : objects )
            addObject( object );
    }

    public double displayUpdateTime( double dividend )
    {
        return updateProfiler.displayAndReset( "Update time", dividend );
    }

    public double displayPhysicsTime( double dividend )
    {
        return physicsProfiler.displayAndReset( "Physics time", dividend );
    }
}
