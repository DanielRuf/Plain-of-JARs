import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
public class csv2json {
  public static void main(String args[]) throws Exception {
    String path2 = System.getProperty("user.dir");
    File directory = new File(path2);    
    File[] myarray;  
    
    myarray=directory.listFiles();
    for (int j = 0; j < myarray.length; j++)
    {
      if (myarray[j].isFile() && myarray[j].toString().endsWith(".csv")) {
        File path=myarray[j];
        String path_current = path.toString();      
        String content = readLines(path_current); 
        JSONArray jsonObject = CDL.toJSONArray(content);
        String json_data = jsonObject.toString(1);
        int index = path.getName().lastIndexOf('.');
        String filename = path.getName().substring(0, index);  
        FileWriter fstream = new FileWriter(path2 + "/"+filename+".csv.json");
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(json_data);
        out.close();
      }
    }
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