package peacefulotter.game.Display.ShaderEffects;

import peacefulotter.game.Maths.Matrix4f;
import peacefulotter.game.Maths.Vector3f;

public class ShaderTransform
{
    private STranslation translation = new STranslation();
    private SRotation rotation = new SRotation();

    public Matrix4f getTransformMatrix()
    {
        return translation.getTranslationMatrix().mul( rotation.getRotationMatrix() );
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
}
