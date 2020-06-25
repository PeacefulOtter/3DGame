package peacefulotter.game.Maths;

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
        return this;
    }

    public float[][] getM() { return m; }
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
