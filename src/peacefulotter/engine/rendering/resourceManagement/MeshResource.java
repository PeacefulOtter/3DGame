package peacefulotter.engine.rendering.resourceManagement;

import static org.lwjgl.opengl.GL15.glGenBuffers;

public class MeshResource
{
    private final int vbo; // VertexBufferObject - array of vertices on the graphics card
    private final int ibo; // IndexBufferObject - array of indices on the graphics card
    private final int size;

    public MeshResource( int size )
    {
        vbo = glGenBuffers();
        ibo = glGenBuffers();
        this.size = size;

        // Used to free memory and thus avoid memory leak
        Resources.addBuffer( vbo );
        Resources.addBuffer( ibo );
    }

    public int getVbo()  { return vbo;  }
    public int getIbo()  { return ibo;  }
    public int getSize() { return size; }
}
