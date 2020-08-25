package peacefulotter.engine.core;


import org.lwjgl.opengl.GL;
import peacefulotter.engine.physics.PhysicsEngine;
import peacefulotter.engine.utils.IO.Input;
import peacefulotter.engine.utils.Logger;
import peacefulotter.engine.utils.ProfileTimer;
import peacefulotter.engine.utils.Time;
import peacefulotter.engine.rendering.RenderingEngine;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class CoreEngine
{
    private static final boolean ENABLE_PROFILER_LOGS = false;

    private final Game game;
    private final long currentWindow;
    private final RenderingEngine renderingEngine;
    private final PhysicsEngine physicsEngine;

    private static final double FRAMES_CAP = 500;
    private static final double FRAME_TIME = 1.0 / FRAMES_CAP;

    private final ProfileTimer GLprofiler;

    private boolean isRunning = false;

    public CoreEngine( Game game )
    {
        this.game = game;
        this.renderingEngine = new RenderingEngine();
        this.physicsEngine = new PhysicsEngine();
        this.currentWindow = renderingEngine.getCurrentWindow();
        this.GLprofiler = new ProfileTimer();
    }

    public void start()
    {
        if ( isRunning ) return;
        game.init();
        game.setEngine( this );
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

            GLprofiler.startInvocation();
            GL.createCapabilities();
            glfwSwapBuffers( currentWindow );
            glfwPollEvents();
            GLprofiler.stopInvocation();

            boolean render = false;

            double startTime = Time.getNanoTime(); // delta between two updates
            double passedTime = startTime - lastTime;
            lastTime = startTime;

            relativeTime += passedTime;
            framesCounter += passedTime;

            if ( framesCounter >= 1.0 )
            {
                // profiling
                double totalTime = ( 1000.0 * framesCounter ) / (double) frames;
                double totalRecordedTime = 0;
                if ( ENABLE_PROFILER_LOGS )
                {
                    System.out.println();
                    totalRecordedTime += game.displayPhysicsTime( frames );
                    totalRecordedTime += game.displayUpdateTime( frames );
                    totalRecordedTime += renderingEngine.displayRenderTime( frames );
                    totalRecordedTime += GLprofiler.displayAndReset( "GL core engine time", frames );
                    totalRecordedTime += Input.displayInputTime( frames );
                    Logger.log( getClass(), "Other time : " + (totalTime - totalRecordedTime) + "ms" );
                    Logger.log( getClass(), "Total time : " + totalTime + "ms" );
                }

                frames = 0;
                framesCounter = 0;
            }

            while( relativeTime > FRAME_TIME )
            {
                relativeTime -= FRAME_TIME;
                render = true;
                game.update( (float) FRAME_TIME );
                game.simulate( (float) FRAME_TIME, physicsEngine );
                Input.execInputs( (float) FRAME_TIME );
            }

            if ( render )
            {
                game.render( renderingEngine );
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

    public RenderingEngine getRenderingEngine() { return renderingEngine; }

    public PhysicsEngine getPhysicsEngine() { return physicsEngine; }
}
