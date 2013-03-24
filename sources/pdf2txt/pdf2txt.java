/**
  *
  * PDF2TXT
  *
  * @version 1.0.0 vom 24.03.2013
  * @author Daniel Ruf 
  */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;  
import java.io.FileFilter;
public class pdf2txt {
  public static void main(String[] args){
    PDDocument pd;
    BufferedWriter wr;
    String path2 = ".";
    File directory = new File(path2);   
    File[] myarray;  
    String version = "1.0.0";
    String program = "PDF2TXT";
    System.out.println(program + " " + version );
    myarray=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return dir.toString().endsWith(".pdf") && !dir.toString().endsWith(".txt.pdf") && dir.isFile();
      }
    });
    System.out.println("Found " + myarray.length + " file(s)");
    for (int j = 0; j < myarray.length; j++)
    { 
      int file_number = j+1;
      System.out.print("\rProcessing file " + file_number + " of " + myarray.length);
      File path=myarray[j];
      String path_current = path.toString();   
      try {
        File input = new File(path_current);
        String filename = path.getName();
        File output = new File(path2 + "/"+filename+".txt");
        pd = PDDocument.load(input);
        PDFTextStripper stripper = new PDFTextStripper();
        wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
        stripper.writeText(pd, wr);
        if (pd != null) {
          pd.close();
        }
        wr.close();
      } catch (Exception e){
      }
    }
    System.out.println("");
    System.out.println("Done");
  }
}