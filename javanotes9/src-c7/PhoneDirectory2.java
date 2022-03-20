import java.util.ArrayList;

/**
 * Implements a "phone directory" consisting of a list of
 * name/number pairs.  This version uses an ArrayList for
 * the list and a record class to define the pairs.  (This
 * class is NOT meant for any serious use.)
 */
public class PhoneDirectory2 {

	/**
	 * An object of type PhoneEntry holds one name/number pair.
	 */
	private record PhoneEntry(String name, String number) { }
	
	/**
	 * An ArrayList that holds the name/value pairs.
	 */
	private ArrayList<PhoneEntry> data = new ArrayList<PhoneEntry>();

	/**
	 * Looks for a name/number pair with a given name.  If found, the index
	 * of the pair in the data array is returned.  If no pair contains the
	 * given name, then the return value is -1.  This private method is
	 * used internally in getNumber() and putNumber().
	 */
	private int find( String name ) {
		for (int i = 0; i < data.size(); i++) {
			PhoneEntry entry = data.get(i);
			if (entry.name().equals(name))
				return i;  // The name has been found in position i.
		}
		return -1;  // The name does not exist in the array.
	}

	/**
	 * Finds the phone number, if any, for a given name.
	 * @return The phone number associated with the name; if the name does
	 *    not occur in the phone directory, then the return value is null.
	 */
	public String getNumber( String name ) {
		int position = find(name);
		if (position == -1)
			return null;   // There is no phone entry for the given name.
		else
			return data.get(position).number(); // Return the number.
	}

	/**
	 * Associates a given name with a given phone number.  If the name
	 * already exists in the phone directory, then the new number replaces
	 * the old one.  Otherwise, a new name/number pair is added.  The
	 * name and number should both be non-null.  An IllegalArgumentException
	 * is thrown if this is not the case.
	 */
	public void putNumber( String name, String number ) {
		if (name == null || number == null)
			throw new IllegalArgumentException("name and number cannot be null");
		PhoneEntry newEntry = new PhoneEntry(name,number);
		   // (Note: Since a PhoneEntry object is immutable, we can't simply
		   // change the value of the number field in the exiting entry -- we
		   // have to create an entirely new entry with the same name and
		   // the new number.)
		int i = find(name);
		if (i >= 0) 
			data.set(i, newEntry); // Replace existing entry.
		else
			data.add(newEntry); // Add a new entry.
	}

} // end class PhoneDirectory2

