package peacefulotter.engine.rendering.resourceManagement;

import peacefulotter.engine.elementary.Disposable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.glCreateProgram;

public class ShaderResource extends Disposable
{
    private final int program;
    private final Map<String, Integer> uniformsMap;
    private final List<String> attributes;
    private final List<String> uniforms;

    public ShaderResource()
    {
        this.program = glCreateProgram();

        if ( program == 0 )
        {
            System.err.println( "Could not find memory location for shader in constructor" );
            System.exit( 1 );
        }

        uniformsMap = new HashMap<>();
        attributes = new ArrayList<>();
        uniforms = new ArrayList<>();
    }

    public void dispose()
    {
        System.out.println("SHADER resource dispose");
        // glDeleteBuffers( program );
    }

    public void addAttributes( String attributeName )
    {
        attributes.add( attributeName );
    }

    public void addUniform( String uniformName )
    {
        uniforms.add( uniformName );
    }

    public void putUniformMap( String uniformName, Integer uniformLoc )
    {
        uniformsMap.put( uniformName, uniformLoc );
    }

    public int getProgram()  { return program;  }
    public Map<String, Integer> getUniformsMap() { return uniformsMap; }
    public List<String> getAttributes() { return attributes; }
    public List<String> getUniforms() { return uniforms; }
}
