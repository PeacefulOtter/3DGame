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

    // [X] renderer should be in a gameobject
    // todo: guis
    // TODO: javafx custom + 3D
    // todo: optimize transform class

    // gui.vs: uniform mat4 T_viewMatrix; T_viewMatrix * T_tr..

}
