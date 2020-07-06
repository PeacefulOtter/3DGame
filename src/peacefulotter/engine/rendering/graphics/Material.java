package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.utils.MappedValues;

import java.util.HashMap;
import java.util.Map;

public class Material extends MappedValues
{
    private final Map<String, Texture> textureMap;

    public Material( Texture diffuse, Texture normalMap, Texture dispMap,
                     float specularIntensity, float specularPower, float dispMapScale, float dispMapOffset )
    {
        super();
        textureMap = new HashMap<>();
        addTexture( "diffuse", diffuse );
        addFloat( "specularIntensity", specularIntensity );
        addFloat( "specularPower", specularPower );
        addTexture( "normalMap", normalMap );
        addTexture( "dispMap", dispMap );

        float baseBias = dispMapScale / 2.0f;
        addFloat( "dispMapScale", dispMapScale );
        addFloat( "dispMapBias", -baseBias + baseBias * dispMapOffset );
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
