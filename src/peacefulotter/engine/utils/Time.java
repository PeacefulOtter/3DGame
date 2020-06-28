package peacefulotter.engine.utils;

public class Time
{
    private static final long SECOND = (long) Math.pow( 10, 9 );

    public static double getNanoTime() { return System.nanoTime() / (double) SECOND; }
}
