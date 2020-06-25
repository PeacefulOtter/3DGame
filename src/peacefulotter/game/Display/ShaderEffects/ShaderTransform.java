package peacefulotter.game.Display.ShaderEffects;

import peacefulotter.game.Maths.Matrix4f;
import peacefulotter.game.Maths.Vector3f;

public class ShaderTransform
{
    private Vector3f translation;

    public ShaderTransform()
    {
        translation = new Vector3f( 0, 0, 0 );
    }

    public Vector3f getTranslation()
    {
        return translation;
    }

    public void setTranslation(Vector3f translation)
    {
        this.translation = translation;
    }

    public void setTranslation( float x, float y, float z )
    {
        translation = new Vector3f( x, y, z );
    }

    public Matrix4f getTransformation()
    {
        Matrix4f translation = new Matrix4f();
        return translation;
    }
}
