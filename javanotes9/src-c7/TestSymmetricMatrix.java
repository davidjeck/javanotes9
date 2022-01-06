import textio.TextIO;

/**
 * A small program to test the class SymmetricMatrix,
 * which represents symmetric matrices of double values.
 */
public class TestSymmetricMatrix {
	
	public static void main(String[] args) {

		System.out.println("This program will create then print out symmetric matrices.");
	    System.out.print("Enter the size of the matrix: ");
	    int n = TextIO.getlnInt();  // Assume answer is a reasonably small positive integer.
	    
	    SymmetricMatrix M = new SymmetricMatrix(n);
	    
	    // Create an "identity" matrix by setting all the diagonal elements to 1.
	    for (int i = 0; i < n; i++) {
	    	M.set(i,i,1);
	    }
	    System.out.println();
	    System.out.println("Here is the identity matrix of size " + n + ":");
	    System.out.println();
	    for (int row = 0; row < n; row++) {
	    	System.out.print("    ");
	    	for (int col = 0; col < n; col++) {
	    		System.out.printf("%6.0f", M.get(row,col));
	    	}
	    	System.out.println();
	    }
	    
	    // Fill M with random values; only elements on and below the diagonal are set.
	    // When the matrix is printed, all elements have a value.
	    for (int i = 0; i < n; i++) {
	    	for (int j = 0; j <= i; j++)
	    		M.set(i,j,10*Math.random());
	    }
	    System.out.println();
	    System.out.println("Here is a matrix filled with random values");
	    System.out.println();
	    for (int row = 0; row < n; row++) {
	    	System.out.print("    ");
	    	for (int col = 0; col < n; col++) {
	    		System.out.printf("%6.2f", M.get(row,col));
	    	}
	    	System.out.println();
	    }
	    
	    // Fill every entry with its row number.
	    for (int i = 0; i < n; i++) {
	    	for (int j = 0; j < n; j++)
	    		M.set(i,j,i);
	    }
	    System.out.println();
	    System.out.println("Here is what we get when every entry is filled with its row number.");
	    System.out.println("(Entries above the diagonal actually show the column number.)");
	    System.out.println();
	    for (int row = 0; row < n; row++) {
	    	System.out.print("    ");
	    	for (int col = 0; col < n; col++) {
	    		System.out.printf("%6.0f", M.get(row,col));
	    	}
	    	System.out.println();
	    }
    
	    
	}
}
