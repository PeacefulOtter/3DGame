package peacefulotter.engine.rendering.Shaders.Transfomations;

import peacefulotter.engine.core.Maths.Matrix4f;

public class SProjection
{
    // how close before it clips / disappears
    private float zNear, zFar;
    private float screenWidth, screenHeight;
    private float fov;

    public Matrix4f getProjectionMatrix()
    {
        return new Matrix4f().initProjection(
                fov, screenWidth, screenHeight, zNear, zFar );
    }

    public void setProjection( float fov, float width, float height, float zNear, float zFar )
    {
        this.fov = fov;
        this.screenWidth = width;
        this.screenHeight = height;
        this.zNear = zNear;
        this.zFar = zFar;
    }
}
