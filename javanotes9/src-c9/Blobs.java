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

/**
 * This program demonstrates recursion by counting the number of
 * squares in a "blob".  The squares are arranged in a grid,
 * and each position in the grid can be either empty or filled.
 * A blob is defined to be a filled square and any square that
 * can be reached from that square by moving horizontally or
 * vertically to other filled squares.  This program fills
 * the grid randomly.  If the user clicks on a filled square,
 * all the squares in the blob that contains that square are
 * colored red, and the number of squares in the blob is
 * reported.  The program can also count and report the number
 * of blobs.  When the user clicks a "New Blobs" button,
 * the grid is randomly re-filled.
 */
public class Blobs extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//-------------------------------------------------------------------------


	final static int SQUARE_SIZE = 9;  // Size of one square in the grid.

	final static int width = 454;  // full size of the Scene
	final static int height = 400;
	
	Canvas canvas;       // Where the blobs are drawn.
	GraphicsContext g;   // For drawing on the canvas.

	Label message;       // For displaying information to the user.

	ComboBox<String> percentFill;   // When the user clicks the "New Blobs" button
									// to randomly fill the grid, this menu controls
									// the probability that a given square in the grid
									// is filled.

	int rows;     // Number of rows in the grid.  This depends on the size of the window.
	int columns;  // Number of columns in the grid.  This depends on the size of the window.

	boolean[][] filled;  // filled[r][c] is true if the square at row r, column c is filled.

	boolean[][] visited; // visited[r][c] is true if the square at row r, column c has
						 //   has already been visited by the getBlobSize() method.


	public void start(Stage stage) {
		
		/* Determine the number of rows and columns and create the
         * filled and visited arrays.  Fill the squares at random. */

		rows = (height - 120) / SQUARE_SIZE;
		columns = (width - 20) / SQUARE_SIZE;
		filled = new boolean[rows][columns];
		visited = new boolean[rows][columns];

		canvas = new Canvas( 1+columns*SQUARE_SIZE, 1+rows*SQUARE_SIZE );
		g = canvas.getGraphicsContext2D();
		canvas.setOnMousePressed( e -> mousePressed(e) );
		
		/* Create the components. */

		message = new Label("Click a square to get the blob size.");
		message.setTextFill(Color.BLUE);
		message.setFont( Font.font(null,FontWeight.BOLD,14) );

		percentFill = new ComboBox<String>();
		percentFill.getItems().add("10% fill");
		percentFill.getItems().add("20% fill");
		percentFill.getItems().add("30% fill");
		percentFill.getItems().add("40% fill");
		percentFill.getItems().add("50% fill");
		percentFill.getItems().add("60% fill");
		percentFill.getItems().add("70% fill");
		percentFill.getItems().add("80% fill");
		percentFill.getItems().add("90% fill");
		percentFill.setEditable(false);
		percentFill.setValue("40% fill");

		Button newButton = new Button("New Blobs");
		newButton.setOnAction( e -> fillGrid() );

		Button countButton = new Button("Count the Blobs");
		countButton.setOnAction( e -> countBlobs() );

		/* Create a root pane and add all the components.
		 * Do the layout by hand! */
		
		Pane root = new Pane(canvas, message, percentFill, newButton, countButton);
		root.setStyle("-fx-background-color: #BBF; -fx-border-color: #00A; -fx-border-width:2px");
		
		canvas.relocate(10,10);
		message.setManaged(false);
		message.relocate(15, height-100);
		message.resize( width-30, 23);
		message.setAlignment(Pos.CENTER);
		countButton.setManaged(false);
		countButton.relocate(15, height-72);
		countButton.resize(width-30, 28);
		newButton.setManaged(false);
		newButton.relocate(15, height-37);
		newButton.resize((width-40)/2, 28);
		percentFill.setManaged(false);
		percentFill.relocate(width/2 + 5, height-37);
		percentFill.resize((width-40)/2, 28);
		
		/* Set up the scene and window and show the window. */
		
		Scene scene = new Scene(root, width, height );
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Random Blob Counter");
		fillGrid();
		stage.show();

	} // end start()



	/**
	 *  When the user clicks the "New Blobs" button, fill the grid of squares
	 *  randomly.  The probability that a given square is filled is given by
	 *  the percentFill Choice menu.  The probabilities corresponding to the
	 *  items in that menu are 0.1, 0.2,... 0.9.  The visited array is cleared
	 *  so there won't be any red-colored squares in the grid.
	 */
	private void fillGrid() {
		double probability = (percentFill.getSelectionModel().getSelectedIndex() + 1) / 10.0;
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				filled[r][c] = (Math.random() < probability);
				visited[r][c] = false;
			}
		message.setText("Click a square to get the blob size.");
		draw();
	}


	/**
	 * When the user clicks the "Count the Blobs" button, find the number
	 * of blobs in the grid and report the number in the message Label.
	 */
	private void countBlobs() {

		int count = 0; // Number of blobs.

		/* First clear out the visited array. The getBlobSize() method will
               mark every filled square that it finds by setting the corresponding
               element of the array to true.  Once a square has been marked as
               visited, it will stay marked until all the blobs have been counted.
               This will prevent the same blob from being counted more than once. */

		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				visited[r][c] = false;

		/* For each position in the grid, call getBlobSize() to get the size
			   of the blob at that position.  If the size is not zero, count a blob.
               Note that if we come to a position that was part of a previously
               counted square, getBlobSize() will return 0 and the blob will not
               be counted again. */

		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				if (getBlobSize(r,c) > 0)
					count++;
			}

		draw();  // Note that all the filled squares will be red!

		message.setText("The number of blobs is " + count);

	} // end countBlobs()


	/**
	 * Counts the squares in the blob at position (r,c) in the
	 * grid.  Squares are only counted if they are filled and
	 * unvisited.  If this routine is called for a position that
	 * has been visited, the return value will be zero.
	 */
	private int getBlobSize(int r, int c) {
		if (r < 0 || r >= rows || c < 0 || c >= columns) {
			// This position is not in the grid, so there is
			// no blob at this position.
			return 0;
		}
		if (filled[r][c] == false || visited[r][c] == true) {
			// This square is not part of a blob, or else it has
			// already been counted, so return zero.
			return 0;
		}
		visited[r][c] = true;   // Mark the square as visited so that
								//    we won't count it again during the
								//    following recursive calls to this method.
		int size = 1;   // Count the square at this position, then count the
						//   the blobs that are connected to this square
						//    horizontally or vertically.
		size += getBlobSize(r-1,c);
		size += getBlobSize(r+1,c);
		size += getBlobSize(r,c-1);
		size += getBlobSize(r,c+1);
		return size;
	}  // end getBlobSize()


	/**
	 * The user has clicked the mouse on the panel.  If the
	 * user has clicked on a position in the grid, count
	 * the number of squares in the blob at that position.
	 */
	private void mousePressed(MouseEvent evt) {
		int row = (int)((evt.getY()-1) / SQUARE_SIZE);
		int col = (int)((evt.getX()-1) / SQUARE_SIZE);
		if (row < 0 || row >= rows || col < 0 || col >= columns) {
			message.setText("Please click on a square!"); // shouldn't happen
			return;
		}
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				visited[r][c] = false;  // Clear visited array before counting.
		int size = getBlobSize(row,col);
		if (size == 0)
			message.setText("There is no blob at (" + row + "," + col + ").");
		else if (size == 1)
			message.setText("Blob at (" + row + "," + col + ") contains 1 square.");
		else
			message.setText("Blob at (" + row + "," + col + ") contains " + size + " squares.");
		draw();
	}


	/**
	 * Paint the panel, showing the grid of squares.  (The other components 
	 * in the panel draw themselves.)
	 */
	public void draw() {
		
		/* Fill the entire canvas with white, then draw  black lines around 
		 * the edges and between the squares of the grid. */

		g.setFill(Color.WHITE);
		g.fillRect(0, 0, columns*SQUARE_SIZE, rows*SQUARE_SIZE);

		g.setStroke(Color.BLACK);
		for (int i = 0; i <= rows; i++)
			g.strokeLine(0.5, 0.5 + i*SQUARE_SIZE, columns*SQUARE_SIZE + 0.5, 0.5 + i*SQUARE_SIZE);
		for (int i = 0; i <= columns; i++)
			g.strokeLine(0.5 + i*SQUARE_SIZE, 0.5, 0.5 + i*SQUARE_SIZE, rows*SQUARE_SIZE + 0.5);

		/* Fill "visited" squares with red and "filled" squares with gray.
               Other squares remain white.  */

		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				if (visited[r][c]) {
					g.setFill(Color.RED);
					g.fillRect(1 + c*SQUARE_SIZE, 1 + r*SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1);
				}
				else if (filled[r][c]) {
					g.setFill(Color.GRAY);
					g.fillRect(1 + c*SQUARE_SIZE, 1 + r*SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1);
				}
			}

	} // end draw();


} // end class Blobs
