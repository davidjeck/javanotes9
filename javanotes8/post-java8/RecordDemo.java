
/* This class demonstrates Java's new "record" feature, which was
 * added to the language in Java 16.  It can be used with Java 16
 * and later.
 */
 
public class RecordDemo {
   
   /* A record is a kind of class, declared using the keyword "record"
    * and providing a list of field declarations after the class name.
    * A FullName object has two fields of type String named firstName
    * and lastName.  The record class automatically defines a constructor
    * with two String parameters that initialize the fields.  It defines
    * accessor methods for the fields, but instead of the usual "getXXX()"
    * nameing convention, the name of the accessor field is the same
    * as the name of the field.  A record class also provides
    * default implementations of toString(), equals(), and
    * hashCode().  Note that a record class is automaticall static and
    * final, and the fields are automatically final.  In this example,
    * as in many cases, the body of the class is empty,
    */
   record FullName(String firstName, String lastName) { }
      

   /* A record class can contain static fields and methods, as
    * well as instance methods.  It can override the built-in 
    * definitions of toString(), equals(), and hashCode().
    * It can define additional constructors.  However, it cannot
    * define additional instance variables.
    */
   record Complex(double re, double im) {
   
      final static public Complex ONE = new Complex(1,0);
      final static public Complex I = new Complex(0,1);
      
      public Complex(Complex c) { // copy constructor
         this(c.re,c.im);
      }
      
      public Complex plus(Complex that) {  // some instance methods
         return new Complex(this.re + that.re, this.im + that.im);
      }
      public Complex minus(Complex that) {
         return new Complex(this.re - that.re, this.im - that.im);
      }
      public Complex times(Complex that) {
         return new Complex(this.re*that.re - this.im*that.im,
                            this.re*that.im + this.im*that.re);
      }
      public Complex dividedBy(Complex that) {
         double denom = that.re*that.re + that.im*that.im;
         double real = (this.re*that.re + this.im*that.im)/denom;
         double imaginary = (this.im*that.re - this.re*that.im)/denom;
         return new Complex(real,imaginary);
      }
      
      public String toString() { // override the predefined toString()
         if (this.im == 0)
            return String.valueOf(this.re);
         else if (this.re == 0)
            return this.im + "*I";
         else if (this.im < 0)
            return this.re + " - " + (-this.im) + "*I";
         else
            return this.re + " + " + this.im + "*I";
      }
      
   } // end record Complex
   
   
   public static void main(String[] args) {
   
      FullName fred = new FullName("Fred","Flintstone"); // call the built-in constructor
      System.out.println("Fred's full name is: " + fred);
      // The built-in toString() will output: FullName[firstName=Fred, lastName=Flintstone]
      System.out.println("The first name is " + fred.firstName()); // call accessor method
      System.out.println("The last name is  " + fred.lastName());  // call accessor method
      System.out.println();
   
      Complex a,b;
      a = new Complex(2,-3);
      b = new Complex(1,1);
      System.out.println("a = " + a + " and b = " + b);
      System.out.println("a+b = " + a.plus(b));
      System.out.println("a-b = " + a.minus(b));
      System.out.println("a*b = " + a.times(b));
      System.out.println("a/b = " + a.dividedBy(b));
   
      Complex oneOverI = Complex.ONE.dividedBy(Complex.I);
      System.out.println("One over the complex number I is " + oneOverI);
   }
   
} // end RecordDemo

