package peacefulotter;

import peacefulotter.game.FPSGame;
import peacefulotter.game.TestGame;

public class Main
{
    private static final String WINDOW_TITLE = "My Game";
    private static final int WINDOW_WIDTH  = 1280;
    private static final int WINDOW_HEIGHT = 720;

    public static void main( String[] args )
    {
        // System.out.println(glGetString(GL_VERSION));
        new FPSGame( WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT );
        // Runtime.getRuntime().addShutdownHook( new Disposable().createDisposeThread() );
    }

}
