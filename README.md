# javanotes9
Full source files for the free textbook "Introduction to Programming Using Java," Version 9

*Introduction to Programming Using Java* is a free introductory textbook covering the Java programming language.
Version 9 is available in two editions, one using JavaFX for GUI programming and one using Swing.  Version 9.
covers Java 17.  It will be released in May 2022.  Preliminary releases are available
at [https://math.hws.edu/javanotes9](https://math.hws.edu/javanotes9) and
[https://math.hws.edu/javanotes9-swing](https://math.hws.edu/javanotes9-swing).

This repository contains the source files that are used to generate the two editions in HTML, PDF, and eBook formats.
The sources incude XML, XSLT, DTD, image, and Java files, plus some scripts for generating the various formats.  The
scripts were written for Linux but should also work on MacOS.  Because of the large variety of formats, the whole
thing has gotten rather complicated.  Since the source files were not originally meant for publication, they
are not very cleanly written, and using them would require a lot of expertise.

The sources are stored in two Eclipse projects named javanotes9 and javanotes9-swing.  The javanotes9 project
contains everything that is needed to produce the JavaFX edition.  To use it to produce the book, you first
need to export the files by editing and running the script *export-source.sh* from the javanotes9 folder.  (The
script must first be edited to specify the correct directory names for the local environment.)  The exported 
directory will contain scripts for building the JavaFX edition in various formats and a file, README.txt,
with instructions for using the scripts.

The javanotes9-swing project contains only files that need to be replaced or added to javanotes9 for the
Swing edition.  To use it to produce the book, you first need to export the files by editing and running
the script *export-source-swing.sh* which can be found in the javanotes9 project.  Again, the export directory
will contain build scripts and a README file.

Note that if you want to use the projects in Eclipse, you will need to configure javanotes9 to support JavaFX,
as discussed in [Subsection 2.6.8](https://math.hws.edu/eck/cs124/javanotes9/c2/s6.html#basics.6.8) of the book.

(Source files for Version 8 of the book can be obtained by checking out the tag Version-8.1.3 in the repository.
Also, the initial commit for the repository consisted of the source files for Version 7 of the book.)

