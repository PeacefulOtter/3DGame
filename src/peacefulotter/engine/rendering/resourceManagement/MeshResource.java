package peacefulotter.engine.rendering.resourceManagement;

import peacefulotter.engine.elementary.Disposable;

import static org.lwjgl.opengl.GL15.*;

public class MeshResource extends Disposable
{
    private final int vbo; // VertexBufferObject - array of vertices on the graphics card
    private final int ibo; // IndexBufferObject - array of indices on the graphics card
    private int size = 0;

    public MeshResource( int size )
    {
        vbo = glGenBuffers();
        ibo = glGenBuffers();
        this.size = size;
    }

    public void dispose()
    {
        System.out.println("hereeee");
        // glDeleteBuffers( vbo );
        // glDeleteBuffers( ibo );
    }

    public int getVbo()  { return vbo;  }
    public int getIbo()  { return ibo;  }
    public int getSize() { return size; }
}
