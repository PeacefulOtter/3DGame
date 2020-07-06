package peacefulotter.engine.utils;

import peacefulotter.engine.core.maths.Vector3f;

import java.util.HashMap;
import java.util.Map;

public abstract class MappedValues
{
    private final Map<String, Vector3f> vectorMap = new HashMap<>();
    private final Map<String, Float> floatMap = new HashMap<>();

    public void addVector3f( String name, Vector3f vector )
    {
        vectorMap.put( name, vector );
    }

    public Vector3f getVector3f( String name )
    {
        if ( vectorMap.containsKey( name ) )
            return vectorMap.get( name );
        return Vector3f.getZero();
    }

    public void addFloat( String name, float value )
    {
        floatMap.put( name, value );
    }

    public float getFloat( String name )
    {
        if ( floatMap.containsKey( name ) )
            return floatMap.get( name );
        return 0;
    }
}
