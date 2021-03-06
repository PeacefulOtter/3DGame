package peacefulotter.engine.rendering.resourceManagement;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;

public final class Resources
{
    private static final List<Integer> buffers = new ArrayList<>();

    public static void addBuffer( int buffer )
    {
        buffers.add( buffer );
    }

    public static void freeBuffers()
    {
        for (int buffer : buffers)
            glDeleteBuffers( buffer );
        System.out.println("free buffers done");
    }
}

