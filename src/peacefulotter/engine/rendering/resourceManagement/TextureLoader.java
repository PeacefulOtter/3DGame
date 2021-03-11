package peacefulotter.engine.rendering.resourceManagement;

import peacefulotter.engine.utils.BufferUtil;
import peacefulotter.engine.utils.ResourceLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

public class TextureLoader implements Callable<TextureBuffer>
{
    private final String path;

    public TextureLoader( String path )
    {
        this.path = path;
    }

    @Override
    public TextureBuffer call() throws Exception
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read( new ResourceLoader().resourceStream( path ) );
        } catch ( IOException e )
        {
            e.printStackTrace();
            Resources.freeBuffers();
            System.exit( -1 );
        }

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int[] pixels = image.getRGB( 0, 0, imageWidth, imageHeight, null, 0, imageWidth );

        ByteBuffer buffer = BufferUtil.createByteBuffer( imageWidth * imageHeight * 4 );

        for ( int y = 0; y < imageHeight; y++ )
        {
            for ( int x = 0; x < imageWidth; x++ )
            {
                int pixel = pixels[ y * imageWidth + x ];
                buffer.put( (byte) ( ( pixel >> 16 ) & 0xff ) );
                buffer.put( (byte) ( ( pixel >> 8 ) & 0xff ) );
                buffer.put( (byte) ( ( pixel ) & 0xff ) );
                if ( image.getColorModel().hasAlpha() )
                    buffer.put( (byte) ( ( pixel >> 24 ) & 0xff ) );
                else
                    buffer.put( (byte) 0xff );
            }
        }

        buffer.flip();

        return new TextureBuffer( buffer, imageWidth, imageHeight );
    }
}
