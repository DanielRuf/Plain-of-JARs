/**
  *
  * AnubisScan
  *
  * @version 1.0.0 vom 28.02.2015
  * @author Daniel Ruf 
  */
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
public class anubisscan {
  public static void main(String[] args) throws Exception{
    String path2 = "."; 
    String version = "1.0.0";
    String program = "AnubisScan";
    System.out.println(program + " " + version );
    String jarfile = "";
    String pyfile = "";
    String password = "";
    String user = "";
    String email = "";
    String directory =".";
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream("config.properties")); 
      jarfile = prop.getProperty("jarfile");
      pyfile = prop.getProperty("pyfile");
      password = prop.getProperty("password");
      user = prop.getProperty("user");
      email = prop.getProperty("email");
      directory = prop.getProperty("directory");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    Runtime rt = Runtime.getRuntime();
    Process pr = rt.exec("java -jar "+jarfile+" "+pyfile+" --keep-files=ALL --force-analysis --password="+password+" --user="+user+" --recursive "+directory+" --email="+email+"");
    System.out.println("");
    System.out.println("Done");
  }
} 