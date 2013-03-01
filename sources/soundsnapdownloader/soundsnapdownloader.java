/**
  *
  * SoundSnapDownloader
  *
  * @version 1.0.0 vom 23.02.2013
  * @author Daniel Ruf
  */
import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLConnection;
public class soundsnapdownloader {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String version = "1.0.0";
    String program = "SoundSnapDownloader";  
    System.out.println(program + " " + version );
    File browser_file = new File("soundsnapids.txt");
    try{browser_file.createNewFile();}catch(Exception e){}
    try{BufferedReader reader = new BufferedReader(new FileReader("soundsnapids.txt"));
      int lines = 0;
      while (reader.readLine() != null) lines++;
      reader.close();
      System.out.println("Found "+lines+" SoundSnap IDs");
      try
      { 
        BufferedReader br = new BufferedReader(new FileReader("soundsnapids.txt"));
        String id;
        int current_line = 0;
        int i = 0;
        while ((id = br.readLine()) != null) {
          current_line++;          
          HttpURLConnection con = (HttpURLConnection)(new URL( "http://www.soundsnap.com/audio/play/"+id ).openConnection());
          con.setInstanceFollowRedirects( false );
          con.connect();        
          String location = con.getHeaderField( "Location" );
          String[] array = location.split("/");
          String filename_new = array[array.length-1];
          filename_new = URLDecoder.decode(filename_new, "UTF-8");
          URLConnection conn = new URL(location).openConnection();
          String content_length= conn.getHeaderField( "content-length" );
          InputStream is = conn.getInputStream();
          String mp3_filename = id+"_"+filename_new;
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
            System.out.print("\rDownloading track "+i_real+" of "+lines+" "+n_perct+"");
          }
          outstream.close();
          File mp3file = new File(mp3_filename);
          i++;
        }
        br.close();
        System.out.println("");
        System.out.println("Done");
      }
      catch(Exception e){};
    }
    catch(Exception e){}
  } // end of main
} // end of class soundsnapdownloader