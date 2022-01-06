#!/bin/bash

# THIS SCRIPT SETS SHELL VARIABLES THAT ARE USED IN THE SCRIPTS
# BUILD-all.sh, BUILD-web-site.sh, BUILD-pdf.sh, and BUILD-linked-pdf.sh.
# Variables that already have a value are NOT reset by this script.


# TOP_DIR is the default directory; other directories are defined
# in this script relative to TOP_DIR.  By default, it is the
# directory in which the script is run.

TOP_DIR=`pwd`


# JAVANOTES_SOURCE_DIR is the directory that contains all the source
# files from which the web site and PDF files will be built.

if [ -z "$JAVANOTES_SOURCE_DIR" ] ; then
   JAVANOTES_SOURCE_DIR="$TOP_DIR/javanotes8"
fi


# BUILD_OUTPUT_DIR is the directory where the output of the build
# process will be placed, if the build succeeds.

if [ -z "$BUILD_OUTPUT_DIR" ] ; then
   BUILD_OUTPUT_DIR="$TOP_DIR/build_output"
fi


# XALAN_DIR is a directory that contains all the jar files that
# are needed to run the xalan XSLT processor.  A standard xalan2 download
# should work.  The jar files are: xalan.jar, serializer.jar, 
# xercesImpl.jar, and xml-apis.jar.  (To use the default setup, the
# TOP_DIR should contain a directory named xalan that contains 
# these jar files.  A link to a xalan download directory would work.)

if [ -z "$XALAN_DIR" ] ; then
   XALAN_DIR="$TOP_DIR/xalan"
fi


# JAVA_COMMAND is the command that is used to execute Java programs.

if [ -z "$JAVA_COMMAND" ] ; then
   JAVA_COMMAND="java"
fi


# LATEX_COMMAND is the command that runs latex, part of the TeX typesetting
# system.  LaTeX is an intermediate step in the production of the PDF files.
# The default command includes the option "--interaction=batchmode", which
# prevents latex from printing error messages and from stopping for user 
# input when an error occurs.  (This surpresses a lot of output from latex!)

if [ -z "$LATEX_COMMAND" ] ; then
   LATEX_COMMAND="latex --interaction=batchmode"
fi


# DVIPDF_COMMAND is the command that runs dvipdf, part of the TeX typesetting
# system.  This is a step in the production of the PDF files.

if [ -z "$DVIPDF_COMMMAND" ] ; then
   DVIPDF_COMMAND="dvipdf"
fi


# If KEEP_LATEX is "no", then the LaTeX files, which are created during the
# production of the PDF files, are discarded.  Changing the values to anything
# else will cause the LaTeX files to be saved in BUILD_OUTPUT_DIR.

if [ -z "$KEEP_LATEX" ] ; then
   KEEP_LATEX="no"
fi


export TOP_DIR
export BUILD_OUTPUT_DIR
export JAVANOTES_SOURCE_DIR 
export XALAN_DIR
export JAVA_COMMAND
export LATEX_COMMAND
export DVIPDF_COMMAND
export KEEP_LATEX
export SCRIPT_DIR
