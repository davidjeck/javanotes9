
This README explains how to use the solutions to the programming
exercises for "Introduction to Programming Using Java, Version 7", 
which is freely available on the web at http://math.hws.edu/javanotes

Each end-of-chapter exercise has a web page that contains a discussion
of the solution and the source code for a sample solution.  The
archive javanotes7-exercise-solutions.zip, which can be downloaded
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

If you want to run the programs in an IDE, such as Eclipse, you should
be able to copy the entire contents of any one of the chapter folders
into a project in the IDE, and then run the programs.  Note: Do not copy
the chapter folder itself; open the folder and copy the contents.
You can put the examples from several chapters to the same project if you 
want; some files, such as TextIO.java, are duplicated in several chapters,
but any two files with the same name are identical, and you only need one
copy of the file in your project.


---- COMPILE AND RUN ON THE COMMAND LINE ----

If you know how to compile programs on the command line, just change into one 
of the chapter directories inside "sources" and use the command 

                  javac *.java
                  
to compile all the programs from that chapter.  As long as your compiler supports 
Java 7 or higher, there should be no errors.  (You might see some warnings, especially
if you use a newer version of java, but warnings do not stop a program from being
compiled or executed.)  You can then run individual programs using the java command.  
For example:

                  java HelloWorld
                  
There is one exercise in Chapter 12 that uses packages.  To compile that
example, change into the chapter12 folder and use the commands

                  javac netgame/newchat/*.java
                  
on Mac or Linux or

                  javac netgame\newchat\*.java
                  
on Windows.  Then, to use the program, you need to run both the server program
and the client program with the following commands (in separate command windows):

                  java netgame.newchat.NewChatRoomServer
and
                  java netgame.newchat.NewChatRoomWindow

                  
