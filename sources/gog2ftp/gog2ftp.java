import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URL;
import java.util.Properties;

/**
  *
  * GOG2FTP
  *
  * @version 1.1.0 vom 06.03.2013
  * @author Daniel Ruf
  */

public class gog2ftp {
  public static void main(String[] args) throws Exception{
    String version = "1.1.0";
    String program = "GOG2FTP";
    System.out.println(program + " " + version );
    String dir ="";
    String user ="";
    String password ="";
    String server ="";
    String url="";
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream("gog2ftp.properties")); 
      dir = prop.getProperty("ftp_directory");
      user = prop.getProperty("ftp_user");
      password = prop.getProperty("ftp_password");
      server = prop.getProperty("ftp_server");
      url = prop.getProperty("url");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    String[] url_array = url.split("/");
    String filename = url_array[url_array.length-1];
    String[] filename_real = filename.split("\\?");
    String filename_real_string = filename_real[0];
    boolean file_exists = false;
    try{
      InputStream url_exists = new URL("ftp://"+user+":"+password+"@"+server+""+dir+""+filename_real_string).openStream(); file_exists=true;
    }  
    catch (Exception ex){}
    if (file_exists) {
      System.out.print("The file already exists on the server");
    }// end of if
    else {
      URLConnection conn = new URL(url).openConnection();
      HttpURLConnection conn2 = (HttpURLConnection)new URL(url).openConnection();
      int response_code= conn2.getResponseCode();
      if (response_code == 403 ) {
        System.out.print("Link expired");
      } // end of if
      else {
        InputStream in = new URL(url).openStream();
        OutputStream out = new URL("ftp://"+user+":"+password+"@"+server+""+dir+""+filename_real_string).openConnection().getOutputStream();
        String content_length= conn.getHeaderField( "content-length" );
        long fileSize = Long.valueOf(content_length).longValue();
        long bytesRead = 0;
        int percentage = -1;
        int r;
        //IOUtils.copy(in, out);
        byte[] buffer = new byte[16384];
        while ((r=in.read(buffer))>=0) {
          out.write(buffer, 0, r);
          bytesRead += r;
          int n = (int)(100*bytesRead/fileSize);
          percentage = n;
          String n_perct = n+"%    ";
          System.out.print("\rTransferring "+filename_real_string+" "+n_perct+"");
        }
        in.close();
        out.close(); 
        }  
      } // end of if-else
      System.out.println("");
      System.out.println("Done");   
    } // end of main   
  }  // end of class