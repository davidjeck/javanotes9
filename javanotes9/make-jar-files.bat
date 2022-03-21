
echo off

REM IF YOU WANT THIS SCRIPT TO MAKE JAR FILES FOR JavaFX PROGRAMS, YOU NEED TO EDIT
REM IT TO SET THE VALUE OF JAVAC_FX.  SEE COMMENTS ABOUT JAVA_FX BELOW.

REM THIS DOS BATCH SCRIPT CREATES JAR FILES FOR EXAMPLE PROGRAMS. 
REM IT USES SOURCE CODE FILES FROM THE "source" DIRECTORY OF THE WEB SITE.
REM IT MUST BE RUN IN THAT DIRECTORY.  IT CREATES A TEMP FOLDER IN THE CURRENT 
REM DIRECTORY WHILE IT IS WORKING.

REM THE SCRIPT CREATES A DIRECTORY NAMED "compiled-jar-files" INSIDE THE "source" DIRECTORY
REM TO HOLD THE JAR FILES.  INISDE THAT DIRECTORY, JAR FILES WILL BE ORGANIZED 
REM BY CHAPTER.

REM THE SCRIPT NEEDS THE COMMANDS javac AND jar TO BE DEFINED, OR ALTERNATIVE COMMANDS
REM CAN BE SET IN THE NEXT TWO LINES, FOR EXAMPLE GIVING FULL PATHS TO THE COMMANDS

set JAVAC=javac
set JAR=jar

REM TO COMPILE PROGRAMS THAT USE JavaFX, JAVAC_FX MUST BE SET TO A javac COMMAND THAT 
REM WILL WORK FOR COMPILING JavaFX PROGRAMS.  IF JAVAC_FX IS LEFT EQUAL TO none, NO JavaFX
REM JAR FILES WILL BE PRODUCED. 

set JAVAC_FX=none

REM FOR EXAMPLE, YOU CAN UNCOMMENT THE SECOND LINE BELOW AND EDIT IT TO
REM USE THE PATH TO THE JavaFX SDK lib DIRECTORY, IN PLACE OF C:\Uses\eck\javafx-sdk-11\lib
REM (To uncomment a line, remove the REM at the beginning.)

REM set JAVAC_FX=%JAVAC%
REM set JAVAC_FX=%JAVAC% --module-path=C:\Users\eck\javafx-sdk-11\lib --add-modules=ALL-MODULE-PATH

echo.

if exist temp (
    echo A file or directory named temp already exists.
    echo It would be deleted when this script is run.
    echo Please delete temp by hand before running this script.
    echo.
    goto :EOF
)

if exist compiled-jar-files (
   echo Note: compiled-jar-files folder already exists; jar files will be stored in that folder.
) else (
   echo Creating folder named compiled-jar-files to hold the jar files.
   mkdir compiled-jar-files
)

call :buildFXjar chapter1 GUIDemo
call :buildtio chapter2 HelloWorld
call :buildtio chapter2 PrintSquare
call :buildtio chapter2 TimedComputation
call :buildtio chapter2 Interest
call :buildtio chapter2 EnumDemo
call :buildtio chapter2 TextBlockDemo
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
call :buildFXjar chapter3 SimpleGraphicsStarter
call :buildFXjar chapter3 MovingRects
call :buildFXjar chapter3 RandomCircles
call :buildtio chapter4 GuessingGame
call :buildtio chapter4 GuessingGame2
call :buildtio chapter4 RowsOfChars
call :buildtio chapter4 ThreeN2
call :buildFXjar chapter4 RandomMosaicWalk Mosaic.java MosaicCanvas.java
call :buildtio chapter5 RollTwoPairs PairOfDice.java
call :buildFXjar chapter5 GrowingCircleAnimation CircleInfo.java
call :buildtio chapter5 HighLow Deck.java Card.java
call :buildFXjar chapter5 ShapeDraw
call :buildFXjar chapter6 HelloWorldFX
call :buildFXjar chapter6 SimpleColorChooser
call :buildFXjar chapter6 RandomStrings

if not "%JAVAC_FX%" == "none" (
	call :cpfiles chapter6 RandomCards RandomCards.java Card.java Deck.java cards.png
	cd temp
	%JAVAC_FX% RandomCards.java
	%JAR% -cmf manifest RandomCards.jar *.class cards.png
	move RandomCards.jar ..\compiled-jar-files\chapter6 > nul
	cd ..
)

