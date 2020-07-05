package peacefulotter.engine.rendering.graphics.meshes;

import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class IndexedModel
{
    private final List<Vector3f> positions = new ArrayList<>();
    private final List<Vector2f> texCoords = new ArrayList<>();
    private final List<Vector3f> normals   = new ArrayList<>();
    private final List<Integer>  indices   = new ArrayList<>();
    private final List<Vector3f> tangents = new ArrayList<>();


    public void calcNormals()
    {
        int indicesLength = indices.size();
        // int positionsLength = positions.size();

        for ( int i = 0; i < indicesLength; i += 3 )
        {
            int i0 = indices.get( i );
            int i1 = indices.get( i + 1);
            int i2 = indices.get( i + 2 );

            Vector3f v0 = positions.get( i0 );
            Vector3f v1 = positions.get( i1 ).sub( v0 );
            Vector3f v2 = positions.get( i2 ).sub( v0 );

            Vector3f normal = v1.cross( v2 ).normalize();

            Vector3f normal0 = normals.get( i0 );
            Vector3f normal1 = normals.get( i1 );
            Vector3f normal2 = normals.get( i2 );

            normal0.set( normal0.add( normal ) );
            normal1.set( normal1.add( normal ) );
            normal2.set( normal2.add( normal ) );

            Vector2f uv0 = texCoords.get( i0 );
            Vector2f uv1 = texCoords.get( i1 );
            Vector2f uv2 = texCoords.get( i2 );

            Vector2f deltaUv1 = uv1.sub( uv0 );
            Vector2f deltaUv2 = uv2.sub( uv0 );

            float r = 1 / ( deltaUv1.getX() * deltaUv2.getY() - deltaUv1.getY() * deltaUv2.getX() );
            Vector3f tangent = v1.mul( deltaUv2.getY() ).sub( v2.mul( deltaUv1.getY() ) ).mul( r );
            tangents.add( tangent.normalize() );
            tangents.add( tangent.normalize() );
            tangents.add( tangent.normalize() );
        }

        /*for ( int i = 0; i < positionsLength; i++ )
        {
            Vector3f normal = normals.get( i );
            normal.set( normal.normalize() );
        }*/


        // tangent.mul( rotationMatrix );
    }


    public List<Vector3f> getPositions() { return positions; }
    public List<Vector2f> getTexCoords() { return texCoords; }
    public List<Vector3f> getNormals()   { return normals; }
    public List<Integer> getIndices()    { return indices; }
    public List<Vector3f> getTangents()  { return tangents; }
}
