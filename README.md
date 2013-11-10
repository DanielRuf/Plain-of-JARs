![Plain of JARs](http://p.yusukekamiyamane.com/icons/search/fugue/icons-24/jar.png) Plain of JARs
=================

A collection of useful JAR files. 

An installed version of Java (Runtime Environment) 7 is required (some JAR files might also work under Java 6).

Please take a look at the **README in the JARs directory** for further information about the different JAR files. 

Some people might want to use a solution like [JARFix for Windows](http://johann.loefflmann.net/en/software/jarfix/index.html) so they do not need the console part with **cd** and **java -jar**, this solution does not work with all JARs.  
You can also use the **Shift-Right Click** shortcut in Windows 7 and later and select the **Open Command Window Here** option to open the console in the right path and you just need to run the corresponding **java -jar** command.

The latest tools include the full sourcecode and the final JAR files are released in two versions: tool.jar and tool_proguard.jar. The tool_proguard versions are shrinked using ProGuard, the config.pro files for the configuration of Proguard are in the corresponding source directories.