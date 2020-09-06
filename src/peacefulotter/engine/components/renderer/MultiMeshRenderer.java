package peacefulotter.engine.components.renderer;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.MultiTextureMesh;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.utils.ResourceLoader;


public class MultiMeshRenderer extends GameComponent
{
    private final MultiTextureMesh mtm;
    private final int size;

    public MultiMeshRenderer( String subFolder, String fileName )
    {
        this.mtm = new ResourceLoader().loadMultiMesh( subFolder, fileName );
        this.size = mtm.getSize();
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        shader.bind();
        shader.updateUniforms( getTransform(), mtm.getMaterials().get( 0 ), renderingEngine );
        for ( int i = 0; i < size; i++ )
        {
            shader.forceUpdateTexture( mtm.getMaterials().get( i ), renderingEngine );
            mtm.getMeshes().get( i ).draw();
        }
    }
}
