package peacefulotter.game.actor;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.components.GameObject;
import peacefulotter.engine.components.MeshRenderer;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.Mesh;
import peacefulotter.engine.rendering.graphics.Texture;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.utils.Logger;

public class Weapon extends GameComponent
{
    // proper to all weapons
    private static final Mesh mesh = new Mesh( "m4a1.obj" );
    private static final Material material = new Material(
            new Texture( "metal.jpg" ),
            new Texture( "metal_normal.jpg" ),
            new Texture( "metal_height.png" ),
            2, 12, 0.04f, -1f );
    private static final float MAX_BULLET_RANGE = 50f;
    private final MeshRenderer meshRenderer;
    private float timeSinceLastFire, reloadTime;
    private boolean isReloading;


    // proper to a certain weapon
    // Automatic or not, the weapon has a certain fire rate
    private static final float FIRE_RATE = 0.7f;
    private static final float BULLET_DAMAGE = 20f;
    private static final int MAGAZINE_NUMBER = 3;
    private static final int MAGAZINE_SIZE = 10;
    private static final float RELOAD_TIME = 5f;
    public static final boolean IS_AUTOMATIC = false;

    private int magazinesLeft, bulletsLeft;


    public Weapon()
    {
        meshRenderer = new MeshRenderer( mesh, material );
        magazinesLeft = MAGAZINE_NUMBER;
        bulletsLeft = MAGAZINE_SIZE;
    }

    @Override
    public void update( float deltaTime )
    {
        super.update( deltaTime );
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

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        if ( hasParent() )
            meshRenderer.render( shader, renderingEngine );
    }

    @Override
    public void setParent( GameObject parent )
    {
        super.setParent( parent );
        meshRenderer.setParent( parent );
    }

    public void fire( Vector3f direction )
    {
        if ( !isReloading && bulletsLeft > 0 && timeSinceLastFire > FIRE_RATE )
        {
            //use to check if it hits a player (=deal damage) or wall (=delete it)
            Vector3f origin = getTransform().getTranslation();
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

    public void setInnerTranslation( Vector3f innerTranslation )
    {
        meshRenderer.setInnerTranslation( innerTranslation );
    }
}
