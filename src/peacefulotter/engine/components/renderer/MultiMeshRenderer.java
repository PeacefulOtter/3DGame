package peacefulotter.engine.components.renderer;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.*;
import peacefulotter.engine.rendering.graphics.meshes.IndexedModel;
import peacefulotter.engine.rendering.graphics.meshes.OBJModel;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.utils.Logger;
import peacefulotter.engine.utils.ResourceLoader;
import peacefulotter.engine.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static peacefulotter.engine.rendering.graphics.Mesh.MODELS_PATH;


public class MultiMeshRenderer extends GameComponent
{
    private static final String MATERIALS_PATH = "/materials/";

    private static final Map<String, MultiTextureMesh> mtms = new HashMap<>();
    private final MultiTextureMesh mtm;
    private final int size;

    public MultiMeshRenderer( String subFolder, String fileName )
    {
        String path = subFolder + fileName;
        if ( mtms.containsKey( path ) )
            this.mtm = mtms.get( path );
        else
        {
            this.mtm = loadMultiMesh( subFolder, fileName );
            mtms.put( path, mtm );
        }
        this.size = mtm.getSize();
    }

    private MultiTextureMesh loadMultiMesh( String subFolder, String fileName )
    {
        MultiTextureMesh mtm = new MultiTextureMesh();
        String path = subFolder + fileName;
        Logger.log( getClass(), " = Loading MultiMesh at : " + path );
        String[] splitArray = fileName.split( "\\." );
        String extension = splitArray[ splitArray.length - 1 ];

        if ( !extension.equals( "obj" ) )
        {
            System.err.println( "File format not supported" );
            new Exception().printStackTrace();
            System.exit( 1 );
        }

        OBJModel currentModel = new OBJModel();
        Map<String, SimpleMaterial> materialMap = null; // associate material name to the material
        boolean isFirstMaterial = true;

        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( new ResourceLoader().resourceStream( MODELS_PATH + subFolder + fileName ) ) ) )
        {
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                String[] split = line.split( " " );
                split = Utils.removeEmptyStrings( split );
                if ( split.length == 0 ) continue;
                String prefix = split[ 0 ];

                if ( prefix.equals( "mtllib" ) )
                {
                    materialMap = loadMaterialFile( subFolder, split[ 1 ] );
                }
                else if ( prefix.equals( "usemtl" ) && materialMap != null )
                {
                    String[] splitFileName = split[ 1 ].split( "\\." );
                    String materialFileName = splitFileName[ 0 ];
                    String materialExtension = splitFileName[ 1 ];

                    if ( materialMap.containsKey( materialFileName ) )
                    {
                        Logger.log( getClass(), "   Adding Material to MTM : " + materialFileName + " at " + currentModel.getIndicesSize() );
                        Material mat = new Material( materialMap.get( materialFileName ) );
                        mat.setNormalMap( new Texture( subFolder, materialFileName + "_normal." + materialExtension ) );
                        mat.setDispMap( new Texture( subFolder, materialFileName + "_height." + materialExtension ) );
                        mtm.addMaterial( mat );
                    }
                    if ( isFirstMaterial )
                        isFirstMaterial = false;
                    else
                        addMeshToMTM( currentModel, mtm, path + mtm.getSize() );
                }
                else if ( prefix.equals( "v" ) )
                {
                    currentModel.addPosition( new Vector3f(
                            Float.parseFloat( split[ 1 ] ),
                            Float.parseFloat( split[ 2 ] ),
                            Float.parseFloat( split[ 3 ] ) ) );
                }
                else if ( prefix.equals( "vt" ) )
                {
                    currentModel.addTexCoord( new Vector2f(
                            Float.parseFloat( split[ 1 ] ),
                            1.0f - Float.parseFloat( split[ 2 ] ) ) );
                }
                else if ( prefix.equals( "vn" ) )
                {
                    currentModel.addNormal( new Vector3f(
                            Float.parseFloat( split[ 1 ] ),
                            Float.parseFloat( split[ 2 ] ),
                            Float.parseFloat( split[ 3 ] ) ) );
                }
                else if ( split[ 0 ].equals( "f" ) )
                {
                    for ( int i = 0; i < split.length - 3; i++ )
                    {
                        currentModel.addIndices( split[ 1 ] );
                        currentModel.addIndices( split[ 2 + i ] );
                        currentModel.addIndices( split[ 3 + i ] );
                    }
                }
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            System.exit( 1 );
        }

        addMeshToMTM( currentModel, mtm, path + mtm.getSize() );

        return mtm;
    }

    private Map<String, SimpleMaterial> loadMaterialFile(String subFolder, String fileName )
    {
        Logger.log( getClass(), "Loading material at : " + subFolder + fileName );
        Map<String, SimpleMaterial> materialMap = new HashMap<>();

        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( new ResourceLoader().resourceStream( MATERIALS_PATH + subFolder + fileName ) ) ) )
        {
            String line;
            SimpleMaterial.MaterialBuilder builder = new SimpleMaterial.MaterialBuilder();
            while ( ( line = reader.readLine() ) != null )
            {
                if ( line.startsWith( "newmtl" ) )
                {
                    builder = new SimpleMaterial.MaterialBuilder();
                }
                else if ( line.startsWith( "Ni" ) )
                {
                    builder.setSpecularIntensity( Float.parseFloat( line.split( " " )[ 1 ] ) );
                }
                else if ( line.startsWith( "Ns" ) )
                {
                    builder.setSpecularPower( Float.parseFloat( line.split( " " )[ 1 ] ) );
                }
                else if ( line.startsWith( "map_" ) )
                {
                    String[] s = line.split( " " )[ 1 ].split("\\.");
                    builder.setTexture( new Texture( subFolder, line.split( " " )[ 1 ] ) );
                    // builder.setNormalTexture( new Texture( subFolder, s[ 0 ] + "_normal." + s[ 1 ] ) );
                    materialMap.put( s[ 0 ], builder.build() );
                }
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            System.exit( 1 );
        }

        return materialMap;
    }

    private void addMeshToMTM( OBJModel currentModel, MultiTextureMesh mtm, String path )
    {
        Logger.log( getClass(), "  Adding Mesh to MTM : " + path );
        OBJModel newModel = new OBJModel();
        currentModel.getIndices().forEach( indexObj -> {
            newModel.addPosition( currentModel.getPosition( indexObj.vertexIndex ) );
            newModel.addTexCoord( currentModel.getTexCoord( indexObj.texCoordIndex ) );
            newModel.addNormal( currentModel.getNormal( indexObj.normalIndex ) );
            newModel.addIndices( indexObj );
        } );
        currentModel.getIndices().clear();
        // treat all the faces and add the meshes of all the faces
        IndexedModel indexedModel = newModel.toIndexedModel( true );
        Mesh mesh = new Mesh( path, ResourceLoader.loadVertices( indexedModel ) );
        mtm.addMesh( mesh );
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        shader.bind();
        shader.updateUniforms( getTransform(), mtm.getMaterials().get( 0 ), renderingEngine );
        mtm.getMeshes().get( 0 ).draw();

        for ( int i = 1; i < size; i++ )
        {
            shader.forceUpdateTexture( mtm.getMaterials().get( i ), renderingEngine );
            mtm.getMeshes().get( i ).draw();
        }
    }
}
