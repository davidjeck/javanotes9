
echo off

REM THIS DOS BATCH SCRIPT CREATES JAR FILES FOR EXAMPLE PROGRAMS. 
REM IT USES SOURCE CODE FILES FROM THE "source" DIRECTORY OF THE WEB SITE.
REM IT MUST BE RUN IN THAT DIRECTORY.  IT CREATES A TEMP FOLDER IN THE CURRENT 
REM DIRECTORY WHILE IT IS WORKING.

REM THE SCRIPT CREATES A DIRECTORY NAMED "compiled-jar-files" INSIDE THE "source" DIRECTORY
REM TO HOLD THE JAR FILES.  INISDE THAT DIRECTORY, JAR FILES WILL BE ORGANIZED 
REM BY CHAPTER.

REM THE SCRIPT NEEDS THE COMMANDS javac AND jar TO BE DEFINED, or alternative commands
REM CAN BE SET IN THE NEXT TWO LINES, FOR EXAMPLE GIVING FULL PATHS TO THE COMMANDS


set JAVAC=javac
set JAR=jar

echo.

if exist compiled-jar-files (
   echo Note: compiled-jar-files folder already exists; jar files will be stored in that folder.
) else (
   echo Creating folder named compiled-jar-files to hold the jar files.
   mkdir compiled-jar-files
)

call :buildjar chapter1 GUIDemo
call :buildtio chapter2 HelloWorld
call :buildtio chapter2 PrintSquare
call :buildtio chapter2 TimedComputation
call :buildtio chapter2 Interest
call :buildtio chapter2 EnumDemo
call :buildtio chapter2 Interest2
call :buildtio chapter3 Interest3
call :buildtio chapter3 ThreeN1
call :buildtio chapter3 ComputeAverage
call :buildtio chapter3 CountDivisors
call :buildtio chapter3 ListLetters
call :buildtio chapter3 LengthConverter
call :buildtio chapter3 ComputeAverage2
call :buildtio chapter3 AverageNumbersFromFile
call :buildtio chapter3 BirthdayProblem
call :buildtio chapter3 ReverseInputNumbers
call :buildjar chapter3 MovingRects
call :buildjar chapter3 RandomCircles
call :buildtio chapter4 GuessingGame
call :buildtio chapter4 GuessingGame2
call :buildtio chapter4 RowsOfChars
call :buildtio chapter4 ThreeN2
call :buildjar chapter4 RandomMosaicWalk Mosaic.java MosaicPanel.java
call :buildtio chapter5 RollTwoPairs PairOfDice.java
call :buildjar chapter5 GrowingCircleAnimation CircleInfo.java
call :buildtio chapter5 HighLow Deck.java Card.java
call :buildjar chapter5 ShapeDraw
call :buildjar chapter6 HelloWorldGUI1
call :buildjar chapter6 HelloWorldGUI2
call :buildjar chapter6 SimpleColorChooser
call :buildjar chapter6 RandomStrings RandomStringsPanel.java
call :buildjar chapter6 ClickableRandomStrings RandomStringsPanel.java
call :buildjar chapter6 SimpleStamper
call :buildjar chapter6 SimpleTrackMouse
call :buildjar chapter6 SimplePaint
call :buildjar chapter6 RandomArt
call :buildjar chapter6 KeyboardAndFocusDemo
call :buildjar chapter6 SubKiller
call :buildjar chapter6 TextAreaDemo
call :buildjar chapter6 SliderDemo
call :buildjar chapter6 BorderDemo
call :buildjar chapter6 SliderAndButtonDemo
call :buildjar chapter6 SimpleCalc
call :buildjar chapter6 NullLayoutDemo
call :buildjar chapter6 HighLowGUI Card.java Hand.java Deck.java
call :buildjar chapter6 MosaicDraw MosaicDrawController.java MosaicPanel.java
call :buildjar chapter6 SimpleDialogDemo
call :buildjar chapter7 RandomStringsWithArray
call :buildtio chapter7 ReverseWithDynamicArray DynamicArrayOfInt.java
call :buildjar chapter7 SimplePaint2
call :buildtio chapter7 TestSymmetricMatrix SymmetricMatrix.java
call :buildjar chapter7 Life MosaicPanel.java
call :buildjar chapter7 Checkers
call :buildtio chapter8 LengthConverter2
call :buildtio chapter8 TryStatementDemo
call :buildtio chapter9 TowersOfHanoi
call :buildjar chapter9 Maze
call :buildjar chapter9 LittlePentominos MosaicPanel.java
call :buildjar chapter9 Blobs
call :buildtio chapter9 ListDemo StringList.java
call :buildtio chapter9 PostfixEval StackOfDouble.java
call :buildjar chapter9 DepthBreadth
call :buildtio chapter9 SortTreeDemo
call :buildtio chapter9 SimpleParser1
call :buildtio chapter9 SimpleParser2
call :buildtio chapter9 SimpleParser3
call :buildtio chapter10 WordListWithTreeSet
call :buildtio chapter10 WordListWithPriorityQueue
call :buildtio chapter10 SimpleInterpreter
call :buildtio chapter10 WordCount
call :buildtio chapter11 DirectoryList
call :buildtio chapter11 PhoneDirectoryFileDemo
call :buildjar chapter11 TrivialEdit
call :buildjar chapter11 SimplePaintWithFiles
call :buildtio chapter11 FetchURL
call :buildtio chapter11 ShowMyNetwork
call :buildtio chapter11 DateServer
call :buildtio chapter11 DateClient
call :buildtio chapter11 CLChatServer
call :buildtio chapter11 CLChatClient
call :buildjar chapter11 SimplePaintWithXML
call :buildjar chapter11 XMLDemo
call :buildtio chapter12 ThreadTest1
call :buildtio chapter12 ThreadTest2
call :buildjar chapter12 RandomArtWithThreads
call :buildjar chapter12 BackgroundComputationDemo
call :buildjar chapter12 MultiprocessingDemo1
call :buildjar chapter12 MultiprocessingDemo2
call :buildjar chapter12 MultiprocessingDemo3
call :buildjar chapter12 TowersOfHanoiGUI
call :buildjar chapter12 GUIChat

