package peacefulotter.engine.core.elementary;

import peacefulotter.engine.rendering.Shaders.Shader;

public interface GameComponent
{
    void init();
    void update();
    void render( Shader shader );
}
