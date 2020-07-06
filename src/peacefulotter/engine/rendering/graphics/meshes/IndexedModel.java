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
    private final List<Vector3f> tangents  = new ArrayList<>();
    private final List<Integer>  indices   = new ArrayList<>();


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

            normal0.set( normal0.add( normal ).normalize() );
            normal1.set( normal1.add( normal ).normalize() );
            normal2.set( normal2.add( normal ).normalize() );
        }

        //for( int i = 0; i < normals.size(); i++)
       //     normals.set( i, normals.get( i ) );
    }

    /*public void calcTangents()
    {
        int indicesLength = indices.size();

        for ( int i = 0; i < indicesLength; i += 3 )
        {
            int i0 = indices.get( i );
            int i1 = indices.get( i + 1 );
            int i2 = indices.get( i + 2 );

            Vector3f v1 = positions.get( i1 ).sub( positions.get( i0 ) );
            Vector3f v2 = positions.get( i2 ).sub( positions.get( i0 ) );

            Vector2f uv0 = texCoords.get( i0 );
            Vector2f uv1 = texCoords.get( i1 );
            Vector2f uv2 = texCoords.get( i2 );

            Vector2f deltaUv1 = uv1.sub( uv0 );
            Vector2f deltaUv2 = uv2.sub( uv0 );

            float dividend = deltaUv1.getX() * deltaUv2.getY() - deltaUv1.getY() * deltaUv2.getX();
            float r = dividend == 0 ? 0.0f : 1.0f/dividend;
            Vector3f tangent = v1.mul( deltaUv2.getY() ).sub( v2.mul( deltaUv1.getY() ) ).mul( r );

            tangents.set( i0, tangents.get( i0 ).add( tangent ) );
            tangents.set( i1, tangents.get( i1 ).add( tangent ) );
            tangents.set( i2, tangents.get( i2 ).add( tangent ) );
            // tangent.mul( rotationMatrix );
        }

        for( int i = 0; i < tangents.size(); i++ )
            tangents.set( i, tangents.get( i ).normalize() );
    }*/

    public void calcTangents()
    {
        for(int i = 0; i < indices.size(); i += 3)
        {
            int i0 = indices.get(i);
            int i1 = indices.get(i + 1);
            int i2 = indices.get(i + 2);

            Vector3f edge1 = positions.get(i1).sub(positions.get(i0));
            Vector3f edge2 = positions.get(i2).sub(positions.get(i0));

            float deltaU1 = texCoords.get(i1).getX() - texCoords.get(i0).getX();
            float deltaV1 = texCoords.get(i1).getY() - texCoords.get(i0).getY();
            float deltaU2 = texCoords.get(i2).getX() - texCoords.get(i0).getX();
            float deltaV2 = texCoords.get(i2).getY() - texCoords.get(i0).getY();

            float dividend = (deltaU1*deltaV2 - deltaU2*deltaV1);
            //TODO: The first 0.0f may need to be changed to 1.0f here.
            float f = dividend == 0 ? 0.0f : 1.0f/dividend;

            Vector3f tangent = new Vector3f(0,0,0);
            tangent.setX(f * (deltaV2 * edge1.getX() - deltaV1 * edge2.getX()));
            tangent.setY(f * (deltaV2 * edge1.getY() - deltaV1 * edge2.getY()));
            tangent.setZ(f * (deltaV2 * edge1.getZ() - deltaV1 * edge2.getZ()));

            tangents.set( i0, tangents.get( i0 ).add( tangent ).normalize() );
            tangents.set( i1, tangents.get( i1 ).add( tangent ).normalize() );
            tangents.set( i2, tangents.get( i2 ).add( tangent ).normalize() );
        }

        //for( int i = 0; i < tangents.size(); i++ )
        //    tangents.set( i, tangents.get( i ).normalize() );
    }


    public List<Vector3f> getPositions() { return positions; }
    public List<Vector2f> getTexCoords() { return texCoords; }
    public List<Vector3f> getNormals()   { return normals; }
    public List<Vector3f> getTangents()  { return tangents; }
    public List<Integer> getIndices()    { return indices; }
}
