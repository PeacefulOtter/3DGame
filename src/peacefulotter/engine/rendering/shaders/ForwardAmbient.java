package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.rendering.Camera;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;
import peacefulotter.engine.utils.ResourceLoader;

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

    public void updateUniforms( STransform transform, Material material )
    {
        material.getTexture().bind();

        Camera camera = getRenderingEngine().getCamera();
        Matrix4f worldMatrix = transform.getTransformationMatrix();
        Matrix4f projectedMatrix = camera.getViewProjection().mul( worldMatrix );

        setUniformMatrix( "MVP", projectedMatrix );
        setUniformVector( "ambientIntensity", getRenderingEngine().getAmbientLight() );
    }

    public static Shader getInstance() { return instance; }
}