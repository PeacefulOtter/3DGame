package peacefulotter.engine.components;

import peacefulotter.engine.rendering.terrain.Terrain;

abstract public class World extends GameObject
{
    abstract protected void generateDecoration( Terrain terrain );
    abstract public Terrain getTerrain();
}
