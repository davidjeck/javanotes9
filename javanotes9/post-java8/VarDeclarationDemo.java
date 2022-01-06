import java.util.*;
import java.util.stream.Collectors;

/* Java 10 introduced a new way of declaring local variables
 * by using the word "var" in place of a type name in the
 * declaration.  The variable must be assigned an initial
 * value, and the actual type of the variable is deduced 
 * from the type of the initial value.  This is most useful
 * with complex types.  This program demonstrates several
 * uses of var.
 */
public class VarDeclarationDemo {

   public static void main(String[] args) {
   
      /* Declare some variables using var. */
   
      var name = "Fred";  // The type of name is String
      var answer = 42;  // Type is int
      var list = new ArrayList<String>(); // Type is ArrayList<String>
      
      list.add("Wilma");
      list.add("Fred");
      list.add("Barney");
      list.add("Betty");
      
      System.out.println("The list " + (list.contains(name)? "does" : "does not")
                            + " contain " + name);
      System.out.println();
                            
      var map = new TreeMap<String,Integer>();
      
      /* var can be used to declare the loop control variable
       * in a for loop or foreach loop. */
      
      for (var i = 0; i < list.size(); i++) {
         map.put( list.get(i), list.get(i).length() );
      }
      
      System.out.println("List elements with their lengths:");
      for (var item : map.entrySet()) {
         // The type of item is Map.Entry<String,Integer> 
         System.out.printf("%15s   %d\n", item.getKey(), item.getValue());
      }
      System.out.println();
      
      var lengths = list.stream()
                        .mapToInt( s -> s.length() )
                        .boxed()
                        .collect( Collectors.toList() ); // Type is List<Integer>
      
      System.out.println("Now, just the lengths");
      for (var item : lengths) {
          System.out.printf("%8d\n", item);
      }
      
      
   }

} // end class VarDeclarationDemo

