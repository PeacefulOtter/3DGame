package peacefulotter.game.Display.Shaders;

import peacefulotter.game.Display.Camera;
import peacefulotter.game.Maths.Matrix4f;
import peacefulotter.game.Maths.Vector3f;

public class ShaderTransform
{
    private Camera camera;

    private STranslation translation = new STranslation();
    private SRotation rotation = new SRotation();
    private SScale scale = new SScale();
    private SProjection projection = new SProjection();

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

    public ShaderTransform setProjection( float fov, float width, float height, float zNear, float zFar )
    {
        projection.setProjection( fov, width, height, zNear, zFar );
        return this;
    }

    public Camera getCamera() { return camera; }
    public void setCamera( Camera camera ) { this.camera = camera; }
}