call :buildFXjar chapter6 SimpleTrackMouse
call :buildFXjar chapter6 SimplePaint
call :buildFXjar chapter6 KeyboardEventDemo
call :buildFXjar chapter6 SubKiller
call :buildFXjar chapter6 TextInputDemo
call :buildFXjar chapter6 SliderDemo
call :buildFXjar chapter6 OwnLayoutDemo
call :buildFXjar chapter6 SimpleCalc

if not "%JAVAC_FX%" == "none" (
	call :cpfiles chapter6 HighLowGUI HighLowGUI.java cards.png Card.java Hand.java Deck.java
	cd temp
	%JAVAC_FX% HighLowGUI.java
	%JAR% -cmf manifest HighLowGUI.jar *.class cards.png
	move HighLowGUI.jar ..\compiled-jar-files\chapter6 > nul
	cd ..
)

call :buildFXjar chapter6 MosaicDraw MosaicCanvas.java
call :buildFXjar chapter7 RandomStringsWithArray
call :buildtio chapter7 ReverseWithDynamicArray DynamicArrayOfInt.java
call :buildFXjar chapter7 SimplePaint2
call :buildtio chapter7 TestSymmetricMatrix SymmetricMatrix.java
call :buildtio chapter7 RecordDemo Complex.java FullName.java
call :buildFXjar chapter7 Life MosaicCanvas.java
call :buildFXjar chapter7 Checkers
call :buildtio chapter8 LengthConverter2
call :buildtio chapter8 TryStatementDemo
call :buildtio chapter9 TowersOfHanoi
call :buildFXjar chapter9 Maze
call :buildFXjar chapter9 LittlePentominos MosaicCanvas.java
call :buildFXjar chapter9 Blobs
call :buildtio chapter9 ListDemo StringList.java
call :buildtio chapter9 PostfixEval StackOfDouble.java
call :buildFXjar chapter9 DepthBreadth
call :buildtio chapter9 SortTreeDemo
call :buildtio chapter9 SimpleParser1
call :buildtio chapter9 SimpleParser2
call :buildtio chapter9 SimpleParser3
call :buildtio chapter10 WordListWithTreeSet
call :buildtio chapter10 WordListWithPriorityQueue
call :buildtio chapter10 SimpleInterpreter
call :buildtio chapter10 WordCount
call :buildtio chapter10 RiemannSumStreamExperiment
call :buildtio chapter11 DirectoryList
call :buildtio chapter11 PhoneDirectoryFileDemo
call :buildFXjar chapter11 TrivialEdit
call :buildFXjar chapter11 SimplePaintWithFiles
call :buildtio chapter11 FetchURL
call :buildtio chapter11 ShowMyNetwork
call :buildtio chapter11 DateServer
call :buildtio chapter11 DateClient
call :buildtio chapter11 CLChatServer
call :buildtio chapter11 CLChatClient
call :buildFXjar chapter11 SimplePaintWithXML
call :buildFXjar chapter11 XMLDemo
call :buildtio chapter12 ThreadTest1
call :buildtio chapter12 ThreadTest2
call :buildFXjar chapter12 RandomArtWithThreads
call :buildFXjar chapter12 BackgroundComputationDemo
call :buildFXjar chapter12 MultiprocessingDemo1
call :buildFXjar chapter12 MultiprocessingDemo2
call :buildFXjar chapter12 MultiprocessingDemo3
call :buildFXjar chapter12 QuicksortThreadDemo
call :buildtio chapter12 ThreadTest4
call :buildFXjar chapter12 TowersOfHanoiGUI
call :buildFXjar chapter12 GUIChat

if not "%JAVAC_FX%" == "none" (
	call :cpfiles chapter12 netgame.chat.ChatRoomWindow
	mkdir temp\netgame
	xcopy /S /Q chapter12\netgame temp\netgame
	cd temp
	%JAVAC_FX% netgame\common\*.java netgame\chat\*.java
	%JAR% -cmf manifest netgame.chat.ChatRoomWindow.jar netgame\common\*.class netgame\chat\*.class
	move netgame.chat.ChatRoomWindow.jar ..\compiled-jar-files\chapter12 > nul
	cd ..
	
	call :cpfiles chapter12 netgame.tictactoe.Main 
	mkdir temp\netgame
	xcopy /S /Q chapter12\netgame temp\netgame
	cd temp
	%JAVAC_FX% netgame\common\*.java netgame\tictactoe\*.java
	%JAR% -cmf manifest netgame.chat.tictactoe.Main.jar netgame\common\*.class netgame\tictactoe\*.class
	move netgame.chat.tictactoe.Main.jar ..\compiled-jar-files\chapter12 > nul
	cd ..
	
	call :cpfiles chapter12 netgame.fivecarddraw.Main 
	mkdir temp\netgame
	xcopy /S /Q chapter12\netgame temp\netgame
	cd temp
	%JAVAC_FX% netgame\common\*.java netgame\fivecarddraw\*.java
	%JAR% -cmf manifest netgame.chat.fivecarddraw.Main.jar netgame\common\*.class netgame\fivecarddraw\*.class netgame\fivecarddraw\cards.png
	move netgame.chat.fivecarddraw.Main.jar ..\compiled-jar-files\chapter12 > nul
	cd ..
)

