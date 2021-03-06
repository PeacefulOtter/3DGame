package peacefulotter.engine.utils;

import org.lwjgl.opengl.GL11;
import peacefulotter.engine.components.renderer.SkyBoxRenderer;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.graphics.Mesh;
import peacefulotter.engine.rendering.graphics.RawModel;
import peacefulotter.engine.rendering.graphics.TextureData;
import peacefulotter.engine.rendering.graphics.Vertex;
import peacefulotter.engine.rendering.graphics.meshes.IndexedModel;
import peacefulotter.engine.rendering.resourceManagement.TextureResource;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class ResourceLoader
{
    public InputStream resourceStream(String resourceName)
    {
        return getClass().getResourceAsStream( resourceName );
    }

    public static Mesh.Vertices loadVertices( IndexedModel indexedModel )
    {
        List<Vector3f> positions = indexedModel.getPositions();
        List<Vector2f> texCoords = indexedModel.getTexCoords();
        List<Vector3f> normals   = indexedModel.getNormals();
        List<Vector3f> tangents  = indexedModel.getTangents();
        List<Integer>  indices   = indexedModel.getIndices();

        int modelSize = indexedModel.getPositions().size();
        Vertex[] vertices = new Vertex[ modelSize ];
        for ( int i = 0; i < modelSize; i++ )
        {
            vertices[ i ] = new Vertex(
                    positions.get( i ),
                    texCoords.get( i ),
                    normals.get( i ),
                    tangents.get( i ) );
        }

        return new Mesh.Vertices( vertices, Utils.toIntArray( indices ) );
    }

    public static RawModel loadToVao( float[] positions, int dimension )
    {
        // Create a new Vertex Array Object in memory and select it (bind)
        // A VAO can have up to 16 attributes (VBO's) assigned to it by default
        int vaoId = glGenVertexArrays();
        glBindVertexArray( vaoId );

        // Create a new Vertex Buffer Object in memory and select it (bind)
        // A VBO is a collection of Vectors which in this case resemble the location of each vertex.
        int vboId = glGenBuffers();
        glBindBuffer( GL_ARRAY_BUFFER, vboId );
        FloatBuffer verticesBuffer = BufferUtil.createSimpleFlippedBuffer( positions );
        glBufferData( GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW );
        // Put the VBO in the attributes list at index 0
        glVertexAttribPointer(0, dimension, GL_FLOAT, false, 0, 0 );
        // Deselect (bind to 0) the VBO
        glBindBuffer( GL_ARRAY_BUFFER, 0 );

        // Deselect (bind to 0) the VAO
        glBindVertexArray( 0 );

        return new RawModel( vaoId, positions.length / dimension );
    }

    public static TextureResource loadCubeMap()
    {
        String[] texturePaths = SkyBoxRenderer.TEXTURE_FILES;
        TextureResource tr = new TextureResource();

        glActiveTexture( GL_TEXTURE0 );
        glBindTexture( GL_TEXTURE_CUBE_MAP, tr.getId() );
        for ( int i = 0; i < texturePaths.length; i++ )
        {
            TextureData data = TextureData.createTextureData( texturePaths[ i ] );
            glTexImage2D( GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA,
                    data.getWidth(), data.getHeight(), 0, GL_RGBA,
                    GL_UNSIGNED_BYTE, data.getBuffer() );
        }

        glTexParameteri( GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
        glTexParameteri( GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR );

        GL11.glTexParameteri( GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE );
        GL11.glTexParameteri( GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE );

        return tr;
    }
}
