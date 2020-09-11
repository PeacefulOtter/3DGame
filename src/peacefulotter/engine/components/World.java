package peacefulotter.engine.components;

import peacefulotter.engine.components.renderer.GUIRenderer;
import peacefulotter.engine.components.renderer.SkyBoxRenderer;
import peacefulotter.engine.components.renderer.TerrainRenderer;
import peacefulotter.engine.components.renderer.WaterRenderer;
import peacefulotter.engine.core.Game;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.rendering.GUI.GUIMaterial;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.terrain.Terrain;
import peacefulotter.engine.rendering.terrain.WaterTile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

// MOVE THE SKYBOX HERE SOMEHOW
abstract public class World extends GameObject
{
    private static final String IMAGE_PATH = "/textures/terrain/height_map2.png";

    private final TerrainRenderer tr;
    private final WaterRenderer wr;
    private final GUIRenderer gr;
    private final SkyBoxRenderer sbr;

    public World()
    {
        /* TERRAIN */
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

        this.wr = new WaterRenderer();
        wr.addWaterTile( new WaterTile( Terrain.SIZE, Terrain.SIZE ) );

        /* GUI */
        this.gr = new GUIRenderer();
        GUIMaterial material = new GUIMaterial( "crosshair_hit.png", new Vector2f( 0.5f, 0.5f ), new Vector2f( 5f, 5f ) );
        gr.addGUIMaterial( material );

        /* SKYBOX */
        this.sbr = new SkyBoxRenderer();
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        tr.render( shader, renderingEngine );
        super.render( shader, renderingEngine );
        wr.render( shader, renderingEngine );
        gr.render( shader, renderingEngine );
        sbr.render( shader, renderingEngine );
    }

    // Special renders for the world
    public void renderWorld( RenderingEngine renderingEngine )
    {
        tr.renderTerrain( renderingEngine );
        wr.renderWater( renderingEngine );
        gr.renderGUI( renderingEngine );
        sbr.renderSkyBox( renderingEngine );
    }

    abstract protected void generateDecoration( Terrain terrain );

    // TO REFACTOR
    public Terrain getTerrain()
    {
        return tr.getTerrain();
    }
}
