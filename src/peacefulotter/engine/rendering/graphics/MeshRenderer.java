package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.rendering.shaders.Shader;

public class MeshRenderer extends GameComponent
{
    private final Mesh mesh;
    private final Material material;

    public MeshRenderer( Mesh mesh, Material material )
    {
        this.mesh = mesh;
        this.material = material;
    }

    @Override
    public void init() { }

    @Override
    public void update( float delta ) { }

    @Override
    public void render( Shader shader )
    {
        shader.bind();
        shader.updateUniforms( getTransform(), material );
        mesh.draw();
    }
}
