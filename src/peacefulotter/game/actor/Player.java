package peacefulotter.game.actor;

import peacefulotter.engine.components.Camera;
import peacefulotter.engine.components.MeshRenderer;
import peacefulotter.engine.components.PhysicsObject;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.Mesh;
import peacefulotter.engine.utils.IO.Input;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static peacefulotter.engine.utils.IO.Input.*;

// returns true if the player is at his max velocity
// i.e. the length of the vector (VelocityX, 0, VelocityZ) is less than the maxVelocity
/*private boolean reachedMaxVelocity( Vector3f velocity )
        {
        return new Vector3f( velocity.getX(), 0, velocity.getZ() ).length() >= currentMaxVelocity;
        }
    */

// player is on the ground and stopped accelerating, then slow him down
        /*if ( !isJumping && !isMoving() )
        {
            Vector3f invDirection = getVelocity().rotate( Vector3f.Y_AXIS, 180 )
                    .mul( deltaTime * SLOW_FACTOR );
            getVelocity().setX( getVelocity().getX() + invDirection.getX() );
            getVelocity().setZ( getVelocity().getZ() + invDirection.getZ() );
        }*/

/* MOVE
        float accAmount = deltaTime * currentAcceleration;
        Vector3f newVelocity = getVelocity().add( direction.mul( accAmount ) );

        if ( reachedMaxVelocity( newVelocity ) )
        {
            setVelocity( newVelocity.resizeTo( currentMaxVelocity ) );
        }
        else if ( !reachedMaxVelocity( getVelocity() ) || !movingArrows.contains( arrow ) )
        {
            setVelocity( newVelocity );
            arrow.fast( accAmount );
        }
        */

/* stopMoving()
 setVelocity( getVelocity().sub( direction.mul( arrow.velocityLength ) ) );
 arrow.reset();
 */


public class Player extends PhysicsObject
{
    private static final float JUMP_HEIGHT = 40f;
    private static final float MOVEMENT_ACCELERATION = 3000f;
    private static final float RUNNING_ACCELERATION = 4000f;
    private static final float MAX_WALKING_VELOCITY = 5000f;
    private static final float MAX_RUNNING_VELOCITY = 9000f;
    private static final float SLOW_FACTOR = 4000f;
    private static final float ROTATION_SENSITIVITY = 180f;
    private static final float CURSOR_SENSITIVITY = 50f;

    private double oldCursorX, oldCursorY;
    private boolean isRunning, isReloading, isJumping, isCrouching;
    private final Weapon weapon;
    private final boolean isUser;
    private float currentAcceleration, currentVelocity, currentMaxVelocity;
    private final Set<VelocityAngle> movingArrows, notMovingArrowsQueue;

    private Player( Weapon weapon, boolean isUser )
    {
        super( Vector3f.getZero() );
        this.weapon = weapon;
        this.isUser = isUser;
        this.currentAcceleration = MOVEMENT_ACCELERATION;
        this.currentMaxVelocity = MAX_WALKING_VELOCITY;
        this.movingArrows = new HashSet<>( 4 );
        this.notMovingArrowsQueue = new HashSet<>( 4 );
    }

