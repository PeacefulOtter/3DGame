package peacefulotter.game;

import peacefulotter.engine.components.*;
import peacefulotter.engine.components.lights.BaseLight;
import peacefulotter.engine.components.lights.DirectionalLight;
import peacefulotter.engine.components.lights.PointLight;
import peacefulotter.engine.components.lights.SpotLight;
import peacefulotter.engine.core.Game;
import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.Window;
import peacefulotter.engine.rendering.graphics.*;
import peacefulotter.engine.rendering.shaders.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static peacefulotter.engine.utils.IO.Input.MOUSE_PRIMARY;
import static peacefulotter.engine.utils.IO.Input.MOUSE_SECONDARY;

/*
CAMERA SPOTLIGHT
     spotLight = new SpotLight(
                new PointLight(
                        new BaseLight( new Vector3f( 1, 0.5f, 0 ), 0.8f ),
                        new Attenuation( 0, 0.1f, 0.02f ),
                        new Vector3f( -2, 0, 5 ),
                        30
                ),
                new Vector3f( 1, 1, 1 ),
                0.7f
        );
        PhongShader.setSpotLights( new SpotLight[] { spotLight } );

PointLight p1 = new PointLight(
            new BaseLight( new Vector3f( 1, 0, 0 ), 4f ),
            new Attenuation( 0, 0.3f, 0.5f ),
            new Vector3f( -2, 0, 3 ),
            10 );
PointLight p2 = new PointLight(
            new BaseLight( new Vector3f( 0.3f, 0.7f, 1 ), 3f ),
            new Attenuation( 1, 0.3f, 0.01f ),
            new Vector3f( 2, 0, 7 ),
            20 );
PhongShader.setPointLights( new PointLight[] { p1, p2 } );
p1.setPosition( new Vector3f( 3, 1, 8 * sinTemp + 10.5f ) );
p2.setPosition( new Vector3f( 7, 1, 8 * cosTemp + 10.5f ) );

PhongShader.setDirectionalLight( new DirectionalLight(
        new BaseLight( new Vector3f( 1, 1, 1 ), 0.8f ), new Vector3f( 1, 1, 1 ) ) );
 */

public class TestGame extends Game
{
    public TestGame( String winName, int winWidth, int winHeight )
    {
        super( winName, winWidth, winHeight );
    }

    GameObject plane2;

    public void init()
    {
        float fieldWidth = 10.0f;
        float fieldDepth = 10.0f;

        Vertex[] vertices = new Vertex[] {
                new Vertex( new Vector3f(  -fieldWidth,     0,  -fieldDepth ),     new Vector2f( 0, 0 ) ),
                new Vertex( new Vector3f(  -fieldWidth,     0, fieldDepth * 3 ), new Vector2f( 0, 1 ) ),
                new Vertex( new Vector3f( fieldWidth * 3, 0,  -fieldDepth ),     new Vector2f( 1, 0 ) ),
                new Vertex( new Vector3f( fieldWidth * 3, 0, fieldDepth * 3 ), new Vector2f( 1, 1 ) )
        };
        int[] indices = new int[] { 0, 1, 2, 2, 1, 3 };

        Mesh mesh = new Mesh( vertices, indices, true ); // new ResourceLoader().loadMesh( "cube.obj", true );
        /*Material material = new Material(
                new Texture( "test.png" ),
                new Vector3f( 1, 1, 1 ),
                1,
                8 );*/
        Material material = new Material();
        material.addTexture( "diffuse", new Texture( "test.png" ) );
        material.addFloat( "specularIntensity", 1 );
        material.addFloat( "specularExponent", 8 );

        GameObject plane1 = new GameObject().addComponent( new MeshRenderer( mesh, material ) );
        plane2 = new GameObject().addComponent( new MeshRenderer( mesh, material ) );
        GameObject plane3 = new GameObject().addComponent( new MeshRenderer( mesh, material ) );
        GameObject plane4 = new GameObject().addComponent( new MeshRenderer( mesh, material ) );
        plane2.getTransform().translate( new Vector3f( 8, 4, 8 ) ).rotate( new Vector3f( 0, 1, 0 ), -30 ).scale( 0.5f );
        plane3.getTransform().translate( new Vector3f( 0, 3, 0 ) ).scale( 0.5f );
        plane4.getTransform().translate( new Vector3f( 0, 6, 0 ) ).scale( 0.5f );
        plane3.addChild( plane4 );
        plane2.addChild( plane3 );

        GameObject dirLightObject = new GameObject();
        DirectionalLight dirLight = new DirectionalLight(
                new Vector3f( 0.6f, 0.3f,  0.6f ),
                0.5f );
        dirLightObject.addComponent( dirLight );
        dirLightObject.getTransform().setRotation( new Quaternion( new Vector3f( 1, -1, 0 ), -45 ) );


        GameObject pointLightObject = new GameObject();
        PointLight pointLight = new PointLight(
                new Vector3f( 0.4f, 2,  0.4f ),
                0.5f,
                new Attenuation( 1, 0, 0.2f ) );
        pointLightObject.addComponent( pointLight );
        pointLightObject.getTransform().translate( new Vector3f( 2, 0.2f, 3 ) );

        GameObject spotLightObject = new GameObject();
        SpotLight spotLight = new SpotLight(
                new Vector3f( 1f, 0,  0f ),
                1f,
                new Attenuation( 0, 0f, 0.01f ),
                0.7f
        );
        spotLightObject.getTransform()
                .rotate( new Vector3f( 0, 1, 0 ), 90 )
                .rotate( new Vector3f( 1, 0, 0 ), 20 )
                .translate( new Vector3f( 5, 0.2f, 1 ) );
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
        plane4.addChild( cameraObject );
        cameraObject.getTransform().translate( new Vector3f(0, 2, 0) );
        addObjects( plane1, plane2, dirLightObject, pointLightObject, spotLightObject );

        super.init();
    }

    float t = 0;

    public void update( float deltaTime )
    {
        t += deltaTime * 5;
        plane2.getTransform().setTranslation( new Vector3f( (float)Math.sin(t)*2, 4, 8 ) );

        super.update( deltaTime );
    }
}
