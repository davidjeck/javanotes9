
/**
 * Reads up to 100 integers from the user, then prints them
 * in reverse order.  An input of zero marks the end of input.
 */
public class ReverseInputNumbers {

   public static void main(String[] args) {
   
     int[] numbers;  // An array for storing the input values.
     int count;      // The number of numbers saved in the array.
     int num;        // One of the numbers input by the user.
     int i;          // for-loop variable.
     
     numbers = new int[100];   // Space for 100 ints.
     count = 0;                // No numbers have been saved yet.
     
     System.out.println("Enter up to 100 positive integers; enter 0 to end.");
     
     while (true) {   // Get the numbers and put them in the array.
        System.out.print("? ");
        num = TextIO.getlnInt();
        if (num <= 0) {
              // Zero marks the end of input; we have all the numbers.
           break;
        }
        numbers[count] = num;  // Put num in position count.
        count++;  // Count the number
     }
     
     System.out.println("\nYour numbers in reverse order are:\n");
     
     for ( i = count - 1; i >= 0; i-- ) {
         System.out.println( numbers[i] );
     }
     
   } // end main();
   
}  // end class ReverseInputNumbers
