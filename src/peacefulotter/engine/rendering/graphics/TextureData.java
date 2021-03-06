package peacefulotter.engine.rendering.graphics;


import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class TextureData
{
    private final ByteBuffer buffer;
    private final int width, height;

    public static TextureData createTextureData( String path )
    {
        int width = 0; int height = 0;
        ByteBuffer buffer = null;
        try ( FileInputStream in = new FileInputStream("./res/textures/skybox/" + path + ".png" ) )
        {
            PNGDecoder decoder = new PNGDecoder( in );
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect( 4 * width * height );
            decoder.decode( buffer, width * 4, PNGDecoder.Format.RGBA );
            buffer.flip();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            System.exit( -1 );
        }
        return new TextureData( buffer, width, height );
    }

    private TextureData( ByteBuffer buffer, int width, int height )
    {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
    }

    public ByteBuffer getBuffer()
    {
        return buffer;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}
