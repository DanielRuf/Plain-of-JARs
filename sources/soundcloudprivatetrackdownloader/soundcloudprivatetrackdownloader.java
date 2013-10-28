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
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.InetSocketAddress;
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
  * SoundCloudPrivateTrackDownloader
  *
  * @version 1.0.0 vom 28.10.2013
  * @author Daniel Ruf
  */

public class soundcloudprivatetrackdownloader {
  public static void main(String[] args) throws Exception {
    String version = "1.0.0";
    String program = "SoundCloudPrivateTrackDownloader";
    System.out.println(program + " " + version );
    String client_id = "b45b1aa10f1ac2941910a7f0d10f8e28";
    int file_number = 0;
    Console console = System.console();
    String proxy_server = console.readLine("Please enter the IP of the proxy server (optional): ");
    String proxy_port = console.readLine("Please enter the port of the proxy server (optional): ");
    String track_id = console.readLine("Please enter the ID of the Soundcloud track: ");
    String secret_token = console.readLine("Please enter the secret token of the Soundcloud track: ");
    HttpURLConnection conn4 = (HttpURLConnection)(new URL("https://api.soundcloud.com/tracks/"+track_id+".json?secret_token="+secret_token+"&client_id="+client_id).openConnection());
    conn4.setConnectTimeout(60000);
    conn4.setReadTimeout(60000);
    conn4.connect();
    InputStream in = conn4.getInputStream();
    InputStreamReader is3 = new InputStreamReader(in);
    StringBuilder sb2=new StringBuilder();
    BufferedReader br2 = new BufferedReader(is3);
    String read2 = br2.readLine();
    while(read2 != null) {
      sb2.append(read2);
      read2 =br2.readLine();
    }
    String json_string = sb2.toString();
    JSONObject track = new JSONObject(json_string);
    int i=0;
    int tracks_count =1;
    file_number++;
    String username_soundcloud = track.getJSONObject("user").getString("username");  
    String title_track = track.getString("title");
    String title_track_original = title_track;
    title_track = title_track.replaceAll("\\\\", "-");
    title_track = title_track.replaceAll("/", "-");
    title_track = title_track.replaceAll(":", "-");
    title_track = title_track.replaceAll("\\*", "-");
    title_track = title_track.replaceAll("\\?", "");
    title_track = title_track.replaceAll("'", "");
    title_track = title_track.replaceAll("\"", "");
    int id = track.getInt("id");
    HttpURLConnection con = null;
    con = (HttpURLConnection)(new URL("https://api.soundcloud.com/tracks/"+id+"/stream?secret_token="+secret_token+"&client_id="+client_id+"&format=json").openConnection());
    con.setInstanceFollowRedirects( false );
    con.setConnectTimeout(60000);
    con.setReadTimeout(60000);
    con.connect();
    if (con.getResponseCode() == 401) {
      if (proxy_server != null && proxy_port != null && !proxy_server.isEmpty() && !proxy_port.isEmpty()) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy_server, Integer.parseInt(proxy_port)));
        try {con = (HttpURLConnection)(new URL("https://api.soundcloud.com/tracks/"+id+"/stream?secret_token="+secret_token+"&client_id="+client_id+"&format=json").openConnection(proxy));}
        catch (Exception ex) {}
      }
      con.setInstanceFollowRedirects( false );
      con.setConnectTimeout(60000);
      con.setReadTimeout(60000);
      con.connect();
    }
    String location = con.getHeaderField( "Location" );
    con.disconnect();
    try {
      String content_length= null;
      InputStream is = null;
      HttpURLConnection conn = (HttpURLConnection)(new URL(location).openConnection());
      conn.setConnectTimeout(60000);
      conn.setReadTimeout(60000);
      conn.connect();
      content_length= conn.getHeaderField( "content-length" );
      is = conn.getInputStream();   
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
        id3v2Tag = new ID3v23Tag();
        mp3file.setId3v2Tag(id3v2Tag);
      }
      id3v2Tag.setArtist(username_soundcloud);
      id3v2Tag.setTitle(title_track_original);
      if (!track.isNull("release_year")){
        id3v2Tag.setYear(track.get("release_year").toString());
      }
      if (!track.isNull("description")){
        id3v2Tag.setComment(track.getString("description"));
      }
      mp3file.save(mp3_id3filename);
      File file_id3 = new File(mp3_id3filename);
      File old_mp3file = new File(mp3_filename);
      old_mp3file.delete();
      file_id3.renameTo(new File(mp3_filename));
    }
    catch (Exception ex) {} 
    System.out.println("");
    System.out.println("Done"); 
  } // end of main
} // end of class soundcloudprivatetrackdownloader