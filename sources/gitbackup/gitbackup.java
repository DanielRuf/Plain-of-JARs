/**
  *
  * GitBackup
  *
  * @version 1.0 vom 01.04.2016
  * @author Daniel Ruf
  */

import java.util.Properties;
import java.util.Map;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
 
public class gitbackup {
  
  public static void main(String[] args) throws FileNotFoundException, IOException, GitAPIException {
    String version = "1.0.0";
    String program = "GitBackup";
    System.out.println(program + " " + version );
    
    Properties prop = new Properties(); 
    prop.load(new FileInputStream("settings.properties"));
    String path = prop.getProperty("path");
    File git_repo = new File(path+"/..");
    
    Git git;
    FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
    repositoryBuilder.addCeilingDirectory( git_repo );
    repositoryBuilder.findGitDir( git_repo );
    
    if( repositoryBuilder.getGitDir() == null ) {
      git = Git.init().setDirectory(git_repo).call();
      System.out.println("Created a new repository at " + git.getRepository().getDirectory());
    } else {
      git = new Git( repositoryBuilder.build() );
      System.out.println("Opening existing repository at " + git.getRepository().getDirectory());
    }
    
    System.out.println("Adding all files, please wait...");
    git.add().addFilepattern(".").call();
    git.commit().setMessage("backup").call();
    
    System.out.println("Compressing repository, please wait...");
    Properties ret = git.gc().call();
    for(Map.Entry<Object, Object> entry : ret.entrySet()) {
      //System.out.println("Ret: " + entry.getKey() + ": " + entry.getValue());
    }
    
    System.out.println("");
    System.out.println("Done.");
  }
}