# javanotes9
This git repository contains the full source files for the free textbook *Introduction to Programming Using Java,* Version 9.
The textbook has been available on the web in a series of versions since 1996.

*Introduction to Programming Using Java* is a free introductory textbook covering the Java programming language.
Version 9 is available in two editions, one using JavaFX for GUI programming and one using Swing.  Version 9
covers Java 17.  It was released in May 2022. The two editions can be accessed online
at [https://math.hws.edu/javanotes](https://math.hws.edu/javanotes) and
[https://math.hws.edu/javanotes-swing](https://math.hws.edu/javanotes-swing).
The front page of each web site has links for downloading the textbook in web-site, PDF, and eBook formats.

This repository contains the source files that are used to generate the two editions in all formats.
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
For editing the XML/XSLT files in both projects, your Eclipse should have the XML tools plugin installed.

To produce the book from the XML/XSLT files, you will need a copy of the XSLT processor 
[https://xml.apache.org/xalan-j/](Xalan-J).  The XSLT files use some features that are specific to Xalan-J.

(Full source files for Version 8 of the book can be obtained by checking out the tag *Version-8.1.3* in the repository.
Also, the initial commit for the repository consisted of the source files for Version 7 of the book.)

