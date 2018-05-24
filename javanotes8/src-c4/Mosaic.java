
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.util.List;


/**
 *  The class Mosaic makes available a window made up of a grid
 *  of colored rectangles.  Routines are provided for opening and
 *  closing the window and for setting and testing the color of rectangles
 *  in the grid.  The program will end when the window is closed, either
 *  because the user click's the window's close box or because the
 *  program calls Mosaic.close().
 *
 *  Each rectangle in the grid has a color.  The color can be
 *  specified by red, green, and blue amounts in the range from
 *  0 to 255.  It can also be given as an object belonging
 *  to the class Color.
 */

public class Mosaic extends Application {
	
	// Note: This class does things with static methods that should really
	// not be done in a JavaFX application!  It is NOT a good example of
	// writing a JavaFX application.  It was written to provide an API
	// to be used as an example in a textbook before objects have been covered.

	private static Stage window;    // The application running the mosaic window (if one is open).
	private static MosaicCanvas canvas;  // A component that actually manages and displays the rectangles.
	private static boolean use3DEffect = true; // When true, 3D Rects and "grouting" are used on the mosaic.
	private static int mosaicRows;      // The number of rows in the mosaic, if the window is open.
	private static int mosaicCols;      // The number of cols in the mosaic, if the window is open.


	/** 
	 * Open the mosaic window with a 20-by-20 grid of squares, where each
	 * square is 15 pixel on a side.  Has no effect if the window is already open.
	 */
	public static void open() {
		open(20,20,15,15);
	}


	/**
	 * Opens the  mosaic window containing a specified number of rows and
	 * a specified number of columns of square.  Each square is 15 pixels
	 * on a side.  Has no effect if the window is already open.
	 */
	public static void open(int rows, int columns) {
		open(rows,columns,15,15);
	}


	/**
	 * Opens the "mosaic" window on the screen.  If the mosaic window was
	 * already open, has no effect.
	 *
	 * Precondition:   The parameters rows, cols, w, and h are positive integers, and
	 *                    the mosaic window is not already open.
	 * Postcondition:  A window is open on the screen that can display rows and
	 *                   columns of colored rectangles.  Each rectangle is w pixels
	 *                   wide and h pixels high.  The number of rows is given by
	 *                   the first parameter and the number of columns by the
	 *                   second.  Initially, all rectangles are black.
	 * Note:  The rows are numbered from 0 to rows - 1, and the columns are 
	 * numbered from 0 to cols - 1.
	 */
	public static void open(int rows, int columns, int blockHeight, int blockWidth) {
		if ( window != null )
			return;
		mosaicRows = rows;
		mosaicCols = columns;
		new Thread( () -> launch(Mosaic.class, new String[] {""+rows,""+columns,""+blockWidth,""+blockHeight}) ).start();
		do {
			delay(100);
		} while (window == null || canvas == null);
	}


	/**
	 * Close the mosaic window, if one is open, and ends the program.
	 * The program will also end if the user closes the window.
	 */
	public static void close() {
		if (window != null) {
			Platform.runLater( () -> window.close() );
		}
	}


	/**
	 * Tests whether the mosaic window is currently open.
	 *
	 * Precondition:   None.
	 * Postcondition:  The return value is true if the window is open when this
	 *                   function is called, and it is false if the window is
	 *                   closed.
	 */
	public static boolean isOpen() {
		return (window != null);
	}


	/**
	 * Inserts a delay in the program (to regulate the speed at which the colors
	 * are changed, for example).  Note that there is already a short delay
	 * of about 1 millisecond between drawing operations.  Calling this method
	 * will add to that delay.
	 *
	 * Precondition:   milliseconds is a positive integer.
	 * Postcondition:  The program has paused for at least the specified number
	 *                   of milliseconds, where one second is equal to 1000
	 *                   milliseconds.
	 */
	public static void delay(int milliseconds) {
		if (milliseconds > 0) {
			try { Thread.sleep(milliseconds); }
			catch (InterruptedException e) { }
		}
	}


	/**
	 * Gets the color of one of the rectangles in the mosaic.
	 * 
	 * Precondition:   row and col are in the valid range of row and column numbers.
	 * Postcondition:  The color of the specified rectangle is returned as
	 *                 object of type color.
	 */
	public static Color getColor(int row, int col) {
		if (canvas == null)
			return Color.BLACK;
		return canvas.getColor(row, col);
	}


	/**
	 * Gets the red component of the color of one of the rectangles.
	 *
	 * Precondition:   row and col are in the valid range of row and column numbers.
	 * Postcondition:  The red component of the color of the specified rectangle is
	 *                   returned as an integer in the range 0 to 255 inclusive.
	 */
	public static int getRed(int row, int col) {
		if (canvas == null)
			return 0;
		if (row < 0 || row >= mosaicRows || col < 0 || col >= mosaicCols) {
			throw new IllegalArgumentException("(row,col) = (" + row + "," + col
					+ ") is not in the mosaic.");
		}
		return (int)(255*canvas.getRed(row, col));
	}


