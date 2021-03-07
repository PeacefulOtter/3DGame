package peacefulotter.game.actor;

import peacefulotter.engine.components.Camera;
import peacefulotter.engine.components.PhysicsObject;
import peacefulotter.engine.components.lights.SpotLight;
import peacefulotter.engine.components.renderer.MultiMeshRenderer;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.terrain.Terrain;
import peacefulotter.engine.utils.IO.Input;
import peacefulotter.game.hud.Hud;
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
    public static final float MAX_WALKING_VELOCITY = 30;
    private static final float MAX_AIMING_VELOCITY = 20;
    private static final float MAX_RUNNING_VELOCITY = 50;

    public static final float WALKING_ACCELERATION = 0.1f;
    private static final float RUNNING_ACCELERATION = 0.3f;

    private static final float JUMP_HEIGHT = 50;

    public static final float ROTATION_SENSITIVITY = 180;
    public static final float CURSOR_SENSITIVITY = 50;
    public static final float SLOW_FACTOR = 1;

    private static final float MAX_HEALTH = 100;

    private final Weapon weapon;
    private final boolean isUser;
    private final Set<VelocityAngle> notMovingArrowsQueue;
    private final FreeMovement freeMovement;
    private final FreeRotation freeRotation;
    private final Terrain terrain;

    private boolean isRunning, isAiming, isReloading, isJumping, isCrouching;
    private float health = MAX_HEALTH;

    private Player( Vector3f position, Terrain terrain, Weapon weapon, boolean isUser )
    {
        super( position );
        this.terrain = terrain;
        this.weapon = weapon;
        this.isUser = isUser;
        this.notMovingArrowsQueue = new HashSet<>( 4 );
        if ( isUser )
        {
            this.freeMovement = new FreeMovement( this, terrain, MAX_WALKING_VELOCITY, WALKING_ACCELERATION, SLOW_FACTOR, JUMP_HEIGHT );
            this.freeRotation = new FreeRotation( this, ROTATION_SENSITIVITY, CURSOR_SENSITIVITY );
            Hud.setPlayer( this );
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

        Input.addMouseButtonPressReleaseCallback( MOUSE_SECONDARY,
                ( deltaTime ) -> aim(),
                ( deltaTime ) -> stopAiming() );


        Input.addKeyCallback( GLFW_KEY_LEFT_ALT,  ( deltaTime ) -> Input.setMouseDisable( false ) );
        Input.addKeyCallback( GLFW_KEY_R,  ( deltaTime ) -> reloadWeapon() );
    }

    public void update( float deltaTime )
    {
        if ( !isUser ) return;

        // player is on the ground or slightly below
        if ( getPosition().getY() <= terrain.getTerrainHeight( getPosition().getX(), getPosition().getZ() ) )
        {
            isJumping = false;

            notMovingArrowsQueue.forEach( freeMovement::stopMoving );
            notMovingArrowsQueue.clear();
        }

        freeMovement.updateVelocity( deltaTime, isJumping );

        if (health > 0.1)
            health -= 0.001;

        super.update( deltaTime );
        Hud.update( weapon, health );
    }

    @Override
    public boolean move( VelocityAngle arrow )
    {
        return !isJumping;
    }

    @Override
    public boolean stopMoving( VelocityAngle arrow )
    {
        if ( isJumping )
            notMovingArrowsQueue.add( arrow );
        return !isJumping;
    }

    private void run()
    {
        if ( isJumping || isAiming || isCrouching ) return;

        freeMovement.setCurrentMaxVelocity( MAX_RUNNING_VELOCITY );
        freeMovement.setCurrentAcceleration( RUNNING_ACCELERATION );
        isRunning = true;
    }

    private void stopRunning()
    {
        if ( isJumping ) return;

        freeMovement.setCurrentMaxVelocity( MAX_WALKING_VELOCITY );
        freeMovement.setCurrentAcceleration( WALKING_ACCELERATION );
        isRunning = false;
    }

    private void aim()
    {
        isAiming = true;
        weapon.getTransform().translate( new Vector3f( -0.6f, 0, 0.3f ) );
        freeMovement.setCurrentMaxVelocity( MAX_AIMING_VELOCITY );
    }

    private void stopAiming()
    {
        isAiming = false;
        weapon.getTransform().translate( new Vector3f( 0.6f, 0, -0.3f ) );
        freeMovement.setCurrentMaxVelocity( MAX_WALKING_VELOCITY );
    }

    private void jump()
    {
        if ( isJumping ) return;
        if ( isCrouching ) { stopCrouching(); return; }

        freeMovement.setVelocityYAxis( freeMovement.getVelocityYAxis() + JUMP_HEIGHT );
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
        private boolean isUser = false;
        private Vector3f position = Vector3f.getZero();
        private Weapon weapon;
        private Camera camera;
        private MultiMeshRenderer mmr;
        private Terrain terrain;
        private SpotLight flashLight;

        public PlayerBuilder setPlayerUser()
        {
            this.isUser = true;
            return this;
        }

        public PlayerBuilder setPosition( Vector3f position )
        {
            this.position = position;
            return this;
        }

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

        public PlayerBuilder setMultiMeshRenderer( MultiMeshRenderer mmr )
        {
            this.mmr = mmr;
            return this;
        }

        public PlayerBuilder setTerrain( Terrain terrain )
        {
            this.terrain = terrain;
            return this;
        }

        public PlayerBuilder setFlashLight( FlashLight flashLight )
        {
            this.flashLight = flashLight;
            return this;
        }

        public Player build()
        {
            if ( weapon == null )
                throw new NullPointerException( "Player needs a weapon" );
            if ( mmr == null )
                throw new NullPointerException( "Player needs a MultiMeshRenderer to be rendered" );
            if ( terrain == null )
                throw new NullPointerException( "Any PhysicsObject must have a terrain." );

            Player player = new Player( position, terrain, weapon, isUser );
            player
                    .addComponent( mmr.fixedTilt() )
                    .addChild( weapon );
            weapon.getTransform().translate( Weapon.PLAYER_ORIGIN() );

            if ( camera != null )
            {
                player.addComponent( camera );
                camera.fixedTilt().getTransform().translate( Camera.PLAYER_CAMERA_TRANSLATION() );
            }
            if ( flashLight != null )
            {
                player.addComponent( flashLight );
            }

            return player;
        }
    }
}
