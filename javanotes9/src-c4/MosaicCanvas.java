import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.application.Platform;

/**
 *  A MosaicCanvas object represents a grid containing rows
 *  and columns of colored rectangles.  There can be "grouting"
 *  between the rectangles.  (The grouting is just drawn as a 
 *  one-pixel outline around each rectangle.)  The rectangles can
 *  optionally be drawn as raised 3D-style rectangles; this effect
 *  works much better with some colors than with others.  Methods are 
 *  provided for getting and setting the colors of the rectangles.
 */
public class MosaicCanvas extends Canvas {


	//------------------ private instance variables --------------------


	private int rows;       // The number of rows of rectangles in the grid.
	private int columns;    // The number of columns of rectangles in the grid.
	private Color defaultColor;   // Color used for any rectangle whose color
	                              //    has not been set explicitly.  This
	                              //    can never be null.
	private Color groutingColor;  // The color for "grouting" between 
	                              //    rectangles.  If this is null, no
	                              //    grouting is drawn.
	private boolean alwaysDrawGrouting;  // Grouting is drawn around default-
	                                     //    colored rects if this is true.
	private boolean use3D = true;   // If true, 3D rects are used; if false, 
	                                //       flat rects are used to draw the rectangles.
	private boolean autopaint = true;  // If true, then when a square's color is set, 
	                                   //     repaint is called automatically.
	private Color[][] grid; // An array that contains the rectangles' colors.
	                        //   If a null occurs in this array, the rectangle
	                        //   is drawn in the default color, and "grouting"
	                        //   will be drawn around that rectangle only if
	                        //   alwaysDrawGrouting is true.  Also, the 
	                        //   rectangle is drawn as a flat rectangle rather
	                        //   than as a 3D rectangle.
	private GraphicsContext g; // The graphics context for drawing on this canvas.


	//------------------------ constructors -----------------------------


	/**
	 *  Construct a MosaicCanvas with 42 rows and 42 columns of rectangles,
	 *  and with preferred rectangle height and width both set to 16.
	 */
	public MosaicCanvas() {
		this(42,42);
	}

	/**
	 *  Construct a MosaicCanvas with specified numbers of rows and columns of rectangles,
	 *  and with preferred rectangle height and width both set to 16.
	 */
	public MosaicCanvas(int rows, int columns) {
		this(rows,columns,16,16);
	}



	/**
	 *  Construct a MosaicCanvas with the specified number of rows and
	 *  columns of rectangles, and with a specified preferred size for the  
	 *  rectangle.  The default rectangle color is black, the
	 *  grouting color is gray, and alwaysDrawGrouting is set to false.
	 *  If a non-null border color is specified, then a border of that color is added
	 *  to the panel, and its width is taken into account in the computation of the preferred
	 *  size of the panel.
	 *  @param rows the mosaic will have this many rows of rectangles.  This must be a positive number.
	 *  @param columns the mosaic will have this many columns of rectangles.  This must be a positive number.
	 *  @param preferredBlockWidth the preferred width of the mosaic will be set to this value times the number of
	 *  columns.  The actual width is set by the component that contains the mosaic, and so might not be
	 *  equal to the preferred width.  Size is measured in pixels.  The value should not be less than about 5,
	 *  and any smaller value will be increased to 5.
	 *  @param preferredBlockHeight the preferred height of the mosaic will be set to this value times the number of
	 *  rows.  The actual height is set by the component that contains the mosaic, and so might not be
	 *  equal to the preferred height.  Size is measured in pixels.  The value should not be less than about 5,
	 *  and any smaller value will be increased to 5.
	 */
	public MosaicCanvas(int rows, int columns, int preferredBlockWidth, int preferredBlockHeight) {
		
		this.rows = rows;
		this.columns = columns;
		if (rows <= 0 || columns <= 0)
			throw new IllegalArgumentException("Rows and Columns must be greater than zero.");
		preferredBlockHeight = Math.max( preferredBlockHeight, 5);
		preferredBlockWidth = Math.max( preferredBlockWidth, 5);
		grid = new Color[rows][columns];
		defaultColor = Color.BLACK;
		groutingColor = Color.GRAY;
		alwaysDrawGrouting = false;
		setWidth(preferredBlockWidth*columns);
		setHeight(preferredBlockHeight*rows);
		g = getGraphicsContext2D();
	}


