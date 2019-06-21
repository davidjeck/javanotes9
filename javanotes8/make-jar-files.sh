#!/bin/bash

# IF YOU WANT THIS SCRIPT TO MAKE JAR FILES FOR JavaFX PROGRAMS, YOU NEED TO EDIT
# IT TO SET THE VALUE OF JAVAC_FX.  SEE COMMENTS ABOUT JAVA_FX BELOW.

# THIS SCRIPT CREATES JAR FILES FOR EXAMPLE PROGRAMS.  IT SHOULD WORK ON MAC AND LINUX.
# IT USES SOURCE CODE FILES FROM THE "source" DIRECTORY OF THE WEB SITE.
# IT MUST BE RUN IN THAT DIRECTORY.  IT CREATES A temp FOLDER IN THE CURRENT 
# DIRECTORY WHILE IT IS WORKING BUT DELETES IT AT THE END.

# THE SCRIPT CREATES A DIRECTORY NAMED "compiled-jar-files" INSIDE THE "source" DIRECTORY
# TO HOLD THE JAR FILES.  INISDE THAT DIRECTORY, JAR FILES WILL BE ORGANIZED BY CHAPTER.

# THE SCRIPT NEEDS THE COMMANDS javac AND jar TO BE DEFINED, OR ALTERNATIVE COMMANDS
# CAN BE SET IN THE NEXT TWO LINES, FOR EXAMPLE GIVING FULL PATHS TO THE COMMANDS.

JAVAC="javac"
JAR="jar"

# TO COMPILE PROGRAMS THAT USE JavaFX, JAVAC_FX MUST BE SET TO A javac COMMAND THAT 
# WILL WORK FOR COMPILING JavaFX PROGRAMS.  IF JAVAC_FX IS LEFT EMPTY, NO JavaFX
# JAR FILES WILL BE PRODUCED. 

JAVAC_FX=""

# IF YOU ARE USING A JDK THAT HAS JavaFX BUILT-IN, YOU CAN JUST SET JAVAC_FX TO
# HAVE THE SAME VALUE AS JAVAC BY UNCOMMENTING THE FIRST LINE BELOW.  IF YOU HAVE
# A SEPARATE JavaFX SDK, YOU CAN UNCOMMENT THE SECOND LINE BELOW AND EDIT IT TO
# USE THE PATH TO THE JavaFX SDK lib DIRECTORY, IN PLACE OF /home/eck/javafx-sdk-11/lib

#JAVAC_FX="$JAVAC"
#JAVAC_FX="$JAVAC --module-path=/home/eck/javafx-sdk-11/lib --add-modules=ALL-MODULE-PATH"


if [ -a "temp" ] ; then
   echo "A file or directory named temp already exists.  It would be deleted by"
   echo "this script.  Please delete it by hand before running this script."
   exit
fi

if ! type "$JAVAC" ; then
   echo "Can't continue without a javac command."
   exit
fi

if ! type "$JAR" ; then
   echo "Can't continue without a jar command."
   exit
fi

if [ -d "compiled-jar-files" ]; then
   echo "Note: compiled-jar-files folder already exists; jar files will be stored in that folder."
else
   echo "Creating folder named compiled-jar-files to hold the jar files."
   mkdir compiled-jar-files
fi

if [ -z "$JAVAC_FX" ] ; then
   echo
   echo JAVAC_FX is empty so jar files for programs that require JavaFX will NOT be built
fi

echo
sleep 1

mkdir temp

