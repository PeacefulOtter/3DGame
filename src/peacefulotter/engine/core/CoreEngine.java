package peacefulotter.engine.core;


import org.lwjgl.opengl.GL;
import peacefulotter.engine.Utils.IO.Input;
import peacefulotter.engine.rendering.BufferUtil;
import peacefulotter.engine.Utils.Time;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.Window;

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

    public CoreEngine( Game game, String winName, int winWidth, int winHeight )
    {
        this.game = game;
        this.renderingEngine = new RenderingEngine( winName, winWidth, winHeight  );
        this.currentWindow = renderingEngine.getCurrentWindow();
    }

    public void start()
    {
        if ( isRunning ) return;

        game.init();
        renderingEngine.initCamera();
        Input.initInputs( currentWindow );

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
        double framesCounter = 0;

        double lastTime = Time.getNanoTime();
        double relativeTime = 0; // time in seconds since the start

        while ( isRunning )
        {
            if ( glfwWindowShouldClose( currentWindow ) )
            {
                stop();
                return;
            }

            GL.createCapabilities();
            glfwSwapBuffers( currentWindow );
            glfwPollEvents();

            boolean render = false;

            double startTime = Time.getNanoTime(); // delta between two updates
            double passedTime = startTime - lastTime;
            lastTime = startTime;

            relativeTime += passedTime;
            framesCounter += passedTime;

            if ( framesCounter >= 1.0 )
            {
                System.out.println(frames);
                frames = 0;
                framesCounter = 0;
            }

            while( relativeTime > FRAME_TIME )
            {
                relativeTime -= FRAME_TIME;
                render = true;
                game.update( (float) FRAME_TIME );
                renderingEngine.updateCamera( (float) FRAME_TIME );
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
