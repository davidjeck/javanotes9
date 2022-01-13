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

cp -r $SOURCE_DIR/$PROJECT $EXPORT_DIR
cp -r $SOURCE_DIR/$PROJECT_SWING/* $EXPORT_DIR/$PROJECT

cd $EXPORT_DIR

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

