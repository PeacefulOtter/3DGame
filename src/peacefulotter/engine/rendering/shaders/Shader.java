package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.components.lights.BaseLight;
import peacefulotter.engine.components.lights.DirectionalLight;
import peacefulotter.engine.components.lights.PointLight;
import peacefulotter.engine.components.lights.SpotLight;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.BufferUtil;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Texture;
import peacefulotter.engine.rendering.resourceManagement.ShaderResource;
import peacefulotter.engine.core.transfomations.STransform;
import peacefulotter.engine.utils.Logger;

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

    // used for multi-texturing = updates the diffuse, normalMaps and height map
    public void forceUpdateTexture( Material material, RenderingEngine renderingEngine )
    {
        resource.getUniformsStruct().forEach( ( struct ) -> {
            String uniformName = struct.name;
            String uniformType = struct.type;

            if ( uniformType.equals( "sampler2D" ) && uniformName.startsWith( RENDERING_PREFIX ) )
            {
                String unprefixedUniformName = uniformName.substring( 2 );
                int samplerSlot = renderingEngine.getSamplerSlot( unprefixedUniformName );
                material.getTexture( unprefixedUniformName ).bind( samplerSlot );
                setUniformI( uniformName, samplerSlot );
            }
        } );
    }

    public void updateUniforms( STransform transform, Material material, RenderingEngine renderingEngine )
    {
        updateUniforms( transform, material, renderingEngine, false );
    }

    public void updateUniforms( STransform transform, Material material, RenderingEngine renderingEngine, boolean isSkybox )
    {
        Matrix4f transformationMatrix = transform.getTransformationMatrix();
        Matrix4f projectionMatrix = renderingEngine.getCamera().getProjectionMatrix();
        Matrix4f viewMatrix = renderingEngine.getCamera().getViewMatrix();

        List<ShaderResource.GLSLStruct> uniformsStruct = resource.getUniformsStruct();

        for ( ShaderResource.GLSLStruct struct : uniformsStruct )
        {
            String uniformName = struct.name;
            String uniformType = struct.type;
            String unprefixedUniformName = uniformName.substring( 2 );

            if ( uniformName.startsWith( TRANSFORM_PREFIX) )
            {
                if ( unprefixedUniformName.equals(  "transformationMatrix" ) )
                    setUniformMatrix( uniformName, transformationMatrix );
                else if ( unprefixedUniformName.equals( "projectionMatrix" ) )
                    setUniformMatrix( uniformName, projectionMatrix );
                else if ( unprefixedUniformName.equals( "viewMatrix" ) )
                {
                    if ( isSkybox )
                    {
                        Matrix4f skyBoxViewMatrix = new Matrix4f( viewMatrix.getM() );
                        skyBoxViewMatrix.setAt( 0, 3, 0 );
                        skyBoxViewMatrix.setAt( 1, 3, 0 );
                        skyBoxViewMatrix.setAt( 2, 3, 0 );
                        setUniformMatrix( uniformName, skyBoxViewMatrix );
                    }
                    else
                        setUniformMatrix( uniformName, viewMatrix );
                }
                else
                    throw new IllegalArgumentException( uniformName + " is not a valid Transform component" );
            }

            else if ( uniformName.startsWith( RENDERING_PREFIX ) )
            {
                if ( uniformType.equals( "sampler2D" ) )
                {
                    int samplerSlot = renderingEngine.getSamplerSlot( unprefixedUniformName );
                    material.getTexture( unprefixedUniformName ).bind( samplerSlot );
                    setUniformI( uniformName, samplerSlot );
                }
                else if ( uniformType.equals( "samplerCube" ) )
                {
                    int samplerSlot = renderingEngine.getSamplerSlot( unprefixedUniformName );
                    material.getTexture( unprefixedUniformName ).bind( samplerSlot, GL_TEXTURE_CUBE_MAP );
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
        setUniformVector( uniformName + ".direction", directionalLight.getTransform().getTransformedRotation().getForward() );
    }

    public void setUniformPointLight( String uniformName, PointLight pointLight )
    {
        setUniformBaseLight( uniformName + ".base", pointLight );
        setUniformF( uniformName + ".atten.constant", pointLight.getAttenuation().getConstant() );
        setUniformF( uniformName + ".atten.linear", pointLight.getAttenuation().getLinear() );
        setUniformF( uniformName + ".atten.exponent", pointLight.getAttenuation().getExponent() );
        setUniformVector( uniformName + ".position", pointLight.getTransform().getTransformedTranslation() );
        setUniformF( uniformName + ".range", pointLight.getRange() );
    }

    public void setUniformSpotLight( String uniformName, SpotLight spotLight )
    {
        setUniformPointLight( uniformName + ".pointLight", spotLight );
        setUniformVector( uniformName + ".direction", spotLight.getTransform().getTransformedRotation().getForward() );
        setUniformF( uniformName + ".cutoff", spotLight.getCutoff() );
    }

    public void bind()
    {
        glUseProgram( resource.getProgram() );
    }
}
