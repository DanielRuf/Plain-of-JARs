/**
  *
  * VirusTotalScan
  *
  * @version 1.0.0 vom 28.02.2015
  * @author Daniel Ruf 
  */
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import com.kanishka.virustotal.dto.ScanInfo;
import com.kanishka.virustotal.exception.APIKeyNotFoundException;
import com.kanishka.virustotal.exception.UnauthorizedAccessException;
import com.kanishka.virustotalv2.VirusTotalConfig;
import com.kanishka.virustotalv2.VirustotalPublicV2;
import com.kanishka.virustotalv2.VirustotalPublicV2Impl;
import com.kanishka.virustotal.dto.GeneralResponse;
import java.io.UnsupportedEncodingException;

public class virustotalscan {
  public static void main(String[] args) throws Exception{
    String path2 = ".";
    File[] myarray;  
    String version = "1.0.0";
    String program = "VirustotalScan";
    System.out.println(program + " " + version );
    String comment = "";
    String apikey = "";
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream("config.properties")); 
      apikey = prop.getProperty("apikey");
      path2 = prop.getProperty("directory");
      comment = prop.getProperty("comment");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    File directory = new File(path2);
    myarray=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return !dir.toString().endsWith(".zip") && !dir.toString().endsWith(".java") && !dir.toString().endsWith(".properties") && !dir.toString().endsWith(".jar") && !dir.toString().endsWith(".class") && !dir.toString().endsWith(".~ava") && dir.isFile();
      }
    });
    System.out.println("Found " + myarray.length + " file(s)");
    for (int j = 0; j < myarray.length; j++)
    { 
      int file_number = j+1;
      System.out.print("\rProcessing file " + file_number + " of " + myarray.length);
      File path=myarray[j];
      String path_current = path.toString();
      File file = new File(path_current);
      String filename = path.getName();
      scanFile(apikey, file, comment);
    }
    System.out.println("");
    System.out.println("Done");
  }
  
  public static void addComment(String resource, String apikey, String comment){
    try {
      VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey(apikey);
      VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();
      
      GeneralResponse gRespo = virusTotalRef.makeAComment(resource, comment);
      System.out.println("");
      System.out.println("Response Code : " + gRespo.getResponseCode());
      System.out.println("Verbose Message : " + gRespo.getVerboseMessage());
      
    } catch (APIKeyNotFoundException ex) {
      System.err.println("API Key not found! " + ex.getMessage());
    } catch (UnsupportedEncodingException ex) {
      System.err.println("Unsupported Encoding Format!" + ex.getMessage());
    } catch (UnauthorizedAccessException ex) {
      System.err.println("Invalid API Key " + ex.getMessage());
    } catch (Exception ex) {
      System.err.println("Something Bad Happened! " + ex.getMessage());
    }
  }
  
  public static void scanFile(String apikey, File file, String comment) {
    try {
      VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey(apikey);
      VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();
      
      ScanInfo scanInformation = virusTotalRef.scanFile(file);
      System.out.println("");
      System.out.println("___SCAN INFORMATION___");
      System.out.println("MD5 :\t" + scanInformation.getMd5());
      System.out.println("Perma Link :\t" + scanInformation.getPermalink());
      System.out.println("Resource :\t" + scanInformation.getResource());
      System.out.println("Scan Date :\t" + scanInformation.getScanDate());
      System.out.println("Scan Id :\t" + scanInformation.getScanId());
      System.out.println("SHA1 :\t" + scanInformation.getSha1());
      System.out.println("SHA256 :\t" + scanInformation.getSha256());
      System.out.println("Verbose Msg :\t" + scanInformation.getVerboseMessage());
      System.out.println("Response Code :\t" + scanInformation.getResponseCode());
      System.out.println("done.");
      
      addComment(scanInformation.getResource(), apikey, comment);
      System.out.println("");
    } catch (APIKeyNotFoundException ex) {
      System.err.println("API Key not found! " + ex.getMessage());
    } catch (UnsupportedEncodingException ex) {
      System.err.println("Unsupported Encoding Format!" + ex.getMessage());
    } catch (UnauthorizedAccessException ex) {
      System.err.println("Invalid API Key " + ex.getMessage());
    } catch (Exception ex) {
      System.err.println("Something Bad Happened! " + ex.getMessage());
    }
  }
} 