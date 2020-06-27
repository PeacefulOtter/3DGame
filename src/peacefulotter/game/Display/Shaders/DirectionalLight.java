package peacefulotter.game.Display.Shaders;

import peacefulotter.game.Maths.Vector3f;

public class DirectionalLight
{
    private BaseLight base;
    private Vector3f direction;

    public DirectionalLight( BaseLight base, Vector3f direction )
    {
        this.base = base;
        this.direction = direction.normalize();
    }

    public BaseLight getBaseLight() { return base; }
    public void setBaseLight( BaseLight base ) { this.base = base; }

    public Vector3f getDirection() { return direction; }
    public void setDirection( Vector3f direction ) { this.direction = direction; }
}
