package peacefulotter.engine.components;

import peacefulotter.engine.core.maths.Quaternion;
import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.RenderingEngine;
import peacefulotter.engine.rendering.graphics.Material;
import peacefulotter.engine.rendering.graphics.Mesh;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

public class MeshRenderer extends GameComponent
{
    private final Mesh mesh;
    private final Material material;
    private boolean fixedTilt;

    public MeshRenderer( Mesh mesh, Material material )
    {
        this.mesh = mesh;
        this.material = material;
    }

    @Override
    public void render( Shader shader, RenderingEngine renderingEngine )
    {
        shader.bind();
        shader.updateUniforms( getTransform(), material, renderingEngine );
        mesh.draw();
    }

    /**
     * Use this method to make the rotation only available around the Y_AXIS
     * MIGHT MOVE THIS SOMEWHERE ELSE
     * @return instance
     */
    public MeshRenderer fixedTilt() { fixedTilt = true; return this; }

    @Override
    public STransform getTransform()
    {
        STransform transform = super.getTransform();
        if ( fixedTilt )
        {
            Vector2f v1 = Vector3f.Z_AXIS.getXZ();
            Vector2f v2 = transform.getRotation().getForward().getXZ();
            transform = new STransform( transform );
            transform.setRotation( new Quaternion( Vector3f.Y_AXIS, Vector2f.calcAngle( v1, v2 ) ) );
        }
        return transform;
    }
}
