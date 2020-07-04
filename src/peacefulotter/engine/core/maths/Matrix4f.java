package peacefulotter.engine.core.maths;

public class Matrix4f
{
    private static final int MATRIX_SIZE = 4;
    private float m[][];

    public Matrix4f()
    {
        this.m = new float[ MATRIX_SIZE ][ MATRIX_SIZE ];
    }

    public Matrix4f initIdentity()
    {
        for( int i = 0; i < MATRIX_SIZE; i++ )
            m[ i ][ i ] = 1;
        return this;
    }

    public Matrix4f initTranslation( float x, float y, float z )
    {
        initIdentity();
        m[ 0 ][ 3 ] = x;
        m[ 1 ][ 3 ] = y;
        m[ 2 ][ 3 ] = z;
        return this;
    }

    // see : https://en.wikipedia.org/wiki/Rotation_matrix
    public Matrix4f initRotation( float x, float y, float z )
    {
        Matrix4f rotationX = new Matrix4f().initIdentity();
        Matrix4f rotationY = new Matrix4f().initIdentity();
        Matrix4f rotationZ = new Matrix4f().initIdentity();

        x = (float) Math.toRadians( x );
        y = (float) Math.toRadians( y );
        z = (float) Math.toRadians( z );

        float cosX = (float) Math.cos( x ); float sinX = (float) Math.sin( x );
        rotationX.m[ 1 ][ 1 ] =  cosX;
        rotationX.m[ 1 ][ 2 ] = -sinX;
        rotationX.m[ 2 ][ 1 ] =  sinX;
        rotationX.m[ 2 ][ 2 ] =  cosX;

        float cosY = (float) Math.cos( y ); float sinY = (float) Math.sin( y );
        rotationY.m[ 0 ][ 0 ] =  cosY;
        rotationY.m[ 0 ][ 2 ] = -sinY;
        rotationY.m[ 2 ][ 0 ] =  sinY;
        rotationY.m[ 2 ][ 2 ] =  cosY;

        float cosZ = (float) Math.cos( z ); float sinZ = (float) Math.sin( z );
        rotationZ.m[ 0 ][ 0 ] =  cosZ;
        rotationZ.m[ 0 ][ 1 ] = -sinZ;
        rotationZ.m[ 1 ][ 0 ] =  sinZ;
        rotationZ.m[ 1 ][ 1 ] =  cosZ;

        m = rotationZ.mul( rotationY.mul( rotationX ) ).getM();

        return this;
    }

    public Matrix4f initScale( float x, float y, float z )
    {
        initIdentity();
        m[ 0 ][ 0 ] = x;
        m[ 1 ][ 1 ] = y;
        m[ 2 ][ 2 ] = z;
        return this;
    }

    public Matrix4f initPerspective( float fovDeg, float aspectRatio, float zNear, float zFar )
    {
        initIdentity();

        float invTanHalfFov = 1f / (float) Math.tan( Math.toRadians( fovDeg ) / 2 );
        float depth = zNear - zFar;

        m[ 0 ][ 0 ] = invTanHalfFov * ( 1f / aspectRatio );
        m[ 1 ][ 1 ] = invTanHalfFov;
        m[ 2 ][ 2 ] =  ( -zNear - zFar ) / depth;
        m[ 2 ][ 3 ] = 2 * zFar * zNear / depth;
        m[ 3 ][ 2 ] = 1;
        m[ 3 ][ 3 ] = 0;

        return this;
    }

    public Matrix4f initOrthographic( float left, float right, float bottom, float top, float near, float far )
    {
        initIdentity();

        float width  = right - left;
        float height = top - bottom;
        float depth  = far - near;

        m[ 0 ][ 0 ] =  2 / width;
        m[ 1 ][ 1 ] =  2 / height;
        m[ 2 ][ 2 ] = -2 / depth;
        m[ 0 ][ 3 ] = -( right + left ) / width;
        m[ 1 ][ 3 ] = -( top + bottom ) / height;
        m[ 2 ][ 3 ] = -( far + near )   / depth;

        return this;
    }

    public Matrix4f initRotation( Vector3f forward, Vector3f upward )
    {
        Vector3f f = forward.normalize();
        Vector3f r = upward.normalize().cross( f );
        Vector3f u = f.cross( r );

        return initRotation( f, u, r );
    }

    public Matrix4f initRotation( Vector3f f, Vector3f u, Vector3f r )
    {
        initIdentity();

        m[ 0 ][ 0 ] = r.getX();
        m[ 0 ][ 1 ] = r.getY();
        m[ 0 ][ 2 ] = r.getZ();
        m[ 1 ][ 0 ] = u.getX();
        m[ 1 ][ 1 ] = u.getY();
        m[ 1 ][ 2 ] = u.getZ();
        m[ 2 ][ 0 ] = f.getX();
        m[ 2 ][ 1 ] = f.getY();
        m[ 2 ][ 2 ] = f.getZ();

        return this;
    }

    public Vector3f transform( Vector3f vector )
    {
        float vX = vector.getX(); float vY = vector.getY(); float vZ = vector.getZ();
        return new Vector3f(
                getAt( 0, 0 ) * vX + getAt( 0, 1 ) * vY + getAt( 0, 2 ) + vZ + getAt( 0, 3 ),
                getAt( 1, 0 ) * vX + getAt( 1, 1 ) * vY + getAt( 1, 2 ) + vZ + getAt( 1, 3 ),
                getAt( 2, 0 ) * vX + getAt( 2, 1 ) * vY + getAt( 2, 2 ) + vZ + getAt( 2, 3 ) );
    }

    public float[][] getM()
    {
        float[][] res = new float[ 4 ][ 4 ];

        for( int i = 0; i < 4; i++ )
            for( int j = 0; j < 4; j++ )
                res[ i ][ j ] = m[ i ][ j ];

        return res;
    }

    public void setM( float[][] other ) { m = other; }

    public float getAt( int x, int y )
    {
        return m[ x ][ y ];
    }

    public void setAt( int x, int y, float value )
    {
        m[ x ][ y ] = value;
    }

    public Matrix4f mul( Matrix4f other )
    {
        Matrix4f res = new Matrix4f();
        for ( int i = 0; i < MATRIX_SIZE; i++ )
        {
            for ( int j = 0; j < MATRIX_SIZE; j++ )
            {
                float value = 0;
                for ( int k = 0; k < MATRIX_SIZE; k++ )
                {
                    value += getAt( i, k ) * other.getAt( k, j );
                }
                res.setAt( i, j, value );
            }
        }
        return res;
    }
}
