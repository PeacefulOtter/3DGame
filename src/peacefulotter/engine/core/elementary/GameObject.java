package peacefulotter.engine.core.elementary;

import peacefulotter.engine.rendering.Shaders.Shader;
import peacefulotter.engine.rendering.Shaders.Transfomations.ShaderTransform;

import java.util.ArrayList;
import java.util.List;

public class GameObject implements GameComponent
{
    private final List<GameComponent> children = new ArrayList<>();
    private final ShaderTransform transform;

    public GameObject( ShaderTransform transform )
    {
        this.transform = transform;
    }

    public void addChild( GameComponent child )
    {
        children.add( child );
    }

    public void init()
    {
        for ( GameComponent child: children )
            child.init();
    }

    public void update( float deltaTime )
    {
        for ( GameComponent child: children )
            child.update( deltaTime );
    }

    public void render( Shader shader )
    {
        for ( GameComponent child: children )
            child.render( shader );
    }

    public ShaderTransform getTransform() { return transform; }
}
