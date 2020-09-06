package peacefulotter.engine.rendering.graphics.meshes;

import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;

import java.util.*;

public class OBJModel
{
    private final List<Vector3f> positions = new ArrayList<>();
    private final List<Vector2f> texCoords = new ArrayList<>();
    private final List<Vector3f> normals   = new ArrayList<>();
    private final List<OBJIndex> indices   = new ArrayList<>();

    private boolean hasTexCoords, hasNormals;

    public void addPosition( Vector3f position )
    {
        positions.add( position );
    }
    public void addTexCoord( Vector2f texCoord )
    {
        texCoords.add( texCoord );
    }
    public void addNormal( Vector3f normal )
    {
        normals.add( normal );
    }
    public void addIndices( String line )
    {
        indices.add( parseOBJIndex( line ) );
    }
    public void addIndices( OBJIndex objIndex )
    {
        indices.add( objIndex );
    }

    public Vector3f getPosition( int index ) { return positions.get( index ); }
    public Vector2f getTexCoord( int index ) { return texCoords.get( index ); }
    public Vector3f getNormal( int index ) { return normals.get( index ); }
    public List<OBJIndex> getIndices() { return indices; }

    public int getIndicesSize() { return indices.size(); }

    private OBJIndex parseOBJIndex( String line )
    {
        String[] values = line.split( "/" );

        OBJIndex result = new OBJIndex();
        result.vertexIndex = Integer.parseInt( values[ 0 ] ) - 1;

        hasTexCoords = values.length > 1 && !values[ 1 ].isEmpty();
        hasNormals = values.length > 2;

        if ( hasTexCoords )
            result.texCoordIndex = Integer.parseInt( values[ 1 ] ) - 1;
        if ( hasNormals )
            result.normalIndex = Integer.parseInt( values[ 2 ] ) - 1;

        return result;
    }

    public IndexedModel toIndexedModel() { return toIndexedModel( false ); }

    public IndexedModel toIndexedModel( boolean isSorted )
    {
        IndexedModel model = new IndexedModel();
        IndexedModel normalModel = new IndexedModel();

        Map<OBJIndex, Integer> resultIndexMap = new HashMap<>();
        Map<Integer, Integer> normalIndexMap = new HashMap<>();
        Map<Integer, Integer> indexMap = new HashMap<>();

        final int[] x = { 0 };

        indices.forEach( ( index ) -> {
            Vector3f pos; Vector2f texCoord; Vector3f normal;
            if ( isSorted )
            {
                pos = positions.get( x[0] );
                texCoord = texCoords.get( x[0] );
                normal = normals.get( x[0]++ );
            }
            else
            {
                pos = positions.get( index.vertexIndex );
                texCoord = Vector2f.getZero();
                normal = Vector3f.getZero();

                if ( hasTexCoords )
                    texCoord = texCoords.get( index.texCoordIndex );
                if ( hasNormals )
                    normal = normals.get( index.normalIndex );
            }

            Integer modelVertexIndex = resultIndexMap.get( index );

            if ( modelVertexIndex == null )
            {
                modelVertexIndex = model.getPositions().size();
                resultIndexMap.put( index, modelVertexIndex );

                model.getPositions().add( pos );
                model.getTexCoords().add( texCoord );
                if ( hasNormals )
                    model.getNormals().add( normal );
            }

            Integer normalModelIndex = normalIndexMap.get( index.vertexIndex );

            if ( normalModelIndex == null )
            {
                normalModelIndex = normalModel.getPositions().size();
                normalIndexMap.put( index.vertexIndex, normalModelIndex );

                normalModel.getPositions().add( pos );
                normalModel.getTexCoords().add( texCoord );
                normalModel.getNormals().add( normal );
                normalModel.getTangents().add( Vector3f.getZero() );
            }

            model.getIndices().add( modelVertexIndex );
            normalModel.getIndices().add( normalModelIndex );
            indexMap.put( modelVertexIndex, normalModelIndex );
        } );

        if ( !hasNormals )
        {
            normalModel.calcNormals();
            int normalsLength = model.getPositions().size();
            for ( int i = 0; i < normalsLength; i++ )
                model.getNormals().add( normalModel.getNormals().get( indexMap.get( i ) ) );
        }

        normalModel.calcTangents();

        for ( int i = 0; i < model.getPositions().size(); i++ )
            model.getTangents().add( normalModel.getTangents().get( indexMap.get( i ) ) );

        return model;
    }
}
