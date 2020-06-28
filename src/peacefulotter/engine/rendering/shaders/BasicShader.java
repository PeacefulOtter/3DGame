package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.rendering.Camera;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.utils.ResourceLoader;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

public class BasicShader extends Shader
{
    private static BasicShader instance = new BasicShader();

    private BasicShader()
    {
        super();
        addVertexShader( new ResourceLoader().loadShader( "basicVertex.vs" ) );
        addFragmentShader( new ResourceLoader().loadShader( "basicFragment.fs" ) );
        compileShader();

        addUniform( "transform" );
        addUniform( "color" );
    }

    public void updateUniforms(STransform transform, Material material )
    {
        material.getTexture().bind();

        Camera camera = getRenderingEngine().getCamera();
        Matrix4f worldMatrix = transform.getTransformationMatrix();
        Matrix4f projectedMatrix = camera.getViewProjection().mul( worldMatrix );

        setUniformMatrix( "transform", projectedMatrix );
        setUniformVector( "color", material.getColor() );
    }

    public static Shader getInstance() { return BasicShader.instance; }
}
