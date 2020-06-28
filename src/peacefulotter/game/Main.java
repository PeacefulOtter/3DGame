package peacefulotter.game;

import peacefulotter.engine.core.Game;

public class Main
{
    private static final String WINDOW_TITLE = "My Game";
    private static final int WINDOW_WIDTH  = 1280;
    private static final int WINDOW_HEIGHT = 720;

    public static void main( String[] args )
    {
        // System.out.println(glGetString(GL_VERSION));
        Game game = new TestGame( WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT );
    }

}
