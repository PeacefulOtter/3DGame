package peacefulotter.game.actor;

import peacefulotter.engine.components.GameObject;
import peacefulotter.engine.components.renderer.MeshRenderer;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.Mesh;
import peacefulotter.engine.rendering.graphics.Texture;
import peacefulotter.engine.utils.Logger;

public class Weapon extends GameObject
{
    // proper to all weapons
    public static Vector3f PLAYER_ORIGIN() { return new Vector3f( 0.7f, 4.8f, -0.3f ); }
    private static final Material WEAPON_MATERIAL = new Material(
            new Texture( "metal.jpg" ),
            new Texture( "metal_normal.jpg" ),
            new Texture( "metal_height.png" ),
            2, 12, 0.04f, -1f );
    private static final float MAX_BULLET_RANGE = 50f;
    private float timeSinceLastFire, reloadTime;
    private boolean isReloading;


    // proper to a certain weapon
    // Automatic or not, the weapon has a certain fire rate
    private static final Mesh WEAPON_MESH = new Mesh( "m4a1.obj" );
    private static final float FIRE_RATE = 2f;
    private static final float BULLET_DAMAGE = 20f;
    private static final int MAGAZINE_NUMBER = 3;
    private static final int MAGAZINE_SIZE = 10;
    private static final float RELOAD_TIME = 5f;
    public static final boolean IS_AUTOMATIC = false;

    private int magazinesLeft, bulletsLeft;


    public Weapon()
    {
        getTransform().scale( 0.11f );
        MeshRenderer mr = new MeshRenderer( WEAPON_MESH, WEAPON_MATERIAL );
        addComponent( mr );

        magazinesLeft = MAGAZINE_NUMBER;
        bulletsLeft = MAGAZINE_SIZE;
    }

    @Override
    public void update( float deltaTime )
    {
        timeSinceLastFire += deltaTime;

        // Reload logic
        if ( isReloading )
        {
            Logger.log( getClass(), "Reloading..." );
            reloadTime += deltaTime;
            if ( reloadTime >= RELOAD_TIME )
            {
                reloadTime = 0;
                isReloading = false;
            }
        }
    }

    public void fire( Vector3f direction )
    {
        if ( !isReloading && bulletsLeft > 0 && timeSinceLastFire > FIRE_RATE )
        {
            //use to check if it hits a player (=deal damage) or wall (=delete it)
            Matrix4f tfm = getTransform().getTransformationMatrix();
            bulletsLeft--;
            if ( bulletsLeft > 0 )
            {
                // DO THIS HERE
                timeSinceLastFire = 0;
            }
            Logger.log( getClass(), bulletsLeft + " / " + magazinesLeft );
        }

    }

    public void reload()
    {
        if ( isReloading || magazinesLeft <= 0 ) return;

        magazinesLeft--;
        bulletsLeft = MAGAZINE_SIZE;
        isReloading = true;
    }

}
