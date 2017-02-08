/**
  *
  * H2SQLExport
  *
* @version 1.0 vom 25.06.2015
  * @author Daniel Ruf
  */
import java.sql.*;
import org.h2.tools.*;
    
public class h2sqlexport {
  
  public static void main(String[] args) throws Exception {
    String version = "1.0.0";
    String program = "H2SQLExport";
    System.out.println(program + " " + version );
    h2sqlexport test = new h2sqlexport();
    test.compact();
    System.out.println("");
    System.out.println("Done");
  }  
  public static void compact() throws Exception {
    String url = "jdbc:h2:./test;MODE=MySQL;MV_STORE=FALSE;MVCC=FALSE";
    String file = "test.sql";
    Script.execute(url, "sa", "", file);
    DeleteDbFiles.execute("./", "test", true);
    RunScript.execute(url, "sa", "", file, null, false);
  }
}  
