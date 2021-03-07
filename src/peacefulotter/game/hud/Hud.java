package peacefulotter.game.hud;

import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.GUI.GUI;
import peacefulotter.engine.rendering.GUI.GUIMaterial;
import peacefulotter.game.actor.Player;
import peacefulotter.game.actor.Weapon;

public class Hud extends GUI
{
    private static final GUIMaterial CROSSHAIR = new GUIMaterial( "crosshair.png", Vector2f.getZero(), new Vector2f( 0.003f, 0.003f ) );
    private static final GUIMaterial AMMUNITION = new GUIMaterial( "rainbow.png", new Vector2f( -0.17f, -0.13f ), new Vector2f( 0.03f, 0.01f ) );
    private static final GUIMaterial HEALTH = new GUIMaterial( "rainbow.png", new Vector2f( 0.17f, -0.13f ), new Vector2f( 0.03f, 0.01f ) );

    private static Player player;

    public Hud()
    {
        addGUIMaterials( CROSSHAIR, AMMUNITION, HEALTH );
    }

    public static void update( Weapon weapon, float health )
    {
        AMMUNITION.getTransform().scale( new Vector3f( weapon.getBulletsLeft() / 100f, 0.02f, 1 ) );
        HEALTH.getTransform().scale( new Vector3f( health / 1000f, 0.02f, 1 ) );
    }

    public static void setPlayer( Player player ) { Hud.player = player; }

}
