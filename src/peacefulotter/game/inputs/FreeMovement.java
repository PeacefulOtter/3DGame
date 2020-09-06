package peacefulotter.game.inputs;

import peacefulotter.engine.components.PhysicsObject;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.elementary.Initializable;
import peacefulotter.engine.rendering.terrain.Terrain;
import peacefulotter.engine.utils.IO.Input;
import peacefulotter.game.actor.VelocityAngle;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;

public class FreeMovement implements Initializable
{
    private static final float GRAVITY = 200f;
    private final PhysicsObject parent;
    private final Set<VelocityAngle> movingArrows;
    private final Terrain terrain;

    private final float slowFactor;
    private final boolean applyGravity;
    private float currentVelocity, currentMaxVelocity, currentAcceleration, velocityYAxis, maxYVelocity;

    public FreeMovement( PhysicsObject parent, float currentMaxVelocity, float currentAcceleration, float slowFactor, float maxYVelocity )
    {
        this( parent, null, currentMaxVelocity, currentAcceleration, slowFactor, maxYVelocity, false );
    }

    public FreeMovement( PhysicsObject parent, Terrain terrain, float currentMaxVelocity, float currentAcceleration, float slowFactor, float maxYVelocity )
    {
        this( parent, terrain, currentMaxVelocity, currentAcceleration, slowFactor, maxYVelocity, true );
    }


    private FreeMovement( PhysicsObject parent, Terrain terrain, float currentMaxVelocity, float currentAcceleration, float slowFactor, float maxYVelocity, boolean applyGravity )
    {
        this.parent = parent;
        this.terrain = terrain;
        this.movingArrows = new HashSet<>( 4 );
        this.currentMaxVelocity = currentMaxVelocity;
        this.currentAcceleration = currentAcceleration;
        this.slowFactor = slowFactor;
        this.velocityYAxis = 0;
        this.maxYVelocity = maxYVelocity;
        this.applyGravity = applyGravity;
    }

    @Override
    public void init()
    {
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
    }

    public void updateVelocity( float deltaTime, boolean isJumping )
    {
        float newAngle = ( isMoving() && !isJumping )
                ? VelocityAngle.getAngle()
                : calcActualAngle();

        // this represents the getForward but without caring about the player horizontal angle
        // forward = getRight - 90° = (-right.z, y, right.x)
        Vector3f right = parent.getTransform().getRotation().getRight();
        Vector3f direction = new Vector3f( -right.getZ(), 0, right.getX() );

        Vector3f position = parent.getPosition();
        float terrainHeight = 0;
        if ( terrain != null )
            terrainHeight = terrain.getTerrainHeight( position.getX(), position.getZ() );

        if ( position.getY() <= terrainHeight && velocityYAxis <= 0 )
        {
            parent.getVelocity().setY( terrainHeight );
            position.setY( terrainHeight );
            velocityYAxis = 0;
        }
        // else, object is in the air : apply gravity
        else if ( applyGravity )
        {
            velocityYAxis -= GRAVITY * deltaTime;
            if ( velocityYAxis < -maxYVelocity )
                velocityYAxis = -maxYVelocity;
        }

        parent.setVelocity( direction
                .rotate( Vector3f.Y_AXIS, newAngle )
                .mul( currentVelocity ).setY( velocityYAxis ) );

        if ( isJumping ) return;

        if ( isMoving() )
        {
            if ( currentVelocity < currentMaxVelocity )
                currentVelocity += currentAcceleration;
            if ( currentVelocity > currentMaxVelocity )
                currentVelocity = currentMaxVelocity;
        }
        else
        {
            if ( currentVelocity > 0 )
                currentVelocity -= slowFactor;
            if ( currentVelocity < 0 )
                currentVelocity = 0;
        }
    }

    private float calcActualAngle()
    {
        Vector2f v1 = parent.getTransform().getRotation().getForward().getXZ();
        Vector2f v2 = parent.getVelocity().getXZ().normalize();
        return Vector2f.calcAngle( v1, v2 );
    }

    public boolean isMoving()
    {
        return movingArrows.size() > 0;
    }

    private void move( VelocityAngle arrow )
    {
        if ( !parent.move( arrow ) ) return;

        movingArrows.add( arrow );
        arrow.setActive( true );
    }

    public void stopMoving( VelocityAngle arrow )
    {
        if ( !parent.stopMoving( arrow ) ) return;

        movingArrows.remove( arrow );
        arrow.setActive( false );
    }

    public float getVelocityYAxis() { return velocityYAxis; }
    public void setVelocityYAxis( float vel ) { velocityYAxis = vel; }

    public void setCurrentMaxVelocity( float newMax ) { this.currentMaxVelocity = newMax; }
    public void setCurrentAcceleration( float newMax ) { this.currentAcceleration = newMax; }
}
