package peacefulotter.game.Utils.IO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class KeyboardManager
{
    private List<Key> keys = new ArrayList<>();

    private Set<Key> downKeys = new HashSet<>();
    private Set<Key> upKeys = new HashSet<>();

    public KeyboardManager()
    {
        keys.add( new Key( GLFW_KEY_UP, () -> { } ) );

        for ( Key k : keys )
        {
            k.getIsKeyPressed().addListener( ( ( observable, newValue ) -> {
                //System.out.println( k.getKeyCode() + " " + newValue );
                if ( newValue ) { downKeys.add( k ); }
                else { upKeys.add( k ); downKeys.remove( k ); }
            } ) );
        }
    }

    public void update( long window )
    {
        for ( Key k : keys )
        {
            k.update( window );
        }
    }
}
