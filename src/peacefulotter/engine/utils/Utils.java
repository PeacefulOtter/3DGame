package peacefulotter.engine.utils;

import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils
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

    public static float barryCentric( Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos )
    {
        float det =  ( p2.getZ() - p3.getZ() ) * ( p1.getX() - p3.getX() )  + ( p3.getX() - p2.getX() ) * ( p1.getZ() - p3.getZ() );
        float l1 = ( ( p2.getZ() - p3.getZ() ) * ( pos.getX() - p3.getX() ) + ( p3.getX() - p2.getX() ) * ( pos.getY() - p3.getZ() ) ) / det;
        float l2 = ( ( p3.getZ() - p1.getZ() ) * ( pos.getX() - p3.getX() ) + ( p1.getX() - p3.getX() ) * ( pos.getY() - p3.getZ() ) ) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.getY() + l2 * p2.getY() + l3 * p3.getY();
    }

}
