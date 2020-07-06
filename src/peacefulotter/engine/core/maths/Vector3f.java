package peacefulotter.engine.core.maths;

public class Vector3f
{
    private float x, y, z;

    public Vector3f( float x, float y, float z )
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3f getZero() { return new Vector3f( 0, 0, 0 ); }

    public float length()
    {
        return (float) Math.sqrt( x*x + y*y + z*z );
    }

    public float dot( Vector3f other )
    {
        return x * other.getX() + y * other.getY() + z * other.getZ();
    }

    public Vector3f cross( Vector3f other )
    {
        float x_ = y * other.getZ() - z * other.getY();
        float y_ = z * other.getX() - x * other.getZ();
        float z_ = x * other.getY() - y * other.getX();

        return new Vector3f( x_, y_, z_ );
    }

    public Vector3f normalize()
    {
        float length = length();
        if ( length == 0 )
            return new Vector3f( x, y, z );
        return new Vector3f( x / length, y / length, z / length );
    }

    public Vector3f rotate( Vector3f axis, float angleDeg )
    {
        return rotate( new Quaternion( axis, angleDeg ) );
    }

    public Vector3f rotate( Quaternion rotation )
    {
        Quaternion conjugate = rotation.conjugate();
        Quaternion w = rotation.mul( this ).mul( conjugate );
        return new Vector3f( w.getX(), w.getY(), w.getZ() );
    }

    public Vector3f add( Vector3f other )
    {
        return new Vector3f( x + other.getX(), y + other.getY(), z + other.getZ() );
    }

    public Vector3f add( float r )
    {
        return new Vector3f( x + r, y + r, z + r );
    }

    public Vector3f sub( Vector3f other )
    {
        return new Vector3f( x - other.getX(), y - other.getY(), z - other.getZ() );
    }

    public Vector3f sub( float r )
    {
        return new Vector3f( x - r, y - r, z - r );
    }

    public Vector3f mul( Vector3f other )
    {
        return new Vector3f( x * other.getX(), y * other.getY(), z * other.getZ() );
    }

    public Vector3f mul( float r )
    {
        return new Vector3f( x * r, y * r, z * r );
    }

    public Vector3f div( Vector3f other )
    {
        return new Vector3f( x / other.getX(), y / other.getY(), z / other.getZ() );
    }

    public Vector3f div( float r )
    {
        return new Vector3f( x / r, y / r, z / r );
    }

    public Vector3f abs() { return new Vector3f( Math.abs( x ), Math.abs( y ), Math.abs( z ) ); }

    public Vector3f lerp( Vector3f destination, float lerpFactor )
    {
        return destination.sub( this ).mul( lerpFactor ).add( this );
    }

    @Override
    public boolean equals( Object other )
    {
        if ( !( other instanceof Vector3f ) ) return false;
        Vector3f vec = (Vector3f) other;
        return  x == vec.getX() &&
                y == vec.getY() &&
                z == vec.getZ();
    }

    public Vector2f getXY() { return new Vector2f( x, y ); }
    public Vector2f getYZ() { return new Vector2f( y, z ); }
    public Vector2f getZX() { return new Vector2f( z, x ); }
    public Vector2f getYX() { return new Vector2f( y, x ); }
    public Vector2f getZY() { return new Vector2f( z, y ); }
    public Vector2f getXZ() { return new Vector2f( x, z ); }

    @Override
    public String toString()
    {
        return "(" + x + ":" + y + ":" + z + ")";
    }

    public float getX() { return x; }
    public void setX( float x ) { this.x = x; }

    public float getY() { return y; }
    public void setY( float y ) { this.y = y; }

    public float getZ() { return z; }
    public void setZ( float z ) { this.z = z; }

    public void set( float x, float y, float z )
    {
        setX( x ); setY( y ); setZ( z );
    }

    public void set( Vector3f vector )
    {
        set( vector.getX(), vector.getY(), vector.getZ() );
    }
}
