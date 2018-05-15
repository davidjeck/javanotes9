/**
 * This program reads a line of text entered by the user.
 * It prints a list of the letters that occur in the text,
 * and it reports how many different letters were found.
 */

public class ListLetters {
   
   public static void main(String[] args) {
   
      String str;  // Line of text entered by the user.
      int count;   // Number of different letters found in str.
      char letter; // A letter of the alphabet.
      
      System.out.println("Please type in a line of text.");
      str = TextIO.getln();
      
      str = str.toUpperCase();
      
      count = 0;
      System.out.println("Your input contains the following letters:");
      System.out.println();
      System.out.print("   ");
      for ( letter = 'A'; letter <= 'Z'; letter++ ) {
          int i;  // Position of a character in str.
          for ( i = 0; i < str.length(); i++ ) {
              if ( letter == str.charAt(i) ) {
                  System.out.print(letter);
                  System.out.print(' ');
                  count++;
                  break;
              }
          }
      }
      
      System.out.println();
      System.out.println();
      System.out.println("There were " + count + " different letters.");
   
   } // end main()
   
} // end class ListLetters
