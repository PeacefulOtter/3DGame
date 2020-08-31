package peacefulotter.game;

import peacefulotter.engine.components.*;
import peacefulotter.engine.components.lights.DirectionalLight;
import peacefulotter.engine.components.lights.PointLight;
import peacefulotter.engine.components.lights.SpotLight;
import peacefulotter.engine.core.Game;
import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.Window;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.Mesh;
import peacefulotter.engine.rendering.graphics.Texture;
import peacefulotter.engine.rendering.shaders.Attenuation;
import peacefulotter.engine.utils.ResourceLoader;
import peacefulotter.game.actor.Ghost;
import peacefulotter.game.actor.Player;
import peacefulotter.game.actor.Weapon;

import static peacefulotter.engine.utils.IO.Input.MOUSE_PRIMARY;
import static peacefulotter.engine.utils.IO.Input.MOUSE_SECONDARY;

// Hide Mouse
// int hideMouse = action == 1 ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL;
// glfwSetInputMode( window, GLFW_CURSOR, hideMouse );

public class FPSGame extends Game
{
    public FPSGame( String winName, int winWidth, int winHeight )
    {
        super( winName, winWidth, winHeight );
    }

    public void init()
    {
        /* MATERIALS */
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

        /* MAP */
        World world = new World( bricks2 );
        /* Mesh mesh = new Mesh( "plane3.obj" );
        GameObject world = new GameObject()
                .addComponent( new MeshRenderer( mesh, bricks2 ) );
        world.getTransform().scale( 4 );


        /* DUMMY */
        /*Player dummy = new Player.PlayerBuilder()
                .setWeapon( new Weapon( Weapon.PLAYER_ORIGIN() ) )
                .setMesh( new Mesh( "reaper.obj" ) )
                .setMaterial( alienMaterial )
                .build( false );

        GameObject sphereObj = new GameObject();
        sphereObj.addComponent( new MeshRenderer( new Mesh( "sphere.obj" ), bricks2 ) );
        sphereObj.getTransform().setTranslation( new Vector3f( 0, 6, 0 ) );
        sphereObj.getTransform().setScale( new Vector3f( 0.04f, 0.04f, 0.04f ) );

        dummy.addChild( sphereObj );
        dummy.getTransform()
                .translate( new Vector3f( 0, 0, 5 ) )
                .rotate( new Vector3f(0, 1, 0 ), 180);*/


        /* PLAYER */
        PhysicsObject player = new Player.PlayerBuilder()
                .setCamera( Camera.CameraBuilder.getDefaultCamera() )
                .setMesh( new Mesh( "reaper.obj" ) )
                .setMaterial( alienMaterial )
                .setWeapon( new Weapon() )
                .build( true );
        player.getTransform().scale( 20 );

        Ghost ghost = new Ghost( false );
        //ghost.addComponent( Camera.CameraBuilder.getDefaultCamera() );
        ghost.getTransform().translate( new Vector3f( 0, 10, 0 ) );


        /* LIGHTS */
        GameObject dirLightObject = new GameObject();
        DirectionalLight dirLight = new DirectionalLight(
                new Vector3f( 1f, 0.9f,0.7f ),
                0.10f );
        dirLightObject.addComponent( dirLight );
        dirLightObject.getTransform().setRotation( new Quaternion( new Vector3f( 1, -1, 0 ), -45 ) );

        addObjects( world, dirLightObject );
        addPhysicalObjects( player, ghost );

        super.init();
    }

    public void update( float deltaTime )
    {
        super.update( deltaTime );
    }
}

