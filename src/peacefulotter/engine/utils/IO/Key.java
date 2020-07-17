package peacefulotter.engine.utils.IO;

public class Key
{
    private final int keyCode;
    private final IOExecutable pressCallback, releaseCallback;
    private boolean isPressed;

    public Key( int keyCode, IOExecutable pressCallback )
    {
        this( keyCode, pressCallback, false );
    }

    public Key( int keyCode, IOExecutable pressCallback, boolean isPressed )
    {
        this( keyCode, pressCallback, null, isPressed );
    }

    public Key( int keyCode, IOExecutable pressCallback, IOExecutable releaseCallback, boolean isPressed )
    {
        this.keyCode = keyCode;
        this.pressCallback = pressCallback;
        this.releaseCallback = releaseCallback;
        this.isPressed = isPressed;
    }


    public void setPressed( boolean isPressed ) { this.isPressed = isPressed; }

    public void triggerPressCallback( float deltaTime )
    {
        if ( isPressed )
            pressCallback.exec( deltaTime );
    }

    public void triggerReleaseCallback( float deltaTime )
    {
        if ( releaseCallback != null )
            releaseCallback.exec( deltaTime );
    }

    public int getKeyCode() { return keyCode; }
}
