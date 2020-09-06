package peacefulotter.engine.rendering.GUI;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;

public class GUIRenderer extends GameComponent
{
    private final GUITexture texture;

    public GUIRenderer( GUITexture texture )
    {
        this.texture = texture;
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        // todo: draw the quad with the texture
    }
}
