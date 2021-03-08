package peacefulotter.engine.components.renderer;

import peacefulotter.engine.components.GameObject;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.SimpleMaterial;
import peacefulotter.engine.rendering.graphics.Texture;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.ShaderTypes;
import peacefulotter.engine.rendering.terrain.Terrain;


public class TerrainRenderer extends GameObject
{
    private static final String TERRAIN_SUBFOLDER = "terrain/";
    private static final Shader terrainShader = ShaderTypes.TERRAIN.getShader();

    private final Material material;

    public TerrainRenderer()
    {
        Texture grass = new Texture( TERRAIN_SUBFOLDER, "grass.png" );
        Texture normalGrass = new Texture( TERRAIN_SUBFOLDER, "grass_normal.png" );
        Texture heightGrass = new Texture( TERRAIN_SUBFOLDER, "grass_height.png" );

        material = new Material( new SimpleMaterial( grass, 1, 1 ) );
        material.addTexture( "aTexture", new Texture( TERRAIN_SUBFOLDER, "grass.png" ) );
        material.addTexture( "rTexture", new Texture( TERRAIN_SUBFOLDER, "mud.png" ) );
        material.addTexture( "gTexture", new Texture( TERRAIN_SUBFOLDER, "flowers.png" ) );
        material.addTexture( "bTexture", new Texture( TERRAIN_SUBFOLDER, "path.png" ) );

        material.addTexture( "aNormalMap", new Texture( TERRAIN_SUBFOLDER, "grass_normal.png" ) );
        material.addTexture( "rNormalMap", new Texture( TERRAIN_SUBFOLDER, "mud_normal.png" ) );
        material.addTexture( "gNormalMap", new Texture( TERRAIN_SUBFOLDER, "flowers_normal.png" ) );
        material.addTexture( "bNormalMap", new Texture( TERRAIN_SUBFOLDER, "path_normal.png" ) );

        material.addTexture( "aDispMap", new Texture( TERRAIN_SUBFOLDER, "grass_height.png" ) );
        material.addTexture( "rDispMap", new Texture( TERRAIN_SUBFOLDER, "mud_height.png" ) );
        material.addTexture( "gDispMap", new Texture( TERRAIN_SUBFOLDER, "flowers_height.png" ) );
        material.addTexture( "bDispMap", new Texture( TERRAIN_SUBFOLDER, "path_height.png" ) );

        material.addTexture( "blendMap", new Texture( TERRAIN_SUBFOLDER, "blend_map.png" ) );
    }

    public GameObject addComponent( Terrain terrain )
    {
        super.addComponent( terrain );
        terrain.setMaterial( material );
        return this;
    }

    public void renderTerrain( RenderingEngine renderingEngine )
    {
        renderAll( terrainShader, renderingEngine );
    }
}
