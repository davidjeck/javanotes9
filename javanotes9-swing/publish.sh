#!/bin/bash

VERSION='javanotes9-swing'

if [ ! -d build_output ] ; then
   echo Cannot find build_output directory
   exit
fi

echo Changing to build_output
cd build_output

if [ -e javanotes9.pdf -o -e javanotes9-linked.pdf -o -e web-site -o -e javanotes9.epub -o -e exercise-programs ] ; then
   echo Creating downloads directory
   mkdir downloads
else
   echo "Nothing to publish!"
   exit
fi

if [ -e "javanotes9.pdf" ] ; then
   echo Move javanotes9.pdf to downloads directory
   mv javanotes9.pdf downloads/$VERSION.pdf
fi

if [ -e "javanotes9-linked.pdf" ] ; then
   echo Move javanotes9-linked.pdf to downloads directory
   mv javanotes9-linked.pdf downloads/$VERSION-linked.pdf
fi

if [ -e "javanotes9.epub" ] ; then
   echo Move javanotes9.epub to downloads directory
   mv javanotes9.epub downloads/$VERSION.epub
fi

if [ -e "javanotes9.mobi" ] ; then
   echo Move javanotes9.mobi to downloads directory
   mv javanotes9.mobi downloads/$VERSION.mobi
fi

if [ -e "web-site" ] ; then
   echo Creating archives of web site and renaming web-site directory to $VERSION
   mv web-site $VERSION-web-site
   zip -r $VERSION.zip $VERSION-web-site > /dev/null
   mv $VERSION.zip downloads
#   tar cf $VERSION.tar $VERSION-web-site
#   bzip2 $VERSION.tar
#   mv $VERSION.tar.bz2 downloads
   mv $VERSION-web-site $VERSION
   mv $VERSION/news.html $VERSION/news-for-archive.html
   mv $VERSION/news-for-web.html $VERSION/news.html
   echo Creating the separate source download
   mkdir $VERSION-example-programs
   cp -a $VERSION/source $VERSION-example-programs
   cp $VERSION/README-running-the-examples.txt $VERSION-example-programs
   zip -r $VERSION-example-programs.zip $VERSION-example-programs > /dev/null
   mv $VERSION-example-programs.zip downloads
   rm -r $VERSION-example-programs
fi

if [ -e "exercise-programs" ] ; then
   echo Creating archive of exercise solutions
   mv exercise-programs $VERSION-exercise-solutions
   zip -r $VERSION-exercise-solutions.zip $VERSION-exercise-solutions > /dev/null
   mv $VERSION-exercise-solutions.zip downloads
   rm -r $VERSION-exercise-solutions
fi

cd ..

echo
echo Output in build_output folder.
echo

