package peacefulotter.engine.rendering.GUI;

import peacefulotter.engine.components.renderer.Renderer;
import peacefulotter.engine.rendering.RenderingEngine;
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

    private final RawModel QUAD = ResourceLoader.loadToVao( POSITIONS, 2 );
    private final List<GUITexture> guiTextures = new ArrayList<>();

    public void addGUITexture( GUITexture texture )
    {
        guiTextures.add( texture );
    }

    @Override
    public void render( RenderingEngine renderingEngine )
    {
        GUIShader.bind();

        glBindVertexArray( QUAD.getVaoId() );
        glEnableVertexAttribArray( 0 );

        guiTextures.forEach( guiTexture -> {
            // guiTexture.bind( 0 );
            // GUIShader.updateUniforms( guiTexture.getTransform(), new Material( guiTexture, 1, 1, 0, 0 ), renderingEngine );
            glDrawArrays( GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount() );
        } );

        glDisableVertexAttribArray( 0 );
        glBindVertexArray( 0 );
    }
}
