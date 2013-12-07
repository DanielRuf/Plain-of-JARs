/**
  *
  * EPUB2XLS
  *
  * @version 1.0.0 vom 07.12.2013
  * @author Daniel Ruf 
  */
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.domain.Book;
import org.apache.commons.lang3.StringEscapeUtils;
public class epub2xls {
  public static void main(String[] args) throws Exception {
    String path2 = ".";
    File directory = new File(path2);    
    File[] epub_files;
    File[] epub_files_local;
    File[] directories;
    String version = "1.0.0";
    String program = "EPUB2XLS";  
    System.out.println(program + " " + version );
    String excel_sheet ="";
    InputStream header = epub2xls.class.getResourceAsStream("jar_files/header.xml");
    String header_string = convertStreamToString(header);
    excel_sheet = header_string;
    epub_files_local=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return dir.getName().toString().toLowerCase().endsWith(".epub") && dir.isFile();
      }
    });
    for (int m = 0; m < epub_files_local.length; m++)
    {
      int epub_file_number_local = m+1;
      System.out.print("\rProcessing file " + epub_file_number_local + " of " + epub_files_local.length+"                         ");
      File epub_local=epub_files_local[m];
      String dir_name_local = directory.getName();
      String epub_name_local = epub_local.getName();
      EpubReader epubReader_local = new EpubReader();
      try { 
        Book book_local = epubReader_local.readEpub(new FileInputStream(epub_local));
        List<Author> authors_local = book_local.getMetadata().getAuthors();
        StringBuilder sb_local = new StringBuilder();
        for(Author s: authors_local) {
          sb_local.append(s.getFirstname() + " " + s.getLastname()).append(',');
        }
        if (sb_local.length()>0) {
          sb_local.deleteCharAt(sb_local.length()-1); 
        } // end of if
        String title_local = book_local.getMetadata().getFirstTitle();
        String authors_string_local = sb_local.toString();
        String file_path_local = dir_name_local + "/" + epub_name_local;
        InputStream row_local = epub2xls.class.getResourceAsStream("jar_files/row.xml");
        String row_string_local = convertStreamToString(row_local);
        row_string_local = row_string_local.replace("#path#", StringEscapeUtils.escapeHtml4(file_path_local));
        row_string_local = row_string_local.replace("#author#", StringEscapeUtils.escapeHtml4(authors_string_local));
        row_string_local = row_string_local.replace("#title#", StringEscapeUtils.escapeHtml4(title_local));
        excel_sheet += row_string_local;
      } catch(Exception x) {
      } finally {
      } // end of try
    }
    if (epub_files_local.length>0) {
      System.out.println("");
    } // end of if
    directories=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return /*dir.getName().toString().toLowerCase().length() == 1 &&*/ dir.isDirectory();
      }
    });
    System.out.println(directories.length + " directories found");
    for (int j = 0; j < directories.length; j++)
    {
      int file_number = j+1;
      System.out.print("\rProcessing directory " + file_number + " of " + directories.length);
      File path=directories[j];
      epub_files=path.listFiles(new FileFilter() {
        public boolean accept(File dir) {
          return dir.getName().toString().toLowerCase().endsWith(".epub") && dir.isFile();
        }
      });
      for (int k = 0; k < epub_files.length; k++)
      {
        int epub_file_number = k+1;
        System.out.print("\rProcessing file " + epub_file_number + " of " + epub_files.length+"                         ");
        File epub=epub_files[k];
        String dir_name = path.getName();
        String epub_name = epub.getName();
        EpubReader epubReader = new EpubReader();
        try { 
          Book book = epubReader.readEpub(new FileInputStream(epub));
          List<Author> authors = book.getMetadata().getAuthors();
          StringBuilder sb = new StringBuilder();
          for(Author s: authors) {
            sb.append(s.getFirstname() + " " + s.getLastname()).append(',');
          }
          if (sb.length()>0) {
            sb.deleteCharAt(sb.length()-1); 
          } // end of if
          String title = book.getMetadata().getFirstTitle();
          String authors_string = sb.toString();
          String file_path = dir_name + "/" + epub_name;
          InputStream row = epub2xls.class.getResourceAsStream("jar_files/row.xml");
          String row_string = convertStreamToString(row);
          row_string = row_string.replace("#path#", StringEscapeUtils.escapeHtml4(file_path));
          row_string = row_string.replace("#author#", StringEscapeUtils.escapeHtml4(authors_string));
          row_string = row_string.replace("#title#", StringEscapeUtils.escapeHtml4(title));
          excel_sheet += row_string;
        } catch(Exception x) {
        } finally {
        } // end of try
      }
    }
    InputStream footer = epub2xls.class.getResourceAsStream("jar_files/footer.xml");
    String footer_string = convertStreamToString(footer);
    excel_sheet += footer_string;
    FileWriter fileWriter = new FileWriter ("ebooks.xls");
    BufferedWriter bufferedWriter = new BufferedWriter (fileWriter);
    bufferedWriter.write (excel_sheet);
    bufferedWriter.close ();
    System.out.println("");
    System.out.println("Done");
  } // end of main
  public static String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }
} // end of class epub2xls