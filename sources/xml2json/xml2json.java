/**
  *
  * XML2JSON
  *
  * @version 2.0.0 vom 05.11.2013
  * @author Daniel Ruf 
  */
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class xml2json {
  public static void main(String[] args) throws Exception {
    String path2 = ".";
    File directory = new File(path2);    
    File[] myarray;
    String version = "2.0.0";
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
      int index = path.getName().lastIndexOf('.');
      String filename = path.getName().substring(0, index);  
      XmlMapper xmlMapper = new XmlMapper();
      List entries = xmlMapper.readValue(path, List.class);
      ObjectMapper jsonMapper = new ObjectMapper();
      jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
      jsonMapper.writeValue(new File(path2 + "/"+filename+".xml.json"), entries);
      //mapper.writeValue(file, data);
      //jsonMapper.writerWithDefaultPrettyPrinter().writeValue(new File ("input.json"), data); 
    }
    System.out.println("");
    System.out.println("Done");
  }
} // end of class xml2json