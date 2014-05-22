/**
  *
  * XSQLite
  *
  * @version 1.0.0 vom 22.05.2014
  * @author Daniel Ruf 
  */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

public class xsqlite
{
  public static void main(String[] args) throws ClassNotFoundException
  {
    String version = "1.0.0";
    String program = "XSQLite";  
    System.out.println(program + " " + version );
    
    // load the sqlite-JDBC driver using the current class loader
    Class.forName("org.sqlite.JDBC");
    
    Connection connection = null;
    try
    {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      
      statement.executeUpdate("drop table if exists person");
      statement.executeUpdate("create table person (id integer primary key, name string)");
      statement.executeUpdate("insert into person(name) values('leo')");
      statement.executeUpdate("insert into person(name) values('yui')");
      ResultSet rs = statement.executeQuery("select * from person");
      ResultSetMetaData rsmd = rs.getMetaData();
      /*
      stmt.executeUpdate("backup to backup.db");
      Restore the database from a backup file:
      // Create a memory database
      Connection conn = DriverManager.getConnection("jdbc:sqlite:");
      // Restore the database from a backup file
      Statement stat = conn.createStatement();
      stat.executeUpdate("restore from backup.db");
      */
      int columns = rsmd.getColumnCount();
      for (int i=1; i<=columns; i++) {
        System.out.print(rsmd.getColumnName(i)+" ");
      } // end of for
      System.out.println("\r");
      while(rs.next())
      {
        for (int i=1; i<=columns; i++) {
          System.out.print(rs.getString(i)+" ");
        } // end of for
        System.out.println("\r");
      }
    }
    catch(SQLException e)
    {
      // if the error message is "out of memory", 
      // it probably means no database file is found
      System.err.println(e.getMessage());
    }
    finally
    {
      try
      {
        if(connection != null)
        connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }
  }
}