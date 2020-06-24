package peacefulotter.game.Maths;

public class Vector2f
{
    private double x, y;

    public Vector2f( double x, double y )
    {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void setX( double newX ) { x = newX; }
    public void setY( double newY ) { y = newY; }

    public double length()
    {
        return Math.sqrt( BMaths.square( x ) + BMaths.square( y ) );
    }

    public double dot( Vector2f other )
    {
        return x * other.getX() + y * other.getY();
    }

    public Vector2f normalize()
    {
        double length = length();
        return new Vector2f( x / length, y / length );
    }

    public Vector2f rotate( double angleDeg )
    {
        double rad = Math.toRadians( angleDeg );
        double cos = Math.cos( rad );
        double sin = Math.sin( rad );
        return new Vector2f(x * cos - y * sin, x * sin + y * cos );
    }

    public Vector2f add( Vector2f other )
    {
        return new Vector2f( x + other.getX(), y + other.getY() );
    }

    public Vector2f add( float r )
    {
        return new Vector2f( x + r, y + r );
    }

    public Vector2f sub( Vector2f other )
    {
        return new Vector2f( x - other.getX(), y - other.getY() );
    }

    public Vector2f sub( float r )
    {
        return new Vector2f( x - r, y - r );
    }

    public Vector2f mul( Vector2f other )
    {
        return new Vector2f( x * other.getX(), y * other.getY() );
    }

    public Vector2f mul( float r )
    {
        return new Vector2f( x * r, y * r );
    }

    public Vector2f div( Vector2f other )
    {
        return new Vector2f( x / other.getX(), y / other.getY() );
    }

    public Vector2f div( float r )
    {
        return new Vector2f( x / r, y / r );
    }
    @Override
    public String toString()
    {
        return "(" + x + ":" + y + ")";
    }
}