    @Override
    public void init()
    {
        if ( !isUser ) return;

        Input.addKeyPressReleaseCallbacks( GLFW_KEY_W,
                ( deltaTime ) -> move( VelocityAngle.FORWARD ),
                ( deltaTime ) -> stopMoving( VelocityAngle.FORWARD ) );
        Input.addKeyPressReleaseCallbacks( GLFW_KEY_D,
                ( deltaTime ) -> move( VelocityAngle.RIGHT ),
                ( deltaTime ) -> stopMoving( VelocityAngle.RIGHT ) );
        Input.addKeyPressReleaseCallbacks( GLFW_KEY_S,
                ( deltaTime ) -> move( VelocityAngle.BACKWARD ),
                ( deltaTime ) -> stopMoving( VelocityAngle.BACKWARD ) );
        Input.addKeyPressReleaseCallbacks( GLFW_KEY_A,
                ( deltaTime ) -> move( VelocityAngle.LEFT  ),
                ( deltaTime ) -> stopMoving( VelocityAngle.LEFT ) );
        Input.addKeyPressReleaseCallbacks( GLFW_KEY_LEFT_CONTROL,
                ( deltaTime ) -> crouch(),
                ( deltaTime ) -> stopCrouching() );
        Input.addKeyPressReleaseCallbacks( GLFW_KEY_LEFT_SHIFT,
                ( deltaTime ) -> run(),
                ( deltaTime ) -> stopRunning() );
        Input.addKeyCallback( GLFW_KEY_SPACE, ( deltaTime ) -> jump(), false );

        Input.addKeyCallback( GLFW_KEY_UP,    ( deltaTime ) -> rotateX( -deltaTime * ROTATION_SENSITIVITY ) );
        Input.addKeyCallback( GLFW_KEY_RIGHT, ( deltaTime ) -> rotateY(  deltaTime * ROTATION_SENSITIVITY ) );
        Input.addKeyCallback( GLFW_KEY_DOWN,  ( deltaTime ) -> rotateX(  deltaTime * ROTATION_SENSITIVITY ) );
        Input.addKeyCallback( GLFW_KEY_LEFT,  ( deltaTime ) -> rotateY( -deltaTime * ROTATION_SENSITIVITY ) );

        Input.addMouseButtonCallback( MOUSE_PRIMARY,
                ( deltaTime ) -> weapon.fire( getForward() ),
                Weapon.IS_AUTOMATIC );

        Input.addMouseButtonCallback( MOUSE_SECONDARY, ( deltaTime ) -> {
            System.out.println("aiminggg");
        } );

        Input.addCursorCallback( ( deltaTime, x, y ) -> {
            if ( x != oldCursorX )
            {
                rotateY( (float) -( oldCursorX - x ) * CURSOR_SENSITIVITY * deltaTime );
                oldCursorX = x;
            }

            else if ( y != oldCursorY )
            {
                rotateX( (float) -( oldCursorY - y ) * CURSOR_SENSITIVITY * deltaTime );
                oldCursorY = y;
            }
        } );


        Input.addKeyCallback( GLFW_KEY_LEFT_ALT,  ( deltaTime ) -> Input.setMouseDisable( false ) );
        Input.addKeyCallback( GLFW_KEY_R,  ( deltaTime ) -> reloadWeapon() );
    }

    @Override
    protected void updateVelocity( float deltaTime )
    {
        super.updateVelocity( deltaTime );

        // player is on the ground or slightly below
        if ( getPosition().getY() <= 0 && getVelocityYAxis() <= 0 )
        {
            isJumping = false;

            notMovingArrowsQueue.forEach( this::stopMoving );
            notMovingArrowsQueue.clear();
        }

        float newAngle = ( isMoving() && !isJumping ) ? VelocityAngle.getAngle() : calcActualAngle();

        setVelocity( getForward()
                .rotate( Vector3f.Y_AXIS, newAngle )
                .mul( deltaTime * currentVelocity )
                .setY( getVelocityYAxis() ) );

        if ( isJumping ) return;

        if ( isMoving() )
        {
            if ( currentVelocity < currentMaxVelocity )
                currentVelocity += currentAcceleration * deltaTime;
            if ( currentVelocity > currentMaxVelocity )
                currentVelocity = currentMaxVelocity;
        }
        else
        {
            if ( currentVelocity > 0 )
                currentVelocity -= SLOW_FACTOR * deltaTime;
            if ( currentVelocity < 0 )
                currentVelocity = 0;
        }
    }

    // keeps track of the velocity of each direction
    private enum VelocityAngle
    {
        FORWARD( 0 ), RIGHT( 90 ), BACKWARD( 180 ), LEFT( -90 );

        private final int rotation;
        private boolean active;

        VelocityAngle( int rotation )
        {
            this.rotation = rotation;
        }

