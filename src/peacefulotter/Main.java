package peacefulotter;

import peacefulotter.game.FPSGame;
import peacefulotter.game.TestGame;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

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

}
