package peacefulotter.game.actor;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;

import java.util.ArrayList;
import java.util.List;

public class Weapon extends GameComponent
{
    private static final float FIRE_RATE = 0.2f;

    private final List<Bullet> bullets;

    private float timeSinceLastFire;
    private boolean allowFire;

    public Weapon()
    {
        this.bullets = new ArrayList<>();
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        super.render( shader, renderingEngine );
        for ( Bullet bullet : bullets )
            bullet.render( shader, renderingEngine );
    }

    @Override
    public void update( float deltaTime )
    {
        super.update( deltaTime );

        timeSinceLastFire += deltaTime;
        for ( Bullet bullet : bullets )
            bullet.simulate( deltaTime );
    }

    public Bullet fire( Vector3f direction )
    {
        Bullet bullet = null;
        if ( timeSinceLastFire > FIRE_RATE )
        {
            bullet = Bullet.BulletBuilder.createBullet( direction, getTransform().getTranslation() );
            bullets.add( bullet );
            timeSinceLastFire = 0;
        }
        return bullet;
    }
}
