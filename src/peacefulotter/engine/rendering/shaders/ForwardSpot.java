package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.rendering.Camera;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

public class ForwardSpot extends Shader
{
    private static ForwardSpot instance = new ForwardSpot();

    private ForwardSpot()
    {
        super();
        addVertexShaderFromFile("forward-spot.vs" );
        addFragmentShaderFromFile( "forward-spot.fs" );

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

        addUniform( "spotLight.pointLight.base.color" );
        addUniform( "spotLight.pointLight.base.intensity" );
        addUniform( "spotLight.pointLight.attenuation.constant" );
        addUniform( "spotLight.pointLight.attenuation.linear" );
        addUniform( "spotLight.pointLight.attenuation.exponent" );
        addUniform( "spotLight.pointLight.position" );
        addUniform( "spotLight.pointLight.range" );
        addUniform( "spotLight.direction" );
        addUniform( "spotLight.cutoff" );
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

        setUniformSpotLight( "spotLight", getRenderingEngine().getSpotLight() );
    }

    public void setUniformSpotLight( String uniformName, SpotLight spotLight )
    {
        setUniformPointLight( uniformName + ".pointLight", spotLight.getPointLight() );
        setUniformVector( uniformName + ".direction", spotLight.getDirection() );
        setUniformF( uniformName + ".cutoff", spotLight.getCutoff() );
    }

    public static Shader getInstance() { return instance; }
}