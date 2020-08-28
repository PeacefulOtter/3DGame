package peacefulotter.engine.components;

import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.elementary.Initializable;
import peacefulotter.engine.elementary.Renderable;
import peacefulotter.engine.elementary.Updatable;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

/**
 * A Component = no children, and transformation is partially based on the parent transformation
 */
public abstract class GameComponent implements Initializable, Updatable, Renderable
{
    private final STransform componentTransform = new STransform();

    private boolean hasParent;

    @Override
    public void init() { }

    @Override
    public void update( float deltaTime ) { }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine ) { }

    public void setParent( STransform parent )
    {
        if ( parent != null )
        {
            parent.addChild( componentTransform );
            hasParent = true;
        } else { hasParent = false; }
    }

    public STransform getTransform()
    {
        if ( !hasParent )
            throw new NullPointerException( "GameComponents must be added to a GameObject before getting its Transformation" );

        return componentTransform;
    }

    public void addToEngine( CoreEngine engine ) { }

    public STransform getComponentTransform() { return componentTransform; }
}
