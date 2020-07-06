package peacefulotter.engine.utils.IO;

public class Key
{
    private final int keyCode;
    private final IOExecutable IOExecutable;
    private boolean isPressed;

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
