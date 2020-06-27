package peacefulotter.game.Display.Shaders;

import peacefulotter.game.Display.Graphics.Material;
import peacefulotter.game.Maths.Matrix4f;
import peacefulotter.game.Utils.RenderUtil;
import peacefulotter.game.Utils.ResourceLoader;

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

    public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material )
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
