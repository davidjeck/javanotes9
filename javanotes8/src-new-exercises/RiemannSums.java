/**
 * 
 */
public class RiemannSums {
	
	public static double leftSum( FunctionR2R f, double a, double b, int n ) {
		double total = 0;
		double dx = (b - a) / n;
		for (int i = 0; i < n; i++) {
			double x = a + i*dx;
			double y = f.valueAt(x);
			total += y;
		}
		return total * (b-a)/n;
	}

	public static double  rightSum( FunctionR2R f, double a, double b, int n ) {
		double total = 0;
		double dx = (b - a) / n;
		for (int i = 1; i <= n; i++) {
			double x = a + i*dx;
			double y = f.valueAt(x);
			total += y;
		}
		return total * (b-a)/n;
	}
	
	public static double trapezoid( FunctionR2R f, double a, double b, int n) {
		return (leftSum(f,a,b,n) + rightSum(f,a,b,n)) / 2.0;
	}
	
	public static void main(String[] args) {
		System.out.println(trapezoid( x -> x*x, 0, 1, 100000));
		System.out.println(leftSum( x -> Math.sin(x), 0, Math.PI, 100000));
	}

}
