package peacefulotter.game;

import peacefulotter.game.Display.Mesh;
import peacefulotter.game.Display.Shader;
import peacefulotter.game.Display.Vertex;
import peacefulotter.game.Maths.Vector3f;
import peacefulotter.game.Utils.IO.Input;
import peacefulotter.game.Utils.ResourceLoader;
import peacefulotter.game.Utils.Time;

public class Game
{
    private Mesh mesh;
    private Shader shader;

    public Game( long window )
    {
        Input.initInputs( window );
        mesh = new Mesh();
        shader = new Shader();

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
    }


    float temp = 0.0f;

    public void update()
    {
        temp += 0.003;
        shader.setUniformF( "uniformFloat", (float)Math.sin( temp ) );
    }

    public void render()
    {
        shader.bind();
        mesh.draw();
    }
}
