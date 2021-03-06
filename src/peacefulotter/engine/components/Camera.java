

package peacefulotter.engine.components;

import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.core.transfomations.STransform;
import peacefulotter.engine.rendering.Window;

public class Camera extends GameComponent
{
    private static final float FOV = 70f;
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 5000f;

    public static Vector3f PLAYER_CAMERA_TRANSLATION() { return new Vector3f(0, 6.5f, 0f ); }
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

    public Matrix4f getProjectionMatrix()
    {
        return projection;
    }

    public Matrix4f getViewMatrix()
    {
        STransform cameraTransform = getTransform();
        Vector3f pos = cameraTransform.getTransformedTranslation().mul( -1 );
        Matrix4f cameraRotation = cameraTransform.getTransformedRotation().conjugate().toRotationMatrix();
        Matrix4f cameraTranslation = new Matrix4f().initTranslation( pos.getX(), pos.getY(), pos.getZ() );
        return cameraRotation.mul( cameraTranslation );
    }


    public static class CameraBuilder
    {
        public static Camera getDefaultCamera()
        {
            return new Camera( FOV, Window.getRatio(), Z_NEAR, Z_FAR );
        }
    }
}
