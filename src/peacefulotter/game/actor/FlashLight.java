package peacefulotter.game.actor;

import peacefulotter.engine.components.lights.SpotLight;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.shaders.Attenuation;

public class FlashLight extends SpotLight
{
    private static final Vector3f FLASHLIGHT_TRANSLATION = new Vector3f( 0, 6f, 0 );

    public FlashLight( Vector3f color, float intensity, Attenuation attenuation, float cutoff )
    {
        super( color, intensity, attenuation, cutoff );
        getTransform()
                .rotate( getTransform().getRotation().getRight(), -5 )
                .translate( FLASHLIGHT_TRANSLATION );
    }
}
