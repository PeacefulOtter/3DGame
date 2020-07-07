package peacefulotter.engine.components;

import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameObject extends GameModel
{
    private final Set<GameComponent> components = new HashSet<>();
    private final Set<GameObject> children = new HashSet<>();

    private CoreEngine engine;

    public GameObject() { super(); }
    public GameObject( Vector3f vel ) { super( vel ); }

    public GameObject addComponent( GameComponent component )
    {
        components.add( component );
        component.setParent( this );

        return this;
    }

    public GameObject addChild( GameObject child )
    {
        children.add( child );
        child.getTransform().setParent( getTransform() );
        return this;
    }

    public void init()
    {
        for ( GameComponent component: components )
            component.init();
    }

    public void update( float deltaTime )
    {
        super.update( deltaTime );
        getTransform().update( deltaTime );

        for ( GameComponent component: components )
            component.update( deltaTime );
    }

    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        for ( GameComponent component: components )
            component.render( shader, renderingEngine );
    }

    @Override
    public void simulate( float deltaTime )
    {
        super.simulate( deltaTime );
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
        simulate( deltaTime );

        for ( GameObject object : children )
            object.simulate( deltaTime );
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
            engine.getPhysicsEngine().addPhysicObject( getPhysicsObject() );

            for ( GameComponent component: components )
                component.addToEngine( engine );

            for( GameObject child: children )
                child.setEngine( engine );
        }
    }
}
