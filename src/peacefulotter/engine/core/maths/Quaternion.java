package peacefulotter.engine.core.maths;

public class Quaternion
{
    private float x, y, z, w;

    public Quaternion(float x, float y, float z, float w )
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float getX() { return x; }
    public void setX( float x ) { this.x = x; }

    public float getY() { return y; }
    public void setY( float y ) { this.y = y; }

    public float getZ() { return z; }
    public void setZ( float z ) { this.z = z; }

    public float getW() { return w; }
    public void setW( float w ) { this.w = w; }

    public float length()
    {
        return (float) Math.sqrt(
                BMaths.square( x ) + BMaths.square( y ) + BMaths.square( z ) + BMaths.square( w ) );
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
}
