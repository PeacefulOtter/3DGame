package peacefulotter.game;

import peacefulotter.engine.components.*;
import peacefulotter.engine.components.lights.DirectionalLight;
import peacefulotter.engine.components.lights.PointLight;
import peacefulotter.engine.components.renderer.MeshRenderer;
import peacefulotter.engine.components.renderer.MultiMeshRenderer;
import peacefulotter.engine.core.Game;
import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.Mesh;
import peacefulotter.engine.rendering.graphics.Texture;
import peacefulotter.engine.rendering.shaders.Attenuation;
import peacefulotter.game.actor.Ghost;
import peacefulotter.game.actor.Player;
import peacefulotter.game.actor.Weapon;

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
        World world = World.UniqueWorld.INSTANCE.getWorld();

        Mesh tree1 = new Mesh( "tree1/", "Tree_OBJ.obj" );
        GameObject tree = new GameObject().addComponent( new MeshRenderer( tree1, alienMaterial ) );
        tree.getTransform().scale( 0.03f ).translate( new Vector3f( 200, 0, 400 ) );
        // MultiMeshRenderer mmr = new MultiMeshRenderer( "tree1/", "Tree_OBJ.obj" );
        // GameObject tree = new GameObject().addComponent( mmr );
        // tree.getTransform().scale( 0.03f ).translate( new Vector3f( 200, 0, 400 ) );


        //Mesh house = new Mesh( "house/", "house3.obj" );
        //GameObject houseObject = new GameObject().addComponent( new MeshRenderer( house, alienMaterial ) );
        //houseObject.getTransform().scale( 30 ).rotate( Vector3f.Y_AXIS, 180 );


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
                //.setCamera( Camera.CameraBuilder.getDefaultCamera() )
                .setMultiMeshRenderer( new MultiMeshRenderer( "reaper/", "reaper.obj") )
                .setWeapon( new Weapon() )
                .build( false );

        Ghost ghost = new Ghost( true );
        ghost.addComponent( Camera.CameraBuilder.getDefaultCamera() );
        ghost.getTransform().translate( new Vector3f( 0, 10, 0 ) );


        /* LIGHTS */
        GameObject dirLightObject = new GameObject();
        DirectionalLight dirLight = new DirectionalLight(
                new Vector3f( 1f, 0.9f,0.7f ),
                0.10f );
        dirLightObject.addComponent( dirLight );
        dirLightObject.getTransform().setRotation( new Quaternion( new Vector3f( 1, -1, 0 ), -45 ) );

        GameObject pointLightObject = new GameObject();
        PointLight pointLight = new PointLight( new Vector3f( 0.3f, 1f, 0.5f ), 0.05f, Attenuation.DEFAULT );
        pointLight.getTransform().translate( new Vector3f( 200, 90, 400 ) );
        pointLightObject.addComponent( pointLight );

        addObjects( world, tree, dirLightObject, pointLightObject );
        addPhysicalObjects( player, ghost );

        super.init();
    }

    public void update( float deltaTime )
    {
        super.update( deltaTime );
    }
}

