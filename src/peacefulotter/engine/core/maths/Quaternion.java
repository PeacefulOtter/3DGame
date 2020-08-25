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

    public Quaternion( Quaternion q )
    {
        this( q.x, q.y, q.z, q.w );
    }

    public Quaternion( float x, float y, float z, float w )
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    //From Ken Shoemake's "Quaternion Calculus and Fast Animation" article
    public Quaternion( Matrix4f rot )
    {
        float trace = rot.getAt(0, 0) + rot.getAt(1, 1) + rot.getAt(2, 2);

        if(trace > 0)
        {
            float s = 0.5f / (float)Math.sqrt(trace+ 1.0f);
            w = 0.25f / s;
            x = (rot.getAt(1, 2) - rot.getAt(2, 1)) * s;
            y = (rot.getAt(2, 0) - rot.getAt(0, 2)) * s;
            z = (rot.getAt(0, 1) - rot.getAt(1, 0)) * s;
        }
        else
        {
            if(rot.getAt(0, 0) > rot.getAt(1, 1) && rot.getAt(0, 0) > rot.getAt(2, 2))
            {
                float s = 2.0f * (float)Math.sqrt(1.0f + rot.getAt(0, 0) - rot.getAt(1, 1) - rot.getAt(2, 2));
                w = (rot.getAt(1, 2) - rot.getAt(2, 1)) / s;
                x = 0.25f * s;
                y = (rot.getAt(1, 0) + rot.getAt(0, 1)) / s;
                z = (rot.getAt(2, 0) + rot.getAt(0, 2)) / s;
            }
            else if(rot.getAt(1, 1) > rot.getAt(2, 2))
            {
                float s = 2.0f * (float)Math.sqrt(1.0f + rot.getAt(1, 1) - rot.getAt(0, 0) - rot.getAt(2, 2));
                w = (rot.getAt(2, 0) - rot.getAt(0, 2)) / s;
                x = (rot.getAt(1, 0) + rot.getAt(0, 1)) / s;
                y = 0.25f * s;
                z = (rot.getAt(2, 1) + rot.getAt(1, 2)) / s;
            }
            else
            {
                float s = 2.0f * (float)Math.sqrt(1.0f + rot.getAt(2, 2) - rot.getAt(0, 0) - rot.getAt(1, 1));
                w = (rot.getAt(0, 1) - rot.getAt(1, 0) ) / s;
                x = (rot.getAt(2, 0) + rot.getAt(0, 2) ) / s;
                y = (rot.getAt(1, 2) + rot.getAt(2, 1) ) / s;
                z = 0.25f * s;
            }
        }

        float length = (float)Math.sqrt(x * x + y * y + z * z + w * w);
        x /= length;
        y /= length;
        z /= length;
        w /= length;
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

    public Quaternion add( Quaternion other )
    {
        return new Quaternion( x + other.getX(), y + other.getY(), z + other.getZ(), w + other.getW() );
    }

    public Quaternion sub( Quaternion other )
    {
        return new Quaternion( x - other.getX(), y - other.getY(), z - other.getZ(), w - other.getW() );
    }

    public float dot( Quaternion other )
    {
        return x * other.getX() + y * other.getY() + z * other.getZ() + w * other.getW();
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

    public void set( float x, float y, float z, float w ) { setX( x ); setY( y ); setZ( z ); setW( w ); }
    public void set( Quaternion q ) { set( q.x, q.y, q.z, q.w ); }

    @Override
    public boolean equals( Object obj )
    {
        if ( !( obj instanceof Quaternion ) )
            return false;
        Quaternion quat = (Quaternion)obj;
        return quat.x == x && quat.y == y && quat.z == z && quat.w == w;
    }
}
