package peacefulotter.engine.components.lights;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.shaders.Attenuation;
import peacefulotter.engine.rendering.shaders.ShaderTypes;

public class SpotLight extends PointLight
{
    private float cutoff;

    public SpotLight( Vector3f color, float intensity, Attenuation attenuation, float cutoff )
    {
        super( color, intensity, attenuation );
        this.cutoff = cutoff;
        setShader( ShaderTypes.SPOT.getShader() );
    }

    public float getCutoff() { return cutoff; }
    public void setCutoff( float cutoff ) { this.cutoff = cutoff; }
}
