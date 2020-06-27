package peacefulotter.engine.core.elementary;

public interface GameComponent
{
    void init();
    void update( float deltaTime );
    void render();
}
