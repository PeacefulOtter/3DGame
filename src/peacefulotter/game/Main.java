package peacefulotter.game;

import peacefulotter.game.Display.Window;

public class Main
{
    private static final String WINDOW_TITLE = "My Game";
    private static final int WINDOW_WIDTH  = 1280;
    private static final int WINDOW_HEIGHT = 720;

    public static void main( String[] args )
    {
        Window window = new Window( WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT );
        GameLogic logic = new GameLogic( window );
        logic.start();
    }

}
