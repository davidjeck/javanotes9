
/**
 * Represents symmetric n-by-n matrices of real numbers.
 */
public class SymmetricMatrix {
	
	private double[][] matrix;  // A triangular matrix to hold the data.
	
	/**
	 * Creates an n-by-n symmetric matrix in which all entries are 0.
	 */
	public SymmetricMatrix(int n) {
		matrix = new double[n][];
		for (int i = 0; i < n; i++)
			matrix[i] = new double[i+1];
	}
	
	/**
	 * Returns the matrix entry at position (row,col).  (If row &lt; col,
	 * the value is actually stored at position (col,row).)
	 */
	public double get( int row, int col ) {
		if (row >= col)
			return matrix[row][col];
		else
			return matrix[col][row];
	}
	
	/**
	 * Sets the value of the matrix entry at (row,col).  (If row &lt; col,
	 * the value is actually stored at position (col,row).)
	 */
	public void set( int row, int col, double value ) {
		if (row >= col)
			matrix[row][col] = value;
		else
			matrix[col][row] = value;
	}
	
	/**
	 * Returns the number of rows and columns in the matrix.
	 */
	public int size() {
		return matrix.length;  // The size is the number of rows.
	}

} // end class SymmetricMatrix
