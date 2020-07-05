package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.utils.MappedValues;

import java.util.HashMap;
import java.util.Map;

public class Material extends MappedValues
{
    private final Map<String, Texture>  textureMap;

    public Material()
    {
        super();
        textureMap = new HashMap<>();
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
}
