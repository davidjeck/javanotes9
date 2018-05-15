/**
 * This program fills a mosaic with random colors.  It then enters
 * a loop in which it randomly selects one of the squares in the
 * mosaic, then randomly selects one of the four neighbors of that
 * square and converts the selected neighbor to the color of the
 * originally selected square.  The effect is to gradually build
 * up larger patches of uniform color.  The animation continues 
 * until the user closes the window.  This program depends on
 * the non-standard classes Mosaic and MosaicCanvas.
 */

public class RandomConvert {

	final static int ROWS = 40;        // Number of rows in the mosaic. 
	final static int COLUMNS = 40;     // Number of columns in the mosaic.
	final static int SQUARE_SIZE = 10; // Size of each square in the mosaic.
	final static int DELAY = 1;        // Millisecond delay after each convert.
	final static int BATCH_SIZE = 10;  // The number of squares tested/converted at a time.


	/**
	 * The main() routine opens the mosaic window, then enters into
	 * a loop in which it repeatedly converts the color of one square.
	 * The loop ends when the user closes the mosaic window.
	 */
	public static void main(String[] args) {
		Mosaic.setUse3DEffect(false);
		Mosaic.open(ROWS, COLUMNS, SQUARE_SIZE, SQUARE_SIZE);
		fillWithRandomColors();
		while (Mosaic.isOpen()) {
			for (int i = 0; i < BATCH_SIZE; i++) {
				int randomRow = (int)(ROWS * Math.random());
				int randomColumn = (int)(COLUMNS * Math.random());
				convertRandomNeighbor(randomRow, randomColumn);
			}
			Mosaic.delay(DELAY);
		}
	}


	/**
	 * Set each square in the mosaic to be a randomly selected color.
	 */
	static void fillWithRandomColors() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				int r = (int)(256*Math.random());
				int g = (int)(256*Math.random());
				int b = (int)(256*Math.random());
				Mosaic.setColor(row,col,r,g,b);
			}
		}
	}


	/**
	 * Select one of the neighbors of the square at position (row,column) in
	 * the mosaic.  Change the color at position (row, column) to match the
	 * color of the selected neighbor.   The neighbors of a square are the
	 * squares above, below, to the left, and to the right of the square.
	 * For squares on the edge of the mosaic, wrap around to the opposite
	 * edge.
	 */
	static void convertRandomNeighbor(int row, int col) {

		/* Get the color components for position (row,col) */

		int red = Mosaic.getRed(row,col);
		int green = Mosaic.getGreen(row,col);
		int blue = Mosaic.getBlue(row,col);

		/* Choose a random direction, and change the value of row
		 * or col to refer to the neighbor that lies in that direction. */

		int directionNum = (int)(4*Math.random());
		switch (directionNum) {
		case 0:    // Choose neighbor above.
			row--;  // Move row number one row up.
			if (row < 0)  // row number is outside the mosaic.
				row = ROWS - 1;  // Wrap around to bottom of the mosaic.
			break;
		case 1:    // Choose neighbor to the right.
			col++;
			if (col >= COLUMNS)
				col = 0;
			break; 
		case 2:    // Choose neighbor below.
			row++;
			if (row >= ROWS)
				row = 0;
			break;
		case 3:    // Choose neighbor to the left.
			col--;
			if (col < 0)
				col = COLUMNS - 1;
			break; 
		}

		/* Change the color of the neighbor to color of the original square. */

		Mosaic.setColor(row,col,red,green,blue);
	}

}
