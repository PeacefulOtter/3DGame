package peacefulotter.engine.components;

import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.elementary.Initializable;
import peacefulotter.engine.elementary.Renderable;
import peacefulotter.engine.elementary.Updatable;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameObject implements Initializable, Updatable, Renderable
{
    private final STransform transform = new STransform();

    private final Set<GameComponent> components = new HashSet<>();
    private final Set<GameObject> children = new HashSet<>();
    private final Set<PhysicsObject> physicsChildren = new HashSet<>();

    private CoreEngine engine;

    public GameObject addComponent( GameComponent component )
    {
        components.add( component );
        component.setParent( getTransform() );
        return this;
    }

    public GameObject addChild( GameObject child )
    {
        children.add( child );
        getTransform().addChild( child.getTransform() );
        return this;
    }

    public void removeChild( GameObject child )
    {
        children.remove( child );
    }

    public GameObject addPhysicalChild( PhysicsObject child )
    {
        physicsChildren.add( child );
        getTransform().addChild( child.getTransform() );
        return addChild( child );
    }

    public void removePhysicalChild( PhysicsObject child )
    {
        physicsChildren.remove( child );
    }


    public void init()
    {
        for ( GameComponent component: components )
            component.init();
    }

    @Override
    public void update( float deltaTime )
    {
        getTransform().update( deltaTime );

        for ( GameComponent component: components )
            component.update( deltaTime );
    }

    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        for ( GameComponent component: components )
            component.render( shader, renderingEngine );
    }


    public void initAll()
    {
        init();

        for ( GameObject child: children )
            child.initAll();
    }

    public void updateAll( float deltaTime )
    {
        update( deltaTime );

        for ( GameObject child: children )
            child.updateAll( deltaTime );
    }

    public void renderAll( Shader shader, RenderingEngine renderingEngine )
    {
        render( shader, renderingEngine );

        for ( GameObject child: children )
            child.renderAll( shader, renderingEngine );
    }

    public void simulateAll( float deltaTime )
    {
        for ( PhysicsObject object : physicsChildren )
            object.simulateAll( deltaTime );
    }

    public List<GameObject> getAttachedObjects()
    {
        List<GameObject> objects = new ArrayList<>();
        objects.add( this );
        children.forEach( ( object ) -> objects.addAll( object.getAttachedObjects() ) );
        return objects;
    }

    public void setEngine( CoreEngine engine )
    {
        if ( this.engine != engine )
        {
            this.engine = engine;

            for ( GameComponent component: components )
                component.addToEngine( engine );

            for( GameObject child: children )
                child.setEngine( engine );
        }
    }

    public STransform getTransform() { return transform; }
    protected CoreEngine getCoreEngine() { return engine; }
    protected Set<PhysicsObject> getPhysicsChildren() { return physicsChildren; }
}
