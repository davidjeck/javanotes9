import textio.TextIO;

/**
 * This program reads a hexadecimal number input by the user and prints the 
 * base-10 equivalent.  If the input contains characters that are not 
 * hexadecimal numbers, then an error message is printed.
 */

public class Hex2Dec {
 
    public static void main(String[] args) {
       String hex;  // Input from user, containing a hexadecimal number.
       long dec;    // Decimal (base-10) equivalent of hexadecimal number.
       int i;       // A position in hex, from 0 to hex.length()-1.
       System.out.print("Enter a hexadecimal number: ");
       hex = TextIO.getlnWord();
       dec = 0;
       for ( i = 0; i < hex.length(); i++ ) {
          int digit = hexValue( hex.charAt(i) );
          if (digit == -1) {
              System.out.println("Error:  Input is not a hexadecimal number.");
              return;  // Ends the main() routine.
          }
          dec = 16*dec + digit;
       }
       System.out.println("Base-10 value:  " + dec);
    }  // end main
    
    /**
     * Returns the hexadecimal value of a given character, or -1 if it is not
     * a valid hexadecimal digit.
     * @param ch the character that is to be converted into a hexadecimal digit
     * @return the hexadecimal value of ch, or -1 if ch is not 
     *     a legal hexadecimal digit
     */
    public static int hexValue(char ch) {
        switch (ch) {
           case '0' -> { return 0; }
           case '1' -> { return 1; }
           case '2' -> { return 2; }
           case '3' -> { return 3; }
           case '4' -> { return 4; }
           case '5' -> { return 5; }
           case '6' -> { return 6; }
           case '7' -> { return 7; }
           case '8' -> { return 8; }
           case '9' -> { return 9; }
           case 'A', 'a' -> { return 10; } // Note: Handle both upper and lower case.
           case 'B', 'b' -> { return 11; }
           case 'C', 'c' -> { return 12; }
           case 'D', 'd' -> { return 13; }
           case 'E', 'e' -> { return 14; }
           case 'F', 'f' -> { return 15; }
           default  -> { return -1; }
        }
     }  // end hexValue


}