call :buildFXjar chapter13 BoundPropertyDemo
call :buildFXjar chapter13 CanvasResizeDemo
call :buildFXjar chapter13 StrokeDemo
call :buildFXjar chapter13 TransformDemo face-smile.png
call :buildFXjar chapter13 ToolPaint SimpleDialogs.java
call :buildFXjar chapter13 TestStopWatch StopWatchLabel.java
call :buildFXjar chapter13 EditListDemo
call :buildFXjar chapter13 SimpleTableDemo
call :buildFXjar chapter13 ScatterPlotTableDemo
call :buildFXjar chapter13 TestDialogs SimpleDialogs.java
call :buildFXjar chapter13 WebBrowser BrowserWindow.java SimpleDialogs.java

if not "%JAVAC_FX%" == "none" (
	call :cpfiles chapter13 TransformDemo TransformDemo.java face-smile.png
	cd temp
	%JAVAC_FX% TransformDemo.java
	%JAR% -cmf manifest TransformDemo.jar *.class *.png
	move TransformDemo.jar ..\compiled-jar-files\chapter13 > nul
	cd ..
	
	call :cpfiles chapter13 SillyStamper SillyStamper.java 
	mkdir temp\stamper_icons
	xcopy /S /Q chapter13\stamper_icons temp\stamper_icons > nul
	cd temp
	%JAVAC_FX% SillyStamper.java
	%JAR% -cmf manifest SillyStamper.jar *.class stamper_icons\*
	move SillyStamper.jar ..\compiled-jar-files\chapter13 > nul
	cd ..
	
	call :cpfiles chapter13 PaintDemo PaintDemo.java tile.png face-smile.png
	cd temp
	%JAVAC_FX% PaintDemo.java
	%JAR% -cmf manifest PaintDemo.jar *.class *.png
	move PaintDemo.jar ..\compiled-jar-files\chapter13 > nul
	cd ..
	
	call :cpfiles chapter13 edu.hws.eck.mdbfx.Main 
	mkdir temp\edu
	xcopy /S /Q chapter13\edu temp\edu > nul
	cd temp
	%JAVAC_FX% edu\hws\eck\mdbfx\*.java
	%JAR% -cmf manifest edu.hws.eck.mdbfx.Main.jar edu\hws\eck\mdbfx\* edu\hws\eck\mdbfx\examples\* 
	move edu.hws.eck.mdbfx.Main.jar ..\compiled-jar-files\chapter13 > nul
	cd ..
)

rmdir /q /s temp

echo.
echo "Note: No jar files were created for the following sample programs:"
echo.
echo Examples for which no jar files are made:
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
echo.
if "%JAVAC_FX%" == "none" (
   echo In addition, no jar files were created for programs that use JavaFX
) else (
   echo chapter4  RandomMosaicWalk2 -- duplicates functionality of RandomMosaicWalk
   echo chapter12 MultiprocessorDemo4 -- duplicates the functionality of MultiprocessorDemo3 
   if not %JAVAC% == %JAVAC_FX% (
      echo.
      echo Remember that jar files that use JavaFX must be run from the command line,
      echo using a java command that includes JavaFX options.
   )
   echo.
   echo Note: Before running chapter13\netgame.chat.ChatRoomWindow.jar, 
   echo       you have to run ChatRoomServer on the command line.
)
echo.
echo JAR FILES CAN BE FOUND IN THE DIRECTORY compiled-jar-files
echo.


exit /b 0


REM %1 is chapter: c1, c2, ...
REM %2 is main class (without .java)
REM %3,%4,... are other files/directories to be included
:buildFXjar
   if "%JAVAC_FX%" == "none" (
      goto :EOF
   )
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
   %JAVAC_FX% *.java
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
   mkdir temp\textio
   xcopy textio-for-windows-jar-files\* temp\textio > nul
   echo import textio.System; >> temp\%2.java
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
   %JAVAC% *.java textio\*.java
   if not exist %NAME%.class (
      cd ..
      echo Error during compilation!  Build aborted.
      goto :EOF
   )
   %JAR% mfc manifest %NAME%.jar *.class textio\*.class
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


