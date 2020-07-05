package peacefulotter.engine.elementary;


import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;

public interface Renderable
{
    void render( Shader shader, RenderingEngine renderingEngine );
}
