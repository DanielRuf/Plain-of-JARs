/**
  *
  * Browsertest
  *
  * @version 1.0.0 vom 24.01.2013
  * @author 
  */
import java.util.Scanner;
import java.io.*; 
public class browsertest {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String version = "1.0.0";
    String program = "Browsertest";  
    System.out.println(program + " " + version );
    File browser_file = new File("browsers.txt");
    try{browser_file.createNewFile();}catch(Exception e){}
    System.out.print("URL: ");
    String url = scanner.nextLine();
    try{BufferedReader reader = new BufferedReader(new FileReader("browsers.txt"));
      int lines = 0;
      while (reader.readLine() != null) lines++;
      reader.close();
      System.out.println("Found "+lines+" browsers");
      try
      { 
        BufferedReader br = new BufferedReader(new FileReader("browsers.txt"));
        String line;
        int current_line = 0;
        while ((line = br.readLine()) != null) {
          line = line.replace("\\", "\\\\");
          current_line++;
          System.out.print("\rStarting browser "+current_line+" of "+lines+"");
          try
          {    
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(""+line+" \""+url+"\"");
          }
          catch(Exception e){}
        }
        br.close();
        System.out.println("");
        System.out.println("Done");
      }
      catch(Exception e){};
    }
    catch(Exception e){}
  } // end of main
} // end of class browsertest
