package peacefulotter.engine.elementary;

public class Disposable
{
    public void dispose() { }

    public DisposeThread createDisposeThread() { return new DisposeThread(); }

    class DisposeThread extends Thread
    {
        public void run() { dispose(); }
    }
}
