#!/bin/bash

VERSION='javanotes-7.0.3'
PROJECT='javanotes7'

SOURCE_DIR='/Users/eck/eclipse-workspace'
EXPORT_DIR="/Users/eck/Desktop/$VERSION-source"
XALAN_DIR="/Users/eck/xalan-j_2_7_0"

if [ -x "$EXPORT_DIR" ] ; then
   rm -r $EXPORT_DIR/*
else
   mkdir $EXPORT_DIR
fi

cp -r $SOURCE_DIR/$PROJECT $EXPORT_DIR

cd $EXPORT_DIR

perl -i -p -e 's/\t/    /g' `find . -name "*.java"`
perl -i -p -e 's/<!DOCTYPE.*javanotes7.dtd" *>//' `find . -name "*.xml" -and ! -name "javanotes7*"`
rm -r `find . -name "CVS"`

rm $PROJECT/export-source.sh
rm $PROJECT/export-source-Mac.sh
mv $PROJECT/publish.sh .
mv $PROJECT/BUILD* .
chmod +x BUILD*.sh publish.sh
cp $PROJECT/README.txt .
ln -s $XALAN_DIR xalan
cd ..


