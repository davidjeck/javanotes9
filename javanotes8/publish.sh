#!/bin/bash

VERSION='javanotes8.0.3'
PROJECT='javanotes8'

if [ ! -d build_output ] ; then
   echo Cannot find build_output directory
   exit
fi

echo Changing to build_output
cd build_output

UPLOADS=""

if [ -e javanotes8.pdf -o -e javanotes8-linked.pdf -o -e web-site ] ; then
   echo Creating downloads directory
   mkdir downloads
else
   echo "Nothing to publish!"
   exit
fi

if [ -e "javanotes8.pdf" ] ; then
   echo Move javanotes8.pdf to downloads directory
   mv javanotes8.pdf downloads/$VERSION.pdf
fi

if [ -e "javanotes8-linked.pdf" ] ; then
   echo Move javanotes8-linked.pdf to downloads directory
   mv javanotes8-linked.pdf downloads/$VERSION-linked.pdf
fi

if [ -e "javanotes8.epub" ] ; then
   echo Move javanotes8.epub to downloads directory
   mv javanotes8.epub downloads/$VERSION.epub
fi

if [ -e "javanotes8.mobi" ] ; then
   echo Move javanotes8.mobi to downloads directory
   mv javanotes8.mobi downloads/$VERSION.mobi
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

#echo Copying files to web site.
#rsync -e ssh -r build_output/* dje@math.hws.edu:/var/www/htdocs/eck/cs124

