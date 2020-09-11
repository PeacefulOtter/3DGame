package peacefulotter.engine.rendering.terrain;

import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.core.transfomations.STransform;

public class WaterTile
{
    private final STransform transform;

    public WaterTile( int x, int z )
    {
        transform = new STransform()
                .translate( new Vector3f( x / 2f, -12f, z / 2f ) )
                .rotate( Vector3f.X_AXIS, 90 )
                .scale( Terrain.SIZE / 2f );
    }

    public STransform getTransform() { return transform; }
}
