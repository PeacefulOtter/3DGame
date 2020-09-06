package peacefulotter.game;

import peacefulotter.engine.components.*;
import peacefulotter.engine.components.lights.DirectionalLight;
import peacefulotter.engine.components.lights.PointLight;
import peacefulotter.engine.components.lights.SpotLight;
import peacefulotter.engine.components.renderer.MeshRenderer;
import peacefulotter.engine.components.renderer.MultiMeshRenderer;
import peacefulotter.engine.core.Game;
import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.graphics.*;
import peacefulotter.engine.rendering.shaders.*;
import peacefulotter.game.actor.Player;
import peacefulotter.game.actor.Weapon;

public class TestGame extends Game
{
    public TestGame( String winName, int winWidth, int winHeight )
    {
        super( winName, winWidth, winHeight );
    }

    GameObject plane2, plane3;

    public void init()
    {
        Material bricks1 = new Material(
                new Texture( "bricks.jpg" ),
                new Texture( "bricks_normal.jpg" ),
                new Texture( "bricks_height.jpg" ),
                1f, 8, 0.04f, -1f );

        Material bricks2 = new Material(
                new Texture( "bricks2.jpg" ),
                new Texture( "bricks2_normal.jpg" ),
                new Texture( "bricks2_height.png" ),
                1f, 4, 0.03f, -0.04f );

        Material alienMaterial = new Material(
                new Texture( "metal.jpg" ),
                new Texture( "metal_normal.jpg" ),
                new Texture( "metal_height.png" ),
                2, 12, 0.04f, -1f );


        Mesh mesh = new Mesh( "plane3.obj" );
        GameObject plane1 = new GameObject()
                .addComponent( new MeshRenderer( mesh, bricks2 ) );
        plane1.getTransform().scale( 2 );
        plane2 = new GameObject().addComponent( new MeshRenderer( mesh, bricks1 ) );
        plane3 = new GameObject().addComponent( new MeshRenderer( mesh, bricks1 ) );
        GameObject plane4 = new GameObject().addComponent( new MeshRenderer( mesh, bricks1 ) );
        plane2.getTransform().translate( new Vector3f( 8, 4, 8 ) ).scale( 0.5f );
        plane3.getTransform().translate( new Vector3f( 0, 3, 0 ) ).scale( 0.5f );
        plane4.getTransform().translate( new Vector3f( 0, 6, 0 ) ).scale( 0.5f );
        plane3.addChild( plane4 );
        plane2.addChild( plane3 );

        GameObject characterObject = new GameObject()
                .addComponent( new MeshRenderer( new Mesh( "alien.obj" ), alienMaterial ) );

        PhysicsObject sphereObject1 = new PhysicsObject( new Vector3f( 30, 0, 10 ), new Vector3f(-5f, 0, 5 ) )
                .addComponent( new MeshRenderer(  new Mesh( "sphere.obj" ), bricks1 ) );
        sphereObject1.getTransform().scale( 0.1f );

        PhysicsObject sphereObject2 = new PhysicsObject( new Vector3f( 10, 0, 30 ), new Vector3f(5f, 0, 0 ) )
                .addComponent( new MeshRenderer(  new Mesh( "sphere.obj" ), alienMaterial ) );
        sphereObject2.getTransform().scale( 0.1f );

        addPhysicalObjects( sphereObject1, sphereObject2 );



        GameObject dirLightObject = new GameObject();
        DirectionalLight dirLight = new DirectionalLight(
                new Vector3f( 1f, 0.9f,0.8f ),
                0.15f );
        dirLightObject.addComponent( dirLight );
        dirLightObject.getTransform().setRotation( new Quaternion( new Vector3f( 1, -1, 0 ), -45 ) );

        GameObject pointLightObject = new GameObject();
        PointLight pointLight = new PointLight(
                new Vector3f( 1f, 0.1f,  0.1f ),
                0.4f,
                new Attenuation( 1f, 0, 0.01f ) );
        pointLightObject.addComponent( pointLight );
        pointLightObject.getTransform().translate( new Vector3f( 4, 0.5f, 4 ) );

        GameObject spotLightObject = new GameObject();
        SpotLight spotLight = new SpotLight(
                new Vector3f( 0f, 0f,  1f ),
                0.5f,
                new Attenuation( 1, 0.01f, 0.0001f ),
                0.7f
        );
        spotLightObject.getTransform()
                .rotate( new Vector3f( 0, 1, 0 ), -90 )
                .rotate( new Vector3f( 1, 0, 0 ), 40 )
                .translate( new Vector3f( 55, 10f, 8 ) );
        spotLightObject.addComponent( spotLight );


        PhysicsObject player = new Player.PlayerBuilder()
                .setCamera( Camera.CameraBuilder.getDefaultCamera() )
                .setMultiMeshRenderer( new MultiMeshRenderer( "reaper/", "reaper.obj" ) )
                .setWeapon( new Weapon() )
                .build( true );

        addObjects( plane1, plane2, characterObject, dirLightObject, pointLightObject, spotLightObject );
        addPhysicalObject( player );

        super.init();
    }

    float t = 0;

    public void update( float deltaTime )
    {
        t += deltaTime * 5;
        plane2.getTransform()
                .setTranslation( new Vector3f( (float)Math.cos(t)*2 - 30, (float)Math.sin(t)*2 + 4, (float)Math.sin(t)*2 + 8 ) );
        super.update( deltaTime );
    }
}
