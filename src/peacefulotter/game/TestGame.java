package peacefulotter.game;

import peacefulotter.engine.Utils.IO.Input;
import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.core.Game;
import peacefulotter.engine.core.Maths.Vector2f;
import peacefulotter.engine.core.Maths.Vector3f;
import peacefulotter.engine.core.elementary.GameObject;
import peacefulotter.engine.core.elementary.Initializable;
import peacefulotter.engine.rendering.Camera;
import peacefulotter.engine.rendering.Graphics.*;
import peacefulotter.engine.rendering.Shaders.*;
import peacefulotter.engine.rendering.Shaders.Transfomations.ShaderTransform;
import peacefulotter.engine.rendering.Window;

/*
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
    private final CoreEngine coreEngine;
    private final GameObject root;

    public TestGame( String winName, int winWidth, int winHeight )
    {
        super( new GameObject( new ShaderTransform() ) );
        root = getRootObject();
        this.coreEngine = new CoreEngine( this, winName, winWidth, winHeight );
    }

    public void init()
    {
        PhongShader.setAmbientLight( new Vector3f( 0.8f, 0.8f, 0.8f ) );

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

        GameObject plane = new GameObject( root.getTransform() );
        MeshRenderer meshRenderer = new MeshRenderer( mesh, material, plane );
        plane.addChild( meshRenderer );
        plane.getTransform().setTranslation( 0, -1, 5 );
        root.addChild( plane );

        super.init();
    }

    public void startEngine() { coreEngine.start(); }
}
