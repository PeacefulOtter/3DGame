package peacefulotter.game;

import peacefulotter.game.Display.Mesh;
import peacefulotter.game.Display.Shader;
import peacefulotter.game.Display.ShaderEffects.STranslation;
import peacefulotter.game.Display.ShaderEffects.ShaderTransform;
import peacefulotter.game.Display.Vertex;
import peacefulotter.game.Maths.Vector3f;
import peacefulotter.game.Utils.IO.Input;
import peacefulotter.game.Utils.ResourceLoader;

public class Game
{
    private Mesh mesh;
    private Shader shader;
    private ShaderTransform transform;

    public Game( long window )
    {
        Input.initInputs( window );
        mesh = new Mesh();
        shader = new Shader();
        transform = new ShaderTransform();

        Vertex[] data = new Vertex[] {
                new Vertex( new Vector3f( -1, -1, 0 ) ),
                new Vertex( new Vector3f(  0,  1, 0 ) ),
                new Vertex( new Vector3f(  1, -1, 0 ) )
        };
        mesh.addVertices( data );

        shader.addVertexShader( new ResourceLoader().loadShader( "basicVertex.vs" ) );
        shader.addFragmentShader( new ResourceLoader().loadShader( "basicFragment.fs" ) );
        shader.compileShader();

        shader.addUniform( "uniformFloat" );
        shader.addUniform( "transform" );
    }


    float temp = 0.0f;
    float cosTemp, sinTemp;

    public void update()
    {
        temp += 0.003;
        cosTemp = (float)Math.cos( temp );
        sinTemp = (float)Math.sin( temp );
    }

    public void render()
    {
        shader.bind();
        shader.setUniformF( "uniformFloat", (float)Math.sin( temp ) );
        transform
                .setTranslation( cosTemp,  sinTemp, 0 )
                .setRotation( 0, 0, sinTemp * 180 );
        shader.setUniformMatrix( "transform", transform.getTransformMatrix() );
        mesh.draw();
    }
}
