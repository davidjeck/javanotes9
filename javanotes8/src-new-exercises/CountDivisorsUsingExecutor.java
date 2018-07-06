import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This program finds the number in the range 1 to some maximum that has the 
 * largest number of divisors.  It prints that number and the number of divisors 
 * that it has.  Note that there might be several numbers that have the maximum
 * number of divisors.  Only one of them is output.
 * 
 * The program's work is divided into a large number of tasks that are executed
 * by an ExecutorService.  Each task consists of finding the maximum number of 
 * divisors among a sequence of 1000 integers.
 */
public class CountDivisorsUsingExecutor {

    /**
     * The upper limit of the range of integers that is to be tested.
     * (This must be a fairly large multiple of 1000 for the thread
     * pool load-balancing strategy to be effective.)
     */
    private final static int MAX = 100000;
   
    
    /**
     * A class to represent the result from one task.  The
     * result consists of the maximum number of divisors in
     * the range of integers assigned to that task, and the
     * integer in the range that gave the maximum number of
     * divisors.
     */
    private static class Result {
        int maxDivisorFromTask;  // Maximum number of divisors found.
        int intWithMaxFromTask;  // Which integer gave that maximum number.
        Result(int maxDivisors, int whichInt) {
            maxDivisorFromTask = maxDivisors;
            intWithMaxFromTask = whichInt;
        }
    }
    
    
   /**
     * A class to represent the task of finding the number in
     * a given range of integers that has the largest number of
     * divisors.  The range is specified in the constructor.
     * The task is executed when the call() method is 
     * called.  At the end of the call() method, a Result
     * object is created to represent the results from this
     * task, and the result object is returned as the value
     * of call().
     */
    private static class Task implements Callable<Result>{
        int min, max; // Start and end of the range of integers for this task.
        Task(int min, int max) {
            this.min = min;
            this.max = max;
        }
        public Result call() {
            int maxDivisors = 0;
            int whichInt = 0;
            for (int i = min; i < max; i++) {
                int divisors = countDivisors(i);
                if (divisors > maxDivisors) {
                    maxDivisors = divisors;
                    whichInt = i;
                }
            }
            return new Result(maxDivisors,whichInt);
        }
    }
        

    /**
     * Finds the number in the range 1 to MAX that has the largest number of
     * divisors, dividing the work into tasks that will be submitted to an
     * ExecutorService.  The Futures that are returned when the tasks are
     * submitted are placed into an ArrayList.  The results from those Futures
     * are combined to produce the final output.
     * @param numberOfThreads the number of threads to be used by the executor
     */
    private static void countDivisorsWithExecutor(int numberOfThreads) {
        
        System.out.println("\nCounting divisors using " + 
                                            numberOfThreads + " threads...");
        
        /* Create the queues and the thread pool, but don't start
         * the threads yet. */
        
        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        
        ArrayList<Future<Result>> results = new ArrayList<>();

        /* Create the tasks and add them to the executor.  Each
         * task consists of a range of 1000 integers, so the number of
         * tasks is (MAX+999)/1000.  (The "+999"  gives the correct number
         * of tasks when MAX is not an exact multiple of 1000.  The last
         * task in that case will consist of the last (MAX%1000)) ints. */
        
        int numberOfTasks = (MAX + 999) / 1000;
        for (int i = 0; i < numberOfTasks; i++) {
            int start = i*1000 + 1;
            int end = (i+1)*1000;
            if (end > MAX)
                end = MAX;
            //System.out.println(start + " " + end);  // for testing
            Future<Result> res = executor.submit( new Task(start,end) );
            results.add(res);
        }
        
        /* As the executor executes the tasks, results become available
         * in the Futures that are stored in the ArrayList.  Get the
         * results and combine them to produce the final output.
         * Note that each call to res.get() blocks, if necessary,
         * until the result is available. */

        int maxDivisorCount = 0;         // Over maximum found by any task.
        int intWithMaxDivisorCount = 0;  // Which integer gave that maximum?
        for (Future<Result> res : results) {
            try {
                Result result = res.get();
                if (result.maxDivisorFromTask > maxDivisorCount) { // new maximum.
                    maxDivisorCount = result.maxDivisorFromTask;
                    intWithMaxDivisorCount = result.intWithMaxFromTask;
                }
            }
            catch (Exception e) {
                System.out.println("An unexpected error occurred! Error:");
                System.out.println(e);
                System.exit(1);
            }
        }
        
        /* Report the results. */
        
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("\nThe largest number of divisors " + 
                "for numbers between 1 and " + MAX + " is " + maxDivisorCount);
        System.out.println("An integer with that many divisors is " + 
                intWithMaxDivisorCount);
        System.out.println("Total elapsed time:  " + 
                (elapsedTime/1000.0) + " seconds.\n");
        
        executor.shutdown(); // Needed since otherwise the threads in the
                             // ExecutorService will stop the Java Virtual
                             // Machine from shutting down normally.
        
    } // end countDivisorsWithExecutor()

    
    /**
     * The main() routine just gets the number of threads from the user and 
     * calls countDivisorsWithThreads() to do the actual work.
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int numberOfThreads = 0;
        while (numberOfThreads < 1 || numberOfThreads > 10) {
            System.out.print("How many threads do you want to use  (1 to 10) ?  ");
            numberOfThreads = in.nextInt();
            if (numberOfThreads < 1 || numberOfThreads > 10)
                System.out.println("Please enter a number from 1 to 10 !");
        }
        countDivisorsWithExecutor(numberOfThreads);
    }
    

    /**
     * Finds the number of divisors of the integer N.  Note that this method does
     * the counting in a stupid way, since it tests every integer in the range
     * 1 to N to see whether it evenly divides N.
     */
    private static int countDivisors(int N) {
        int count = 0;
        for (int i = 1; i <= N ; i++) {
            if ( N % i == 0 )
                count ++;
        }
        return count;
    }

} // end CountDivisorsUsingThreadPool
