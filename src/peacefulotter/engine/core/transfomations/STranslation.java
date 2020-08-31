package peacefulotter.engine.core.transfomations;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector3f;

public class STranslation
{
    private Vector3f translation;

    public STranslation()
    {
        this.translation = Vector3f.getZero();
    }
    public STranslation( STranslation translation )
    {
        this.translation = new Vector3f( translation.getTranslationVector() );
    }

    public Vector3f getTranslationVector() { return translation; }

    public void setTranslation( Vector3f translation )
    {
        this.translation = translation;
    }

    public void translate( Vector3f vector ) { translation = translation.add( vector ); }

    public Matrix4f getTranslationMatrix()
    {
        return new Matrix4f().initTranslation(
                translation.getX(), translation.getY(), translation.getZ()
        );
    }
}
