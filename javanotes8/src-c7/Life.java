import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;

/**
 * This program is a very simple implementation of John H. Conway's famous "Game of Life".
 * In this game, the user sets up a board that contains a grid of cells, where each cell can be 
 * either "living" or "dead".  Once the board is set up and the game is started, it runs itself.
 * The board goes through a sequence of "generations."  In each generation, every cell can
 * change its state from living to dead or vice versa, depending on the number of neighbors
 * that it has.  The rules are:
 * 
 *     1.  If a cell is dead, and if it has exactly 3 living neighbors, then the cell comes
 *         to life; if the number of neighbors is less than or greater than 3, then the dead 
 *         cell remains dead.  (That is, three living neighbors give birth to a new cell.)
 *         
 *     2.  If a cell is alive, and if it has exactly 2 or 3 living neighbors, then the cell
 *         remains alive; otherwise, it dies.  (If a cell has 0 or 1 neighbors, it dies of
 *         loneliness; if it has 4 or more neighbors, it dies of overcrowding.)
 *         
 * It is important that all these changes happen simultaneously in each generation.  When
 * counting neighbors, the 8 cells that are next to a given cell horizontally, vertically,
 * and diagonally are considered.  Ideally, the board would be infinite.  On a finite board,
 * special consideration must be given to cells that lie along the boundary.  In this program, 
 * the approach is to consider the left edge to be next to the right edge and the top edge
 * to be next to the bottom edge.  This effectively turns the board into a "torus" (the shape
 * of the surface of a doughnut), which is finite but has no boundary.
 * 
 * The program's window shows a Life board with some control buttons beneath the board.
 * The user can create a board configuration by clicking and dragging on the board to create
 * living cells.  Clicking and dragging while holding down the right mouse button will change
 * living cells back to dead.  There is also a button that will set the state of each cell to
 * be a random value.  When the program first starts, the board contains a simple configuration
 * of five living cells (the "R pentomino") that will give a long animation before settling
 * down to static patterns and simple repeaters. 
 * 
 * The board in this program is represented by an object of type MosaicCanvas, which is
 * a custom subclass of Canvas.  The program requires MosaicCanvas.java.
 */
