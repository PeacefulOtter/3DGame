package peacefulotter.engine.rendering.Shaders;

import peacefulotter.engine.rendering.Graphics.Material;
import peacefulotter.engine.core.Maths.Matrix4f;
import peacefulotter.engine.rendering.BufferUtil;
import peacefulotter.engine.Utils.ResourceLoader;
import peacefulotter.engine.rendering.RenderingEngine;

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
        material.getTexture().bind();

        setUniformMatrix( "transform", projectedMatrix );
        setUniformVector( "color", material.getColor() );
    }

    public static Shader getInstance() { return BasicShader.instance; }
}
