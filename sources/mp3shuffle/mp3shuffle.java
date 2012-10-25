/**
  *
  * MP3SHUFFLE
  *
  * @version 1.1.0 vom 25.10.2012
  * @Daniel Ruf 
  */
import java.io.IOException;
import java.io.*;
import java.lang.String;
import java.io.File;
import java.util.Collections;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Date;
public class mp3shuffle {
  public static void main(String args[]) throws Exception {
    String path2 = ".";
    File directory = new File(path2);    
    File[] myarray;
    String version = "1.1.0";
    String program = "MP3SHUFFLE";  
    System.out.println(program + " " + version );
    myarray=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return dir.toString().endsWith(".mp3") && dir.isFile();
      }
    });
    System.out.println("Found " + myarray.length + " file(s)");
    ArrayList<Integer> numbers = new ArrayList<Integer>();
    ArrayList<Long> dates = new ArrayList<Long>();
    for(int i = 0; i < myarray.length; i++)
    {
      numbers.add(i+1);
      dates.add(new Date().getTime());
    }
    Collections.shuffle(numbers); 
    for (int j = 0; j < myarray.length; j++)
    {
      int file_number = j+1;
      System.out.print("\rProcessing file " + file_number + " of " + myarray.length);
      File path=myarray[j];
      path.setLastModified(dates.get(j));
      String path_current = path.toString();  
      UUID timestamp = UUID.randomUUID();      
      String new_filename = numbers.get(j) + "_" + timestamp + ".mp3"; 
      path.renameTo(new File (new_filename));  
    }
    System.out.println("");
    System.out.println("Done");
  }
}