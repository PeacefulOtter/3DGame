package peacefulotter.engine.components.renderer;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.Texture;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.core.transfomations.STransform;
import peacefulotter.engine.rendering.shaders.ShaderTypes;
import peacefulotter.engine.rendering.terrain.Terrain;

import java.util.ArrayList;
import java.util.List;

public class TerrainRenderer extends GameComponent
{
    private static final String TERRAIN_SUBFOLDER = "terrain/";

    private final List<Terrain> terrains = new ArrayList<>();
    private final Shader terrainShader = ShaderTypes.TERRAIN.getShader();
    private final Material material;

    public TerrainRenderer()
    {
        material = new Material(
                new Texture( TERRAIN_SUBFOLDER, "grass.png" ), Texture.getDefaultNormal(), Texture.getDefaultHeight(),
                1f, 4, 0.03f, -0.04f );
        material.addTexture( "aTexture", new Texture( TERRAIN_SUBFOLDER, "grass.png" ) );
        material.addTexture( "rTexture", new Texture( TERRAIN_SUBFOLDER, "mud.png" ) );
        material.addTexture( "gTexture", new Texture( TERRAIN_SUBFOLDER, "flowers.png" ) );
        material.addTexture( "bTexture", new Texture( TERRAIN_SUBFOLDER, "path.png" ) );
        material.addTexture( "blendMap", new Texture( TERRAIN_SUBFOLDER, "blend_map.png" ) );
    }

    public void addTerrain( Terrain terrain )
    {
        terrains.add( terrain );
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        STransform transform = getTransform();
        terrains.forEach( terrain -> terrain.render( shader, renderingEngine, transform, material ) );
    }

    public void renderTerrain( RenderingEngine renderingEngine )
    {
        STransform transform = getTransform();
        terrains.forEach( terrain -> terrain.render( terrainShader, renderingEngine, transform, material ) );
    }

    @Override
    public void addToEngine( CoreEngine engine )
    {
        engine.getRenderingEngine().addTerrainRenderer( this );
    }
}
