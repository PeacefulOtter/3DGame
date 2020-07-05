package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.components.lights.BaseLight;
import peacefulotter.engine.components.lights.DirectionalLight;
import peacefulotter.engine.components.lights.PointLight;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.BufferUtil;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.resourceManagement.ShaderResource;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;
import peacefulotter.engine.utils.ResourceLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

abstract public class Shader
{
    private static final String ATTRIBUTE_TAG = "in vec";
    private static final int ATTRIBUTE_TAG_LENGTH = ATTRIBUTE_TAG.length();
    private static final String UNIFORM_TAG = "uniform";
    private static final int UNIFORM_TAG_LENGTH = UNIFORM_TAG.length();
    private static final String STRUCT_TAG = "struct";
    private static final int STRUCT_TAG_LENGTH = STRUCT_TAG.length();

    private static final Map<String, ShaderResource> loadedShaders = new HashMap<>();

    private final ShaderResource resource;

    public Shader( String shaderName )
    {
        if ( loadedShaders.containsKey( shaderName ) )
            resource = loadedShaders.get( shaderName );
        else
        {
            resource = new ShaderResource();
            loadedShaders.put( shaderName, resource );
        }

        String vertexShaderText = new ResourceLoader().loadShader( shaderName + ".vs" );
        String fragmentShaderText = new ResourceLoader().loadShader( shaderName + ".fs" );

        addVertexShader( vertexShaderText );
        addFragmentShader( fragmentShaderText );

        addAllAttributes( vertexShaderText );

        compileShader();

        addAllUniforms( vertexShaderText );
        addAllUniforms( fragmentShaderText );
    }

    abstract public void updateUniforms( STransform transform, Material material, RenderingEngine renderingEngine );

    private String getPropertyName( String line )
    {
        return line.substring( line.indexOf( " " ) + 1 );
    }

    private String getUniformType( String line )
    {
        return line.substring( 0, line.indexOf( ' ' ) );
    }

    private void addAllAttributes( String shaderText )
    {
        List<String> attributesLine = allAllProperty( shaderText, ATTRIBUTE_TAG, ATTRIBUTE_TAG_LENGTH );

        for ( String attributeLine : attributesLine )
        {
            String attributeName = getPropertyName( attributeLine );
            setAttribLocation( attributeName, resource.getAttributes().size() );
            resource.addAttributes( attributeName );
        }
    }

    private void addAllUniforms( String shaderText )
    {
        // ONLY FOR FRAGMENT SHADER
        Map<String, List<GLSLStruct>> structs = findUniformStruct( shaderText );
        List<String> uniformsLine = allAllProperty( shaderText, UNIFORM_TAG, UNIFORM_TAG_LENGTH );

        for ( String uniformLine : uniformsLine )
            addUniformWithStructCheck( getPropertyName( uniformLine ), getUniformType( uniformLine ), structs );
    }

    private void addUniformWithStructCheck( String uniformName, String uniformType, Map<String, List<GLSLStruct>> structs )
    {
        boolean addThis = true;
        List<GLSLStruct> structComponents = structs.get( uniformType );
        if ( structComponents != null )
        {
            addThis = false;
            for ( GLSLStruct structComponent: structComponents )
            {
                addUniformWithStructCheck(
                        uniformName + "." + structComponent.name,
                        structComponent.type,
                        structs );
            }
        }

        if ( addThis )
        {
            addUniform( uniformName );
            resource.addUniform( uniformName );
        }
    }

    private List<String> allAllProperty( String shaderText, String property, int length )
    {
        List<String> propertyList = new ArrayList<>();

        int startLocation = shaderText.indexOf( property );
        while ( startLocation != -1 )
        {
            int begin = startLocation + length + 1;
            int end = shaderText.indexOf( ";", begin );
            String line = shaderText.substring( begin, end );

            propertyList.add( line );

            startLocation = shaderText.indexOf( property, startLocation + length );
        }

        return propertyList;
    }

    private void addUniform( String uniformName )
    {
        int uniformLoc = glGetUniformLocation( resource.getProgram(), uniformName );

        if ( uniformLoc == -1 )
        {
            System.err.println( "Could not find uniform " + uniformName );
            System.exit( 1 );
        }

        resource.putUniformMap( uniformName, uniformLoc );
    }

