package peacefulotter.engine.components.lights;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.shaders.Attenuation;
import peacefulotter.engine.rendering.shaders.ForwardPoint;

public class PointLight extends BaseLight
{
    private Attenuation attenuation;
    private Vector3f position;
    private float range;

    public PointLight( Vector3f color, float intensity, Attenuation attenuation, Vector3f position )
    {
        super( color, intensity );
        this.attenuation = attenuation;
        this.position = position;
        calcAndUpdateRange();
        System.out.println(range);
        setShader( ForwardPoint.getInstance() );
    }

    public void calcAndUpdateRange()
    {
        float a = attenuation.getExponent();
        float b = attenuation.getLinear();
        float c = attenuation.getConstant();
        range = 10;
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

    public Vector3f getPosition() { return position; }
    public void setPosition( Vector3f position ) { this.position = position; }

    public float getRange() { return range; }
}
