package peacefulotter.game;

import peacefulotter.game.Utils.IO.KeyboardManager;

public class Game
{
    private final KeyboardManager keyboardManager = new KeyboardManager();

    public Game()
    {
    }

    public void inputs( long window )
    {
        keyboardManager.update( window );
    }

    public void update()
    {

    }

    public void render()
    {

    }
}
