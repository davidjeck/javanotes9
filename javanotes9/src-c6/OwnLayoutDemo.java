
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * This program demonstrates how to do your own layout.  The root container
 * for the screen is a Pane, which will resize its children to their
 * preferred size, but will not set their location.  This program does
 * not do anything useful.
 *
 * The program sets the locations of a canvas that shows a checkerboard
 * and a label.  The Pane will set the size of the label.  Since a canvas
 * is not resizable, it is simply shown at its natural size.  Two
 * buttons are also shown.  They are set to be "unmanaged" which stops
 * any container from setting their size and positions.  This makes it
 * possible to set the size of the buttons as well as their locations.
 */

public class OwnLayoutDemo extends Application {

	public static void main(String[] args) {
		launch();
	}

	//-------------------------------------------------------------------------

	Checkerboard board;  // A canvas on which a checker board is drawn,
	                     // defined by a static nested subclass.

	Button resignButton;      // Two buttons.
	Button newGameButton;

	Label message;   // A label for displaying messages to the user.

	int clickCount;   // Counts how many times the button was clicked.


	/**
	 * The start method uses a Pane as a root node of the scene.  The preferred
	 * size of the Pane is set explicitly to 500-by-420; the stage will take its
	 * size from the preferred size of the Pane.  Four child nodes are added to
	 * the Pane.  The location of each child is set, and sizes are set for
	 * two buttons.  The buttons are set to be unmanaged to stop the Pane
	 * from setting their sizes.  (Without the preferred size for the Pane,
	 * it would be just large enough to show the checkerboard and the label,
	 * but not the buttons, which it ignores.)
	 */
	public void start(Stage stage) {

		/* Create the child nodes. */

		board = new Checkerboard(); // a subclass of Canvas, defined below
		board.draw();  // draws the content of the checkerboard

		newGameButton = new Button("New Game");
		newGameButton.setOnAction( e -> doNewGame() );

		resignButton = new Button("Resign");
		resignButton.setOnAction( e -> doResign() );

		message = new Label("Click \"New Game\" to begin.");
		message.setTextFill( Color.rgb(100,255,100) ); // Light green.
		message.setFont( Font.font(null, FontWeight.BOLD, 18) );

		/* Set the location of each child by calling its relocate() method */

		board.relocate(20,20);
		newGameButton.relocate(370, 120);
		resignButton.relocate(370, 200);
		message.relocate(20, 370);
		
		/* Set the sizes of the buttons.  For this to have an effect, make
		 * the butons "unmanaged."  If they are managed, the Pane will set
		 * their sizes. */
		
		resignButton.setManaged(false);
		resignButton.resize(100,30);
		newGameButton.setManaged(false);
		newGameButton.resize(100,30);
		
		/* Create the Pane and give it a preferred size. */
		
		Pane root = new Pane();
		
		root.setPrefWidth(500);
		root.setPrefHeight(420);
		
		/* Add the child nodes to the Pane and set up the rest of the GUI */

		root.getChildren().addAll(board, newGameButton, resignButton, message);
		root.setStyle("-fx-background-color: darkgreen; -fx-border-color: darkred; -fx-border-width:3");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Doing your own layout");
		stage.show();

	} // end start()

	
	/**
	 * A method to be called when the user clicks "New Game"
	 */
	private void doNewGame() {
		clickCount++;
		if (clickCount == 1)
			message.setText("First click:  \"New Game\" was clicked.");
		else
			message.setText("Click no. " + clickCount + ":  \"New Game\" was clicked."); 
	}
	
	
	/**
	 * A method to be called when the user clicks "Resign"
	 */
	private void doResign() {
		clickCount++;
		if (clickCount == 1)
			message.setText("First click:  \"Resign\" was clicked.");
		else
			message.setText("Click no. " + clickCount + ":  \"Resign\" was clicked."); 
	}


	/**
	 * This canvas displays a 320-by-320 checkerboard pattern with
	 * a 2-pixel dark red border.  The canvas will be exactly
	 * 324-by-324 pixels.
	 */
	private static class Checkerboard extends Canvas {

		public Checkerboard() {
			super(324,324); // Call constructor from Canvas class to set the size.
		}

		/**
		 * Draws the content of the canvas.
		 */
		public void draw() {
			
			GraphicsContext g = getGraphicsContext2D();

			// Draw a 2-pixel dark red border around the edges of the board.

			g.setStroke(Color.DARKRED);
			g.setLineWidth(2);
			g.strokeRect( 1, 1, 322, 322 );

			// Draw  checkerboard pattern in gray and lightGray.

			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					if ( row % 2 == col % 2 )
						g.setFill(Color.LIGHTGRAY);
					else
						g.setFill(Color.GRAY);
					g.fillRect(2 + col*40, 2 + row*40, 40, 40);
				}
			}
			
		}
		
	} // end nested class Checkerboard


} // end class OwnLayoutDemo




