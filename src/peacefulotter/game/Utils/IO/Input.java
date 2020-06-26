package peacefulotter.game.Utils.IO;

import peacefulotter.game.Maths.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;


public class Input
{
    private static final int MOUSE_PRIMARY = 0;
    private static final int MOUSE_SECONDARY = 1;
    private static final int MOUSE_SCROLL = 2;
    private static final int MOUSE_M5 = 3;
    private static final int MOUSE_M4 = 4;

    private static final List<Key> keys = new ArrayList<>();
    private static final List<Key> mouseButtons = new ArrayList<>();

    private static int mousePrimaryState = GLFW_RELEASE;
    private static int mouseSecondaryState = GLFW_RELEASE;

    private static Vector2f mousePosition = new Vector2f( 0, 0 );

    public static void initInputs( long window )
    {
        initKeyCallbacks( window );
        initMouseButtonsCallbacks( window );
        initCursorCallback( window );
    }


    public static void addKeyCallback( int keyCode, IOExecutable executable )
    {
        keys.add( new Key( keyCode, executable ) );
    }

    private static void initKeyCallbacks( long window )
    {
        glfwSetKeyCallback( window, ( wd, key, scancode, action, mods ) -> {
            if ( action == GLFW_PRESS || action == GLFW_REPEAT )
            {
                for ( Key k : keys )
                {
                    if ( k.getKeyCode() == key )
                    {
                        k.exec();
                    }
                }
            }
        } );
    }


    public static void addMouseCallback( int keyCode, IOExecutable executable )
    {
        mouseButtons.add( new Key( keyCode, executable ) );
        /*mouseButtons.add( new Key( MOUSE_PRIMARY, () -> {
            System.out.println( "primary pressed" );
        } ) );
        mouseButtons.add( new Key( MOUSE_SECONDARY, () -> {
            System.out.println( "secondary pressed" );
        } ) );
        mouseButtons.add( new Key( MOUSE_SCROLL, () -> {
            System.out.println( "scroll pressed" );
        } ) );
        mouseButtons.add( new Key( MOUSE_M4, () -> {
            System.out.println( "M4 pressed" );
        } ) );
        mouseButtons.add( new Key( MOUSE_M5, () -> {
            System.out.println( "M5 pressed" );
        } ) );*/
    }

    private static void initMouseButtonsCallbacks( long window )
    {
        glfwSetMouseButtonCallback( window, ( wd, button, action, mods ) -> {
            for ( Key k : mouseButtons )
            {
                if ( k.getKeyCode() == button )
                {
                    if ( button == MOUSE_PRIMARY ) { mousePrimaryState = action; }
                    else if ( button == MOUSE_SECONDARY ) { mouseSecondaryState = action; }
                    k.exec();
                }
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
}
