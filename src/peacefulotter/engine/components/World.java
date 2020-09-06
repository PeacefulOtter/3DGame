package peacefulotter.engine.components;

import peacefulotter.engine.components.renderer.TerrainRenderer;
import peacefulotter.engine.rendering.terrain.Terrain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class World extends GameObject
{
    private static final String IMAGE_PATH = "/textures/terrain/height_map2.png";

    private final TerrainRenderer tr;

    public enum UniqueWorld
    {
        INSTANCE();

        private final World world;

        UniqueWorld() { this.world = new World(); }

        public World getWorld() { return world; }
    }

    private World()
    {
        this.tr = new TerrainRenderer();
        // generate the terrain based on a terrain height map image
        try
        {
            BufferedImage image = ImageIO.read( getClass().getResourceAsStream( IMAGE_PATH ) );
            tr.addTerrain( new Terrain( 0, 0, image ) );
        }
        catch ( IOException e ) { e.printStackTrace(); }

        addComponent( tr );
    }

    public Terrain getTerrain()
    {
        return tr.getTerrains().get( 0 );
    }
}
