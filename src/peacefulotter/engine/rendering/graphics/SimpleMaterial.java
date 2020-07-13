package peacefulotter.engine.rendering.graphics;

public class SimpleMaterial
{
    private Texture texture;
    private float specularIntensity = 1;
    private float specularPower = 0;

    public void setTexture( Texture texture )
    {
        this.texture = texture;
    }

    public void setSpecularIntensity( float specularIntensity )
    {
        this.specularIntensity = specularIntensity;
    }

    public void setSpecularPower( float specularPower )
    {
        this.specularPower = specularPower;
    }
}
