package peacefulotter.engine.core.elementary;

import peacefulotter.engine.rendering.Shaders.Shader;

public interface GameComponent
{
    void init();
    void update( float deltaTime );
    void render( Shader shader );
}
