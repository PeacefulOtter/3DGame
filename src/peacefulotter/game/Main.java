package peacefulotter.game;

import peacefulotter.game.Display.Window;

public class Main
{
    private static final String WINDOW_TITLE = "My Game";
    private static final int WINDOW_WIDTH  = 1280;
    private static final int WINDOW_HEIGHT = 720;

    public static void main( String[] args )
    {
        GameLogic logic = new GameLogic( WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT );
        logic.start();
    }

}
