package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.rendering.Camera;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.utils.ResourceLoader;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;


public class PhongShader extends Shader
{
    private static final int MAX_POINT_LIGHTS = 4;
    private static final int MAX_SPOT_LIGHTS = 4;

    private static final PhongShader instance = new PhongShader();

    private static Vector3f ambientLight = new Vector3f( 0.1f, 0.1f, 0.1f );
    private static DirectionalLight directionalLight = new DirectionalLight(
            new BaseLight( new Vector3f( 1, 1, 1 ), 0 ),
            new Vector3f( 0, 0, 0 ) );
    private static PointLight[] pointLights = new PointLight[] {};
    private static SpotLight[] spotLights = new SpotLight[] {};

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

        for ( int i = 0; i < MAX_POINT_LIGHTS; i++ )
        {
            addUniform( "pointLights[" + i + "].base.color" );
            addUniform( "pointLights[" + i + "].base.intensity" );
            addUniform( "pointLights[" + i + "].attenuation.constant" );
            addUniform( "pointLights[" + i + "].attenuation.linear" );
            addUniform( "pointLights[" + i + "].attenuation.exponent" );
            addUniform( "pointLights[" + i + "].position" );
            addUniform( "pointLights[" + i + "].range" );
        }

        for ( int i = 0; i < MAX_SPOT_LIGHTS; i++ )
        {
            addUniform( "spotLights[" + i + "].pointLight.base.color" );
            addUniform( "spotLights[" + i + "].pointLight.base.intensity" );
            addUniform( "spotLights[" + i + "].pointLight.attenuation.constant" );
            addUniform( "spotLights[" + i + "].pointLight.attenuation.linear" );
            addUniform( "spotLights[" + i + "].pointLight.attenuation.exponent" );
            addUniform( "spotLights[" + i + "].pointLight.position" );
            addUniform( "spotLights[" + i + "].pointLight.range" );
            addUniform( "spotLights[" + i + "].direction" );
            addUniform( "spotLights[" + i + "].cutoff" );
        }
    }

    public void updateUniforms(STransform transform, Material material )
    {
        Camera camera = getRenderingEngine().getCamera();
        Matrix4f worldMatrix = transform.getTransformationMatrix();
        Matrix4f projectedMatrix = camera.getViewProjection().mul( worldMatrix );
        material.getTexture().bind();

        setUniformMatrix( "transformProjected", projectedMatrix );
        setUniformMatrix( "transform", worldMatrix );
        setUniformVector( "baseColor", material.getColor() );

        setUniformF( "specularIntensity", material.getSpecularIntensity() );
        setUniformF( "specularExponent", material.getSpecularExponent() );
        setUniformVector( "eyePos", camera.getPosition() );

        setUniformVector( "ambientLight", ambientLight );
        setUniformDirLight( "dirLight", directionalLight );

        for ( int i = 0; i < pointLights.length; i++ )
            setUniformPointLights( "pointLights[" + i + "]", pointLights[ i ] );

        for ( int i = 0; i < spotLights.length; i++ )
            setUniformSpotLights( "spotLights[" + i + "]", spotLights[ i ] );
    }

    public void setUniformBaseLight( String uniformName, BaseLight baseLight )
    {
        setUniformVector( uniformName + ".color", baseLight.getColor() );
        setUniformF( uniformName + ".intensity", baseLight.getIntensity() );
    }

    public void setUniformDirLight( String uniformName, DirectionalLight directionalLight )
    {
        setUniformBaseLight( uniformName + ".base", directionalLight.getBaseLight() );
        setUniformVector( uniformName + ".direction", directionalLight.getDirection() );
    }

    public void setUniformPointLights( String uniformName, PointLight pointLight )
    {
        setUniformBaseLight( uniformName + ".base", pointLight.getBaseLight() );
        setUniformF( uniformName + ".attenuation.constant", pointLight.getAttenuation().getConstant() );
        setUniformF( uniformName + ".attenuation.linear", pointLight.getAttenuation().getLinear() );
        setUniformF( uniformName + ".attenuation.exponent", pointLight.getAttenuation().getExponent() );
        setUniformVector( uniformName + ".position", pointLight.getPosition() );
        setUniformF( uniformName + ".range", pointLight.getRange() );
    }

    public void setUniformSpotLights( String uniformName, SpotLight spotLight )
    {
        setUniformPointLights( uniformName + ".pointLight", spotLight.getPointLight() );
        setUniformVector( uniformName + ".direction", spotLight.getDirection() );
        setUniformF( uniformName + ".cutoff", spotLight.getCutoff() );
    }


    public static Shader getInstance() { return PhongShader.instance; }

    public static Vector3f getAmbientLight() { return PhongShader.ambientLight; }
    public static void setAmbientLight( Vector3f ambientLight ) { PhongShader.ambientLight = ambientLight; }

    public static DirectionalLight getDirectionalLight() { return PhongShader.directionalLight; }
    public static void setDirectionalLight( DirectionalLight directionalLight ) { PhongShader.directionalLight = directionalLight; }

    public static void setPointLights( PointLight[] pointLights )
    {
        if ( pointLights.length > MAX_POINT_LIGHTS )
        {
            System.err.println(
                    "Error : you passed in too many point lights, max allowed is " +
                            MAX_POINT_LIGHTS + ", you passed " + pointLights.length );
            new Exception().printStackTrace();
            System.exit(1);
        }

        PhongShader.pointLights = pointLights;
    }

    public static void setSpotLights( SpotLight[] spotLights )
    {
        if ( spotLights.length > MAX_SPOT_LIGHTS )
        {
            System.err.println(
                    "Error : you passed in too many point lights, max allowed is " +
                            MAX_SPOT_LIGHTS + ", you passed " + spotLights.length );
            new Exception().printStackTrace();
            System.exit(1);
        }

        PhongShader.spotLights = spotLights;
    }
}