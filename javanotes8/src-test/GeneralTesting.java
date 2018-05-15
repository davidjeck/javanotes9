import java.io.Closeable;
import java.io.IOException;
import java.util.*;

public class GeneralTesting {
	
	public static <T> int countOccurrences(T[] list, T itemToCount) {
		   int count = 0;
		   if (itemToCount == null) {
		      for ( T listItem : list )
		         if (listItem == null)
		            count++;
		   }
		   else {
		      for ( T listItem : list )
		         if (itemToCount.equals(listItem))
		            count++;
		   }
		   return count;
		}   
	
	
	public static void main(String[] args) {
		System.out.println( countOccurrences(new String[] {"fred","barney","fred"}, "fred"));
		System.out.println( countOccurrences(new Integer[] { 17, 18, 19, 20, 17}, 17));
	}
	

}
