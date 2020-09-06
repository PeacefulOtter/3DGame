package peacefulotter.engine.rendering.graphics;

// A basic material used when loading a texture from a .mtl file
public class SimpleMaterial
{
    private final Texture texture;
    private final float specularIntensity, specularPower;

    private SimpleMaterial( Texture texture, float specularIntensity, float specularPower )
    {
        this.texture = texture;
        this.specularIntensity = specularIntensity;
        this.specularPower = specularPower;
    }

    public Texture getTexture() { return texture; }
    public float getSpecularIntensity() { return specularIntensity; }
    public float getSpecularPower() { return specularPower; }

    public static class MaterialBuilder
    {
        private Texture texture;
        private float specularIntensity = 1;
        private float specularPower = 0;

        public MaterialBuilder setTexture( Texture texture )
        {
            this.texture = texture;
            return this;
        }

        public MaterialBuilder setSpecularIntensity( float specularIntensity )
        {
            this.specularIntensity = specularIntensity;
            return this;
        }

        public MaterialBuilder setSpecularPower( float specularPower )
        {
            this.specularPower = specularPower;
            return this;
        }

        public SimpleMaterial build()
        {
            return new SimpleMaterial( texture, specularIntensity, specularPower );
        }
    }
}
