package peacefulotter.engine.rendering;

import peacefulotter.engine.Utils.Time;
import peacefulotter.engine.core.Maths.Matrix4f;
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

    private Vector3f position;
    private Vector3f forward, upward;
    private GameObject parent;
    private Matrix4f projection;

    private SpotLight spotLight;

    public Camera( float fov, float aspectRatio, float zNear, float zFar )
    {
        this.position = new Vector3f( 0, 0, 0 );
        this.forward = new Vector3f( 0, 0, 1 );
        this.upward = new Vector3f( 0, 1, 0 );
        this.projection = new Matrix4f().initPerspective( fov, aspectRatio, zNear, zFar );
    }

    public Matrix4f getViewProjection() {
        Matrix4f cameraRotation = new Matrix4f().initRotation( forward, upward );
        Matrix4f cameraTranslation = new Matrix4f().initTranslation( -position.getX(), -position.getY(), -position.getZ() );
        return projection.mul( cameraRotation.mul( cameraTranslation ) );
    }

    public void setParent( GameObject parent ) { this.parent = parent; }

    @Override
    public void init()
    {
        Input.addKeyCallback( GLFW_KEY_W, ( deltaTime ) -> move( getForward(), 0.02f ) );
        Input.addKeyCallback( GLFW_KEY_D, ( deltaTime ) -> move( getRight(), 0.02f ) );
        Input.addKeyCallback( GLFW_KEY_S, ( deltaTime ) -> move( getForward(), -0.02f ) );
        Input.addKeyCallback( GLFW_KEY_A, ( deltaTime ) -> move( getLeft(), 0.02f ) );

        Input.addKeyCallback( GLFW_KEY_UP, ( deltaTime ) -> rotateX( -0.5f ) );
        Input.addKeyCallback( GLFW_KEY_RIGHT, ( deltaTime ) -> rotateY( 0.5f ) );
        Input.addKeyCallback( GLFW_KEY_DOWN, ( deltaTime ) -> rotateX( 0.5f ) );
        Input.addKeyCallback( GLFW_KEY_LEFT, ( deltaTime ) -> rotateY( -0.5f ) );

        Input.addMouseCallback( MOUSE_PRIMARY, ( deltaTime ) -> { } );
        Input.addMouseCallback( MOUSE_SECONDARY, ( deltaTime ) -> { } );

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

        Vector2f deltaPos = Input.getMousePosition().sub( Window.getCenter() );
        float angle = deltaTime / 10;
        rotateY( deltaPos.getX() * angle );
        rotateX( deltaPos.getY() * angle );
    }

    @Override
    public void render( Shader shader ) { }

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

