#!/bin/bash

# THIS SCRIPT BUILDS THE JAVANOTES PDF FILES FOR lulu.com.

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

rm -rf tex
mkdir tex

echo
echo Building PDF...
echo
echo Running Xalan to create LaTeX source files...

for PART in all one two ; do 

  if ! $XALAN_COMMAND -PARAM lulu $PART -xsl convert-lulu.xsl -in javanotes9-tex.xml -out tex/javanotes9-$PART.tex ; then
    echo
    echo "An error occurred while trying to run xalan on convert-lulu.xsl."
    echo "Cleaning up and exiting from BUILD-PDF.sh"
    echo
    rm -rf tex
    exit 1
  fi
  echo
  echo Created tex/javanotes9-$PART.tex
done

echo
echo Copying other files...

mkdir tex/images
cp images-tex/* tex/images
cp texmacros.tex tex

cd tex

echo
echo Running latex three times for each PDF...
echo

for PART in all one two ; do 

  echo
  echo javanotes9-$PART
  echo

  $LATEX_COMMAND javanotes9-$PART.tex
  $LATEX_COMMAND javanotes9-$PART.tex
  $LATEX_COMMAND javanotes9-$PART.tex

  if [ ! -e "javanotes9-$PART.dvi" ] ; then
    echo
    echo "An error occurred while trying to run latex on javanotes9-$PART.tex."
    echo "Exiting from BUILD-PDF.sh; latex files are in $JAVA_SOURCE_DIR/tex"
    echo
    cd ..
    exit 1
  fi

done

echo
echo
echo Running dvipdf... 
echo

for PART in all one two ; do 

  echo
  echo javanotes9-$PART
  echo

  $DVIPDF_COMMAND javanotes9-$PART.dvi

  if [ ! -e "javanotes9-$PART.pdf" ] ; then
     echo
     echo "An error occurred while trying to run dvipdf on javanotes9-$PART.dvi."
     echo "Exiting from BUILD-lulu.sh; latex files are in $JAVA_SOURCE_DIR/tex"
     echo
     cd ..
     exit 1
  fi

  if [ ! -e "$BUILD_OUTPUT_DIR" ] ; then
     mkdir $BUILD_OUTPUT_DIR
  fi
   
  if ! mv javanotes9-$PART.pdf $BUILD_OUTPUT_DIR ; then
    echo "PDF file successfully generated, but could not be moved to $BUILD_OUTPUT_DIR."
    echo "PDF can be found in $JAVA_SOURCE_DIR/tex"
    cd ..
    exit 1
  fi
  
done

echo
echo "BUILD-lulu.sh completed."
echo "javanotes9-*.pdf created in $BUILD_OUTPUT_DIR"

cd ..

rm -rf $BUILD_OUTPUT_DIR/latex-source-lulu
mv tex $BUILD_OUTPUT_DIR/latex-source-lulu
echo latex-source created in $BUILD_OUTPUT_DIR

echo
exit 0