	//--------- methods for getting and setting grid properties ----------


	/**
	 *  Set the defaultColor.  If c is null, the color will be set to black.
	 *  When a mosaic is first created, the defaultColor is black.
	 *  This is the color that is used for rectangles whose color
	 *  value is null.  Such rectangles are always drawn as flat rather
	 *  than 3D rectangles.
	 */
	public void setDefaultColor(Color c) {
		if (c == null)
			c = Color.BLACK;
		if (! c.equals(defaultColor)) {
			defaultColor = c;
			forceRedraw();
		}
	}


	/**
	 *  Return the defaultColor, which cannot be null.
	 */
	public Color getDefaultColor() {
		return defaultColor;
	}


	/**
	 *  Set the color of the "grouting" that is drawn between rectangles.
	 *  If the value is null, no grouting is drawn and the rectangles
	 *  fill the entire grid.   When a mosaic is first created, the
	 *  groutingColor is gray.
	 */
	public void setGroutingColor(Color c) {
		if (c == null || ! c.equals(groutingColor)) {
			groutingColor = c;
			forceRedraw();
		}
	}


	/**
	 *  Get the current groutingColor, which can be null.
	 */
	public Color getGroutingColor(Color c) {
		return groutingColor;
	}


	/**
	 *  Set the value of alwaysDrawGrouting.  If this is false, then
	 *  no grouting is drawn around rectangles whose color value is null.
	 *  When a mosaic is first created, the value is false.
	 */
	public void setAlwaysDrawGrouting(boolean always) {
		if (alwaysDrawGrouting != always) {
			alwaysDrawGrouting = always;
			forceRedraw();
		}
	}
	
	
	/**
	 * Get the value of the use3D property.
	 */
	public boolean getUse3D() {
		return use3D;
	}
	
	
	/**
	 * Set the use3D property.  When this property is true, the rectangles are
	 * drawn as "3D" rects, which are supposed appear to be raised.  When use3D
	 * is false, they are drawn as regular "flat" rects.  Note that flat rects
	 * are always used for background squares that have not been assigned a color.
	 * The default value of use3D is true;
	 */
	public void setUse3D(boolean use3D) {
		this.use3D = use3D;
	}


	/**
	 *  Get the value of the alwaysDrawGrouting property.
	 */   
	public boolean getAlwaysDrawGrouting() {
		return alwaysDrawGrouting; 
	}


	/**
	 *  Set the number of rows and columns in the grid.  If the value of
	 *  the preserveData parameter is false, then the color values of all
	 *  the rectangles in the new grid are set to null.  If it is true,
	 *  then as much color data as will fit is copied from the old grid.
	 *  The number of rows and number of columns must be positive.
	 */
	public void setGridSize(int rows, int columns, boolean preserveData) {
		if (rows <= 0 && columns <= 0) 
			throw new IllegalArgumentException("Rows and columns must be positive.");
		Color[][] newGrid = new Color[rows][columns];
		if (preserveData) {
			int rowMax = Math.min(rows,this.rows);
			int colMax = Math.min(columns,this.columns);
			for (int r = 0; r < rowMax; r++)
				for (int c = 0; c < colMax; c++)
					newGrid[r][c] = grid[r][c];
		}
		grid = newGrid;
		this.rows = rows;
		this.columns = columns;
		forceRedraw();
	}


	/**
	 *  Return the number of rows of rectangles in the grid.
	 */
	public int getRowCount() {
		return rows;
	}


	/**
	 *  Return the number of columns of rectangles in the grid.
	 */
	public int getColumnCount() {
		return columns;
	}   


	//------------------ other useful public methods ---------------------



	/**
	 *  Get the color which has been set for the rectangle in the specified
	 *  row and column of the grid.  This value can be null if no
	 *  color has been set for that rectangle.  (Such rectangles are
	 *  actually displayed using the defaultColor.)  If the specified
	 *  rectangle is outside the grid, then null is returned.
	 */
	public Color getColor(int row, int col) {
		if (row >=0 && row < rows && col >= 0 && col < columns)
			return grid[row][col];
		else
			return null;
	}


