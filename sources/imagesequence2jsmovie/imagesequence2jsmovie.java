/**
  *
  * ImageSequence2JSMovie
  *
  * @version 1.0.0 vom 01.01.2013
  * @author Daniel Ruf 
  */
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
public class imagesequence2jsmovie {
  public static void main(String[] args) throws Exception{      
    String path2 = ".";    
    File directory = new File(path2);    
    File[] myarray;
    String version = "1.0.0";
    String program = "ImageSequence2JSMovie";  
    System.out.println(program + " " + version );
    myarray=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return dir.toString().endsWith(".jpg") && dir.isFile();
      }
    });
    if (myarray.length > 0)
    {
      System.out.println("Found " + myarray.length + " frames");
      String[] dirs = {"js", "frames", "img"};
      String[] js_files = {"jquery-1.8.3.min.js", "jquery.jsmovie.1.4.3b.min.js", "jquery.jsmovie.1.4.3b.js"};
      String[] img_files = {"loader.png"};
      String[] html_files = {"index.html"};
      for(int i = 0; i < dirs.length; i++)
      {
        File dir = new File(dirs[i]);
        dir.mkdir();
      }
      for(int i = 0; i < js_files.length; i++)
      {
        File f=new File("js/"+js_files[i]);
        InputStream js_file = imagesequence2jsmovie.class.getResourceAsStream("jar_files/"+js_files[i]); 
        OutputStream out=new FileOutputStream(f);
        byte buf[]=new byte[1024];
        int len;
        while((len=js_file.read(buf))>0)
        out.write(buf,0,len);
        out.close();
        js_file.close();
      }
      for(int i = 0; i < html_files.length; i++)
      {
        File f=new File(html_files[i]);
        InputStream html_file = imagesequence2jsmovie.class.getResourceAsStream("jar_files/"+html_files[i]); 
        OutputStream out=new FileOutputStream(f);
        byte buf[]=new byte[1024];
        int len;
        while((len=html_file.read(buf))>0)
        out.write(buf,0,len);
        out.close();
        html_file.close();
      }
      for(int i = 0; i < img_files.length; i++)
      {
        File f=new File("img/"+img_files[i]);
        InputStream inputStream = imagesequence2jsmovie.class.getResourceAsStream("jar_files/"+img_files[i]); 
        OutputStream out=new FileOutputStream(f);
        byte buf[]=new byte[1024];
        int len;
        while((len=inputStream.read(buf))>0)
        out.write(buf,0,len);
        out.close();
        inputStream.close();
      }
      File path_first=myarray[0];
      File path_last=myarray[myarray.length-1];
      int index_first = path_first.getName().lastIndexOf('.');
      String filename_first = path_first.getName().substring(0, index_first);
      int index_last = path_last.getName().lastIndexOf('.');
      String filename_last = path_last.getName().substring(0, index_last);
      
      String frame_name = filename_first.replaceAll("[0-9]", "#");
      int frame_first= Integer.parseInt(filename_first.replaceAll("\\D+", ""));
      int frame_last = Integer.parseInt(filename_last.replaceAll("\\D+", ""));
      
      BufferedImage bimg = ImageIO.read(myarray[0]);
      int frame_width          = bimg.getWidth();
      int frame_height         = bimg.getHeight();    
      
      InputStream generated_file = imagesequence2jsmovie.class.getResourceAsStream("jar_files/generated.js");
      String generated_string = convertStreamToString(generated_file);
      generated_string = generated_string.replace("#FRAME_NAME#", frame_name);
      generated_string = generated_string.replace("#FRAME_FIRST#", String.valueOf(frame_first));
      generated_string = generated_string.replace("#FRAME_LAST#", String.valueOf(frame_last));
      generated_string = generated_string.replace("#FRAME_WIDTH#", String.valueOf(frame_width));
      generated_string = generated_string.replace("#FRAME_HEIGHT#", String.valueOf(frame_height));
      
      FileWriter fileWriter = new FileWriter ("js/generated.js");
      BufferedWriter bufferedWriter = new BufferedWriter (fileWriter);
      bufferedWriter.write (generated_string);
      bufferedWriter.close ();
      
      for (int j = 0; j < myarray.length; j++)
      {
        int file_number = j+1;
        System.out.print("\rProcessing frame " + file_number + " of " + myarray.length);
        File path=myarray[j];
        String path_current = path.toString();
        path.renameTo(new File("frames/"+path.getName()));
      }
      System.out.println("");
      System.out.println("Done");
    }
    else {
      System.out.println("Found " + myarray.length + " frames");
    }
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
  public static String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }
}