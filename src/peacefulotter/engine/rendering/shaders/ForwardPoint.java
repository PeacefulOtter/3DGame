package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.components.lights.PointLight;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.components.Camera;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

import java.awt.*;

public class ForwardPoint extends Shader
{
    private static ForwardPoint instance = new ForwardPoint();

    private ForwardPoint()
    {
        super( "forward-point" );
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

        setUniformPointLight( "pointLight", (PointLight)renderingEngine.getActiveLight() );
    }

    public void setUniformPointLight( String uniformName, PointLight pointLight )
    {
        setUniformBaseLight( uniformName + ".base", pointLight );
        setUniformF( uniformName + ".attenuation.constant", pointLight.getAttenuation().getConstant() );
        setUniformF( uniformName + ".attenuation.linear", pointLight.getAttenuation().getLinear() );
        setUniformF( uniformName + ".attenuation.exponent", pointLight.getAttenuation().getExponent() );
        setUniformVector( uniformName + ".position", pointLight.getTransform().getTransformedTranslation() );
        setUniformF( uniformName + ".range", pointLight.getRange() );
    }


    public static Shader getInstance() { return instance; }
}
