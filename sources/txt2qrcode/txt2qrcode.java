/**
  *
  * TXT2QRCode
  *
  * @version 1.1.0 vom 27.12.2014
  * @author Daniel Ruf 
  */
import java.io.IOException;
import java.io.File;
import java.io.FileFilter;
import java.io.BufferedReader;
import java.io.FileReader;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.client.j2se.MatrixToSvgImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.FormatException;       
public class txt2qrcode {
  public static void main(String[] args) throws Exception{      
    String path2 = ".";    
    File directory = new File(path2);    
    File[] myarray;
    String version = "1.1.0";
    String program = "TXT2QRCode";  
    System.out.println(program + " " + version );
    myarray=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return dir.toString().endsWith(".txt") && dir.isFile();
      }
    });
    System.out.println("Found " + myarray.length + " file(s)");
    for (int j = 0; j < myarray.length; j++)
    {
      int file_number = j+1;
      System.out.print("\rProcessing file " + file_number + " of " + myarray.length);
      File path=myarray[j];
      String path_current = path.toString();      
      String content = readLines(path_current);     
      int index = path.getName().lastIndexOf('.');
      String filename = path.getName().substring(0, index);
      QRCodeWriter writer = new QRCodeWriter();
      BitMatrix bitMatrix = null;    
      try {
        bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 300, 300);
        MatrixToImageWriter.writeToFile(bitMatrix, "PNG", new File(filename+".png"));
        MatrixToSvgImageWriter.writeToFile(bitMatrix, new File(filename + ".svg"));
      } catch (WriterException e){
        e.printStackTrace();
      } catch (IOException e){
        e.printStackTrace();
      }
    }
    System.out.println("");
    System.out.println("Done");
  }
  public static String readLines(String aFile) throws IOException {
    StringBuilder contents = new StringBuilder();    
    try {
      BufferedReader input =  new BufferedReader(new FileReader(aFile));
      try {
        String line = null;
        while (( line = input.readLine()) != null){contents.append(line); contents.append("\n");}
      }
      finally {input.close();}
    }
    catch (IOException ex){
      ex.printStackTrace();
    }
    return contents.toString();
  }
}