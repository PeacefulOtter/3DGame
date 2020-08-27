package peacefulotter.game.actor;

// keeps track of which angle around the Y_AXIS the player is
public enum VelocityAngle
{
    FORWARD( 0 ), RIGHT( 90 ), BACKWARD( 180 ), LEFT( -90 );

    private final int rotation;
    private boolean active;

    VelocityAngle( int rotation )
    {
        this.rotation = rotation;
    }

    public static float getAngle()
    {
        int angle = 0;

        if ( RIGHT.active )
            angle += RIGHT.rotation;
        if ( LEFT.active )
            angle += LEFT.rotation;
        if ( FORWARD.active && !BACKWARD.active )
        {
            angle += Math.signum( -angle ) * 45;
        }
        else if ( BACKWARD.active && !FORWARD.active )
        {
            angle += Math.signum( angle ) * 45;
            if ( angle == 0 )
                angle = 180;
        }
        return angle;
    }

    public void setActive( boolean active ) { this.active = active; }
}
