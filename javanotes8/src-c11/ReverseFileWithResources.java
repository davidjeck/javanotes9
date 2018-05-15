import java.io.*;
import java.util.ArrayList;

/**
 * Reads numbers from a file named data.dat and writes them to a file
 * named result.dat in reverse order.  The input file should contain
 * exactly one real number per line.
 */
public class ReverseFileWithResources {

	public static void main(String[] args) {

		ArrayList<Double> numbers;  // An ArrayList for holding the data.

		numbers = new ArrayList<Double>();
		
		// Read the data from the input file.

		try( TextReader data = new TextReader(new FileReader("data.dat")) ) {
			// Read numbers, adding them to the ArrayList.
			while ( data.eof() == false ) {  // Read until end-of-file.
				double inputNumber = data.getlnDouble();
				numbers.add( inputNumber );
			}
		}
		catch (FileNotFoundException e) {
			    // Can only be caused by the TextReader constructor
			System.out.println("Can't open input file data.dat!");
			System.out.println("Error: " + e);
		    return;  // Return from main(), since an error has occurred.
		}
		catch (IOException e) {
			    // Can occur when the TextReader tries to read a number.
			System.out.println("Error while reading from file: " + e);
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
