package peacefulotter.engine.rendering.resourceManagement;

import peacefulotter.engine.elementary.Disposable;

public class TextureResource extends Disposable
{
    private int id;

    public TextureResource( int id )
    {
        this.id = id;
    }

    public void dispose()
    {
        System.out.println("texture resource dispos");
        // glDeleteBuffers( id );
    }

    public int getId()  { return id;  }
}
