/**
  *
  * File2Zip
  *
  * @version 1.0.0 vom 28.02.2015
  * @author Daniel Ruf 
  */
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
public class file2zip {
  public static void main(String[] args) throws Exception{
    String path2 = ".";
    File[] myarray;  
    String version = "1.0.0";
    String program = "File2Zip";
    System.out.println(program + " " + version );
    File directory = new File(path2);
    String packer = "";
    String zip_directory =".";
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream("config.properties")); 
      packer = prop.getProperty("packer_path");
      zip_directory = prop.getProperty("zip_directory");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    myarray=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return !dir.toString().endsWith(".zip") && !dir.toString().endsWith(".java") && !dir.toString().endsWith(".properties") && !dir.toString().endsWith(".jar") && !dir.toString().endsWith(".class") && !dir.toString().endsWith(".~ava") && dir.isFile();
      }
    });
    System.out.println("Found " + myarray.length + " file(s)");
    for (int j = 0; j < myarray.length; j++)
    { 
      int file_number = j+1;
      System.out.print("\rProcessing file " + file_number + " of " + myarray.length);
      File path=myarray[j];
      String path_current = path.toString();
      File file = new File(path_current);
      String filename = path.getName();
      Runtime rt = Runtime.getRuntime();
      Process pr = rt.exec(packer+" a -pinfected -tzip "+zip_directory+"/"+filename+".zip "+file);
      //Process pr = rt.exec(packer+" a -pinfected -t7z "+zip_directory+"/"+filename+".7z "+file);
    }
    System.out.println("");
    System.out.println("Done");
  }
} 