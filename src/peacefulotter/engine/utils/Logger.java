package peacefulotter.engine.utils;

import java.time.LocalTime;
import java.util.StringJoiner;

public class Logger
{
    private static final boolean LOG_ACTIVE = true;

    public static void log( String msg )
    {
        log( "", msg );
    }

    public static void log( Class<?> c, String msg )
    {
        log( "[" + c.getSimpleName() +  "]", msg );
    }

    public static void log( Object obj ) { log( obj.toString() ); }

    public static void log( Class<?> c, Object obj ) { log( c, obj.toString() ); }

    /* Main method */
    public static void log( String prefix, String msg )
    {
        if ( !LOG_ACTIVE ) return;
        System.out.println( getTime() + " " + prefix + " " + msg );
    }

    public static void err( String errMessage, StackTraceElement[] trace )
    {
        StringJoiner sj = new StringJoiner( "\n" );
        sj.add( errMessage );
        for ( StackTraceElement traceElement : trace )
            sj.add( "\tat " + traceElement );

        log( "= ERR =", sj.toString() );
    }

    private static String getTime()
    {
        String time = LocalTime.now().toString();
        return time.substring( 0, time.length() - 3 );
    }
}
