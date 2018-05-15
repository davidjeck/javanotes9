
public class TransposeMatrix {
	
	/**
	 * Creates transpose of a given 2D array of integers.
	 * @param matrix  the original array
	 * @return the transpose of matrix
	 */
	public static int[][] computeTranspose( int[][] matrix ) {
		int[][] transpose;
		int R = matrix.length;     // the number of rows in matrix
		int C = matrix[0].length;  // the number of columns in matrix
		transpose = new int[C][R];
		for ( int i = 0; i < C; i++) { // goes through ROWS of the transpose
			for ( int j = 0; j < R; j++ ) { // goes through COLUMNS of the transpose
				transpose[i][j] = matrix[j][i];
			}
		}
		return transpose;
	}
	
	/**
	 * Prints out the items of a 2D array of ints in rows and columns,
	 * with 6 spaces in each column.
	 */
	public static void print( int[][] array ) {
		    // Note that this uses a for-each loop where the loop
		    // control variable is of type int[].  This works because
		    // a 2D array is actually a 1D array of 1D arrays, where
		    // each 1D array is one of the rows of the 2D array.
		for ( int[] row : array ) { 
			    // print out one row from the array
			System.out.print("   ");
			for ( int item : row ) {
				   // print with 1 blank space and 5 spaces for the integer;
				   // if an integer needs more than 5 spaces, the columns will
				   // be messed up, but all the integers will still be readable.
				System.out.printf(" %5d", item); 
			}
			System.out.println();
		}
	}
	
	/**
	 * Test the subroutines by creating two arrays and printing them and
	 * their transposes.  The arrays are constructed so that it is easy
	 * to see that the transposes are correct.
	 */
	public static void main(String[] args) {
		int[][] orig = {
				{ 1, 2, 3, 4, 5, 6 },
				{ 10, 20, 30, 40, 50, 60 },
				{ 100, 200, 300, 400, 500, 600 }
		};
		System.out.println("Original matrix:");
		System.out.println();
		print(orig);
		System.out.println();
		System.out.println("The transpose:");
		System.out.println();
		print( computeTranspose(orig) );
		System.out.println();
		System.out.println();

		orig = new int[][] {
				{1, 1, 1, 1, 1, 1, 1},
				{2, 2, 2, 2, 2, 2, 2},
				{3, 3, 3, 3, 3, 3, 3},
				{4, 4, 4, 4, 4, 4, 4},
				{5, 5, 5, 5, 5, 5, 5},
				{6, 6, 6, 6, 6, 6, 6},
				{7, 7, 7, 7, 7, 7, 7},
		};
		System.out.println("Original matrix:");
		System.out.println();
		print(orig);
		System.out.println();
		System.out.println("The transpose:");
		System.out.println();
		print( computeTranspose(orig) );
		System.out.println();

	}

}
