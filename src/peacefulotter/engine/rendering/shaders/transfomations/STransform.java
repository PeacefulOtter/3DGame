package peacefulotter.engine.rendering.shaders.transfomations;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.elementary.Updatable;

public class STransform implements Updatable
{
    private final STranslation translation;
    private final SRotation rotation;
    private final SScale scale;

    private STransform parent;
    private Matrix4f parentTranslation = new Matrix4f().initIdentity();
    private Quaternion parentRotation = new Quaternion( 0, 0, 0, 1 );

    private boolean hasChanged, hasChangedBis;

    public STransform()
    {
        this.translation = new STranslation();
        this.rotation = new SRotation();
        this.scale = new SScale();
    }

    public STransform( STransform origin )
    {
        this.translation = new STranslation( origin.translation );
        this.rotation = new SRotation( origin.rotation );
        this.scale = new SScale( origin.scale );
    }

    public Matrix4f getTransformationMatrix()
    {
        return parentTranslation.mul(
                translation.getTranslationMatrix().mul(
                    rotation.getRotationMatrix().mul(
                        scale.getScaleMatrix() ) ) );
    }


    public Vector3f getTransformedTranslation()
    {
        return parentTranslation.transform( getTranslation() );
    }

    public Quaternion getTransformedRotation()
    {
        return parentRotation.mul( getRotation() );
    }

    public Vector3f getTranslation() { return translation.getTranslationVector(); }

    public Quaternion getRotation() { return rotation.getRotationQuaternion(); }

    public Vector3f getScale() { return scale.getScaleVector(); }

    public STransform setTranslation( Vector3f vector )
    {
        translation.setTranslation( vector );
        hasChanged = true;
        return this;
    }

    public STransform setRotation( Quaternion quaternion )
    {
        rotation.setRotation( quaternion );
        hasChanged = true;
        return this;
    }

    public STransform setScale( Vector3f vector )
    {
        scale.setScale( vector );
        hasChanged = true;
        return this;
    }

    public STransform translate( Vector3f vector )
    {
        translation.translate( vector );
        if ( !vector.equals( Vector3f.getZero() ) )
            hasChanged = true;
        return this;
    }

    public STransform rotate( Quaternion q )
    {
        rotation.rotate( q );
        return this;
    }

    public STransform rotate( Vector3f axis, float angleDeg )
    {
        rotation.rotate( axis, angleDeg );
        if ( angleDeg != 0 )
            hasChanged = true;
        return this;
    }

    public STransform scale( float percentage ) { return scale( percentage, percentage, percentage ); }

    public STransform scale( float scaleX, float scaleY, float scaleZ )
    {
        scale.setScale( new Vector3f( scaleX, scaleY, scaleZ ) );
        hasChanged = true;
        return this;
    }

    public void setParent( STransform parent ) { this.parent = parent; }

    @Override
    public void update( float deltaTime )
    {
        if ( hasChanged )
        {
            if ( hasChangedBis )
            {
                hasChanged = false;
                hasChangedBis = false;
            }
            else
            {
                hasChangedBis = true;
            }
        }

        if ( parent != null && parent.hasChanged )
        {
            hasChanged = true;
            parentTranslation = parent.getTransformationMatrix();
            parentRotation = parent.getTransformedRotation();
        }
    }

    public void lookAt( Vector3f point, Vector3f up )
    {
        setRotation( getLookAtDirection( point, up ) );
    }

    public Quaternion getLookAtDirection( Vector3f point, Vector3f up )
    {
        return new Quaternion( new Matrix4f().initRotation( point.sub( getTranslation() ).normalize(), up ) );
    }
}
