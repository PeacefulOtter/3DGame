package peacefulotter.engine.utils.IO;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.Window;

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
    private static final Set<Key> keysReleased = new HashSet<>();
    private static final List<Key> mouseButtons = new ArrayList<>();
    private static final List<CursorPosExecutable> cursorPosCallbacks = new ArrayList<>();

    private static int mousePrimaryState = GLFW_RELEASE;
    private static int mouseSecondaryState = GLFW_RELEASE;

    private static final Vector2f cursorPosition = new Vector2f( 0, 0 );

    private static long window;

    private Input() {}

    public static void initInputs( long window )
    {
        Input.window = window;
        initKeyCallbacks();
        initMouseButtonsCallbacks();
        initCursorCallback();
        setMouseDisable( true );
    }

    public static void execInputs( float deltaTime )
    {
        for ( Key k : keysPressed )
            k.triggerPressCallback( deltaTime );

        for ( Key k : keysReleased )
            k.triggerReleaseCallback( deltaTime );
        keysReleased.clear();

        for ( Key k : mouseButtons )
            k.triggerPressCallback( deltaTime );
    }

    public static void addKeyCallback( int keyCode, IOExecutable executable )
    {
        keys.add( new Key( keyCode, executable, true ) );
    }

    public static void addKeyPressReleaseCallbacks( int keyCode, IOExecutable pressCallback, IOExecutable releaseCallback )
    {
        keys.add( new Key( keyCode, pressCallback, releaseCallback, true ) );
    }

    private static void initKeyCallbacks()
    {
        glfwSetKeyCallback( window, ( wd, key, scancode, action, mods ) ->
        {
            System.out.println("keycallback");
            for ( Key k : keys )
            {
                if ( k.getKeyCode() == key )
                {
                    if ( action == GLFW_PRESS || action == GLFW_REPEAT )
                        keysPressed.add( k );
                    else
                    {
                        keysReleased.add( k );
                        keysPressed.remove( k );
                    }
                }
            }
        } );
    }


    public static void addMouseCallback( int keyCode, IOExecutable executable )
    {
        mouseButtons.add( new Key( keyCode, executable ) );
    }

    private static void initMouseButtonsCallbacks()
    {
        glfwSetMouseButtonCallback( window, ( wd, button, action, mods ) -> {
            for ( Key k : mouseButtons )
            {
                if ( k.getKeyCode() == button )
                {
                    k.setPressed( action == 1 );
                }
            }
        } );
    }

    private static void initCursorCallback()
    {
        glfwSetCursorPosCallback( window, ( wd, xpos, ypos ) -> {
            System.out.println("updating mouse position");
            cursorPosition.setX( (float) xpos );
            cursorPosition.setY( (float) ypos );
            for ( CursorPosExecutable callback : cursorPosCallbacks )
                callback.exec( cursorPosition );
        } );
    }

    public static void addCursorPosCallback( CursorPosExecutable callback )
    {
        cursorPosCallbacks.add( callback );
    }

    public static Vector2f getCursorPosition() { return cursorPosition; }

    public static int getMousePrimaryState() { return mousePrimaryState; }

    public static Vector3f calcMouseRay( Matrix4f projectionViewMatrix )
    {
        // convert mouse position to opengl coordinate system
        float x = ( 2f * cursorPosition.getX() ) / Window.getWidth() - 1f;
        float y = ( 2f * cursorPosition.getY() ) / Window.getHeight() - 1f;
        Vector3f clipCoords = new Vector3f( x, y, -1f );

        // to eye coordinates
        //// INVERT PROJECTION MATRIX
        Matrix4f invertedProjMatrix = projectionViewMatrix.invert();
        Vector3f eyeCoords = invertedProjMatrix.transform( clipCoords );

        // to world coords
        //// INVERT VIEW MATRIX
        Vector3f rayWorld = invertedProjMatrix.transform( eyeCoords );
        return rayWorld.normalize();
    }

    public static void setMouseDisable( boolean disable )
    {
        int hideMouse = disable ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL;
        glfwSetInputMode( window, GLFW_CURSOR, hideMouse );
    }
}
