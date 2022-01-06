import java.util.ArrayList;
import java.util.Optional;
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
public class CountDivisorsUsingStreamAPI {

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
    private static class Task {
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
     * The main() routine just gets the number of threads from the user and 
     * calls countDivisorsWithThreads() to do the actual work.
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ArrayList<Task> tasks = new ArrayList<>();
        int numberOfTasks = (MAX + 999) / 1000;
        for (int i = 0; i < numberOfTasks; i++) {
            int start = i*1000 + 1;
            int end = (i+1)*1000;
            if (end > MAX)
                end = MAX;
            tasks.add( new Task(start,end) );
        }
        Optional<Result> max;
        max = tasks.parallelStream()
               .map( task -> task.call() )
               .max( (r1,r2) -> r1.maxDivisorFromTask - r2.maxDivisorFromTask);
        Result bestRes = max.get();
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("\nThe largest number of divisors " + 
                "for numbers between 1 and " + MAX + 
                " is " + bestRes.maxDivisorFromTask);
        System.out.println("An integer with that many divisors is " + 
                bestRes.intWithMaxFromTask);
        System.out.println("Total elapsed time:  " + 
                (elapsedTime/1000.0) + " seconds.\n");
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
