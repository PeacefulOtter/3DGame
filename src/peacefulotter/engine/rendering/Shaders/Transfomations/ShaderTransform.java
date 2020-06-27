package peacefulotter.engine.rendering.Shaders.Transfomations;

import peacefulotter.engine.rendering.Camera;
import peacefulotter.engine.core.Maths.Matrix4f;
import peacefulotter.engine.core.Maths.Vector3f;

public class ShaderTransform
{
    private static final STranslation translation = new STranslation();
    private static final SRotation rotation = new SRotation();
    private static final SScale scale = new SScale();
    private static final SProjection projection = new SProjection();

    private static Camera camera;

    public Matrix4f getTransformationMatrix()
    {
        return translation.getTranslationMatrix().mul(
                rotation.getRotationMatrix().mul(
                        scale.getScaleMatrix()
                ) );
    }

    public Matrix4f getProjectedTransformationMatrix()
    {
        Matrix4f projectionMatrix = projection.getProjectionMatrix();
        Matrix4f cameraRotation = new Matrix4f().initCamera( camera.getForward(), camera.getUpward() );
        Vector3f cameraPos = camera.getPosition();
        Matrix4f cameraTranslation = new Matrix4f().initTranslation( -cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ() );
        return projectionMatrix.mul( cameraRotation.mul( cameraTranslation.mul( getTransformationMatrix() ) ) );
    }

    public ShaderTransform setTranslation( float x, float y, float z )
    {
        translation.setTranslation( x, y , z );
        return this;
    }

    public ShaderTransform setTranslation( Vector3f vector )
    {
        translation.setTranslation( vector );
        return this;
    }

    public ShaderTransform setRotation( float x, float y, float z )
    {
        rotation.setRotation( x, y , z );
        return this;
    }

    public ShaderTransform setRotation( Vector3f vector )
    {
        rotation.setRotation( vector );
        return this;
    }

    public ShaderTransform setScale( float x, float y, float z )
    {
        scale.setScale( x, y, z );
        return this;
    }

    public ShaderTransform setScale( Vector3f vector )
    {
        scale.setScale( vector );
        return this;
    }

    public static void setProjection( float fov, float width, float height, float zNear, float zFar )
    {
        projection.setProjection( fov, width, height, zNear, zFar );
    }

    public static Camera getCamera() { return camera; }
    public static void setCamera( Camera camera ) { ShaderTransform.camera = camera; }
}
