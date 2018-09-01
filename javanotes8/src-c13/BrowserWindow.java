import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.concurrent.Worker;

import java.util.ArrayList;


/**
 * This package-private class defines one browser window for use with 
 * class WebBrowser.  The window can load and display one web page at
 * a time.  There is a text-input box at the bottom of the window
 * where the user can input a URL to be opened.  There is a Window
 * menu that lets the user open a new, empty BrowserWindow, or 
 * a BrowserWindow that will load a specified URL.  The Window menu
 * also contains a list of currently open BrowserWindows, and the
 * user can bring a window to the front by selecting it from that
 * menu.  This class is meant only for use with WebBrowser.java.
 */
class BrowserWindow extends Stage {

	private WebBrowser owner;    // the WebBrowser application
	private WebEngine webEngine; // loads and manages pages
	private Menu windowMenu;     // holds window-related commands

	/**
	 * The constructor sets up the window, and starts loading the
	 * initial web page location, if given.  It does not show the window.
	 * @param browser     the WebBrowser application that is createing this window
	 * @param initialURL  if non-null, the window will attempt to load this URL
	 */
	BrowserWindow(WebBrowser browser, String initialURL) {
		
		owner = browser;
		
		/* The WebView is the actual visual control in the window, showing a web page.
		 * The WebEngine is responsible for loading and managing the web page.  It is
		 * the major part of the controller for the WebView.  (The data model for the
		 * web view is a data structure that contains the web page content.) */
		
		WebView webview = new WebView();
		webEngine = webview.getEngine();
		
		/* Create controls for the bottom of the window. */
		
		Label status = new Label("Status: Idle");  // Displays the page load status.
		status.setMaxWidth(Double.POSITIVE_INFINITY);
		Label location = new Label("Location: (empty)");  // Displays the current URL.
		location.setMaxWidth(Double.POSITIVE_INFINITY);
		TextField urlInput = new TextField();  // Where the user inputs a URL to load.
		urlInput.setMaxWidth(600);
		Button loadButton = new Button("Load"); // For loading the URL in the textfield.
		loadButton.setOnAction( e -> doLoad(urlInput.getText()) );
		loadButton.defaultButtonProperty().bind( urlInput.focusedProperty() );
		Button cancelButton = new Button("Cancel");  // For canceling a load.
		cancelButton.setDisable(true);  // Will be enabled only when a load is in progress.
		
		/* Put together the GUI */
		
		HBox loader = new HBox(8,new Label("URL:"), urlInput, loadButton, cancelButton);
		HBox.setHgrow(urlInput, Priority.ALWAYS);
		
		VBox bottom = new VBox(10, location, status, loader);
		bottom.setStyle("-fx-padding: 10px; -fx-border-color:black; -fx-border-width:3px 0 0 0");		
		
		BorderPane root = new BorderPane(webview);
		root.setBottom(bottom);
		root.setTop(makeMenuBar());
		
		setScene( new Scene(root) );
		
		/* Get a title for the window and a URL for the location label from
		 * corresponding properties of the web engine. */
		
		webEngine.locationProperty().addListener( (o,oldVal,newVal) -> {
			if (newVal == null || newVal.equals("about:blank"))
				location.setText("Location: (empty)");
			else
				location.setText("Location: " + newVal);
		});
		
		webEngine.titleProperty().addListener( (o,oldVal,newVal) -> {
			if (newVal == null)
				setTitle("Untitled " + owner.getNextUntitledCount());
			else
				setTitle(newVal); 
		});
		
		/* The "state" of the worker that loads pages is reported in the 
		 * status label.   When the worker is "running", it means that
		 * a web page is being loaded; the "Cancel" button is only enabled
		 * when that is true. */
		
		webEngine.getLoadWorker().stateProperty().addListener( (o,oldVal,newVal) -> {
			status.setText("Status: " + newVal);
			switch (newVal) {
			case READY:
				status.setText("Status:  Idle.");
				break;
			case SCHEDULED:
			case RUNNING:
				status.setText("Status:  Loading a web page.");
				break;
			case SUCCEEDED:
				status.setText("Status:  Web page has been successfully loaded.");
				break;
			case FAILED:
				status.setText("Status:  Loading of the web page has failed.");
				break;
			case CANCELLED:
				status.setText("Status:  Loading of the web page has been cancelled.");
				break;
			}
			cancelButton.setDisable(newVal != Worker.State.RUNNING);
		});
		
		cancelButton.setOnAction( e -> { // Cancel the ongoing page load
			if ( webEngine.getLoadWorker().getState() == Worker.State.RUNNING )
				webEngine.getLoadWorker().cancel();
			
		});
		
		/* Set up handlers to deal with popup dialogs from javascript on the web page,
		 * generated by the javascript alert(), prompt(), and confirm() functions. */
		
		webEngine.setOnAlert( evt -> SimpleDialogs.message(evt.getData(), "Alert from web page") );
		webEngine.setPromptHandler( promptData -> 
					SimpleDialogs.prompt(promptData.getMessage(), 
							                  "Query from web page", promptData.getDefaultValue()));
		webEngine.setConfirmHandler( str -> 
					SimpleDialogs.confirm(str, "Confirmation Needed").equals("yes") );
		
		/* If the initialURL is not null, load that page! */
		
		if (initialURL != null) {
			   // load the initial web page, if any
			doLoad(initialURL);
		}
		
	} // end constructor
	
	
	/**
	 * Loads a specified URL, but nothing is done if the
	 * url string is null or empty.  The string should be
	 * a valid URL, meaning it should start with one or
	 * more letters followed by a colon.  If that is not
	 * the case, then "http://" is prepended to the string.
	 * Thus, it will accept a string such as "google.com"
	 * but will transform that to "http://google.com".
	 * (It seems like if the string is still not a legal
	 * URL, the web engine will simply load an empty page.
	 * It is not reported as an error.)
	 */
	private void doLoad(String url) {
		if (url == null || url.trim().length() == 0)
			return;
		url = url.trim();
		if ( ! url.matches("^[a-zA-Z]+:.*")) {
			url = "http://" + url;
		}
		System.out.println("Loading URL " + url);
		webEngine.load(url);
	}
	
	
	/**
	 * Create a menu bar for the window.  The only menu is a "Window" menu.
	 * It contains a "New" command that opens a new, empty window, a
	 * "Close" command that closes this window, an "Open" command that 
	 * opens a URL in this window, and a separator bar.  Before the menu
	 * is actually shown, additional items will be added corresponding to
	 * each open window.
	 */
	private MenuBar makeMenuBar() {
		MenuItem newWin = new MenuItem("New Window");
		newWin.setOnAction( e -> owner.newBrowserWindow(null) );
		MenuItem close = new MenuItem("Close Window");
		close.setOnAction( e -> hide() );
		MenuItem open = new MenuItem("Open URL in New Window...");
		open.setOnAction( e -> {
			String url = SimpleDialogs.prompt(
					"Enter the URL of the page that you want to open.", "Get URL");
			if (url != null && url.trim().length() > 0)
				owner.newBrowserWindow(url);
		});
		windowMenu = new Menu("Window");
		windowMenu.getItems().addAll(newWin,close,open,new SeparatorMenuItem());
		windowMenu.setOnShowing( e -> populateWindowMenu() );
		MenuBar menubar = new MenuBar(windowMenu);
		return menubar;
	}
	

