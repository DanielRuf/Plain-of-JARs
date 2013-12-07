/**
  *
  * EPUBSorter
  *
  * @version 1.0.0 vom 09.11.2013
  * @author Daniel Ruf 
  */
import java.io.File;
import java.io.FileFilter;
import java.util.List;
public class epubsorter {
  public static void main(String[] args) {
    String path2 = ".";
    File directory = new File(path2);    
    File[] pdf_files;
    File[] epub_files;
    String version = "1.0.0";
    String program = "EPUBSorter";  
    System.out.println(program + " " + version );
    pdf_files=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return dir.toString().toLowerCase().endsWith(".pdf") && dir.isFile();
      }
    });
    epub_files=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return dir.toString().toLowerCase().endsWith(".epub") && dir.isFile();
      }
    });
    System.out.println("Found " + pdf_files.length + " PDF file(s)");
    for (int j = 0; j < pdf_files.length; j++)
    {
      int file_number = j+1;
      System.out.print("\rProcessing PDF file " + file_number + " of " + pdf_files.length);
      File path=pdf_files[j];
      File dir = new File("pdf");
      if (!dir.exists()) {
        dir.mkdir();  
      }
      path.renameTo(new File("pdf/"+path.getName()));
    }
    System.out.println("");
    System.out.println("Found " + epub_files.length + " EPUB file(s)");
    for (int j = 0; j < epub_files.length; j++)
    {
      int file_number = j+1;
      System.out.print("\rProcessing EPUB file " + file_number + " of " + epub_files.length);
      File path=epub_files[j];
      File dir = new File(String.valueOf(path.getName().toString().toUpperCase().charAt(0)));
      if (!dir.exists()) {
        dir.mkdir();  
      }
      path.renameTo(new File(path.getName().toString().toUpperCase().charAt(0)+"/"+path.getName()));
    }
    System.out.println("");
    System.out.println("Done");
  } // end of main
} // end of class epubsorter