	/**
	 * Like getRed, but returns the green component of the color.
	 */
	public static int getGreen(int row, int col) {
		if (canvas == null)
			return 0;
		if (row < 0 || row >= mosaicRows || col < 0 || col >= mosaicCols) {
			throw new IllegalArgumentException("(row,col) = (" + row + "," + col
					+ ") is not in the mosaic.");
		}
		return (int)(255*canvas.getGreen(row, col));
	}


	/**
	 * Like getRed, but returns the blue component of the color.
	 */
	public static int getBlue(int row, int col) {
		if (canvas == null)
			return 0;
		if (row < 0 || row >= mosaicRows || col < 0 || col >= mosaicCols) {
			throw new IllegalArgumentException("(row,col) = (" + row + "," + col
					+ ") is not in the mosaic.");
		}
		return (int)(255*canvas.getBlue(row, col));
	}


	/**
	 * Sets the color of one of the rectangles in the window.
	 *
	 * Precondition:   row and col are in the valid range of row and column numbers.
	 * Postcondition:  The color of the rectangle in row number row and column
	 *                 number col has been set to the color specified by c.
	 *                 If c is null, the color of the rectangle is set to black.
	 */
	public static void setColor(int row, int col, Color c) {
		if (canvas == null)
			return;
		if (row < 0 || row >= mosaicRows || col < 0 || col >= mosaicCols) {
			throw new IllegalArgumentException("(row,col) = (" + row + "," + col
					+ ") is not in the mosaic.");
		}
		canvas.setColor(row,col,c);
	}


	/**
	 * Sets the color of one of the rectangles in the window.
	 *
	 * Precondition:   row and col are in the valid range of row and column numbers,
	 *                   and r, g, and b are in the range 0 to 255, inclusive.
	 * Postcondition:  The color of the rectangle in row number row and column
	 *                   number col has been set to the color specified by r, g,
	 *                   and b.  r gives the amount of red in the color with 0 
	 *                   representing no red and 255 representing the maximum 
	 *                   possible amount of red.  The larger the value of r, the 
	 *                   more red in the color.  g and b work similarly for the 
	 *                   green and blue color components.
	 */
	public static void setColor(int row, int col, int red, int green, int blue) {
		if (canvas == null)
			return;
		if (row < 0 || row >= mosaicRows || col < 0 || col >= mosaicCols) {
			throw new IllegalArgumentException("(row,col) = (" + row + "," + col
					+ ") is not in the mosaic.");
		}
		canvas.setColor(row,col,red/255.0,green/255.0,blue/255.0);
	}


	/**
	 * Fills the entire mosaic with a specified color.  If c is null, the mosaic
	 * is filled with black.
	 * 
	 * Precondition:  The mosaic window must be open.
	 */
	public static void fill(Color c) {
		canvas.fill(c);
	}


	/**
	 * Fills the entire mosaic with a color that is specified by giving its
	 * red, green, and blue components (numbers in the range 0 to 255).
	 * 
	 * Precondition:  The mosaic window must be open.
	 */
	public static void fill(int red, int green, int blue) {
		canvas.fill(red/255.0,green/255.0,blue/255.0);
	}


	/**
	 * Fill the entire mosaic window with random colors by setting
	 * the color of each rectangle to a randomly selected red/blue/green
	 * values.
	 * 
	 * Precondition:  The mosaic window must be open.
	 */
	public static void fillRandomly() {
		canvas.fillRandomly();
	}
	
	
	/**
	 *  If use3DEffect is true, which is the default, then rectangles are drawn
	 *  as "3D" rects, which is supposed to make them look raised up from their
	 *  background, and a 1-pixel gray border is drawn around the outside of
	 *  the rectangles, giving better definition to the rows and columns.  If
	 *  use3DEffect is set to false, ordinary "flat" rects are used, with no
	 *  border between them.  The mosaic window does not have to be open when
	 *  this is called.
	 */
	public static void setUse3DEffect(boolean use3D) {
		use3DEffect = use3D;
		if (canvas != null) {
			canvas.setGroutingColor(use3DEffect? Color.GRAY : null);
			canvas.setUse3D(use3DEffect);
		}
	}
	
	
	/**
	 * Returns the value of the use3DEffect property.
	 * @return
	 */
	public boolean getUse3DEffect() {
		return use3DEffect;
	}

	
	public void start(Stage stage) {
		window = stage;
		List<String> params = getParameters().getUnnamed();
		if (params.size() != 4)
			canvas = new MosaicCanvas();
		else
			canvas = new MosaicCanvas(Integer.parseInt(params.get(0)),Integer.parseInt(params.get(1)),
					Integer.parseInt(params.get(2)),Integer.parseInt(params.get(3)));
		if (!use3DEffect)
			canvas.setGroutingColor(null);
		canvas.setUse3D(use3DEffect);
		canvas.forceRedraw();
		Pane pane = new Pane(canvas);
		StackPane root = new StackPane(pane);
		root.setStyle("-fx-border-width: 2px; -fx-border-color: #333");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setOnCloseRequest( e -> { System.exit(0); } );
		stage.setTitle("Mosaic");
		stage.setResizable(false);
		stage.show();
	}
	

}  // end of class Mosaic
