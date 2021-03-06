package peacefulotter;

import peacefulotter.game.FPSGame;

public class Main
{
    private static final String WINDOW_TITLE = "My Game";
    private static final int WINDOW_WIDTH  = 1280;
    private static final int WINDOW_HEIGHT = 720;

    public static void main( String[] args )
    {
        new FPSGame( WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT );
        // Runtime.getRuntime().addShutdownHook( new Disposable().createDisposeThread() );
    }

    // renderer should be in a gameobject
    // todo: guis
    // TODO: javafx custom + 3D
    // todo: optimize transform class

}
