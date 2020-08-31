package peacefulotter.engine.rendering.graphics;

public class SimpleMaterial
{
    private final Texture texture, normal;
    private final float specularIntensity, specularPower;

    private SimpleMaterial( Texture texture, Texture normal, float specularIntensity, float specularPower )
    {
        this.texture = texture;
        this.normal = normal;
        this.specularIntensity = specularIntensity;
        this.specularPower = specularPower;
    }

    public Texture getTexture() { return texture; }
    public Texture getNormal() { return normal; }
    public float getSpecularIntensity() { return specularIntensity; }
    public float getSpecularPower() { return specularPower; }

    public static class MaterialBuilder
    {
        private Texture texture, normal;
        private float specularIntensity = 1;
        private float specularPower = 0;

        public MaterialBuilder setTexture( Texture texture )
        {
            this.texture = texture;
            return this;
        }

        public MaterialBuilder setNormalTexture( Texture normal )
        {
            this.normal = normal;
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
            return new SimpleMaterial( texture, normal, specularIntensity, specularPower );
        }
    }
}
