package peacefulotter.engine.rendering.terrain;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.Mesh;
import peacefulotter.engine.rendering.graphics.meshes.IndexedModel;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.utils.ResourceLoader;
import peacefulotter.engine.utils.Utils;

import java.awt.image.BufferedImage;
import java.util.List;

// KIND OF A MESHRENDERER... THIS IS BAD
public class Terrain extends GameComponent
{
    private static final float HALF_MAX_PIXEL_COLOR = 255 * 255 * 255 / 2f;
    private static final int MAX_DESIRED_HEIGHT = 85;
    public static final int SIZE = 1300;

    private static int TERRAIN_ID = 0;

    private final Vector3f worldPos;
    private Mesh mesh;
    private Material material;
    private float[][] heights;

    public Terrain( int gridX, int gridZ, BufferedImage image )
    {
        this.worldPos = new Vector3f( gridX * SIZE, 0, gridZ * SIZE );
        generateTerrain( image );
    }

    public void setMaterial( Material material )
    {
        this.material = material;
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        shader.bind();
        shader.updateUniforms( getTransform(), material, renderingEngine );
        mesh.draw();
    }

    private void generateTerrain( BufferedImage image )
    {
        final int VERTEX_COUNT = image.getHeight();
        int count = VERTEX_COUNT * VERTEX_COUNT;
        int indicesCapacity = 6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1);

        heights = new float[ VERTEX_COUNT ][ VERTEX_COUNT ];
        IndexedModel model = new IndexedModel( count, indicesCapacity );

        for( int i = 0; i < VERTEX_COUNT; i++ )
        {
            for( int j = 0; j < VERTEX_COUNT; j++ )
            {
                float x = (float) j / (VERTEX_COUNT - 1);
                float y = calculateHeight( j, i, image );
                float z = (float) i / (VERTEX_COUNT - 1);
                heights[ j ][ i ] = y;
                model.getPositions().add( new Vector3f( x * SIZE, y, z * SIZE ) );
                model.getTexCoords().add( new Vector2f( x, z ) );
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

    public float getTerrainHeight( float worldX, float worldZ )
    {
        float tX = worldX - worldPos.getX();
        float tZ = worldZ - worldPos.getZ();

        float gridSquareSize =  SIZE / (float) (heights.length - 1);
        int gridX = (int) Math.floor( tX / gridSquareSize );
        int gridZ = (int) Math.floor( tZ / gridSquareSize );

        if ( gridX < 0 || gridX >= heights.length - 1 || gridZ < 0 || gridZ >= heights.length - 1 )
            return 0;

        float xCoord = ( tX % gridSquareSize ) / gridSquareSize;
        float zCoord = ( tZ % gridSquareSize ) / gridSquareSize;

        if ( xCoord <= 1 - zCoord )
            return Utils.barryCentric(
                    new Vector3f(0, heights[gridX][gridZ], 0),
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoord, zCoord) );
        else
                return Utils.barryCentric(
                        new Vector3f(1, heights[gridX + 1][gridZ], 0),
                        new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
                        new Vector3f(0, heights[gridX][gridZ + 1], 1),
                        new Vector2f(xCoord, zCoord) );
    }

    public Vector3f getWorldPos() { return worldPos; }

    public Mesh getMesh() { return mesh; }

    public int getSize() { return SIZE; }
}
