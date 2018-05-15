#!/bin/bash

# THIS SCRIPT BUILDS THE JAVANOTES WEB SITE.

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
   dest="web/source/chapter$1"
   mkdir -p $dest
   cp c$1-$2/* web/c$1
   rm web/c$1/*.xml
   cp -a src-c$1/* $dest
   shift 2
   for f in $@ ; do
       cp -a $f $dest
   done
}

cd $JAVANOTES_SOURCE_DIR

rm -rf web
mkdir web

echo
echo Building web site...
echo
echo Running Xalan to create web site html files...

if  $XALAN_COMMAND -xsl convert-web.xsl -in javanotes8.xml ; then

   echo Copying other web site files...

   cp javanotes.css web
   cp -r TextIO_Javadoc web
   cp javanotes8-cover-180x235.png web
   cp news.html web
   cp news-for-web.html web
   cp README.txt web/README-full-source.txt
   cp -r src-textio/textiogui web/source/textiogui
   cp make-jar-files.sh web/source
   cp make-jar-files.bat web/source
   chmod +x web/source/make-jar-files.sh
   cp README-running-the-examples.txt web
   cp README-exercise-solutions.txt web

   copyfiles 1 overview
   copyfiles 2 basics src-textio/TextIO.java
   copyfiles 3 control src-textio/TextIO.java
   copyfiles 4 subroutines src-textio/TextIO.java
   copyfiles 5 OOP src-textio/TextIO.java
   copyfiles 6 GUI1 src-c5/Hand.java src-c5/Deck.java src-c5/Card.java src-c5/BlackjackHand.java src-c4/MosaicPanel.java
   copyfiles 7 arrays src-textio/TextIO.java src-c4/MosaicPanel.java
   copyfiles 8 robustness src-textio/TextIO.java
   copyfiles 9 recursion src-textio/TextIO.java src-c4/MosaicPanel.java
   copyfiles 10 generics src-textio/TextIO.java
   copyfiles 11 IO src-textio/TextIO.java
   copyfiles 12 threads src-textio/TextIO.java
   copyfiles 13 GUI2 src-c5/Hand.java src-c5/Deck.java src-c5/Card.java 
   
   if [ ! -e "$BUILD_OUTPUT_DIR" ] ; then
      mkdir $BUILD_OUTPUT_DIR
   else
      rm -rf $BUILD_OUTPUT_DIR/web-site
   fi
   
   if [ ! -e "web/index.html" ] ; then
      echo Some error occurred while creating the web site.
      exit 1
   fi
   
   if ! mv web $BUILD_OUTPUT_DIR/web-site ; then
      echo Web site successfully generated, but could not be moved to $BUILD_OUTPUT_DIR.
      echo The web site can be found in $JAVA_SOURCE_DIR/web
      exit 1
    fi

   echo
   echo "BUILD-web-site.sh completed."
   echo "Created Javanotes web site in $BUILD_OUTPUT_DIR/web-site."
   echo
   exit 0
   
else
   echo
   echo "An error occurred while trying to run xalan on convert-web.xsl."
   echo "Cleaning up and exiting from BUILD-web-site.sh"
   echo
   rm -rf web
   exit 1
fi





