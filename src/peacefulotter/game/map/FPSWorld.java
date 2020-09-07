package peacefulotter.game.map;

import peacefulotter.engine.components.World;
import peacefulotter.engine.components.renderer.MultiMeshRenderer;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.terrain.Terrain;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FPSWorld extends World
{
    private static final String BLEND_MAP_PATH = "/textures/terrain/blend_map.png";
    private static final int DECORATION_AMOUNT = 25;

    public static final FPSWorld INSTANCE = new FPSWorld();

    private FPSWorld() { super(); }

    @Override
    protected void generateDecoration( Terrain terrain )
    {
        try
        {
            BufferedImage image = ImageIO.read( getClass().getResourceAsStream( BLEND_MAP_PATH ) );
            for ( int i = 0; i < DECORATION_AMOUNT; i++ )
            {
                float posX = (float) Math.random();
                float posZ = (float) Math.random();
                int imageX = Math.round( posX * image.getWidth() );
                int imageZ = Math.round( posZ * image.getHeight() );
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
}
