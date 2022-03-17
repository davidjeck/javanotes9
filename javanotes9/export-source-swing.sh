#!/bin/bash

# This script will export the source files for the Swing edition of 
# Introduction to Programming using Java.  The exported directory
# will contain the files that are needed to build the book in 
# various formats, plus a README.txt file with mor information.
# The source for the exports is the Eclipse project directory
# that contains this script.

# ----------------------------------------------------------------
# ---- Edit the following variables for your environment! --------
#-----------------------------------------------------------------

# PROJECT is the name of the main Eclipse project directory (the directory
# that contains this script!).  javanotes9 should be correct.
# PROJECT_SWING is the name of the Eclipse project that contains the
# additional needed for the Swing edition.   This script copies files
# from PROJECT, then modifies the output using files from PROJECT_SWING. 

PROJECT='javanotes9'
PROJECT_SWING="$PROJECT-swing"

# SOURCE_DIR refers the directory that contains the two Eclipse projects,
# so $SOURCE_DIR/$PROJECT is the JavaFX Eclipse project directory, and
# $SOURCE_DIR/$PROJECT_SWING is the supplemental Swing Eclipse project.

SOURCE_DIR='/home/eck/git/javanotes9'

# VERSION is only used as part of the name of the EXPORT_DIR.
# EXPORT_DIR is directory to which the files are exported.
#
# WARNING:  If the EXPORT_DIR already exists, its current contents
#           will be deleted!!!

VERSION='javanotes-9.0-swing'
EXPORT_DIR="/home/eck/Desktop/$VERSION-source"

# The scripts for building the book use xalan-j to process the XSLT
# files.  You can set XALAN_DIR to the directory that contains the
# xalan-j .jar files, or you can leave it empty and set the XALAN_DIR
# directory in BUILD-env.sh.

XALAN_DIR="/home/eck/xalan-j_2_7_2"

#---------------------------------------------------------------

if [ -x "$EXPORT_DIR" ] ; then
   rm -r $EXPORT_DIR/*
else
   mkdir $EXPORT_DIR
fi

#First copy all the files for the JavaFX edition

cp -r $SOURCE_DIR/$PROJECT $EXPORT_DIR

#Remove chapters 6 and 13, which cover the FX GUI and will be entirely replaced

rm -r $EXPORT_DIR/$PROJECT/c6-GUI1
rm -r $EXPORT_DIR/$PROJECT/c13-GUI2
rm -r $EXPORT_DIR/$PROJECT/src-c6
rm -r $EXPORT_DIR/$PROJECT/src-c13

#Remove files that are not needed for Swing (and are not replaced by files of the same name)

FILES_TO_REMOVE="c2-basics/eclipse-fx.png images-tex/eclipse-fx.eps
  src-c3/RandomCircles.java
  images-tex/alerts.eps images-tex/bound-property-demo.eps images-tex/colorRadioButtons.eps
  images-tex/edit-list-demo.eps images-tex/gradient-rect.eps images-tex/HelloWorldFX-screenshot.eps 
  images-tex/linear-gradient.eps images-tex/line-attributes.eps images-tex/mosaic-draw.eps 
  images-tex/radial-gradient.eps images-tex/randomCards.eps images-tex/randomStrings.eps 
  images-tex/scatter-plot.eps images-tex/scene-graph.eps images-tex/scratch-off.eps 
  images-tex/smudge-rect.eps images-tex/StatCalcGUI-less-ugly.eps images-tex/StatCalcGUI-ugly.eps 
  images-tex/table-demo.eps images-tex/TextInputDemo.eps images-tex/transforms.eps"
for f in $FILES_TO_REMOVE ; do rm $EXPORT_DIR/$PROJECT/$f ; done

#Copy files for the Swing edition, adding chapters 6 and 13 and adding/replacing other files

cp -r $SOURCE_DIR/$PROJECT_SWING/* $EXPORT_DIR/$PROJECT

cd $EXPORT_DIR

# We need Card.java, Deck.java, Hand.java in the source directory for chapter 13.

cp $PROJECT/src-extra/Card.java $PROJECT/src-extra/Deck.java $PROJECT/src-extra/Hand.java $PROJECT/src-c13

#Remove source folder src-extra, which contains duplicates of files from javanotes9 that are
#also needed in javanotes9-swing

rm -r $PROJECT/src-extra

perl -i -p -e 's/\t/    /g' `find . -name "*.java"`
perl -i -p -e 's/<!DOCTYPE.*javanotes9.dtd" *>//' `find . -name "*.xml" -and ! -name "javanotes9*"`

rm $PROJECT/export-source*.sh
mv $PROJECT/publish.sh .
mv $PROJECT/BUILD*.sh .
chmod +x BUILD*.sh publish.sh
cp $PROJECT/README.txt .
if [ -d "$XALAN_DIR" ] ; then
   ln -s $XALAN_DIR xalan
fi

echo Exported source to $EXPORT_DIR