call :cpfiles chapter12 netgame.chat.ChatRoomWindow
mkdir temp\netgame
xcopy /S /Q chapter12\netgame temp\netgame
cd temp
%JAVAC% netgame\common\*.java netgame\chat\*.java
%JAR% -cmf manifest netgame.chat.ChatRoomWindow.jar netgame\common\*.class netgame\chat\*.class
move netgame.chat.ChatRoomWindow.jar ..\compiled-jar-files\chapter12 > nul
cd ..

call :cpfiles chapter12 netgame.tictactoe.Main 
mkdir temp\netgame
xcopy /S /Q chapter12\netgame temp\netgame
cd temp
%JAVAC% netgame\common\*.java netgame\tictactoe\*.java
%JAR% -cmf manifest netgame.chat.tictactoe.Main.jar netgame\common\*.class netgame\tictactoe\*.class
move netgame.chat.tictactoe.Main.jar ..\compiled-jar-files\chapter12 > nul
cd ..

call :cpfiles chapter12 netgame.fivecarddraw.Main 
mkdir temp\netgame
xcopy /S /Q chapter12\netgame temp\netgame
cd temp
%JAVAC% netgame\common\*.java netgame\fivecarddraw\*.java
%JAR% -cmf manifest netgame.chat.fivecarddraw.Main.jar netgame\common\*.class netgame\fivecarddraw\*.class netgame\fivecarddraw\cards.png
move netgame.chat.fivecarddraw.Main.jar ..\compiled-jar-files\chapter12 > nul
cd ..


