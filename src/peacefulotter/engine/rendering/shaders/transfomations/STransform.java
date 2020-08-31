package peacefulotter.engine.rendering.shaders.transfomations;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.elementary.Updatable;
import peacefulotter.engine.utils.Logger;


public class STransform implements Updatable
{
    private final STranslation translation;
    private final SRotation rotation;
    private final SScale scale;

    private Matrix4f transformationMatrix = new Matrix4f().initIdentity();
    private Vector3f transformedTranslation = Vector3f.getZero();
    private Quaternion transformedRotation = new Quaternion( 0, 0, 0, 1 );

    private STransform parent;
    private boolean translationChanged, rotationChanged, scaleChanged;

    public STransform()
    {
        this.translation = new STranslation();
        this.rotation = new SRotation();
        this.scale = new SScale();
        calculateTransformationMatrix();
        calculateTransformedTranslation();
        calculateTransformedRotation();
    }

    /** Copy Constructor **/
    public STransform( STransform origin )
    {
        this.translation = new STranslation( origin.translation );
        this.rotation = new SRotation( origin.rotation );
        this.scale = new SScale( origin.scale );
        if ( origin.parent != null )
            this.parent = new STransform( origin.parent );
    }

    private void calculateTransformationMatrix()
    {
        Matrix4f base = translation.getTranslationMatrix().mul(
                            rotation.getRotationMatrix().mul(
                                scale.getScaleMatrix() ) );
        if ( parent != null )
            transformationMatrix = parent.getTransformationMatrix().mul( base );
        else
            transformationMatrix = base;
    }

    public Matrix4f getTransformationMatrix()
    {
        //return new Matrix4f( transformationMatrix.getM() );
        if ( parent != null )
            return parent.getTransformationMatrix().mul(
                    translation.getTranslationMatrix().mul(
                        rotation.getRotationMatrix().mul(
                            scale.getScaleMatrix() ) ) );
        return translation.getTranslationMatrix().mul(
                rotation.getRotationMatrix().mul(
                        scale.getScaleMatrix() ) );
    }

    private void calculateTransformedTranslation()
    {
        if ( parent != null )
            transformedTranslation = parent.getTransformationMatrix().transform( getTranslation() );
        else
            transformedTranslation = getTranslation();
    }

    public Vector3f getTransformedTranslation()
    {
        //return new Vector3f( transformedTranslation );
        if ( parent != null )
            return parent.getTransformationMatrix().transform( getTranslation() );
        return getTranslation();
    }

    private void calculateTransformedRotation()
    {
        if ( parent != null )
            transformedRotation = parent.getTransformedRotation().mul( getRotation() );
        else
            transformedRotation = getRotation();
    }

    public Quaternion getTransformedRotation()
    {
        //return new Quaternion( transformedRotation );
        if ( parent != null )
            return parent.getTransformedRotation().mul( getRotation() );

        return getRotation();
    }

    public Vector3f getTranslation() { return translation.getTranslationVector(); }

    public Quaternion getRotation() { return rotation.getRotationQuaternion(); }

    public Vector3f getScale() { return scale.getScaleVector(); }

    public STransform setTranslation( Vector3f newTranslation )
    {
        if ( !getTranslation().equals( newTranslation ) )
        {
            translation.setTranslation( newTranslation );
            translationChanged = true;
        }
        return this;
    }

    public STransform setRotation( Quaternion newRotation )
    {
        if ( !getRotation().equals( newRotation ) )
        {
            rotation.setRotation( newRotation );
            rotationChanged = true;
        }
        return this;
    }

    public STransform setScale( Vector3f newScale )
    {
        if ( !getScale().equals( newScale ) )
        {
            scale.setScale( newScale );
            scaleChanged = true;
        }
        return this;
    }

    public STransform translate( Vector3f vector )
    {

        translation.translate( vector );
        translationChanged = true;
        return this;
    }

    public STransform rotate( Quaternion q )
    {
        rotation.rotate( q );
        rotationChanged = true;
        return this;
    }

    public STransform rotate( Vector3f axis, float angleDeg )
    {
        if ( angleDeg != 0 )
        {
            rotationChanged = true;
            return rotate( new Quaternion( axis, angleDeg ) );
        }
        return this;
    }

    public STransform scale( float newScale )
    {
        return scale( new Vector3f( newScale, newScale, newScale ) );
    }

    public STransform scale( Vector3f newScale )
    {
        if ( !newScale.equals( Vector3f.getZero() ) )
        {
            setScale( getScale().add( newScale ) );
            scale.setScale( newScale );
            scaleChanged = true;
        }
        return this;
    }


    @Override
    public void update( float deltaTime )
    {
        //System.out.println(this);
        //System.out.println( translationChanged + " " + rotationChanged + " " + scaleChanged );
        if ( translationChanged || rotationChanged || scaleChanged )
            calculateTransformationMatrix();
        if ( translationChanged )
            calculateTransformedTranslation();
        if ( rotationChanged )
            calculateTransformedRotation();
        // translationChanged = false;
        // rotationChanged = false;
        // scaleChanged = false;
    }


    public boolean hasParent() { return parent != null; }

    public STransform getParent() { return new STransform( parent ); }

    public void setParent( STransform transform )
    {
        parent = transform;
        calculateTransformationMatrix();
        calculateTransformedTranslation();
        calculateTransformedRotation();
    }


    public void lookAt( Vector3f point, Vector3f up )
    {
        setRotation( getLookAtDirection( point, up ) );
    }

    public Quaternion getLookAtDirection( Vector3f point, Vector3f up )
    {
        return new Quaternion( new Matrix4f().initRotation( point.sub( getTranslation() ).normalize(), up ) );
    }

    @Override
    public String toString()
    {
        return  "[T] " + getTranslation() + " | [R] " + getRotation() + " | [S] " + getScale();
    }
}
