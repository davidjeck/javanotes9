import textio.TextIO;

/**
 * This program will convert measurements expressed in inches,
 * feet, yards, or miles into each of the possible units of
 * measure.  The measurement is input by the user, followed by
 * the unit of measure.  For example:  "17 feet", "1 inch", or
 * "2.73 mi".  Abbreviations in, ft, yd, and mi are accepted.
 * The program will continue to read and convert measurements
 * until the user enters an input of 0.
 */

public class LengthConverter {

	public static void main(String[] args) {

		double measurement;  // Numerical measurement, input by user.
		String units;        // The unit of measure for the input, also
		                     //    specified by the user.

		double inches, feet, yards, miles;  // Measurement expressed in
		                                    //   each possible unit of
		                                    //   measure.

		System.out.println("""
				Enter measurements in inches, feet, yards, or miles.
				For example:  1 inch    17 feet    2.73 miles
				You can use abbreviations:   in   ft  yd   mi
				I will convert your input into the other units
				of measure.
				""");

		while (true) {

			/* Get the user's input, and convert units to lower case. */

			System.out.print("Enter your measurement, or 0 to end:  ");
			measurement = TextIO.getDouble();
			if (measurement == 0)
				break;  // Terminate the while loop.
			units = TextIO.getlnWord();            
			units = units.toLowerCase();  // convert units to lower case

			/* Convert the input measurement to inches. */

			if (units.equals("inch") || units.equals("inches") 
					|| units.equals("in")) {
				inches = measurement;
			}
			else if (units.equals("foot") || units.equals("feet") 
					|| units.equals("ft")) {
				inches = measurement * 12;
			}
			else if (units.equals("yard") || units.equals("yards") 
					|| units.equals("yd")) {
				inches = measurement * 36;
			}
			else if (units.equals("mile") || units.equals("miles") 
					|| units.equals("mi")) {
				inches = measurement * 12 * 5280;
			}
			else {
				System.out.println("Sorry, but I don't understand \"" 
						+ units + "\".");
				continue;  // back to start of while loop
			}

			/* Convert measurement in inches to feet, yards, and miles. */

			feet = inches / 12;
			yards = inches / 36;
			miles = inches / (12*5280);

			/* Output measurement in terms of each unit of measure. */

			System.out.printf("""
					That's equivalent to:
					%14.5g inches
					%14.5g feet
					%14.5g yards
					%14.5g miles
					""", inches, feet, yards, miles);

			System.out.println();

		} // end while

		System.out.println();
		System.out.println("OK!  Bye for now.");

	} // end main()

} // end class LengthConverter
