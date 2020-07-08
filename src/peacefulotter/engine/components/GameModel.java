package peacefulotter.engine.components;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.elementary.Initializable;
import peacefulotter.engine.elementary.Renderable;
import peacefulotter.engine.elementary.Simulatable;
import peacefulotter.engine.elementary.Updatable;
import peacefulotter.engine.physics.colliders.BoundingSphere;
import peacefulotter.engine.physics.colliders.BoundingSpherePhysicsObject;
import peacefulotter.engine.physics.colliders.PhysicsObject;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

public abstract class GameModel implements Initializable, Updatable, Renderable, Simulatable
{
    private final STransform transform = new STransform();
    private final PhysicsObject physicsObject;

    public GameModel() { this( Vector3f.getZero() ); }
    public GameModel( Vector3f vel )
    {
        physicsObject = new BoundingSpherePhysicsObject( Vector3f.getZero(), vel, 6 );
    }

    @Override
    public void update( float deltaTime )
    {
        //if ( !physicsObject.getVelocity().equals( Vector3f.getZero() ) )
        //    transform.setTranslation( physicsObject.getPosition() );
    }

    @Override
    public void simulate( float deltaTime )
    {
        if ( !physicsObject.getVelocity().equals( Vector3f.getZero() ) )
        {
            Vector3f newPos = transform.getTranslation().add(
                    physicsObject.getVelocity().mul( deltaTime ) );
            transform.setTranslation( newPos );
            physicsObject.setPosition( newPos );
        }

    }

    public STransform getTransform() { return transform; }
    public PhysicsObject getPhysicsObject() { return physicsObject; }
}
