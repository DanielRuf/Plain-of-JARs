/**
  *
  * EmailDownloader
  *
  * @version 1.0.0 vom 28.02.2015
  * @author Daniel Ruf 
  */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;

public class emaildownloader {
  private String saveDirectory;
  
  public static void main(String[] args) {
    String version = "1.0.0";
    String program = "EmailDownloader";
    System.out.println(program + " " + version );
    String host = "";
    String port = "";
    String username = "";
    String password = "";
    String saveDirectory = "";
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream("config.properties")); 
      host = prop.getProperty("host");
      port = prop.getProperty("port");
      username = prop.getProperty("username");
      password = prop.getProperty("password");
      saveDirectory = prop.getProperty("directory");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    File createDirs = new File(saveDirectory);
    createDirs.mkdirs();
    emaildownloader receiver = new emaildownloader();
    receiver.setSaveDirectory(saveDirectory);
    receiver.downloadEmailAttachments(host, port, username, password);
    System.out.println("");
    System.out.println("Done.");
  }
  
  public void setSaveDirectory(String dir) {
    this.saveDirectory = dir;
  }
  
  public void downloadEmailAttachments(String host, String port,
  String username, String password) {
    Properties properties = new Properties();
    properties.put("mail.imap.host", host);
    properties.put("mail.imap.port", port);
    //properties.setProperty("mail.imap.socketFactory.class","javax.net.ssl.SSLSocketFactory");
    properties.setProperty("mail.imap.socketFactory.fallback", "false");
    properties.setProperty("mail.imap.starttls.enable", "true");
    properties.setProperty("mail.imap.socketFactory.port",
    String.valueOf(port));
    Session session = Session.getDefaultInstance(properties);
    try {
      Store store = session.getStore("imap");
      store.connect(username, password);
      Folder folderInbox = store.getFolder("INBOX");
      folderInbox.open(Folder.READ_WRITE);
      Message[] arrayMessages = folderInbox.getMessages();
      for (int i = 0; i < arrayMessages.length; i++) {
        Message message = arrayMessages[i];
        if (!message.isSet(Flags.Flag.SEEN)) {
          //Address[] fromAddress = message.getFrom();
          //String from = fromAddress[0].toString();
          //String subject = message.getSubject();
          //String sentDate = message.getSentDate().toString();
          String contentType = message.getContentType();
          String messageContent = "";
          String attachFiles = "";
          if (contentType.contains("multipart")) { 
            Multipart multiPart = (Multipart)message.getContent();
            int numberOfParts = multiPart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
              MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
              if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                String fileName = part.getFileName();
                attachFiles += fileName + ", ";
                part.saveFile(saveDirectory + File.separator + fileName);
              } else {
                messageContent = part.getContent().toString();
              }
            } 
            if (attachFiles.length() > 1) {
              attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
            }
          } else if (contentType.contains("text/plain")|| contentType.contains("text/html")) {
            Object content = message.getContent();
            if (content != null) {
              messageContent = content.toString();
            }
          }   
          System.out.println("Message #" + (i + 1) + ":");
          //System.out.println("\t From: " + from);
          //System.out.println("\t Subject: " + subject);
          //System.out.println("\t Sent Date: " + sentDate);
          //System.out.println("\t Message: " + messageContent);
          //System.out.println("\t Attachments: " + attachFiles);
          message.setFlag(Flags.Flag.SEEN,true);
        }
      }   
      folderInbox.close(false);
      store.close();
    } catch (NoSuchProviderException ex) {
      System.out.println("No provider for imap.");
      ex.printStackTrace();
    } catch (MessagingException ex) {
      System.out.println("Could not connect to the message store");
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}