

package peacefulotter.engine.components;

import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector3f;

public class Camera extends GameComponent
{
    private final Matrix4f projection;
    private Vector3f innerTranslation;

    public Camera( float fovDeg, float aspectRatio, float zNear, float zFar )
    {
        this.projection = new Matrix4f().initPerspective( fovDeg, aspectRatio, zNear, zFar );
        this.innerTranslation = Vector3f.getZero();
    }

    public void setInnerTranslation( Vector3f translation )
    {
        innerTranslation = translation;
    }

    @Override
    public void addToEngine( CoreEngine engine )
    {
        engine.getRenderingEngine().setCamera( this );
    }

    public Matrix4f getViewProjection()
    {
        Vector3f pos = getTransform().getTranslation().add( innerTranslation ).mul( -1 );
        Matrix4f cameraRotation = getTransform().getRotation().conjugate().toRotationMatrix();
        Matrix4f cameraTranslation = new Matrix4f().initTranslation( pos.getX(), pos.getY(), pos.getZ() );

        return projection.mul( cameraRotation.mul( cameraTranslation ) );
    }


    @Override
    public void update( float deltaTime )
    {
        // spotLight.getPointLight().setPosition( position );
        // spotLight.setDirection( forward );
    }
}
