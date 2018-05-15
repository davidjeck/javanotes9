#!/bin/bash

# THIS SCRIPT EXECUTES THE THREE BUILD SCRIPTS BUILD-web-site.sh,
# BUILD-pdf.sh, AND BUILD-linked-pdf.sh.  The shell variables that
# are used in those scripts can be set in the BUILD-env.sh script;
# see that file for more information.

# This script sends output from the other scripts to /dev/null,
# and you just see a report on the result.  If an error occurs,
# you can run the individual script to see the full output
# from that script.

# If you are not running the scripts from the directory that
# contains the scripts, you will have to change SCRIPT_DIR
# to the directory that contains the scripts.

SCRIPT_DIR=`pwd`

echo Running BUILD-web-site.sh...
if $SCRIPT_DIR/BUILD-web-site.sh > /dev/null 2> /dev/null ; then
   echo "Ran BUILD-web-site.sh successfully."
else
   echo "ERROR while running BUILD-web-site.sh."
fi

echo
echo Running BUILD-exercise-programs.sh...
if $SCRIPT_DIR/BUILD-exercise-programs.sh > /dev/null 2> /dev/null ; then
   echo "Ran BUILD-exercise-programs.sh successfully."
else
   echo "ERROR while running BUILD-web-site.sh."
fi

echo
echo Running BUILD-pdf.sh...
if $SCRIPT_DIR/BUILD-pdf.sh > /dev/null 2> /dev/null ; then
   echo "Ran BUILD-pdf.sh with no reported error."
   echo "(But check the output!)"
else
   echo "ERROR while running BUILD-pdf.sh."
fi

echo
echo Running BUILD-linked-pdf.sh...
if $SCRIPT_DIR/BUILD-linked-pdf.sh > /dev/null 2> /dev/null ; then
   echo "Ran BUILD-linked-pdf.sh with no reported error."
   echo "(But check the output!)"
else
  echo "ERROR while running BUILD-linked-pdf.sh."
fi

echo
if [ -e "$BUILD_OUTPUT_DIR" ] ; then
   echo Contents of $BUILD_OUTPUT_DIR after running scripts:
   ls -l $BUILD_OUTPUT_DIR
   echo
fi
