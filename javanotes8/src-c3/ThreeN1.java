import textio.TextIO;

/**  
 * This program prints out a 3N+1 sequence starting from a positive 
 * integer specified by the user.  It also counts the number of 
 * terms in the sequence, and prints out that number.
 */
public class ThreeN1 {

	public static void main(String[] args) {                

		int N;       // for computing terms in the sequence
		int counter; // for counting the terms

		System.out.print("Starting point for sequence: ");
		N = TextIO.getlnInt();
		while (N <= 0) {
			System.out.print("The starting point must be positive. Please try again: ");
			N = TextIO.getlnInt();
		}
		// At this point, we know that N &gt; 0

		counter = 0;
		while (N != 1) {
			if (N % 2 == 0)
				N = N / 2;
			else
				N = 3 * N + 1;
			System.out.println(N);
			counter = counter + 1;
		}

		System.out.println();
		System.out.print("There were ");
		System.out.print(counter);
		System.out.println(" terms in the sequence.");

	}  // end of main()

}  // end of class ThreeN1
