package peacefulotter.game.actor;

import peacefulotter.engine.components.PhysicsObject;
import peacefulotter.engine.utils.IO.Input;
import peacefulotter.game.inputs.FreeMovement;
import peacefulotter.game.inputs.FreeRotation;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class Ghost extends PhysicsObject
{
    private static final float MAX_VELOCITY = 50;
    private static final float MAX_ACCELERATION = 10;
    private static final float SLOW_FACTOR = 8;
    private static final float HEIGHT_SLOW_FACTOR = 1;
    private static final float HEIGHT_ACCELERATION = 3;

    private final FreeMovement freeMovement;
    private final FreeRotation freeRotation;
    private final boolean isUser;

    public Ghost( boolean isUser )
    {
        super( false );
        this.isUser = isUser;
        freeMovement = new FreeMovement( this, MAX_VELOCITY, MAX_ACCELERATION, SLOW_FACTOR );
        freeRotation = new FreeRotation( this, Player.ROTATION_SENSITIVITY, Player.CURSOR_SENSITIVITY );
    }

    @Override
    public void init()
    {
        if ( !isUser ) return;

        freeMovement.init();
        freeRotation.init();

        Input.addKeyCallback( GLFW_KEY_SPACE, ( deltaTime -> gainHeight( 1 ) ) );
        Input.addKeyCallback( GLFW_KEY_LEFT_CONTROL, ( deltaTime ) -> gainHeight( -1 ) );
    }

    @Override
    protected void updateVelocity( float deltaTime )
    {
        if ( !isUser ) return;

        super.updateVelocity( deltaTime );
        freeMovement.updateVelocity( false );
        setVelocityYAxis( getVelocityYAxis() - Math.signum( getVelocityYAxis() ) * HEIGHT_SLOW_FACTOR );
    }

    private void gainHeight( int direction )
    {
        setVelocityYAxis( getVelocityYAxis() + direction * HEIGHT_ACCELERATION );
    }

    @Override
    public boolean move( VelocityAngle arrow )
    {
        return true;
    }

    @Override
    public boolean stopMoving(VelocityAngle arrow)
    {
        return true;
    }
}
