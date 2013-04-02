import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URL;
import java.util.Properties;
import org.apache.commons.net.ftp.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.util.*;
import org.apache.commons.net.*;
import java.util.*;
/**
  *
  * WebsiteBackup
  *
  * @version 1.1.0 vom 02.04.2013
  * @author Daniel Ruf
  */
public class websitebackup {
  public static void main(String[] args) throws Exception{
    String version = "1.1.0";
    String program = "WebsiteBackup";
    System.out.println(program + " " + version );
    String dir ="";
    String username ="";
    String password ="";
    String server ="";
    String db_host ="";
    String db_user ="";
    String db_password ="";
    String db_name ="";
    String memory_limit ="";
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream("backup.properties")); 
      dir = prop.getProperty("ftp_directory");
      username = prop.getProperty("ftp_user");
      password = prop.getProperty("ftp_password");
      server = prop.getProperty("ftp_server");
      db_host = prop.getProperty("db_host");
      db_user = prop.getProperty("db_user");
      db_password = prop.getProperty("db_password");
      db_name = prop.getProperty("db_name");
      memory_limit = prop.getProperty("memory_limit");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    FTPClient client = new FTPClient();
    InputStream fis = null;
    client.connect(server);
    client.login(username, password);
    client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
    client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
    client.type(FTP.BINARY_FILE_TYPE);
    //System.out.print(client.getReplyString());
    client.changeWorkingDirectory(dir);
    System.out.print("\rExtracting ...");
    InputStream generated_file = websitebackup.class.getResourceAsStream("jar_files/backup.php");
    String generated_string = convertStreamToString(generated_file);
    generated_string = generated_string.replace("#db_host#", db_host);
    generated_string = generated_string.replace("#db_user#", db_user);
    generated_string = generated_string.replace("#db_password#", db_password);
    generated_string = generated_string.replace("#db_name#", db_name);
    generated_string = generated_string.replace("#memory_limit#", memory_limit);
    FileWriter fileWriter = new FileWriter ("backup.php");
    BufferedWriter bufferedWriter = new BufferedWriter (fileWriter);
    bufferedWriter.write (generated_string);
    bufferedWriter.close ();
    fis = new FileInputStream("backup.php");
    System.out.print("\rUploading ...");
    client.storeFile("backup.php", fis);
    client.makeDirectory("backup");
    System.out.print("\rSetting chmod rights ...");
    client.sendSiteCommand("chmod " + "777" + " backup.php");
    client.sendSiteCommand("chmod " + "777" + " backup");
    System.out.print("\rBacking up ...          ");
    try {
      InputStream startBackup = new URL("http://"+server+""+dir+"/backup.php").openStream(); 
      backupFinished(server, dir, username, password);
      System.out.print("\rDownloading backup ...  ");
      FileOutputStream fos = new FileOutputStream("backup.zip");
      client.retrieveFile("backup/backup.zip", fos);
      fos.close();  
    }
    catch(java.net.SocketException e) {
      System.out.println("\rPlease set the memory_limit to a higher value and try again");
    }
    fis.close();
    System.out.print("\rCleaning up ...         "); 
    File f = new File("backup.php");
    f.delete();
    client.deleteFile("backup.php");
    client.deleteFile("backup.sql");
    client.deleteFile("backup/backup.zip");
    client.deleteFile("backup/backup_finished.txt");
    client.removeDirectory("backup");
    //client.completePendingCommand();
    client.logout();
    client.disconnect();
    System.out.println("");
    System.out.println("Done");   
  } // end of main 
  public static String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }
  public static boolean backupFinished(String server, String dir, String username, String password) throws Exception{
    try {
      HttpURLConnection conn2 = (HttpURLConnection)new URL("http://"+server+""+dir+"/backup/backup_finished.txt").openConnection();
      int response_code= conn2.getResponseCode();
      if (response_code == 404 ) {
        return false;
      } // end of if
      else {
        return true;  
      } // end of if-else
    }
    catch (Exception e){
      return false;
    }
  }  
}  // end of class