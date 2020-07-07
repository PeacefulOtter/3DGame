package peacefulotter.engine.physics.colliders;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.elementary.Interactable;
import peacefulotter.engine.elementary.Interactor;
import peacefulotter.engine.physics.InteractionHandler;

public class PhysicsObject implements Interactable, Interactor
{
    private Vector3f position;
    private Vector3f velocity;
    private float radius;
    private InteractionHandler handler;

    public PhysicsObject( Vector3f velocity )
    {
        this( Vector3f.getZero(), velocity, 6 );
    }

    public PhysicsObject( Vector3f position, Vector3f velocity, float radius )
    {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.handler = new ObjectInteractionHandler();
    }

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

    public BoundingSphere getBoundingSphere()
    {
        return new BoundingSphere( position, radius );
    }

    private static class ObjectInteractionHandler implements InteractionHandler
    {
        @Override
        public void interactWith( PhysicsObject other )
        {
            //v System.out.println("interaction Handler");
        }
    }

    public Vector3f getPosition() { return position; }
    public void setPosition( Vector3f position ) { this.position = position; }

    public Vector3f getVelocity() { return velocity; }
    public void setVelocity( Vector3f velocity ) { this.velocity = velocity; }

    public float getRadius() { return radius; }
    public void setRadius( float radius ) { this.radius = radius; }
}
