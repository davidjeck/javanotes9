package textio;

import java.io.PrintWriter;
import java.io.BufferedReader;

/**
 * This is a "fake" System class, for use with the GUI version of
 * TextIO.  It provides only a few of the capabilities of 
 * java.lang.System.  Note that for this class, System.out
 * and System.err are PrintWriters, and System.in is a BufferedReader.
 * A few subroutines are provided as simple wrappers for the
 * real System class.
 */
public class System {
    
    public static PrintWriter err = TextIO.getOut();
    public static PrintWriter out = err;
    public static BufferedReader in = TextIO.getIn();
    
    public static long currentTimeMillis() {
        return java.lang.System.currentTimeMillis();
    }
    
    public static long nanoTime() {
        return java.lang.System.nanoTime();
    }
    
    public static void exit(int code) {
        java.lang.System.exit(code);
    }
    
    public static String getProperty(String name) {
        return java.lang.System.getProperty(name);
    }

    public static String getProperty(String name, String def) {
        return java.lang.System.getProperty(name, def);
    }
    
    public static String lineSeparator() {
        return java.lang.System.lineSeparator();
    }
    
    // Returns null, which will cause a NullPointerException if
    // an attempt is made to use the console.  This is actually
    // correct behavior since the GUI version of TextIO is not
    // a proper console.
    public static java.io.Console console() {
        return null;
    }

}
