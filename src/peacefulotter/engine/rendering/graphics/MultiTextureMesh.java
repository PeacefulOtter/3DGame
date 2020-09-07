package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class MultiTextureMesh
{
    private final List<Mesh> meshes = new ArrayList<>();
    private final List<Material> materials = new ArrayList<>();

    public void addMesh( Mesh mesh )
    {
        meshes.add( mesh );
    }

    public void addMaterial( Material material )
    {
        materials.add( material );
    }

    public int getSize() { return Math.min( meshes.size(), materials.size() ); }
    public List<Mesh> getMeshes() { return meshes; }
    public List<Material> getMaterials() { return materials; }
}
