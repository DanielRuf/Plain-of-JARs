import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader; 
import java.io.Console;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.lang.String;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
/**
  *
  * ReverbNationDownloader
  *
  * @version 1.0.0 vom 03.03.2013
  * @author Daniel Ruf
  */
  
public class reverbnationdownloader {
  public static void main(String[] args) throws Exception{
    String version = "1.0.0";
    String program = "ReverbNationDownloader";
    System.out.println(program + " " + version );
    String headerName = null, returnCookie = "";
    StringBuilder contents = new StringBuilder(2048);
    BufferedReader br = null;
    String client ="234s3rwas";
    Console console = System.console();
    String reverbnation_artist = console.readLine("Please enter the Artist: ");
    String song_id = console.readLine("Please enter the SongID: ");
    URL url2 = new URL("http://www.reverbnation.com/"+reverbnation_artist.toLowerCase()+"/song/"+song_id+"");    
    br = new BufferedReader(new InputStreamReader(url2.openStream()));
    String line = "";
    while (line != null)
    {
      line = br.readLine();
      contents.append(line);
    }
    br.close();
    String password ="";
    String artist="";
    String song="";
    Pattern p = Pattern.compile("(?i)(.*)(pass: \")(.+?)(\",)(.*)");
    Matcher m = p.matcher(contents.toString());
    if(m.matches()) {
      password =m.group(3);
    }   
    Pattern p2 = Pattern.compile("(?i)(.*)(<span class=\"song_name\">)(.+?)(</span>)(.*)");
    Matcher m2 = p2.matcher(contents.toString());
    if(m2.matches()) {
      song =m2.group(3);
    }
    Pattern p3 = Pattern.compile("(?i)(.*)(<span class=\"artist_name\">By: )(.+?)(</span>)(.*)");
    Matcher m3 = p3.matcher(contents.toString());
    if(m3.matches()) {
      artist =m3.group(3);
    }
    URL url = new URL("http://www.reverbnation.com/audio_player/html_player_stream/"+password+"?client="+client+"&song_id="+song_id+"");
    URLConnection urlConnection = url.openConnection();
    String is_human = "is_human=true";
    urlConnection.setRequestProperty("Cookie", is_human);
    urlConnection.connect(); 
    InputStream is = urlConnection.getInputStream();
    OutputStream outstream = new FileOutputStream(new File(artist+ " - "+song+".mp3"));
    String content_length= urlConnection.getHeaderField( "content-length" );
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
      String n_perct = n+"%    ";
      System.out.print("\rDownloading track "+artist+" - "+song+" " +n_perct+"");
    }
    outstream.close();
    System.out.println("");
    System.out.println("Done"); 
  } // end of main
} // end of class reverbnation