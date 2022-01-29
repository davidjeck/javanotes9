#!/bin/bash

VERSION='javanotes-9.0-swing'
PROJECT='javanotes9'
#project javanotes9-swing contains alternative files for the Swing version of the textbook
PROJECT_SWING='javanotes9-swing'

SOURCE_DIR='/home/eck/git/javanotes9'
EXPORT_DIR="/home/eck/Desktop/$VERSION-source"
XALAN_DIR="/home/eck/xalan-j_2_7_1"

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
ln -s $XALAN_DIR xalan
cd ..

echo Exported source to $EXPORT_DIR

