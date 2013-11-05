/**
  *
  * CSV2JSON
  *
  * @version 2.0.0 vom 05.11.2012
  * @author Daniel Ruf 
  */
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class csv2json {
  public static void main(String[] args) throws Exception {
    String path2 = ".";
    File directory = new File(path2);    
    File[] myarray;
    String version = "2.0.0";
    String program = "CSV2JSON";  
    System.out.println(program + " " + version );
    myarray=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return dir.toString().endsWith(".csv") && dir.isFile();
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
      CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
      CsvMapper csvMapper = new CsvMapper();
      List data = csvMapper.reader(Map.class).with(bootstrap).readValues(path).readAll();
      ObjectMapper mapper = new ObjectMapper();
      //mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
      //mapper.writeValue(file, data);
      mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path2 + "/"+filename+".csv.json"), data);
    }
    System.out.println("");
    System.out.println("Done");
  }
}