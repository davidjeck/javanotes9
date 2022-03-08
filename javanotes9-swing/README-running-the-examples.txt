
This README file explains how to use the example programs for
"Introduction to Programming Using Java, Version 9.0, Swing edition", 
which is freely available on the web at http://math.hws.edu/javanotes-swing

Although some exercise solutions will work with earlier versions,
Java 17 is required for many of them.  Information about getting
Java can be found in Section 2.6 of the book.

The web site for the textbook includes Java source code files 
for the examples in the book.  If you download the web site, 
you can find the files in a folder named "sources."  The example
programs are also available as a separate download.  The files are
arranged by chapter, with a folder inside "sources" for each chapter.
There are also links to individual files in the index.html file inside
"sources," or on the web at http://math.hws.edu/javanotes-swing/sources/index.html

You have several options for running the examples...


---- RUN IN AN IDE ----

If you want to run the examples in an IDE, such as Eclipse, you should
be able to copy-and-paste the entire contents of any one of the chapter folders
into a project in the IDE, and then run the programs.  Note: When adding items
to an Eclipse project, do not copy the chapter folder itself; open the 
folder and copy the contents into the src folder in the Eclipse project.
You can put the examples from several chapters into the same project if you 
want; some files, such as TextIO.java, are duplicated in several chapters,
but any two files with the same name are identical, and you only need one
copy of the file in your project.


---- COMPILE AND RUN ON THE COMMAND LINE ----

If you know how to compile programs on the command line, and if you have
downloaded the examples, you can easily compile and run all the examples.
Just change into one of the chapter directories inside  the "sources" 
directory, and use a command of the form

                  javac ExampleClassName.java
                  
For example:
                  javac HelloWorld.java
                  
As long as your compiler supports Java 17 or higher, there should be no errors.  
(You might see some warnings, especially if you use a newer version of Java, 
but warnings do not stop a program from being compiled or executed.)  You can 
then run the compiled programs using the java command.  For example:

                  java HelloWorld
                                    
Several examples from Chapter 12, Section 5 and the Mandelbrot Set example
from Chapter 13, Section 5 use packages.  The source files for these examples
can be found in the directories chapter12/netgame and chapter13/edu.  Compiling
and running programs from packages is a little harder; see the discussions in
the textbook in the relevant sections.

                  
---- MAKE EXECUTABLE JAR FILES -----
                  
If you find it easier to run programs by double-clicking, you can 
use clickable, executable jar files to run examples, but you 
need to create the jar files. 

If you are running Mac or Linux, just open a Terminal window and change 
into the "sources" directory. To run the script named "make-jar-files.sh", 
use the command:

                 ./make-jar-files.sh
                 
If you are running Windows, just open a DOS command window and change 
into the "sources" directory.  To run the script, use the command

                 make-jar-files.bat
                 
These commands build the executable jar files for the examples.  They will
create a folder named "compiled-jar-files" inside the "sources" folder to hold
the jar files.  Inside "compiled-jar-files", the jar files will be organized 
by chapter.  The script will take some time to run, since there are a lot of 
examples.

You should be able to run an executable jar file just by double-clicking it.  
(On Linux, you might have to right-click it and select a command such as 
"Open with... JDK 17".)  It is also possible to run executable jar files
from the command line, using a command of the form

   java -jar JarFileName.jar
   
Note that programs that are meant to be run on the command line, such as
those that use TextIO, will be packaged in their jar files with a 
special version of TextIO that runs in its own window. 
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

