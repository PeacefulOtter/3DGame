package peacefulotter.engine.rendering.Graphics;

import peacefulotter.engine.core.elementary.GameComponent;
import peacefulotter.engine.core.elementary.GameObject;
import peacefulotter.engine.rendering.Shaders.Shader;
import peacefulotter.engine.rendering.Shaders.Transfomations.ShaderTransform;

public class MeshRenderer implements GameComponent
{
    private final Mesh mesh;
    private final Material material;
    private final GameObject parent;

    public MeshRenderer( Mesh mesh, Material material, GameObject parent )
    {
        this.mesh = mesh;
        this.material = material;
        this.parent = parent;
    }

    @Override
    public void init() { }

    @Override
    public void update( float deltaTime ) { }

    @Override
    public void render( Shader shader )
    {
        ShaderTransform transform = parent.getTransform();
        shader.bind();
        shader.updateUniforms( transform.getTransformationMatrix(), transform.getProjectedTransformationMatrix(), material );
        mesh.draw();
    }
}
