package peacefulotter.engine.rendering;

import peacefulotter.engine.core.Maths.Vector2f;
import peacefulotter.engine.core.Maths.Vector3f;
import peacefulotter.engine.Utils.IO.Input;
import peacefulotter.engine.core.elementary.GameComponent;
import peacefulotter.engine.core.elementary.GameObject;
import peacefulotter.engine.rendering.Shaders.*;
import peacefulotter.engine.rendering.Shaders.Transfomations.ShaderTransform;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static peacefulotter.engine.Utils.IO.Input.MOUSE_PRIMARY;
import static peacefulotter.engine.Utils.IO.Input.MOUSE_SECONDARY;


public class Camera implements GameComponent
{
    public static final Vector3f Y_AXIS = new Vector3f( 0, 1, 0 );

    private final Vector2f centerWindow;

    private Vector3f position;
    private Vector3f forward, upward;
    private GameObject parent;

    private SpotLight spotLight;

    public Camera( Vector2f centerWindow )
    {
        this(
                new Vector3f( 0, 0, 0 ),
                new Vector3f( 0, 0, 1 ),
                new Vector3f( 0, 1, 0 ),
                centerWindow );
    }

    public Camera( Vector3f position, Vector3f forward, Vector3f upward, Vector2f centerWindow )
    {
        this.position = position;
        this.forward = forward.normalize();
        this.upward = upward.normalize();
        this.centerWindow = centerWindow;
    }

    public void setParent( GameObject parent ) { this.parent = parent; }

    @Override
    public void init()
    {
        Input.addKeyCallback( GLFW_KEY_W, () -> move( getForward(), 0.02f ) );
        Input.addKeyCallback( GLFW_KEY_D, () -> move( getRight(), 0.02f ) );
        Input.addKeyCallback( GLFW_KEY_S, () -> move( getForward(), -0.02f ) );
        Input.addKeyCallback( GLFW_KEY_A, () -> move( getLeft(), 0.02f ) );

        Input.addKeyCallback( GLFW_KEY_UP, () -> rotateX( -0.5f ) );
        Input.addKeyCallback( GLFW_KEY_RIGHT, () -> rotateY( 0.5f ) );
        Input.addKeyCallback( GLFW_KEY_DOWN, () -> rotateX( 0.5f ) );
        Input.addKeyCallback( GLFW_KEY_LEFT, () -> rotateY( -0.5f ) );

        Input.addMouseCallback( MOUSE_PRIMARY, () -> { } );
        Input.addMouseCallback( MOUSE_SECONDARY, () -> { } );

        spotLight = new SpotLight(
                new PointLight(
                        new BaseLight( new Vector3f( 1, 0.5f, 0 ), 0.8f ),
                        new Attenuation( 0, 0.1f, 0.02f ),
                        new Vector3f( -2, 0, 5 ),
                        30
                ),
                new Vector3f( 1, 1, 1 ),
                0.7f
        );
        PhongShader.setSpotLights( new SpotLight[] { spotLight } );
    }

    @Override
    public void update( float deltaTime )
    {
        spotLight.getPointLight().setPosition( position );
        spotLight.setDirection( forward );

        if ( Input.getMousePrimaryState() == Input.MOUSE_RELEASED ) return;

        Vector2f deltaPos = Input.getMousePosition().sub( centerWindow );
        rotateY( deltaPos.getX() * deltaTime );
        rotateX( deltaPos.getY() * deltaTime );
    }

    @Override
    public void render() { }

    public void move( Vector3f direction, float amount )
    {
        position = position.add( direction.mul( amount ) );
    }

    public void rotateX( float angle )
    {
        Vector3f horAxis = Y_AXIS.cross( forward ).normalize();
        forward = forward.rotate( angle, horAxis ).normalize();
        upward = forward.cross( horAxis ).normalize();
    }

    public void rotateY( float angle )
    {
        Vector3f horAxis = Y_AXIS.cross( forward ).normalize();
        forward = forward.rotate( angle, Y_AXIS ).normalize();
        upward = forward.cross( horAxis ).normalize();
    }

    public Vector3f getLeft()  { return forward.cross( upward ).normalize(); }
    public Vector3f getRight() { return upward.cross( forward ).normalize(); }

    public Vector3f getPosition() { return position; }
    public void setPosition( Vector3f position ) { this.position = position; }

    public Vector3f getForward() { return forward; }
    public void setForward( Vector3f forward ) { this.forward = forward; }

    public Vector3f getUpward() { return upward; }
    public void setUpward( Vector3f upward ) { this.upward = upward; }
}

