package peacefulotter.engine.components;

import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.core.GameObject;
import peacefulotter.engine.core.elementary.Initializable;
import peacefulotter.engine.core.elementary.Renderable;
import peacefulotter.engine.core.elementary.Updatable;
import peacefulotter.engine.rendering.shaders.Shader;
import peacefulotter.engine.rendering.shaders.transfomations.STransform;

public class GameComponent implements Initializable, Updatable, Renderable
{
    private GameObject parent;

    @Override
    public void init() { }

    @Override
    public void update( float deltaTime ) { }

    @Override
    public void render( Shader shader ) { }

    public void setParent( GameObject parent ) { this.parent = parent; }

    public STransform getTransform() { return parent.getTransform(); }

    public void addToEngine( CoreEngine engine ) { }
}
