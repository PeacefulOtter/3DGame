package peacefulotter.engine.components.renderer;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.components.GameObject;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.Texture;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.core.transfomations.STransform;
import peacefulotter.engine.rendering.shaders.ShaderTypes;
import peacefulotter.engine.rendering.terrain.Terrain;


public class TerrainRenderer extends GameObject
{
    private static final String TERRAIN_SUBFOLDER = "terrain/";
    private static final Shader terrainShader = ShaderTypes.TERRAIN.getShader();

    private final Material material;
    private Terrain actualTerrain; // TO REFACTOR

    public TerrainRenderer()
    {
        material = new Material(
                new Texture( TERRAIN_SUBFOLDER, "grass.png" ), Texture.getDefaultNormal(), Texture.getDefaultHeight(),
                0.9f, 1.3f, 0.02f, 0f );
        material.addTexture( "aTexture", new Texture( TERRAIN_SUBFOLDER, "grass.png" ) );
        material.addTexture( "rTexture", new Texture( TERRAIN_SUBFOLDER, "mud.png" ) );
        material.addTexture( "gTexture", new Texture( TERRAIN_SUBFOLDER, "flowers.png" ) );
        material.addTexture( "bTexture", new Texture( TERRAIN_SUBFOLDER, "path.png" ) );
        material.addTexture( "blendMap", new Texture( TERRAIN_SUBFOLDER, "blend_map.png" ) );
    }

    public GameObject addComponent( Terrain terrain )
    {
        super.addComponent( terrain );
        terrain.setMaterial( material );
        actualTerrain = terrain; // to refactor
        return this;
    }

    public void renderTerrain( RenderingEngine renderingEngine )
    {
        renderAll( terrainShader, renderingEngine );
    }

    // TO REFACTOR
    public Terrain getTerrain()
    {
        return actualTerrain;
    }
}
