/**
  *
  * Webcomic2PDF
  *
  * @version 1.0.1 vom 27.03.2014
  * @author Daniel Ruf
  */
import org.jsoup.*;
import java.util.logging.*;
import org.jsoup.parser.*;
import org.jsoup.helper.*;
import org.jsoup.select.*;
import org.jsoup.nodes.*;
import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;
import org.apache.pdfbox.pdmodel.edit.*;
import org.apache.pdfbox.pdmodel.graphics.xobject.*;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;
public class webcomic2pdf {
  public static void main(String[] args) throws Exception {
    String version = "1.0.1";
    String program = "Webcomic2PDF";
    System.out.println(program + " " + version );
    File directory = new File("pages");
    if (!directory.exists()) {
      directory.mkdir();  
    }
    else {
      File[] files = directory.listFiles();
      if(files!=null){
        for(int i=0; i<files.length; i++) {
          files[i].delete();
        }
      }
    } // end of if-else    
    try {
      int pages_count = 0;
      int current_page = 1;
      int i = 1;
      int current_pages_count = 0;
      System.out.println("");
      System.out.println("List of all supported comics:");
      ObjectMapper mapper = new ObjectMapper();
      BufferedReader fileReader = new BufferedReader(new InputStreamReader(webcomic2pdf.class.getResourceAsStream("jar_files/comics.json")));
      JsonNode rootNode = mapper.readTree(fileReader);
      JsonNode comics = rootNode.get("comics");
      System.out.println("#\tName");
      for (int comic = 0; comic < comics.size(); comic ++) {
        System.out.println(comic+"\t"+comics.get(comic).get("name").textValue());
      }
      System.out.println("");
      System.out.print("Select comic #:");
      int comic = Integer.parseInt(System.console().readLine());
      String comic_name=comics.get(comic).get("name").textValue();
      String comic_url=comics.get(comic).get("url").textValue();
      String comic_image_selector=comics.get(comic).get("image_selector").textValue();
      String comic_last_page_selector=comics.get(comic).get("last_page_selector").textValue();
      String comic_first_page=comics.get(comic).get("first_page").textValue();
      String comic_url_parameter=comics.get(comic).get("url_parameter").textValue();
      pages_count=getPages(comic_first_page,comic_last_page_selector,comic_url_parameter);
      Logger log = LogManager.getLogManager().getLogger("");
      //Logger.getLogger( webcomic2pdf.class.getName() );
      for (Handler h : log.getHandlers()) {
        h.setLevel(Level.OFF);
      }
      System.out.println(comic_name);
      System.out.println("Total available pages:"+pages_count);
      System.out.print("From page #:");
      int from = Integer.parseInt(System.console().readLine());
      System.out.print("To page #:");
      int to = Integer.parseInt(System.console().readLine());
      current_page=from;
      current_pages_count = to-from+1;
      while (current_page <= to) {
        String image_real = getPage(current_page,comic_url,comic_image_selector);
        String content_length= null;
        InputStream is = null;
        HttpURLConnection conn = (HttpURLConnection)(new URL(image_real.toString()).openConnection());
        conn.setConnectTimeout(60000);
        conn.setReadTimeout(60000);
        conn.connect();
        content_length= conn.getHeaderField( "content-length" );
        is = conn.getInputStream();
        String[] array = image_real.toString().split("/");
        String image_local = array[array.length-1];
        OutputStream outstream = new FileOutputStream(new File("pages/"+current_page+"_"+image_local));
        long fileSize = Long.valueOf(content_length).longValue();
        long bytesRead = 0;
        int percentage = -1;
        byte[] buffer = new byte[4096];
        int len;
        while ((len = is.read(buffer)) > 0) {
          outstream.write(buffer, 0, len);
          bytesRead += len;
          int n = (int)(100*bytesRead/fileSize);
          percentage = n;
          String n_perct = n+"%    ";
          System.out.print("\rDownloading page "+i+" of "+current_pages_count+" "+n_perct+"");
        }  
        outstream.close();
        i++;
        current_page ++;
      }
      File[] myarray=directory.listFiles(new FileFilter() {
        public boolean accept(File dir) {
          return dir.toString().endsWith(".jpg") && dir.isFile();
        }
      });
      System.out.println("");
      if(myarray.length>0){
        System.out.println("Generating PDF");
        PDDocument document = new PDDocument();
        for(int k=0; k<myarray.length; k++) {
          InputStream in = new FileInputStream(myarray[k]);
          BufferedImage bimg = ImageIO.read(in);
          float width = bimg.getWidth();
          float height = bimg.getHeight();
          PDPage page = new PDPage(new PDRectangle(width, height));
          document.addPage(page); 
          PDXObjectImage img = new PDJpeg(document, new FileInputStream(myarray[k]));
          PDPageContentStream contentStream = new PDPageContentStream(document, page);
          contentStream.drawImage(img, 0, 0);
          contentStream.close();
          in.close();
        }
        document.save(comic_name+".pdf");
        document.close();
      }
      File[] files_del = directory.listFiles();
      if(files_del!=null){
        for(int k=0; k<files_del.length; k++) {
          files_del[k].delete();
        }
      }
      directory.delete();
      System.out.println("Done");
    } catch(IOException e) {
    }
  } // end of main
  static String getPage(int page, String url, String selector) {
    try {
      Document doc = Jsoup.connect(url+page).get();
      Elements image = doc.select(selector);
      return image.attr("abs:src").toString();
    } catch(IOException e) {
      return "";
    }
  }
  static int getPages(String url, String selector, String parameter) {
    try {
      Document doc = Jsoup.connect(url).get();
      Elements last_page = doc.select(selector);
      return Integer.parseInt(last_page.attr("href").toString().replace(parameter,""));
    } catch(Exception e) {
      return 0;
    }
  }
} // end of class webcomic2pdf