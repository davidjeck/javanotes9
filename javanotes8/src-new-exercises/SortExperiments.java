import java.util.Arrays;

public class SortExperiments {
	
	final static int SIZE = 100000; // The length of arrays that will be sorted.
	
	/**
	 * Creates a random string.  The length of the string is between 5 and 25,
	 * and it is made up of randomly selected uppercase letters.
	 */
	private static String randomString() {
		int length = 5 + (int)(21*Math.random());
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char ch = (char)('A' + (int)(26*Math.random()));
			str.append(ch);
		}
		return str.toString();
	}
	
	/**
	 * Creates an array of random real numbers.  The items in the array
	 * are random numbers in the range 0.0 to 1.0.
	 * @param count The length of the array that is created.
	 */
	private static double[] randomNumbers(int count) {
		double[] numbers = new double[count];
		for (int i = 0; i < count; i++)
			numbers[i] = Math.random();
		return numbers;
	}
	
	/**
	 * Creates an array of random strings. The items in the
	 * array are created by calling the function randomString();
	 * @param count the size of the array that is created
	 */
	private static String[] randomStrings(int count) {
		String[] strings = new String[count];
		for (int i = 0; i < count; i++)
			strings[i] = randomString();
		return strings;
	}
	
	/**
	 * Sort an array of real numbers using the selection sort algorithm.
	 */
	private static void selectionSort(double[] numbers) {
		for (int top = numbers.length-1; top > 0; top-- ) {
			int maxloc = 0;
			for (int i = 1; i <= top; i++) {
				if (numbers[i] > numbers[maxloc])
					maxloc = i;
			}
			double temp = numbers[top];
			numbers[top] = numbers[maxloc];
			numbers[maxloc] = temp;
		}
	}
			
	/**
	 * Sort an array of strings using the selection sort algorithm.
	 */
	private static void selectionSort(String[] numbers) {
		for (int top = numbers.length-1; top > 0; top-- ) {
			int maxloc = 0;
			for (int i = 1; i <= top; i++) {
				if (numbers[i].compareTo(numbers[maxloc]) > 0)
					maxloc = i;
			}
			String temp = numbers[top];
			numbers[top] = numbers[maxloc];
			numbers[maxloc] = temp;
		}
	}
			
	public static void main(String[] args) {
		
		long startTime;  // time when a sort begin.
		long endTime;    // time when a sort ends.
		
		double[] numberList1;  // An array of random numbers.
		double[] numberList2;  // A copy of numberList1.
		
		String[] stringList1;  // An array of random strings.
		String[] stringList2;  // A copy of stringList1.
		
		/* Make sure the selection sort methods are correct.  The outputs
		   should be correctly sorted. */
		
		System.out.println("First, test that selection sort works on doubles.");
		System.out.println("The 10 output numbers should be in increasing order.");
		numberList1 = randomNumbers(10);
		selectionSort(numberList1);
		for (double n : numberList1)
			System.out.println( "   " + n );
		System.out.println();
		
		System.out.println("Next, test that selection sort works on strings.");
		System.out.println("The 10 output strings should be in alphabetical order.");
		System.out.println("(Also tests that random strings are made correctly.");
		stringList1 = randomStrings(10);
		selectionSort(stringList1);
		for (String str : stringList1)
			System.out.println( "   " + str );
		System.out.println();
		
		System.out.println();
		System.out.println("Times for sorting arrays of size " + SIZE + ":");
		System.out.println();
		
		/* Create the arrays. */
		
		numberList1 = randomNumbers(SIZE);
		numberList2 = Arrays.copyOf(numberList1, SIZE);
		stringList1 = randomStrings(SIZE);
		stringList2 = Arrays.copyOf(stringList1, SIZE);
		
		/* do the sorts and output the times */
		
		startTime = System.currentTimeMillis();
		selectionSort(numberList1);
		endTime = System.currentTimeMillis();
		System.out.printf("Milliseconds to sort %d numbers with selectionSort: %d",
								SIZE, endTime-startTime);
		System.out.println();
		
		startTime = System.currentTimeMillis();
		Arrays.sort(numberList2);
		endTime = System.currentTimeMillis();
		System.out.printf("Milliseconds to sort %d numbers with Arrays.sort(): %d",
								SIZE, endTime-startTime);
		System.out.println();
		
		startTime = System.currentTimeMillis();
		selectionSort(stringList1);
		endTime = System.currentTimeMillis();
		System.out.printf("Milliseconds to sort %d strings with selectionSort: %d",
								SIZE, endTime-startTime);
		System.out.println();
		
		startTime = System.currentTimeMillis();
		Arrays.sort(stringList2);
		endTime = System.currentTimeMillis();
		System.out.printf("Milliseconds to sort %d strings with Arrays.sort(): %d",
								SIZE, endTime-startTime);
		System.out.println();
		System.out.println();
		
		
	}

}
