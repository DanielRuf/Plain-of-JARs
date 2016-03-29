import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.sql.*;
import org.h2.tools.*;
import java.net.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
  *
  * Dumper
  *
  * @version 1.0 vom 27.05.2014
  * @author Daniel Ruf
  */

public class dumper {
  
  public static void main(String[] args) throws Exception {
    //java.security.Security.setProperty("networkaddress.cache.ttl" , "0");
    //java.security.Security.setProperty("networkaddress.cache.negative.ttl" , "0");
    try{
      String version = "1.0.0";
      String program = "Dumper";
      System.out.println(program + " " + version );
      String domain ="homepage-baukasten.de";
      String ending =".de.tl";
      Class.forName("org.h2.Driver");
      Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override 
      public void run() {
      try {
      Connection conn = DriverManager.getConnection("jdbc:h2:./test;MODE=MySQL;MV_STORE=FALSE;MVCC=FALSE;MAX_COMPACT_TIME=2000", "sa", "");
      conn.createStatement().execute("SHUTDOWN COMPACT");
      conn.close();
      } catch(Exception e) {
      
      }
      
      }
      });
      Connection conn = DriverManager.getConnection("jdbc:h2:./test;MODE=MySQL;MV_STORE=FALSE;MVCC=FALSE;MAX_COMPACT_TIME=2000", "sa", "");
      conn.createStatement().execute("CREATE TABLE IF NOT EXISTS WEBSITES(ID BIGINT auto_increment, NAME VARCHAR)");
      conn.createStatement().execute("ALTER TABLE WEBSITES ADD CONSTRAINT IF NOT EXISTS NAME_UNIQUE UNIQUE(NAME)");
      conn.createStatement().execute("ALTER TABLE WEBSITES DROP COLUMN IF EXISTS ID");
      conn.createStatement().execute("ALTER TABLE WEBSITES ADD COLUMN IF NOT EXISTS ID BIGINT auto_increment BEFORE NAME");
      conn.createStatement().execute("SHUTDOWN COMPACT");
      conn.close();
      int dataset=0;
      String padding=""; 
      while (true) { 
        try {
          //getPages("http://www.homepage-baukasten.de/forum/viewonline.php", "div.forum_main a.gen", conn);
          DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
          Date date = new Date();
          int websites = getPages("http://www."+domain+"/forum/viewonline.php", "div.forum_main span.gen a", domain, ending);
          if (websites>0) {
            dataset++;
            if (dataset<10) {
              padding="0000";
              }
            else if(dataset<100){
              padding="000";
              }
            else if(dataset<1000){
              padding="00";
              }
            else if(dataset<10000){
              padding="0";
              }
            else {
              padding="";
            } // end of if-else
            // end of if
            System.out.println(padding+""+dataset+"\t"+dateFormat.format(date)+"("+System.currentTimeMillis() / 1000+")\tAdded "+websites+" website(s)");  
          } // end of if
          Thread.sleep(1000*5);
        } catch(InterruptedException ex) {
          Thread.currentThread().interrupt();
        }
        
      } // end of while
    }
    catch(Exception e) {
      System.out.println(e);
    }
    //conn.close();
    
  } // end of main
  //private static void getPages(String url, String selector, Connection conn) {
  private static int getPages(String url, String selector, String domain, String ending) {
    try {
      Document doc = Jsoup.connect(url).get();
      Elements elements = doc.select(selector);
      int websites = 0;
      for (Element element : elements) {
        if (element.attr("abs:href").toString().contains("http://www."+domain+"/profile.php?of=")) {
          Connection conn = DriverManager.getConnection("jdbc:h2:./test;MODE=MySQL;MV_STORE=FALSE;MVCC=FALSE;MAX_COMPACT_TIME=2000", "sa", "");          
          String test = element.attr("abs:href").toString().replace("http://www."+domain+"/profile.php?of=","");
          //String[] parts = test.split("&sid=");
          String website = test+""+ending;
          website = website.toLowerCase();
          ResultSet records = conn.createStatement().executeQuery("SELECT COUNT(ID) FROM WEBSITES WHERE NAME = '"+website+"'");
          records.next();
          boolean recordExists = records.getInt(1)!=0;
          if(!recordExists){
            conn.createStatement().executeUpdate("INSERT INTO WEBSITES(NAME) VALUES('"+website+"')");
            websites++;  
          } // end of if
          conn.close();
        } // end of if
      }
      return websites;
    } catch(Exception e) {
      System.out.println(e.getMessage());
      return 0;
    }
  }
} // end of class dumper
