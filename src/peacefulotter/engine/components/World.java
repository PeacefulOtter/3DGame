package peacefulotter.engine.components;

import peacefulotter.engine.components.renderer.SkyBoxRenderer;
import peacefulotter.engine.components.renderer.TerrainRenderer;
import peacefulotter.engine.core.Game;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.terrain.Terrain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

// MOVE THE SKYBOX HERE SOMEHOW
abstract public class World extends GameObject
{
    private static final String IMAGE_PATH = "/textures/terrain/height_map2.png";

    private final TerrainRenderer tr;
    private final SkyBoxRenderer sbr;

    public World()
    {
        this.tr = new TerrainRenderer();
        // generate the terrain based on a terrain height map image
        try
        {
            BufferedImage image = ImageIO.read( getClass().getResourceAsStream( IMAGE_PATH ) );
            Terrain terrain = new Terrain( 0, 0, image );
            generateDecoration( terrain );
            tr.addComponent( terrain );
        }
        catch ( IOException e ) { e.printStackTrace(); }

        this.sbr = new SkyBoxRenderer();
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        tr.render( shader, renderingEngine );
        super.render( shader, renderingEngine );
    }

    // Special renders for the world
    public void renderWorld( RenderingEngine renderingEngine )
    {
        tr.renderTerrain( renderingEngine );
        sbr.renderSkyBox( renderingEngine );
    }

    abstract protected void generateDecoration( Terrain terrain );

    // TO REFACTOR
    public Terrain getTerrain()
    {
        return tr.getTerrain();
    }
}
