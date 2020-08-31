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

import java.util.List;

public class Terrain
{
    private static final int REPETITION = 5;
    private static final int SIZE = 800;
    private static final int VERTEX_COUNT = 128;

    private final float x, z;
    private final Mesh mesh;
    private final Material material;

    public Terrain( int gridX, int gridZ, Material material )
    {
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.mesh = generateTerrain();
        this.material = material;
    }

    public void render( Shader shader, RenderingEngine renderingEngine, STransform transform )
    {
        shader.bind();
        shader.updateUniforms( transform, material, renderingEngine );
        mesh.draw();
    }

    private Mesh generateTerrain()
    {
        int count = VERTEX_COUNT * VERTEX_COUNT;
        int indicesCapacity = 6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1);
        IndexedModel model = new IndexedModel( count, indicesCapacity );

        for( int i = 0; i < VERTEX_COUNT; i++ )
        {
            for( int j = 0; j < VERTEX_COUNT; j++ )
            {
                model.getPositions().add( new Vector3f( (float)j/((float)VERTEX_COUNT - 1) * SIZE, 0, (float)i/((float)VERTEX_COUNT - 1) * SIZE ) );
                model.getTexCoords().add( new Vector2f( (float)j/((float)VERTEX_COUNT - 1), (float)i/((float)VERTEX_COUNT - 1) ).mul( REPETITION ) );
                model.getNormals().add( new Vector3f( 0, 1, 0 ) );
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

        return new Mesh( v.getVertices(), v.getIndices() );
    }

    public float getX()
    {
        return x;
    }

    public float getZ()
    {
        return z;
    }

    public Mesh getMesh()
    {
        return mesh;
    }

    public Material getMaterial() { return material; }
}
