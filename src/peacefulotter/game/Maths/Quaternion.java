package peacefulotter.game.Maths;

public class Quaternion
{
    private double x, y, z, w;

    public Quaternion(double x, double y, double z, double w )
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public double getX() { return x; }
    public void setX( double x ) { this.x = x; }

    public double getY() { return y; }
    public void setY( double y ) { this.y = y; }

    public double getZ() { return z; }
    public void setZ( double z ) { this.z = z; }

    public double getW() { return w; }
    public void setW( double w ) { this.w = w; }

    public double length()
    {
        return Math.sqrt( BMaths.square( x ) + BMaths.square( y ) + BMaths.square( z ) + BMaths.square( w ) );
    }

    public Quaternion normalize()
    {
        double length = length();
        return new Quaternion( x / length, y / length, z / length, w  / length );
    }

    public Quaternion conjugate()
    {
        return new Quaternion( -x, -y, -z, w );
    }

    public Quaternion mul( Quaternion other )
    {
        double x_ = x * other.getW() + w * other.getX() + y * other.getZ() - z * other.getY();
        double y_ = y * other.getW() + w * other.getY() + z * other.getX() - x * other.getZ();
        double z_ = z * other.getW() + w * other.getZ() + x * other.getY() - y * other.getX();
        double w_ = w * other.getW() - x * other.getX() - y * other.getY() - z * other.getZ();
        return new Quaternion( x_, y_, z_, w_ );
    }

    public Quaternion mul( Vector3f other )
    {
        double x_ =  w * other.getX() + y * other.getZ() - z * other.getY();
        double y_ =  w * other.getY() + z * other.getX() - x * other.getZ();
        double z_ =  w * other.getZ() + x * other.getY() - y * other.getX();
        double w_ = -x * other.getX() - y * other.getY() - z * other.getZ();
        return new Quaternion( x_, y_, z_, w_ );
    }
}
