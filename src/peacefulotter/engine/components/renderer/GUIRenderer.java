package peacefulotter.engine.components.renderer;

import peacefulotter.engine.core.maths.Matrix4f;
import peacefulotter.engine.rendering.GUI.GUIMaterial;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.RawModel;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.ShaderTypes;
import peacefulotter.engine.utils.ResourceLoader;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;


public class GUIRenderer extends Renderer
{
    private static final float[] POSITIONS = new float[] { -1, 1, -1, -1, 1, 1, 1, -1 };
    private static final Shader GUIShader = ShaderTypes.GUI.getShader();

    private final RawModel QUAD;
    private final List<GUIMaterial> guiMaterials = new ArrayList<>();

    public GUIRenderer()
    {
        QUAD = ResourceLoader.loadToVao( POSITIONS, 2 );
    }

    public void addGUIMaterial( GUIMaterial material )
    {
        material.setTransparency( true );
        guiMaterials.add( material );
    }

    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        shader.bind();

        glBindVertexArray( QUAD.getVaoId() );
        glEnableVertexAttribArray( 0 );

        guiMaterials.forEach( guiMaterial -> {
            if ( guiMaterial.hasTransparency() )
                RenderingEngine.disableCulling();

            shader.updateUniforms( guiMaterial.getTransform(), guiMaterial, renderingEngine );
            glDrawArrays( GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount() );

            if ( guiMaterial.hasTransparency() )
                RenderingEngine.enableCulling();
        } );

        glDisableVertexAttribArray( 0 );
        glBindVertexArray( 0 );


    }

    public void renderGUI( RenderingEngine renderingEngine )
    {
        render( GUIShader, renderingEngine );
    }
}
