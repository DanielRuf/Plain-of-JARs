import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.*;
import java.io.File;
import java.io.IOException;
import java.io.Console;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.String.*;   
import org.json.JSONException;

/**
  *
  * SoundCloudSetDownloader
  *
  * @version 1.0.0 vom 15.02.2013
  * @author 
  */

public class soundcloudsetdownloader {
  public static void main(String[] args) throws Exception {
    String version = "1.0.0";
    String program = "SoundCloudSetDownloader";
    System.out.println(program + " " + version );
    String client_id = "b45b1aa10f1ac2941910a7f0d10f8e28";
    Console console = System.console();
    String username = console.readLine("Please enter the name of the Soundcloud artist: ");
    URLConnection conn3 = new URL("https://api.soundcloud.com/users/"+username+"/playlists.json?client_id="+client_id).openConnection();
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
    JSONArray playlists = new JSONArray(json_string);
    int playlists_count = playlists.length();
    int i=0;
    while(i<playlists_count)
    {
      JSONArray tracks = playlists.getJSONObject(i).getJSONArray("tracks");
      String playlist_name = playlists.getJSONObject(i).getString("title");
      int tracks_count = tracks.length();
      int j =0;
      while(j<tracks_count)
      { 
        String username_soundcloud = tracks.getJSONObject(j).getJSONObject("user").getString("username");  
        String title_track = tracks.getJSONObject(j).getString("title");
        int id = tracks.getJSONObject(j).getInt("id");
        HttpURLConnection con = (HttpURLConnection)(new URL( "https://api.soundcloud.com/tracks/"+id+"/stream?client_id="+client_id+"" ).openConnection());
        con.setInstanceFollowRedirects( false );
        con.connect();
        //int responseCode = con.getResponseCode();
        //System.out.println( responseCode );
        String location = con.getHeaderField( "Location" );
        URLConnection conn = new URL(location).openConnection();
        //int file_size = conn.getContentLength();
        String content_length= conn.getHeaderField( "content-length" );
        InputStream is = conn.getInputStream();
        OutputStream outstream = new FileOutputStream(new File(""+username_soundcloud+" - "+playlist_name+" - "+title_track+".mp3"));
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
          int j_real = j+1;
          int i_real = i+1;
          String n_perct = n+"%    ";
          System.out.print("\rDownloading track "+j_real+" of "+tracks_count+" from playlist "+i_real+" of "+playlists_count+" "+n_perct+"");
        }
        outstream.close();
        j++;
      }
      i++;
    } 
    System.out.println("");
    System.out.println("Done"); 
  } // end of main
} // end of class soundcloud
