/**
  *
  * TXT2PDF
  *
  * @version 1.0.0 vom 24.03.2013
  * @author Daniel Ruf 
  */
import org.apache.pdfbox.pdmodel.PDDocument;   
import org.apache.pdfbox.pdmodel.PDPage; 
import org.apache.pdfbox.TextToPDF;
import java.io.Reader;   
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileOutputStream;      
public class txt2pdf {
  public static void main(String[] args) {
    PDDocument doc = null;
    PDPage page = null;  
    String path2 = ".";
    File directory = new File(path2);   
    File[] myarray;  
    String version = "1.0.0";
    String program = "TXT2PDF";
    System.out.println(program + " " + version );
    myarray=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return dir.toString().endsWith(".txt") && !dir.toString().endsWith(".pdf.txt") && dir.isFile();
      }
    });
    System.out.println("Found " + myarray.length + " file(s)");
    for (int j = 0; j < myarray.length; j++)
    { 
      int file_number = j+1;
      System.out.print("\rProcessing file " + file_number + " of " + myarray.length);
      File path=myarray[j];
      String path_current = path.toString();   
      try{
        doc = new PDDocument();
        doc = new TextToPDF().createPDFFromText(new FileReader(path_current));
        String filename = path.getName();
        doc.save(new FileOutputStream(filename+".pdf"));     
        doc.close();
      } catch (Exception e){  
      }
      System.out.println("");
      System.out.println("Done");
      
    }
  }
}