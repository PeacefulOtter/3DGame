package peacefulotter.game.Display;

import peacefulotter.game.Maths.Vector3f;

public class Camera
{
    public static final Vector3f Y_AXIS = new Vector3f( 0, 1, 0 );

    private Vector3f position;
    private Vector3f forward, upward;

    public Camera()
    {
        this(
                new Vector3f( 0, 0, 0 ),
                new Vector3f( 0, 0, 1 ),
                new Vector3f( 0, 1, 0 ) );
    }

    public Camera( Vector3f position, Vector3f forward, Vector3f upward )
    {
        this.position = position;
        this.forward = forward.normalize();
        this.upward = upward.normalize();
    }

    public void move( Vector3f direction, float amount )
    {
        position = position.add( direction.mul( amount ) );
    }

    public void rotateX( )
    {
        Vector3f horAxis = Y_AXIS.cross( forward ).normalize();
        float angle = 0;
        forward.rotate( angle, horAxis ).normalize();
        upward = forward.cross( horAxis ).normalize();
    }

    public void rotateY()
    {
        Vector3f horAxis = Y_AXIS.cross( forward ).normalize();
        float angle = 0;
        forward.rotate( angle, Y_AXIS ).normalize();
        upward = forward.cross( horAxis ).normalize();
    }

    public Vector3f getLeft() { return upward.cross( forward ).normalize(); }
    public Vector3f getRight() { return forward.cross( upward ).normalize(); }

    public Vector3f getPosition() { return position; }
    public void setPosition( Vector3f position ) { this.position = position; }

    public Vector3f getForward() { return forward; }
    public void setForward( Vector3f forward ) { this.forward = forward; }

    public Vector3f getUpward() { return upward; }
    public void setUpward( Vector3f upward ) { this.upward = upward; }
}

