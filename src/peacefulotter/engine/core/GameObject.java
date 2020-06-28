package peacefulotter.engine.core;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.core.elementary.Initializable;
import peacefulotter.engine.core.elementary.Renderable;
import peacefulotter.engine.core.elementary.Updatable;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

import java.util.HashSet;
import java.util.Set;

public class GameObject implements Initializable, Updatable, Renderable
{
    private final Set<GameComponent> components = new HashSet<>();
    private final Set<GameObject> children = new HashSet<>();

    private STransform transform;
    private CoreEngine engine;

    public GameObject()
    {
        this.transform = new STransform();
    }

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

    public void render( Shader shader )
    {
        for ( GameComponent component: components )
            component.render( shader );

        for ( GameObject child: children )
            child.render( shader );
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
