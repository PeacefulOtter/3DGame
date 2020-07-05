package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.components.Camera;
import peacefulotter.engine.components.lights.BaseLight;
import peacefulotter.engine.components.lights.DirectionalLight;
import peacefulotter.engine.components.lights.PointLight;
import peacefulotter.engine.components.lights.SpotLight;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.BufferUtil;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.resourceManagement.ShaderResource;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

import java.util.List;

import static org.lwjgl.opengl.GL20.*;

public class Shader
{
    private static final String TRANSFORM_PREFIX = "T_";
    private static final String RENDERING_PREFIX = "R_";
    private static final String CAMERA_PREFIX    = "C_";
    private final ShaderResource resource;

    protected Shader( ShaderResource resource )
    {
        this.resource = resource;
    }

    public void updateUniforms( STransform transform, Material material, RenderingEngine renderingEngine )
    {
        Matrix4f worldMatrix = transform.getTransformationMatrix();
        Matrix4f MVPMatrix = renderingEngine.getCamera().getViewProjection().mul( worldMatrix );

        List<ShaderResource.GLSLStruct> uniformsStruct = resource.getUniformsStruct();

        for ( ShaderResource.GLSLStruct struct : uniformsStruct )
        {
            String uniformName = struct.name;
            String uniformType = struct.type;

            if ( uniformName.startsWith( TRANSFORM_PREFIX) )
            {
                if ( uniformName.equals( TRANSFORM_PREFIX + "MVP" ) )
                    setUniformMatrix( uniformName, MVPMatrix );
                else if ( uniformName.equals( TRANSFORM_PREFIX + "model" ) )
                    setUniformMatrix( uniformName, worldMatrix );
                else
                    throw new IllegalArgumentException( uniformName + " is not a valid Transform component" );
            }

            else if ( uniformName.startsWith( RENDERING_PREFIX ) )
            {
                String unprefixedUniformName = uniformName.substring( 2 );
                if ( uniformType.equals( "sampler2D" ) )
                {
                    int samplerSlot = renderingEngine.getSamplerSlot( unprefixedUniformName );
                    material.getTexture( unprefixedUniformName ).bind( samplerSlot );
                    setUniformI( uniformName, samplerSlot );
                }
                else if ( uniformType.equals( "vec3" ) )
                    setUniformVector( uniformName, renderingEngine.getVector3f( unprefixedUniformName ) );
                else if ( uniformType.equals( "float" ) )
                    setUniformF( uniformName, renderingEngine.getFloat( unprefixedUniformName ) );
                else if ( uniformType.equals( "DirectionalLight" ) )
                    setUniformDirLight( uniformName, (DirectionalLight) renderingEngine.getActiveLight() );
                else if ( uniformType.equals( "PointLight" ) )
                    setUniformPointLight( uniformName, (PointLight) renderingEngine.getActiveLight() );
                else if ( uniformType.equals( "SpotLight" ) )
                    setUniformSpotLight( uniformName, (SpotLight) renderingEngine.getActiveLight() );
                else
                    throw new IllegalArgumentException( uniformType + " is not a supported type" );
            }
            else if ( uniformName.startsWith( CAMERA_PREFIX ) )
            {
                if ( uniformName.equals( CAMERA_PREFIX + "eyePos" ) )
                    setUniformVector( uniformName, renderingEngine.getCamera().getTransform().getTransformedTranslation() );
                else
                    throw new IllegalArgumentException( uniformName + " is not a Camera component" );
            }
            else
            {
                if ( uniformType.equals( "vec3" ) )
                    setUniformVector( uniformName, material.getVector3f( uniformName ) );
                else if ( uniformType.equals( "float" ) )
                    setUniformF( uniformName, material.getFloat( uniformName ) );
                else
                    throw new IllegalArgumentException( uniformType + " is not a supported Material" );
            }
        }
    }


    public void setUniformI( String uniformName, int value )
    {
        glUniform1i( resource.getUniformsMap().get( uniformName ), value );
    }

    public void setUniformF( String uniformName, float value )
    {
        glUniform1f( resource.getUniformsMap().get( uniformName ), value );
    }

    public void setUniformVector( String uniformName, Vector3f value )
    {
        glUniform3f( resource.getUniformsMap().get( uniformName ), value.getX(), value.getY(), value.getZ() );
    }

    public void setUniformMatrix( String uniformName, Matrix4f value )
    {
        glUniformMatrix4fv( resource.getUniformsMap().get( uniformName ), true, BufferUtil.createFlippedBuffer( value ) );
    }


    public void setUniformBaseLight( String uniformName, BaseLight baseLight )
    {
        setUniformVector( uniformName + ".color", baseLight.getColor() );
        setUniformF( uniformName + ".intensity", baseLight.getIntensity() );
    }

    public void setUniformDirLight( String uniformName, DirectionalLight directionalLight )
    {
        setUniformBaseLight( uniformName + ".base", directionalLight );
        setUniformVector( uniformName + ".direction", directionalLight.getDirection() );
    }

    public void setUniformPointLight( String uniformName, PointLight pointLight )
    {
        setUniformBaseLight( uniformName + ".base", pointLight );
        setUniformF( uniformName + ".attenuation.constant", pointLight.getAttenuation().getConstant() );
        setUniformF( uniformName + ".attenuation.linear", pointLight.getAttenuation().getLinear() );
        setUniformF( uniformName + ".attenuation.exponent", pointLight.getAttenuation().getExponent() );
        setUniformVector( uniformName + ".position", pointLight.getPosition() );
        setUniformF( uniformName + ".range", pointLight.getRange() );
    }

    public void setUniformSpotLight( String uniformName, SpotLight spotLight )
    {
        setUniformPointLight( uniformName + ".pointLight", spotLight );
        setUniformVector( uniformName + ".direction", spotLight.getDirection() );
        setUniformF( uniformName + ".cutoff", spotLight.getCutoff() );
    }

    public void bind()
    {
        glUseProgram( resource.getProgram() );
    }
}
