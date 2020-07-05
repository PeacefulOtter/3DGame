package peacefulotter.engine.rendering.graphics;

import peacefulotter.engine.core.maths.Vector2f;
import peacefulotter.engine.core.maths.Vector3f;

public class Vertex
{
    public static final int SIZE = 8;

    private Vector3f pos;
    private Vector2f textureCoordinates;
    private Vector3f normal;
    private Vector3f tangent;

    public Vertex( Vector3f pos )
    {
        this( pos, Vector2f.ZERO );
    }

    public Vertex( Vector3f pos, Vector2f textureCoordinates )
    {
        this( pos, textureCoordinates, Vector3f.ZERO );
    }

    public Vertex( Vector3f pos, Vector2f textureCoordinates, Vector3f normal )
    {
        this( pos, textureCoordinates, normal, Vector3f.ZERO );
    }

    public Vertex(Vector3f pos, Vector2f textureCoordinates, Vector3f normal, Vector3f tangent )
    {
        this.pos = pos;
        this.textureCoordinates = textureCoordinates;
        this.normal = normal;
        this.tangent = tangent;
    }

    public Vector3f getPos() { return pos; }
    public void setPos( Vector3f vector ) { pos = vector; }

    public Vector2f getTextureCoordinates() { return textureCoordinates; }
    public void setTextureCoordinates( Vector2f vector ) { textureCoordinates = vector; }

    public Vector3f getNormal() { return normal; }
    public void setNormal( Vector3f normal ) { this.normal = normal; }

    public Vector3f getTangent() { return tangent; }
    public void setTangent( Vector3f tangent ) { this.tangent = tangent; }
}
