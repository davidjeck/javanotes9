import textio.TextIO;

/**
 *  Computes the average of a sequence of real numbers entered by the
 *  user.  The numbers must be entered one per line.  A blank input
 *  line marks the end of the input.
 */
public class ComputeAverage2 {

	public static void main(String[] args) {
		String str;     // The user's input.
		double number;  // The input converted into a number.
		double total;   // The total of all numbers entered.
		double avg;     // The average of the numbers.
		int count;      // The number of numbers entered.
		total = 0;
		count = 0;
		System.out.println("Enter your numbers, press return to end.");
		while (true) {
			System.out.print("? ");
			str = TextIO.getln();
			if (str.equals("")) {
				break; // Exit the loop, since the input line was blank.
			}
			try {
				number = Double.parseDouble(str);
				// If an error occurs, the next 2 lines are skipped!
				total = total + number;
				count = count + 1;
			}
			catch (NumberFormatException e) {
				System.out.println("Not a legal number!  Try again.");
			}
		}
		avg = total/count;
		System.out.printf("The average of %d numbers is %1.6g%n", count, avg);
	}

}