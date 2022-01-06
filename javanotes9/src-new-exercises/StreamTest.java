import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This program tests the stream API by doing various computations.
 */
public class StreamTest {

	/**
	 * Data for one student about a score on a test.
	 */
	private static class ScoreInfo {
		String firstName;
		String lastName;
		int score;
		ScoreInfo( String lName, String fName, int s ) {
			firstName = fName;
			lastName = lName;
			score = s;
		}
	}
	
	/**
	 * A test array of StudentInfo that will be used to create streams.
	 */
	private static ScoreInfo[] scoreData = new ScoreInfo[] {
			new ScoreInfo("Smith","John",70),
			new ScoreInfo("Doe","Mary",85),
			new ScoreInfo("Page","Alice",82),
			new ScoreInfo("Cooper","Jill",97),
			new ScoreInfo("Flintstone","Fred",66),
			new ScoreInfo("Rubble","Barney",80),
			new ScoreInfo("Smith","Judy",48),
			new ScoreInfo("Dean","James",90),
			new ScoreInfo("Russ","Joe",55),
			new ScoreInfo("Wolfe","Bill",73),
			new ScoreInfo("Dart","Mary",54),
			new ScoreInfo("Rogers","Chris",78),
			new ScoreInfo("Toole","Pat",51),
			new ScoreInfo("Khan","Omar",93),
			new ScoreInfo("Smith","Ann",95)
	};
		
	
	public static void main(String[] args) {
		
		/* The number of students can be computed simply by applying
		 * the .count() operation to a stream created from the array. */
		
		long count = Arrays.stream(scoreData).parallel().count();
		System.out.println("Number of students: " + count);
		
		/* To get the average, use the .sum() operation, and divide by count. */
		
	    int total = Arrays.stream(scoreData).parallel()
	    		              .mapToInt( s -> s.score )
	    		              .sum();
	    System.out.printf("Average grade:  %1.2f%n", (double)total/count );
	    
	    /* To get the number of students who got an A, first filter the
	     * stream letting through only students who satisfy the
	     * predicate  s -> s.score >= 90.  This predicate returns true
	     * only for students whose score is greater than or equal to 90.
	     * The number is then computed by applying .count() to the 
	     * resulting stream. */
	    
		long countA = Arrays.stream(scoreData).parallel()
	               .filter( s -> s.score >= 90 )
	               .count();
		System.out.println("Number who got an A: " + countA);
	    System.out.println();
	    
	    /* To get a list of names of students who failed, first filter
	     * the stream to let through only ScoreInfo objects with score
	     * less than 70.  Since we want the names of students, we need
	     * to convert the stream of ScoreInfo objects into a stream
	     * of strings by applying a mapping operation.  Then applying
	     * .collect(Collectors.toList()) produces the List that we need. */

	    List<String> failing = Arrays.stream(scoreData)
	    		                          .filter( s -> (s.score < 70) )
	    		                          .map( s -> (s.firstName + " " + s.lastName) )
	    		                          .collect( Collectors.toList() );
	    
	    /* Now we need to print the list, which can easily be done with
	     * a for-each loop.  However, it can also be done using .forEach()
	     * operation on a stream.  In this case, the String Consumer in the
	     * .forEach() operation is given as the method reference 
	     * System.out::println.  That is, it is the println() method in the
	     * object System.out.  This method reference is the same as writing
	     * the lambda expression  s -> System.out.println(s).  */
	    
	    System.out.println("Failing students: ");
	    failing.stream().forEach( System.out::println );
	    System.out.println();
	    
	    /* To print the data ordered by last name, apply the .sorted() operation
	     * to sort the values in the stream by last name, then use .forEach()
	     * to output the information from each ScoreInfo object in the stream. 
	     * The .sorted() operation takes a parameter that is a Comparator for
	     * comparing two values from the stream.  In this case, we just
	     * compare the lastNames from two ScoreInfo objects, using the standard
	     * compareTo() method for Strings. */
	    
	    System.out.println("Data sorted by last name:");
	    Arrays.stream(scoreData)
	         .sorted( (s1,s2) -> s1.lastName.compareTo(s2.lastName) )
	         .forEach( s -> System.out.printf("  %s, %s: %d%n", s.lastName, s.firstName, s.score) );
	    System.out.println();
	    
	    /* The data can be output ordered by score in the same way, using a
	     * different comparator in the .sorted() operation. */
	    
	    System.out.println("Data sorted by score:");
	    Arrays.stream(scoreData)
	         .sorted( (s1,s2) -> s1.score - s2.score )
	         .forEach( s -> System.out.printf("  %s, %s: %d%n", s.lastName, s.firstName, s.score) );
	    
	}
	
} // end StreamTest
