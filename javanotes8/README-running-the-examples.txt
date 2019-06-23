
This README file explains how to use the example programs for
"Introduction to Programming Using Java, Version 8.1", which is freely
available on the web at http://math.hws.edu/javanotes

Note that the GUI programs require Java 8 or higher, with JavaFX.
Except for the JDK 8, 9, or 10 from Oracle, JavaFX is separate
from the JDK.  And for Java 11 or later, running programs that
use JavaFX requires some special commands or configuration.
Section 2.6 of the textbook discusses obtaining and using a
JDK and JavaFX.

The web site for "Introduction to Programming Using Java, Version 8.1" includes
Java source code files for the examples in the book.  If you download the
web site, you can find the files in a folder named "sources."  The example
programs are also available as a separate download.  The files are
arranged by chapter, with a folder inside "sources" for each chapter.
There are also links to individual files in the index.html file inside
"sources," or on the web at http://math.hws.edu/javanotes/sources/index.html

You have several options for running the examples...


---- RUN IN AN IDE ----

If you want to run the examples in an IDE, such as Eclipse, you should
be able to copy-and-paste the entire contents of any one of the chapter folders
into a project in the IDE, and then run the programs.  For GUI programs,
which use JavaFX, you will very likely need to configure Eclipse as
discussed in Section 2.6 of the book.  Note: When adding items to
an Eclipse project, do not copy the chapter folder itself; open the 
folder and copy the contents into the src folder in the Eclipse project.
You can put the examples from several chapters into the same project if you 
want; some files, such as TextIO.java, are duplicated in several chapters,
but any two files with the same name are identical, and you only need one
copy of the file in your project.


---- COMPILE AND RUN ON THE COMMAND LINE ----

If you know how to compile programs on the command line, and if you have
downloaded the examples, you can easily compile and run all the examples.
For non-GUI programs, just change into one of the chapter directories inside 
the "sources" directory, and use a command of the form

                  javac ExampleClassName.java
                  
For example:
                  javac HelloWorld.java
                  
As long as your compiler supports Java 8 or higher, there should be no errors.  
(You might see some warnings, especially if you use a newer version of Java, 
but warnings do not stop a program from being compiled or executed.)  You can 
then run the compiled programs using the java command.  For example:

                  java HelloWorld
                  
For GUI programs, which use JavaFX, you will have to add command-line options to
the javac and java commands, as discussed in Section 2.6 of the textbook,
but the basic idea is the same.
                  
Several examples from Chapter 12, Section 5 and the Mandelbrot Set example
from Chapter 13, Section 5 use packages.  The source files for these examples
can be found in the directories chapter12/netgame and chapter13/edu.  Compiling
and running programs from packages is a little harder; see the discussions in
the textbook in the relevant sections.

                  
---- MAKE EXECUTABLE JAR FILES -----
                  
If you find it easier to run programs by double-clicking, you can 
use clickable, executable jar files to run the non-JavaFX examples, but you 
need to create the jar files.  You can also use executable jar files
to run the JavaFX programs, but --- unless you are using an older JDK that
has JavaFX as a built-in part of Java --- you will still need to run those
jar files on the command line.

If you are running Mac or Linux, just open a Terminal window and change 
into the "sources" directory.  If you want to make .jar files for the
GUI programs as well as for the non-GUI programs, you will need to
edit the "make-jar-files.sh" script to tell it where to find the
JavaFX SDK.  See the comments in that file for morei nformation.  To run
the script named "make-jar-files.sh", use the command:

                 ./make-jar-files.sh
                 
If you are running Windows, just open a DOS command window and change 
into the "sources" directory.  If you want to build jar files for JavaFX
files, edit the "make-jar-files.bat" script to tell it where to find
the JavaFX SDK; if you do not do this, the script will only build the
non-JavaFX jar files.  To run the script, use the command

                 make-jar-files.bat
                 
These commands build the executable jar files for the examples.  They will
create a folder named "compiled-jar-files" inside the "sources" folder to hold
the jar files.  Inside "compiled-jar-files", the jar files will be organized 
by chapter.  The script will take some time to run, since there are a lot of 
examples.

For the non-JavaFX files, you should be able to run an executable jar file
just by double-clicking it.  (On Linux, you might have to right-click it 
and select a command such as "Open with... JDK 8".)  If you have an older
JDK with JavaFX built in, that will also be true for JavaFX programs.
Otherwise, you will need to run the jar file on the command line, using
a java command with JavaFX options added, as discussed in Section 2.6.3.
The exact command depends on the location of the JavaFX SDK on your system,
but a typical command would be something like

   java -p /home/eck/javafx-sdk-11/lib --add-modules=ALL-MODULE-PATH -jar JarFileName.jar
   
Note that programs that are meant to be run on the command line, such as
those that use TextIO, will be packaged in their jar files with a special
version of TextIO that runs in its own window (without using JavaFX).  
The window is opened when you execute the jar file.  You will have to 
close the window, by clicking its close button, when the program ends.
The GUI version of TextIO can be found in the package "textiogui" inside
the "sources" folder.  See the classes in that package for information 
about how to use it, if you are interested in using it for your own 
programs.  (For Windows, the files for TextIO that are used in 
the .jar files can be found in the folder "textio-for-windows-jar-files" 
inside the sources folder. Those files should not be used for other 
purposes.  They are, basically, a kludge that I had to use when writing 
the .bat script for Windows.)

There are several examples for which jar files are not made in any case.  There 
are several reasons for leaving them out.  For example, some require command-line
arguments and so cannot be run by double-clicking the jar file.

