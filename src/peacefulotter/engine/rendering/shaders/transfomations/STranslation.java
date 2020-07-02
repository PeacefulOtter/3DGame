package peacefulotter.engine.rendering.shaders.transfomations;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector3f;

public class STranslation
{
    private Vector3f translation = Vector3f.ZERO;

    public Vector3f getTranslationVector()
    {
        return translation;
    }

    public void setTranslation( Vector3f translation )
    {
        this.translation = translation;
    }

    public Matrix4f getTranslationMatrix()
    {
        return new Matrix4f().initTranslation(
                translation.getX(), translation.getY(), translation.getZ()
        );
    }
}
