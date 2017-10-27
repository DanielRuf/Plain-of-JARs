import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

/**
  *
  * FontSquirrelDownloader
  *
  * @version 1.0.1 vom 27.10.2017
  * @author Daniel Ruf
  */

public class fontsquirreldownloader {
  public static void main(String[] args) throws Exception {
    String version = "1.0.1";
    String program = "FontSquirrelDownloader";
    System.out.println(program + " " + version );
    URLConnection conn3 = new URL("https://www.fontsquirrel.com/api/fontlist/all").openConnection();
    InputStream in = conn3.getInputStream();
    InputStreamReader is2 = new InputStreamReader(in);
    StringBuilder sb=new StringBuilder();
    BufferedReader br = new BufferedReader(is2);
    String read = br.readLine();
    while(read != null) {
      sb.append(read);
      read =br.readLine();
    }
    String json_string = sb.toString();
    JSONArray fonts = new JSONArray(json_string);
    int fontfacekits_count = fonts.length();
    int i=0;
    while(i<fontfacekits_count)
      {
      String fontname = fonts.getJSONObject(i).getString("family_urlname");
      HttpURLConnection con = (HttpURLConnection)(new URL( "https://www.fontsquirrel.com/fontfacekit/"+fontname+"" ).openConnection());
      con.setInstanceFollowRedirects( false );
      con.connect();
      String content_length= con.getHeaderField( "content-length" );
      InputStream is = con.getInputStream();
      String fontfacekit_filename = fontname+"-fontfacekit.zip";
      OutputStream outstream = new FileOutputStream(new File(fontfacekit_filename));
      long fileSize = Long.valueOf(content_length).longValue();
      long bytesRead = 0;
      int percentage = -1;
      byte[] buffer = new byte[4096];
      int len;
      while ((len = is.read(buffer)) > 0) {
        outstream.write(buffer, 0, len);
        bytesRead += len;
        int n = (int)(100*bytesRead/fileSize);
        percentage = n;
        int i_real = i+1;
        String n_perct = n+"%    ";
        System.out.print("\rDownloading fontfacekit "+i_real+" of "+fontfacekits_count+" "+n_perct+"");
      }
      outstream.close();
      i++;
    } 
    System.out.println("");
    System.out.println("Done"); 
  } // end of main
} // end of class
