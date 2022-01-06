
/* This program demonstrates the new switch statement syntax
 * that was introduced in Java 14.  (The old syntax is still
 * allowed.)  There is also a new switch expression.
 */
public class NewSwitchDemo {

   public static void main(String[] args) {
   
       /* In the new switch syntax, a case is not restricted to a single value;
        * it can include a list of constant values, separated by commas.  The ":"
        * in the old syntax is replaced by "->".  The "->" is followed by a 
        * single statement, which can be a block statement (that is, a sequence
        * of zero or more statements, enclosed between braces).  No break statement
        * is necessary (or allowed) after that single statement.  However, a break
        * is allowed within a block statement, to exit the switch statement
        * early.
        */
        
       switch ( (int)(3*Math.random()) ) {  // a simple example from the textbook
          case 0 -> System.out.println("ROCK");
          case 1 -> System.out.println("PAPER");
          case 2 -> System.out.println("SCISSORS");
       }
       
       System.out.println( 
           switch ( (int)(3*Math.random()) ) { // The same example, as a switch expression
               case 0 -> "ROCK";
               case 1 -> "PAPER";
               case 2 -> "SCISSORS";
               default -> "";  // A default case is required because a switch expression
                               // must return a value in all possible cases.
           }
       );
       System.out.println();
       
       String month = (new String[] { "January", "February", "March", "April", "May", "June", 
           "July", "August", "September", "October", "November", "December" }) [ (int)(12*Math.random()) ];
       int year = 1800 + (int)(221*Math.random());
           
       switch ( month ) {  // An example demonstrating multiple constants and a block statement in a case
           case "September", "April", "June", "November" ->
               System.out.printf("%s, %d had 30 days.%n%n", month, year);
           case "February" -> {
               int days;
               if ( year % 100 == 0 ) {
                   if ( year % 400 == 0 )
                       days = 29;
                   else
                       days = 28; 
               }
               else if ( year % 4 == 0 ) {
                   days = 29;
               }
               else {
                   days = 28;
               }
               System.out.printf("%s, %d had %d days.%n%n", month, year, days);
           }
           default -> 
               System.out.printf("%s, %d had 31 days.%n%n", month, year);
       }
       
       /* Some code using function romanNumeralValue, defined below. */
   
       String roman = "MMCMLXIV";
       int arabic = 0;
       for (int i = 0; i < roman.length(); i++) {
           int val = romanNumeralValue(roman.charAt(i));
           if (i+1 < roman.length()) {
              int nextVal = romanNumeralValue(roman.charAt(i+1));
              if (nextVal > val) {
                 arabic += nextVal - val;
                 i++;
                 continue;
              }
           }
           arabic += val;
       }
       System.out.printf("The value of the roman numeral %s is %d%n%n", roman, arabic);
   }
   
   
   /* This method uses a switch expression to compute the
    * return value.  Note that in the new switch syntax,
    * a case can specify a list of constants, separated by
    * commas.  The ":" in the old syntax is replaced by
    * "->".  For a switch expression, the "->" can
    * be followed by a single value representing the
    * value of the expression for that case.  A single block 
    * statement is also allowed, and in that case, the
    * value is specified using a yield statement.
    */
   static int romanNumeralValue(char ch) {
       return switch(ch) {
          case 'M', 'm' -> 1000;
          case 'D', 'd' ->  500;
          case 'C', 'c' ->  100;
          case 'L', 'l' ->   50;
          case 'X', 'x' ->   10;
          case 'V', 'v' ->    5;
          case 'I', 'i' ->  { // A silly example of using yield in a block statement.
             int x = 1;
             yield x; // The value of the switch expression is specified using "yield <value>".
          }
          default -> throw new IllegalArgumentException("illegal char '" + ch + "'");
       }; // Note: semicolon required to end the return statement.
   }


} // end class NewSwitchDemo


