import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;

/**
 * This program demonstrates stack and queue operations.  The program shows
 * a grid of squares.  When the user clicks on one of the squares, a computation
 * is begun that visits all the squares of the grid.  As the squares
 * are "encountered", they are colored red.  Red squares have been encountered
 * but not yet processed.  A square is processed by adding its horizontal
 * and vertical neighbors to the set of encountered squares, if they have
 * not previously been encountered.  Once a square has been processed in
 * this way, it is "finished", and it is colored gray.  At the end of the
 * process, all the squares are gray.
 *    The question is, how does the program decide which red square to
 * process?  There can be many red squares waiting for processing.
 * The user can specify one of three methods for deciding which square
 * to process:  with a stack, with a queue, or at random.  If the random
 * method is chosen, then a red square is chosen for processing at random
 * from among all the red squares.  If a queue is used, the red squares
 * are stored on a queue and are processed in FIFO order.  If a stack
 * is used, then the squares are processed in LIFO order.
 *    (Note:  If the user clicks on a white square while a computation is
 * already running, then that square will be "encountered" and added to
 * the set of red squares.)
 */
public class DepthBreadth extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//----------------------------------------------------------------------

	
	// ------------------ NESTED CLASSES for stacks and queues --------------

	/**
	 * Represents one square in the grid, by specifying the
	 * row number and column number where it is found.
	 */
	private static class Location {
		// 
		int row;
		int column;
		Location(int r, int c) {
				// Constructor, specifying the row and column of a square.
			row = r;
			column = c;
		}
	}  // end nested class Location


	/**
	 * Represents a node in a linked list of Locations.  Both the
	 * Stack and the Queue class use this type of linked list.
	 */
	private static class Node {
		Location loc;  // Represents one square in the grid.
		Node next;     // Pointer to next Node in the linked list.
	}  // end nested class Node


	/**
	 * A stack of Locations, with the standard operations,
	 * plus a getSize() method that returns the number of 
	 * Locations on the stack.
	 */
	private static class Stack {
		private Node top = null;  // Pointer to the top of the stack.
		private int size = 0;     // Number of items on the stack.
		void push(Location loc) {
				// Add the specified location to the top of the stack.
			Node newTop = new Node();
			newTop.loc = loc;
			newTop.next = top;
			top = newTop;
			size++;
		}
		Location pop() {
				// Remove and return the top Location on the stack.
				// (Note that this can throw a NullPointerException if
				// it is called when the stack is empty.)
			Location topItem = top.loc;
			top = top.next;
			size--;
			return topItem;
		}
		boolean isEmpty() {
			// Return true if the stack is empty.
			return top == null;
		}
		int getSize() {
			// Return the number of Locations on the stack. 
			return size;
		}
	}  // end nested class Stack


	/**
	 * A queue of Locations, with the standard operations,
	 * plus a getSize() method that returns the number of 
	 * Locations on the queue.
	 */
	private static class Queue {
		private Node head = null;  // Points to first Node in the queue.
		private Node tail = null;  // Points to last Node in the queue.
		private int size;   // Number of items on the queue.
		void enqueue(Location loc) {
				// Add the specified Location to the end of the queue.
			Node newTail = new Node();
			newTail.loc = loc;
			if (head == null) {
				head = newTail;
				tail = newTail;
			}
			else {
				tail.next = newTail;
				tail = newTail;
			}
			size++;
		}
		Location dequeue() {
				// Remove and return the first item in the queue.
				// (Note that this will throw a NullPointerException
				// if the queue is empty.)
			Location firstItem = head.loc;
			head = head.next;
			if (head == null)
				tail = null;
			size--;
			return firstItem;
		}
		boolean isEmpty() {
			// Return true if the queue is empty.
			return head == null;
		}
		int getSize() {
			// Return the number of items on the queue.
			return size;
		}
	}  // end nested class Queue


	//------------------------------------------------------------------------------------------
	

	private final static int SQUARE_SIZE = 12;  // Size of one square in the grid.
	
	private Canvas canvas;       // The canvas where the squares are drawn.
	private GraphicsContext g;   // Graphics context for drawing on the canvas.
	
	private int width = 334;     // Size of the scene (since I'm doing my own layout).
	private int height = 410;

	private int rows;     // Number of rows in the grid.  This depends on the size of the panel.
	private int columns;  // Number of columns in the grid.  This depends on the size of the panel.

	private boolean[][] encountered;  // encountered[r][c] is set to true when a square is
									  //   first encountered.  (See comment at top of file.)
									  //   A square that has been encountered but not
									  //   finished is red.

	private boolean[][] finished;   // finished[r][c] is set to true when a square is
									//   finished (i.e. processed).  Finished squares are gray.

	private Button abortButton;  // User can click this to terminate the computation.

	private Label message;   // For displaying information to the user.

	private ComboBox<String> methodChoice; // For selecting the method of 
											//   selecting which red square to process.

	private final static int STACK = 0,     // Possible values for the method.
			QUEUE = 1,
			RANDOM = 2;

	private int method; // Used to hold the selected method while a
						//    computation is running.

	private AnimationTimer timer;  // A timer that drives the computation.
						           // When no computation is in progress, timer is null.                          

	private Queue queue;                     // Exactly one of these is used to store the
	private Stack stack;                     //   red squares while the computation is running.
	private ArrayList<Location> randomList;  //   Which one is used depends on the method.



	/**
	 * Start method sets up the GUI and event handlers.
	 */
	public void start(Stage stage) {

		/* Determine the number of rows and columns and create the
                  encountered and finished arrays. */

		rows = (height - 130) / SQUARE_SIZE;
		columns = (width - 20) / SQUARE_SIZE;

		encountered = new boolean[rows][columns];
		finished = new boolean[rows][columns];

		/* Create the components. */
		
		canvas = new Canvas(1+columns*SQUARE_SIZE, 1+rows*SQUARE_SIZE);
		g = canvas.getGraphicsContext2D();
		canvas.setOnMousePressed( e -> mousePressed(e) );

		message = new Label("Click any square to begin.");
		message.setTextFill(Color.BLUE);
		message.setFont(Font.font( null, FontWeight.BOLD, 14 ));

		methodChoice = new ComboBox<String>();
		methodChoice.getItems().add("Stack");
		methodChoice.getItems().add("Queue");
		methodChoice.getItems().add("Random");
		methodChoice.setEditable(false);
		methodChoice.setValue("Queue");

		abortButton = new Button("Abort");
		abortButton.setDisable(true);
		abortButton.setOnAction( e -> doAbort() );

		Label lb = new Label("Use:");  // An unchanging informational label.
		lb.setTextFill(Color.BLUE);
		message.setFont(Font.font( null, FontWeight.BOLD, 14 ));

		/* Create a root pane and add all the components.
		 * Do the layout by hand! */
		
		Pane root = new Pane(canvas, message, abortButton, methodChoice, lb);
		root.setStyle("-fx-background-color: #BBF; -fx-border-color: #00A; -fx-border-width:2px");
		
		canvas.relocate(10,10);
		message.setManaged(false);
		message.relocate(15, height-118);
		message.resize(width-30, 25);
		message.setAlignment(Pos.CENTER);
		abortButton.setManaged(false);
		abortButton.relocate(75, height-85);
		abortButton.resize(width-150, 30);
		methodChoice.setManaged(false);
		methodChoice.relocate(75, height-42);
		methodChoice.resize(width-150, 30);
		lb.relocate(30, height-35);
		
		/* Set up the scene and window and show the window. */
		
		Scene scene = new Scene(root, width, height );
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Stack and Queue Demo");
		draw();  // Draw initial grid.
		stage.show();

	} // end start();


	/**
	 * The user has clicked the mouse on the canvas.  If the user has clicked on 
	 * a position in the grid, start a computation to start processing from that 
	 * square, or if a computation is already running, "encounter" the square.
	 */
	public void mousePressed(MouseEvent evt) {
		int row = (int)((evt.getY() - 1) / SQUARE_SIZE);
		int col = (int)((evt.getX() - 1) / SQUARE_SIZE);
		if (row < 0 || row >= rows || col < 0 || col >= columns)
			return; // shouldn't happen
		if (timer == null) {
				// Start a new computation at the point where the user clicked.
			startComputation(row,col);
		}
		else {
				// A computation is already in progress.
				// Mark the square where the user clicked as encountered.
			encounter(row,col);
			draw();
		}
	} // end mousePressed()



	/**
	 * Begin a new computation.  Set all the squares back to unencountered 
	 * and start a timer that will process the squares beginning with
	 * the square at (startRow,startCol).
	 */	
	void startComputation(int startRow, int startCol) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				encountered[r][c] = false;
				finished[r][c] = false;  
			}
		}
		method = methodChoice.getSelectionModel().getSelectedIndex();
		switch (method) {
		case STACK:
			stack = new Stack();
			message.setText("Using a stack.");
			break;
		case QUEUE:
			queue = new Queue();
			message.setText("Using a queue.");
			break;
		case RANDOM:
			randomList = new ArrayList<Location>();
			message.setText("Using a randomized list.");
			break;
		}
		abortButton.setDisable(false);
		methodChoice.setDisable(true);
		encounter(startRow,startCol);
		timer = new AnimationTimer() {
			final double oneTwentiethSecond = 1e9/20;  // 1/20 of one billion nanoseconds.
			long previousTime = 0;  // Time of the previousCall to continueComputation 
			public void handle(long time) {
				if ( (time - previousTime) > 0.95*oneTwentiethSecond) { 
					continueComputation();
					previousTime = time;
				}
			}
		};
		timer.start();
	}


	/**
	 * Do one step in a computation, by processing
	 * one location from the stack, queue, or arraylist.
	 * If no more items are available, finish the computation.
	 */
	public void continueComputation() {
		Location loc = removeItem();
		if (loc != null) {
			finish(loc.row, loc.column);
		}
		else {
				// All squares have already been "finished".  The
				// computation is complete.
			timer.stop();
			timer = null;
			methodChoice.setDisable(false);
			abortButton.setDisable(true);
			message.setText("Click any square to begin.");
			queue = null;
			stack = null;
			randomList = null;
		}
	}


	/**
	 * Stop the computation, if one is running.  This is called
	 * when the user clicks the Abort button.
	 */
	void doAbort() {
		if (timer != null) {
			timer.stop();
			timer = null;
			methodChoice.setDisable(false);
			abortButton.setDisable(true);
			message.setText("Click any square to begin.");
			queue = null;
			stack = null;
			randomList = null;
		}
	}


	/**
	 * Get the next item to be processed from the appropriate data structure.  
	 * The data structure that is being used depends on the method.  If the data 
	 * structure is empty, return null.  Also, display the size of the data 
	 * structure to the user.
	 */
	Location removeItem() {
		Location loc = null;
		switch (method) {
		case STACK:
			if ( ! stack.isEmpty() )
				loc = stack.pop();
			message.setText("Stack size is " + stack.getSize());
			break;
		case QUEUE:
			if ( ! queue.isEmpty() )
				loc = queue.dequeue();
			message.setText("Queue size is " + queue.getSize());
			break;
		case RANDOM:
			if ( randomList.size() > 0 ) {
				int index = (int)(randomList.size()*Math.random());
				loc = randomList.get(index);
				randomList.remove(index);
			}
			message.setText("List size is " + randomList.size());
			break;
		}
		return loc;
	}


	/**
	 * If there is a square at (r,c) that has not already been encountered,
	 * encounter it and add it to the data structure.  The data structure
	 * that is used depends on the method.  Also, display the size of the 
	 * data structure.
	 */
	void encounter(int r, int c) {
		if (r < 0 || r >= rows || c < 0 || c >= columns || encountered[r][c] == true)
			return;
		Location loc = new Location(r,c);
		switch (method) {
		case STACK:
			stack.push(loc);
			message.setText("Stack size is " + stack.getSize());
			break;
		case QUEUE:
			queue.enqueue(loc);
			message.setText("Queue size is " + queue.getSize());
			break;
		case RANDOM:
			randomList.add(loc);
			message.setText("List size is " + randomList.size());
			break;
		}
		encountered[r][c]  = true;
	}


	/**
	 * Process the red square at (r,c) by encountering its horizontal and 
	 * vertical neighbors.  Any neighbors will be changed to red, and
	 * the square at (r,c) will be changed to gray.  The grid is
	 * redrawn to show the change.
	 */
	void finish(int r, int c) {
		encounter(r-1,c);
		encounter(r+1,c);
		encounter(r,c-1);
		encounter(r,c+1);
		finished[r][c] = true;
		draw();
	}

	/**
	 * Paint the grid of squares.  This is called every time the grid data changes, even
	 * if it's just one square that has been changed.  (Not too efficient, but efficient
	 * enough for this program.)
	 */
	public void draw() {

		/* Fill the entire canvas with white, then draw  black lines around 
		 * the edges and between the squares of the grid. */

		g.setFill(Color.WHITE);
		g.fillRect(0, 0, 1+columns*SQUARE_SIZE, 1+rows*SQUARE_SIZE);

		g.setStroke(Color.BLACK);
		for (int i = 0; i <= rows; i++)
			g.strokeLine(0.5, 0.5 + i*SQUARE_SIZE, columns*SQUARE_SIZE + 0.5, 0.5 + i*SQUARE_SIZE);
		for (int i = 0; i <= columns; i++)
			g.strokeLine(0.5 + i*SQUARE_SIZE, 0.5, 0.5 + i*SQUARE_SIZE, rows*SQUARE_SIZE + 0.5);

		/* Fill "encountered" squares with red and "finished" squares with gray.
               Other squares remain white.  */

		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				if (finished[r][c]) {
					g.setFill(Color.GRAY);
					g.fillRect(1 + c*SQUARE_SIZE, 1 + r*SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1);
				}
				else if (encountered[r][c]) {
					g.setFill(Color.RED);
					g.fillRect(1 + c*SQUARE_SIZE, 1 + r*SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1);
				}
			}

	} // end draw();



} // end class DepthBreadth
