#!/bin/bash

# THIS SCRIPT BUILDS JAVANOTES AS AN EPUB EBOOK.
# IT SHOULD BE CONSIDERED (PERMANENTLY) EXPERIMENTAL.

# The script can convert the .epub file to a .mobi file
# if an appropriate utilty is available and you uncomment
# and edit one of the following lines.  The first uses
# an old utility named kindlegen from amazon.com that is no 
# longer available for download.  The second uses a command
# that is installed on Linux as part of the Calibre ebook
# viewer.

# KINDLEGEN='/home/eck-mint13/KindleGen/kindlegen'
# EBOOKCONVERT='/usr/bin/ebook-convert'

# VARIABLES USED IN THIS SCRIPT CAN BE SET IN BUILT-env.sh; see that file
# for more information.

source BUILD-env.sh

# can't do anything if user hasn't set up xalan, so check that first.

if [ ! -f $XALAN_DIR/xalan.jar ] ; then
   echo Cannot find the xalan.jar file in $XALAN_DIR
   echo Cannot proceed without xalan.
   echo Did you set up Xalan-J correctly?  See README.txt.
   exit 1
fi

XALAN_COMMAND="$JAVA_COMMAND -cp $XALAN_DIR/xalan.jar:$XALAN_DIR/serializer.jar:$XALAN_DIR/xercesImpl.jar:$XALAN_DIR/xml-apis.jar org.apache.xalan.xslt.Process"

function copyfiles() {
   for f in `ls -1 $1` ; do
       ext="${f##*.}"
#       ext="${ext,,}"
       if [ "$ext" = "gif" -o "$ext" = "jpg" -o "$ext" = "jpeg" -o "$ext" = "png" ] ; then
          cp $1/$f $2
       fi
   done
}

echo
echo Building epub...

cd $JAVANOTES_SOURCE_DIR

echo in directory `pwd`

rm -rf epub

echo
echo Creating directory epub in $JAVANOTES_SOURCE_DIR...

if ! cp -r epub-files epub ; then
   echo "Error: could not create epub!"
   exit 1
fi

echo
echo Running Xalan to create epub files...

if  $XALAN_COMMAND -xsl convert-epub.xsl -in javanotes9-epub.xml ; then

   if [ ! -e "epub/OEBPS/javanotes9.opf" ] ; then
      echo Some error occurred while running xalan; epub/OEBPS/javanotes9.opf not created
      exit 1
   fi
   if [ ! -d "epub/OEBPS/c1" ] ; then
      echo Some error occurred while running xalan; epub/OEBPS/c1 not created
      exit 1
   fi
   
   echo
   echo Copying other files...

   cp javanotes-epub.css epub/OEBPS
   cp javanotes9-cover-518x675.jpg epub/OEBPS
   copyfiles c1-overview epub/OEBPS/c1
   copyfiles c2-basics epub/OEBPS/c2
   copyfiles c3-control epub/OEBPS/c3
   copyfiles c4-subroutines epub/OEBPS/c4
   copyfiles c5-OOP epub/OEBPS/c5
   copyfiles c6-GUI1 epub/OEBPS/c6
   copyfiles c7-arrays epub/OEBPS/c7
   copyfiles c8-robustness epub/OEBPS/c8
   copyfiles c9-recursion epub/OEBPS/c9
   copyfiles c10-generics epub/OEBPS/c10
   copyfiles c11-IO epub/OEBPS/c11
   copyfiles c12-threads epub/OEBPS/c12
   copyfiles c13-GUI2 epub/OEBPS/c13
   
   if [ ! -e "$BUILD_OUTPUT_DIR" ] ; then
      mkdir $BUILD_OUTPUT_DIR
   fi
   
   cd epub
   zip -X -Z store javanotes9.zip mimetype
   zip -Z store -r javanotes9.zip META-INF
   zip -r javanotes9.zip OEBPS
   cd ..

   if ! mv epub/javanotes9.zip $BUILD_OUTPUT_DIR/javanotes9.epub ; then
      echo Epub successfully generated, but could not be moved to $BUILD_OUTPUT_DIR.
      echo The work directory can be found in $JAVA_SOURCE_DIR/epub
      exit 1
    fi
    
   echo
   echo "BUILD-epub.sh completed."
   echo "Created javanotes9.epub in $BUILD_OUTPUT_DIR."
   echo
   
   if [ -x "$KINDLEGEN" ]; then
      echo
      echo Converting EPUB to MOBI with kindlegen
      echo
      cd $BUILD_OUTPUT_DIR
      $KINDLEGEN javanotes9.epub 
      if [ -f "javanotes9.mobi" ]; then
         echo
         echo Finished EPUB and MOBI ebook generation.
      else
         echo
         echo EPUB was created successfully, but MOBI could not be created.
      fi
   elif [ -x "$EBOOKCONVERT" ]; then
      echo
      echo Converting epub to mobi with ebook-convert
      echo
      cd $BUILD_OUTPUT_DIR
      $EBOOKCONVERT javanotes9.epub javanotes9.mobi
      if [ -f "javanotes9.mobi" ]; then
         echo
         echo Finished EPUB and MOBI ebook generation.
      else
         echo
         echo EPUB was created successfully, but MOBI could not be created.
      fi
   else
      echo
      echo Skipping conversion of EPUB to MOBI because no conversion utility is available.
   fi
   
   exit 0
   
else
   echo
   echo "An error occurred while trying to run xalan on convert-epub.xsl."
   exit 1
fi





