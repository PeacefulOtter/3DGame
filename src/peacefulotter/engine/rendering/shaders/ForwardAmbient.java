package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.components.Camera;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

public class ForwardAmbient extends Shader
{
    private static ForwardAmbient instance = new ForwardAmbient();

    private ForwardAmbient()
    {
        super();
        addVertexShaderFromFile( "forward-ambient.vs" );
        addFragmentShaderFromFile( "forward-ambient.fs" );

        setAttribLocation("position", 0 );
        setAttribLocation("textureCoord", 1 );

        compileShader();

        addUniform( "MVP" );
        addUniform( "ambientIntensity" );
    }

    public void updateUniforms( STransform transform, Material material, RenderingEngine renderingEngine )
    {
        material.getTexture( "diffuse" ).bind();

        Camera camera = renderingEngine.getCamera();
        Matrix4f worldMatrix = transform.getTransformationMatrix();
        Matrix4f projectedMatrix = camera.getViewProjection().mul( worldMatrix );

        setUniformMatrix( "MVP", projectedMatrix );
        setUniformVector( "ambientIntensity", renderingEngine.getAmbientLight() );
    }

    public static Shader getInstance() { return instance; }
}
