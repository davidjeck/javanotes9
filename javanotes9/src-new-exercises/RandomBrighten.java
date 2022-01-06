/**
 * This program opens a Mosaic window that is initially filled with black.
 * A "disturbance" moves randomly around in the window.  Each time it visits
 * a square, the green component of the color of that square is increased
 * until, after about ten visits, it has reached the maximum possible level.
 * The animation continues until the user closes the window.
 */

public class RandomBrighten {
   
   final static int ROWS = 80;        // Number of rows in the mosaic.
   final static int COLUMNS = 80;     // Number of columns in the mosaic.
   final static int SQUARE_SIZE = 5;  // Size of each square in the mosaic.
   
   static int currentRow;    // Row currently containing the disturbance.
   static int currentColumn; // Column currently containing disturbance.
   
   /**
    * The main program creates the window, fills it with random colors,
    * and then moves the disturbance in a random walk around the window
    * as long as the window is open.
    */
   public static void main(String[] args) {
      Mosaic.open( ROWS, COLUMNS, SQUARE_SIZE, SQUARE_SIZE );
      currentRow = ROWS / 2;   // start at center of window
      currentColumn = COLUMNS / 2;
      Mosaic.setUse3DEffect(false);
      while (Mosaic.isOpen()) {
         brightenSquare(currentRow, currentColumn);
         randomMove();
      }
   }  // end main
   
   /**
    * Add a bit of green to the rectangle in a given row and column.
    * Precondition:   The specified rowNum and colNum are in the valid range
    *                 of row and column numbers.
    * Postcondition:  The green component of the color of the square has
    *                 been increased by 25, except that it does not go
    *                 over its maximum possible value, 255.
    */
   static void brightenSquare(int row, int col) {
      int g = Mosaic.getGreen(row,col);
      g += 25;
      if (g > 255)
          g = 255;
      Mosaic.setColor(row,col,0,g,0);
   }
   
   /**
    * Move the disturbance.
    * Precondition:   The global variables currentRow and currentColumn
    *                 are within the legal range of row and column numbers.
    * Postcondition:  currentRow or currentColumn is changed to one of the
    *                 neighboring positions in the grid -- up, down, left, or
    *                 right from the current position.  If this moves the
    *                 position outside of the grid, then it is moved to the
    *                 opposite edge of the grid.
    */
   static void randomMove() {
      int directionNum; // Randomly set to 0, 1, 2, or 3 to choose direction.
      directionNum = (int)(4*Math.random());
      switch (directionNum) {
      case 0:  // move up 
         currentRow--;
         if (currentRow < 0)
            currentRow = ROWS - 1;
         break;
      case 1:  // move right
         currentColumn++;
         if (currentColumn >= COLUMNS)
            currentColumn = 0;
         break; 
      case 2:  // move down
         currentRow ++;
         if (currentRow >= ROWS)
            currentRow = 0;
         break;
      case 3:  // move left  
         currentColumn--;
         if (currentColumn < 0)
            currentColumn = COLUMNS - 1;
         break; 
      }
   }  // end randomMove
   
} // end class RandomBrighten