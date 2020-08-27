package peacefulotter.game.actor;

import peacefulotter.engine.components.Camera;
import peacefulotter.engine.components.MeshRenderer;
import peacefulotter.engine.components.PhysicsObject;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.Mesh;
import peacefulotter.engine.utils.IO.Input;
import peacefulotter.game.inputs.FreeMovement;
import peacefulotter.game.inputs.FreeRotation;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;
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



public class Player extends PhysicsObject
{
    private static final float JUMP_HEIGHT = 40f;
    private static final float RUNNING_ACCELERATION = 4000f;
    private static final float MAX_RUNNING_VELOCITY = 9000f;

    public static final float MAX_WALKING_VELOCITY = 5000f;
    public static final float WALKING_ACCELERATION = 3000f;
    public static final float ROTATION_SENSITIVITY = 180f;
    public static final float CURSOR_SENSITIVITY = 50f;
    public static final float SLOW_FACTOR = 4000f;

    private final Weapon weapon;
    private final boolean isUser;
    private final Set<VelocityAngle> notMovingArrowsQueue;
    private final FreeMovement freeMovement;
    private final FreeRotation freeRotation;

    private boolean isRunning, isReloading, isJumping, isCrouching;

    private Player( Weapon weapon, boolean isUser )
    {
        this.weapon = weapon;
        this.isUser = isUser;
        this.notMovingArrowsQueue = new HashSet<>( 4 );
        if ( isUser )
        {
            this.freeMovement = new FreeMovement( this, MAX_WALKING_VELOCITY, WALKING_ACCELERATION, SLOW_FACTOR );
            this.freeRotation = new FreeRotation( this, ROTATION_SENSITIVITY, CURSOR_SENSITIVITY );
        }
        else { this.freeMovement = null; this.freeRotation = null; }
    }

    @Override
    public void init()
    {
        if ( !isUser ) return;
        assert freeMovement != null;
        assert freeRotation != null;

        freeMovement.init();
        freeRotation.init();

        Input.addKeyPressReleaseCallbacks( GLFW_KEY_LEFT_CONTROL,
                ( deltaTime ) -> crouch(),
                ( deltaTime ) -> stopCrouching() );
        Input.addKeyPressReleaseCallbacks( GLFW_KEY_LEFT_SHIFT,
                ( deltaTime ) -> run(),
                ( deltaTime ) -> stopRunning() );
        Input.addKeyCallback( GLFW_KEY_SPACE, ( deltaTime ) -> jump(), false );

        Input.addMouseButtonCallback( MOUSE_PRIMARY,
                ( deltaTime ) -> weapon.fire( getForward() ),
                Weapon.IS_AUTOMATIC );

        Input.addMouseButtonCallback( MOUSE_SECONDARY, ( deltaTime ) ->
            System.out.println("aiminggg")
        );


        Input.addKeyCallback( GLFW_KEY_LEFT_ALT,  ( deltaTime ) -> Input.setMouseDisable( false ) );
        Input.addKeyCallback( GLFW_KEY_R,  ( deltaTime ) -> reloadWeapon() );
    }

    @Override
    protected void updateVelocity( float deltaTime )
    {
        if ( !isUser ) return;
        assert freeMovement != null;

        super.updateVelocity( deltaTime );

        // player is on the ground or slightly below
        if ( getPosition().getY() <= 0 && getVelocityYAxis() <= 0 )
        {
            isJumping = false;

            notMovingArrowsQueue.forEach( freeMovement::stopMoving );
            notMovingArrowsQueue.clear();
        }

        freeMovement.updateVelocity( deltaTime, isJumping );
    }

    @Override
    public boolean move( VelocityAngle arrow )
    {
        return !isJumping;
    }
    @Override
    public boolean stopMoving( VelocityAngle arrow )
    {
        // since this method is only triggered once when a key is released, if the player jumps
        // its movement continues until he lands. This is the reason why we need to keep track
        // of the movements while in air, and apply the stop only once he lands.
        if ( isJumping )
        {
            notMovingArrowsQueue.add( arrow );
            return false;
        }
        return true;
    }

    private void run()
    {
        if ( isJumping ) return;

        freeMovement.setCurrentMaxVelocity( MAX_RUNNING_VELOCITY );
        freeMovement.setCurrentAcceleration( RUNNING_ACCELERATION );
        isRunning = true;
    }

    private void stopRunning()
    {
        if ( isJumping ) return;

        freeMovement.setCurrentMaxVelocity( MAX_WALKING_VELOCITY );
        freeMovement.setCurrentAcceleration(WALKING_ACCELERATION);
        isRunning = false;
    }

    private void jump()
    {
        if ( isJumping ) return;
        if ( isCrouching ) { stopCrouching(); return; }

        setVelocityYAxis( getVelocityYAxis() + JUMP_HEIGHT );
        isJumping = true;
    }

    private void crouch()
    {
        // ...
        isCrouching = true;
    }

    private void stopCrouching() { this.isCrouching = false; }

    private void reloadWeapon()
    {
        if ( isRunning || isJumping ) return;
        weapon.reload();
    }

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
                    .addComponent( new MeshRenderer( mesh, material ).fixedTilt() )
                    .addChild( weapon );

            if ( camera != null )
            {
                camera.setInnerTranslation( Camera.PLAYER_CAMERA() );
                player.addComponent( camera );
            }

            return player;
        }
    }
}
