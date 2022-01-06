/* This program demonstrates text blocks, a new feature in Java 15.
 * It can be compiled and run with Java 15 or later.  A text block
 * is a string literal that extends over several lines.  It begins
 * and ends with a triple double-quote (""").  The opening triple
 * quote must be followed (except for whitespace) by a newline, which 
 * is not part of the string represented by the literal.  Text blocks 
 * make it easier to use multiline strings in a Java program.
 */

public class TextBlockDemo {

   public static void main(String[] args) {
      
      /* Define a multiline string.  Note that extra whitespace
       * is stripped from the start of each line -- essentially
       * enough is stripped to shove the string over to the 
       * left margin. */
   
      String poem = """ 
         As I was walking down the stair,
            I met a man who wasn't there.
         He wasn't there again today.
            I wish, I wish he'd go away!""";
            
      System.out.println(poem);
      System.out.println();
      
      /* A text block can inlude escaped characters such as \t, \n,
       * and \\.  Characters other than '\' are not special in textblocks.
       * For example, something that looks like a Java comment inside
       * a text block is not a comment; it is part of the string.
       * Also note that when the closing """ is on a line by itself,
       * then the newline that precedes the """ is part of the string. */
      
      String program = """
         /**
          * The standard HelloWorld program.
          */
         public class HelloWorld {\n
            public static void main(String[] args) {
               System.out.println("Hello World"); // greet the world!!
            }\n
         }
         """;
         
      System.out.println(program);
      
      /* A text block can be used anywhere a string literal could be used,
       * such as in a formatted print statement. */
      
      int miles = 17;
      System.out.printf("""
            The equivalent of %d miles is:
                %d yards
                %d feet
                %d inches
         """, miles, 1760*miles, 5280*miles, 12*5280*miles);
                  
   }

} // end TextBlockDemo

