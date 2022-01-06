import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;

/**
 * This program compares execution times for an algorithm implemented
 * three different ways:  Using a basic for loop, using Java's stream
 * API with a sequential stream, and using Java's stream API with
 * a parallel stream.  (The problem that is solved is to compute
 * the value of a Riemann sum, but that's not the point of the program.
 * (Results will depend on the machine where the program is run.
 * The for loop is expected to be faster than a sequential stream.
 * On a multi-core machine, the parallel stream should be faster than
 * the for loop when the number of subintervals in the sum is large,
 * but exactly how large will vary.)
 */
public class RiemannSumStreamExperiment {
	
	/**
	 * Use a basic for loop to compute a Riemann sum.
	 * @param f  The function that is to be summed.
	 * @param a  The left endpoint of the interval over which f is summed.
	 * @param b  The right endpoint.
	 * @param n  The number of subdivisions of the interval.
	 * @return  the value computed for the Riemann sum.
	 */
	private static double riemannSumWithForLoop( 
			DoubleUnaryOperator f, double a, double b, int n) {
		double sum = 0;
		double dx = (b - a) / n;
		for (int i = 0; i < n; i++) {
			sum = sum + f.applyAsDouble(a + i*dx);
		}
		return sum * dx;
	}
	
	/**
	 * Use a sequential stream to compute a Riemann sum.
	 */
	private static double riemannSumWithStream( 
			DoubleUnaryOperator f, double a, double b, int n) {
		double dx = (b - a) / n;
		double sum = IntStream.range(0,n)
				        .mapToDouble( i -> f.applyAsDouble(a + i*dx) )
				        .sum();
		return sum * dx;
	}

	/**
	 * Use a parallel stream to compute a Riemann sum.
	 */
	private static double riemannSumWithParallelStream( 
			DoubleUnaryOperator f, double a, double b, int n) {
		double dx = (b - a) / n;
		double sum = IntStream.range(0,n).parallel()
				        .mapToDouble( i -> f.applyAsDouble(a + i*dx) )
				        .sum();
		return sum * dx;
	}
	
	/**
	 * Compute the same Riemann sum using each the three methods, and
	 * report the time that each method takes.
	 */
	private static void timedExperiment( 
			DoubleUnaryOperator f, double a, double b, int n ) {
		long start, end;
		double ans;
		System.out.printf("For n = %,d:%n", n);
		start = System.nanoTime();
		ans = riemannSumWithForLoop(f,a,b,n);
		end = System.nanoTime();
		System.out.printf("  Got %1.15g using a for loop in          %,d nanoseconds.%n", ans, end - start);
		start = System.nanoTime();
		ans = riemannSumWithStream(f,a,b,n);
		end = System.nanoTime();
		System.out.printf("  Got %1.15g using a sequential stream in %,d nanoseconds.%n", ans, end - start);
		start = System.nanoTime();
		ans = riemannSumWithParallelStream(f,a,b,n);
		end = System.nanoTime();
		System.out.printf("  Got %1.15g using a parallel stream in   %,d nanoseconds.%n", ans, end - start);
		System.out.println();
	}
	
	/**
	 * The main program runs experiments to compare the execution time for
	 * the three different methods of computing a Riemann sum.  The comparison
	 * is done for n subdivisions of the interval, for several values of n.
	 * The function that is summed is f(x) = sin(x), which takes a relatively
	 * long time to evaluate.
	 */
	public static void main(String[] args) {
		
		/* First, run each of the three methods to give the Java just-in-time
		 * compiler a chance to optimize the code.  Without this "priming" of
		 * the compiler, the times for the first experiment would likely be 
		 * longer as the compiler does its work.  (Try commenting out these lines
		 * to see the effect.)  You might also try running the program with the
		 * just-in-time compiler turned off, using the command
		 *                      java -Xint RiemannSumStreamExperiment */
		
		riemannSumWithForLoop(Math::sin, 0, Math.PI, 10000);
		riemannSumWithStream(Math::sin, 0, Math.PI, 10000);
		riemannSumWithParallelStream(Math::sin, 0, Math.PI, 10000);
		
		/* Run the experiment for n from one thousand to 100 million */
		
		System.out.println("Running the experiments for f(x) = sin(x):);");
		System.out.println();
		for (int n = 1000; n <= 100000000; n = 10*n) {
			timedExperiment( x -> Math.sin(x), 0, Math.PI, n );
		}
	}

} // end RiemannSumStreamExperiment
