package peacefulotter.game;

import peacefulotter.engine.core.Game;
import peacefulotter.engine.core.elementary.GameObject;
import peacefulotter.engine.rendering.Shaders.Transfomations.ShaderTransform;

public class FPSGame extends Game
{
    public FPSGame()
    {
        super( new GameObject( new ShaderTransform() ) );
    }

    @Override
    public void startEngine()
    {

    }
}
