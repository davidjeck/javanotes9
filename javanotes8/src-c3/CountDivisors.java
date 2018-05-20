import textio.TextIO;

/**
 * This program reads a positive integer from the user.
 * It counts how many divisors that number has, and
 * then it prints the result.
 * 
 * (Note: This program works for any integer in the range
 * 1 to 2147483646, but it crashes with a division-by-zero
 * when run with input 2147483647, the largest value of type
 * int.  This happens because 2147483647 + 1 is -2147483648,
 * the smallest value of type int.   So, after processing
 * testDivisor = 2147483647, testDivisor becomes -2147483648,
 * and the loop continues.  Eventually, testDivisor will be 
 * zero, and the expression  N % testDivisor is trying to
 * divide N by zero.)
 */

public class CountDivisors {

	public static void main(String[] args) {

		int N;  // A positive integer entered by the user.
		        // Divisors of this number will be counted.

		int testDivisor;  // A number between 1 and N that is a
		                  // possible divisor of N.

		int divisorCount;  // Number of divisors of N that have been found.

		int numberTested;  // Used to count how many possible divisors
		                   // of N have been tested.  When the number
		                   // reaches 10000000, a period is output and
		                   // the value of numberTested is reset to zero.

		/* Get a positive integer from the user. */

		while (true) {
			System.out.print("Enter a positive integer: ");
			N = TextIO.getlnInt();
			if (N > 0)
				break;
			System.out.println("That number is not positive.  Please try again.");
		}

		/* Count the divisors, printing a "." after every 10000000 tests. */

		divisorCount = 0;
		numberTested = 0;

		for (testDivisor = 1; testDivisor <= N; testDivisor++) {
			if ( N % testDivisor == 0 )
				divisorCount++;
			numberTested++;
			if (numberTested == 10000000) {
				System.out.print('.');
				numberTested = 0;
			}
		}

		/* Display the result. */

		System.out.println();
		System.out.println("The number of divisors of " + N
				+ " is " + divisorCount);

	} // end main()

} // end class CountDivisors
