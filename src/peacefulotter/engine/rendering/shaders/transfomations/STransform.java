package peacefulotter.engine.rendering.shaders.transfomations;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector3f;

public class STransform
{
    private final STranslation translation = new STranslation();
    private final SRotation rotation = new SRotation();
    private final SScale scale = new SScale();

    private STransform parent;

    public Matrix4f getTransformationMatrix()
    {
        return translation.getTranslationMatrix().mul(
                rotation.getRotationMatrix().mul(
                        scale.getScaleMatrix()
                ) );
    }

    /*public STransform setTranslation(float x, float y, float z )
    {
        translation.setTranslation( x, y , z );
        return this;
    }*/

    /*public STransform setTranslation(Vector3f vector )
    {
        translation.setTranslation( vector );
        return this;
    }*/

    public Vector3f getTranslation() { return translation.getTranslationVector(); }

    public STransform setRotation( Quaternion quaternion )
    {
        rotation.setRotation( quaternion );
        return this;
    }

    public STransform setScale( float x, float y, float z )
    {
        scale.setScale( x, y, z );
        return this;
    }

    public STransform setScale( Vector3f vector )
    {
        scale.setScale( vector );
        return this;
    }

    public void setParent( STransform parent ) { this.parent = parent; }
}