        public static float getAngle()
        {
            int angle = 0;

            if ( RIGHT.active )
                angle += RIGHT.rotation;
            if ( LEFT.active )
                angle += LEFT.rotation;
            if ( FORWARD.active && !BACKWARD.active )
            {
                angle += Math.signum( -angle ) * 45;
            }
            else if ( BACKWARD.active && !FORWARD.active )
            {
                angle += Math.signum( angle ) * 45;
                if ( angle == 0 )
                    angle = 180;
            }
            return angle;
        }
    }

    private float calcActualAngle()
    {
        Vector2f v2 = getVelocity().getXZ().normalize();
        Vector2f v1 = getForward().getXZ();
        float actualAngle = (float) Math.atan2( v1.getY(), v1.getX() ) - (float) Math.atan2( v2.getY(), v2.getX() );
        actualAngle *= 180f / (float) Math.PI;
        return actualAngle;
    }

    private boolean isMoving()
    {
        return movingArrows.size() > 0;
    }

    private void move( VelocityAngle arrow )
    {
        if ( isJumping ) return;

        movingArrows.add( arrow );
        arrow.active = true;
    }

    private void stopMoving( VelocityAngle arrow )
    {
        // since this method is only triggered once when a key is released, if the player jumps
        // its movement continues until he lands. This is the reason why we need to keep track
        // of the movements while in air, and apply the stop only once he lands.
        if ( isJumping )
        {
            notMovingArrowsQueue.add( arrow );
            return;
        }

        movingArrows.remove( arrow );
        arrow.active = false;
    }

    private void run()
    {
        if ( isJumping ) return;

        currentAcceleration = RUNNING_ACCELERATION;
        currentMaxVelocity = MAX_RUNNING_VELOCITY;
        isRunning = true;
    }

    private void stopRunning()
    {
        if ( isJumping ) return;

        currentAcceleration = MOVEMENT_ACCELERATION;
        currentMaxVelocity = MAX_WALKING_VELOCITY;
        this.isRunning = false;
    }

    private void jump()
    {
        if ( isJumping ) return;

        setVelocityYAxis( getVelocityYAxis() + JUMP_HEIGHT );
        isJumping = true;
    }

    private void crouch()
    {
        // ...
        isCrouching = true;
    }

    private void stopCrouching() { this.isCrouching = false; }

    private void reloadWeapon() { weapon.reload(); }

    private void rotateX( float angleDeg ) { getTransform().rotate( getRight(), angleDeg ); }

    private void rotateY( float angleDeg ) { getTransform().rotate( Vector3f.Y_AXIS, angleDeg ); }

    public Vector3f getForward()  { return getTransform().getRotation().getForward(); }
    public Vector3f getBackward() { return getTransform().getRotation().getBack(); }
    public Vector3f getRight()    { return getTransform().getRotation().getRight(); }
    public Vector3f getLeft()     { return getTransform().getRotation().getLeft(); }


    public static class PlayerBuilder
    {
        private Weapon weapon;
        private Camera camera;
        private Mesh mesh;
        private Material material;

        public PlayerBuilder setWeapon( Weapon weapon )
        {
            this.weapon = weapon;
            return this;
        }

        public PlayerBuilder setCamera( Camera camera )
        {
            this.camera = camera;
            return this;
        }

        public PlayerBuilder setMesh( Mesh mesh )
        {
            this.mesh = mesh;
            return this;
        }

        public PlayerBuilder setMaterial( Material material )
        {
            this.material = material;
            return this;
        }

        public Player build( boolean isUser )
        {
            if ( weapon == null )
                throw new NullPointerException( "Player needs a weapon" );
            if ( mesh == null || material == null )
                throw new NullPointerException( "Player needs a mesh and a material" );


            Player player = new Player( weapon, isUser );
            player
                    .addComponent( new MeshRenderer( mesh, material ) )
                    .addChild( weapon );

            if ( camera != null )
            {
                player.addComponent( camera );
                camera.setInnerTranslation( Camera.PLAYER_CAMERA() );
            }

            return player;
        }
    }
}
