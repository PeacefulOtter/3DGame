package peacefulotter.engine.Utils;

public class Time
{
    public static final long SECOND = (long) Math.pow( 10, 9 );
    private static double delta;

    public static long getNanoTime() { return System.nanoTime(); }

    public static double getDeltaNano() { return delta; }

    public static void setDeltaNano( double delta ) { Time.delta = delta; }
}
