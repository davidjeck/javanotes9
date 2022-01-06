
This README explains how to use the solutions to the programming
exercises for "Introduction to Programming Using Java, Version 8.1", 
which is freely available on the web at http://math.hws.edu/javanotes

Note that the GUI programs require Java 8 or higher, with JavaFX.
Except for the JDK 8, 9, or 10 from Oracle, JavaFX is separate
from the JDK.  And for Java 11 or later, running programs that
use JavaFX requires some special commands or configuration.
Section 2.6 of the textbook discusses obtaining and using a
JDK and JavaFX.

Each end-of-chapter exercise has a web page that contains a discussion
of the solution and the source code for a sample solution.  The
archive javanotes8-exercise-solutions.zip, which can be downloaded
using a link on the web site, contains all of the source code for
the solutions, extracted from the solution web pages.  It also contains
all extra files that are required by the solutions.  The solutions
are organized into folders, with one folder for each chapter (except
Chapter 1, which has no exercises.)  This is done as a convenience
to help you run the solutions, but please don't just run them!  Try
working on the exercises yourself, and read my discussion of the 
solutions on the web pages.  You'll learn a lot more that way.

You can also get solutions to individual exercises by copy-and-pasting
the code from the solution web page into a text editor.  (Copy from
the web page open in a web browser, not from the HTML source of the
web page.  The HTML source contains extra markup that will be seen
as errors by the Java compiler.)

You have two options for running the solutions...


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
then run the compiled program using the java command.  For example:

                  java HelloWorld
                  
For GUI programs, which use JavaFX, you will have to add command-line options to
the javac and java commands, as discussed in Section 2.6 of the textbook,
but the basic idea is the same.
 
 
There is one exercise in Chapter 12 that uses packages.  Furthermore, that
exercise uses JavaFX, so you will need to add JavaFX options to the javac
and java commands.  Let's say that you've defined commands  jfxc  and  jfx
that are equivale to the  javac  and  java  commands with JavaFX options
included, as discussed in Section 2.6.3.  Then you can compile the
example with the command

                  jfxc netgame/newchat/*.java
                  
on Mac or Linux or

                  jfxc netgame\newchat\*.java
                  
on Windows.  Then, to use the program, you need to run both the server program
and the client program with the following commands (in separate command windows):

                  jfx netgame.newchat.NewChatRoomServer
and
                  jfx netgame.newchat.NewChatRoomWindow

Of course, if you are using an older JDK with built-in JavaFX, you can use
the javac and java commands.   
