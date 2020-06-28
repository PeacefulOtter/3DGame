package peacefulotter.engine.utils.IO;

import peacefulotter.engine.core.maths.Vector2f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class Input
{
    public static final int MOUSE_PRIMARY = 0;
    public static final int MOUSE_SECONDARY = 1;
    public static final int MOUSE_SCROLL = 2;
    public static final int MOUSE_M5 = 3;
    public static final int MOUSE_M4 = 4;

    public static final int MOUSE_RELEASED = 0;
    public static final int MOUSE_PRESSED = 1;

    private static final Set<Key> keys = new HashSet<>();
    private static final Set<Key> keysPressed = new HashSet<>();
    private static final List<Key> mouseButtons = new ArrayList<>();

    private static int mousePrimaryState = GLFW_RELEASE;
    private static int mouseSecondaryState = GLFW_RELEASE;

    private static final Vector2f mousePosition = new Vector2f( 0, 0 );

    public static void initInputs( long window )
    {
        initKeyCallbacks( window );
        initMouseButtonsCallbacks( window );
        initCursorCallback( window );
    }

    public static void execInputs( float deltaTime )
    {
        for ( Key k : keysPressed )
        {
            k.exec( deltaTime );
        }
    }

    public static void addKeyCallback( int keyCode, IOExecutable executable )
    {
        keys.add( new Key( keyCode, executable ) );
    }

    private static void initKeyCallbacks( long window )
    {
        glfwSetKeyCallback( window, ( wd, key, scancode, action, mods ) ->
        {
            for ( Key k : keys )
            {
                if ( k.getKeyCode() == key )
                {
                    if ( action == GLFW_PRESS || action == GLFW_REPEAT )
                        keysPressed.add( k );
                    else
                        keysPressed.remove( k );
                }
            }
        } );
    }


    public static void addMouseCallback( int keyCode, IOExecutable executable )
    {
        mouseButtons.add( new Key( keyCode, executable ) );
    }

    private static void initMouseButtonsCallbacks( long window )
    {
        glfwSetMouseButtonCallback( window, ( wd, button, action, mods ) -> {
            for ( Key k : mouseButtons )
            {
                if ( k.getKeyCode() == button )
                {
                    if ( button == MOUSE_PRIMARY )
                    {
                        mousePrimaryState = action;
                        int hideMouse = action == 1 ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL;
                        glfwSetInputMode( window, GLFW_CURSOR, hideMouse );
                    }
                    else if ( button == MOUSE_SECONDARY ) { mouseSecondaryState = action; }
                }
                // k.exec()
            }
        } );
    }

    private static void initCursorCallback( long window )
    {
        glfwSetCursorPosCallback( window, ( wd, xpos, ypos ) -> {
            mousePosition.setX( (float) xpos );
            mousePosition.setY( (float) ypos );
        } );
    }

    public static Vector2f getMousePosition() { return mousePosition; }

    public static int getMousePrimaryState() { return mousePrimaryState; }
}
