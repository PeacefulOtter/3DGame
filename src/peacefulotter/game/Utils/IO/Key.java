package peacefulotter.game.Utils.IO;

import peacefulotter.game.Utils.Listener.ListenedObject;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Key
{
    private int keyCode;
    private ListenedObject<Boolean> isKeyPressed = new ListenedObject<>( false );
    private KeyExecutable executable;

    public Key( int keyCode, KeyExecutable executable )
    {
        this.keyCode = keyCode;
        this.executable = executable;
    }

    public boolean isKeyDown( long window )
    {
        return glfwGetKey( window, keyCode ) == 1;
    }

    public void update( long window )
    {
        boolean isKeyDown = isKeyDown( window );
        if ( isKeyDown != isKeyPressed.get() )
            isKeyPressed.set( isKeyDown( window ) );
        if ( isKeyDown )
            exec();
    }

    public void exec() { executable.exec(); }

    public int getKeyCode() { return keyCode; }
    public ListenedObject<Boolean> getIsKeyPressed() { return isKeyPressed; }
}
