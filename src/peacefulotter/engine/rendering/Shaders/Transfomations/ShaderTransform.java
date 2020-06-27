package peacefulotter.engine.rendering.Shaders.Transfomations;

import peacefulotter.engine.core.Maths.Matrix4f;
import peacefulotter.engine.core.Maths.Vector3f;

public class ShaderTransform
{
    private final STranslation translation = new STranslation();
    private final SRotation rotation = new SRotation();
    private final SScale scale = new SScale();

    public Matrix4f getTransformationMatrix()
    {
        return translation.getTranslationMatrix().mul(
                rotation.getRotationMatrix().mul(
                        scale.getScaleMatrix()
                ) );
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

}
