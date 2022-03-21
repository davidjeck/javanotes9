#!/bin/bash

# This script will export the source files for the JavaFX edition of 
# Introduction to Programming using Java.  The exported directory
# will contain the files that are needed to build the book in 
# various formats, plus a README.txt file with mor information.
# The source for the exports is the Eclipse project directory
# that contains this script.


# ----------------------------------------------------------------
# ---- Edit the following variables for your environment! --------
#-----------------------------------------------------------------

# PROJECT is the name of the Eclipse project directory (the directory
# that contains this script!).  javanotes9 should be correct.

PROJECT='javanotes9'

# SOURCE_DIR refers the directory that contains the Eclipse project,
# so $SOURCE_DIR/$PROJECT is the Eclipse project directory.

SOURCE_DIR='/home/eck/git/javanotes9'

# VERSION is only used as part of the name of the EXPORT_DIR.
# EXPORT_DIR is directory to which the files are exported.
#
# WARNING:  If the EXPORT_DIR already exists, its current contents
#           will be deleted!!!

VERSION='javanotes-9.0'
EXPORT_DIR="/home/eck/Desktop/$VERSION-source"

# The scripts for building the book use xalan-j to process the XSLT
# files.  You can set XALAN_DIR to the directory that contains the
# xalan-j .jar files, or you can leave it empty and set the XALAN_DIR
# directory in BUILD-env.sh.

XALAN_DIR="/home/eck/xalan-j_2_7_2"

#---------------------------------------------------------------

if [ ! -x "$SOURCE_DIR" ] ; then
   echo "Source directory $SOURCE_DIR does not exist!"
   echo "You need to edit this script to specify directories!"
   exit
fi

if [ ! -x "$SOURCE_DIR/$PROJECT" ] ; then
   echo "Project directory $SOURCE_DIR/$PROJECT does not exist!"
   echo "You need to edit this script to specify directories!"
   exit
fi

if [ -x "$EXPORT_DIR" ] ; then
   rm -r $EXPORT_DIR/*
else
   mkdir $EXPORT_DIR
fi

cp -r $SOURCE_DIR/$PROJECT $EXPORT_DIR

cd $EXPORT_DIR

perl -i -p -e 's/\t/    /g' `find . -name "*.java"`
perl -i -p -e 's/<!DOCTYPE.*javanotes9.dtd" *>//' `find . -name "*.xml" -and ! -name "javanotes9*"`

rm $PROJECT/export-source*.sh
mv $PROJECT/publish.sh .
mv $PROJECT/BUILD* .
chmod +x BUILD*.sh publish.sh
cp $PROJECT/README.txt .
if [ -d "$XALAN_DIR" ] ; then
   ln -s $XALAN_DIR xalan
fi

echo Exported source to $EXPORT_DIR
