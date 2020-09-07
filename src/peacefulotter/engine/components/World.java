package peacefulotter.engine.components;

import peacefulotter.engine.components.renderer.TerrainRenderer;
import peacefulotter.engine.rendering.terrain.Terrain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


abstract public class World extends GameObject
{
    private static final String IMAGE_PATH = "/textures/terrain/height_map2.png";

    private final TerrainRenderer tr;

    public World()
    {
        this.tr = new TerrainRenderer();
        // generate the terrain based on a terrain height map image
        try
        {
            BufferedImage image = ImageIO.read( getClass().getResourceAsStream( IMAGE_PATH ) );
            Terrain terrain = new Terrain( 0, 0, image );
            generateDecoration( terrain );
            tr.setTerrain( terrain );
        }
        catch ( IOException e ) { e.printStackTrace(); }

        addComponent( tr );
    }

    abstract protected void generateDecoration( Terrain terrain );

    public Terrain getTerrain()
    {
        return tr.getTerrain();
    }
}
