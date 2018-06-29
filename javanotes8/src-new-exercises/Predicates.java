import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * This class defines some static methods for working
 * with Collections and Predicates.  (The Predicate
 * interface is NOT a standard part of Java.)
 */
public class Predicates {

   
   /**
    * Remove every object, obj, from coll for which pred.test(obj) is true.
    */
   public static <T> void remove(Collection<T> coll, Predicate<T> pred) {
      Iterator<T> iter = coll.iterator();
      while (iter.hasNext()) {
         T item = iter.next();
         if (pred.test(item))
            iter.remove();
      }
   } // end remove()

   
   /**
    * Remove every object, obj, from coll for which pred.test(obj) is false.  
    * (That is, retain the objects for which the predicate is true.)
    */
   public static <T> void retain(Collection<T> coll, Predicate<T> pred){
      Iterator<T> iter = coll.iterator();
      while (iter.hasNext()) {
         T item = iter.next();
         if ( ! pred.test(item) )
            iter.remove();
      }
   } // end retain()
   
   
   /**
    * Return a List that contains all the objects, obj, from the collection, 
    * coll, for which pred.test(obj) is true.
    */
   public static <T> List<T> collect(Collection<T> coll, Predicate<T> pred) {
      List<T> list = new ArrayList<T>();
      for ( T item : coll ) {
         if (pred.test(item))
            list.add(item);
      }
      return list;
   } // end collect()

   
   /**
    * Return the index of the first item in list for which the predicate is true, if any.
    * If there is no such item, return -1.
    */
   public static <T> int find(ArrayList<T> list, Predicate<T> pred) {
      // 
      for (int i = 0; i < list.size(); i++) {
         T item = list.get(i);
         if (pred.test(item))
            return i;
      }
      return -1;
   } // end find()

   
} // end class Predicates