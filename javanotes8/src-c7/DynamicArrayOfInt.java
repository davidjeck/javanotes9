import java.util.Arrays;

/**
 * Represents a list of int values that can grow and shrink.
 */
public class DynamicArrayOfInt {
	
	private int[] items = new int[8];  // partially full array holding the ints
	private int itemCt;
	
	/**
	 * Return the item at a given index in the array.  
	 * Throws ArrayIndexOutOfBoundsException if the index is not valid.
	 */
	public int get( int index ) {
		if ( index < 0 || index >= itemCt )
			throw new ArrayIndexOutOfBoundsException("Illegal index, " + index);
		return items[index];
	}
	
	/**
	 * Set the value of the array element at a given index. 
	 * Throws ArrayIndexOutOfBoundsException if the index is not valid.
	 */
	public void set( int index, int item ) {
		if ( index < 0 || index >= itemCt )
			throw new ArrayIndexOutOfBoundsException("Illegal index, " + index);
		items[index] = item;
	}
	
	/**
	 * Returns the number of items currently in the array.
	 */
	public int size() {
		return itemCt;
	}
	
	/**
	 * Adds a new item to the end of the array.  The size increases by one.
	 */
	public void add(int item) {
		if (itemCt == items.length)
			items = Arrays.copyOf( items, 2*items.length );
		items[itemCt] = item;
		itemCt++;
	}
	
	/**
	 * Removes the item at a given index in the array.  The size of the array
	 * decreases by one.  Items following the removed item are  moved down
	 * one space in the array.
	 * Throws ArrayIndexOutOfBoundsException if the index is not valid.
	 */
	public void remove(int index) {
		if ( index < 0 || index >= itemCt )
			throw new ArrayIndexOutOfBoundsException("Illegal index, " + index);
		for (int j = index+1; j < itemCt; j++)
			items[j-1] = items[j];
		itemCt--;
	}
	
}
