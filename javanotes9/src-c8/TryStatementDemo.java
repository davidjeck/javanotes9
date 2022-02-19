import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This demo program tests a try..catch..finally statement, including
 * its resource allocation/autoclose feature.  Read the comments for
 * more information.
 */
public class TryStatementDemo {
	
	/**
	 * An object of type TestResource represents a "resource" that can
	 * be automatically closed by a try..catch statement.  For that to
	 * work, it has to implement AutoCloseable, which requires it to
	 * define a close() method.  In a try statement with resource
	 * allocation, the close() method will be called at the end of
	 * the try, as long as the allocation succeeds.
	 */
	static class TestResource implements AutoCloseable {
		static int nextID;
		int id;  // Each TestResource object has its own id.
		public TestResource() {
			    // Print a message in the constructor so we can tell
			    // when the object is created.
			nextID++;
			id = nextID;
			System.out.println("TestResource object #" +
		             id + " is being created.");
				// NOTE:  Try adding an exception here, to see how it is
				// handled by the try..catch.  Just uncomment the next line.
				// throw new RuntimeException("Error in constructor from TestResource " + id);
		}
		public void close() {
			    // Print a message when the object's close() method
			    // is called, so we can see that that happens. Note that
			    // the program never calls close().  It is called 
			    // automatically for the resources that are allocated in
			    // the try statement.
			System.out.println("TestResource object #" +
		             id + " is being closed.");
				// NOTE:  You can also try adding an exception at this point.
				// throw new RuntimeException("Error in close() from TestResource " + id);
		}
	}
	
	
	public static void main(String[] args) {
		
		System.out.println("This program demonstrates a try..catch statement with");
		System.out.println("several optional features.  Three resources are created");
		System.out.println("as part of the try, and they will be closed automatically.");
		System.out.println("The program will ask you for an integer.  Try running the");
		System.out.println("program several times with both valid and invalid responses");
		System.out.println("to see the flow of control in both the normal and in the");
		System.out.println("error case.  A lot of extra output is generated to show");
		System.out.println("what order things happen in.");
		System.out.println();
		
		System.out.println("***Ready to start the try..catch..finally.");

		    // The first three lines of the try includes three resource allocations.
		    // Each one is a variable declaration with initialization.  Assuming that
		    // The allocation succeeds, the resource will be closed automatically at
		    // the end of the try.  These resource variables can only be used in
		    // the try part of the statement; they are local to that block.

		try( Scanner in = new Scanner(System.in); 
				TestResource one = new TestResource(); 
				TestResource two = new TestResource() ) {
			
			System.out.println("***Starting the try part.");
			System.out.print("What's your favorite number? ");
			
			    // The next line will throw NoSuchElementException if the user's
			    // input is not a legal integer.
			int n = in.nextInt();
			
			System.out.println( (n+1) + " is better!");
			System.out.println("***Finishing the try part.");
		}

		catch (NoSuchElementException e) {
			    // The catch clause is executed only if a NoSuchElementException
			    // occurs.  This happens if the user's input in not a legal int.
			System.out.println("***Starting the catch part.");
			System.out.println("Sorry, that's not an integer!");
			System.out.println("***Finishing the catch part.");
		}

		finally {
			    // The finally clause is always executed, no matter what else happens.
			System.out.println("***In the finally part.");
		}
		
		System.out.println("***Done with the entire try..catch..finally.");
		
	}

}
