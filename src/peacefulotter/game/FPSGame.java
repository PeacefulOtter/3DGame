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
import peacefulotter.game.actor.Player;

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

    GameObject plane2, plane3;

    public void init()
    {
        // new ResourceLoader().loadMaterial( "reaper.mtl" );

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
        Mesh mesh = new Mesh( "plane3.obj" );
        GameObject map = new GameObject()
                .addComponent( new MeshRenderer( mesh, bricks2 ) );
        map.getTransform().scale( 2 );


        /* DUMMY */
        GameObject sphereObj = new GameObject();
        sphereObj.addComponent( new MeshRenderer( new Mesh( "sphere.obj" ), bricks2 ) );
        sphereObj.getTransform().translate( new Vector3f( 0, 6, 0) ).scale( 0.04f );

        PhysicsObject dummyObj =  new PhysicsObject( Vector3f.getZero() );
        GameObject dummyReaper = new GameObject()
                .addComponent( new MeshRenderer( new Mesh( "reaper.obj" ), alienMaterial ) );
        dummyObj
                .addChild( dummyReaper )
                .addChild( sphereObj );
        dummyObj.getTransform()
                .translate( new Vector3f( 0, 0, 5 ) )
                .rotate( new Vector3f(0, 1, 0 ), 180);


        /* PLAYERS */
        PhysicsObject characterObject = new Player();
        GameComponent reaperObject = new MeshRenderer( new Mesh( "reaper.obj" ), alienMaterial );
        GameObject weaponObject = new GameObject();
        weaponObject.addComponent( new MeshRenderer( new Mesh( "m4a1.obj" ), alienMaterial ) );
        weaponObject.getTransform().translate( new Vector3f( 1, 4.5f, 0.3f ) );

        Camera camera = new Camera(
                70f,
                (float) Window.getWidth() / (float) Window.getHeight(),
                0.01f, 1000f );
        camera.setInnerTranslation( new Vector3f( 0, 6.5f, 0f ) );

        characterObject
                .addComponent( reaperObject )
                .addChild( weaponObject )
                .addComponent( camera );



        /* LIGHTS */
        GameObject dirLightObject = new GameObject();
        DirectionalLight dirLight = new DirectionalLight(
                new Vector3f( 1f, 0.9f,0.8f ),
                0.15f );
        dirLightObject.addComponent( dirLight );
        dirLightObject.getTransform().setRotation( new Quaternion( new Vector3f( 1, -1, 0 ), -45 ) );


        addObjects( map, dirLightObject );
        addPhysicalObjects( dummyObj, characterObject );

        super.init();
    }

    public void update( float deltaTime )
    {
        super.update( deltaTime );
    }
}

