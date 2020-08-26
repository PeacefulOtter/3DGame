package peacefulotter.engine.rendering.shaders.transfomations;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.elementary.Updatable;
import peacefulotter.engine.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class STransform implements Updatable
{
    private final List<STransform> children;

    private final STranslation translation;
    private final SRotation rotation;
    private final SScale scale;

    private Matrix4f parentTranslation;
    private Quaternion parentRotation;

    private boolean hasChanged;

    public STransform()
    {
        this.translation = new STranslation();
        this.rotation = new SRotation();
        this.scale = new SScale();
        this.parentTranslation = new Matrix4f().initIdentity();
        this.parentRotation = new Quaternion( 0, 0, 0, 1 );
        this.children = new ArrayList<>();
    }

    /** Copy Constructor **/
    public STransform( STransform origin )
    {
        this.translation = new STranslation( origin.translation );
        this.rotation = new SRotation( origin.rotation );
        this.scale = new SScale( origin.scale );
        this.parentTranslation = new Matrix4f( origin.parentTranslation.getM() );
        this.parentRotation = new Quaternion( origin.parentRotation );
        this.children = new ArrayList<>();
        children.addAll( origin.children );
        this.hasChanged = origin.hasChanged;
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

    public STransform setTranslation( Vector3f newTranslation )
    {
        Vector3f oldTranslation = translation.getTranslationVector();
        translation.setTranslation( newTranslation );
        if ( !oldTranslation.equals( newTranslation ) )
            hasChanged = true;
        return this;
    }

    public STransform setRotation( Quaternion newRotation )
    {
        Quaternion oldRotation = rotation.getRotationQuaternion();
        rotation.setRotation( newRotation );
        if ( !oldRotation.equals( newRotation ) )
            hasChanged = true;
        return this;
    }

    public STransform setScale( Vector3f newScale )
    {
        Vector3f oldScale = scale.getScaleVector();
        scale.setScale( newScale );
        if ( !oldScale.equals( newScale ) )
            hasChanged = true;
        return this;
    }

    public STransform translate( Vector3f vector )
    {
        translation.translate( vector );
        hasChanged = true;
        return this;
    }

    public STransform rotate( Quaternion q )
    {
        rotation.rotate( q );
        hasChanged = true;
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

    public void addChild( STransform child ) { children.add( child ); }

    public void notifyParentChange( STransform parentTransform )
    {
        parentTranslation = parentTransform.getTransformationMatrix();
        parentRotation = parentTransform.getTransformedRotation();
        hasChanged = true;
    }

    public boolean hasChanged() { return hasChanged; }

    @Override
    public void update( float deltaTime )
    {
        // if the transform changed, update the children transform
        if ( hasChanged )
        {
            children.forEach( child -> child.notifyParentChange( this ) );
            hasChanged = false;
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

    @Override
    public String toString()
    {
        return getTranslation() + " " + getRotation() + " " + getScale();
    }
}
