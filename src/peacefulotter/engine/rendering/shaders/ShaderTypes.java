package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.rendering.resourceManagement.ShaderResource;


public enum ShaderTypes
{
    AMBIENT( "forward-ambient" ),
    DIRECTIONAL( "forward-directional" ),
    POINT( "forward-point" ),
    SPOT( "forward-spot" );

    private final ShaderResource resource;
    private final Shader shader;

    ShaderTypes( String shaderName )
    {
        this.resource = new ShaderResource( shaderName );
        this.shader = new Shader( resource );
    }

    public Shader getShader() { return shader; }
}