import textio.TextIO;
import java.util.ArrayList;

/**
 * Reads a list of non-zero numbers for the user, then prints
 * out the input numbers in the reverse of the order in which
 * they were entered.  there is no limit on the number of inputs.
 */
public class ReverseWithArrayList {
	
	public static void main(String[] args) {
		ArrayList<Integer> list;
		list = new ArrayList<Integer>();
		System.out.println("Enter some non-zero integers.  Enter 0 to end.");
		while (true) {
			System.out.print("? ");
			int number = TextIO.getlnInt();
			if (number == 0)
				break;
			list.add(number);
		}
		System.out.println();
		System.out.println("Your numbers in reverse are:");
		for (int i = list.size() - 1; i >= 0; i--) {
			System.out.printf("%10d%n", list.get(i));
		}
	}

}