# $1 is chapter: c1, c2, ...
# $2 is main class (without .java)
# $3,$4,... are other files/directories to be included
function buildFXjar {
   if [ -z "$JAVAC_FX" ] ; then
      return;
   fi
   rm -rf temp/*
   echo
   echo Building $1/$2
   if [ ! -f "$1/$2.java" ]; then
       echo Cannot find file $1/$2.java !  Aborting build.
       return
   fi
   cp $1/$2.java temp
   CHPT="$1"
   NAME="$2"
   shift 2
   for x in $@ ; do
      if [ ! -e "$CHPT/$x" ]; then
          echo Cannot find $CHPT/$x !  Aborting build.
          return
      fi
      cp -a $CHPT/$x temp
   done
   echo "Main-Class: $NAME" > temp/manifest
   cd temp
   $JAVAC_FX `find . -name "*.java"`
   $JAR mfc manifest $NAME.jar `find . ! -name "*.java" -a ! -name manifest -a ! -name ".*" -a -type f`
   chmod +x $NAME.jar
   cd ..
   if [ ! -e "compiled-jar-files/$CHPT" ]; then
      mkdir compiled-jar-files/$CHPT
   fi
   mv temp/$NAME.jar compiled-jar-files/$CHPT/$NAME.jar
}

# For simple TextIO programs, using a GUI version of TextIO
# $1 is chapter: c1, c2, ...
# $2 is main class (without .java)
# $3,$4,... are other files/directories to be included
function buildTextIOjar {
   rm -rf temp/*
   echo
   echo Building $1/$2
   if [ ! -f "$1/$2.java" ]; then
       echo Cannot find file $1/$2.java !  Aborting build.
       return
   fi
   cp -a textiogui temp
   echo "import textiogui.TextIO;" > temp/$2.java
   echo "import textiogui.System;" >> temp/$2.java
   cat $1/$2.java >> temp/$2.java
   if grep "import textio.TextIO;" $1/$2.java > /dev/null ; then
      perl -i -p -e 's/import textio.TextIO;//' temp/$2.java
   fi
   CHPT="$1"
   NAME="$2"
   shift 2
   for x in $@ ; do
      if [ ! -e "$CHPT/$x" ]; then
          echo Cannot find $CHPT/$x !  Aborting build.
          return
      fi
      cp -a $CHPT/$x temp
   done
   echo "Main-Class: $NAME" > temp/manifest
   cd temp
   $JAVAC `find . -name "*.java"`
   $JAR mfc manifest $NAME.jar `find . ! -name "*.java" -a ! -name manifest -a ! -name ".*" -a -type f`
   chmod +x $NAME.jar
   cd ..
   if [ ! -e "compiled-jar-files/$CHPT" ]; then
      mkdir compiled-jar-files/$CHPT
   fi
   mv temp/$NAME.jar compiled-jar-files/$CHPT/$NAME.jar
}


# for building a program defined in one or more packages;
# the main class file is not copied, it's just used in the manifest.
# $1 is chapter: c1, c2, ...
# $2 is main class (with periods and without .java)
# $3 is the package dir that is to be created
# $4 can be "TextIO".  If so, TextIO is used to provide System in all .java files in $3
# then directories to be copied into the package dir
# This script does nothing if JavaFX can't be compiled, even if it's a TextIO program,
# since the only TextIO case is ChatRoomServer, which is useless without the GUI
# program ChatRoomWindow.
function buildpackagejar {
   if [ -z "$JAVAC_FX" ] ; then
       return;
   fi
   rm -rf temp/*
   echo
   echo Building $1/$2
   CHPT="$1"
   NAME="$2"
   DIR="$3"
   TEXTIO="no"
   shift 3
   if [ "TextIO" = "$1" ] ; then
      cp -a textiogui temp
      TEXTIO="yes"
      shift
   fi
   mkdir -p temp/$DIR
   for x in $@ ; do
      if [ ! -e "$CHPT/$x" ]; then
          echo "Cannot find $CHPT/$x !  Aborting build."
          return
      fi
      cp -a $CHPT/$x temp/$DIR
   done
   if [ "$TEXTIO" = "yes" ] ; then
      perl -i -p -e 's/System\./textiogui.System./g' `find temp/$DIR -name "*.java"`
   fi
   echo "Main-Class: $NAME" > temp/manifest
   cd temp
   $JAVAC_FX `find . -name "*.java"`
   $JAR mfc manifest $NAME.jar `find . ! -name "*.java" -a ! -name manifest -a ! -name ".*" -a -type f`
   chmod +x $NAME.jar
   cd ..
   if [ ! -e "compiled-jar-files/$CHPT" ]; then
      mkdir compiled-jar-files/$CHPT
   fi
   mv temp/$NAME.jar compiled-jar-files/$CHPT/$NAME.jar
}



buildFXjar chapter1 GUIDemo
buildTextIOjar chapter2 HelloWorld
buildTextIOjar chapter2 PrintSquare
buildTextIOjar chapter2 TimedComputation
buildTextIOjar chapter2 Interest
buildTextIOjar chapter2 EnumDemo
buildTextIOjar chapter2 Interest2
buildTextIOjar chapter3 Interest3
buildTextIOjar chapter3 ThreeN1
buildTextIOjar chapter3 ComputeAverage
buildTextIOjar chapter3 CountDivisors
buildTextIOjar chapter3 ListLetters
buildTextIOjar chapter3 LengthConverter
buildTextIOjar chapter3 ComputeAverage2
buildTextIOjar chapter3 AverageNumbersFromFile
buildTextIOjar chapter3 BirthdayProblem
buildTextIOjar chapter3 ReverseInputNumbers
buildFXjar chapter3 SimpleGraphicsStarter
buildFXjar chapter3 MovingRects
buildFXjar chapter3 RandomCircles
buildTextIOjar chapter4 GuessingGame
buildTextIOjar chapter4 GuessingGame2
buildTextIOjar chapter4 RowsOfChars
buildTextIOjar chapter4 ThreeN2
buildFXjar chapter4 RandomMosaicWalk Mosaic.java MosaicCanvas.java
buildTextIOjar chapter5 RollTwoPairs PairOfDice.java
buildFXjar chapter5 GrowingCircleAnimation CircleInfo.java
buildTextIOjar chapter5 HighLow Deck.java Card.java
buildFXjar chapter5 ShapeDraw
buildFXjar chapter6 HelloWorldFX
buildFXjar chapter6 SimpleColorChooser
buildFXjar chapter6 RandomStrings
buildFXjar chapter6 RandomCards Card.java Deck.java cards.png
buildFXjar chapter6 SimpleTrackMouse
buildFXjar chapter6 SimplePaint
buildFXjar chapter6 KeyboardEventDemo
buildFXjar chapter6 SubKiller
buildFXjar chapter6 TextInputDemo
buildFXjar chapter6 SliderDemo
buildFXjar chapter6 OwnLayoutDemo
buildFXjar chapter6 SimpleCalc
buildFXjar chapter6 HighLowGUI Card.java Hand.java Deck.java cards.png
buildFXjar chapter6 MosaicDraw MosaicCanvas.java
buildFXjar chapter7 RandomStringsWithArray
buildTextIOjar chapter7 ReverseWithDynamicArray DynamicArrayOfInt.java
buildFXjar chapter7 SimplePaint2
buildTextIOjar chapter7 TestSymmetricMatrix SymmetricMatrix.java
buildFXjar chapter7 Life MosaicCanvas.java
buildFXjar chapter7 Checkers
buildTextIOjar chapter8 LengthConverter2
buildTextIOjar chapter8 TryStatementDemo
buildTextIOjar chapter9 TowersOfHanoi
buildFXjar chapter9 Maze
buildFXjar chapter9 LittlePentominos MosaicCanvas.java
buildFXjar chapter9 Blobs
buildTextIOjar chapter9 ListDemo StringList.java
buildTextIOjar chapter9 PostfixEval StackOfDouble.java
buildFXjar chapter9 DepthBreadth
buildTextIOjar chapter9 SortTreeDemo
buildTextIOjar chapter9 SimpleParser1
buildTextIOjar chapter9 SimpleParser2
buildTextIOjar chapter9 SimpleParser3
buildTextIOjar chapter10 WordListWithTreeSet
buildTextIOjar chapter10 WordListWithPriorityQueue
buildTextIOjar chapter10 SimpleInterpreter
buildTextIOjar chapter10 WordCount
buildFXjar chapter10 RiemannSumStreamExperiment
buildTextIOjar chapter11 DirectoryList
buildTextIOjar chapter11 PhoneDirectoryFileDemo
buildFXjar chapter11 TrivialEdit
buildFXjar chapter11 SimplePaintWithFiles
buildFXjar chapter11 FetchURL
buildFXjar chapter11 ShowMyNetwork
buildFXjar chapter11 DateServer
buildFXjar chapter11 DateClient
buildFXjar chapter11 CLChatServer
buildFXjar chapter11 CLChatClient
buildFXjar chapter11 SimplePaintWithXML
buildFXjar chapter11 XMLDemo
buildTextIOjar chapter12 ThreadTest1
buildTextIOjar chapter12 ThreadTest2
buildFXjar chapter12 RandomArtWithThreads
buildFXjar chapter12 BackgroundComputationDemo
buildFXjar chapter12 MultiprocessingDemo1
buildFXjar chapter12 MultiprocessingDemo2
buildFXjar chapter12 MultiprocessingDemo3
buildTextIOjar chapter12 ThreadTest4
buildFXjar chapter12 TowersOfHanoiGUI
buildFXjar chapter12 GUIChat
buildpackagejar chapter12 netgame.chat.ChatRoomServer netgame TextIO netgame/chat netgame/common
buildpackagejar chapter12 netgame.chat.ChatRoomWindow netgame netgame/chat netgame/common
buildpackagejar chapter12 netgame.tictactoe.Main netgame netgame/tictactoe netgame/common
buildpackagejar chapter12 netgame.fivecarddraw.Main netgame netgame/fivecarddraw netgame/common
buildFXjar chapter13 BoundPropertyDemo
buildFXjar chapter13 CanvasResizeDemo
buildFXjar chapter13 StrokeDemo
buildFXjar chapter13 PaintDemo tile.png face-smile.png
buildFXjar chapter13 TransformDemo face-smile.png
buildFXjar chapter13 ToolPaint SimpleDialogs.java
buildFXjar chapter13 TestStopWatch StopWatchLabel.java
buildFXjar chapter13 SillyStamper stamper_icons
buildFXjar chapter13 EditListDemo
buildFXjar chapter13 SimpleTableDemo
buildFXjar chapter13 ScatterPlotTableDemo
buildFXjar chapter13 TestDialogs SimpleDialogs.java
buildFXjar chapter13 WebBrowser BrowserWindow.java SimpleDialogs.java
buildpackagejar chapter13 edu.hws.eck.mdbfx.Main edu edu/hws


rm -rf temp

echo
echo "Note: No jar files were created for the following sample programs:"
echo
echo chapter2  CreateProfile -- can overwrite a file in the current directory without warning
echo chapter2  SeparateEnumDemo -- duplicates functionality of EnumDemo
echo chapter4  CopyTextFile -- requires command-line arguments
echo chapter8  LengthConverter3 -- duplicates functionality of LenghtConverter2
echo chapter11 ReverseFile -- requires a file named data.dat in the current directory
echo chapter11 ReverseFileWithScanner -- requires a file named data.dat in the current directory
echo chapter11 ReverseFileWithResources -- requires a file named data.dat in the current directory
echo chapter11 CopyFile -- requires command-line parameters
echo chapter11 CopyFileAsResource -- requires command-line parameters
echo chapter12 ThreadTest3 -- duplicates the functionality of ThreadTest2
echo chapter12 CLDateServerWithThreads -- duplicates functionality of DateServer
echo chapter12 CLDateServerWithThreadPool -- duplicates functionality of DateServer
echo chapter12 CLMandelbrotMaster -- needs command line arguments
echo chapter12 CLMandelbrotWorker -- needs to run on several machines, or needs command-line arguments

if [ -z "$JAVAC_FX" ] ; then
   echo
   echo In addition, no jar files were made for any programs that require JavaFX
else
   echo chapter4  RandomMosaicWalk2 -- duplicates functionality of RandomMosaicWalk
   echo chapter12 MultiprocessorDemo4 -- duplicates the functionality of MultiprocessorDemo3
   if [ "$JAVAC" != "$JAVAC_FX" ] ; then
      echo
      echo Remember that jar files that use JavaFX must be run from the command line,
      echo using a java command that includes JavaFX options.
   fi
fi

echo
echo JAR FILES CAN BE FOUND IN THE DIRECTORY compiled-jar-files
echo

