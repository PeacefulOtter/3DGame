package peacefulotter.engine.rendering.Shaders;

import peacefulotter.engine.rendering.Graphics.Material;
import peacefulotter.engine.core.Maths.Matrix4f;
import peacefulotter.engine.rendering.RenderUtil;
import peacefulotter.engine.Utils.ResourceLoader;

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

    public void updateUniforms( Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material )
    {
        if ( material.getTexture() != null )
            material.getTexture().bind();
        else
            RenderUtil.unbindTextures();
        setUniformMatrix( "transform", projectedMatrix );
        setUniformVector( "color", material.getColor() );
    }

    public static Shader getInstance() { return BasicShader.instance; }
}
