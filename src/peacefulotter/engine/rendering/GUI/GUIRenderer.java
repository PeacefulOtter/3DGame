package peacefulotter.engine.rendering.GUI;

import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.ShaderTypes;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;


public class GUIRenderer
{
    private static final Shader GUIShader = ShaderTypes.GUI.getShader();

    private final QuadMesh QUAD = new QuadMesh();
    private final List<GUITexture> guiTextures = new ArrayList<>();

    public void addGUITexture( GUITexture texture )
    {
        guiTextures.add( texture );
    }

    public void renderGUI( RenderingEngine renderingEngine )
    {
        GUIShader.bind();

        glBindVertexArray( QUAD.getVaoId() );
        glEnableVertexAttribArray( 0 );

        guiTextures.forEach( guiTexture -> {
            // guiTexture.bind( 0 );
            // GUIShader.updateUniforms( guiTexture.getTransform(), new Material( guiTexture, 1, 1, 0, 0 ), renderingEngine );
            glDrawArrays( GL_TRIANGLE_STRIP, 0, 4 );
        } );

        glDisableVertexAttribArray( 0 );
        glBindVertexArray( 0 );
    }

    public void setRenderingEngine( RenderingEngine renderingEngine )
    {
        renderingEngine.setGUIRenderer( this );
    }
}
