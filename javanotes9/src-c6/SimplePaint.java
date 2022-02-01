import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * A simple program where the user can sketch curves in a variety of
 * colors.  A color palette is shown along the right edge of the canvas.
 * The user can select a drawing color by clicking on a color in the
 * palette.  Under the colors is a "Clear button" that the user
 * can click to clear the sketch.  The user draws by clicking and
 * dragging in a large white area that occupies most of the canvas.
 */
public class SimplePaint extends Application {

	/**
	 * This main routine allows this class to be run as a program.
	 */
	public static void main(String[] args) {
		launch();
	}

	//-----------------------------------------------------------------

	
	/*
	 * Array of colors corresponding to available colors in the palette.
	 * (The last color is a slightly darker version of yellow for
	 * better visibility on a white background.)
	 */
	private final Color[] palette = {
			Color.BLACK, Color.RED, Color.GREEN, Color.BLUE,
			Color.CYAN, Color.MAGENTA, Color.color(0.95,0.9,0)
	};

	private int currentColorNum = 0;  // The currently selected drawing color,
	                                  //   coded as an index into the above array

	private double prevX, prevY;   // The previous location of the mouse, when
	                               // the user is drawing by dragging the mouse.

	private boolean dragging;   // This is set to true while the user is drawing.

	private Canvas canvas;  // The canvas on which everything is drawn.

	private GraphicsContext g;  // For drawing on the canvas.


	/**
	 * The start() method creates the GUI, sets up event listening, and
	 * shows the window on the screen.
	 */
	public void start(Stage stage) {
		
		/* Create the canvans and draw its content for the first time. */
		
		canvas = new Canvas(600,400);
		g = canvas.getGraphicsContext2D();
		clearAndDrawPalette();
		
		/* Respond to mouse events on the canvas, by calling methods in this class. */
		
		canvas.setOnMousePressed( e -> mousePressed(e) );
		canvas.setOnMouseDragged( e -> mouseDragged(e) );
		canvas.setOnMouseReleased( e -> mouseReleased(e) );
		
		/* Configure the GUI and show the window. */
		
		Pane root = new Pane(canvas);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Simple Paint");
		stage.show();
	}


	/**
	 * Fills the canvas with white and draws the color palette and (simulated)
	 * "Clear" button on the right edge of the canvas.  This method is called when
	 * the canvas is created and when the user clicks "Clear."
	 */
	public void clearAndDrawPalette() {

		int width = (int)canvas.getWidth();    // Width of the canvas.
		int height = (int)canvas.getHeight();  // Height of the canvas.

		g.setFill(Color.WHITE);
		g.fillRect(0,0,width,height);

		int colorSpacing = (height - 56) / 7;
		// Distance between the top of one colored rectangle in the palette
		// and the top of the rectangle below it.  The height of the
		// rectangle will be colorSpacing - 3.  There are 7 colored rectangles,
		// so the available space is divided by 7.  The available space allows
		// for the gray border and the 50-by-50 CLEAR button.

		/* Draw a 3-pixel border around the canvas in gray.  This has to be
			 done by drawing three rectangles of different sizes. */

		g.setStroke(Color.GRAY);
		g.setLineWidth(3);
		g.strokeRect(1.5, 1.5, width-3, height-3);

		/* Draw a 56-pixel wide gray rectangle along the right edge of the canvas.
			 The color palette and Clear button will be drawn on top of this.
			 (This covers some of the same area as the border I just drew. */

		g.setFill(Color.GRAY);
		g.fillRect(width - 56, 0, 56, height);

		/* Draw the "Clear button" as a 50-by-50 white rectangle in the lower right
			 corner of the canvas, allowing for a 3-pixel border. */

		g.setFill(Color.WHITE);
		g.fillRect(width-53,  height-53, 50, 50);
		g.setFill(Color.BLACK);
		g.fillText("CLEAR", width-48, height-23); 

		/* Draw the seven color rectangles. */
		
		for (int N = 0; N < 7; N++) {
			g.setFill( palette[N] );
			g.fillRect(width-53, 3 + N*colorSpacing, 50, colorSpacing-3);
		}

		/* Draw a 2-pixel white border around the color rectangle
			 of the current drawing color. */

		g.setStroke(Color.WHITE);
		g.setLineWidth(2);
		g.strokeRect(width-54, 2 + currentColorNum*colorSpacing, 52, colorSpacing-1);

	} // end clearAndDrawPalette()


