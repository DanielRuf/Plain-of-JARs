/**
  *
  * MailSender
  *
  * @version 1.0.0 vom 28.02.2015
  * @author Daniel Ruf 
  */
import java.util.Properties;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class mailsender {
  public static void main(String[] args) {
    String path2 = ".";
    File[] myarray;  
    String version = "1.0.0";
    String program = "MailSender";
    System.out.println(program + " " + version );
    
    String username = "";
    String from ="";
    String host="";
    String port = "";
    String password = "";
    String email_addresses="";
    String email_subject="";
    String email_text="";
    
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream("config.properties")); 
      username = prop.getProperty("username");
      from = prop.getProperty("from");
      host = prop.getProperty("host");
      port = prop.getProperty("port");
      password = prop.getProperty("password");
      email_addresses = prop.getProperty("email_addresses");
      email_subject = prop.getProperty("email_subject");
      email_text = prop.getProperty("email_text");
      path2 = prop.getProperty("directory");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    final String username_final = username;
    final String password_final = password;
    
    File directory = new File(path2);
    
    Properties props = new Properties();
    props.put("mail.smtp.auth", true);
    props.put("mail.smtp.starttls.enable", true);
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", port);
    
    myarray=directory.listFiles(new FileFilter() {
      public boolean accept(File dir) {
        return dir.toString().endsWith(".zip") && dir.isFile();
      }
    });
    System.out.println("Found " + myarray.length + " file(s)");
    for (int j = 0; j < myarray.length; j++)
    { 
      int file_number = j+1;
      System.out.print("\rProcessing file " + file_number + " of " + myarray.length);
      File path=myarray[j];
      String path_current = path.toString();
      File file_file = new File(path_current);
      String file = path_current;
      String filename = path.getName();
      
      Session session = Session.getInstance(props, 
      new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username_final, password_final);
        }
      });
      try {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email_addresses));
        message.setSubject(email_subject);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(email_text);
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        //System.out.println("Sending");
        Transport.send(message);
        //System.out.println("Done");
      } catch (MessagingException e) {
        e.printStackTrace();
      }
    }
    System.out.println("");
    System.out.println("Done");
  }
}