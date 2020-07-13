

package peacefulotter.engine.core.maths;

public class Matrix3f
{
    private static final int MATRIX_SIZE = 3;
    private final float[][] m;

    public Matrix3f()
    {
        this.m = new float[ MATRIX_SIZE ][ MATRIX_SIZE ];
    }

    public Matrix3f initIdentity()
    {
        for( int i = 0; i < MATRIX_SIZE; i++ )
            m[ i ][ i ] = 1;
        return this;
    }

    public Matrix3f initCollision( float maxX, float maxY, float maxZ )
    {
        initIdentity();
        m[ 0 ][ 0 ] = 1 / maxX;
        m[ 1 ][ 1 ] = 1 / maxY;
        m[ 2 ][ 2 ] = 1 / maxZ;
        return this;
    }
}
