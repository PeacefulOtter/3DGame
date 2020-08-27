package peacefulotter.game.actor;

import peacefulotter.engine.components.PhysicsObject;
import peacefulotter.engine.utils.IO.Input;
import peacefulotter.game.inputs.FreeMovement;
import peacefulotter.game.inputs.FreeRotation;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class Ghost extends PhysicsObject
{
    private final FreeMovement freeMovement;
    private final FreeRotation freeRotation;

    public Ghost()
    {
        freeMovement = new FreeMovement( this, Player.MAX_WALKING_VELOCITY, Player.WALKING_ACCELERATION, Player.SLOW_FACTOR );
        freeRotation = new FreeRotation( this, Player.ROTATION_SENSITIVITY, Player.CURSOR_SENSITIVITY );
    }

    @Override
    public void init()
    {
        freeMovement.init();
        freeRotation.init();

        Input.addKeyCallback( GLFW_KEY_SPACE, this::gainHeight );
        Input.addKeyCallback( GLFW_KEY_LEFT_CONTROL, ( deltaTime ) -> gainHeight( -deltaTime ) );
    }

    @Override
    protected void updateVelocity( float deltaTime )
    {
        freeMovement.updateVelocity( deltaTime, false );
    }

    private void gainHeight( float deltaTime )
    {
        setVelocityYAxis( getVelocityYAxis() + deltaTime * Player.WALKING_ACCELERATION);
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
