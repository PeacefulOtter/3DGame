package peacefulotter.engine.Utils.IO;

public class Key
{
    private int keyCode;
    private IOExecutable IOExecutable;

    public Key( int keyCode, IOExecutable IOExecutable)
    {
        this.keyCode = keyCode;
        this.IOExecutable = IOExecutable;
    }

    public void exec() { IOExecutable.exec(); }

    public int getKeyCode() { return keyCode; }
}
