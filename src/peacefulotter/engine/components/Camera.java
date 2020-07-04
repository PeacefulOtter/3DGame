

package peacefulotter.engine.components;

import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.Window;
import peacefulotter.engine.utils.IO.IOExecutable;
import peacefulotter.engine.utils.IO.Input;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;

public class Camera extends GameComponent
{
    private static final Vector3f Y_AXIS = new Vector3f( 0, 1, 0 );
    private static final float rotationSensitivity = 130;
    private static final float movingSensitivity = 35;

    private final Matrix4f projection;

    public Camera( float fovDeg, float aspectRatio, float zNear, float zFar )
    {
        this.projection = new Matrix4f().initPerspective( fovDeg, aspectRatio, zNear, zFar );
    }

    @Override
    public void addToEngine( CoreEngine engine )
    {
        engine.getRenderingEngine().setCamera( this );
    }

    public Matrix4f getViewProjection()
    {
        Vector3f pos = getTransform().getTranslation().mul( -1 );
        Matrix4f cameraRotation = getTransform().getRotation().conjugate().toRotationMatrix();
        Matrix4f cameraTranslation = new Matrix4f().initTranslation( pos.getX(), pos.getY(), pos.getZ() );

        return projection.mul( cameraRotation.mul( cameraTranslation ) );
    }

    @Override
    public void init()
    {
        Input.addKeyCallback( GLFW_KEY_W, ( deltaTime ) -> move( getForward(), deltaTime ) );
        Input.addKeyCallback( GLFW_KEY_D, ( deltaTime ) -> move( getRight(),   deltaTime ) );
        Input.addKeyCallback( GLFW_KEY_S, ( deltaTime ) -> move( getBackward(), deltaTime ) );
        Input.addKeyCallback( GLFW_KEY_A, ( deltaTime ) -> move( getLeft(),   deltaTime ) );
        Input.addKeyCallback( GLFW_KEY_SPACE,         ( deltaTime ) -> move( getUp(), deltaTime ) );
        Input.addKeyCallback( GLFW_KEY_LEFT_CONTROL,  ( deltaTime ) -> move( getDown(), deltaTime ) );

        Input.addKeyCallback( GLFW_KEY_UP,    ( deltaTime ) -> rotateX( -deltaTime * rotationSensitivity ) );
        Input.addKeyCallback( GLFW_KEY_RIGHT, ( deltaTime ) -> rotateY(  deltaTime * rotationSensitivity ) );
        Input.addKeyCallback( GLFW_KEY_DOWN,  ( deltaTime ) -> rotateX(  deltaTime * rotationSensitivity ) );
        Input.addKeyCallback( GLFW_KEY_LEFT,  ( deltaTime ) -> rotateY( -deltaTime * rotationSensitivity ) );
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
        getTransform().setTranslation(
                getTransform().getTranslation().add(
                        direction.mul( amount * movingSensitivity ) ) );
    }

    private void rotateX( float angleDeg ) { getTransform().rotate( getRight(), angleDeg ); }

    private void rotateY( float angleDeg ) { getTransform().rotate( Y_AXIS, angleDeg ); }

    public void addMouseCallback( int keyCode, IOExecutable exec )
    {
        Input.addMouseCallback( keyCode, exec );
    }


    public Vector3f getForward()  { return getTransform().getRotation().getForward(); }
    public Vector3f getBackward() { return getTransform().getRotation().getBack(); }
    public Vector3f getUp()       { return getTransform().getRotation().getUp(); }
    public Vector3f getDown()     { return getTransform().getRotation().getDown(); }
    public Vector3f getRight()    { return getTransform().getRotation().getRight(); }
    public Vector3f getLeft()     { return getTransform().getRotation().getLeft(); }

}