	/**
	 * This method is called when the user clicks the window menu, but before the
	 * menu actually pops up.  It removes any items beyond the four permanent
	 * items that were added in makeMenuBar(), and it replaces them with
	 * an item corresponding to each item in the window lists.  A window
	 * item can be used to bring the corresponding window to the front.
	 * If there is more than one window, the menu also includes a 
	 * Close All option.
	 */
	private void populateWindowMenu() {
		ArrayList<BrowserWindow> windows = owner.getOpenWindowList();
		while (windowMenu.getItems().size() > 4) {
		       // The menu contains 4 permanent items.  Remove the other
		       // items, which correspond to open windows and are left 
		       // over from the previous time the menu was shown.
			windowMenu.getItems().remove(windowMenu.getItems().size() - 1);
		}
		if (windows.size() > 1) {
		       // Add a "Close All" command only if this is not the only window. 
			MenuItem item = new MenuItem("Close All and Exit");
			item.setOnAction( e -> Platform.exit() );
			windowMenu.getItems().add(item);
			windowMenu.getItems().add( new SeparatorMenuItem() );
		}
		for (BrowserWindow window : windows) {
			String title = window.getTitle(); // Menu item text is the window title.
			if (title.length() > 60) {
				    // Let's not use absurdly long menu item texts.
				title = title.substring(0,57) + ". . .";
			}
			MenuItem item = new MenuItem(title);
			final BrowserWindow win = window; // (for use in a lambda expression)
		        // The event handler for this menu item will bring the corresponding
		        // window to the front by calling its requestFocus() method.
			item.setOnAction( e -> win.requestFocus() );
			windowMenu.getItems().add(item);
			if (window == this) {
				   // Since this window is already at the front, the item
				   // corresponding to this window is disabled.
				item.setDisable(true);
			}
		}
	}
	
	
} // end BrowserWindow
