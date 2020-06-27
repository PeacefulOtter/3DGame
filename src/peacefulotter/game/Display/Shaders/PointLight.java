package peacefulotter.game.Display.Shaders;

import peacefulotter.game.Maths.Vector3f;

public class PointLight
{
    private BaseLight base;
    private Attenuation attenuation;
    private Vector3f position;

    public PointLight( BaseLight base, Attenuation attenuation, Vector3f position )
    {
        this.base = base;
        this.attenuation = attenuation;
        this.position = position;
    }

    public BaseLight getBaseLight() { return base; }
    public void setBaseLight( BaseLight base ) { this.base = base; }

    public Attenuation getAttenuation() { return attenuation; }
    public void setAttenuation( Attenuation attenuation ) { this.attenuation = attenuation; }

    public Vector3f getPosition() { return position; }
    public void setPosition( Vector3f position ) { this.position = position; }
}
