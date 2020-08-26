package peacefulotter.engine.components;

import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.terrain.Terrain;

public class World extends GameObject
{
    public World( Material material )
    {
        TerrainRenderer tr = new TerrainRenderer();
        tr.addTerrain( new Terrain( 0, 0, material ) );
        addComponent( tr );
    }
}