call :buildjar chapter13 PaintWithOffScreenCanvas
call :buildjar chapter13 TransparencyDemo
call :buildjar chapter13 StrokeDemo
call :buildjar chapter13 ChoiceDemo
call :buildjar chapter13 StatesAndCapitalsTableDemo
call :buildjar chapter13 ScatterPlotTableDemo
call :buildjar chapter13 SimpleWebBrowserWithThread
call :buildjar chapter13 SimpleWebBrowser
call :buildjar chapter13 SimpleRTFEdit
call :buildjar chapter13 CustomComponentTest StopWatchLabel.java MirrorText.java

call :cpfiles chapter13 SillyStamper SillyStamper.java 
mkdir temp\stamper_icons
xcopy /S /Q chapter13\stamper_icons temp\stamper_icons > nul
cd temp
%JAVAC% SillyStamper.java
%JAR% -cmf manifest SillyStamper.jar *.class stamper_icons\*
move SillyStamper.jar ..\compiled-jar-files\chapter13 > nul
cd ..

call :cpfiles chapter13 HighLowWithImages HighLowWithImages.java cards.png Card.java Hand.java Deck.java
cd temp
%JAVAC% HighLowWithImages.java
%JAR% -cmf manifest HighLowWithImages.jar *.class cards.png
move HighLowWithImages.jar ..\compiled-jar-files\chapter13 > nul
cd ..

call :cpfiles chapter13 PaintDemo PaintDemo.java QueenOfHearts.png TinySmiley.png
cd temp
%JAVAC% PaintDemo.java
%JAR% -cmf manifest PaintDemo.jar *.class *.png
move PaintDemo.jar ..\compiled-jar-files\chapter13 > nul
cd ..

call :cpfiles chapter13 SoundAndCursorDemo SoundAndCursorDemo.java 
mkdir temp\snc_resources
xcopy /S /Q chapter13\snc_resources temp\snc_resources > nul
cd temp
%JAVAC% SoundAndCursorDemo.java
%JAR% -cmf manifest SoundAndCursorDemo.jar *.class snc_resources\*
move SoundAndCursorDemo.jar ..\compiled-jar-files\chapter13 > nul
cd ..

call :cpfiles chapter13 edu.hws.eck.mdb.Main 
mkdir temp\edu
xcopy /S /Q chapter13\edu temp\edu > nul
cd temp
%JAVAC% edu\hws\eck\mdb\*.java
%JAR% -cmf manifest edu.hws.eck.mdb.Main.jar edu\hws\eck\mdb\* edu\hws\eck\mdb\examples\* 
move edu.hws.eck.mdb.Main.jar ..\compiled-jar-files\chapter13 > nul
cd ..


rmdir /q /s temp

echo.
echo.
echo Note: No jar files were created for the following sample programs:
echo.
echo Examples for which no jar files are made:
echo chapter2 CreateProfile -- can overwrite a file without warning
echo chapter4 CopyTextFile -- requires command-line arguments
echo chapter4 RandomMosaicWalk2 -- duplicates functionality of RandomMosaicWalk
echo chapter8 LengthConverter3 -- duplicates functionality of LenghtConverter2
echo chapter11 ReverseFile -- requires a file named data.dat in the current directory
echo chapter11 ReverseFileWithScanner -- requires a file named data.dat 
echo chapter11 ReverseFileWithResources -- requires a file named data.dat 
echo chapter11 CopyFile -- requires command-line parameters
echo chapter11 CopyFileAsResource -- requires command-line parameters
echo chapter12 BackgroundCompWithInvoke -- duplicates BackgroundComputationDemo
echo chapter12 CLDateServerWithThreads -- duplicates functionality of DateServer
echo chapter12 CLDateServerWithThreadPool -- duplicates functionality of DateServer
echo chapter12 CLMandelbrotMaster -- needs command line arguments
echo chapter12 CLMandelbrotWorker -- needs to run on several machines
echo.
echo Note: Before running chapter13\netgame.chat.ChatRoomWindow.jar, 
echo       you have to run ChatRoomServer on the command line.
echo.
echo JAR FILES CAN BE FOUND IN THE DIRECTORY compiled-jar-files
echo.


