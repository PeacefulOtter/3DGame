package peacefulotter.game;

import org.lwjgl.system.windows.INPUT;
import peacefulotter.game.Display.Camera;
import peacefulotter.game.Display.Graphics.Mesh;
import peacefulotter.game.Display.Graphics.Texture;
import peacefulotter.game.Display.Graphics.Vertex;
import peacefulotter.game.Display.Shaders.Shader;
import peacefulotter.game.Display.Shaders.ShaderTransform;
import peacefulotter.game.Display.Window;
import peacefulotter.game.Maths.Vector2f;
import peacefulotter.game.Maths.Vector3f;
import peacefulotter.game.Utils.IO.Input;
import peacefulotter.game.Utils.RenderUtil;
import peacefulotter.game.Utils.ResourceLoader;

import static org.lwjgl.glfw.GLFW.*;
import static peacefulotter.game.Utils.IO.Input.MOUSE_PRIMARY;
import static peacefulotter.game.Utils.IO.Input.MOUSE_SECONDARY;


public class Game
{
    private Window window;
    private Mesh mesh;
    private Shader shader;
    private ShaderTransform transform;
    private Camera camera;
    private Texture texture;

    public Game( Window window  )
    {
        Input.initInputs( window.getWindow() );
        this.window = window;
        mesh = new Mesh(); // new ResourceLoader().loadMesh( "cube.obj" );
        texture = new ResourceLoader().loadTexture( "test.png" );
        shader = new Shader();
        transform = new ShaderTransform();
        transform.setProjection( 70f, window.WIDTH, window.HEIGHT, 0.1f, 1000f );
        camera = new Camera();
        transform.setCamera( camera );

        Vertex[] vertices = new Vertex[] {
                new Vertex( new Vector3f( -1, -1, 0 ), new Vector2f( 0, 0 ) ),
                new Vertex( new Vector3f(  0,  1, 0 ), new Vector2f( 0.5f, 0 ) ),
                new Vertex( new Vector3f(  1, -1, 0 ), new Vector2f( 1.0f, 0 ) ),
                new Vertex( new Vector3f(  0, -1, 1 ), new Vector2f( 0, 0.5f ) )
        };
        int[] indices = new int[] { 3, 1, 0,
                                    2, 1, 3,
                                    0, 1, 2,
                                    0, 2, 3 };
        mesh.addVertices( vertices, indices );

        shader.addVertexShader( new ResourceLoader().loadShader( "basicVertex.vs" ) );
        shader.addFragmentShader( new ResourceLoader().loadShader( "basicFragment.fs" ) );
        shader.compileShader();

        shader.addUniform( "transform" );

        initCameraMovement();
    }

    private void initCameraMovement()
    {
        Input.addKeyCallback( GLFW_KEY_W, () -> {
            camera.move( camera.getForward(), 0.4f );
        } );
        Input.addKeyCallback( GLFW_KEY_D, () -> {
            camera.move( camera.getRight(), 0.4f );
        } );
        Input.addKeyCallback( GLFW_KEY_S, () -> {
            camera.move( camera.getForward(), -0.4f );
        } );
        Input.addKeyCallback( GLFW_KEY_A, () -> {
            camera.move( camera.getLeft(), 0.4f );
        } );

        Input.addKeyCallback( GLFW_KEY_UP, () -> {
            camera.rotateX( -2f );
        } );
        Input.addKeyCallback( GLFW_KEY_RIGHT, () -> {
            camera.rotateY( 2f );
        } );
        Input.addKeyCallback( GLFW_KEY_DOWN, () -> {
            camera.rotateX( 2f );
        } );
        Input.addKeyCallback( GLFW_KEY_LEFT, () -> {
            camera.rotateY( -2f );
        } );

        Input.addMouseCallback( MOUSE_PRIMARY, () -> { } );
        Input.addMouseCallback( MOUSE_SECONDARY, () -> { } );
    }



    float temp = 0.0f;
    float cosTemp, sinTemp;

    public void update()
    {
        temp += 0.001;
        cosTemp = (float)Math.cos( temp );
        sinTemp = (float)Math.sin( temp );

        camera.update( new Vector2f( window.WIDTH / 2, window.HEIGHT / 2 ) );
    }

    public void render()
    {
        RenderUtil.setClearColor( camera.getPosition().div( 2048f ).abs() );
        shader.bind();
        transform.setTranslation( 0, 0, 5 );
                //.setRotation(0, sinTemp * 180, 0 );
                //  .setScale( 0.5f, 0.5f, 0.5f );
        shader.setUniformMatrix( "transform", transform.getProjectedTransformationMatrix() );
        texture.bind();
        mesh.draw();
    }
}
