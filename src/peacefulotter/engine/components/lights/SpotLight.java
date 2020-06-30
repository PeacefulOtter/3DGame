package peacefulotter.engine.components.lights;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.shaders.Attenuation;
import peacefulotter.engine.rendering.shaders.ForwardSpot;

public class SpotLight extends PointLight
{
    private float cutoff;

    public SpotLight( Vector3f color, float intensity, Attenuation attenuation, float cutoff )
    {
        super( color, intensity, attenuation );
        this.cutoff = cutoff;
        setShader( ForwardSpot.getInstance() );
    }

    public Vector3f getDirection() { return getTransform().getRotation().getForward(); }

    public float getCutoff() { return cutoff; }
    public void setCutoff( float cutoff ) { this.cutoff = cutoff; }
}
