/**
  *
  * FileUploader
  *
  * @version 1.0 vom 25.06.2015
  * @author Daniel Ruf
  */
import java.io.File;
import java.io.IOException;
import java.io.FileFilter;
import java.util.Properties;
import java.io.FileInputStream;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpHost;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class fileuploader {
  
  public static void main(String[] args) throws Exception {
    File[] myarray;  
    String version = "1.0.0";
    String program = "FileUploader";
    System.out.println(program + " " + version );
    Properties prop = new Properties();
    String path2 = "";
    String upload_path = "";
    try {
      prop.load(new FileInputStream("settings.properties")); 
      path2 = prop.getProperty("testfiles_directory");
      upload_path = prop.getProperty("upload_path");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    File directory = new File(path2); 
    myarray=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return dir.isFile();
      }
    });
    int numfiles=myarray.length;
    int status_200=0;
    int status_403=0;
    for (int j = 0; j < myarray.length; j++)
    { 
      int file_number = j+1;
      System.out.print("\rProcessing file " + file_number + " of " + myarray.length);
      File path=myarray[j];
      String path_current = path.toString();
      File file = new File(path_current);
      String filename = path.getName();
      
      CloseableHttpClient httpClient = HttpClients.createDefault();
      HttpPost uploadFile = new HttpPost(upload_path);
      
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
      builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, filename);
      HttpEntity multipart = builder.build();
      
      uploadFile.setEntity(multipart);
      
      CloseableHttpResponse response = httpClient.execute(uploadFile);
      //System.out.println(response.getStatusLine());
      if(response.getStatusLine().getStatusCode()==200)status_200++;
      if(response.getStatusLine().getStatusCode()==403)status_403++;
      EntityUtils.consume(response.getEntity());
      response.close();
    }
    System.out.println("");
    System.out.println("Results:");
    System.out.println("200\t "+status_200+"/"+numfiles);
    System.out.println("403\t "+status_403+"/"+numfiles);
    
  } // end of main
  
} // end of class stressuploader
