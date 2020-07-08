

package peacefulotter.engine.components;

import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.Window;
import peacefulotter.engine.utils.IO.Input;

import static org.lwjgl.glfw.GLFW.*;

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

    private void rotateX( float angleDeg ) { getTransform().rotate( getRight(), angleDeg ); }

    private void rotateY( float angleDeg ) { getTransform().rotate( Y_AXIS, angleDeg ); }

    public Vector3f getRight()    { return getTransform().getRotation().getRight(); }

}
