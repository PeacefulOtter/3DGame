package peacefulotter.engine.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

public class Logger
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern( "HH:mm:ss.SSS" );
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
        return LocalDateTime.now().format( FORMATTER );
    }
}
