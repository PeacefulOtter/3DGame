package peacefulotter.game;


import org.lwjgl.opengl.GL;
import peacefulotter.game.Display.Window;
import peacefulotter.game.Utils.RenderUtil;
import peacefulotter.game.Utils.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class GameLogic
{
    private final Game game;
    private final Window window;
    private final long currentWindow;

    private static final double FRAMES_CAP = 500;
    private static final double FRAME_TIME = 1.0 / FRAMES_CAP;

    private boolean isRunning = false;

    public GameLogic( String winName, int winWidth, int winHeight )
    {
        this.window = new Window( winName, winWidth, winHeight );
        RenderUtil.initGraphics();
        // System.out.println(glGetString(GL_VERSION));
        currentWindow = window.getWindow();
        game = new Game( window );
    }

    public void start()
    {
        if ( isRunning ) return;
        isRunning = true;
        run();
    }

    public void stop()
    {
        if ( !isRunning ) return;

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(currentWindow);
        glfwDestroyWindow(currentWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        isRunning = false;
    }

    private void run()
    {
        int frames = 0;
        int framesCounter = 0;

        long lastTime = Time.getNanoTime();
        double relativeTime = 0; // time in seconds since the start

        while ( isRunning )
        {
            if ( glfwWindowShouldClose( currentWindow ) )
            {
                stop();
                return;
            }
            GL.createCapabilities();
            glfwSwapBuffers(currentWindow);
            glfwPollEvents();

            long passedTime = (long) Time.getDeltaNano( lastTime ); // delta between two updates
            lastTime = Time.getNanoTime();
            relativeTime += passedTime / (double) Time.SECOND;

            framesCounter += passedTime;
            if ( framesCounter >= Time.SECOND )
            {
                System.out.println(frames);
                frames = 0;
                framesCounter = 0;
            }

            boolean render = false;
            while( relativeTime > FRAME_TIME )
            {
                relativeTime -= FRAME_TIME;
                render = true;

                game.update();
            }

            if ( render )
            {
                render();
                frames++;
            }
            else
            {
                try
                {
                    Thread.sleep( 1 );
                } catch ( InterruptedException e )
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void render()
    {
        RenderUtil.clearScreen();
        game.render();
    }
}
