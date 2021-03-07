package peacefulotter.engine.components.renderer;

import peacefulotter.engine.core.transfomations.STransform;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.RawModel;
import peacefulotter.engine.rendering.graphics.SimpleMaterial;
import peacefulotter.engine.rendering.graphics.Texture;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.ShaderTypes;
import peacefulotter.engine.utils.ResourceLoader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class SkyBoxRenderer
{
    public static final String SKYBOX_FOLDER = "skybox/night/";
    public static final String[] TEXTURE_FILES  = { "right", "left", "top", "bottom", "back", "front" };

    private static final Shader SKYBOX_SHADER = ShaderTypes.SKYBOX.getShader();
    private static final float SIZE = 1000f;
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
    private final STransform transform;

    public SkyBoxRenderer()
    {
        RawModel cube = ResourceLoader.loadToVao( VERTICES, 3 );
        Texture texture = new Texture( SKYBOX_FOLDER, "", Texture.TextureTypes.CUBE_MAP );
        Material material = new Material( new SimpleMaterial( texture, 1, 1 ) );
        skyBox = new SkyBox( cube, material );
        transform = new STransform();
    }

    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        shader.bind();
        shader.updateUniforms( transform, skyBox.material, renderingEngine, true );
        skyBox.draw();
    }

    public void renderSkyBox( RenderingEngine renderingEngine )
    {
        render( SKYBOX_SHADER, renderingEngine );
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
