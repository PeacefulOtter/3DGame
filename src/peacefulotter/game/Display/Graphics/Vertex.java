package peacefulotter.game.Display.Graphics;

import peacefulotter.game.Maths.Vector2f;
import peacefulotter.game.Maths.Vector3f;

public class Vertex
{
    public static final int SIZE = 8;

    private Vector3f pos;
    private Vector2f textureCoordinates;
    private Vector3f normal;

    public Vertex( Vector3f pos )
    {
        this( pos, new Vector2f( 0, 0 ) );
    }

    public Vertex( Vector3f pos, Vector2f textureCoordinates )
    {
        this( pos, textureCoordinates, new Vector3f( 0, 0, 0 ) );
    }

    public Vertex( Vector3f pos, Vector2f textureCoordinates, Vector3f normal )
    {
        this.pos = pos;
        this.textureCoordinates = textureCoordinates;
        this.normal = normal;
    }

    public Vector3f getPos() { return pos; }
    public void setPos( Vector3f vector ) { pos = vector; }

    public Vector2f getTextureCoordinates() { return textureCoordinates; }
    public void setTextureCoordinates( Vector2f vector ) { textureCoordinates = vector; }

    public Vector3f getNormal() { return normal; }
    public void setNormal( Vector3f normal ) { this.normal = normal; }

}
