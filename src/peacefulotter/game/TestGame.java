package peacefulotter.game;

import peacefulotter.engine.components.*;
import peacefulotter.engine.components.lights.DirectionalLight;
import peacefulotter.engine.components.lights.PointLight;
import peacefulotter.engine.components.lights.SpotLight;
import peacefulotter.engine.core.Game;
import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.Window;
import peacefulotter.engine.rendering.graphics.*;
import peacefulotter.engine.rendering.shaders.*;

import static peacefulotter.engine.utils.IO.Input.MOUSE_PRIMARY;
import static peacefulotter.engine.utils.IO.Input.MOUSE_SECONDARY;

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
                1, 8, 0.04f, -0.5f );


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

        GameObject alienObject = new GameObject().addComponent(
                new MeshRenderer(  new Mesh( "alien.obj" ), alienMaterial ) );
        addObject( alienObject );
        alienObject.getTransform().translate( new Vector3f( 3, 0, 3 ) ).scale( 0.5f );

        GameObject sphereObject1 = new GameObject( new Vector3f(-10f, 0, 0 ) ).addComponent(
                new MeshRenderer(  new Mesh( "sphere.obj" ), bricks1 ) );
        addObject( sphereObject1 );
        sphereObject1.getTransform().translate( new Vector3f( 30, 0, 30 ) ).scale( 0.3f );

        GameObject sphereObject2 = new GameObject( new Vector3f(10f, 0, 0 ) ).addComponent(
                new MeshRenderer(  new Mesh( "sphere.obj" ), bricks2 ) );
        addObject( sphereObject2 );
        sphereObject2.getTransform().translate( new Vector3f( 10, 0, 30 ) ).scale( 0.3f );



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

        GameObject cameraObject = new GameObject();
        Camera camera = new Camera(
                70f,
                (float) Window.getWidth() / (float) Window.getHeight(),
                0.01f, 1000f );
        camera.addMouseCallback( MOUSE_PRIMARY, ( deltaTime ) -> {
            System.out.println("shoottingg");
        } );
        camera.addMouseCallback( MOUSE_SECONDARY, ( deltaTime ) -> {
            System.out.println("aiminggg");
        } );
        cameraObject.getTransform().translate( new Vector3f( 0, 1, 0 ) );
        cameraObject.addComponent( camera );
        // Hide Mouse
        // int hideMouse = action == 1 ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL;
        // glfwSetInputMode( window, GLFW_CURSOR, hideMouse );
        cameraObject.getTransform().translate( new Vector3f(0, 2, 0) );

        addObjects( plane1, plane2, dirLightObject, pointLightObject, spotLightObject, cameraObject );

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
