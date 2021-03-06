package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.utils.MappedValues;

import java.util.HashMap;
import java.util.Map;

// A material is a made up of three textures: diffuse, normal and dispMap used to render nicely the mesh
public class Material extends MappedValues
{
    private final Map<String, Texture> textureMap;
    private boolean transparent;

    public Material( SimpleMaterial sm )
    {
        this( sm.getTexture(), sm.getSpecularIntensity(), sm.getSpecularPower(), 0f, 0f );
    }

    public Material( Texture diffuse, float specularIntensity, float specularPower, float dispMapScale, float dispMapOffset )
    {
        this( diffuse, Texture.getDefaultNormal(), Texture.getDefaultHeight(), specularIntensity, specularPower, dispMapScale, dispMapOffset );
    }

    public Material(
            Texture diffuse, Texture normalMap, Texture dispMap,
            float specularIntensity, float specularPower, float dispMapScale, float dispMapOffset )
    {
        super();
        textureMap = new HashMap<>();
        addTexture( "diffuse", diffuse );
        addTexture( "normalMap", normalMap );
        addTexture( "dispMap", dispMap );

        addFloat( "specularIntensity", specularIntensity );
        addFloat( "specularPower", specularPower );

        float baseBias = dispMapScale / 2.0f;
        addFloat( "dispMapScale", dispMapScale );
        addFloat( "dispMapBias", -baseBias + baseBias * dispMapOffset );
    }

    public void addTexture( String name, Texture texture )
    {
        textureMap.put( name, texture );
    }

    public void setNormalMap( Texture texture ) { textureMap.replace( "normalMap", texture ); }
    public void setDispMap( Texture texture ) { textureMap.replace( "dispMap", texture ); }

    public Texture getTexture( String name )
    {
        if ( textureMap.containsKey( name ) )
            return textureMap.get( name );
        return new Texture( "test.png" );
    }

    public boolean hasTransparency() { return transparent; }
    public void setTransparency( boolean transparency ) { transparent = transparency; }

    @Override
    public String toString()
    {
        return textureMap.toString();
    }
}
