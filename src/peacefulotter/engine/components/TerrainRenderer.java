package peacefulotter.engine.components;

import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;
import peacefulotter.engine.rendering.terrain.Terrain;

import java.util.ArrayList;
import java.util.List;

public class TerrainRenderer extends GameComponent
{
    private final List<Terrain> terrains = new ArrayList<>();

    public void addTerrain( Terrain terrain )
    {
        terrains.add( terrain );
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        STransform transform = getTransform();
        terrains.forEach( terrain -> terrain.render( shader, renderingEngine, transform ) );
    }
}
