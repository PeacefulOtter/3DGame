package peacefulotter.game.actor;

import peacefulotter.engine.components.PhysicsObject;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.utils.IO.Input;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static peacefulotter.engine.utils.IO.Input.*;

public class Player extends PhysicsObject
{
    private static final float GRAVITY = 55f;
    private static final float JUMP_HEIGHT = 20f;
    private static final float MAX_WALKING_VELOCITY = 10f;
    private static final float MAX_RUNNING_VELOCITY = 30f;
    private static final float SLOW_FACTOR = 15f;
    private static final float ROTATION_SENSITIVITY = 180f;
    private static final Vector3f Y_AXIS = new Vector3f( 0, 1, 0 );

    private final float movingSensitivity, runningSensitivity;
    private boolean isMoving, isRunning, isReloading, isJumping, isCrouching;

    private final Weapon weapon;
    private float actualMovementSensitivity, maxVelocity;

    public Player()
    {
        this( 50f, 70f );
    }

    public Player( float movingSensitivity, float runningSensitivity )
    {
        super( Vector3f.getZero() );
        this.movingSensitivity = movingSensitivity;
        this.runningSensitivity = runningSensitivity;
        this.actualMovementSensitivity = movingSensitivity;
        this.maxVelocity = MAX_WALKING_VELOCITY;
        this.weapon = new Weapon();
        addComponent( weapon );
    }

    @Override
    public void init()
    {
        Input.addKeyPressReleaseCallbacks( GLFW_KEY_W,
                ( deltaTime ) -> move( getForward().setY(0), deltaTime ),
                ( deltaTime ) -> stopMoving() );
        Input.addKeyPressReleaseCallbacks( GLFW_KEY_D,
                ( deltaTime ) -> move( getRight(), deltaTime ),
                ( deltaTime ) -> stopMoving() );
        Input.addKeyPressReleaseCallbacks( GLFW_KEY_S,
                ( deltaTime ) -> move( getBackward().setY(0), deltaTime ),
                ( deltaTime ) -> stopMoving() );
        Input.addKeyPressReleaseCallbacks( GLFW_KEY_A,
                ( deltaTime ) -> move( getLeft(), deltaTime ),
                ( deltaTime ) -> stopMoving() );
        Input.addKeyPressReleaseCallbacks( GLFW_KEY_LEFT_CONTROL,
                ( deltaTime ) -> crouch(),
                ( deltaTime ) -> stopCrouching() );
        Input.addKeyPressReleaseCallbacks( GLFW_KEY_LEFT_SHIFT,
                ( deltaTime ) -> run(),
                ( deltaTime ) -> stopRunning() );
        Input.addKeyCallback( GLFW_KEY_SPACE, ( deltaTime ) -> jump() );

        Input.addKeyCallback( GLFW_KEY_UP,    ( deltaTime ) -> rotateX( -deltaTime * ROTATION_SENSITIVITY ) );
        Input.addKeyCallback( GLFW_KEY_RIGHT, ( deltaTime ) -> rotateY(  deltaTime * ROTATION_SENSITIVITY ) );
        Input.addKeyCallback( GLFW_KEY_DOWN,  ( deltaTime ) -> rotateX(  deltaTime * ROTATION_SENSITIVITY ) );
        Input.addKeyCallback( GLFW_KEY_LEFT,  ( deltaTime ) -> rotateY( -deltaTime * ROTATION_SENSITIVITY ) );

        addMouseCallback( MOUSE_PRIMARY, ( deltaTime ) -> {
            Bullet b = weapon.fire( getForward() );
            if ( b != null ) addPhysicalChild( b );
        } );
        addMouseCallback( MOUSE_SECONDARY, ( deltaTime ) -> {
            System.out.println("aiminggg");
        } );
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        super.render( shader, renderingEngine );
        weapon.render( shader, renderingEngine );
    }

    @Override
    public void simulate( float deltaTime )
    {
        super.simulate( deltaTime );
        if ( getPosition().getY() <= 0 )
        {
            setVelocity( getVelocity().setY( 0 ) );
            setPosition( getPosition().setY( 0 ) );
            isJumping = false;
        }
        else
            setVelocity( getVelocity().setY( getVelocity().getY() - GRAVITY * deltaTime ) );

        if ( !isMoving || !checkMaxVelocity( getVelocity() ) )
        {
            Vector3f invDirection = getVelocity().rotate( Y_AXIS, 180 ).mul( deltaTime * SLOW_FACTOR );
            getVelocity().setX( getVelocity().getX() + invDirection.getX() );
            getVelocity().setZ( getVelocity().getZ() + invDirection.getZ() );
        }
    }

    // returns true if the player is not at his max velocity
    private boolean checkMaxVelocity( Vector3f velocity )
    {
        return Math.abs( velocity.getX() ) <= maxVelocity && Math.abs( velocity.getZ() ) <= maxVelocity;
    }


    public void move( Vector3f direction, float deltaTime )
    {
        if ( isJumping ) return;

        Vector3f newVelocity = getVelocity().add( direction.mul( deltaTime * actualMovementSensitivity ) );
        if ( checkMaxVelocity( newVelocity ) )
            setVelocity( newVelocity );
        isMoving = true;
    }

    public void stopMoving() { this.isMoving = false; }

    private void run()
    {
        if ( isJumping ) return;

        actualMovementSensitivity = runningSensitivity;
        maxVelocity = MAX_RUNNING_VELOCITY;
        isRunning = true;
    }

    public void stopRunning()
    {
        actualMovementSensitivity = movingSensitivity;
        maxVelocity = MAX_WALKING_VELOCITY;
        this.isRunning = false;
    }

    private void jump()
    {
        if ( isJumping ) return;

        Vector3f velocity = getVelocity();
        velocity.setY( velocity.getY() + JUMP_HEIGHT );
        setVelocity( velocity );
        isJumping = true;
    }

    private void crouch()
    {
        // ...
        isCrouching = true;
    }

    private void stopCrouching() { this.isCrouching = false; }

    private void rotateX( float angleDeg ) { getTransform().rotate( getRight(), angleDeg ); }

    private void rotateY( float angleDeg ) { getTransform().rotate( Y_AXIS, angleDeg ); }

    public Vector3f getForward()  { return getTransform().getRotation().getForward(); }
    public Vector3f getBackward() { return getTransform().getRotation().getBack(); }
    public Vector3f getUp()       { return getTransform().getRotation().getUp(); }
    public Vector3f getDown()     { return getTransform().getRotation().getDown(); }
    public Vector3f getRight()    { return getTransform().getRotation().getRight(); }
    public Vector3f getLeft()     { return getTransform().getRotation().getLeft(); }
}
