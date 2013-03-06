import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
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
  * URL2FTP
  *
  * @version 1.0.0 vom 28.02.2013
  * @author Daniel Ruf
  */

public class url2ftp {
  public static void main(String[] args) throws Exception{
    String version = "1.0.0";
    String program = "URL2FTP";
    System.out.println(program + " " + version );
    String dir ="";
    String user ="";
    String password ="";
    String server ="";
    String url="";
    Properties prop = new Properties();
    File browser_file = new File("browsers.txt");
    try{browser_file.createNewFile();}catch(Exception e){}
    try {
      prop.load(new FileInputStream("url2ftp.properties")); 
      dir = prop.getProperty("ftp_directory");
      user = prop.getProperty("ftp_user");
      password = prop.getProperty("ftp_password");
      server = prop.getProperty("ftp_server");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    try{BufferedReader reader = new BufferedReader(new FileReader("files.txt"));
      int lines = 0;
      while (reader.readLine() != null) lines++;
      reader.close();
      System.out.println("Found "+lines+" files");
      try
      { 
        BufferedReader br = new BufferedReader(new FileReader("files.txt"));
        String line;
        int current_line = 0;
        while ((url = br.readLine()) != null) {
          current_line++;
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
            InputStream in = new URL(url).openStream();
            OutputStream out = new URL("ftp://"+user+":"+password+"@"+server+""+dir+""+filename_real_string).openConnection().getOutputStream();
            String content_length= new URL(url).openConnection().getHeaderField( "content-length" );
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
              System.out.print("\rTransferring "+current_line+" of "+lines+" "+n_perct+"");
            }
            in.close();
            out.close();
          } // end of if-else
          
        }
        br.close();
      }
      catch(Exception e){};  
      System.out.println("");
      System.out.println("Done");  
    }
    catch(Exception e){} 
  } // end of main   
}  // end of class