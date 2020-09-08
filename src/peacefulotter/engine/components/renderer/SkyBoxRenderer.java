package peacefulotter.engine.components.renderer;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.core.transfomations.STransform;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.RawModel;
import peacefulotter.engine.rendering.graphics.SimpleMaterial;
import peacefulotter.engine.rendering.graphics.Texture;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.ShaderTypes;
import peacefulotter.engine.utils.ResourceLoader;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class SkyBoxRenderer extends Renderer
{
    public static final String[] TEXTURE_FILES  = { "right", "left", "top", "bottom", "back", "front" };

    private static final Shader SKYBOX_SHADER = ShaderTypes.SKYBOX.getShader();
    private static final float SIZE = 500f;
    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };

    private final SkyBox skyBox;

    public SkyBoxRenderer()
    {
        RawModel cube = ResourceLoader.loadToVao( VERTICES, 3 );
        Texture texture = new Texture( "skybox/", "", Texture.TextureTypes.CUBE_MAP );
        Material material = new Material( new SimpleMaterial( texture, 1, 1 ) );
        material.addTexture( "cubeMap", texture );
        skyBox = new SkyBox( cube, material );
    }

    public void renderSkyBox( RenderingEngine renderingEngine )
    {
        // glDepthMask( false );
        // glDepthRange( 1f, 1f );

        SKYBOX_SHADER.bind();
        SKYBOX_SHADER.updateUniforms( new STransform(), skyBox.material, renderingEngine );
        skyBox.draw();

        // glDepthRange( 0f, 1f );
        // glDepthMask( true );
    }

    private static class SkyBox
    {
        private final RawModel model;
        private final Material material;

        public SkyBox( RawModel model, Material material )
        {
            this.model = model;
            this.material = material;
        }

        public void draw()
        {
            glBindVertexArray( model.getVaoId() );
            glEnableVertexAttribArray( 0 );

            glDrawArrays( GL_TRIANGLES, 0, model.getVertexCount() );

            glDisableVertexAttribArray( 0 );
            glBindVertexArray( 0 );
        }
    }
}