	/**
	 *  Return the red component of color of the rectangle in the
	 *  specified row and column, as a double value in the range
	 *  0.0 to 1.0.  If the specified rectangle lies outside 
	 *  the grid or if no color has been specified for the rectangle,
	 *  then the red component of the defaultColor is returned.
	 */
	public double getRed(int row, int col) {
		if (row >=0 && row < rows && col >= 0 && col < columns && grid[row][col] != null)
			return grid[row][col].getRed();
		else
			return defaultColor.getRed();
	}


	/**
	 *  Return the green component of color of the rectangle in the
	 *  specified row and column, as a double value in the range
	 *  0.0 to 1.0.  If the specified rectangle lies outside 
	 *  the grid or if no color has been specified for the rectangle,
	 *  then the green component of the defaultColor is returned.
	 */
	public double getGreen(int row, int col) {
		if (row >=0 && row < rows && col >= 0 && col < columns && grid[row][col] != null)
			return grid[row][col].getGreen();
		else
			return defaultColor.getGreen();
	}


	/**
	 *  Return the blue component of color of the rectangle in the
	 *  specified row and column, as a double value in the range
	 *  0.0 to 1.0.  If the specified rectangle lies outside 
	 *  the grid or if no color has been specified for the rectangle,
	 *  then the blue component of the defaultColor is returned.
	 */
	public double getBlue(int row, int col) {
		if (row >=0 && row < rows && col >= 0 && col < columns && grid[row][col] != null)
			return grid[row][col].getBlue();
		else
			return defaultColor.getBlue();
	}


	/**
	 *  Set the color of the rectangle in the specified row and column.
	 *  If the rectangle lies outside the grid, this is simply ignored.
	 *  The color can be null.  Rectangles for which the color is null
	 *  will be displayed in the defaultColor, and they will always be shown
	 *  as flat rather than 3D rects.
	 */
	public void setColor(int row, int col, Color c) {
		if (row >=0 && row < rows && col >= 0 && col < columns) {
			grid[row][col] = c;
			drawSquare(row,col);
		}
	}


	/**
	 *  Set the color of the rectangle in the specified row and column,
	 *  where the RGB color components are given as double values in 
	 *  the range 0.0 to 1.0. Values are clamped to lie in that range.
	 *  If the rectangle lies outside the grid, this is simply ignored.
	 */
	public void setColor(int row, int col, double red, double green, double blue) {
		if (row >=0 && row < rows && col >= 0 && col < columns) {
			red = (red < 0)? 0 : ( (red > 1)? 1 : red );
			green = (green < 0)? 0 : ( (green > 1)? 1 : green );
			blue = (blue < 0)? 0 : ( (blue > 1)? 1 : blue );
			grid[row][col] = Color.color(red,green,blue);
			drawSquare(row,col);
		}
	}


	/**
	 *  Set the color of the rectangle in the specified row and column.
	 *  The color is specified by giving hue, saturation, and brightness
	 *  components of the color.  The hue should be in the range 0.0 to 360.0,
	 *  and the saturation and brightness should be in the range 0.0. to 1.0.
	 *  Their values are clamped to lie in those ranges.
	 *  If the rectangle lies outside the grid, this is simply ignored.
	 */
	public void setHSBColor(int row, int col, 
			double hue, double saturation, double brightness) {
		if (row >=0 && row < rows && col >= 0 && col < columns) {
			hue = (hue < 0)? 0 : ( (hue > 360)? 360 : hue );
			saturation = (saturation < 0)? 0 : ( (saturation > 1)? 1 : saturation );
			brightness = (brightness < 0)? 0 : ( (brightness > 1)? 1 : brightness );
			grid[row][col] = Color.hsb(hue,saturation,brightness);
			drawSquare(row,col);
		}
	}


	/**
	 *  Set all rectangles of the grid to have the specified color.
	 *  The color can be null.  In that case, the rectangles are
	 *  drawn as flat rather than 3D rects in the defaultColor.
	 */
	public void fill(Color c) {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				grid[i][j] = c;
		forceRedraw();      
	}


	/**
	 *  Set all rectangles of the grid to have the color specified by
	 *  the given red, green, and blue components.  These components 
	 *  should be double values in the range 0 to 1 and are clamped to lie
	 *  in that range.
	 */
	public void fill(double red, double green, double blue) {
		red = (red < 0)? 0 : ( (red > 1)? 1 : red );
		green = (green < 0)? 0 : ( (green > 1)? 1 : green );
		blue = (blue < 0)? 0 : ( (blue > 1)? 1 : blue );
		fill(Color.color(red,green,blue));
	}


