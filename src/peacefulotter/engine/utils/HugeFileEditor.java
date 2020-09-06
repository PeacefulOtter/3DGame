package peacefulotter.engine.utils;

import java.io.*;

public class HugeFileEditor
{
    public static void main(String[] args)
    {
        File newFile = new File( "/home/peacefulotter/Documents/dev/java/3DGameEngine/res/models/house/house3.obj" );
        try { newFile.createNewFile(); } catch( IOException e ) { e.printStackTrace(); }

        int i = 0;
        try ( BufferedReader br = new BufferedReader( new FileReader( "/home/peacefulotter/Documents/dev/java/3DGameEngine/res/models/house/house.obj" ) );
              BufferedWriter bw = new BufferedWriter( new FileWriter( newFile ) ) )
        {
            String line;
            while( (line=br.readLine()) != null ) {
                if ( i++ > 500_000 )
                {
                    br.close();
                }
                if ( line.startsWith( "mtllib" ) )
                {
                    bw.write( "mtllib house.mtl" );
                }
                else
                {
                    bw.write( line + "\n" );
                }
            }


        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
