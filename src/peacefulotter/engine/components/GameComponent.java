package peacefulotter.engine.components;

import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.elementary.Initializable;
import peacefulotter.engine.elementary.Renderable;
import peacefulotter.engine.elementary.Updatable;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;
import peacefulotter.engine.utils.Logger;

/**
 * A Component = no children, and transformation is based on the parent transformation
 */
public abstract class GameComponent implements Initializable, Updatable, Renderable
{
    private GameObject parent;
    private STransform actualTransform;
    private Vector3f innerTranslation = Vector3f.getZero();
    private Quaternion innerRotation = new Quaternion( 0, 0, 0, 1 );
    private float innerScale = 1;

    @Override
    public void init() { }

    @Override
    public void update( float deltaTime ) { }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine ) { }

    public void setParent( GameObject parent ) { this.parent = parent; }

    public boolean hasParent() { return parent != null; }

    public STransform getTransform()
    {
        if ( !hasParent() )
            throw new NullPointerException( "GameComponents must be added to a GameObject before getting its Transformation" );

        if ( actualTransform == null || actualTransform.hasChanged() )
            actualTransform = new STransform( parent.getTransform() )
                                .translate( innerTranslation )
                                .rotate( innerRotation )
                                .scale( innerScale );
        return actualTransform;
    }

    public void addToEngine( CoreEngine engine ) { }


    public void setInnerTranslation( Vector3f innerTranslation )
    {
        this.innerTranslation = innerTranslation;
    }

    public void setInnerRotation( Quaternion innerRotation )
    {
        this.innerRotation = innerRotation;
    }

    public void setInnerScale( float percentage )
    {
        this.innerScale = percentage;
    }
}
