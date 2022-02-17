
/**
 * This class tests the record classes that are defined
 * in FullName.java and in Complex.java.  
 */
public class RecordDemo {

	public static void main(String[] args) {
		
		// To test the FullName record class, create objects using
		// the canonical constructor that has two parameters and
		// the additional one-parameter constructor.  Then test
		// the toString() method by printing both objects.
		
		FullName cartoonCharacter = new FullName("Fred", "Flintstone");
		FullName singer = new FullName("Cher"); // A person with just one name.
		
		System.out.println("You do know " + cartoonCharacter
				                  + " and " + singer + ", don't you??");
		System.out.println();
		
		// Print the named constants from the Complex record class, and
		// print some objects created using the two Complex constructors.
		
		System.out.println("Complex number 0:      " + Complex.ZERO);
		System.out.println("Complex number 1:      " + Complex.ONE);
		System.out.println("Complex number i:      " + Complex.I);
		System.out.println("Complex number 3.14:   " + new Complex(3.14));
		System.out.println("Complex number -2i:    " + new Complex(0,-2));
		System.out.println("Complex number 1+i:    " + new Complex(1,1));
		System.out.println("Complex number 42-17i: " + new Complex(42,-17));
		System.out.println();
		
		// Create some complex numbers and do some arithmetic with them.
		// Note that x is equal to A + B + C.
		
		Complex A,B,C,x;
		A = new Complex(1,1);
		B = new Complex(3,-7);
		C = new Complex(0,2);
		x = new Complex(4,-4);
		
		System.out.println("Testing complex arithmetic (and the equals() method).");
		System.out.println();
		System.out.println("A + B + C: " + (A.plus(B).plus(C)) );
		if ( x.equals(A.plus(B).plus(C)) )
			System.out.println("   This answer is correct.");
		else
			System.out.println("   This answer is not correct.");
		System.out.println();
		System.out.println( "A+B shoudd be 10-4i.  It is computed as " + A.times(B) );
		System.out.println( "A-B should be -2+8i.  Is it computed as " + A.minus(B) );
		System.out.println( "1/C should be -0.5i.  It is computed as " + Complex.ONE.dividedBy(C) );
		System.out.println( "B/A should be -2-5i.  It is computed as " + B.dividedBy(A) );
		if ( A.times(B).equals(B.times(A)) )
			System.out.println("Commutativity of multiplication works!");
		else
			System.out.println("Multiplication is broken!");
		System.out.println();
		
		System.out.println("A*x*x+B*x+C is computed as: " +
						(A.times(x).times(x)).plus(B.times(x)).plus(C) );
	}

}
