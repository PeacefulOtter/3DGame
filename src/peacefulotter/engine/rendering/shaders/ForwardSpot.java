package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.components.lights.SpotLight;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.components.Camera;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

public class ForwardSpot extends Shader
{
    private static ForwardSpot instance = new ForwardSpot();

    private ForwardSpot()
    {
        super( "forward-spot" );
    }

    public void updateUniforms( STransform transform, Material material, RenderingEngine renderingEngine )
    {
        material.getTexture( "diffuse" ).bind();

        Camera camera = renderingEngine.getCamera();
        Matrix4f worldMatrix = transform.getTransformationMatrix();
        Matrix4f projectedMatrix = camera.getViewProjection().mul( worldMatrix );

        setUniformMatrix( "model", worldMatrix );
        setUniformMatrix( "MVP", projectedMatrix );
        setUniformF( "specularIntensity", material.getFloat( "specularIntensity" ) );
        setUniformF( "specularExponent", material.getFloat( "specularExponent" ) );
        setUniformVector( "eyePos", camera.getTransform().getTransformedTranslation() );

        setUniformSpotLight( "spotLight", (SpotLight)renderingEngine.getActiveLight() );
    }

    public void setUniformSpotLight( String uniformName, SpotLight spotLight )
    {
        setUniformPointLight( uniformName + ".pointLight", spotLight );
        setUniformVector( uniformName + ".direction", spotLight.getDirection() );
        setUniformF( uniformName + ".cutoff", spotLight.getCutoff() );
    }

    public static Shader getInstance() { return instance; }
}
