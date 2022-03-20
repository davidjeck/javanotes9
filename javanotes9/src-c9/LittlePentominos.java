import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * This program solves pentominos puzzles.  A pentomino consists of 5 connected squares.  There are exactly
 * 12 ways to make a pentomino (counting rotations and reflections of a piece as the same piece).  A
 * pentominos puzzle consists of trying to place the pieces on a board, without overlapping any of
 * the pieces.  This version uses an 8-by-8 board.  In this case, the pentominos will fill
 * 60 of the 64 available squares on the board.  The squares that are to be left empty are selected
 * in advance at random and are colored black.  The main point of the program is to demo a
 * recursive backtracking algorithm, so the process is slowed down enough to see what is going
 * on.  See https://math.hws.edu/eck/js/pentominos/pentominos.html for a greatly enhanced version of this 
 * program, written in JavaScript, including the ability to quickly find solutions.
 * 
 * Note that this class depends on another class, MosaicCanvas.
 */
public class LittlePentominos extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	//---------------------------------------------------------------------------------------------

	private MosaicCanvas board;  // for displaying the board on the screen

	private boolean[] used = new boolean[13];  //  used[i] tells whether piece # i is already on the board

	private int numused;     // number of pieces currently on the board, from 0 to 12

	private GameThread gameThread = null;   // a thread to run the puzzle solving procedure

	volatile private int delay = 100;           

	private static final int rows = 8, cols = 8;  // number of rows and columns in the board

	private final static int GO_MESSAGE = 1;      // the values for the message variable   
	private final static int RESTART_RANDOM_MESSAGE = 6;



	/**
	 * This data structure represents the pieces.  There are 12 pieces, and each piece can be rotated
	 * and flipped over.  Some of these motions leave the piece changed because of symmetry.  Each distinct 
	 * position of each piece has a line in this array.  Each line has 9 elements.  The first element is
	 * the number of the piece, from 1 to 12.  The remaining 8 elements describe the shape of the piece
	 * in the following peculiar way:  One square is assumed to be at position (0,0) in a grid; the square is
	 * chosen as the "top-left" square in the piece, in the sense that all the other squares are either to the
	 * right of this square in the same row, or are in lower rows.  The remaining 4 squares in the piece are
	 * encoded by 8 numbers that give the row and column of each of the remaining squares.  If the eight numbers
	 * that describe the piece are (a,b,c,d,e,f,g,h) then when the piece is placed on the board with the top-left 
	 * square at position (r,c), the remaining squares will be at positions (r+a,c+b), (r+c,c+d), (r+e,c+f), and
	 * (r+g,c+h).  This representation is used in the putPiece() and removePiece() methods. 
	 */
	private  static final int[][] pieces = {
		{ 1, 0,1,0,2,0,3,0,4 },  // Describes piece 1 (the "I" pentomino) in its horizontal orientation.
		{ 1, 1,0,2,0,3,0,4,0 },  // Describes piece 1 (the "I" pentomino) in its vertical orientation.
		{ 2, 1,-1,1,0,1,1,2,0 }, // The "X" pentomino, in its only orientation.
		{ 3, 0,1,1,0,2,-1,2,0 }, // etc....
		{ 3, 1,0,1,1,1,2,2,2 },
		{ 3, 0,1,1,1,2,1,2,2 },
		{ 3, 1,-2,1,-1,1,0,2,-2 },
		{ 4, 1,0,2,0,2,1,2,2 },
		{ 4, 0,1,0,2,1,0,2,0 },
		{ 4, 1,0,2,-2,2,-1,2,0 },
		{ 4, 0,1,0,2,1,2,2,2 },
		{ 5, 0,1,0,2,1,1,2,1 },
		{ 5, 1,-2,1,-1,1,0,2,0 },
		{ 5, 1,0,2,-1,2,0,2,1 },
		{ 5, 1,0,1,1,1,2,2,0 },
		{ 6, 1,0,1,1,2,1,2,2 },
		{ 6, 1,-1,1,0,2,-2,2,-1 },
		{ 6, 0,1,1,1,1,2,2,2 },
		{ 6, 0,1,1,-1,1,0,2,-1 },
		{ 7, 0,1,0,2,1,0,1,2 },
		{ 7, 0,1,1,1,2,0,2,1 },
		{ 7, 0,2,1,0,1,1,1,2 },
		{ 7, 0,1,1,0,2,0,2,1 },
		{ 8, 1,0,1,1,1,2,1,3 },
		{ 8, 1,0,2,0,3,-1,3,0 },
		{ 8, 0,1,0,2,0,3,1,3 },
		{ 8, 0,1,1,0,2,0,3,0 },
		{ 8, 0,1,1,1,2,1,3,1 },
		{ 8, 0,1,0,2,0,3,1,0 },
		{ 8, 1,0,2,0,3,0,3,1 },
		{ 8, 1,-3,1,-2,1,-1,1,0 },
		{ 9, 0,1,1,-2,1,-1,1,0 },
		{ 9, 1,0,1,1,2,1,3,1 },
		{ 9, 0,1,0,2,1,-1,1,0 },
		{ 9, 1,0,2,0,2,1,3,1 },
		{ 9, 0,1,1,1,1,2,1,3 },
		{ 9, 1,0,2,-1,2,0,3,-1 },
		{ 9, 0,1,0,2,1,2,1,3 },
		{ 9, 1,-1,1,0,2,-1,3,-1 },
		{ 10, 1,-2,1,-1,1,0,1,1 },
		{ 10, 1,-1,1,0,2,0,3,0 },
		{ 10, 0,1,0,2,0,3,1,1 },
		{ 10, 1,0,2,0,2,1,3,0 },
		{ 10, 0,1,0,2,0,3,1,2 },
		{ 10, 1,0,1,1,2,0,3,0 },
		{ 10, 1,-1,1,0,1,1,1,2 },
		{ 10, 1,0,2,-1,2,0,3,0 },
		{ 11, 1,-1,1,0,1,1,2,1 },
		{ 11, 0,1,1,-1,1,0,2,0 },
		{ 11, 1,0,1,1,1,2,2,1 },
		{ 11, 1,0,1,1,2,-1,2,0 },
		{ 11, 1,-2,1,-1,1,0,2,-1 },
		{ 11, 0,1,1,1,1,2,2,1 },
		{ 11, 1,-1,1,0,1,1,2,-1 },
		{ 11, 1,-1,1,0,2,0,2,1 },
		{ 12, 0,1,1,0,1,1,2,1 },
		{ 12, 0,1,0,2,1,0,1,1 },
		{ 12, 1,0,1,1,2,0,2,1 },
		{ 12, 0,1,1,-1,1,0,1,1 },
		{ 12, 0,1,1,0,1,1,1,2 },
		{ 12, 1,-1,1,0,2,-1,2,0 },
		{ 12, 0,1,0,2,1,1,1,2 },
		{ 12, 0,1,1,0,1,1,2,0 }
	};

	private Color pieceColor[] = {  // the colors of pieces number 1 through 12; pieceColor[0] is not used
			null,
			Color.rgb(200,0,0),
			Color.rgb(150,150,255),
			Color.rgb(0,200,200),
			Color.rgb(255,150,255),
			Color.rgb(0,200,0),
			Color.rgb(150,255,255),
			Color.rgb(200,200,0),
			Color.rgb(0,0,200),
			Color.rgb(255,150,150),
			Color.rgb(200,0,200),
			Color.rgb(255,255,150),
			Color.rgb(150,255,150)
	};


	/**
	 * Sets up and shows the window, which holds nothing but a MosaicCanvas.
	 * Starts a thread that solves pentominos puzzles (slowly, so the backtracking
	 * process can be seen!).
	 */
	public void start(Stage stage) {

		board = new MosaicCanvas(rows,cols,50,50);  // for displaying the board
		board.setAlwaysDrawGrouting(true);
		board.setDefaultColor(Color.WHITE);
		board.setGroutingColor(Color.LIGHTGRAY);

		board.setOnMousePressed( e -> startSolving() );  // mouse click restarts with new board
		
		Pane root = new Pane(board);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Pentominos Solver Demo");
		stage.show();
		
		startSolving();  // creates and starts the thread

	}



	/**
	 * Start or restart, with a board that is empty except for four black squares.
	 */
	private void startSolving() {  // Start the thread, if not already done, and set up a random board.
		if (gameThread == null || !gameThread.isAlive()) {
			gameThread = new GameThread();
			gameThread.setDaemon(true);
			gameThread.start();
		}
		gameThread.setMessage(RESTART_RANDOM_MESSAGE);
	}



	private class GameThread extends Thread {  // This represents the thread that solves the puzzle.
											   // All the real work of the program is done in this class.

		boolean aborted;  // used in play() to test whether the solution process has been aborted by a "restart"

		volatile int message = 0;   // "message" is used by user-interface thread to send control messages
									// to the game-playing thread.  A value of  0 indicates "no message".

		int[][] blockCheck;  // Used for checking for blocking.
		int blockCheckCt;  // Number of times block check has been run -- used in controlling recursive counting instead of just using a boolean array.

		boolean putPiece(int p, int row, int col) {  // try to place a piece on the board, return true if it fits
			if (board.getColor(row,col) != null)
				return false;
			for (int i = 1; i < 8; i += 2) {
				if (row+pieces[p][i] < 0 || row+pieces[p][i] >= rows || col+pieces[p][i+1] < 0 || col+pieces[p][i+1] >= cols)
					return false;
				else if (board.getColor(row+pieces[p][i],col+pieces[p][i+1]) != null)  // one of the squares needed is already occupied
					return false;
			}
			board.setColor(row,col,pieceColor[pieces[p][0]]);
			for (int i = 1; i < 8; i += 2)
				board.setColor(row + pieces[p][i], col + pieces[p][i+1], pieceColor[pieces[p][0]]);
			return true;
		}

		void removePiece(int p, int row, int col) { // Remove piece p from the board, at position (row,col).
			board.setColor(row,col,null);
			for (int i = 1; i < 9; i += 2) {
				board.setColor(row + pieces[p][i], col + pieces[p][i+1], null);
			}
		}

		void play(int row, int col) {   // Recursive procedure that tries to solve the puzzle
										// parameter "square" is the number of the next empty
										// to be filled.  This is only complicated because all
										// the details of speed/pause/step are handled here.
			for (int p=0; p<pieces.length; p++) {
				if (!aborted && (used[pieces[p][0]] == false)) {
					if (!putPiece(p,row,col))
						continue;
					if (obviousBlockExists()) {
						removePiece(p,row,col);
						continue;
					}
					used[pieces[p][0]] = true;  // stop this piece from being used again on the board
					numused++;
					if (message == RESTART_RANDOM_MESSAGE) {
						aborted = true;
						return;  
					}
					if (numused == 12) {  // puzzle is solved
						setMessage(RESTART_RANDOM_MESSAGE);
						doDelay(5000);
						return;
					}
					doDelay(delay);
					int nextRow = row;  // find next empty space, going left-to-right then top-to-bottom
					int nextCol = col;
					while (board.getColor(nextRow,nextCol) != null) { // find next empty square
						nextCol++;
						if (nextCol == cols) {
							nextCol = 0;
							nextRow++;
							if (nextRow == row)  // We've gone beyond the end of the board!
								throw new IllegalStateException("Internal Error -- moved beyond end of board!");
						}
					}
					play(nextRow, nextCol);  // and try to complete the solution
					if (aborted)
						return;
					removePiece(p,row,col);  // backtrack
					numused--;
					used[pieces[p][0]] = false;
				}
			}
		}

		boolean obviousBlockExists() { // Check whether the board has a region that can never be filled because of the number of squares it contains.
			blockCheckCt++;
			for (int r = 0; r < rows; r++)
				for (int c = 0; c < cols; c++) {
					int blockSize = countEmptyBlock(r,c);
					if (blockSize % 5 != 0)
						return true;
				}
			return false;
		}

		int countEmptyBlock(int r, int c) {  // Find the size of one empty region on the board; recursive routine called by obviousBlockExists.
			if (blockCheck[r][c] == blockCheckCt || board.getColor(r,c) != null)
				return 0;
			int c1 = c, c2 = c;
			while (c1 > 0 && blockCheck[r][c1-1] < blockCheckCt && board.getColor(r,c1-1) == null)
				c1--;
			while (c2 < cols-1 && blockCheck[r][c2+1] < blockCheckCt && board.getColor(r,c2+1) == null)
				c2++;
			for (int i = c1; i <= c2; i++)
				blockCheck[r][i] = blockCheckCt;
			int ct = c2 - c1 + 1;
			if (r > 0)
				for (int i = c1; i <= c2; i++)
					ct += countEmptyBlock(r-1,i);
			if (r < rows-1)
				for (int i = c1; i <= c2; i++)
					ct += countEmptyBlock(r+1,i);
			return ct;
		}


		void setUpRandomBoard() { // Set up a random board; that is, select at random the squares that will be left empty.
			board.clear();
			int x,y;
			int placed = 0;
			int choice = (int)(3*Math.random());
			switch (choice) {
			case 0: // totally random
				for (int i=0; i < 4; i ++) {
					do {
						x = (int)(cols*Math.random());
						y = (int)(rows*Math.random());
					} while (board.getColor(y,x) != null);
					board.setColor(y,x,Color.BLACK);
				}
				break;
			case 1: // Symmetric random
				while (placed < 4) {
					x = (int)(cols*Math.random());
					y = (int)(rows*Math.random());
					if (board.getColor(y,x) == null) {
						board.setColor(y,x,Color.BLACK);
						placed++;
					}
					if (placed < 4 && board.getColor(y,cols-1-x) == null) {
						board.setColor(y,cols-1-x,Color.BLACK);
						placed++;
					}
					if (placed < 4 && board.getColor(rows-1-y,x) == null) {
						board.setColor(rows-1-y,x,Color.BLACK);
						placed++;
					}
					if (placed < 4 && board.getColor(rows-1-y,cols-1-x) == null) {
						board.setColor(rows-1-y,cols-1-x,Color.BLACK);
						placed++;
					}
				}
				break;
			default: // random block
				int blockrows = 2;
				int blockcols = 2;
				x = (int)((cols - blockcols+ 1)*Math.random());
				y = (int)((rows - blockrows + 1)*Math.random());
				for (int r = 0; r < blockrows; r++)
					for (int c = 0; c < blockcols; c++) {
						board.setColor(y+r,x+c,Color.BLACK);
						placed++;
					}
				break;
			}
		}


		synchronized void doDelay(int milliseconds) {
			try {
				wait(milliseconds);
			}
			catch (InterruptedException e) {
			}
		}


		synchronized void setMessage(int message) {  // send control message to game thread
			this.message = message;
			if (message > 0)
				notify();  // wake game thread if it is sleeping or waiting for a message (in the doDelay method)
		}


		/**
		 * The run method for the thread that runs the game.
		 */
		public void run() { 
			while (true) {
				try {
					board.forceRedraw();
					if (message == RESTART_RANDOM_MESSAGE) {
						setUpRandomBoard();
						setMessage(GO_MESSAGE);
						doDelay(1000);
					}
					board.forceRedraw();
					doDelay(25);
					// begin next game
					message = 0;
					for (int i=1; i<=12; i++)
						used[i] = false;
					numused = 0;
					int startRow = 0;  // represents the upper left corner of the board
					int startCol = 0;
					while (board.getColor(startRow,startCol) != null) {
						startCol++;  // move past any filled squares, since Play(square) assumes the square is empty
						if (startCol == cols) {
							startCol = 0;
							startRow++;
						}
					}
					blockCheck = new int[rows][cols];
					blockCheckCt = 0;
					aborted = false;
					if (!obviousBlockExists())
						play(startRow,startCol);   // run the recursive algorithm that will solve the puzzle
					board.forceRedraw();
				}
				catch (Exception e) {
					System.out.println("An internal error has occurred:\n"+ e + "\nRESTARTING.");
					e.printStackTrace();
					message = RESTART_RANDOM_MESSAGE;
				}
			} // end while
		}

	} // end nested class GameThread


}

