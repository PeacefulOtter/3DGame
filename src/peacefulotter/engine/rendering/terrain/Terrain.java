package peacefulotter.engine.rendering.terrain;

import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.Mesh;
import peacefulotter.engine.rendering.graphics.meshes.IndexedModel;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.core.transfomations.STransform;
import peacefulotter.engine.utils.ResourceLoader;

import java.awt.image.BufferedImage;
import java.util.List;

public class Terrain
{
    private static final float HALF_MAX_PIXEL_COLOR = 255 * 255 * 255 / 2f;
    private static final int MAX_DESIRED_HEIGHT = 70;
    private static final int REPETITION = 40;
    private static final int SIZE = 800;

    private static int TERRAIN_ID = 0;

    private final Vector3f worldPos;
    private Mesh mesh;

    public Terrain( int gridX, int gridZ, BufferedImage image )
    {
        this.worldPos = new Vector3f( gridX * SIZE, 0, gridZ * SIZE );
        generateTerrain( gridX, gridZ, image );
    }

    public void render( Shader shader, RenderingEngine renderingEngine, STransform transform, Material material )
    {
        shader.bind();
        shader.updateUniforms( transform, material, renderingEngine );
        mesh.draw();
    }

    private void generateTerrain( int gridX, int gridZ, BufferedImage image )
    {
        final int VERTEX_COUNT = image.getHeight();
        int count = VERTEX_COUNT * VERTEX_COUNT;
        int indicesCapacity = 6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1);

        IndexedModel model = new IndexedModel( count, indicesCapacity );

        for( int i = 0; i < VERTEX_COUNT; i++ )
        {
            for( int j = 0; j < VERTEX_COUNT; j++ )
            {
                float x = (float) j / (VERTEX_COUNT - 1);
                float y = calculateHeight( j, i, image );
                float z = (float) i / (VERTEX_COUNT - 1);
                model.getPositions().add( new Vector3f( x * SIZE, y, z * SIZE ) );
                model.getTexCoords().add( new Vector2f( x, z ).mul( REPETITION ) );
                model.getNormals().add( calculateNormal( j, i, image ) );
                model.getTangents().add( Vector3f.getZero() );
            }
        }

        List<Integer> indices = model.getIndices();
        for( int gz = 0; gz < VERTEX_COUNT - 1; gz++ )
        {
            for( int gx = 0; gx < VERTEX_COUNT - 1; gx++ )
            {
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices.add( topLeft );
                indices.add( bottomLeft );
                indices.add( topRight );
                indices.add( topRight );
                indices.add( bottomLeft );
                indices.add( bottomRight );
            }
        }

        model.calcTangents();
        Mesh.Vertices v = ResourceLoader.loadVertices( model );
        mesh = new Mesh( "TERRAIN_ID" + TERRAIN_ID++, v.getVertices(), v.getIndices() );
    }

    private float calculateHeight( int x, int z, BufferedImage image )
    {
        if ( x < 0 || x >= image.getWidth() || z < 0 || z >= image.getHeight() )
            return 0;
        float height = image.getRGB( x , z );
        return ( height + HALF_MAX_PIXEL_COLOR ) * MAX_DESIRED_HEIGHT / HALF_MAX_PIXEL_COLOR;
    }

    private Vector3f calculateNormal( int x, int z, BufferedImage image )
    {
        float heightL = calculateHeight( x-1, z, image );
        float heightR = calculateHeight( x+1, z, image );
        float heightD = calculateHeight( x, z-1, image );
        float heightU = calculateHeight( x, z+1, image );
        Vector3f normal = new Vector3f( heightL - heightR, 2f, heightD - heightU );
        normal.normalize();
        return normal;
    }


    public Vector3f getWorldPos() { return worldPos; }

    public Mesh getMesh()
    {
        return mesh;
    }
}
