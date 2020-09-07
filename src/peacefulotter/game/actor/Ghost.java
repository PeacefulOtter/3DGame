package peacefulotter.game.actor;

import peacefulotter.engine.components.PhysicsObject;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.terrain.Terrain;
import peacefulotter.engine.utils.IO.Input;
import peacefulotter.game.inputs.FreeMovement;
import peacefulotter.game.inputs.FreeRotation;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class Ghost extends PhysicsObject
{
    private static final float MAX_VELOCITY = 100;
    private static final float MAX_ACCELERATION = 8;
    private static final float SLOW_FACTOR = 1;
    private static final float HEIGHT_SLOW_FACTOR = 1;
    private static final float HEIGHT_ACCELERATION = 3;

    private final FreeMovement freeMovement;
    private final FreeRotation freeRotation;
    private final boolean isUser;

    public Ghost( Terrain terrain, boolean isUser )
    {
        super( Vector3f.getZero() );
        this.isUser = isUser;
        freeMovement = new FreeMovement( this, terrain, MAX_VELOCITY, MAX_ACCELERATION, SLOW_FACTOR, MAX_VELOCITY - 50, false );
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
    public void update( float deltaTime )
    {
        if ( !isUser ) return;

        freeMovement.updateVelocity( deltaTime, false );
        float yVelocity = freeMovement.getVelocityYAxis();
        freeMovement.setVelocityYAxis( yVelocity - Math.signum( yVelocity ) * HEIGHT_SLOW_FACTOR );

        super.update( deltaTime );
    }

    private void gainHeight( int direction )
    {
        freeMovement.setVelocityYAxis( freeMovement.getVelocityYAxis() + direction * HEIGHT_ACCELERATION );
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
