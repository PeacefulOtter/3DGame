package peacefulotter.game.map;

import peacefulotter.engine.components.World;
import peacefulotter.engine.components.renderer.MultiMeshRenderer;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.terrain.Terrain;
import peacefulotter.engine.utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FPSWorld extends World
{
    private static final String TERRAIN_PATH = "/textures/terrain/height_map2.png";
    private static final String BLEND_MAP_PATH = "/textures/terrain/blend_map.png";
    private static final int DECORATION_AMOUNT = 25;

    public static final FPSWorld INSTANCE = new FPSWorld();

    private static Terrain FPS_TERRAIN;

    private FPSWorld()
    {
        super();
        // generate the terrain based on a terrain height map image
        try
        {
            BufferedImage image = ImageIO.read( getClass().getResourceAsStream( TERRAIN_PATH ) );
            Terrain terrain = new Terrain( 0, 0, image );
            generateDecoration( terrain );
            FPS_TERRAIN = terrain;
        }
        catch ( IOException e ) { e.printStackTrace(); }
    }

    @Override
    protected void generateDecoration( Terrain terrain )
    {
        if ( terrain != null )
            return;
        Logger.log( getClass(), "Generating Terrain" );
        try
        {
            BufferedImage image = ImageIO.read( getClass().getResourceAsStream( BLEND_MAP_PATH ) );
            for ( int i = 0; i < DECORATION_AMOUNT; i++ )
            {
                float posX = (float) Math.random();
                float posZ = (float) Math.random();
                int imageX = Math.round( posX * ( image.getWidth()  - 1 ) );
                int imageZ = Math.round( posZ * ( image.getHeight() - 1 ) );
                float worldX = posX * terrain.getSize();
                float worldZ = posZ * terrain.getSize();
                Vector3f pos = new Vector3f( worldX, terrain.getTerrainHeight( worldX, worldZ ), worldZ );

                Color color = new Color( image.getRGB( imageX, imageZ ) );
                if ( color.getRed() > 100 )
                {
                    MultiMeshRenderer mmr = new MultiMeshRenderer( "fern/", "fern.obj" );
                    mmr.getTransform().scale( 0.1f ).translate( pos );
                    addComponent( mmr );
                }
                else if ( color.getGreen() > 100 )
                {
                    MultiMeshRenderer mmr = new MultiMeshRenderer( "flowers/", "flowers.obj" );
                    mmr.getTransform().scale( 0.03f ).translate( pos );
                    addComponent( mmr );
                }
                else if ( color.getBlue() < 50 )
                {
                    MultiMeshRenderer mmr = new MultiMeshRenderer( "tree3/", "tree"+Math.round(Math.random()+1)+".obj" );
                    mmr.getTransform().scale( 2f ).translate( pos );
                    addComponent( mmr );
                }
            }
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }

    @Override
    public Terrain getTerrain()
    {
        return FPS_TERRAIN;
    }
}
