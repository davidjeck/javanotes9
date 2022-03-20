
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;

/**
 * A basic multi-window web browser.  This class is responsible for
 * creating new windows and for maintaining a list of currently open
 * windows.  The program ends when all windows have been closed.
 * The windows are of type BrowserWindow.  The program also requires
 * the class SimpleDialogs.  The first window, which opens when the
 * program starts, goes to https://math.hws.edu/javanotes/index.html.
 */
public class WebBrowser extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//----------------------------------------------------------------------------------------------------
	
	private ArrayList<BrowserWindow> openWindows;  // list of currently open web browser windows
	private Rectangle2D screenRect;                // usable area of the primary screen
	private double locationX, locationY;           // location for next window to be opened
	private double windowWidth, windowHeight;      // window size, computed from screenRect
	private int untitledCount;                     // how many "Untitled" window titles have been used
	
	
	/* Opens a window that will load the URL https://math.hws.edu/javanotes/index.html
	 * (the front page of the textbook in which this program is an example).
	 * Note that the Stage parameter to this method is never used.
	 */
	public void start(Stage stage) {
		
		openWindows = new ArrayList<BrowserWindow>();  // List of open windows.
		
		screenRect = Screen.getPrimary().getVisualBounds();
		
		   // (locationX,locationY) will be the location of the upper left
		   // corner of the next window to be opened.  For the first window,
		   // the window is moved a little down and over from the top-left
		   // corner of the primary screen's visible bounds.
		locationX = screenRect.getMinX() + 30;
		locationY = screenRect.getMinY() + 20;
		
		   // The window size depends on the height and width of the screen's
		   // visual bounds, allowing some extra space so that it will be
		   // possible to stack several windows, each displaced from the
		   // previous one.  (For aesthetic reasons, limit the width to be
		   // at most 1.6 times the height.)
		windowHeight = screenRect.getHeight() - 160;
		windowWidth = screenRect.getWidth() - 130;
		if (windowWidth > windowHeight*1.6)
			windowWidth = windowHeight*1.6;
		
		   // Open the first window, showing the front page of this textbook.
		newBrowserWindow("https://math.hws.edu/javanotes/index.html");
		
	} // end start()
	
	/**
	 * Get the list of currently open windows.  The browser windows use this
	 * list to construct their Window menus.
	 * A package-private method that is meant for use only in BrowserWindow.java.
	 */
	ArrayList<BrowserWindow> getOpenWindowList() {
		return openWindows;
	}
	
	/**
	 * Get the number of window titles of the form "Untitled XX" that have been
	 * used.  A new window that is opened with a null URL gets a title of
	 * that form.  This method is also used in BrowserWindow to provide a
	 * title for any web page that does not itself provide a title for the page.
	 * A package-private method that is meant for use only in BrowserWindow.java.
	 */
	int getNextUntitledCount() {
		return ++untitledCount;
	}
	
	/**
	 * Open a new browser window.  If url is non-null, the window will load that URL.
	 * A package-private method that is meant for use only in BrowserWindow.java.
	 * This method manages the locations for newly opened windows.  After a window
	 * opens, the next window will be offset by 30 pixels horizontally and by 20
	 * pixels vertically from the location of this window; but if that makes the
	 * window extend outside screenRect, the horizontal or vertical position will
	 * be reset to its minimal value.
	 */
	void newBrowserWindow(String url) {
		BrowserWindow window = new BrowserWindow(this,url);
		openWindows.add(window);   // Add new window to open window list.
		window.setOnHidden( e -> {
			    // Called when the window has closed.  Remove the window
			    // from the list of open windows.
			openWindows.remove( window );
			System.out.println("Number of open windows is " + openWindows.size());
			if (openWindows.size() == 0) {
				// Program ends automatically when all windows have been closed.
				System.out.println("Program will end because all windows have been closed");
			}
		});
		if (url == null) {
			window.setTitle("Untitled " + getNextUntitledCount());
		}
		window.setX(locationX);         // set location and size of the window
		window.setY(locationY);
		window.setWidth(windowWidth);
		window.setHeight(windowHeight);
		window.show();
		locationX += 30;    // set up location of NEXT window
		locationY += 20;
		if (locationX + windowWidth + 10 > screenRect.getMaxX()) {
			    // Window would extend past the right edge of the screen,
			    // so reset locationX to its original value.
			locationX = screenRect.getMinX() + 30;
		}
		if (locationY + windowHeight + 10 > screenRect.getMaxY()) {
			    // Window would extend past the bottom edge of the screen,
			    // so reset locationY to its original value.
			locationY = screenRect.getMinY() + 20;
		}
	}
	
	
} // end WebBrowser
