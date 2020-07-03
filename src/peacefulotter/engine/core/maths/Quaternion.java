package peacefulotter.engine.core.maths;

public class Quaternion
{
    private float x, y, z, w;

    public Quaternion( Vector3f axis, float angleDeg )
    {
        double halfRadAngle = Math.toRadians( angleDeg ) / 2;
        float cosHalfAngle = (float) Math.cos( halfRadAngle );
        float sinHalfAngle = (float) Math.sin( halfRadAngle );
        setX( axis.getX() * sinHalfAngle );
        setY( axis.getY() * sinHalfAngle );
        setZ( axis.getZ() * sinHalfAngle );
            setW( cosHalfAngle );
    }

    public Quaternion( float x, float y, float z, float w )
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float length()
    {
        return (float) Math.sqrt( x*x + y*y + z*z + w*w );
    }

    public Quaternion normalize()
    {
        float length = length();
        return new Quaternion( x / length, y / length, z / length, w  / length );
    }

    public Quaternion conjugate()
    {
        return new Quaternion( -x, -y, -z, w );
    }

    public Quaternion mul( Quaternion other )
    {
        float x_ = x * other.getW() + w * other.getX() + y * other.getZ() - z * other.getY();
        float y_ = y * other.getW() + w * other.getY() + z * other.getX() - x * other.getZ();
        float z_ = z * other.getW() + w * other.getZ() + x * other.getY() - y * other.getX();
        float w_ = w * other.getW() - x * other.getX() - y * other.getY() - z * other.getZ();
        return new Quaternion( x_, y_, z_, w_ );
    }

    public Quaternion mul( Vector3f other )
    {
        float x_ =  w * other.getX() + y * other.getZ() - z * other.getY();
        float y_ =  w * other.getY() + z * other.getX() - x * other.getZ();
        float z_ =  w * other.getZ() + x * other.getY() - y * other.getX();
        float w_ = -x * other.getX() - y * other.getY() - z * other.getZ();
        return new Quaternion( x_, y_, z_, w_ );
    }

    public Matrix4f toRotationMatrix()
    {
        Vector3f forward =  new Vector3f(2.0f * (x * z - w * y), 2.0f * (y * z + w * x), 1.0f - 2.0f * (x * x + y * y));
        Vector3f up = new Vector3f(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z - w * x));
        Vector3f right = new Vector3f(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y - w * z), 2.0f * (x * z + w * y));

        return new Matrix4f().initRotation( forward, up, right );
    }

    public Vector3f getForward() { return new Vector3f( 0,0,1 ).rotate( this ); }

    public Vector3f getBack()    { return new Vector3f( 0,0,-1 ).rotate( this ); }

    public Vector3f getUp()      { return new Vector3f( 0,1,0 ).rotate( this ); }

    public Vector3f getDown()    { return new Vector3f( 0,-1,0 ).rotate( this ); }

    public Vector3f getRight()   { return new Vector3f( 1,0,0 ).rotate( this ); }

    public Vector3f getLeft()    { return new Vector3f( -1,0,0 ).rotate( this ); }

    public float getX() { return x; }
    public void setX( float x ) { this.x = x; }

    public float getY() { return y; }
    public void setY( float y ) { this.y = y; }

    public float getZ() { return z; }
    public void setZ( float z ) { this.z = z; }

    public float getW() { return w; }
    public void setW( float w ) { this.w = w; }
}
