package peacefulotter.game.hud;

import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.rendering.GUI.GUI;
import peacefulotter.engine.rendering.GUI.GUIMaterial;
import peacefulotter.game.actor.Player;
import peacefulotter.game.actor.Weapon;

public class Hud extends GUI
{
    private static final GUIMaterial CROSSHAIR = new GUIMaterial( "crosshair.png", Vector2f.getZero(), new Vector2f( 0.003f, 0.003f ) );
    private static final GUIMaterial AMMUNITION = new GUIMaterial( "ammo.png",
            new Vector2f( -0.24f, -0.135f ), new Vector2f( 0.03f, 0.01f ),
            GUIMaterial.ANCHOR_X.LEFT, GUIMaterial.ANCHOR_Y.BOTTOM );
    private static final GUIMaterial HEALTH = new GUIMaterial( "health.jpg",
            new Vector2f( 0.24f, -0.135f ), new Vector2f( 0.03f, 0.01f ),
            GUIMaterial.ANCHOR_X.RIGHT, GUIMaterial.ANCHOR_Y.BOTTOM );

    private static Player player;

    public Hud()
    {
        addGUIMaterials( CROSSHAIR, AMMUNITION, HEALTH );
    }

    public static void update( Weapon weapon, float health )
    {
        AMMUNITION.setWidth( weapon.getBulletsLeft() / 100f );
        HEALTH.setWidth( health / 1000f );
    }

    public static void setPlayer( Player player ) { Hud.player = player; }

}
