package peacefulotter.engine.components.renderer;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.MultiTextureMesh;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.utils.ResourceLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MultiMeshRenderer extends GameComponent
{
    private static final Map<String, MultiTextureMesh> mtms = new HashMap<>();
    private final MultiTextureMesh mtm;
    private final int size;

    public MultiMeshRenderer( String subFolder, String fileName )
    {
        String path = subFolder + fileName;
        if ( mtms.containsKey( path ) )
            this.mtm = mtms.get( path );
        else
        {
            this.mtm = new ResourceLoader().loadMultiMesh( subFolder, fileName );
            mtms.put( path, mtm );
        }
        this.size = mtm.getSize();
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        shader.bind();
        shader.updateUniforms( getTransform(), mtm.getMaterials().get( 0 ), renderingEngine );
        mtm.getMeshes().get( 0 ).draw();

        for ( int i = 1; i < size; i++ )
        {
            shader.forceUpdateTexture( mtm.getMaterials().get( i ), renderingEngine );
            mtm.getMeshes().get( i ).draw();
        }
    }
}
