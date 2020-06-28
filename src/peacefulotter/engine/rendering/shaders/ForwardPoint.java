package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.rendering.Camera;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

public class ForwardPoint extends Shader
{
    private static ForwardPoint instance = new ForwardPoint();

    private ForwardPoint()
    {
        super();
        addVertexShaderFromFile("forward-point.vs" );
        addFragmentShaderFromFile( "forward-point.fs" );

        setAttribLocation("position", 0 );
        setAttribLocation("textureCoord", 1 );
        setAttribLocation("normal", 2 );

        compileShader();

        addUniform( "model" );
        addUniform( "MVP" );
        addUniform( "specularIntensity" );
        addUniform( "specularExponent" );
        addUniform( "eyePos" );
        addUniform( "diffuse" );

        addUniform( "pointLight.base.color" );
        addUniform( "pointLight.base.intensity" );
        addUniform( "pointLight.attenuation.constant" );
        addUniform( "pointLight.attenuation.linear" );
        addUniform( "pointLight.attenuation.exponent" );
        addUniform( "pointLight.position" );
        addUniform( "pointLight.range" );
    }

    public void updateUniforms( STransform transform, Material material )
    {
        material.getTexture().bind();

        Camera camera = getRenderingEngine().getCamera();
        Matrix4f worldMatrix = transform.getTransformationMatrix();
        Matrix4f projectedMatrix = camera.getViewProjection().mul( worldMatrix );

        setUniformMatrix( "model", worldMatrix );
        setUniformMatrix( "MVP", projectedMatrix );
        setUniformF( "specularIntensity", material.getSpecularIntensity() );
        setUniformF( "specularExponent", material.getSpecularExponent() );
        setUniformVector( "eyePos", camera.getPosition() );

        setUniformPointLight( "pointLight", getRenderingEngine().getPointLight() );
    }

    public void setUniformPointLight( String uniformName, PointLight pointLight )
    {
        setUniformBaseLight( uniformName + ".base", pointLight.getBaseLight() );
        setUniformF( uniformName + ".attenuation.constant", pointLight.getAttenuation().getConstant() );
        setUniformF( uniformName + ".attenuation.linear", pointLight.getAttenuation().getLinear() );
        setUniformF( uniformName + ".attenuation.exponent", pointLight.getAttenuation().getExponent() );
        setUniformVector( uniformName + ".position", pointLight.getPosition() );
        setUniformF( uniformName + ".range", pointLight.getRange() );
    }


    public static Shader getInstance() { return instance; }
}
