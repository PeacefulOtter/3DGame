package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.components.lights.DirectionalLight;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.components.Camera;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

public class ForwardDirectional extends Shader
{
    private static ForwardDirectional instance = new ForwardDirectional();

    private ForwardDirectional()
    {
        super();
        addVertexShaderFromFile("forward-directional.vs" );
        addFragmentShaderFromFile( "forward-directional.fs" );

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
        addUniform( "dirLight.base.color" );
        addUniform( "dirLight.base.intensity" );
        addUniform( "dirLight.direction" );
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
        setUniformDirLight( "dirLight", (DirectionalLight) getRenderingEngine().getActiveLight());
    }

    public static Shader getInstance() { return instance; }
}
