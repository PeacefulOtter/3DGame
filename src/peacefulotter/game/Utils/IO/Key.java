package peacefulotter.game.Utils.IO;

public class Key
{
    private int keyCode;
    private Executable executable;

    public Key( int keyCode, Executable executable )
    {
        this.keyCode = keyCode;
        this.executable = executable;
    }

    public void exec() { executable.exec(); }

    public int getKeyCode() { return keyCode; }
}
