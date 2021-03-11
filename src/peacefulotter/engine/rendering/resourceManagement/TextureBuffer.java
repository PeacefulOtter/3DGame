package peacefulotter.engine.rendering.resourceManagement;

import java.nio.ByteBuffer;

public class TextureBuffer {
    public final ByteBuffer buffer;
    public final int imageWidth, imageHeight;

    public TextureBuffer( ByteBuffer buffer, int imageWidth, int imageHeight )
    {
        this.buffer = buffer;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }
}