	/**
	 * Change the drawing color after the user has clicked the
	 * mouse on the color palette at a point with y-coordinate y.
	 */
	private void changeColor(int y) {

		int width = (int)canvas.getWidth(); 
		int height = (int)canvas.getHeight(); 
		int colorSpacing = (height - 56) / 7;  // Space for one color rectangle.
		int newColor = y / colorSpacing;       // Which color number was clicked?

		if (newColor < 0 || newColor > 6)      // Make sure the color number is valid.
			return;

		/* Remove the highlight from the current color, by drawing over it in gray.
			 Then change the current drawing color and draw a highlight around the
			 new drawing color.  */
		
		g.setLineWidth(2);
		g.setStroke(Color.GRAY);
		g.strokeRect(width-54, 2 + currentColorNum*colorSpacing, 52, colorSpacing-1);
		currentColorNum = newColor;
		g.setStroke(Color.WHITE);
		g.strokeRect(width-54, 2 + currentColorNum*colorSpacing, 52, colorSpacing-1);

	} // end changeColor()



	/**
	 * This is called when the user presses the mouse anywhere in the canvas.  
	 * There are three possible responses, depending on where the user clicked:  
	 * Change the current color, clear the drawing, or start drawing a curve.  
	 * (Or do nothing if user clicks on the border.)
	 */
	public void mousePressed(MouseEvent evt) {

		if (dragging == true)  // Ignore mouse presses that occur
			return;            //    when user is already drawing a curve.
							   //    (This can happen if the user presses
							   //    two mouse buttons at the same time.)

		int x = (int)evt.getX();   // x-coordinate where the user clicked.
		int y = (int)evt.getY();   // y-coordinate where the user clicked.

		int width = (int)canvas.getWidth();    // Width of the canvas.
		int height = (int)canvas.getHeight();  // Height of the canvas.

		if (x > width - 53) {
			// User clicked to the right of the drawing area.
			// This click is either on the clear button or
			// on the color palette.
			if (y > height - 53)
				clearAndDrawPalette();  //  Clicked on "CLEAR button".
			else
				changeColor(y);  // Clicked on the color palette.
		}
		else if (x > 3 && x < width - 56 && y > 3 && y < height - 3) {
			// The user has clicked on the white drawing area.
			// Start drawing a curve from the point (x,y).
			prevX = x;
			prevY = y;
			dragging = true;
			g.setLineWidth(2);  // Use a 2-pixel-wide line for drawing.
			g.setStroke( palette[currentColorNum] );
		}

	} // end mousePressed()


	/**
	 * Called whenever the user releases the mouse button. Just sets
	 * dragging to false.
	 */
	public void mouseReleased(MouseEvent evt) {
		dragging = false;
	}


	/**
	 * Called whenever the user moves the mouse while a mouse button is held down.  
	 * If the user is drawing, draw a line segment from the previous mouse location 
	 * to the current mouse location, and set up prevX and prevY for the next call.  
	 * Note that in case the user drags outside of the drawing area, the values of
	 * x and y are "clamped" to lie within this area.  This avoids drawing on the color 
	 * palette or clear button.
	 */
	public void mouseDragged(MouseEvent evt) {

		if (dragging == false)
			return;  // Nothing to do because the user isn't drawing.

		double x = evt.getX();   // x-coordinate of mouse.
		double y = evt.getY();   // y-coordinate of mouse.

		if (x < 3)                          // Adjust the value of x,
			x = 3;                           //   to make sure it's in
		if (x > canvas.getWidth() - 57)       //   the drawing area.
			x = (int)canvas.getWidth() - 57;

		if (y < 3)                          // Adjust the value of y,
			y = 3;                           //   to make sure it's in
		if (y > canvas.getHeight() - 4)       //   the drawing area.
			y = canvas.getHeight() - 4;

		g.strokeLine(prevX, prevY, x, y);  // Draw the line.

		prevX = x;  // Get ready for the next line segment in the curve.
		prevY = y;

	} // end mouseDragged()


} // end class SimplePaint
