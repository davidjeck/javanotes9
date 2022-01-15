/**
 * This program demonstrates the use of enum types.
 * The enum types Day and Month are defined in this file.
 */
public class EnumDemo {

	// Define two enum types.  (In Java 17, these definitions could also be
	// inside main(), but earlier versions of Java require them to be outside
	// main(), or in a separate .java file.)

	enum Day { SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY }

	enum Month { JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC }

	public static void main(String[] args) {

		Day tgif;     // Declare a variable of type Day.
		Month libra;  // Declare a variable of type Month.

		tgif = Day.FRIDAY;    // Assign a value of type Day to tgif.
		libra = Month.OCT;    // Assign a value of type Month to libra.

		System.out.print("My sign is libra, since I was born in ");
		System.out.println(libra);   // Output value will be:  OCT
		System.out.print("That's the ");
		System.out.print( libra.ordinal() );
		System.out.println("-th month of the year.");
		System.out.println("   (Counting from 0, of course!)");

		System.out.print("Isn't it nice to get to ");
		System.out.println(tgif);   // Output value will be:  FRIDAY

		System.out.println( tgif + " is the " + tgif.ordinal() 
		                                + "-th day of the week.");
	}

}