    private static class GLSLStruct
    {
        public String name;
        public String type;

        @Override
        public String toString() { return type + " " + name; }
    }

    private Map<String, List<GLSLStruct>> findUniformStruct( String shaderText )
    {
        Map<String, List<GLSLStruct>> structMap = new HashMap<>();
        int startLocation = shaderText.indexOf( STRUCT_TAG );

        while ( startLocation != -1 )
        {
            int begin = startLocation + STRUCT_TAG_LENGTH + 1;
            int braceBegin = shaderText.indexOf( "{", begin );
            int braceEnd = shaderText.indexOf( "}", braceBegin );

            String structName = shaderText.trim().substring( begin, braceBegin ).trim();
            List<GLSLStruct> componentStructs = new ArrayList<>();

            int componentSemicolonPosition = shaderText.indexOf( ";", braceBegin );
            while ( componentSemicolonPosition != -1 && componentSemicolonPosition < braceEnd )
            {
                int componentNameStart = componentSemicolonPosition;
                while( !Character.isWhitespace( shaderText.charAt( componentNameStart ) ) )
                    componentNameStart--;

                // int componentTypeEnd = componentNameStart - 1;
                int componentTypeStart = componentNameStart - 1;

                while ( !Character.isWhitespace( shaderText.charAt( componentTypeStart - 1 ) ) )
                    componentTypeStart--;

                String componentName = shaderText.substring( componentNameStart, componentSemicolonPosition ).trim();
                String componentType = shaderText.substring( componentTypeStart, componentNameStart );

                GLSLStruct struct = new GLSLStruct();
                struct.name = componentName;
                struct.type = componentType;

                componentStructs.add( struct );

                componentSemicolonPosition = shaderText.indexOf( ";", componentSemicolonPosition + 1 );
            }



            structMap.put( structName, componentStructs );

            startLocation = shaderText.indexOf( STRUCT_TAG, startLocation + STRUCT_TAG_LENGTH );
        }

        return structMap;
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

    public void addVertexShader( String text )
    {
        addProgramShader( text, GL_VERTEX_SHADER );
    }

    public void addVertexShaderFromFile( String fileName ) {
        addVertexShader( new ResourceLoader().loadShader( fileName ) );
    }

    public void addGeometryShader( String text )
    {
        addProgramShader( text, GL_GEOMETRY_SHADER );
    }

    public void addFragmentShader( String text )
    {
        addProgramShader( text, GL_FRAGMENT_SHADER );
    }

    public void addFragmentShaderFromFile( String fileName ) {
        addFragmentShader( new ResourceLoader().loadShader( fileName ) );
    }

    public void setAttribLocation( String attributeName, int location )
    {
        glBindAttribLocation( resource.getProgram(), location, attributeName );
    }

    public void compileShader()
    {
        int program = resource.getProgram();

        glLinkProgram( program );

        if ( glGetProgrami( program, GL_LINK_STATUS ) == 0 )
        {
            System.err.println( glGetShaderInfoLog( program ) );
            System.exit( 1 );
        }

        glValidateProgram( program );

        if ( glGetProgrami( program, GL_VALIDATE_STATUS ) == 0 )
        {
            System.err.println( glGetShaderInfoLog( program ) );
            System.exit( 1 );
        }
    }

    private void addProgramShader( String text, int type )
    {
        int shader = glCreateShader( type );

        if ( shader == 0 )
        {
            System.err.println( "Could not find memory location when creating a shader" );
            System.exit( 1 );
        }

        glShaderSource( shader, text );
        // glBindAttribLocation ( program, 0, "vertexPosition_modelspace" );
        glCompileShader( shader );

        if ( glGetShaderi( shader, GL_COMPILE_STATUS ) == 0 )
        {
            System.err.println( glGetShaderInfoLog( shader ) );
            System.exit( 1 );
        }

        glAttachShader( resource.getProgram(), shader );
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

    public void bind()
    {
        glUseProgram( resource.getProgram() );
    }
}
