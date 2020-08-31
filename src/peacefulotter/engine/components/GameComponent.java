package peacefulotter.engine.components;

import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.elementary.Initializable;
import peacefulotter.engine.elementary.Renderable;
import peacefulotter.engine.elementary.Updatable;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.core.transfomations.STransform;

/**
 * Represents the most basic object of the engine, something that can be initialized, updated and rendered.
 * It has its own transformation to be able to manipulate the component in the 3D space.
 */
public abstract class GameComponent implements Initializable, Updatable, Renderable
{
    private final STransform transform = new STransform();
    private boolean fixedTilt;

    @Override
    public void init() { }

    @Override
    public void update( float deltaTime )
    {
        transform.update( deltaTime );
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine ) { }

    public void addToEngine( CoreEngine engine ) { }


    public GameComponent fixedTilt() { fixedTilt = true; return this; }

    public STransform getTransform()
    {
        if ( !fixedTilt ) return transform;

        Vector3f v1 = Vector3f.Y_AXIS;
        Vector3f v2;
        if ( transform.hasParent() )
            v2 = transform.getParent().getRotation().getForward();
        else
            v2 = transform.getRotation().getForward();

        transform.setRotation( new Quaternion(
                transform.getRotation().getRight(),
                -Vector3f.calcAngle( v1, v2 ) + 90 ) );

        return transform;
    }
}
