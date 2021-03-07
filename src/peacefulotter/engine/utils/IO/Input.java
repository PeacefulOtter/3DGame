package peacefulotter.engine.utils.IO;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.Window;
import peacefulotter.engine.rendering.resourceManagement.Resources;
import peacefulotter.engine.utils.ProfileTimer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Input
{
    private static final ProfileTimer profiler = new ProfileTimer();

    public static final int MOUSE_PRIMARY = 0;
    public static final int MOUSE_SECONDARY = 1;
    public static final int MOUSE_SCROLL = 2;
    public static final int MOUSE_M5 = 3;
    public static final int MOUSE_M4 = 4;

    public static final int MOUSE_RELEASED = 0;
    public static final int MOUSE_PRESSED = 1;

    private static final Set<Key> keys = new HashSet<>();
    private static final List<Key> mouseButtons = new ArrayList<>();
    private static final List<CursorPosExecutable> cursorCallbacks = new ArrayList<>();

    private static int mousePrimaryState = GLFW_RELEASE;
    private static int mouseSecondaryState = GLFW_RELEASE;

    private static final double[] glfwCursorX = new double[ 1 ];
    private static final double[] glfwCursorY = new double[ 1 ];

    private static long window;

    public static void initInputs( long window )
    {
        Input.window = window;
        setMouseDisable( true );
    }

    public static void execInputs( float deltaTime )
    {
        profiler.startInvocation();

        glfwPollEvents();

        // prevent ALT+F4 and use a custom system.exit() to avoid memory leaks
        int alt = glfwGetKey( window, GLFW_KEY_LEFT_ALT );
        int f4 = glfwGetKey( window, GLFW_KEY_F4 );
        if (alt == 1 && f4 == 1)
        {
            Resources.freeBuffers();
            System.exit( 1 );
        }

        callKeyCallbacks( deltaTime );
        callMouseButtonsCallbacks( deltaTime );
        callCursorCallbacks( deltaTime );

        profiler.stopInvocation();
    }

    /**
     * KEYS CALLBACKS
     */

    public static void addKeyCallback( int keyCode, IOExecutable executable )
    {
        addKeyCallback( keyCode, executable, true );
    }

    public static void addKeyCallback( int keyCode, IOExecutable executable, boolean canRepeat )
    {
        keys.add( new Key( keyCode, executable, canRepeat ) );
    }

    public static void addKeyPressReleaseCallbacks( int keyCode, IOExecutable pressCallback, IOExecutable releaseCallback )
    {
        addKeyPressReleaseCallbacks( keyCode, pressCallback, releaseCallback, true );
    }

    public static void addKeyPressReleaseCallbacks( int keyCode, IOExecutable pressCallback, IOExecutable releaseCallback, boolean canRepeat )
    {
        keys.add( new Key( keyCode, pressCallback, releaseCallback, canRepeat ) );
    }

    private static void callKeyCallbacks( float deltaTime )
    {
        for ( Key k : keys )
        {
            int action = glfwGetKey( window, k.getKeyCode() );
            k.callbackAction( action, deltaTime );
        }
    }


    /**
     * MOUSE BUTTONS CALLBACKS
     */

    public static void addMouseButtonCallback( int keyCode, IOExecutable executable )
    {
        addMouseButtonCallback( keyCode, executable, true );
    }

    public static void addMouseButtonCallback( int keyCode, IOExecutable executable, boolean canRepeat )
    {
        mouseButtons.add( new Key( keyCode, executable, canRepeat ) );
    }

    public static void addMouseButtonPressReleaseCallback( int keyCode, IOExecutable pressCallback, IOExecutable releaseCallback )
    {
        mouseButtons.add( new Key( keyCode, pressCallback, releaseCallback, false ) );
    }

    private static void callMouseButtonsCallbacks( float deltaTime )
    {
        for ( Key k : mouseButtons )
        {
            int action = glfwGetMouseButton( window, k.getKeyCode() );
            k.callbackAction( action, deltaTime );
        }
    }


    /**
     * CURSOR CALLBACKS
     */

    public static void addCursorCallback( CursorPosExecutable callback )
    {
        cursorCallbacks.add( callback );
    }

    private static void callCursorCallbacks( float deltaTime )
    {
        glfwGetCursorPos( window, glfwCursorX, glfwCursorY );

        for ( CursorPosExecutable callback : cursorCallbacks )
            callback.exec( deltaTime, glfwCursorX[ 0 ], glfwCursorY[ 0 ] );
    }




    public static int getMousePrimaryState() { return mousePrimaryState; }

    public static Vector3f calcMouseRay( Matrix4f projectionViewMatrix )
    {
        // convert mouse position to opengl coordinate system
        float x = ( 2f * (float)glfwCursorX[ 0 ] ) / Window.getWidth() - 1f;
        float y = ( 2f * (float)glfwCursorY[ 0 ] ) / Window.getHeight() - 1f;
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



    public static double displayInputTime( double dividend ) { return profiler.displayAndReset( "Input time", dividend ); }
}
