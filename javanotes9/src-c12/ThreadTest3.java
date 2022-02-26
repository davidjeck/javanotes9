import textio.TextIO;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This program counts the number of prime integers between 3000001 and 6000000.
 * The work is divided among one to six threads.  The number of threads is
 * chosen by the user.
 * 
 * This is a slightly modified version of ThreadTest2.  It uses an AtomicInteger
 * object to add up the total number of primes found by the various threads.
 * ThreadTest2 used an ordinary int variable and used synchronization to handle
 * the race condition when a value is added to the total.
 */
public class ThreadTest3 {

	/**
	 * The starting point for the range of integers that are tested for primality.
	 * The range is from (start+1) to (2*start).  Note the value of start is chosen
	 * to be divisible by 2, 3, 4, 5, and 6 to make it easy to divide up the range
	 * among the threads.
	 */
	private static final int START = 6000000;

	/**
	 * The total number of primes found.  Each thread counts the number of primes in
	 * a different range of integers.  After it finishes counting, it adds its count
	 * to the total.
	 */
	private static AtomicInteger total = new AtomicInteger();

	/**
	 * A Thread belonging to this class will count primes in a specified range
	 * of integers.  The range is from min to max, inclusive, where min and max
	 * are given as parameters to the constructor.  After counting, the thread
	 * outputs a message about the number of primes that it has found, and it
	 * adds its count to the overall total by using the addAndGet() method
	 * in an AtomicInteger.
	 */
	private static class CountPrimesThread extends Thread {
		int count = 0;
		int min, max;
		public CountPrimesThread(int min, int max) {
			this.min = min;
			this.max = max;
		}
		public void run() {
			count = countPrimes(min,max);
			System.out.println("There are " + count + 
					" primes between " + min + " and " + max);
			int currentTotal = total.addAndGet(count);
			    // NOTE THAT A RACE CONDITION STILL OCCURS RIGHT HERE.
			    // The value of total might have increased again before
			    // currentTotal is output.  However, using an AtomicInteger
			    // does ensure that the final total is correct.
			System.out.println("Number of primes found so far: " + currentTotal);
		}
	}

	/**
	 * Counts the primes in the range from (START+1) to (2*START), using a specified number
	 * of threads.  The total elapsed time is printed.
	 * @param numberOfThreads
	 */
	private static void countPrimesWithThreads(int numberOfThreads) {
		int increment = START/numberOfThreads;
		System.out.println("\nCounting primes between " + (START+1) + " and " 
				+ (2*START) + " using " + numberOfThreads + " threads...\n");
		long startTime = System.currentTimeMillis();
		CountPrimesThread[] worker = new CountPrimesThread[numberOfThreads];
		for (int i = 0; i < numberOfThreads; i++)
			worker[i] = new CountPrimesThread( START+i*increment+1, START+(i+1)*increment );
		for (int i = 0; i < numberOfThreads; i++)
			worker[i].start();
		for (int i = 0; i < numberOfThreads; i++) {
			while (worker[i].isAlive()) {
				try {
					worker[i].join();
				}
				catch (InterruptedException e) {
				}
			}
		}
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("\nThe number of primes is " + total + ".");
		System.out.println("\nTotal elapsed time:  " + (elapsedTime/1000.0) + " seconds.\n");
	}

	/**
	 * Gets the number of threads from the user and counts primes using that many threads.
	 */
	public static void main(String[] args) {
		int processors = Runtime.getRuntime().availableProcessors();
		if (processors == 1)
			System.out.println("Your computer has only 1 available processor.\n");
		else
			System.out.println("Your computer has " + processors + " available processors.\n");
		int numberOfThreads = 0;
		while (numberOfThreads < 1 || numberOfThreads > 6) {
			System.out.print("How many threads do you want to use  (from 1 to 6) ?  ");
			numberOfThreads = TextIO.getlnInt();
			if (numberOfThreads < 1 || numberOfThreads > 6)
				System.out.println("Please enter 1, 2, 3, 4, 5, or 6 !");
		}
		countPrimesWithThreads(numberOfThreads);
	}

	/**
	 * Count the primes between min and max, inclusive.
	 */
	private static int countPrimes(int min, int max) {
		int count = 0;
		for (int i = min; i <= max; i++)
			if (isPrime(i))
				count++;
		return count;
	}

	/**
	 * Test whether x is a prime number.
	 * x is assumed to be greater than 1.
	 */
	private static boolean isPrime(int x) {
		int top = (int)Math.sqrt(x);
		for (int i = 2; i <= top; i++)
			if ( x % i == 0 )
				return false;
		return true;
	}

}
