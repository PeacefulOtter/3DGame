package peacefulotter.engine.components;

import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.Mesh;
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
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        shader.bind();
        shader.updateUniforms( getTransform(), material, renderingEngine );
        mesh.draw();
    }
}
