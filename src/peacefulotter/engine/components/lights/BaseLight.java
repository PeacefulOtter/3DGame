package peacefulotter.engine.components.lights;

import peacefulotter.engine.components.GameComponent;
import peacefulotter.engine.core.CoreEngine;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.shaders.Shader;

public class BaseLight extends GameComponent
{
    private Vector3f color;
    private float intensity;
    private Shader shader;

    public BaseLight( Vector3f color, float intensity )
    {
        this.color = color;
        this.intensity = intensity;
    }

    @Override
    public void addToEngine( CoreEngine engine )
    {
        engine.getRenderingEngine().addLight( this );
    }

    public Shader getShader() { return shader; }
    protected void setShader( Shader shader ) { this.shader = shader; }

    public Vector3f getColor() { return color; }
    public void setColor( Vector3f color ) { this.color = color; }

    public float getIntensity() { return intensity; }
    public void setIntensity( float intensity ) { this.intensity = intensity; }
}
