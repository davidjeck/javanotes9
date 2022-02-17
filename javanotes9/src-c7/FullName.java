
/**
 * A record class for representing names.  A name consists of a
 * first name and a last name.  However, to accommodate people
 * who use only one name, the last name can be null.
 */
public record FullName(String firstName, String lastName) {

	/**
	 * Canonical constructor takes two parameters representing
	 * the first name and the last name.  The last name can be 
	 * null, but if the first name is null an IllegalArgumentException
	 * will be thrown.
	 */
	public FullName {
		if (firstName == null) {
			throw new IllegalArgumentException("First name can't be null.");
		}
	}
	
	/**
	 * Constructor for creating a FullName for a person who uses
	 * only one name.  The parameter represents that name, which 
	 * becomes the firstName field of the FullName.  The lastName
	 * field is null.
	 */
	public FullName(String name) {
		this(name,null);
	}
	
	/**
	 * For a person who uses only one name, returns that name.
	 * Otherwise, returns a string containing the first name
	 * and the last name, separated by a space.
	 */
	public String toString() {
		if (lastName == null)
			return firstName;
		else
			return firstName + " " + lastName;
	}
	
	/**
	 * Returns true if this FullName is the name of a person
	 * who uses only one name.
	 */
	public boolean oneNameOnly() {
		return lastName == null;
	}
	
} // end record FullName

