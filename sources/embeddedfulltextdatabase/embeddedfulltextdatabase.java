/**
  *
  * EmbeddedFulltextDatabase
  *
  * @version 1.0.0 vom 31.08.2014
  * @author Daniel Ruf
  */
import java.sql.*;
import org.h2.tools.*;
import org.apache.lucene.*;
import org.h2.fulltext.FullTextLucene;
public class embeddedfulltextdatabase {
  
  public static void main(String[] args) throws Exception {
    String version = "1.0.0";
    String program = "EmbeddedFulltextDatabase";
    System.out.println(program + " " + version );
    Class.forName("org.h2.Driver");
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override 
      public void run() {
        try {
          Connection conn = DriverManager.getConnection("jdbc:h2:./test;MODE=MySQL;MV_STORE=FALSE;MVCC=FALSE;MAX_COMPACT_TIME=2000", "sa", "");
          conn.createStatement().execute("DROP ALL OBJECTS DELETE FILES;");
          conn.createStatement().execute("SHUTDOWN COMPACT");
          conn.close();
        } catch(Exception e) {
          
        }
        
      }
    });
    
    Connection conn = DriverManager.getConnection("jdbc:h2:./test;MODE=MySQL;MV_STORE=FALSE;MVCC=FALSE;MAX_COMPACT_TIME=2000", "sa", "");
    conn.createStatement().execute("CREATE ALIAS IF NOT EXISTS FTL_INIT FOR \"org.h2.fulltext.FullTextLucene.init\";");
    conn.createStatement().execute("CALL FTL_INIT();");
    conn.createStatement().execute("CREATE TABLE IF NOT EXISTS TEST(ID INT PRIMARY KEY, NAME VARCHAR);");
    conn.createStatement().execute("INSERT INTO TEST SELECT * FROM ( SELECT 1, 'Hello World') x WHERE NOT EXISTS (SELECT * FROM TEST);");
    conn.createStatement().execute("CALL FTL_CREATE_INDEX('PUBLIC', 'TEST', NULL);");
    ResultSet rs = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT T.*, SCORE FROM FTL_SEARCH_DATA('Hello World', 0, 0) FT, TEST T WHERE FT.TABLE='TEST' AND T.ID=FT.KEYS[0];");
    //ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM FTL_SEARCH('Hello 123', 0, 0);");
    //ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM TEST;");
    ResultSetMetaData meta = rs.getMetaData();
    conn.createStatement().execute("CALL FTL_DROP_ALL();");
    int columns = meta.getColumnCount();
    int[] len = new int[columns];
    for (int i=1; i<=columns; i++) {
      if (meta.getColumnName(i).length()>len[i-1])len[i-1]=meta.getColumnName(i).length()+5;
    } // end of for
    
    while(rs.next())
    {
      for (int i=1; i<=columns; i++) {
        if (rs.getString(i).length()>len[i-1])len[i-1]=rs.getString(i).length()+5;
      }
    }
    rs.beforeFirst();
    for (int i=1; i<=columns; i++) {
      System.out.format("%-"+len[i-1]+"s",meta.getColumnName(i));
    } // end of for
    System.out.println("\r");
    while(rs.next())
    {
      for (int i=1; i<=columns; i++) { 
        if (meta.getColumnName(i).equals("SCORE")) {
          System.out.format("%-"+len[i-1]+"s",rs.getString(i)); 
        } // end of if
        else {
          System.out.format("%-"+len[i-1]+"s",rs.getString(i)); 
        } // end of if-else
      } // end of for
      System.out.println("\r");
    }
    rs.close();
    conn.createStatement().execute("DROP ALL OBJECTS DELETE FILES;");
    conn.createStatement().execute("SHUTDOWN COMPACT");
    conn.close();
  } // end of main
  
} // end of class database
