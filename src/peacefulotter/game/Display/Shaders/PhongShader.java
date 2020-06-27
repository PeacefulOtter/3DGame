package peacefulotter.game.Display.Shaders;

import peacefulotter.game.Display.Camera;
import peacefulotter.game.Display.Graphics.Material;
import peacefulotter.game.Display.Shaders.Transfomations.ShaderTransform;
import peacefulotter.game.Maths.Matrix4f;
import peacefulotter.game.Maths.Vector3f;
import peacefulotter.game.Utils.RenderUtil;
import peacefulotter.game.Utils.ResourceLoader;

public class PhongShader extends Shader
{
    private static PhongShader instance = new PhongShader();

    private static Vector3f ambientLight = new Vector3f( 0.1f, 0.1f, 0.1f );
    private static DirectionalLight directionalLight = new DirectionalLight(
            new BaseLight( new Vector3f( 1, 1, 1 ), 0 ), new Vector3f( 0, 0, 0 ) );

    private PhongShader()
    {
        super();
        addVertexShader( new ResourceLoader().loadShader( "phongVertex.vs" ) );
        addFragmentShader( new ResourceLoader().loadShader( "phongFragment.fs" ) );
        compileShader();

        addUniform( "transform" );
        addUniform( "transformProjected" );
        addUniform( "baseColor" );
        addUniform( "ambientLight" );

        addUniform( "specularIntensity" );
        addUniform( "specularExponent" );
        addUniform( "eyePos" );

        addUniform( "dirLight.base.color" );
        addUniform( "dirLight.base.intensity" );
        addUniform( "dirLight.direction" );
    }

    public void updateUniforms( Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material, Vector3f cameraPos )
    {
        if ( material.getTexture() != null )
            material.getTexture().bind();
        else
            RenderUtil.unbindTextures();

        setUniformMatrix( "transformProjected", projectedMatrix );
        setUniformMatrix( "transform", worldMatrix );
        setUniformVector( "baseColor", material.getColor() );

        setUniformF( "specularIntensity", material.getSpecularIntensity() );
        setUniformF( "specularExponent", material.getSpecularExponent() );
        setUniformVector( "eyePos", cameraPos );

        setUniformVector( "ambientLight", ambientLight );
        setUniformDirLight( "dirLight", directionalLight );
    }

    public void setUniformBaseLight( String uniformName, BaseLight baseLight )
    {
        setUniformVector( uniformName + ".color", baseLight.getColor() );
        setUniformF( uniformName + ".intensity", baseLight.getIntensity() );
    }

    public void setUniformDirLight( String uniformName, DirectionalLight directionalLight )
    {
        setUniformBaseLight( uniformName + ".base", directionalLight.getBase() );
        setUniformVector( uniformName + ".direction", directionalLight.getDirection() );
    }


    public static Shader getInstance() { return PhongShader.instance; }

    public static Vector3f getAmbientLight() { return PhongShader.ambientLight; }
    public static void setAmbientLight( Vector3f ambientLight ) { PhongShader.ambientLight = ambientLight; }

    public static DirectionalLight getDirectionalLight() { return PhongShader.directionalLight; }
    public static void setDirectionalLight( DirectionalLight directionalLight ) { PhongShader.directionalLight = directionalLight; }

}
