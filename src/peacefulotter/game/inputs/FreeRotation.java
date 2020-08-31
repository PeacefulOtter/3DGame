package peacefulotter.game.inputs;

import peacefulotter.engine.components.GameObject;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.elementary.Initializable;
import peacefulotter.engine.utils.IO.Input;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;

public class FreeRotation implements Initializable
{
    private final GameObject parent;
    private final float rotationSensitivity, cursorSensitivity;
    private double oldCursorX, oldCursorY;

    public FreeRotation( GameObject parent, float rotationSensitivity, float cursorSensitivity )
    {
        this.parent = parent;
        this.rotationSensitivity = rotationSensitivity;
        this.cursorSensitivity = cursorSensitivity;
    }

    @Override
    public void init()
    {
        Input.addKeyCallback( GLFW_KEY_UP,    ( deltaTime ) -> rotateX( -deltaTime * rotationSensitivity ) );
        Input.addKeyCallback( GLFW_KEY_RIGHT, ( deltaTime ) -> rotateY(  deltaTime * rotationSensitivity ) );
        Input.addKeyCallback( GLFW_KEY_DOWN,  ( deltaTime ) -> rotateX(  deltaTime * rotationSensitivity ) );
        Input.addKeyCallback( GLFW_KEY_LEFT,  ( deltaTime ) -> rotateY( -deltaTime * rotationSensitivity ) );

        Input.addCursorCallback( ( deltaTime, x, y ) -> {
            if ( x != oldCursorX )
            {
                rotateY( (float) -( oldCursorX - x ) * cursorSensitivity * deltaTime );
                oldCursorX = x;
            }
            else if ( y != oldCursorY )
            {
                rotateX( (float) -( oldCursorY - y ) * cursorSensitivity * deltaTime );
                oldCursorY = y;
            }
        } );
    }

    private void rotateX( float angleDeg )
    {
        parent.getTransform().rotate( parent.getTransform().getRotation().getRight(), angleDeg );
    }

    private void rotateY( float angleDeg )
    {
        parent.getTransform().rotate( Vector3f.Y_AXIS, angleDeg );
    }
}
