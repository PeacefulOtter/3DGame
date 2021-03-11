package peacefulotter.engine.components.renderer;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.RawModel;
import peacefulotter.engine.rendering.graphics.SimpleMaterial;
import peacefulotter.engine.rendering.graphics.Texture;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.ShaderTypes;
import peacefulotter.engine.rendering.terrain.WaterTile;
import peacefulotter.engine.utils.ResourceLoader;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class WaterRenderer extends GameComponent
{
    private static final float[] POSITIONS = new float[] { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
    private static final Shader WATER_SHADER = ShaderTypes.WATER.getShader();

    private final RawModel waterQuad;
    private final Material waterMaterial;
    private final List<WaterTile> waterTiles;

    public WaterRenderer()
    {
        waterQuad = ResourceLoader.loadToVao( POSITIONS, 2 );
        waterMaterial = new Material( new SimpleMaterial( new Texture( "terrain/water.jpg" ), 1, 1 ));
        waterTiles = new ArrayList<>();
    }

    public void addWaterTile( WaterTile tile )
    {
        waterTiles.add( tile );
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        shader.bind();

        glBindVertexArray( waterQuad.getVaoId() );
        glEnableVertexAttribArray( 0 );

        waterTiles.forEach( tile -> {
            shader.updateUniforms( tile.getTransform(), waterMaterial, renderingEngine );
            glDrawArrays( GL_TRIANGLES, 0, waterQuad.getVertexCount() );
        } );

        glDisableVertexAttribArray( 0 );
        glBindVertexArray( 0 );

    }

    public void renderWater( RenderingEngine renderingEngine )
    {
        render( WATER_SHADER, renderingEngine );
    }
}
