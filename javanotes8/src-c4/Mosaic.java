
import java.awt.*;
import javax.swing.*;


/**
 *  The class Mosaic makes available a window made up of a grid
 *  of colored rectangles.  Routines are provided for opening and
 *  closing the window and for setting and testing the color of rectangles
 *  in the grid.
 *
 *  Each rectangle in the grid has a color.  The color can be
 *  specified by red, green, and blue amounts in the range from
 *  0 to 255.  It can also be given as an object belonging
 *  to the class Color.
 */

public class Mosaic {

	private static JFrame window;       // A mosaic window, null if no window is open.
	private static MosaicPanel canvas;  // A component that actually manages and displays the rectangles.
	private static boolean use3DEffect = true; // When true, 3D Rects and "grouting" are used on the mosaic.
	private static int mosaicRows;      // The number of rows in the mosaic, if the window is open.
	private static int mosaicCols;      // The number of cols in the mosaic, if the window is open.


	/** 
	 * Open a mosaic window with a 20-by-20 grid of squares, where each
	 * square is 15 pixel on a side.
	 */
	public static void open() {
		open(20,20,15,15);
	}


	/**
	 * Opens a mosaic window containing a specified number of rows and
	 * a specified number of columns of square.  Each square is 15 pixels
	 * on a side.
	 */
	public static void open(int rows, int columns) {
		open(rows,columns,15,15);
	}


	/**
	 * Opens a "mosaic" window on the screen.  If another mosaic window was
	 * already open, that one is closed and a new one is created.
	 *
	 * Precondition:   The parameters rows, cols, w, and h are positive integers.
	 * Postcondition:  A window is open on the screen that can display rows and
	 *                   columns of colored rectangles.  Each rectangle is w pixels
	 *                   wide and h pixels high.  The number of rows is given by
	 *                   the first parameter and the number of columns by the
	 *                   second.  Initially, all rectangles are black.
	 * Note:  The rows are numbered from 0 to rows - 1, and the columns are 
	 * numbered from 0 to cols - 1.
	 */
	public static void open(int rows, int columns, int blockWidth, int blockHeight) {
		if (window != null)
			window.dispose();
		canvas = new MosaicPanel(rows,columns,blockWidth,blockHeight);
		mosaicRows = rows;
		mosaicCols = columns;
		if ( ! use3DEffect ) {
			canvas.setGroutingColor(null);
			canvas.setUse3D(false);
		}
		window = new JFrame("Mosaic Window");
		window.setContentPane(canvas);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		if (window.getWidth() > screen.width - 20 || window.getHeight() > screen.height - 100) {
			// change size to fit on screen
			int w = window.getWidth();
			int h = window.getHeight();
			if (window.getWidth() > screen.width - 20)
				w = screen.width - 20;
			if (window.getHeight() > screen.height - 100)
				h = screen.height - 100;
			window.setSize(w,h);
		}
		window.setLocation( (screen.width - window.getWidth())/2, (screen.height - window.getHeight())/2 );
		window.setVisible(true);
	}


	/**
	 * Close the mosaic window, if one is open.
	 */
	public static void close() {
		if (window != null) {
			window.dispose();
			window = null;
			canvas = null;
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
	 * are changed, for example).
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
			return Color.black;
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
		return canvas.getRed(row, col);
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
		return canvas.getGreen(row, col);
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
		return canvas.getBlue(row, col);
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
		canvas.setColor(row,col,red,green,blue);
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
		canvas.fill(red,green,blue);
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
			canvas.repaint();
		}
	}
	
	
	/**
	 * Returns the value of the use3DEffect property.
	 * @return
	 */
	public boolean getUse3DEffect() {
		return use3DEffect;
	}


}  // end of class Mosaic