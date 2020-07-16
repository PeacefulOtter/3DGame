package peacefulotter.game.actor;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.components.GameObject;
import peacefulotter.engine.components.MeshRenderer;
import peacefulotter.engine.components.PhysicsObject;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.physics.colliders.BoundingSphere;
import peacefulotter.engine.physics.colliders.Collider;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.Mesh;
import peacefulotter.engine.rendering.graphics.Texture;

public class Bullet extends PhysicsObject
{
    private static final float VELOCITY = 10f;
    private static final float BULLET_RADIUS = 0.003f;
    private static final GameComponent BULLET_MESH = new MeshRenderer(
            new Mesh( "sphere.obj" ),
            new Material(
                new Texture( "bricks2.jpg" ),
                new Texture( "bricks2_normal.jpg" ),
                new Texture( "bricks2_height.png" ),
                1f, 4, 0.03f, -0.04f ) );

    private Bullet( Vector3f direction, Vector3f originPos )
    {
        super( originPos, direction.mul( VELOCITY ) );
    }

    public Collider getCollider() { return new BoundingSphere( getPosition(), BULLET_RADIUS ); }

    public static class BulletBuilder
    {
        public static Bullet createBullet( Vector3f direction, Vector3f originPos )
        {
            System.out.println(originPos);
            Bullet bullet = new Bullet( direction, originPos );
            bullet.addComponent( new MeshRenderer(
                    new Mesh( "sphere.obj" ),
                    new Material(
                            new Texture( "bricks2.jpg" ),
                            new Texture( "bricks2_normal.jpg" ),
                            new Texture( "bricks2_height.png" ),
                            1f, 4, 0.03f, -0.04f ) ) );
            Vector3f translation = originPos.add( new Vector3f( 0, 6, 0 ) );
            bullet.getTransform().translate( translation ).scale( BULLET_RADIUS );
            return bullet;
        }
    }
}
