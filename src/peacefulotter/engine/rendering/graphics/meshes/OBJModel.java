package peacefulotter.engine.rendering.graphics.meshes;

import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.utils.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class OBJModel
{
    private final List<Vector3f> positions = new ArrayList<>();
    private final List<Vector2f> texCoords = new ArrayList<>();
    private final List<Vector3f> normals   = new ArrayList<>();
    private final List<OBJIndex> indices   = new ArrayList<>();
    private boolean hasTexCoords, hasNormals;

    private InputStream resourceStream( String resourceName )
    {
        return getClass().getResourceAsStream( resourceName );
    }


    public OBJModel( String filePath )
    {
        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( resourceStream( filePath ) ) ) )
        {
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                String[] split = line.split( " " );
                split = Util.removeEmptyStrings( split );
                if ( split.length < 3 )
                    continue;
                String prefix = split[ 0 ];

                if ( prefix.equals( "v" ) )
                {
                    positions.add( new Vector3f(
                            Float.parseFloat( split[ 1 ] ),
                            Float.parseFloat( split[ 2 ] ),
                            Float.parseFloat( split[ 3 ] ) ) );
                }
                else if ( prefix.equals( "vt" ) )
                {
                    texCoords.add( new Vector2f(
                            Float.parseFloat( split[ 1 ] ),
                            1.0f - Float.parseFloat( split[ 2 ] ) ) );
                }
                else if ( prefix.equals( "vn" ) )
                {
                    normals.add( new Vector3f(
                            Float.parseFloat( split[ 1 ] ),
                            Float.parseFloat( split[ 2 ] ),
                            Float.parseFloat( split[ 3 ] ) ) );
                }
                else if ( split[ 0 ].equals( "f" ) )
                {
                    for (int i = 0; i < split.length - 3; i++)
                    {
                        indices.add( parseOBJIndex( split[ 1 ] ) );
                        indices.add( parseOBJIndex( split[ 2 + i ] ) );
                        indices.add( parseOBJIndex( split[ 3 + i ] ) );
                    }
                }
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            System.exit( 1 );
        }
    }

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

    public IndexedModel toIndexedModel()
    {
        IndexedModel model = new IndexedModel();
        IndexedModel normalModel = new IndexedModel();

        Map<OBJIndex, Integer> resultIndexMap = new HashMap<>();
        Map<Integer, Integer> normalIndexMap = new HashMap<>();
        Map<Integer, Integer> indexMap = new HashMap<>();

        indices.forEach( ( index ) -> {
            Vector3f pos = positions.get( index.vertexIndex );
            Vector2f texCoord = Vector2f.getZero();
            Vector3f normal = Vector3f.getZero();

            if ( hasTexCoords )
                texCoord = texCoords.get( index.texCoordIndex );
            if ( hasNormals )
                normal = normals.get( index.normalIndex );


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
                System.out.println(Vector3f.getZero());
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

        // for( int i = 0; i < model.getTexCoords().size(); i++ )
        //    model.getTexCoords().get( i ).setY( 1.0f - model.getTexCoords().get( i ).getY() );

        return model;
    }
}
