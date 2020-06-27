package peacefulotter.engine.core;


import org.lwjgl.opengl.GL;
import peacefulotter.engine.rendering.BufferUtil;
import peacefulotter.engine.Utils.Time;
import peacefulotter.engine.rendering.RenderingEngine;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class CoreEngine
{
    private final Game game;
    private final long currentWindow;
    private final RenderingEngine renderingEngine;

    private static final double FRAMES_CAP = 500;
    private static final double FRAME_TIME = 1.0 / FRAMES_CAP;

    private boolean isRunning = false;

    public CoreEngine( Game game, long currentWindow )
    {
        this.game = game;
        this.currentWindow = currentWindow;
        this.renderingEngine = new RenderingEngine();
    }

    public void start()
    {
        if ( isRunning ) return;
        game.init();
        isRunning = true;
        run();
    }

    public void stop()
    {
        if ( !isRunning ) return;

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks( currentWindow );
        glfwDestroyWindow( currentWindow );

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull( glfwSetErrorCallback( null ) ).free();

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
                game.update( passedTime );
            }

            if ( render )
            {
                renderingEngine.render( game.getRootObject() );
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
}
