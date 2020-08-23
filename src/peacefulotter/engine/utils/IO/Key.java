package peacefulotter.engine.utils.IO;


import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

public class Key
{
    private final int keyCode;
    private final IOExecutable pressCallback, releaseCallback;
    private boolean hasBeenPressed, canRepeat;

    public Key( int keyCode, IOExecutable pressCallback, boolean canRepeat )
    {
        this( keyCode, pressCallback, null, canRepeat );
    }

    public Key( int keyCode, IOExecutable pressCallback, IOExecutable releaseCallback, boolean canRepeat )
    {
        this.keyCode = keyCode;
        this.pressCallback = pressCallback;
        this.releaseCallback = releaseCallback;
        // canRepeat might be false for instance for semi-automatic weapons
        this.canRepeat = canRepeat;
    }

    public void callbackAction( int action, float deltaTime )
    {
        if ( action == GLFW_PRESS || action == GLFW_REPEAT )
            triggerPressCallback( deltaTime );
        else
            triggerReleaseCallback( deltaTime );
    }


    public void triggerPressCallback( float deltaTime )
    {
        if ( !hasBeenPressed || canRepeat )
            pressCallback.exec( deltaTime );
        hasBeenPressed = true;
    }

    public void triggerReleaseCallback( float deltaTime )
    {
        if ( hasBeenPressed )
        {
            if ( releaseCallback != null )
                releaseCallback.exec( deltaTime );
            hasBeenPressed = false;
        }
    }

    public int getKeyCode() { return keyCode; }
}
