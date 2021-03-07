package peacefulotter;

import peacefulotter.game.FPSGame;

public class Main
{
    private static final String WINDOW_TITLE = "My Game";
    private static final int WINDOW_WIDTH  = 1920;
    private static final int WINDOW_HEIGHT = 1080;

    public static void main( String[] args )
    {
        Runtime.getRuntime().addShutdownHook(new Thread( () -> System.out.println("shutdown hook") ) );
        new FPSGame( WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT );
    }
}
