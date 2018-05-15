/**
 * This program reads the user's first name and last name,
 * separated by a space.  It then prints the user's first and
 * last names separately, along with the number of characters
 * in each name.  It also prints the user's initials.  Note that
 * this program will crash if the user's input does not contain
 * a space.
 */
public class FirstNameLastName {
	
	public static void main(String[] args) {
		
		String input;     // The input line entered by the user.
		int space;        // The location of the space in the input.
		String firstName; // The first name, extracted from the input.
		String lastName;  // The last name, extracted from the input.
		
		System.out.println();
		System.out.println("Please enter your first name and last name, separated by a space.");
		System.out.print("? ");
		input = TextIO.getln();
		
		space = input.indexOf(' ');
		firstName = input.substring(0, space);
		lastName = input.substring(space+1);
		
		System.out.println("Your first name is " + firstName + ", which has "
                + firstName.length() + " characters.");
		System.out.println("Your last name is " + lastName + ", which has "
                + lastName.length() + " characters.");
		System.out.println("Your initials are " + firstName.charAt(0) + lastName.charAt(0));
		
	}

}
