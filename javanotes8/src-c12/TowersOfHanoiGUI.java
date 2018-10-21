
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *  This program shows an animation of the famous Towers of Hanoi problem, for a pile
 *  of ten disks.  Three control buttons allow the user to control the animation.
 *  A "Next" button allows the user to see just one move in the solution.  Clicking
 *  the "Run" button will let the animation run on its own; while it is running,
 *  "Run" changes to "Pause", and clicking the button will pause the animation.
 *  A "Start Again" button allows the user to restart the problem from the beginning.
 *  
 *  The program is an example of using the wait() and notify() methods.  The
 *  wait() method is used to pause the animation between moves.  When the user
 *  clicks "Next" or "Run", the notify() method is called to notify the thread to
 *  wake up and continue.  A "status" variable is used to communicate commands to
 *  the thread.
 */
public class TowersOfHanoiGUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//-----------------------------------------------------------

	private static Color BACKGROUND_COLOR = Color.rgb(255,255,180); // 4 colors used in drawing.
	private static Color BORDER_COLOR = Color.rgb(100,0,0);
	private static Color DISK_COLOR = Color.rgb(0,0,180);
	private static Color MOVE_DISK_COLOR = Color.rgb(180,180,255);

	private Canvas canvas;     // The canvas where the "towers" are drawn.
	private GraphicsContext g; // The graphics context for drawing on the canvas.

	private int status;   // Controls the execution of the thread; value is one of the following constants.

	private static final int GO = 0;       // a value for status, meaning thread is to run continuously	
	private static final int PAUSE = 1;    // a value for status, meaning thread should not run
	private static final int STEP = 2;     // a value for status, meaning thread should run one step then pause
	private static final int RESTART = 3;  // a value for status, meaning thread should start again from the beginning

	/* 
	 The following variables are the data needed for the animation.  The
      three "piles" of disks are represented by the variables tower and
      towerHeight.  towerHeight[i] is the number of disks on pile number i.
      For i=0,1,2 and for j=0,1,...,towerHeight[i]-1, tower[i][j] is an integer
      representing one of the ten disks.  (The disks are numbered from 1 to 10.)

      During the solution, as one disk is moved from one pile to another,
      the variable moveDisk is the number of the disk that is being moved,
      and moveTower is the number of the pile that it is currently on.
      This disk is not stored in the tower variable.  It is drawn in a
      different color from the other disks.
	 */

	private int[][] tower;
	private int[] towerHeight;
	private int moveDisk;
	private int moveTower;


	private Button runPauseButton;  // 3 control buttons for controlling the animation
	private Button nextStepButton;
	private Button startOverButton;


	/**
	 * Set up the GUI and event handling.
	 */
	public void start (Stage stage) {
		canvas = new Canvas(430,143);
		g = canvas.getGraphicsContext2D();

		runPauseButton = new Button("Run");
		runPauseButton.setOnAction( e -> doStopGo() );
		runPauseButton.setMaxWidth(10000);
		runPauseButton.setPrefWidth(10);
		nextStepButton = new Button("Next Step");
		nextStepButton.setOnAction( e -> doNextStep() );
		nextStepButton.setMaxWidth(10000);
		nextStepButton.setPrefWidth(10);
		startOverButton = new Button("Start Over");
		startOverButton.setOnAction( e -> doRestart() );
		startOverButton.setMaxWidth(10000);
		startOverButton.setPrefWidth(10);
		startOverButton.setDisable(true);
		HBox bottom = new HBox( runPauseButton, nextStepButton, startOverButton);
		bottom.setStyle("-fx-border-color: rgb(100,0,0); -fx-border-width: 4px 0 0 0");
		HBox.setHgrow(runPauseButton, Priority.ALWAYS);
		HBox.setHgrow(nextStepButton, Priority.ALWAYS);
		HBox.setHgrow(startOverButton, Priority.ALWAYS);
		
		BorderPane root = new BorderPane(canvas);
		root.setBottom(bottom);
		root.setStyle("-fx-border-color: rgb(100,0,0); -fx-border-width: 4px");
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();

		new AnimationThread().start();  // Create and start the thread that will solve
		                                // the puzzles.  The thread will immediately
                                        // block until user clicks "Run" or "Next Step".
	} // end start()


	/**
	 *  Event-handling methods for the control buttons.  Changes in the
	 *  value of the status variable will be seen by the animation thread,
	 *  which will respond appropriately.
	 */
	synchronized private void doStopGo() {
		if (status == GO) {  // Animation is running.  Pause it.
			status = PAUSE;
			nextStepButton.setDisable(false);
			runPauseButton.setText("Run");
		}
		else {  // Animation is paused.  Start it running.
			status = GO;
			nextStepButton.setDisable(true);  // disabled when animation is running
			runPauseButton.setText("Pause");
		}
		notify();  // Wake up the thread so it can see the new status value!
	}
	
	synchronized private void doNextStep() {
		status = STEP;
		notify();
	}

	synchronized private void doRestart() {
		status = RESTART;
		notify();
	}


	/**
	 *  The run() method for the animation thread.  Runs in an infinite loop.
	 *  In the loop, the thread first sets up the initial state of the "towers"
	 *  and of the buttons.  This includes setting the status to PAUSED, and
	 *  calling checkStatus(), which will not return until the user clicks the
	 *  "Run" button or the "Next" Button.  Once this happens, it calls
	 *  the solve() method to run the recursive algorithm that solves the
	 *  Towers Of Hanoi problem.  During the solution, checkStatus() is
	 *  called after each move.  If the user clicks the "Start Again" button,
	 *  checkStatus() will throw an IllegalStateException, which will cause
	 *  the solve() method to be aborted.  The exception is caught to prevent
	 *  it from crashing the thread.  
	 */
	private class AnimationThread extends Thread {
		AnimationThread() {
			    // The constructor sets this thread to be a Daemon thread.
			    // Otherwise, the thread will keep the Java Virtual Machine
			    // from exiting when the window is closed.
			setDaemon(true);
		}
		public void run() {
			while (true) {
				Platform.runLater( () -> {
					runPauseButton.setText("Run");
					runPauseButton.setDisable(false);
					nextStepButton.setDisable(false);
					startOverButton.setDisable(true);
				});
				setUpProblem();  // Sets up the initial state of the puzzle.
				status = PAUSE;
				checkStatus(); // Returns only when user has clicked "Run" or "Next Step"
				Platform.runLater( () -> startOverButton.setDisable(false) );
				try {
					solve(10,0,1,2);  // Move 10 disks from pile 0 to pile 1.
						// When solution is done, give the user a chance to see it,
						// and make them click "Restart" to continue with a new solution.
					status = PAUSE;
					Platform.runLater( () -> { // Make sure user can only click startOver.
						runPauseButton.setDisable(true);
						nextStepButton.setDisable(true);
						startOverButton.setDisable(false);
					} );
					checkStatus();  // Returns only when use clicks "Start Over".
				}
				catch (IllegalStateException e) {
					// Exception was thrown because user clicked "Start Over".
				}
			}
		}
	}


	/**
	 *  This method is called before starting the solution and after each
	 *  move of the solution.  If the status is PAUSE, it waits until
	 *  the status changes.  If the status is RESTART, it throws
	 *  an IllegalStateException that will abort the solution.
	 *  When this method returns, the value of status must be 
	 *  RUN or STEP.
	 */
	synchronized private void checkStatus() {
		while (status == PAUSE) {
			try {
				wait();
			}
			catch (InterruptedException e) {
			}
		}
		// At this point, status is RUN, STEP, or RESTART.
		if (status == RESTART)
			throw new IllegalStateException("Restart");
		// At this point, status is RUN or STEP, and solution should proceed.
	}


	/**
	 * Sets up the initial state of the Towers Of Hanoi puzzle, with
	 * all the disks on the first pile.  
	 */
	synchronized private void setUpProblem() {
		moveDisk= 0;
		tower = new int[3][10];
		for (int i = 0; i < 10; i++)
			tower[0][i] = 10 - i;
		towerHeight = new int[3];
		towerHeight[0] = 10;
		Platform.runLater( () -> drawInitialFrame() );
	}


	/**
	 * Solves the TowersOfHanoi problem to move the specified
	 * number of disks from one pile to another.
	 * @param disks the number of disks to be moved
	 * @param from the number of the pile where the disks are now
	 * @param to the number of the pile to which the disks are to be moved
	 * @param spare the number of the pile that can be used as a spare
	 */
	private void solve(int disks, int from, int to, int spare) {
		if (disks == 1)
			moveOne(from,to);
		else {
			solve(disks-1, from, spare, to);
			moveOne(from,to);
			solve(disks-1, spare, to, from);
		}
	}


	/**
	 * Move the disk at the top of pile number fromStack to
	 * the top of pile number toStack.  (The disk changes to
	 * a new color, then moves, then changes back to the standard
	 * color.)  The delay() method is called to insert some short
	 * delays into the animation.  After the move, if the value of
	 * status was STEP, indicating that only one step was to be
	 * executed before pausing, then the value of STATUS is changed
	 * to PAUSE.  In any case, at the end of the method, the
	 * checkStatus() method is called.
	 */
	synchronized private void moveOne(int fromStack, int toStack) {
		moveDisk = tower[fromStack][towerHeight[fromStack]-1];
		moveTower = fromStack;
		delay(120);
		towerHeight[fromStack]--;
		putDisk(MOVE_DISK_COLOR,moveDisk,moveTower,towerHeight[fromStack]);
		delay(80);
		putDisk(BACKGROUND_COLOR,moveDisk,moveTower,towerHeight[fromStack]);
		delay(80);
		moveTower = toStack;
		putDisk(MOVE_DISK_COLOR,moveDisk,moveTower,towerHeight[toStack]);
		delay(80);
		putDisk(DISK_COLOR,moveDisk,moveTower,towerHeight[toStack]);
		tower[toStack][towerHeight[toStack]] = moveDisk;
		towerHeight[toStack]++;
		moveDisk = 0;
		if (status == STEP)
			status = PAUSE;
		checkStatus();
	}


	/**
	 * Simple utility method for inserting a delay of a specified
	 * number of milliseconds.
	 */
	synchronized private void delay(int milliseconds) {
		try {
			wait(milliseconds);
		}
		catch (InterruptedException e) {
		}
	}


	/**
	 * Draw a specified disk to the off-screen canvas.  This is
	 * used only during the moveOne() method, to draw the disk
	 * that is being moved.  This method is called from the animation
	 * thread. It uses Platform.runLater() to apply the drawing to
	 * the canvas.
	 * @param color the color of the disk (use background color to erase)
	 * @param disk the number of the disk that is to be drawn, 1 to 10
	 * @param t the number of the pile on top of which the disk is drawn
	 * @param h the height of the tower
	 */
	private void putDisk(Color color, int disk, int t, int h) {
		Platform.runLater( () -> {
			g.setFill(color);
			if (color == BACKGROUND_COLOR) {
				   // When drawing in the background color, to erase a disk, a slightly
				   // larger roundrect is drawn.  This is done to make sure that the
				   // disk is completely erased, since the anti-aliasing that was done
				   // when the disk was drawn can allow the disk color to bleed into pixels
				   // that lie outside the actual disk.
				g.fillRoundRect(75+140*t - 5*disk - 6, 116-12*h - 1, 10*disk+12, 12, 10, 10);
			}
			else {
				g.fillRoundRect(75+140*t - 5*disk - 5, 116-12*h, 10*disk+10, 10, 10, 10);
			}
		});
	}


	/**
	 * Called to draw the starting state of the towers, with all the
	 * disks on the first base.
	 * This method is called on the JavaFX application thread.
	 */
	private void drawInitialFrame() {
		g.setFill(BACKGROUND_COLOR);
		g.fillRect(0,0,430,143);
		g.setFill(BORDER_COLOR);
		g.fillRect(10,128,130,5);
		g.fillRect(150,128,130,5);
		g.fillRect(290,128,130,5);
		g.setFill(DISK_COLOR);
		for (int t = 0; t < 3; t++) {
			for (int i = 0; i < towerHeight[t]; i++) {
				int disk = tower[t][i];
				g.fillRoundRect(75+140*t - 5*disk - 5, 116-12*i, 10*disk+10, 10, 10, 10);
			}
		}
	}



} // end class TowersOfHanoiGUI
