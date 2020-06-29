package peacefulotter.engine.components;

import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.elementary.Initializable;
import peacefulotter.engine.elementary.Renderable;
import peacefulotter.engine.elementary.Updatable;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

import java.util.HashSet;
import java.util.Set;

public class GameObject implements Initializable, Updatable, Renderable
{
    private final Set<GameComponent> components = new HashSet<>();
    private final Set<GameObject> children = new HashSet<>();
    private final STransform transform = new STransform();

    private CoreEngine engine;


    public GameObject addComponent( GameComponent component )
    {
        components.add( component );
        component.setParent( this );

        return this;
    }

    public GameObject addChild( GameObject child )
    {
        children.add( child );
        child.setEngine( engine );
        child.getTransform().setParent( transform );

        return this;
    }

    public void init()
    {
        for ( GameComponent component: components )
            component.init();

        for ( GameObject child: children )
            child.init();
    }

    public void update( float deltaTime )
    {
        for ( GameComponent component: components )
            component.update( deltaTime );

        for ( GameObject child: children )
            child.update( deltaTime );
    }

    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        for ( GameComponent component: components )
            component.render( shader, renderingEngine );

        for ( GameObject child: children )
            child.render( shader, renderingEngine );
    }

    public STransform getTransform() { return transform; }

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
}
