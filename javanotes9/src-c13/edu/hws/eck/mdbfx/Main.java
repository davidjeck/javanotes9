package edu.hws.eck.mdbfx;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Screen;
import java.util.prefs.*;

/**
 * This class is used to run the Mandelbrot viewer program. 
 * The start method shows a window containing a MandelbrotPane.
 * This class also works with "preferences."  When the window is closed,
 * an event handler saves the current window size and location to
 * the user's Java preferences.  It also saves the most recently selected
 * directory where the user saved or opened a file.  These values are restored 
 * from the user's preferences the next time the program starts.
 */
public class Main extends Application {
	
	/* The Mandelbrot program defined in package edu.hws.edu.mdbfx is a JavFX
	 * port of an older Swing version of the program.  Some things could have
	 * been done in a more JavaFX-like way, and since it is a fairly complex
	 * program, it is likely that the port has introduced a few unnoticed bugs.
	 * For a nicer Mandelbrot program in Java, see 
	 *     https://math.hws.edu/eck/js/mandelbrot/java/MB-java.html
	 * A pretty nice JavaScript version is at 
	 *     https://math.hws.edu/eck/js/mandelbrot/MB.html
	 */

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Open a window containing a MandelbrotPane, and apply preferences
	 * from previous runs if possible.
	 */
	public void start(Stage stage) {
		MandelbrotPane content = new MandelbrotPane();
		stage.setScene( new Scene(content) );
		stage.setOnHidden( e -> {
			content.closing();
			savePreferences(stage,content);
		} );
		applyPreferences(stage, content);
		stage.setMinHeight(240);
		stage.setMinWidth(240);
		stage.setTitle(I18n.tr("frame.title"));
		stage.show();
	}
	
	
	/**
	 * Called when the program ends to try to save window bounds and current
	 * directory to user's preferences, so they can be restored the next
	 * time the program is run.  Since this is not a critical function, errors
	 * are ignored.  If an error occurs, the data simply won't be saved.
	 */
	private static void savePreferences(Stage window, MandelbrotPane content) {
		try {
			String pathName = "/edu/hws/eck/mdbfx";
			   // The pathname uniquely identifies this program.  (It is unique
			   // because it comes from the package name, which should not be used
			   // for any other program under Java's package naming guidelines.
			Preferences root = Preferences.userRoot();
			Preferences node = root.node(pathName);
			   // This "node" holds the preferences associated with the pathName.
			   // Preferences take the form of key/value pairs, with put() and
			   // get() operations for changing/reading values.
			String boundsString = (int)window.getX() + "," + (int)window.getY() + "," 
										+ (int)window.getWidth() + "," + (int)window.getHeight();
			node.put("mandelbrot.window.bounds", boundsString);  
			Menus menus = content.getMenus();
			String currentDir = menus.getSelectedDirectoryInFileChooser();
			if (currentDir != null)
				node.put("mandelbrot.filechooser.directory", currentDir); 
		}
		catch (Exception e) {
		}
	}
	
	
	/**
	 * This method is called when the program starts to read and apply the
	 * preferences that were saved the last time the program ran.  Since
	 * this is not a critical function, errors are ignored.  If an error
	 * occurs, preferences will simply not be restored.
	 */
	private static void applyPreferences(Stage window, MandelbrotPane content) {
		try {
			String pathName = "/edu/hws/eck/mdbfx";  // Identifies prefs for this program.
			Preferences root = Preferences.userRoot();
			if (! root.nodeExists(pathName) )
				return;  // There are no saved prefs for this program yet.
			Preferences node = root.node(pathName);
			String boundsString = node.get("mandelbrot.window.bounds",null);
			if (boundsString != null) {
				   // Try to restore window bounds, ignoring any error.
				String[] bounds = boundsString.split(",");
				try {  // set window bounds, after running some sanity checks
					double x = Integer.parseInt(bounds[0]);
					double y = Integer.parseInt(bounds[1]);
					double w = Integer.parseInt(bounds[2]);
					double h = Integer.parseInt(bounds[3]);
					if (w <= 0 || h <= 0 || w > 5000 || h > 5000)
						throw new NumberFormatException();  // unreasonable values.
					Rectangle2D screen = Screen.getPrimary().getVisualBounds();
					if (x < screen.getMinX() || x > screen.getMaxX() - 300)
						x = screen.getMinX() + 20;
					if (y < screen.getMinY() || y > screen.getMaxY() - 200)
						y = screen.getMinY() + 20;
					if (w < 300)
						w = 300;
					else if (x + w > screen.getMaxX())
						w = screen.getMaxX() - x;
					if (h < 200)
						h = 200;
					else if (y + h > screen.getMaxY())
						h = screen.getMaxY() - y;
					window.setX(x);
					window.setY(y);
					window.setWidth(w);
					window.setHeight(h);
				}
				catch (NumberFormatException e) {
					// in case of error, ignore the bounds
				}
			}
			Menus menus = content.getMenus();
			String directory = node.get("mandelbrot.filechooser.directory", null);
			if (directory != null) {
				   // Try to restore the current directory.
				menus.setSelectedDirectoryInFileChooser(directory);
			}
		}
		catch (Exception e) {
		}
	}
		
}
