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

    public Collider getCollider() { return new BoundingSphere( position, 6 ); }


    @Override
    public PhysicsObject addChild( GameObject child )
    {
        super.addChild( child );
        return this;
    }

    @Override
    public PhysicsObject addPhysicalChild( PhysicsObject child )
    {
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
    public void simulate( float deltaTime )
    {
        if ( !velocity.equals( Vector3f.getZero() ) )
        {
            Vector3f newPos = getTransform().getTranslation().add( velocity.mul( deltaTime ) );
            getTransform().setTranslation( newPos );
            setPosition( newPos );
        }
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
    public void interactWith(Interactable other, IntersectData intersectData)
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

        }
    }

    public Vector3f getPosition() { return position; }
    public void setPosition( Vector3f position ) { this.position = position;  }

    public Vector3f getVelocity() { return velocity; }
    public void setVelocity( Vector3f velocity ) { this.velocity = velocity; }

    public float getRadius() { return radius; }
    public void setRadius( float radius ) { this.radius = radius; }
}
