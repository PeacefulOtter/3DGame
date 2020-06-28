package peacefulotter.engine.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util
{
    public static String[] removeEmptyStrings( String[] data )
    {
        List<String> res = new ArrayList<>();

        for ( int i = 0; i < data.length; i++ )
        {
            if ( !data[ i ].equals( "" ) )
            {
                res.add( data[ i ] );
            }
        }

        return Arrays.copyOf( res.toArray(), res.size(), String[].class );
    }

    public static int[] toIntArray( List<Integer> data )
    {
        int[] res = new int[ data.size() ];
        for ( int i = 0; i < data.size(); i++)
        {
            res[ i ] = data.get(i);
        }
        return res;
    }
}
