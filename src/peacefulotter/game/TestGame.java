package peacefulotter.game;

import peacefulotter.engine.components.*;
import peacefulotter.engine.components.lights.BaseLight;
import peacefulotter.engine.components.lights.DirectionalLight;
import peacefulotter.engine.components.lights.PointLight;
import peacefulotter.engine.components.lights.SpotLight;
import peacefulotter.engine.core.Game;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.Window;
import peacefulotter.engine.rendering.graphics.*;
import peacefulotter.engine.rendering.shaders.*;

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
        Material material = new Material(
                new Texture( "test.png" ),
                new Vector3f( 1, 1, 1 ),
                1,
                8 );

        GameObject plane = new GameObject();
        MeshRenderer meshRenderer = new MeshRenderer( mesh, material );
        plane.addComponent( meshRenderer );
        plane.getTransform().getTranslation().set( 0, -1, 5 );

        GameObject dirLightObject = new GameObject();
        DirectionalLight dirLight = new DirectionalLight(
                new Vector3f( 0.6f, 0.3f,  0.6f ),
                0.5f,
                new Vector3f( 1, 1, 1 ) );
        dirLightObject.addComponent( dirLight );

        GameObject pointLightObject = new GameObject();
        PointLight pointLight = new PointLight(
                new Vector3f( 0.4f, 1,  0.4f ),
                0.4f,
                new Attenuation( 1, 0, 1f ),
                new Vector3f( 5, 0, 5 ) );
        dirLightObject.addComponent( pointLight );

        GameObject spotLightObject = new GameObject();
        SpotLight spotLight = new SpotLight(
                new Vector3f( 1f, 0,  0f ),
                1f,
                new Attenuation( 0, 0.05f, 0 ),
                new Vector3f( 5, 0, 5 ),
                new Vector3f( 1, 0, 0 ),
                0.7f
        );
        spotLightObject.addComponent( spotLight );

        addObject( plane );
        addObject( dirLightObject );
        addObject( pointLightObject );
        addObject( spotLightObject );

        Camera camera = new Camera(
                (float)Math.toRadians(70),
                (float) Window.getWidth() / (float) Window.getHeight(),
                0.01f, 1000f);
        setCamera( camera );
        addComponent( camera );

        super.init();
    }
}