exit /b 0


REM %1 is chapter: c1, c2, ...
REM %2 is main class (without .java)
REM %3,%4,... are other files/directories to be included
:buildjar
   if exist temp rmdir /S /Q temp
   mkdir temp
   echo.
   echo Building %1\%2
   if not exist %1\%2.java (
       echo Cannot find file %1\%2.java !  Aborting build.
       goto :EOF
   )
   copy %1\%2.java temp > nul
   set CHPT=%1
   set NAME=%2
   shift
   shift
   :lp
      if --==-%1- goto :pl
      if not exist %CHPT%\%1 (
          echo Cannot find %CHPT%\%1 !  Aborting build.
          goto :EOF
      )
      xcopy %CHPT%\%1 temp > nul
      shift
      goto :lp
   :pl
   echo Main-Class: %NAME%> temp\manifest
   cd temp
   %JAVAC% *.java
   if not exist %NAME%.class (
      cd ..
      echo Error during compilation!  Build aborted.
      goto :EOF
   )
   %JAR% mfc manifest %NAME%.jar *.class
   cd ..
   if not exist compiled-jar-files\%CHPT% (
      mkdir compiled-jar-files\%CHPT%
   )
   move temp\%NAME%.jar compiled-jar-files\%CHPT%\%NAME%.jar > nul
   goto :EOF


REM For simple TextIO programs, using a GUI version of TextIO
REM %1 is chapter: c1, c2, ...
REM %2 is main class (without .java)
REM %3,%4,... are other files/directories to be included
:buildtio
   if exist temp rmdir /S /Q temp
   mkdir temp
   echo.
   echo Building %1\%2
   if not exist %1\%2.java (
       echo Cannot find file %1\%2.java !  Aborting build.
       goto :EOF
   )
   mkdir temp\textiogui
   xcopy textiogui\* temp\textiogui > nul
   echo import textiogui.TextIO; > temp\%2.java
   echo import textiogui.System; >> temp\%2.java
   type %1\%2.java >> temp\%2.java
   set CHPT=%1
   set NAME=%2
   shift
   shift
   :lop
      if --==-%1- goto :pol
      if not exist %CHPT%\%1 (
          echo Cannot find %CHPT%\%1 !  Aborting build.
          goto :EOF
      )
      xcopy %CHPT%\%1 temp > nul
      shift
      goto :lop
   :pol
   echo Main-Class: %NAME%> temp\manifest
   cd temp
   %JAVAC% *.java textiogui\*.java
   if not exist %NAME%.class (
      cd ..
      echo Error during compilation!  Build aborted.
      goto :EOF
   )
   %JAR% mfc manifest %NAME%.jar *.class textiogui\*.class
   cd ..
   if not exist compiled-jar-files\%CHPT% (
      mkdir compiled-jar-files\%CHPT%
   )
   move temp\%NAME%.jar compiled-jar-files\%CHPT%\%NAME%.jar > nul
   goto :EOF


REM used for jar files that have other stuff besides .class files from the main directory.
REM just copies files into temp and makes the manifest file; jar is run separately
REM %1 is chapter: c1, c2, ...
REM %2 is main class (with periods and without .java)
REM %3 etc are files and directories to be copied into temp
:cpfiles
   if exist temp rmdir /S /Q temp
   mkdir temp
   echo.
   echo Building %1\%2
   set CHPT=%1
   set NAME=%2
   shift
   shift
   :loop
      if --==-%1- goto :pool
      if not exist %CHPT%\%1 (
          echo Cannot find %CHPT%\%1 !  Aborting build.
          goto :EOF
      )
      copy %CHPT%\%1 temp > nul
      shift
      goto :loop
   :pool
   echo Main-Class: %NAME%> temp\manifest
   if not exist compiled-jar-files\%CHPT% (
      mkdir compiled-jar-files\%CHPT%
   )
   goto :EOF


