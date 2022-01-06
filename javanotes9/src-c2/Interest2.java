import textio.TextIO;

/**
 * This class implements a simple program that will compute
 * the amount of interest that is earned on an investment over
 * a period of one year.  The initial amount of the investment
 * and the interest rate are input by the user.  The value of
 * the investment at the end of the year is output.  The
 * rate must be input as a decimal, not a percentage (for
 * example, 0.05 rather than 5).
 * 
 * This program requires TextIO for input. TextIO is not a 
 * standard part of Java, so it must be made available when
 * the program is compiled or run.
 */
public class Interest2 {

	public static void main(String[] args) {

		double principal;  // The value of the investment.
		double rate;       // The annual interest rate.
		double interest;   // The interest earned during the year.

		System.out.print("Enter the initial investment: ");
		principal = TextIO.getlnDouble();

		System.out.print("Enter the annual interest rate (as a decimal): ");
		rate = TextIO.getlnDouble();

		interest = principal * rate;       // Compute this year's interest.
		principal = principal + interest;  // Add it to principal.

		System.out.printf("The amount of interest is $%1.2f%n", interest);
		System.out.printf("The value after one year is $%1.2f%n", principal);

	} // end of main()

} // end of class Interest2
