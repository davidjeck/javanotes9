#!/bin/bash

# THIS SCRIPT CREATES JAR FILES FOR EXAMPLE PROGRAMS.  IT SHOULD WORK ON MAC AND LINUX.
# IT USES SOURCE CODE FILES FROM THE "source" DIRECTORY OF THE WEB SITE.
# IT MUST BE RUN IN THAT DIRECTORY.  IT CREATES A TEMP FOLDER IN THE CURRENT 
# DIRECTORY WHILE IT IS WORKING.

# THE SCRIPT CREATES A DIRECTORY NAMED "compiled-jar-files" INSIDE THE "source" DIRECTORY
# TO HOLD THE JAR FILES.  INISDE THAT DIRECTORY, JAR FILES WILL BE ORGANIZED 
# BY CHAPTER.

# THE SCRIPT NEEDS THE COMMANDS javac AND jar TO BE DEFINED, or alternative commands
# CAN BE SET IN THE NEXT TWO LINES, FOR EXAMPLE GIVING FULL PATHS TO THE COMMANDS

JAVAC="javac"
JAR="jar"

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
echo
sleep 1

mkdir temp

# $1 is chapter: c1, c2, ...
# $2 is main class (without .java)
# $3,$4,... are other files/directories to be included
function buildjar {
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
   $JAVAC `find . -name "*.java"`
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


# for building a program defines in one or more packages;
# the main class file is not copied, it's just used in the manifest.
# $1 is chapter: c1, c2, ...
# $2 is main class (with periods and without .java)
# $3 is the package dir that is to be created
# $4 can be "TextIO".  If so, TextIO is used to provide System in all .java files in $3
# then directories to be copied into the package dir
function buildpackagejar {
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
   $JAVAC `find . -name "*.java"`
   $JAR mfc manifest $NAME.jar `find . ! -name "*.java" -a ! -name manifest -a ! -name ".*" -a -type f`
   chmod +x $NAME.jar
   cd ..
   if [ ! -e "compiled-jar-files/$CHPT" ]; then
      mkdir compiled-jar-files/$CHPT
   fi
   mv temp/$NAME.jar compiled-jar-files/$CHPT/$NAME.jar
}



buildjar chapter1 GUIDemo
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
buildjar chapter3 MovingRects
buildjar chapter3 RandomCircles
buildTextIOjar chapter4 GuessingGame
buildTextIOjar chapter4 GuessingGame2
buildTextIOjar chapter4 RowsOfChars
buildTextIOjar chapter4 ThreeN2
buildjar chapter4 RandomMosaicWalk Mosaic.java MosaicPanel.java
buildTextIOjar chapter5 RollTwoPairs PairOfDice.java
buildjar chapter5 GrowingCircleAnimation CircleInfo.java
buildTextIOjar chapter5 HighLow Deck.java Card.java
buildjar chapter5 ShapeDraw
buildjar chapter6 HelloWorldGUI1
buildjar chapter6 HelloWorldGUI2
buildjar chapter6 SimpleColorChooser
buildjar chapter6 RandomStrings RandomStringsPanel.java
buildjar chapter6 ClickableRandomStrings RandomStringsPanel.java
buildjar chapter6 SimpleStamper
buildjar chapter6 SimpleTrackMouse
buildjar chapter6 SimplePaint
buildjar chapter6 RandomArt
buildjar chapter6 KeyboardAndFocusDemo
buildjar chapter6 SubKiller
buildjar chapter6 TextAreaDemo
buildjar chapter6 SliderDemo
buildjar chapter6 BorderDemo
buildjar chapter6 SliderAndButtonDemo
buildjar chapter6 SimpleCalc
buildjar chapter6 NullLayoutDemo
buildjar chapter6 HighLowGUI Card.java Hand.java Deck.java
buildjar chapter6 MosaicDraw MosaicDrawController.java MosaicPanel.java
buildjar chapter6 SimpleDialogDemo
buildjar chapter7 RandomStringsWithArray
buildTextIOjar chapter7 ReverseWithDynamicArray DynamicArrayOfInt.java
buildjar chapter7 SimplePaint2
buildTextIOjar chapter7 TestSymmetricMatrix SymmetricMatrix.java
buildjar chapter7 Life MosaicPanel.java
buildjar chapter7 Checkers
buildTextIOjar chapter8 LengthConverter2
buildTextIOjar chapter8 TryStatementDemo
buildTextIOjar chapter9 TowersOfHanoi
buildjar chapter9 Maze
buildjar chapter9 LittlePentominos MosaicPanel.java
buildjar chapter9 Blobs
buildTextIOjar chapter9 ListDemo StringList.java
buildTextIOjar chapter9 PostfixEval StackOfDouble.java
buildjar chapter9 DepthBreadth
buildTextIOjar chapter9 SortTreeDemo
buildTextIOjar chapter9 SimpleParser1
buildTextIOjar chapter9 SimpleParser2
buildTextIOjar chapter9 SimpleParser3
buildTextIOjar chapter10 WordListWithTreeSet
buildTextIOjar chapter10 WordListWithPriorityQueue
buildTextIOjar chapter10 SimpleInterpreter
buildTextIOjar chapter10 WordCount
buildTextIOjar chapter11 DirectoryList
buildTextIOjar chapter11 PhoneDirectoryFileDemo
buildjar chapter11 TrivialEdit
buildjar chapter11 SimplePaintWithFiles
buildTextIOjar chapter11 FetchURL
buildTextIOjar chapter11 ShowMyNetwork
buildTextIOjar chapter11 DateServer
buildTextIOjar chapter11 DateClient
buildTextIOjar chapter11 CLChatServer
buildTextIOjar chapter11 CLChatClient
buildjar chapter11 SimplePaintWithXML
buildjar chapter11 XMLDemo
buildTextIOjar chapter12 ThreadTest1
buildTextIOjar chapter12 ThreadTest2
buildjar chapter12 RandomArtWithThreads
buildjar chapter12 BackgroundComputationDemo
buildjar chapter12 MultiprocessingDemo1
buildjar chapter12 MultiprocessingDemo2
buildjar chapter12 MultiprocessingDemo3
buildjar chapter12 TowersOfHanoiGUI
buildjar chapter12 GUIChat
buildpackagejar chapter12 netgame.chat.ChatRoomServer netgame TextIO netgame/chat netgame/common
buildpackagejar chapter12 netgame.chat.ChatRoomWindow netgame netgame/chat netgame/common
buildpackagejar chapter12 netgame.tictactoe.Main netgame netgame/tictactoe netgame/common
buildpackagejar chapter12 netgame.fivecarddraw.Main netgame netgame/fivecarddraw netgame/common
buildjar chapter13 HighLowWithImages cards.png Card.java Hand.java Deck.java
buildjar chapter13 PaintWithOffScreenCanvas
buildjar chapter13 SoundAndCursorDemo snc_resources
buildjar chapter13 TransparencyDemo
buildjar chapter13 StrokeDemo
buildjar chapter13 PaintDemo QueenOfHearts.png TinySmiley.png
buildjar chapter13 ChoiceDemo
buildjar chapter13 SillyStamper stamper_icons
buildjar chapter13 StatesAndCapitalsTableDemo
buildjar chapter13 ScatterPlotTableDemo
buildjar chapter13 SimpleWebBrowserWithThread
buildjar chapter13 SimpleWebBrowser
buildjar chapter13 SimpleRTFEdit
buildjar chapter13 CustomComponentTest StopWatchLabel.java MirrorText.java
buildpackagejar chapter13 edu.hws.eck.mdb.Main edu/hws/eck edu/hws/eck/mdb


rm -rf temp

echo
echo "Note: No jar files were created for the following sample programs:"
echo
echo Examples for which no jar files are made:
echo chapter2 CreateProfile -- can overwrite a file in the current directory without warning
echo chapter4 CopyTextFile -- requires command-line arguments
echo chapter4 RandomMosaicWalk2 -- duplicates functionality of RandomMosaicWalk
echo chapter8 LengthConverter3 -- duplicates functionality of LenghtConverter2
echo chapter11 ReverseFile -- requires a file named data.dat in the current directory
echo chapter11 ReverseFileWithScanner -- requires a file named data.dat in the current directory
echo chapter11 ReverseFileWithResources -- requires a file named data.dat in the current directory
echo chapter11 CopyFile -- requires command-line parameters
echo chapter11 CopyFileAsResource -- requires command-line parameters
echo chapter12 BackgroundCompWithInvoke -- duplicates functionality of BackgroundComputationDemo
echo chapter12 CLDateServerWithThreads -- duplicates functionality of DateServer
echo chapter12 CLDateServerWithThreadPool -- duplicates functionality of DateServer
echo chapter12 CLMandelbrotMaster -- needs command line arguments
echo chapter12 CLMandelbrotWorked -- needs to run on several machines, or needs command-line arguments
echo
echo JAR FILES CAN BE FOUND IN THE DIRECTORY compiled-jar-files
echo

