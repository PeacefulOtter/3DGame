package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.core.maths.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class Material
{
    private Map<String, Texture> textureMap = new HashMap<>();
    private Map<String, Vector3f> vectorMap = new HashMap<>();
    private Map<String, Float> floatMap = new HashMap<>();

    // private Texture texture;
    // private Vector3f color;
    // private float specularIntensity, specularExponent; // how much does it reflects light - tiny or wide ?

    /*public Material( Texture texture )
    {
        this( texture, new Vector3f( 1, 1, 1 ) );
    }

    public Material( Texture texture, Vector3f color )
    {
        this( texture, color, 2, 32 );
    }

    public Material( Texture texture, Vector3f color, float specularIntensity, float specularExponent )
    {
        this.texture = texture;
        this.color = color;
        this.specularIntensity = specularIntensity;
        this.specularExponent = specularExponent;
    }*/

    public Material()
    {

    }

    public void addTexture( String name, Texture texture )
    {
        textureMap.put( name, texture );
    }

    public Texture getTexture( String name )
    {
        return textureMap.get( name );
    }

    public void addVector3f( String name, Vector3f vector )
    {
        vectorMap.put( name, vector );
    }

    public Vector3f getVector3f( String name )
    {
        return vectorMap.get( name );
    }

    public void addFloat( String name, float value )
    {
        floatMap.put( name, value );
    }

    public float getFloat( String name )
    {
        return floatMap.get( name );
    }
}
