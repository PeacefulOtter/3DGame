package peacefulotter.engine.components.lights;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.shaders.Attenuation;
import peacefulotter.engine.rendering.shaders.ShaderTypes;

public class PointLight extends BaseLight
{
    private Attenuation attenuation;
    private float range;

    public PointLight( Vector3f color, float intensity, Attenuation attenuation )
    {
        super( color, intensity );
        this.attenuation = attenuation;
        calcAndUpdateRange();
        setShader( ShaderTypes.POINT.getShader() );
    }

    public void calcAndUpdateRange()
    {
        float a = attenuation.getExponent();
        float b = attenuation.getLinear();
        float c = attenuation.getConstant();
        range = 100;
        /*if ( a != 0 )
            range = (float) ( ( -b + Math.sqrt( b * b - 4 * a * c ) ) / ( 2 * a ) );
        else
            range = 10; // to change*/
    }

    public Attenuation getAttenuation() { return attenuation; }
    public void setAttenuation( Attenuation attenuation )
    {
        this.attenuation = attenuation;
        calcAndUpdateRange();
    }

    public Vector3f getPosition() { return getTransform().getTranslation(); }
    public void setPosition( Vector3f position ) { getTransform().getTranslation().set( position ); }

    public float getRange() { return range; }
}
