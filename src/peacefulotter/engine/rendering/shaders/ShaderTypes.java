package peacefulotter.engine.rendering.shaders;

import peacefulotter.engine.rendering.resourceManagement.ShaderResource;


public enum ShaderTypes
{
    AMBIENT( "forward-ambient" ),
    DIRECTIONAL( "forward-directional" ),
    POINT( "forward-point" ),
    SPOT( "forward-spot" ),
    TERRAIN( "terrain" ),
    GUI( "gui" );

    private final Shader shader;

    ShaderTypes( String shaderName )
    {
        ShaderResource resource = new ShaderResource( shaderName );
        this.shader = new Shader( resource );
    }

    public Shader getShader() { return shader; }
}
