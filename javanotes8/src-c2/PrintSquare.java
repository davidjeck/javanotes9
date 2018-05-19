import textio.TextIO;

/**
 * A program that reads an integer that is typed in by the
 * user and computes and prints the square of that integer.
 */
public class PrintSquare {

	public static void main(String[] args) {

		int userInput;  // The number input by the user.
		int square;     // The userInput, multiplied by itself.

		System.out.print("Please type a number: ");
		userInput = TextIO.getlnInt();
		square = userInput * userInput;

		System.out.println();
		System.out.println("The number that you entered was " + userInput);
		System.out.println("The square of that number is " + square);
		System.out.println();

	} // end of main()

} //end of class PrintSquare
