package peacefulotter.engine.core.maths;

public class Vector3f
{
    public static final Vector3f ZERO = new Vector3f( 0, 0, 0 );
    private float x, y, z;

    public Vector3f( float x, float y, float z )
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float length()
    {
        return (float) Math.sqrt( BMaths.square( x ) + BMaths.square( y ) + BMaths.square( z ) );
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
        return new Vector3f( x / length, y / length, z / length );
    }

    public Vector3f rotate( float angleDeg, Vector3f axis )
    {
        double halfRadAngle = Math.toRadians( angleDeg / 2 );
        float cosHalfAngle = (float) Math.cos( halfRadAngle );
        float sinHalfAngle = (float) Math.sin( halfRadAngle );

        Quaternion rotation = new Quaternion(
                axis.getX() * sinHalfAngle,
                axis.getY() * sinHalfAngle,
                axis.getZ() * sinHalfAngle,
                cosHalfAngle
        );
        Quaternion conjugate = rotation.conjugate();
        // were using the conjugate to get rid of the imaginary values
        // and keep the full rotation
        // reminder :             Q = (  D*sin(angle/2), w*cos(angle/2) )
        //              conjugate Q = ( -D*sin(angle/2), w*cos(angle/2) )
        Quaternion w = rotation.mul( this ).mul( conjugate ); // Q*V*Q(conjugate)

        if ( w.getY() <= -0.9999 )
            return new Vector3f( getX(), getY(), getZ() );

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

    public Vector3f rotate( Quaternion rotation )
    {
        Quaternion conjugate = rotation.conjugate();
        Quaternion w = rotation.mul( this ).mul( conjugate );
        return new Vector3f( w.getX(), w.getY(), w.getZ() );
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
}
