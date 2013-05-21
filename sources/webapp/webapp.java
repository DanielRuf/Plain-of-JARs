import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
/**
  *
  * WebApp
  *
  * @version 1.0.0 vom 21.05.2013
  * @author Daniel Ruf
  */
public class webapp {
  public static void main(String [] args){
    String version = "1.0.0";
    String program = "WebApp";
    System.out.println(program + " " + version );
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream("webapp.properties")); 
      final String title = prop.getProperty("title");
      final int width = Integer.parseInt(prop.getProperty("width"));
      final int height = Integer.parseInt(prop.getProperty("height"));
      final String url = prop.getProperty("url");
      final boolean fullscreen = Boolean.parseBoolean(prop.getProperty("fullscreen"));
      final boolean resizable = Boolean.parseBoolean(prop.getProperty("resizable"));
      final String logo = prop.getProperty("logo");
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          webappview mainFrame = new webappview(title,url,fullscreen,width,height,resizable,logo);
          mainFrame.setVisible(true);
        }
      });
    } catch (IOException ex) {
      ex.printStackTrace();
    }  
  }    
}

class webappview extends JFrame{
  JFXPanel javafxPanel;
  WebView webComponent;
  JPanel mainPanel;
  private GraphicsDevice device;
  public webappview(String title, String url, boolean fullscreen, int width, int height, boolean resizable, String logo){
    super(title);
    if(new File(logo).isFile()){
      Image icon = Toolkit.getDefaultToolkit().getImage(webapp.class.getResource(logo));
      this.setIconImage(icon);
    }
    System.out.println(logo);
    javafxPanel = new JFXPanel();
    initSwingComponents(width,height,resizable,fullscreen);
    loadJavaFXScene(url);
    device=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    if(fullscreen){
      this.dispose();
      this.setUndecorated(true);
      device.setFullScreenWindow(this);
      this.setVisible(true);
      this.setResizable(false);
    }                                                  
  }
  
  private void initSwingComponents(int width, int height, boolean resizable,boolean fullscreen){ 
    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(javafxPanel, BorderLayout.CENTER);
    this.add(mainPanel);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(width,height);
    this.setLocationRelativeTo(null);
    if(fullscreen){
      this.setResizable(false);
    }else {
      this.setResizable(resizable);
    } // end of if-else
  }
  
  private void loadJavaFXScene(final String url){
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        BorderPane borderPane = new BorderPane();
        webComponent = new WebView();
        webComponent.getEngine().load(url);
        borderPane.setCenter(webComponent);
        Scene scene = new Scene(borderPane);
        javafxPanel.setScene(scene);
      }
    });
  }
}