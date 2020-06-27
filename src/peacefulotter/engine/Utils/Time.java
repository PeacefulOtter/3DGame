package peacefulotter.engine.Utils;

public class Time
{
    public static final long SECOND = (long) Math.pow( 10, 9 );

    public static long getNanoTime() { return System.nanoTime(); }

    public static double getDeltaNano( float nanos ) { return getNanoTime() - nanos; }
}
