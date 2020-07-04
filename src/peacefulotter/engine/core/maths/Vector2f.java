package peacefulotter.engine.core.maths;

public class Vector2f
{
    public static final Vector2f ZERO = new Vector2f( 0, 0 );

    private float x, y;

    public Vector2f( float x, float y )
    {
        this.x = x;
        this.y = y;
    }

    public float length()
    {
        return (float) Math.sqrt( x*x + y*y );
    }

    public float dot( Vector2f other )
    {
        return x * other.getX() + y * other.getY();
    }

    public Vector2f normalize()
    {
        float length = length();
        return new Vector2f( x / length, y / length );
    }

    public Vector2f rotate( float angleDeg )
    {
        double rad = Math.toRadians( angleDeg );
        double cos = Math.cos( rad );
        double sin = Math.sin( rad );
        return new Vector2f( (float) (x * cos - y * sin), (float) (x * sin + y * cos) );
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

    public Vector2f abs() { return new Vector2f( Math.abs( x ), Math.abs( y ) ); }

    public Vector2f lerp( Vector2f destination, float lerpFactor )
    {
        return destination.sub( this ).mul( lerpFactor ).add( this );
    }

    public float cross( Vector2f other )
    {
        return x * other.getY() - y * other.getX();
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public void setX( float newX ) { x = newX; }
    public void setY( float newY ) { y = newY; }

    public void set( float x, float y ) { setX( x ); setY( y ); }
    public void set( Vector2f vector ) { set( vector.getX(), vector.getY() ); }

    @Override
    public boolean equals( Object other )
    {
        if ( !( other instanceof Vector2f ) ) return false;
        Vector2f vec = (Vector2f) other;
        return  x == vec.getX() &&
                y == vec.getY();}

    @Override
    public String toString()
    {
        return "(" + x + ":" + y + ")";
    }
}
