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
import java.io.Console;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v23Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
  *
  * SoundCloudTrackDownloader
  *
  * @version 1.1.0 vom 20.02.2013
  * @author 
  */

public class soundcloudtrackdownloader {
  public static void main(String[] args) throws Exception {
    String version = "1.1.0";
    String program = "SoundCloudTrackDownloader";
    System.out.println(program + " " + version );
    String client_id = "b45b1aa10f1ac2941910a7f0d10f8e28";
    int file_number = 0;
    Console console = System.console();
    String username = console.readLine("Please enter the name of the Soundcloud artist: ");
    URLConnection conn3 = new URL("https://api.soundcloud.com/users/"+username+"/tracks.json?client_id="+client_id).openConnection();
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
    JSONArray tracks = new JSONArray(json_string);
    int tracks_count = tracks.length();
    int i=0;
    while(i<tracks_count)
    {
      file_number++;
      String username_soundcloud = tracks.getJSONObject(i).getJSONObject("user").getString("username");  
      String title_track = tracks.getJSONObject(i).getString("title");
      int id = tracks.getJSONObject(i).getInt("id");
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
      String mp3_filename = file_number+"_"+username_soundcloud+" - "+title_track+".mp3";
      String mp3_id3filename = file_number+"_"+username_soundcloud+" - "+title_track+"_id3.mp3";
      OutputStream outstream = new FileOutputStream(new File(mp3_filename));
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
        System.out.print("\rDownloading track "+i_real+" of "+tracks_count+" "+n_perct+"");
      }
      outstream.close();
      Mp3File mp3file = new Mp3File(mp3_filename);
      ID3v2 id3v2Tag;
      if (mp3file.hasId3v2Tag()) {
        id3v2Tag = mp3file.getId3v2Tag();
      } else {
        // mp3 does not have an ID3v2 tag, let's create one..
        id3v2Tag = new ID3v23Tag();
        mp3file.setId3v2Tag(id3v2Tag);
      }
      id3v2Tag.setArtist(username_soundcloud);
      id3v2Tag.setTitle(title_track);
      //id3v2Tag.setAlbum("The Album");
      id3v2Tag.setYear(tracks.getJSONObject(i).get("release_year").toString());
      id3v2Tag.setComment(tracks.getJSONObject(i).getString("description"));
      mp3file.save(mp3_id3filename);
      File file_id3 = new File(mp3_id3filename);
      File old_mp3file = new File(mp3_filename);
      old_mp3file.delete();
      file_id3.renameTo(new File(mp3_filename));
      i++;
    } 
    System.out.println("");
    System.out.println("Done"); 
  } // end of main
} // end of class soundcloud
