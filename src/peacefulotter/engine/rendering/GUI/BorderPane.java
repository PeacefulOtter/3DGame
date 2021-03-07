package peacefulotter.engine.rendering.GUI;

import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.shaders.Shader;

import java.util.List;

public class BorderPane extends GUI
{
    private List<GUI> guis;

    public void addGUI(GUI gui)
    {
        guis.add( gui );
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        for ( GUI gui: guis )
            gui.render( shader, renderingEngine );
    }
}
