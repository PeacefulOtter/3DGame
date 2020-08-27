package peacefulotter.engine.components;

import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.elementary.Interactable;
import peacefulotter.engine.elementary.Interactor;
import peacefulotter.engine.elementary.Simulatable;
import peacefulotter.engine.physics.InteractionHandler;
import peacefulotter.engine.physics.IntersectData;
import peacefulotter.engine.physics.colliders.BoundingSphere;
import peacefulotter.engine.physics.colliders.Collider;
import peacefulotter.engine.utils.Logger;
import peacefulotter.game.actor.VelocityAngle;


/*
Vector3f direction = intersectData.getDirection().normalize();
            Vector3f otherDirection = direction.reflect( getVelocity() ).normalize();
            if ( !getVelocity().equals( Vector3f.getZero() ) )
                setVelocity( getVelocity().reflect( direction ) );
            if ( !other.getVelocity().equals( Vector3f.getZero() ) )
                other.setVelocity( other.getVelocity().reflect( otherDirection ) );

 */

public class PhysicsObject extends GameObject implements Simulatable, Interactable, Interactor
{
    private static final float GRAVITY = 48f;

    private final InteractionHandler handler;
    private final Vector3f velocity;
    private final float maxYVelocity;

    private Vector3f position;
    private Collider collider;
    private float velocityYAxis;

    public PhysicsObject()
    {
        this( Vector3f.getZero(), Vector3f.getZero() );
    }

    public PhysicsObject( Vector3f velocity )
    {
        this( Vector3f.getZero(), velocity );
    }

    public PhysicsObject( Vector3f position, Vector3f velocity )
    {
        this( position, velocity, 50f );
    }

    public PhysicsObject( Vector3f velocity, float maxYVelocity )
    {
        this( Vector3f.getZero(), velocity, maxYVelocity );
    }

    public PhysicsObject( Vector3f position, Vector3f velocity, float maxYVelocity )
    {
        this.position = position;
        this.velocity = velocity;
        this.handler = new ObjectInteractionHandler();
        this.collider = new BoundingSphere( position, 6 );
        this.velocityYAxis = 0;
        this.maxYVelocity = maxYVelocity;
    }

    public void setCollider( Collider collider ) { this.collider = collider; }

    public Collider getCollider() { return collider; }


    @Override
    public PhysicsObject addChild( GameObject child )
    {
        super.addChild( child );
        return this;
    }

    @Override
    public PhysicsObject addPhysicalChild( PhysicsObject child )
    {
        getCoreEngine().getPhysicsEngine().addPhysicObject( child );
        super.addChild( child );
        return this;
    }

    @Override
    public PhysicsObject addComponent( GameComponent component )
    {
        super.addComponent( component );
        return this;
    }

    @Override
    public void update( float deltaTime )
    {
        super.update( deltaTime );
        updateVelocity( deltaTime );
    }

    protected void updateVelocity( float deltaTime )
    {
        // object is on the ground or slightly below
        // !!!! GET THE MAP HEIGHT
        int height = 0;
        if ( position.getY() <= height && velocityYAxis <= 0 )
        {
             velocity.setY( height );
            position.setY( height );
            velocityYAxis = 0;
        }
        // else, object is in the air : apply gravity
        else
        {
            velocityYAxis -= GRAVITY * deltaTime;
            if ( velocityYAxis < -maxYVelocity )
                velocityYAxis = -maxYVelocity;
        }
    }

    @Override
    public void updateAll( float deltaTime )
    {
        update( deltaTime );
        super.updateAll( deltaTime );
    }

    @Override
    public void simulate( float deltaTime )
    {
        Vector3f newPos = getTransform().getTranslation().add( velocity.mul( deltaTime ) );
        getTransform().setTranslation( newPos );
        setPosition( newPos );
        collider.setPosition( position );
    }

    public void simulateAll( float deltaTime )
    {
        simulate( deltaTime );

        for ( PhysicsObject object : getPhysicsChildren() )
            object.simulate( deltaTime );
    }

    @Override
    public void setEngine( CoreEngine engine )
    {
        if ( getCoreEngine() != engine )
        {
            super.setEngine( engine );
            engine.getPhysicsEngine().addPhysicObject( this );
        }
    }

    @Override
    public void interactWith( Interactable other, IntersectData intersectData )
    {
        //System.out.println( "interact with");
        other.acceptInteraction( handler, intersectData );
    }

    @Override
    public void acceptInteraction( InteractionHandler other, IntersectData intersectData )
    {
        //System.out.println("accepting interaction");
        other.interactWith( this, intersectData );
    }

    private static class ObjectInteractionHandler implements InteractionHandler
    {
        // public void interactWith( PhysicsObjectHerited other );

        @Override
        public void interactWith( PhysicsObject other, IntersectData intersectData )
        {
            //System.out.println("interactWith");
        }
    }

    public boolean isMoving() { return false; }
    public boolean move( VelocityAngle arrow ) { return true; }
    public boolean stopMoving( VelocityAngle arrow ) { return true; }

    public Vector3f getPosition() { return position; }
    public void setPosition( Vector3f position ) { this.position = position;  }

    public Vector3f getVelocity() { return velocity; }
    public void setVelocity( Vector3f velocity ) { this.velocity.set( velocity ); }

    public float getVelocityYAxis() { return velocityYAxis; }
    public void setVelocityYAxis( float velocity ) { velocityYAxis = velocity; }
}