	/**
	 *  Fill all the rectangles with randomly selected colors.
	 */
	public void fillRandomly() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				grid[i][j] = Color.color(Math.random(),Math.random(),Math.random());
			}
		}
		forceRedraw();
	}


	/**
	 *   Clear the mosaic by setting all the colors to null.
	 */
	public void clear() {
		fill(null);
	}


	/**
	 * Returns the current value of the autopaint property.
	 * @see #setAutopaint(boolean)
	 */
	public boolean getAutopaint() {
		return autopaint;
	}

	
	/**
	 * Sets the value of the autopaint property.  When this property is true,
	 * then every call to one of the setColor methods automatically results in
	 * repainting that square on the screen.  When it is desired to avoid this
	 * immediate repaint -- for example, during a long sequence of setColors
	 * that will all show up at once -- then the value of the autopaint property
	 * can be set to false.  When the value is false, color changes are recorded
	 * in the data for the mosaic but are not made on the screen.  When the
	 * autopaint property is reset to true, the changes are applied and the
	 * entire mosaic is repainted.  The default value of this property is
	 * true.  
	 * <p>Note that clearing or filling the mosaic will cause an immediate 
	 * screen update, even if autopaint is false.
	 */
	public void setAutopaint(boolean autopaint) {
		if (this.autopaint == autopaint)
			return;
		this.autopaint = autopaint;
		if (autopaint) 
			forceRedraw();
	}

	/**
	 * This method can be called to force redrawing of the entire mosaic.  The only
	 * time it might be necessary for users of this class to call this method is
	 * while the autopaint property is set to false, and it is desired to show
	 * all the changes that have been made to the mosaic, without resetting
	 * the autopaint property to true.
	 * @see #setAutopaint(boolean)
	 */
	final public void forceRedraw() {
		drawAllSquares();
	}

	/**
	 *   Return an object that contains the color data that
	 *   is needed to redraw the mosaic.  This includes the 
	 *   defaultColor, the groutingColor, the number of rows and
	 *   columns, the color of each rectangle, and the
	 *   value of alwaysDrawGrouting.
	 */
	public Object copyColorData() {
		    // Note:  This is a fudge.  The data about defaultColor,
		    // groutingColor, alwaysDrawGrouting, and use3D are added to the
		    // last row of the grid.  boolean values are represented by a
		    // null for false and a non-null value for true.
		Color[][] copy = new Color[rows][columns];
		    // Replace the last row with a longer row.
		copy[rows-1] = new Color[columns+4];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				copy[r][c] = grid[r][c];
		copy[rows-1][columns] = defaultColor;
		copy[rows-1][columns+1] = groutingColor;
		copy[rows-1][columns+2] = (alwaysDrawGrouting? Color.BLACK : null);
		copy[rows-1][columns+3] = (use3D? Color.BLACK : null);
		return copy;
	}


	/**
	 *  The parameter to this method should be an Object that
	 *  was created by the copyColorData() method.  This method
	 *  will restore the data in the object to the grid.  This
	 *  can change the size of the grid, the colors in the grid,
	 *  the defaultColor, the groutingColor, and the value of
	 *  alwaysDrawGrouting.  If the object is of the proper
	 *  form, then the return value is true.  If not, the return
	 *  value is false and no changes are made to the current data.
	 */
	public boolean restoreColorData(Object data) {
		if (data == null || !(data instanceof Color[][]))
			return false;
		Color[][] newGrid = (Color[][])data;
		int newRows = newGrid.length;
		if (newRows == 0 || newGrid[0].length == 0)
			return false;
		int newColumns = newGrid[0].length;
		for (int r = 1; r < newRows-1; r++)
			if (newGrid[r].length != newColumns)
				return false;
		if (newGrid[newRows-1].length != newColumns+4)
			return false;
		if (newGrid[newRows-1][newColumns] == null)
			return false;
		rows = newRows;
		columns = newColumns;
		grid = new Color[rows][columns];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				grid[i][j] = newGrid[i][j];
		defaultColor = newGrid[newRows-1][newColumns];
		groutingColor = newGrid[newRows-1][newColumns+1];
		alwaysDrawGrouting = newGrid[newRows-1][newColumns+2] != null;
		use3D = newGrid[newRows-1][newColumns+3] != null;
		forceRedraw();
		return true;
	}

	/**
	 * Given an x-coordinate of a pixel in the MosaicCanvas, this method returns
	 * the row number of the mosaic rectangle that contains that pixel.  If
	 * the x-coordinate does not lie within the bounds of the mosaic, the return
	 * value is -1 or is equal to the number of columns, depending on whether
	 * x is to the left or to the right of the mosaic.
	 */
	public int xCoordToColumnNumber(double x) {
		if (x < 0)
			return -1;
		double colWidth = getWidth() / columns;
		int col = (int)( x / colWidth);
		if (col >= columns)
			return columns;
		else
			return col;
	}

	/**
	 * Given a y-coordinate of a pixel in the MosaicCanvas, this method returns
	 * the column number of the mosaic rectangle that contains that pixel.  If
	 * the y-coordinate does not lie within the bounds of the mosaic, the return
	 * value is -1  or is equal to the number of rows, depending on whether
	 * y is above or below the mosaic.
	 */
	public int yCoordToRowNumber(double y) {
		if (y < 0)
			return -1;
		double rowHeight = getHeight() / rows;
		int row = (int)(y / rowHeight);
		if (row >= rows)
			return rows;
		else
			return row;
	}

	// private implementation section -- the only part that actually draws squares
	
	private void drawSquare(int row, int col) {
		if ( autopaint ) {
			if (Platform.isFxApplicationThread()) {
				drawOneSquare(row,col);
			}
			else {
				Platform.runLater( () -> drawOneSquare(row,col) );
			}
			try { // to avoid overwhelming the application thread with draw operations...
				Thread.sleep(1);
			}
			catch (InterruptedException e) {
			}
		}
	}
	
	private void drawAllSquares() {
		if (Platform.isFxApplicationThread()) {
			for (int r = 0; r < rows; r++)
				for (int c = 0; c < columns; c++)
					drawOneSquare(r,c);
		}
		else {
			Platform.runLater( () -> {
			for (int r = 0; r < rows; r++)
				for (int c = 0; c < columns; c++)
					drawOneSquare(r,c);
			} );
		}
		try { // to avoid overwhelming the application thread with draw operations...
			Thread.sleep(1);
		}
		catch (InterruptedException e) {
		}
	}
	
	private void drawOneSquare(int row, int col) {
		   // only called from two previous methods
		double rowHeight = getHeight() / rows;
		double colWidth = getWidth() / columns;
		int y = (int)Math.round(rowHeight*row);
		int h = Math.max(1, (int)Math.round(rowHeight*(row+1)) - y);
		int x = (int)Math.round(colWidth*col);
		int w = Math.max(1, (int)Math.round(colWidth*(col+1)) - x);
		Color c = grid[row][col];
		g.setFill( (c == null)? defaultColor : c );
		if (groutingColor == null || (c == null && !alwaysDrawGrouting)) {
			if (!use3D || c == null)
				g.fillRect(x,y,w,h);
			else
				fill3DRect(c,x,y,w,h);
		}
		else {
			if (!use3D || c == null)
				g.fillRect(x+1,y+1,w-2,h-2);
			else
				fill3DRect(c,x+1,y+1,w-2,h-2);
			g.setStroke(groutingColor);
			g.strokeRect(x+0.5,y+0.5,w-1,h-1);
		}
	}

	private void fill3DRect(Color color, int x, int y, int width, int height) {
		double h = color.getHue();
		double b = color.getBrightness();
		double s = color.getSaturation();
		if (b > 0.8) {
			b = 0.8;
			g.setFill(Color.hsb(h,s,b));
		}
		else if (b < 0.2) {
			b = 0.2;
			g.setFill(Color.hsb(h,s,b));
		}
		g.fillRect(x,y,width,height);
		g.setStroke(Color.hsb(h,s,b+0.2));
		g.strokeLine(x+0.5,y+0.5,x+width-0.5,y+0.5);
		g.strokeLine(x+0.5,y+0.5,x+0.5,y+height-0.5);
		g.setStroke(Color.hsb(h,s,b-0.2));
		g.strokeLine(x+width-0.5,y+1.5,x+width-0.5,y+height-0.5);
		g.strokeLine(x+1.5,y+height-0.5,x+width-0.5,y+height-0.5);
	}
	


} // end class MosaicCanvas
