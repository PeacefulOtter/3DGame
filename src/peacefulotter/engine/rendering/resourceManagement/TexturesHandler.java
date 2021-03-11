package peacefulotter.engine.rendering.resourceManagement;

import peacefulotter.engine.rendering.graphics.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/***
 * Used for Multi-Threading texture loading
 */
public class TexturesHandler
{
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final List<Texture> textures = new ArrayList<>();
    private static final List<Future<TextureBuffer>> threads = new ArrayList<>();

    public static void create( Texture texture, String path )
    {
        Callable<TextureBuffer> callable = new TextureLoader( path );
        Future<TextureBuffer> future = executor.submit(callable);
        textures.add( texture );
        threads.add( future );
    }

    public static void setTextureResources()
    {
        for ( int i = 0; i < threads.size(); i++ )
        {
            try
            {
                textures.get( i ).setResource( threads.get( i ).get() );
            } catch ( InterruptedException | ExecutionException e )
            {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }
}
