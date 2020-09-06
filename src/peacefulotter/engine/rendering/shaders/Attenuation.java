package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.core.maths.Vector3f;

public class Attenuation extends Vector3f
{
    public Attenuation( float constant, float linear, float exponent )
    {
        super( constant, linear, exponent );
    }

    public float getConstant() { return getX(); }
    public float getLinear()   { return getY(); }
    public float getExponent() { return getZ(); }

    public static final Attenuation DEFAULT = new Attenuation( 0, 0.01f, 0 );
}
