package peacefulotter.engine.components;

import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.Window;
import peacefulotter.engine.utils.IO.Input;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static peacefulotter.engine.utils.IO.Input.MOUSE_PRIMARY;
import static peacefulotter.engine.utils.IO.Input.MOUSE_SECONDARY;


public class Camera extends GameComponent
{
    public static final Vector3f Y_AXIS = new Vector3f( 0, 1, 0 );

    private final Matrix4f projection;
    private Vector3f position;
    private Vector3f forward, upward;

    public Camera( float fov, float aspectRatio, float zNear, float zFar )
    {
        this.position = Vector3f.ZERO;
        this.forward = new Vector3f( 0, 0, 1 );
        this.upward = new Vector3f( 0, 1, 0 );
        this.projection = new Matrix4f().initPerspective( fov, aspectRatio, zNear, zFar );
    }

    @Override
    public void addToEngine(CoreEngine engine)
    {
        engine.getRenderingEngine().setCamera( this );
    }

    public Matrix4f getViewProjection()
    {
        Matrix4f cameraRotation = new Matrix4f().initRotation( forward, upward );
        Matrix4f cameraTranslation = new Matrix4f().initTranslation( -position.getX(), -position.getY(), -position.getZ() );
        return projection.mul( cameraRotation.mul( cameraTranslation ) );
    }

    @Override
    public void init()
    {
        Input.addKeyCallback( GLFW_KEY_W, ( deltaTime ) -> move( getForward(), deltaTime ) );
        Input.addKeyCallback( GLFW_KEY_D, ( deltaTime ) -> move( getRight(), deltaTime ) );
        Input.addKeyCallback( GLFW_KEY_S, ( deltaTime ) -> move( getForward(), -deltaTime ) );
        Input.addKeyCallback( GLFW_KEY_A, ( deltaTime ) -> move( getLeft(), deltaTime ) );

        Input.addKeyCallback( GLFW_KEY_UP, ( deltaTime ) -> rotateX( -deltaTime ) );
        Input.addKeyCallback( GLFW_KEY_RIGHT, ( deltaTime ) -> rotateY( deltaTime ) );
        Input.addKeyCallback( GLFW_KEY_DOWN, ( deltaTime ) -> rotateX( deltaTime ) );
        Input.addKeyCallback( GLFW_KEY_LEFT, ( deltaTime ) -> rotateY( -deltaTime ) );

        Input.addMouseCallback( MOUSE_PRIMARY, ( deltaTime ) -> { } );
        Input.addMouseCallback( MOUSE_SECONDARY, ( deltaTime ) -> { } );
    }

    @Override
    public void update( float deltaTime )
    {
        // spotLight.getPointLight().setPosition( position );
        // spotLight.setDirection( forward );

        if ( Input.getMousePrimaryState() == Input.MOUSE_RELEASED ) return;

        Vector2f deltaPos = Input.getMousePosition().sub( Window.getCenter() );
        float angle = deltaTime / 10;
        rotateY( deltaPos.getX() * angle );
        rotateX( deltaPos.getY() * angle );
    }

    public void move( Vector3f direction, float amount )
    {
        position = position.add( direction.mul( amount * 20 ) );
    }

    public void rotateX( float angle )
    {
        Vector3f horAxis = Y_AXIS.cross( forward ).normalize();
        forward = forward.rotate( horAxis, angle * 200 ).normalize();
        upward = forward.cross( horAxis ).normalize();
    }

    public void rotateY( float angle )
    {
        Vector3f horAxis = Y_AXIS.cross( forward ).normalize();
        forward = forward.rotate( Y_AXIS, angle * 200 ).normalize();
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

