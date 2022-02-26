import textio.TextIO;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import java.util.ArrayList;

/**
 * This program counts the number of prime integers between 6000001 and 12000000.
 * The work is divided among one to six threads.  The problem is divided into
 * subtasks which are executed by a thread pool with one thread for each
 * available processor.  The number of subtasks is selected by the user.
 * 
 * This version of the program uses a ExecutorService to manage a thread pool
 * that executes subtasks.  The subtasks are objects of type CountPrimesTask,
 * which implements Callable<Integer>.  The value returned by the task's 
 * call() method is the number of primes that were found between a specified
 * minimum and maximum value.
 */
public class ThreadTest4 {

	/**
	 * The starting point for the range of integers that are tested for primality.
	 * The range is from (START+1) to (2*START). 
	 */
	private static final int START = 6000000;

	/**
	 * An object belonging to this class will count primes in a specified range
	 * of integers.  The range is from min to max, inclusive, where min and max
	 * are given as parameters to the constructor.  The counting is done in
	 * the call() method, which returns the number of primes that were found.
	 */
	private static class CountPrimesTask implements Callable<Integer> {
		int min, max;
		public CountPrimesTask(int min, int max) {
			this.min = min;
			this.max = max;
		}
		public Integer call() {
			int count = countPrimes(min,max);
			return count;
		}
	}

	/**
	 * Counts the primes in the range from (START+1) to (2*START), using an ExecutorService
	 * thread pool.  The total elapsed time is printed.  The computation is broken up
	 * into numberOfTasks subtasks, each represented by an object of type CountPrimesTask.
	 * The tasks are submitted to an ExecutorService for execution.
	 * 
	 * When a Callable<T> is submitted to the executor, it returns a
	 * Future<T> representing the result of the task, which will only
	 * be available at a future time.  The Future's get() method
	 * can be used to retrieve the task's output, when it is available.
	 * get() will block until the computation has been completed.
	 */
	private static void countPrimesWithExecutor(int numberOfTasks) {
		
		System.out.println("\nCounting primes between " + (START+1) + " and " 
				+ (2*START) + " using " + numberOfTasks + " tasks...\n");
		long startTime = System.currentTimeMillis();
		
		double increment = (double)START/numberOfTasks;  // size of a subtask
		
		/* Create a thread pool to execute the subtasks, with one thread per processor. */
		
		int processors = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(processors);
		
		/* An ArrayList used to store the Futures that are created when the tasks
		 * are submitted to the ExecutorService. */
		
		ArrayList<Future<Integer>> results = new ArrayList<>();
		
		/* Create the subtasks, add them to the executor, and save the Futures. */
		
		int min = START+1;  // The start of the range of integers for one subtask.
		int max;            // The end of the range of integers for one subtask.
		for (int i = 0; i < numberOfTasks; i++) {
			
			max = (int)(START+1 + (i+1)*increment);
			if (i == numberOfTasks-1) {
				max = 2*START;
			}
			// System.out.println("(min,max) = " + min + "," + max);  // for testing
			
			CountPrimesTask oneTask = new CountPrimesTask(min, max);
			Future<Integer> oneResult = executor.submit( oneTask );
			results.add(oneResult);  // Save the Future representing the (future) result.
			min = max + 1;
		}
		
		/* Executor has to be shut down, or its existence will stop the Java Virtual
		 * Machine from exiting.  (Threads in the executor are not daemon threads.) */
		
		executor.shutdown();
		
		/* Add up the results from all of the subtasks.  Results are obtained from the
		 * Futures by calling their get() methods.  The for loop will not complete
		 * until all tasks have completed and returned their output. */
		
		int total = 0;
		for ( Future<Integer> res : results) {
			try {
				total += res.get();  // Waits for task to complete!
			} catch (Exception e) {
				   // Should not occur in this program.  An exception can
				   // be thrown if the task was cancelled, if an exception
				   // occurred while the task was computing, or if the
				   // thread that is waiting on get() is interrupted.
				System.out.println("Error occurred while computing: " + e);
			}
		}
		
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("\nThe number of primes is " + total + ".");
		System.out.println("\nTotal elapsed time:  " + (elapsedTime/1000.0) + " seconds.\n");
	}

	
	/**
	 * Gets the number of tasks from the user and counts primes using that many tasks.
	 */
	public static void main(String[] args) {
		int processors = Runtime.getRuntime().availableProcessors();
		if (processors == 1)
			System.out.println("Your computer has only 1 available processor.\n");
		else
			System.out.println("Your computer has " + processors + " available processors.\n");
		System.out.println("This program breaks up the computation into a number of tasks.");
		System.out.println("For load balancing, the number of tasks should be at least");
		System.out.println("several times the number of processors.  (Try 100 tasks.)");
		System.out.println();
		int numberOfTasks = 0;
		while (numberOfTasks < 1 || numberOfTasks > 1000) {
			System.out.print("How many tasks do you want to use  (from 1 to 1000) ?  ");
			numberOfTasks = TextIO.getlnInt();
			if (numberOfTasks < 1 || numberOfTasks > 1000)
				System.out.println("Please enter a number in the range 1 to 1000 !");
		}
		countPrimesWithExecutor(numberOfTasks);
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