public class Life extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//---------------------------------------------------------------------------------------

	private final int GRID_SIZE = 100;  // Number of squares along each side of the board
	                                    // (Should probably not be less than 10 or more than 200,)

	private boolean[][] alive;   // Represents the board.  alive[r][c] is true if the cell in row r, column c is alive.

	private MosaicCanvas lifeBoard;  // Displays the game to the user.  White squares are alive; black squares are dead.

	private AnimationTimer timer;  // Drives the game when the user presses the "Start" button.

	private Button  stopGoButton;  // Button for starting and stopping the running of the game.
	private Button  nextButton;    // Button for computing just the next generation.
	private Button  randomButton;  // Button for filling the board randomly with each cell having a 25% chance of  being alive.
	private Button  clearButton;   // Button for clearing the board, that is setting all the cells to "dead".
	private Button  quitButton;    // Button for ending the program.
	
	private CheckBox fastCheckbox; // When checked, the animation runs at full speed, with a new frame
	                               // generated in each call to the AnimationTimer's handle() method.
	                               // (This should be 60 frames per second.)  When not checked, there will
	                               // be at least 1/10 second between frames, giving about 6 frames per second.

	private boolean animationIsRunning;   // set to true when the timer is started, false when it is paused
	
	
	/**
	 * Create a life game board, initially empty, and add it and some buttons to
	 * the GUI.  Set up event handling for the buttons.
	 * The number of cells on each side of the grid is GRID_SIZE.
	 */
	public void start(Stage stage) {
		
		/* Create and configure the board, including setting up mouse event listeners */
		
		int cellSize = 800/GRID_SIZE; // Aim for about a 800-by-800 pixel board.
		lifeBoard = new MosaicCanvas(GRID_SIZE,GRID_SIZE,cellSize,cellSize);
		if (cellSize < 5)
			lifeBoard.setGroutingColor(null);  // Don't show grouting if cells are too small.
		lifeBoard.setUse3D(false);
		lifeBoard.setOnMousePressed( e -> mousePressed(e) );
		lifeBoard.setOnMouseDragged( e -> mouseDragged(e) );
		lifeBoard.setStyle("-fx-border-color:darkgray; -fx-border-width:3px");
		
		/* Create the buttons and checkbox.  Add action event listeners to the buttons. */
		
		clearButton = new Button("Clear");
		stopGoButton = new Button("Start");
		quitButton = new Button("Quit");
		nextButton = new Button("One Step");
		randomButton = new Button("Random Fill");
		stopGoButton.setOnAction( e -> doStopGo() );
		quitButton.setOnAction( e -> System.exit(0) );
		randomButton.setOnAction( e -> doRandom() );
		nextButton.setOnAction( e -> {
			doFrame();
			showBoard();
		});
		clearButton.setOnAction( e -> {
			alive = new boolean[GRID_SIZE][GRID_SIZE];
			showBoard();
		});
		fastCheckbox = new CheckBox("Fast");
		
		/* Create, but do not start, the animation timers.  The user has to press "Start" tp start it. */
		
		timer = new AnimationTimer() {
			final double oneTenthSecond = 1e8; // 1e8 nanoseconds = 1/10 second
			long previousTime;  // Time when a new frame was last generated.
			public void handle(long time) {
				if ( (time-previousTime) > 0.975*oneTenthSecond  || fastCheckbox.isSelected()) {
					doFrame();
					showBoard();
					previousTime = time;
				}
			}
		};
		
		/* Build the scene graph. */

		HBox bottom = new HBox(20, 
				stopGoButton, fastCheckbox, nextButton, randomButton, clearButton, quitButton );
		bottom.setStyle("-fx-padding:8px; -fx-border-color:darkgray; -fx-border-width: 3px 0 0 0");
		bottom.setAlignment(Pos.CENTER);
		
		BorderPane root = new BorderPane();
		root.setCenter(lifeBoard);
		root.setBottom(bottom);
		
		/* Create the array that holds the state for every cell on the board.  Set some cells
		 * to true for the "R pentomino" initial configuration, and draw the initial board. */ 
		
		alive = new boolean[GRID_SIZE][GRID_SIZE];
		alive[49][49] = true;
		alive[50][49] = true;
		alive[51][49] = true;
		alive[49][50] = true;
		alive[50][48] = true;
		showBoard();
		
		/* Set up the scene and stage, and show the window. */
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Conway's Game of Life");
		stage.show();
		
	} // end start();


	/**
	 * Compute the next generation of cells.  The "alive" array is modified to reflect the
	 * state of each cell in the new generation.  (Note that this method does not actually
	 * draw the new board; it only sets the values in the "alive" array.  The board is
	 * redrawn in the showBoard() method.)
	 */
	private void doFrame() { // Compute the new state of the Life board.
		boolean[][] newboard = new boolean[GRID_SIZE][GRID_SIZE];
		for ( int r = 0; r < GRID_SIZE; r++ ) {
			int above, below; // rows considered above and below row number r
			int left, right;  // columns considered left and right of column c
			above = r > 0 ? r-1 : GRID_SIZE-1;
			below = r < GRID_SIZE-1 ? r+1 : 0;
			for ( int c = 0; c < GRID_SIZE; c++ ) {
				left =  c > 0 ? c-1 : GRID_SIZE-1;
				right = c < GRID_SIZE-1 ? c+1 : 0;
				int n = 0; // number of alive cells in the 8 neighboring cells
				if (alive[above][left])
					n++;
				if (alive[above][c])
					n++;
				if (alive[above][right])
					n++;
				if (alive[r][left])
					n++;
				if (alive[r][right])
					n++;
				if (alive[below][left])
					n++;
				if (alive[below][c])
					n++;
				if (alive[below][right])
					n++;
				if (n == 3 || (alive[r][c] && n == 2))
					newboard[r][c] = true;
				else
					newboard[r][c] = false;
			}
		}
		alive = newboard;
	}


	/**
	 *  Sets the color of every square in the display to show whether the corresponding
	 *  cell on the Life board is alive or dead. 
	 */
	private void showBoard() {
		lifeBoard.setAutopaint(false);  // For efficiency, prevent redrawing of individual squares.
		                                // Failure to turn off autopaint would SEVERLY slow
		                                // down the program!
		for (int r = 0; r < GRID_SIZE; r++) {
			for (int c = 0; c < GRID_SIZE; c++) {
				if (alive[r][c])
					lifeBoard.setColor(r,c,Color.WHITE);  // alive sells are white
				else
					lifeBoard.setColor(r,c,null);  // Shows the background color, black.
			}
		}
		lifeBoard.setAutopaint(true);  // Redraws the whole board, and turns on drawing of individual squares.
	}


	/**
	 * This method is called for the button that is used to start and stop the array.
	 * If the animation is running, it is paused.  If it is not running, it is started.
	 * The text on the Start/Stop button is changed and some buttons are disabled and
	 * enabled, depending on whether the animation is running or not.
	 */
	private void doStopGo() {

		if (animationIsRunning) {  // If the game is currently running, stop it.
			timer.stop();  // This stops the game by turning off the timer that drives the game.
			clearButton.setDisable(false);  // Some buttons are disabled while the game is running.
			randomButton.setDisable(false);
			nextButton.setDisable(false);
			stopGoButton.setText("Start");  // Change text of button to "Start", since it can be used to start the game again.
			animationIsRunning = false;
		}
		else {  // If the game is not currently running, start it.
			timer.start();  // This starts the game by turning the timer that will drive the game.
			clearButton.setDisable(true);  // Buttons that modify the board are disabled while the game is running.
			randomButton.setDisable(true);
			nextButton.setDisable(true);
			stopGoButton.setText("Stop"); // Change text of button to "Stop", since it can be used to stop the game.
			animationIsRunning = true;
		}
	}


	/**
	 * This method is called when the user clicks the "Random" button.  It fills the
	 * alive array with random values and redraws the board.
	 */
	private void doRandom() {
		for (int r = 0; r < GRID_SIZE; r++) {
			for (int c = 0; c < GRID_SIZE; c++)
				alive[r][c] = (Math.random() < 0.25);  // 25% probability that the cell is alive.
		}
		showBoard();
	}



	/**
	 * This method is called when the user presses a mouse button on the canvas.
	 * The square containing the mouse comes to life or, if the right-mouse button is down, dies.
	 */
	private void mousePressed(MouseEvent e) {
		if (animationIsRunning)
			return;
		int row = lifeBoard.yCoordToRowNumber(e.getY());
		int col = lifeBoard.yCoordToRowNumber(e.getX());
		if (row >= 0 && row < lifeBoard.getRowCount() && col >= 0 && col < lifeBoard.getColumnCount()) {
			if (e.getButton() == MouseButton.SECONDARY) {
				lifeBoard.setColor(row,col,null);
				alive[row][col] = false;
			}
			else {
				lifeBoard.setColor(row,col,Color.WHITE);
				alive[row][col] = true;
			}
		}
	}


	/**
	 * The square containing the mouse comes to life or, if the right-mouse button is down, dies.
	 * Dragging the mouse into a square has the same effect as clicking in that square.
	 */
	private void mouseDragged(MouseEvent e) {
		mousePressed(e);  // 
	}

}
