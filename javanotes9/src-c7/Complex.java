/**
 * A record type for representing complex numbers, where
 * a complex number consists of two real numbers called its
 * real and imaginary parts.  The class includes methods for
 * doing arithmetic with complex numbers.
 */
public record Complex(double re, double im)  {
	
	// Some named constants for common complex numbers.

	public final static Complex ONE = new Complex(1,0);
	public final static Complex ZERO = new Complex(0,0);
	public final static Complex I = new Complex(0,1);

	/**
	 * This constructor creates a complex number with a given
	 * real part and with imaginary part zero.
	 */
	public Complex(double re) {
		this(re,0);
	}
	
	/**
	 * Creates string representations of complex number such
	 * as:  3.0 + I*5.0,  -I*3.14,   2.7 - I*8.6,   3.14
	 */
	public String toString() {
		if (im == 0)
			return String.valueOf(re);
		else if (re == 0) {
			if (im < 0)
				return "-I*" + (-im);
			else
				return "I*" + im;
		}
		else if (im < 0)
			return re + " - " + "I*" + (-im);
		else
			return re + " + " + "I*" + im;
	}

	// Some methods for doing arithmetic on two complex numbers
	
	public Complex plus(Complex c) {
		return new Complex(re + c.re, im + c.im);
	}
	public Complex minus(Complex c) {
		return new Complex(re - c.re, im - c.im);
	}
	public Complex times(Complex c) {
		return new Complex(re*c.re - im*c.im,
				re*c.im + im*c.re);
	}
	public Complex dividedBy(Complex c) {
		double denom = c.re*c.re + c.im*c.im;
		double real = (re*c.re + im*c.im)/denom;
		double imaginary = (im*c.re - re*c.im)/denom;
		return new Complex(real,imaginary);
	}
		
} // end record Complex

