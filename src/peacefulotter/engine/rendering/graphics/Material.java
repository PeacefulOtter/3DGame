package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.core.maths.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class Material
{
    private final Map<String, Texture>  textureMap;
    private final Map<String, Vector3f> vectorMap;
    private final Map<String, Float>    floatMap;

    public Material()
    {
        textureMap = new HashMap<>();
        vectorMap = new HashMap<>();
        floatMap = new HashMap<>();
    }

    public void addTexture( String name, Texture texture )
    {
        textureMap.put( name, texture );
    }

    public Texture getTexture( String name )
    {
        if ( textureMap.containsKey( name ) )
            return textureMap.get( name );
        return new Texture( "test.png" );
    }

    public void addVector3f( String name, Vector3f vector )
    {
        vectorMap.put( name, vector );
    }

    public Vector3f getVector3f( String name )
    {
        if ( vectorMap.containsKey( name ) )
            return vectorMap.get( name );
        return Vector3f.ZERO;
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
