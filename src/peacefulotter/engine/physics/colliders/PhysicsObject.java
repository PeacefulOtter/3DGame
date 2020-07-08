package peacefulotter.engine.physics.colliders;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.elementary.Interactable;
import peacefulotter.engine.elementary.Interactor;
import peacefulotter.engine.physics.InteractionHandler;

public abstract class PhysicsObject implements Interactable, Interactor
{
    private final InteractionHandler handler;

    private Vector3f position;
    private Vector3f velocity;
    private float radius;

    public PhysicsObject( Vector3f velocity )
    {
        this( Vector3f.getZero(), velocity );
    }

    public PhysicsObject( Vector3f position, Vector3f velocity )
    {
        this.position = position;
        this.velocity = velocity;
        this.handler = new ObjectInteractionHandler();
    }

    abstract public Collider getCollider();

    @Override
    public void interactWith( Interactable other )
    {
        //System.out.println( "interact with");
        other.acceptInteraction( handler );
    }

    @Override
    public void acceptInteraction( InteractionHandler other )
    {
        //System.out.println("accepting interaction");
        other.interactWith( this );
    }

    private class ObjectInteractionHandler implements InteractionHandler
    {
        // public void interactWith( PhysicsObjectHerited other );

        @Override
        public void interactWith( PhysicsObject other )
        {
            if ( !getVelocity().equals( Vector3f.getZero() ) )
                PhysicsObject.this.velocity = PhysicsObject.this.velocity.mul( -1 );
            if ( !other.getVelocity().equals( Vector3f.getZero() ) )
                other.setVelocity( other.getVelocity().mul( -1 ) );
        }
    }

    public Vector3f getPosition() { return position; }
    public void setPosition( Vector3f position ) { this.position = position;  }

    public Vector3f getVelocity() { return velocity; }
    public void setVelocity( Vector3f velocity ) { this.velocity = velocity; }

    public float getRadius() { return radius; }
    public void setRadius( float radius ) { this.radius = radius; }
}
