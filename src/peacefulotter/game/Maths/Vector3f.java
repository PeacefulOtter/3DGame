package peacefulotter.game.Maths;

public class Vector3f
{
    private double x, y, z;

    public Vector3f(double x, double y, double z )
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() { return x; }
    public void setX( double x ) { this.x = x; }

    public double getY() { return y; }
    public void setY( double y ) { this.y = y; }

    public double getZ() { return z; }
    public void setZ( double z ) { this.z = z; }

    public double length()
    {
        return Math.sqrt( BMaths.square( x ) + BMaths.square( y ) + BMaths.square( z ) );
    }

    public double dot( Vector3f other )
    {
        return x * other.getX() + y * other.getY() + z * other.getZ();
    }

    public Vector3f cross( Vector3f other )
    {
        double x_ = y * other.getZ() - z * other.getY();
        double y_ = z * other.getX() - x * other.getZ();
        double z_ = x * other.getY() - y * other.getX();

        return new Vector3f( x_, y_, z_ );
    }

    public Vector3f normalize()
    {
        double length = length();
        return new Vector3f( x / length, y / length, z / length );
    }

    public Vector3f rotate( double angleDeg )
    {
        return null;
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
    @Override
    public String toString()
    {
        return "(" + x + ":" + y + ":" + z + ")";
    }
}
