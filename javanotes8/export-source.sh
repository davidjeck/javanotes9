#!/bin/bash

VERSION='javanotes-8.0'
PROJECT='javanotes8'

SOURCE_DIR='/home/eck/git/javanotes8'
EXPORT_DIR="/home/eck/Desktop/$VERSION-source"
XALAN_DIR="/home/eck/xalan-j_2_7_1"

if [ -x "$EXPORT_DIR" ] ; then
   rm -r $EXPORT_DIR/*
else
   mkdir $EXPORT_DIR
fi

cp -r $SOURCE_DIR/$PROJECT $EXPORT_DIR

cd $EXPORT_DIR

perl -i -p -e 's/\t/    /g' `find . -name "*.java"`
perl -i -p -e 's/<!DOCTYPE.*javanotes8.dtd" *>//' `find . -name "*.xml" -and ! -name "javanotes8*"`

rm $PROJECT/export-source.sh
rm $PROJECT/export-source-Mac.sh
mv $PROJECT/publish.sh .
mv $PROJECT/BUILD* .
chmod +x BUILD*.sh publish.sh
cp $PROJECT/README.txt .
ln -s $XALAN_DIR xalan
cd ..

echo Exported source to $EXPORT_DIR

