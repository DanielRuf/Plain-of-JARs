/**
  *
  * XSQL
  *
  * @version 1.0.0 vom 22.05.2014
  * @author Daniel Ruf 
  */
import java.sql.*;
import org.h2.tools.*;
public class xsql {
  public static void main(String[] a) throws Exception {
    String version = "1.0.0";
    String program = "XSQL";  
    System.out.println(program + " " + version );
    Class.forName("org.h2.Driver");
    Connection conn = DriverManager.getConnection("jdbc:h2:./test;MODE=MySQL;MV_STORE=FALSE;MVCC=FALSE", "sa", "");
    //conn.createStatement().executeUpdate("DROP TABLE IF EXISTS TEST");
    //conn.createStatement().executeUpdate("CREATE TABLE TEST(ID INT auto_increment, NAME VARCHAR)");
    //conn.createStatement().executeUpdate("INSERT INTO TEST(NAME) VALUES('Hello'), ('World')");
    
    conn.createStatement().execute("DROP TABLE IF EXISTS TEST");
    conn.createStatement().execute("CREATE TABLE TEST(ID INT auto_increment, NAME VARCHAR)");
    conn.createStatement().execute("INSERT INTO TEST(NAME) VALUES('Hello'), ('World')");
    
    //Script.execute("jdbc:h2:test;MODE=MySQL", "sa", "", "export.sql");
    //DeleteDbFiles.execute(".", null, true);
    //RunScript.execute("jdbc:h2:test;MODE=MySQL", "sa", "", "export.sql", null, false);
    ResultSet rs = conn.createStatement().executeQuery("SELECT ID, NAME FROM TEST");
    ResultSetMetaData meta = rs.getMetaData();
    int columns = meta.getColumnCount();
    for (int i=1; i<=columns; i++) {
      System.out.print(meta.getColumnName(i)+" ");
    } // end of for
    System.out.println("\r");
    while(rs.next())
    {
      for (int i=1; i<=columns; i++) {
        System.out.print(rs.getString(i)+" ");
      } // end of for
      System.out.println("\r");
    }
    rs.close();
    conn.close();
  }
}