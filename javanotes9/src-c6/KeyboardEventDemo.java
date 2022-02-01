
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;


/**
 * This program demonstrates processing of KeyEvents.  A colored square
 * is drawn on a canvas.  By pressing the arrow keys, the user can move
 * the square up, down, left, or right.  By pressing the keys
 * R, G, B, or K, the user can change the color of the square to red,
 * green, blue, or black, respectively.  If the user types a lower case
 * r, g, b, or k, the square is filled; if upper case, only the outline
 * of the square is drawn.  Pressing the space bar returns the square
 * to its original color, fill, and position.  When the SHIFT key is down, 
 * a thick cyan border is drawn around the canvas.
 */
public class KeyboardEventDemo extends Application {

	public static void main(String[] args) {
		launch();
	}

	//------------------------------------------------------------------------

	
	private static final int SQUARE_SIZE = 60;  // Length of side of square.

	private Color squareColor = Color.RED;  // The color of the square.
	
	private boolean filled = true;     // Is the square filled as opposed to just outlined?
	
	private boolean border = false;    // Is a border drawn on the canvas?

	private double squareTop = 170, squareLeft = 170;  // Coordinates corner of square.
	
	private Canvas canvas;  // The canvas on which everything is drawn.


	/** The start() method sets up the GUI and installs listeners for key events
	 * on the scene.  The scene gets a chance to handle key events that are not
	 * consumed by other components.
	 */
	public void start(Stage stage) {
		
		canvas = new Canvas(400,400);
		draw(); // Draw the canvas in its initial state.
		Pane root = new Pane(canvas);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Keyboard Event Demo");
		stage.setResizable(false);
		
		scene.setOnKeyPressed( e -> keyPressed(e) );
		scene.setOnKeyTyped( e -> keyTyped(e) );
		scene.setOnKeyReleased( e -> keyReleased(e) );
		
		stage.show();
	}
	
	//--------------- Key event handling methods --------------------
	
	private void keyTyped(KeyEvent evt) {
		
		String ch = evt.getCharacter();
		System.out.println("Char Typed: " + ch);  // for testing

		switch (ch) {  // (Note: Old switch statement syntax used here, just as an example.)
		case "r":
			squareColor = Color.RED;
			filled = true;
			draw();
			break;
		case "g":
			squareColor = Color.GREEN;
			filled = true;
			draw();
			break;
		case "b":
			squareColor = Color.BLUE;
			filled = true;
			draw();
			break;
		case "k":
			squareColor = Color.BLACK;
			filled = true;
			draw();
			break;
		case "R":
			squareColor = Color.RED;
			filled = false;
			draw();
			break;
		case "G":
			squareColor = Color.GREEN;
			filled = false;
			draw();
			break;
		case "B":
			squareColor = Color.BLUE;
			filled = false;
			draw();
			break;
		case "K":
			squareColor = Color.BLACK;
			filled = false;
			draw();
			break;
		}

	}  // end keyTyped()

	
	/**
	 * This is called each time the user presses a key while the window has
	 * the input focus.  If the key pressed was one of the arrow keys,
	 * the square is moved (except that it is not allowed to move off the
	 * edge of the canvas).  If the SHIFT key is pressed, a border is added
	 * to the canvas.  The space bar restores the original settings.
	 */
	private void keyPressed(KeyEvent evt) { 

		KeyCode key = evt.getCode();  // keyboard code for the pressed key
		System.out.println("Key Pressed: " + key);  // for testing

		if (key == KeyCode.LEFT) {  // left arrow key
			squareLeft -= 8;
			if (squareLeft < 3)
				squareLeft = 3;
			draw();
		}
		else if (key == KeyCode.RIGHT) {  // right arrow key
			squareLeft += 8;
			if (squareLeft > canvas.getWidth() - SQUARE_SIZE - 3)
				squareLeft = canvas.getWidth() - SQUARE_SIZE - 3;
			draw();
		}
		else if (key == KeyCode.UP) {  // up arrow key
			squareTop -= 8;
			if (squareTop < 3)
				squareTop = 3;
			draw();
		}
		else if (key == KeyCode.DOWN) {  // down arrow key
			squareTop += 8;
			if (squareTop > canvas.getHeight() - SQUARE_SIZE - 3)
				squareTop = canvas.getHeight() - SQUARE_SIZE - 3;
			draw();
		}
		else if (key == KeyCode.SHIFT) {
			border = true;
			draw();
		}
		else if (key == KeyCode.SPACE) {
			squareColor = Color.RED;
			filled = true;
			squareLeft = 170;
			squareTop = 170;
			draw();
		}

	}  // end keyPressed()
	

	/**
	 * This is called each time the user releases a key while the window
	 * has the input focus.  Only the SHIFT key is handled here; when it is
	 * released, the border is removed from the canvas.
	 */
	private void keyReleased(KeyEvent evt) {
		KeyCode key = evt.getCode();
		System.out.println("Key Released: " + key);  // for testing
		if (key == KeyCode.SHIFT) {
			border = false;
			draw();
		}
	}

	
	// ------------------ the method for drawing the canvas content -----------


	/**
	 * Draws a square, message and maybe a border in the canvas.  The border
	 * is shown if the SHIFT key is held down.
	 */
	public void draw() {
		
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.setFill(Color.WHITE);
		g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

		/* Draw the square. */

		if (filled) {
			g.setFill(squareColor);
			g.fillRect(squareLeft, squareTop, SQUARE_SIZE, SQUARE_SIZE);
		}
		else {
			g.setStroke(squareColor);
			g.setLineWidth(10);
			g.strokeRect(squareLeft+5, squareTop+5, SQUARE_SIZE-10, SQUARE_SIZE-10);
		}
		
		/* Maybe draw a Cyan border around the edges of the canvas. */
		
		if (border) {
			g.setStroke(Color.CYAN);
			g.setLineWidth(6);
			g.strokeRect(3,3,canvas.getWidth()-6,canvas.getHeight()-6);
		}
		
		/* Draw a message with instructions for using the keyboard. */

		g.setFill(squareColor == Color.BLACK ? Color.MAGENTA : Color.BLACK);
		g.fillText("Arrow Keys Move Square.\n" +
						"k,r,g,b change color and fill the square.\n" +
						"K,R,G,B change color and outline the square.\n"+
						"Hold down SHIFT to draw a border.\n" +
						"Space Bar restores original settings.", 
						20, 30);

	}  // end draw()


} // end class KeyboardEventDemo
