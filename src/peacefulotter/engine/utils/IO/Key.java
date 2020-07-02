package peacefulotter.engine.utils.IO;

public class Key
{
    private int keyCode;
    private IOExecutable IOExecutable;
    private boolean isPressed = false;

    public Key( int keyCode, IOExecutable executable )
    {
        this( keyCode, executable, false );
    }

    public Key( int keyCode, IOExecutable executable, boolean isPressed )
    {
        this.keyCode = keyCode;
        this.IOExecutable = executable;
        this.isPressed = isPressed;
    }

    public void setPressed( boolean isPressed ) { this.isPressed = isPressed; }

    public void exec( float deltaTime )
    {
        if ( isPressed )
            IOExecutable.exec( deltaTime );
    }

    public int getKeyCode() { return keyCode; }
}
