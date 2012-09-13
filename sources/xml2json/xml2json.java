/**
  *
  * XML2JSON
  *
  * @version 1.1.1 vom 13.09.2012
  * @Daniel Ruf 
  */

import org.json.JSONException;                    
import org.json.JSONObject;
import org.json.XML;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
public class xml2json {  
  public static void main(String args[]) throws Exception {
    String path2 = ".";
    File directory = new File(path2);   
    File[] myarray;  
    String version = "1.1.1";
    String program = "XML2JSON";
    System.out.println(program + " " + version );
    myarray=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return dir.toString().endsWith(".xml") && dir.isFile();
      }
    });
    System.out.println("Found " + myarray.length + " file(s)");
    
    for (int j = 0; j < myarray.length; j++)
    { 
      int file_number = j+1;
      System.out.print("\rProcessing file " + file_number + " of " + myarray.length);
      File path=myarray[j];
      String path_current = path.toString();      
      String content = readLines(path_current); 
      JSONObject jsonObject = XML.toJSONObject(content);
      String json_data = jsonObject.toString(1);
      int index = path.getName().lastIndexOf('.');
      String filename = path.getName().substring(0, index);  
      FileWriter fstream = new FileWriter(path2 + "/"+filename+".xml.json");
      BufferedWriter out = new BufferedWriter(fstream);
      out.write(json_data);
      out.close();
    }
    System.out.println("");
    System.out.println("Done");
  }
  public static String readLines(String aFile) throws IOException {
    StringBuilder contents = new StringBuilder();
    try {
      BufferedReader input =  new BufferedReader(new FileReader(aFile));
      try {
        String line = null;
        while (( line = input.readLine()) != null){contents.append(line); contents.append("\n");}
      }
      finally {input.close();}
    }
    catch (IOException ex){
      ex.printStackTrace();
    }
    return contents.toString();
  }
}