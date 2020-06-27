package peacefulotter.engine.rendering.Shaders;

import peacefulotter.engine.rendering.Camera;
import peacefulotter.engine.rendering.Graphics.Material;
import peacefulotter.engine.core.Maths.Matrix4f;
import peacefulotter.engine.core.Maths.Vector3f;
import peacefulotter.engine.rendering.BufferUtil;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.Shaders.Transfomations.ShaderTransform;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

abstract public class Shader
{
    private RenderingEngine renderingEngine;
    private int program;
    private Map<String, Integer> uniforms;

    public Shader()
    {
        program = glCreateProgram();
        if ( program == 0 )
        {
            System.err.println( "Could not find memory location for shader in constructor" );
            System.exit( 1 );
        }

        uniforms = new HashMap<>();
    }

    public void updateUniforms( ShaderTransform transform, Material material ) {}

    public void addUniform( String uniformName )
    {
        int uniformLoc = glGetUniformLocation( program, uniformName );

        if ( uniformLoc == -1 )
        {
            System.err.println( "Could not find uniform " + uniformName );
            System.exit( 1 );
        }

        uniforms.put( uniformName, uniformLoc );
    }

    public void setUniformI( String uniformName, int value )
    {
        glUniform1i( uniforms.get( uniformName ), value );
    }

    public void setUniformF( String uniformName, float value )
    {
        glUniform1f( uniforms.get( uniformName ), value );
    }

    public void setUniformVector( String uniformName, Vector3f value )
    {
        glUniform3f( uniforms.get( uniformName ), value.getX(), value.getY(), value.getZ() );
    }

    public void setUniformMatrix( String uniformName, Matrix4f value )
    {
        glUniformMatrix4fv( uniforms.get( uniformName ), true, BufferUtil.createFlippedBuffer( value ) );
    }

    public void addVertexShader( String text )
    {
        addProgramShader( text, GL_VERTEX_SHADER );
    }

    public void addGeometryShader( String text )
    {
        addProgramShader( text, GL_GEOMETRY_SHADER );
    }

    public void addFragmentShader( String text )
    {
        addProgramShader( text, GL_FRAGMENT_SHADER );
    }

    public void compileShader()
    {
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

        glAttachShader( program, shader );
    }

    public void bind()
    {
        glUseProgram( program );
    }


    public RenderingEngine getRenderingEngine()
    {
        return renderingEngine;
    }

    public void setRenderingEngine( RenderingEngine renderingEngine )
    {
        this.renderingEngine = renderingEngine;
    }
}
