/**
  *
  * DirectoryWatcher
  *
  * @version 1.0.1 vom 06.04.2016
  * @author Daniel Ruf
  */

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
 
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import java.util.Properties;
import java.nio.file.Files;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.HashMap;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.nio.file.FileVisitResult;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
 
public class directorywatcher {
  private final WatchService watcher;
  private Git git;
  private final Map<WatchKey,Path> keys;
  @SuppressWarnings("unchecked")
  static <T> WatchEvent<T> cast(WatchEvent<?> event) {
    return (WatchEvent<T>)event;
  }
  
  public static void main(String[] args) throws IOException, GitAPIException {
    String version = "1.0.1";
    String program = "DirectoryWatcher";
    System.out.println(program + " " + version );
    
    Properties prop = new Properties(); 
    prop.load(new FileInputStream("settings.properties"));
    String path = prop.getProperty("path");
    File git_repo = new File(path).getParentFile();
    
    Path dir = Paths.get(path);
    new directorywatcher(dir, git_repo).processEvents();
  }
  
  private void register(Path dir) throws IOException {
    WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    keys.put(key, dir);
  }
  
  private void registerAll(final Path start) throws IOException {
    Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
      throws IOException
      {
        register(dir);
        return FileVisitResult.CONTINUE;
      }
    });
  }
  
  directorywatcher(Path dir, File git_repo) throws IOException, GitAPIException {
    this.watcher = FileSystems.getDefault().newWatchService();
    this.keys = new HashMap<WatchKey,Path>();
    registerAll(dir);
    
    FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
    repositoryBuilder.addCeilingDirectory( git_repo );
    repositoryBuilder.findGitDir( git_repo );
    if( repositoryBuilder.getGitDir() == null ) {
      this.git = Git.init().setDirectory(git_repo).call();
      System.out.println("Created a new repository at " + git.getRepository().getDirectory().getParentFile());
    } else {
      this.git = new Git( repositoryBuilder.build() );
      System.out.println("Opening existing repository at " + git.getRepository().getDirectory().getParentFile());
    }
    
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override 
      public void run() {
        try{
          Git git;
          FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
          repositoryBuilder.addCeilingDirectory( git_repo );
          repositoryBuilder.findGitDir( git_repo );
          if( repositoryBuilder.getGitDir() != null ) {
            git = new Git( repositoryBuilder.build() );
            Properties ret = git.gc().call();
            for(Map.Entry<Object, Object> entry : ret.entrySet()) {
              //System.out.println("Ret: " + entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("Compressed repository."); 
          } 
        } catch (GitAPIException gaex){
          System.err.println(gaex);
        } catch (IOException ex) {
          System.err.println(ex);
        }
      }
    });
  }
  
  void processEvents() throws GitAPIException {
    String lastEvent = null;
    String lastEventFile = null;
    String currentEvent = null;
    String currentEventFile = null;
    
    for (;;) {
      WatchKey key;
      try {
        key = watcher.take();
      } catch (InterruptedException x) {
        return;
      }
      
      Path dir = keys.get(key);
      if (dir == null) {
        System.err.println("WatchKey not recognized!!");
        continue;
      }
      
      for (WatchEvent<?> event: key.pollEvents()) {
        WatchEvent.Kind kind = event.kind();
        WatchEvent<Path> ev = cast(event);
        Path name = ev.context();
        Path child = dir.resolve(name);
        // rename / move actiond are system specific and would need special binaries
        if (name.toString() == ".git") {
          continue;
        }
        
        if (kind == ENTRY_CREATE) {
          try {
            if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
              registerAll(child);
            }
          } catch (IOException x) {
          }
          git.add().addFilepattern(".").call();
          git.commit().setMessage("added "+child.toString()).call();
        }
        
        if (kind == ENTRY_MODIFY && !Files.isDirectory(child, NOFOLLOW_LINKS)) {
          git.add().addFilepattern(".").call();
          git.commit().setMessage("modified "+child.toString()).call(); 
        }
        
        if (kind == ENTRY_DELETE) {
          git.add().addFilepattern(".").call();
          git.commit().setMessage("deleted "+child.toString()).call();
        }
        currentEventFile = child.toString();
      }
      
      boolean valid = key.reset();
      if (!valid) {
        keys.remove(key);
        if (keys.isEmpty()) {
          break;
        }
      }
    }  
  }
}