package peacefulotter.game.Display.Graphics;

import peacefulotter.game.Maths.Vector2f;
import peacefulotter.game.Maths.Vector3f;

public class Vertex
{
    public static final int SIZE = 5;

    private Vector3f pos;
    private Vector2f textureCoordinates;

    public Vertex( Vector3f pos )
    {
        this( pos, new Vector2f( 0, 0 ) );
    }

    public Vertex( Vector3f pos, Vector2f textCoordinates )
    {
        this.pos = pos;
        this.textureCoordinates = textCoordinates;
    }

    public Vector3f getPos() { return pos; }
    public void setPos( Vector3f vector ) { pos = vector; }

    public Vector2f getTextureCoordinates() { return textureCoordinates; }
    public void setTextureCoordinates( Vector2f vector ) { textureCoordinates = vector; }
}
