#!/bin/bash

# THIS SCRIPT EXTRACTS THE SOLUTION PROGRAMS FROM THE EXERCISES

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


cd $JAVANOTES_SOURCE_DIR

rm -rf exercise-programs
mkdir exercise-programs

echo
echo Extracting programs from exercises...
echo
echo Running Xalan to extract program files...

if  $XALAN_COMMAND -xsl convert-exercise-progs.xsl -in javanotes8.xml ; then

   for dir in `ls -1 exercise-programs` ; do
       cp -r src-textio/textio exercise-programs/$dir/textio
   done
   cp src-c4/Mosaic.java src-c4/MosaicCanvas.java exercise-programs/chapter4
   cp src-c4/ArrayProcessor.java exercise-programs/chapter4
   cp src-c5/Deck.java src-c5/Hand.java src-c5/BlackjackHand.java src-c5/Card.java exercise-programs/chapter5
   cp src-c5/Deck.java src-c5/Hand.java src-c5/BlackjackHand.java src-c5/Card.java src-c6/cards.png exercise-programs/chapter6
   cp exercise-programs/chapter5/StatCalc.java exercise-programs/chapter6/StatCalc.java
   cp src-c8/Expr.java exercise-programs/chapter8
   mkdir exercise-programs/chapter12/netgame
   cp -r src-c12/netgame/common exercise-programs/chapter12/netgame
   mkdir exercise-programs/chapter12/netgame/newchat
   mv exercise-programs/chapter12/*Message*.java exercise-programs/chapter12/*Chat*.java exercise-programs/chapter12/netgame/newchat
   rm -r exercise-programs/chapter6/textio
   rm -r exercise-programs/chapter11/textio
   rm -r exercise-programs/chapter13/textio
   
   cp README-exercise-solutions.txt exercise-programs
   
   if [ ! -d "$BUILD_OUTPUT_DIR" ] ; then
       mkdir $BUILD_OUTPUT_DIR
   fi

   if ! mv exercise-programs $BUILD_OUTPUT_DIR ; then
      echo Exercise programs successfully generated, but could not be moved to $BUILD_OUTPUT_DIR.
      echo The output can be found in $JAVA_SOURCE_DIR/exercise-programs
      exit 1
    fi

   echo
   echo "BUILD-exercise-programs.sh completed."
   echo "Extracted exercise programs into $BUILD_OUTPUT_DIR/exercise-programs."
   echo
   exit 0
   
else
   echo
   echo "An error occurred while trying to run xalan on convert-exercise-progs.xsl."
   echo "Cleaning up and exiting from BUILD-exercise-programs.sh"
   echo
   rm -rf exercise-programs
   exit 1
fi





