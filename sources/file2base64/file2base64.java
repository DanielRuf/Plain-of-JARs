/**
  *
  * File2Base64
  *
  * @version 1.0.0 vom 20.01.2013
  * @author Daniel Ruf 
  */
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import org.apache.commons.codec.binary.Base64;
public class file2base64 {
  public static void main(String[] args) throws Exception{
    String path2 = ".";
    File directory = new File(path2);   
    File[] myarray;  
    String version = "1.0.0";
    String program = "File2Base64";
    System.out.println(program + " " + version );
    myarray=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return !dir.toString().endsWith(".base64") && !dir.toString().endsWith(".java") && !dir.toString().endsWith(".jar") && !dir.toString().endsWith(".class") && !dir.toString().endsWith(".~ava") && dir.isFile();
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
      InputStream is = new FileInputStream(file);
      long length = file.length();
      byte[] bytes = new byte[(int)length];
      int offset = 0;
      int numRead = 0;
      while (offset < bytes.length
      && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {offset += numRead;}
      is.close();
      String encodedString = Base64.encodeBase64String(bytes);
      Writer output = null;
      String filename = path.getName();
      File file2 = new File(path2 + "/"+filename+".base64");
      output = new BufferedWriter(new FileWriter(file2));
      output.write(encodedString);
      output.close();
    }
    System.out.println("");
    System.out.println("Done");
  }
} // end of class base64
