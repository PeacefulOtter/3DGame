package peacefulotter.engine.components.lights;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.shaders.ShaderTypes;

public class DirectionalLight extends BaseLight
{
    public DirectionalLight( Vector3f color, float intensity )
    {
        super( color, intensity );
        setShader( ShaderTypes.DIRECTIONAL.getShader() );
    }

    public Vector3f getDirection() { return getTransform().getTransformedRotation().getForward(); }
}
