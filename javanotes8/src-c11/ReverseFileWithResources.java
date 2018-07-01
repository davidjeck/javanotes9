import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Reads numbers from a file named data.dat and writes them to a file
 * named result.dat in reverse order.  The input file should contain
 * exactly one real number per line.
 *    This version of the ReverseFile program uses try-with-resource
 * statements to safely open and close the Scanner that is used to
 * read the data and the PrintWriter that is used to
 * write the data.
 */
public class ReverseFileWithResources {

	public static void main(String[] args) {

		ArrayList<Double> numbers;  // An ArrayList for holding the data.

		numbers = new ArrayList<Double>();
		
		// Read the data from the input file.

		try( Scanner data = new Scanner(new File("data.dat")) ) {
			// Read numbers, adding them to the ArrayList.
			while ( data.hasNextDouble() ) {  // Read until end-of-file.
				double inputNumber = data.nextDouble();
				numbers.add( inputNumber );
			}
		}
		catch (FileNotFoundException e) {
			    // Can be caused if file does not exist or can't be read.
			System.out.println("Can't open input file data.dat!");
			System.out.println("Error: " + e);
		    return;  // Return from main(), since an error has occurred.
		}
		
		// Write the data to the output file.
		
		try( PrintWriter result = new PrintWriter("result.dat") ) {
			// Output the numbers in reverse order.
			for (int i = numbers.size()-1; i >= 0; i--)
				result.println(numbers.get(i));
			result.flush();  // Make sure data is actually sent to the file.
			if (result.checkError())
				System.out.println("Some error occurred while writing the file.");
			else
				System.out.println("Done!");
		}
		catch (FileNotFoundException e) {
		        // Can only be caused by the PrintWriter constructor
			System.out.println("Can't open file result.dat!");
			System.out.println("Error: " + e);
		}

	}  // end of main()

} // end class ReverseFileWithTextReader
