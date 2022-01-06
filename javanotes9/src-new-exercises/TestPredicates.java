import java.util.*;
import java.util.function.Predicate;

/**
 * Perform some simple tests on the Predicates class.
 */
public class TestPredicates {

   /**
    * For convenience make a Collection containing some integers.  The Collection 
    * is actually a TreeSet, but that is not relevant to the main program.
    */
   static Collection<Integer> makeSet() {
      Collection<Integer> set = new TreeSet<Integer>();
      set.add(Integer.valueOf(32));
      set.add(Integer.valueOf(17));
      set.add(Integer.valueOf(142));
      set.add(Integer.valueOf(56));
      set.add(Integer.valueOf(1899));
      set.add(Integer.valueOf(57));
      set.add(Integer.valueOf(999));
      set.add(Integer.valueOf(86));
      set.add(Integer.valueOf(83));
      set.add(Integer.valueOf(100));
      return set;
   } // end makeSet()

   /**
    * Main routine tests the Predicates class by making Collections of Integers 
    * and applying the methods from the Predicates class to them.
    */
   public static void main(String[] args) {

      Collection<Integer> coll;    // A collection (from makeSet() method).

      List<Integer> result;        // The result of applying collect() to coll.

      Predicate<Integer> pred = i -> (i % 2 == 0);  // Tests if an Integer is even.

      coll = makeSet();
      System.out.println("Original collection: " + coll);

      Predicates.remove(coll,pred);
      System.out.println("Remove evens: " + coll);

      coll = makeSet();
      Predicates.retain(coll,pred);
      System.out.println("Retain evens: " + coll);

      coll = makeSet();
      result = Predicates.collect(coll,pred);
      System.out.println("Collect evens: " + result);

      ArrayList<Integer> list = new ArrayList<Integer>();
      int i = Predicates.find(list,pred);
      System.out.println("Find first even: at index " + i);


      pred = n -> (n > 100);        // Tests if an Integer is bigger than 100.

      coll = makeSet();
      System.out.println("Original collection: " + coll);

      Predicates.remove(coll,pred);
      System.out.println("Remove big: " + coll);

      coll = makeSet();
      Predicates.retain(coll,pred);
      System.out.println("Retain big: " + coll);

      coll = makeSet();
      result = Predicates.collect(coll,pred);
      System.out.println("Collect big: " + result);

      list = new ArrayList<Integer>(coll);
      i = Predicates.find(list,pred);
      System.out.println("Find first big: at index " + i);

   } // end main()

} // end class TestPredicates