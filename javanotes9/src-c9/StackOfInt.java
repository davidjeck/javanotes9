
/**
 *  An object of type StackOfInt is a stack of integerss, with the 
 *  standard stack operations push(int N), pop(), and isEmpty().
 *  Internally, the stack is implemented as a linked list.
 */
public class StackOfInt {


	/**
	 * An object of type Node holds one of the items on the stack.
	 */
	private record Node (
		int item,       // One of the items in the list
		Node next       // Pointer to the node that holds the next item.
	) { }


	private Node top;  // Pointer to the Node that is at the top of the stack.
					   //    If top == null, then the stack is empty.

	/**
	 * Add N to the top of the stack.
	 */
	public void push( int N ) {
		Node newTop = new Node( N, top );
		top = newTop;
	}

	/**
	 * Remove the top item from the stack, and return it.
	 * @return the item that was removed from the top of the stack
	 * @throws IllegalStateException if the stack is empty when method is called.
	 */
	public int pop() {
		if ( top == null )
			throw new IllegalStateException();
		int topItem = top.item;
		top = top.next;
		return topItem;
	}


	/**
	 * Returns true if the stack is empty.  Returns false
	 * if there are one or more items on the stack.
	 */
	public boolean isEmpty() { 
		return top == null;
	}


} // end class StackOfInt

