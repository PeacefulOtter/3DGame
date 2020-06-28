package peacefulotter.engine.rendering.shaders.transfomations;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector3f;

public class STranslation
{
    private Vector3f translation;

    public STranslation()
        {
            translation = new Vector3f( 0, 0, 0 );
        }

    public Vector3f getTranslation()
        {
            return translation;
        }

    public void setTranslation( Vector3f translation )
        {
            this.translation = translation;
        }

    public void setTranslation( float x, float y, float z )
        {
            translation = new Vector3f( x, y, z );
        }

    public Matrix4f getTranslationMatrix()
    {
        Matrix4f translationMatrix = new Matrix4f().initTranslation(
                translation.getX(), translation.getY(), translation.getZ()
        );
        return translationMatrix;
    }
}
