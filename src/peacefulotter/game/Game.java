package peacefulotter.game;

import peacefulotter.game.Display.Camera;
import peacefulotter.game.Display.Mesh;
import peacefulotter.game.Display.Shader;
import peacefulotter.game.Display.ShaderEffects.ShaderTransform;
import peacefulotter.game.Utils.IO.Input;
import peacefulotter.game.Utils.ResourceLoader;

public class Game
{
    private Mesh mesh;
    private Shader shader;
    private ShaderTransform transform;
    private Camera camera;

    public Game( long window, int wWidth, int wHeight )
    {
        Input.initInputs( window );
        mesh = new ResourceLoader().loadMesh( "cube.obj" );
        shader = new Shader();
        transform = new ShaderTransform();
        transform.setProjection( 70f, wWidth, wHeight, 0.1f, 1000f );
        camera = new Camera();
        transform.setCamera( camera );

        /*Vertex[] vertices = new Vertex[] {
                new Vertex( new Vector3f( -1, -1, 0 ) ),
                new Vertex( new Vector3f(  0,  1, 0 ) ),
                new Vertex( new Vector3f(  1, -1, 0 ) ),
                new Vertex( new Vector3f(  0, -1, 1 ) )
        };
        int[] indices = new int[] { 0, 1, 3,
                                    3, 1, 2,
                                    2, 1, 0,
                                    0, 2, 3 };
        mesh.addVertices( vertices, indices );*/

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
        temp += 0.001;
        cosTemp = (float)Math.cos( temp );
        sinTemp = (float)Math.sin( temp );
    }

    public void render()
    {
        shader.bind();
        shader.setUniformF( "uniformFloat", 1 );
        transform
                .setTranslation( 0, 0, 5 )
                .setRotation(0, sinTemp * 180, 0 );
                //  .setScale( 0.5f, 0.5f, 0.5f );
        shader.setUniformMatrix( "transform", transform.getProjectedTransformationMatrix() );
        mesh.draw();
    }
}
