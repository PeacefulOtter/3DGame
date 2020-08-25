package peacefulotter.engine.utils;

public class ProfileTimer
{
    private int numInvocations;
    private double startTime, totalTime;

    public ProfileTimer()
    {
        numInvocations = 0;
        startTime = 0;
        totalTime = 0;
    }

    public void startInvocation()
    {
        startTime = Time.getNanoTime();
    }

    public void stopInvocation()
    {
        assert startTime > 0;
        numInvocations++;
        totalTime += Time.getNanoTime() - startTime;
    }

    public double displayAndReset( String message ) { return displayAndReset( message, numInvocations ); }

    public double displayAndReset( String message, double dividend )
    {
        double time = 0;
        if ( dividend != 0 )
            time = ( 1000.0 * totalTime ) / dividend;

        Logger.log( getClass(), message + " : " + time + "ms" );
        totalTime = 0;
        numInvocations = 0;

        return time;
    }
}
