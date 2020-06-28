package peacefulotter.engine.components.lights;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.shaders.Attenuation;
import peacefulotter.engine.rendering.shaders.ForwardSpot;

public class SpotLight extends PointLight
{
    private Vector3f direction;
    private float cutoff;

    public SpotLight( Vector3f color, float intensity, Attenuation attenuation, Vector3f position, Vector3f direction, float cutoff )
    {
        super( color, intensity, attenuation, position );
        this.direction = direction.normalize();
        this.cutoff = cutoff;
        setShader( ForwardSpot.getInstance() );
    }

    public Vector3f getDirection() { return direction; }
    public void setDirection( Vector3f direction ) { this.direction = direction.normalize(); }

    public float getCutoff() { return cutoff; }
    public void setCutoff( float cutoff ) { this.cutoff = cutoff; }
}
