import textio.TextIO;

/**
 * This program is a demonstration of the StringList class.
 * The user can choose to perform any of three list operations:
 * find, insert, and delete.  After each operation, the contents
 * of the list are displayed.   Note that any list item input by
 * the user is converted to lower case.
 */
public class ListDemo {


	public static void main(String[] args) {

		StringList list;    // The list of strings.

		String usersItem;   // An item typed in by the user.

		String[] elements;  // The elements in the list, obtained by
							//    calling list.getElements().  This
							//    is used to display the list contents
							//    to the user.

		boolean done;       // This will be set to true then the user
							//   wants to exit from the program.

		done = false;

		list = new StringList();  // Start with an empty list.

		while (done == false) {

			// Get and display the elements that are currently in the list.

			elements = list.getElements();
			if (elements.length == 0)
				System.out.println("\n\nThere are no elements in the list.");
			else {
				System.out.println("\n\nElements of the list:");
				for (int i = 0; i < elements.length; i++)
					System.out.println("   " + elements[i]);
			}

			// Display a menu of available operations, and get the
			// user's choice.

			System.out.println("\n\nChoose an operation on the list:");
			System.out.println("    1.  Add an item.");
			System.out.println("    2.  Delete an item.");
			System.out.println("    3.  Find an item.");
			System.out.println("    4.  Exit from this program.");
			System.out.print("Enter the number of your choice:  ");
			int menuChoice = TextIO.getlnInt();

			// Carry out the operation selected by the user.  For
			// items 1 to 3, get a string from the user and call
			// the appropriate method from the list.

			switch (menuChoice) {
			case 1:  // Insert an item.
				System.out.print("\nEnter the item to be added:  ");
				usersItem = TextIO.getln().trim().toLowerCase();
				list.insert(usersItem);
				System.out.println("OK");
				break;
			case 2:  // Delete an item.
				System.out.print("\nEnter the item to be deleted:  ");
				usersItem = TextIO.getln().trim().toLowerCase();
				if ( list.delete(usersItem) )
					System.out.println("OK");
				else
					System.out.println("That item was not found in the list.");
				break;
			case 3:  // Check whether an item occurs in the list.
				System.out.print("\nEnter an item to find:  ");
				usersItem = TextIO.getln().trim().toLowerCase();
				if ( list.find(usersItem) )
					System.out.println("Yes, that item is in the list.");
				else
					System.out.println("No, that item is not in the list.");
				break;
			case 4:  // Exit from this program.
				done = true;
				break;
			default:
				System.out.println("Illegal choice.");
				break;
			} // end switch


		} // end while

		System.out.println("\n\nExiting program.");

	} // end main()


}  // end class ListDemo

