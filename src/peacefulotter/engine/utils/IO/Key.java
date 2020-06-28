package peacefulotter.engine.utils.IO;

public class Key
{
    private final int keyCode;
    private final IOExecutable IOExecutable;

    public Key( int keyCode, IOExecutable IOExecutable)
    {
        this.keyCode = keyCode;
        this.IOExecutable = IOExecutable;
    }

    public void exec( float deltaTime ) { IOExecutable.exec( deltaTime ); }

    public int getKeyCode() { return keyCode; }
}
