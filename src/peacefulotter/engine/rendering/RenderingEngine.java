package peacefulotter.engine.rendering;

import org.lwjgl.opengl.GL;
import peacefulotter.engine.components.*;
import peacefulotter.engine.components.lights.BaseLight;
import peacefulotter.engine.components.lights.DirectionalLight;
import peacefulotter.engine.components.lights.PointLight;
import peacefulotter.engine.components.lights.SpotLight;
import peacefulotter.engine.core.maths.Vector3f;
import peacefulotter.engine.rendering.shaders.*;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

/*

    Move camera inside Game class ?
 */


public class RenderingEngine
{
    private final Window window;
    private final Vector3f ambientLight;
    private final List<BaseLight> lights;
    private BaseLight activeLight;
    private Camera camera;

    public RenderingEngine()
    {
        this.lights = new ArrayList<>();
        this.window = new Window();

        GL.createCapabilities();
        glClearColor( 0, 0, 0, 0 );
        glFrontFace( GL_CW );
        glCullFace( GL_BACK );
        glEnable( GL_CULL_FACE );
        glEnable( GL_DEPTH_TEST );
        glEnable( GL_DEPTH_CLAMP );
        glEnable( GL_TEXTURE_2D );

        ambientLight = new Vector3f( 0.2f, 0.2f, 0.2f );
    }


    public void render( GameObject object )
    {
        if ( camera == null )
            System.err.println( "Main camera not found." );

        clearScreen();

        Shader forwardAmbient = ForwardAmbient.getInstance();
        forwardAmbient.setRenderingEngine( this );

        object.render( forwardAmbient, this );

        glEnable( GL_BLEND );
        glBlendFunc( GL_ONE, GL_ONE );
        glDepthMask( false );
        glDepthFunc( GL_EQUAL );

        for ( BaseLight light : lights )
        {
            // light.getShader().setRenderingEngine( this ); // move this to init
            activeLight = light;
            object.render( light.getShader(), this );
        }

        glDepthFunc( GL_LESS );
        glDepthMask( true );
        glDisable( GL_BLEND );
    }

    public Vector3f getAmbientLight() { return ambientLight; }

    public void addLight( BaseLight light ) { lights.add( light ); }

    public BaseLight getActiveLight() { return activeLight; }

    private static void clearScreen()
    {
        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
    }

    private static void setTextures( boolean enable )
    {
        if ( enable ) { glEnable( GL_TEXTURE_2D ); }
        else { glDisable( GL_TEXTURE_2D ); }
    }

    private static void unbindTextures()
    {
        glBindTexture( GL_TEXTURE_2D, 0 );
    }

    private static void setClearColor( Vector3f color )
    {
        glClearColor( color.getX(), color.getY(), color.getZ(), 1 );
    }

    public long getCurrentWindow() { return window.getWindow(); }

    public Camera getCamera() { return camera; }
    public void setCamera( Camera camera ) { this.camera = camera; }
}
