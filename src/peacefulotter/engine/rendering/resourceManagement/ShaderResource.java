package peacefulotter.engine.rendering.resourceManagement;

import peacefulotter.engine.elementary.Disposable;
import peacefulotter.engine.utils.Logger;
import peacefulotter.engine.utils.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class ShaderResource extends Disposable
{
    private static final String SHADER_PATH = "/shaders/";
    private static final String INCLUDE_DIRECTIVE = "#include";
    private static final int INCLUDE_DIRECTIVE_LENGTH = INCLUDE_DIRECTIVE.length();

    private static final String ATTRIBUTE_TAG = "in vec";
    private static final int ATTRIBUTE_TAG_LENGTH = ATTRIBUTE_TAG.length();
    private static final String UNIFORM_TAG = "uniform";
    private static final int UNIFORM_TAG_LENGTH = UNIFORM_TAG.length();
    private static final String STRUCT_TAG = "struct";
    private static final int STRUCT_TAG_LENGTH = STRUCT_TAG.length();

    private final int program;

    private final Map<String, Integer> uniformsMap;
    private final List<GLSLStruct> uniformsStruct;

    public ShaderResource( String shaderName )
    {
        this.program = glCreateProgram();

        if ( program == 0 )
        {
            System.err.println( "Could not find memory location for shader in constructor" );
            System.exit( 1 );
        }

        uniformsMap = new HashMap<>();
        uniformsStruct = new ArrayList<>();

        Logger.log( getClass(), "========= Loading..  " + shaderName );

        String vertexShaderText = loadShader( shaderName + ".vs" );
        String fragmentShaderText = loadShader( shaderName + ".fs" );

        addVertexShader( vertexShaderText );
        addFragmentShader( fragmentShaderText );

        addAllAttributes( vertexShaderText );

        compileShader();

        Logger.log( getClass(), " == Now Adding all uniforms for the VERTEX shader." );
        addAllUniforms( vertexShaderText );
        Logger.log( getClass(), " == Now Adding all uniforms for the FRAGMENT shader." );
        addAllUniforms( fragmentShaderText );
    }

    private String loadShader( String fileName )
    {
        Logger.log( getClass(), "Loading shader at : " + fileName );
        StringJoiner sj = new StringJoiner( "\n" );

        try ( BufferedReader reader = new BufferedReader(
                new InputStreamReader( new ResourceLoader().resourceStream( SHADER_PATH + fileName ) ) ) )
        {
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                if ( line.startsWith( INCLUDE_DIRECTIVE ) )
                    sj.add( loadShader( line.substring( INCLUDE_DIRECTIVE_LENGTH + 2, line.length() - 2 ) ) );
                else if ( !line.startsWith( "//" ) )
                    sj.add( line );
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            System.exit( 1 );
        }
        return sj.toString();
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

    private String getPropertyName( String line )
    {
        return line.substring( line.indexOf( " " ) + 1 ).trim();
    }

    private String getUniformType( String line )
    {
        return line.substring( 0, line.indexOf( " " ) ).trim();
    }

    private void addAllAttributes( String shaderText )
    {
        List<String> attributesLine = allAllProperty( shaderText, ATTRIBUTE_TAG, ATTRIBUTE_TAG_LENGTH );
        Logger.log( getClass(), "Attributes : " + attributesLine );
        int i = 0;
        for ( String attributeLine : attributesLine )
            setAttribLocation( getPropertyName( attributeLine ), i++ );
    }

    private void addAllUniforms( String shaderText )
    {
        List<String> uniformsLine = allAllProperty( shaderText, UNIFORM_TAG, UNIFORM_TAG_LENGTH );
        Logger.log( getClass(), "Uniforms : " + uniformsLine);
        for ( String uniformLine : uniformsLine )
        {
            String uniformName = getPropertyName( uniformLine );
            String uniformType = getUniformType( uniformLine );
            uniformsStruct.add( new GLSLStruct( uniformName, uniformType ) );
            addUniform( uniformName, uniformType, findUniformStruct( shaderText ) );
        }
    }

    private void addUniform( String uniformName, String uniformType, Map<String, List<GLSLStruct>> structs )
    {
        Logger.log( getClass(), "Trying to add uniform : " + uniformName);
        boolean addThis = true;
        List<GLSLStruct> structComponents = structs.get( uniformType );

        if ( structComponents != null )
        {
            addThis = false;
            for ( GLSLStruct structComponent: structComponents )
            {
                addUniform(
                        uniformName + "." + structComponent.name,
                        structComponent.type,
                        structs );
            }
        }

        if ( addThis )
        {
            Logger.log( getClass(), "Now adding " + uniformName );
            int uniformLoc = glGetUniformLocation( program, uniformName );

            if ( uniformLoc == -1 )
                throw new IllegalArgumentException( "Could not find uniform " + uniformName );

            uniformsMap.put( uniformName, uniformLoc );
            Logger.log( getClass(), "Added " + uniformName + " at " + uniformLoc );
        }
    }

    private Map<String, List<GLSLStruct>> findUniformStruct( String shaderText )
    {
        Map<String, List<GLSLStruct>> structMap = new HashMap<>();
        int startLocation = shaderText.indexOf( STRUCT_TAG );

        while ( startLocation != -1 )
        {
            int begin = startLocation + STRUCT_TAG_LENGTH;
            int braceBegin = shaderText.indexOf( "{", begin ) - 1;
            int braceEnd = shaderText.indexOf( "}", braceBegin );

            String structName = shaderText.trim().substring( begin, braceBegin ).trim();
            Logger.log( getClass(), "StructName : " + structName);
            List<GLSLStruct> componentStructs = new ArrayList<>();

            int componentSemicolonPosition = shaderText.indexOf( ";", braceBegin );
            while ( componentSemicolonPosition != -1 && componentSemicolonPosition < braceEnd )
            {
                int componentNameStart = componentSemicolonPosition;
                while( !Character.isWhitespace( shaderText.charAt( componentNameStart ) ) )
                    componentNameStart--;

                int componentTypeStart = componentNameStart - 1;

                while ( !Character.isWhitespace( shaderText.charAt( componentTypeStart - 1 ) ) )
                    componentTypeStart--;

                String componentName = shaderText.substring( componentNameStart, componentSemicolonPosition ).trim();
                String componentType = shaderText.substring( componentTypeStart, componentNameStart );

                GLSLStruct struct = new GLSLStruct( componentName, componentType );
                componentStructs.add( struct );

                componentSemicolonPosition = shaderText.indexOf( ";", componentSemicolonPosition + 1 );
            }

            structMap.put( structName, componentStructs );

            startLocation = shaderText.indexOf( STRUCT_TAG, startLocation + STRUCT_TAG_LENGTH );
        }

        return structMap;
    }

    private void addVertexShader( String text )
    {
        addProgramShader( text, GL_VERTEX_SHADER );
    }

    private void addGeometryShader( String text )
    {
        addProgramShader( text, GL_GEOMETRY_SHADER );
    }

    private void addFragmentShader( String text )
    {
        addProgramShader( text, GL_FRAGMENT_SHADER );
    }

    private void setAttribLocation( String attributeName, int location )
    {
        Logger.log( getClass(), "setAttribLocation : " + attributeName + " at " + location );
        glBindAttribLocation( program, location, attributeName );
    }

    private void compileShader()
    {
        glLinkProgram( program );

        if ( glGetProgrami( program, GL_LINK_STATUS ) == 0 )
            throw new IllegalArgumentException( glGetProgramInfoLog( program, 1024 ) );

        glValidateProgram( program );

        if ( glGetProgrami( program, GL_VALIDATE_STATUS ) == 0 )
            throw new IllegalArgumentException( glGetProgramInfoLog( program, 1024 ) );
    }

    private void addProgramShader( String text, int type )
    {
        int shader = glCreateShader( type );

        if ( shader == 0 )
            throw new IllegalArgumentException( "Could not find memory location when creating a shader" );

        glShaderSource( shader, text );
        glCompileShader( shader );

        if ( glGetShaderi( shader, GL_COMPILE_STATUS ) == 0 )
            throw new IllegalArgumentException( glGetShaderInfoLog( shader ) );

        glAttachShader( program, shader );
    }

    public void dispose()
    {
        System.out.println("SHADER resource dispose");
        // glDeleteBuffers( program );
    }


    public int getProgram()  { return program;  }
    public Map<String, Integer> getUniformsMap() { return uniformsMap; }
    public List<GLSLStruct> getUniformsStruct() { return uniformsStruct; }

    public static class GLSLStruct
    {
        public String name;
        public String type;

        public GLSLStruct( String name, String type )
        {
            this.name = name;
            this.type = type;
        }

        @Override
        public String toString() { return type + " " + name; }
    }
}
