
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * MosaicDraw is a JavaFX Application whose scene includes a MosaicCanvas and a
 * MenuBar.  The mosaic is made up of rows and columns of squares.  The user 
 * can click-and-drag the mouse to color the squares.  A menu bar with some 
 * options is shown at the top of the window. 
 * 
 * Note that this class depends on MosaicCanvas.java.
 */
public class MosaicDraw extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	//----------------------------------------------------------------------------------
	
	private final static int ROWS = 40;        // rows in the mosaic
	private final static int COLUMNS = 40;     // columns in the mosaic
	private final static int SQUARE_SIZE = 15; // size of each square

	private final static int DRAW_TOOL = 0;      // possible values for currentTool
	private final static int ERASE_TOOL = 1;
	private final static int DRAW_3x3_TOOL = 2;
	private final static int ERASE_3x3_TOOL = 3;

	private int currentTool;  // The current tool; this is changed when the
	                          // user makes a selection from the Tools menu.

	private int currentRed, currentGreen, currentBlue;  // The current color.
					// These change when the user selects from the Color menu
					// They are used whenever a square is painted.  (NOTE:
					// I am using three integers to represent the color, rather
					// than a variable of type Color, to make it easier to add
					// in the random color variation.)

	private MosaicCanvas mosaic;     // The panel where the drawing takes place.

	private CheckMenuItem useRandomness;  // If selected, then a small random variation is
										  // added to the current color whenever a 
										  // square is painted.  This menu item is added
										  // to the Control menu in createMenuBar().
	
	private CheckMenuItem useSymmetry;  // If selected, then whenever a square is painted
										// or erased, the three symmetrical squares
										// obtained by reflecting the square vertically
										// and horizontally are also painted or erased.
										// This menu item is added to the Control menu
										// in createMenuBar().

	/**
	 * Create the canvas and menu bar and add them to the window.
	 * Set up mouse handling on the canvas.
	 */
	public void start(Stage stage) {
		
		mosaic = new MosaicCanvas(ROWS, COLUMNS, SQUARE_SIZE, SQUARE_SIZE);
		mosaic.setOnMousePressed( e -> doMouse(e));
		mosaic.setOnMouseDragged( e -> doMouse(e));
		mosaic.clear();
		currentRed = 255;
		currentGreen = 0;
		currentBlue = 0;

		BorderPane root = new BorderPane();
		Scene scene = new Scene(root);
		
		root.setCenter(mosaic);
		root.setTop( createMenuBar() );
		
		stage.setTitle("Mosaic Draw");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setX(150);  // Put the window at screen coords (150,100).
		stage.setY(100);
		stage.show();
	}	
	

	/**
	 * Creates and returns a menu bar that contains options that affect the
	 * drawing that is done on the MosaicCanvas.
	 */
	private MenuBar createMenuBar() {
		
		MenuBar menuBar = new MenuBar();

		Menu controlMenu = new Menu("Control");
		MenuItem fill = new MenuItem("Fill");
		controlMenu.getItems().add(fill);
		fill.setOnAction(e -> doFill());
		MenuItem clear = new MenuItem("Clear");
		controlMenu.getItems().add(clear);
		clear.setOnAction(e -> mosaic.clear());
		controlMenu.getItems().add(new SeparatorMenuItem());
		useRandomness = new CheckMenuItem("Use Randomness");
		useRandomness.setSelected(true);
		useSymmetry = new CheckMenuItem("Use Symmetry");
		CheckMenuItem useGrouting = new CheckMenuItem("Use Grouting");
		mosaic.setGroutingColor(null);  // turn off grouting to match state of menu item
		useGrouting.setOnAction( e -> doUseGrouting(useGrouting.isSelected()));
		controlMenu.getItems().addAll(useRandomness, useSymmetry, useGrouting);
		
		Menu colorMenu = new Menu("Color");
		String[] colorNames = { "Red", "Green", "Blue", "Cyan", "Magenta", "Yellow", "Gray" };
		ToggleGroup colorGroup = new ToggleGroup();
		for (int i = 0; i < colorNames.length; i++) {
			String colorName = colorNames[i];
			RadioMenuItem item = new RadioMenuItem(colorName);
			item.setOnAction( e -> doColorChoice(colorName) );
			item.setToggleGroup(colorGroup);
			colorMenu.getItems().add(item);
			if (i == 0)
				item.setSelected(true);
		}
		
		Menu toolMenu = new Menu("Tools");
		ToggleGroup toolGroup = new ToggleGroup();
		toolGroup.selectedToggleProperty().addListener( e -> doToolChoice(toolGroup.getSelectedToggle()) );
		addRadioMenuItem(toolMenu,"Draw",toolGroup, true);
		addRadioMenuItem(toolMenu,"Erase",toolGroup, false);
		addRadioMenuItem(toolMenu,"Draw 3x3",toolGroup, false);
		addRadioMenuItem(toolMenu,"Erase 3x3",toolGroup, false);
		
		menuBar.getMenus().addAll(controlMenu,colorMenu,toolMenu);
		return menuBar;
		
	}  // end createMenuBar()
	

	/**
	 * Utility method to create a radio menu item, add it to a ToggleGroup, and add it to a menu.
	 */
	private void addRadioMenuItem(Menu menu, String command, ToggleGroup group, boolean selected) {
		RadioMenuItem menuItem = new RadioMenuItem(command);
		menuItem.setToggleGroup(group);
		menu.getItems().add(menuItem);
		if (selected) {
			menuItem.setSelected(true);
		}
	}


	/**
	 * Erases the square in a specified row and column.  If symmetry is turned
	 * on, the three symmetrical squares are also erased.
	 */
	private void eraseSquare(int row, int col) {
		mosaic.setColor(row, col, null);
		if (useSymmetry.isSelected()) {
			mosaic.setColor(mosaic.getRowCount() - 1 - row, col, null);
			mosaic.setColor(row, mosaic.getColumnCount() - 1 - col, null);
			mosaic.setColor(mosaic.getRowCount() - 1 - row, mosaic.getColumnCount() - 1 - col, null);
		}
	}
	

	/**
	 * Applies the current drawing color to the square in a given row and column.
	 * If randomness is turned on, a random amount is added to the red, green, and 
	 * blue components of the drawing color.  If symmetry is turned on, then the
	 * three symmetrical squares are also painted.
	 */
	private void paintSquare(int row, int col) {
		int r = currentRed;
		int g = currentGreen;
		int b = currentBlue;
		if (useRandomness.isSelected()) {
			if (r < 60)
				r = (int)(60*Math.random());
			else if (r > 255-60)
				r = 255 - (int)(60*Math.random());
			else
				r = r + (int)(60*Math.random() - 30);
			if (g < 60)
				g = (int)(60*Math.random());
			else if (g > 255-60)
				g = 255 - (int)(60*Math.random());
			else
				g = g + (int)(60*Math.random() - 30);
			if (b < 60)
				b = (int)(60*Math.random());
			else if (b > 255-60)
				b = 255 - (int)(60*Math.random());
			else
				b = b + (int)(60*Math.random() - 30);
		}
		Color color = Color.rgb(r,g,b);
		mosaic.setColor(row, col, color);
		if (useSymmetry.isSelected()) {
			mosaic.setColor(mosaic.getRowCount() - 1 - row, col, color);
			mosaic.setColor(row, mosaic.getColumnCount() - 1 - col, color);
			mosaic.setColor(mosaic.getRowCount() - 1 - row, mosaic.getColumnCount() - 1 - col, color);
		}
	}

	
	/**
	 * This method is called when the user clicks the mouse or drags it over the
	 * square in the specified row and column.  It takes the appropriate action,
	 * depending on which drawing tool is currently selected.
	 */
	private void applyCurrentTool(int row, int col) {
		int minrow, mincol, maxrow, maxcol;
		switch (currentTool) {
		case DRAW_TOOL:
			paintSquare(row,col);
			break;
		case ERASE_TOOL:
			eraseSquare(row,col);
			break;
		case DRAW_3x3_TOOL:
			minrow = Math.max(0, row-1);
			maxrow = Math.min(mosaic.getRowCount()-1, row+1);
			mincol = Math.max(0, col-1);
			maxcol = Math.min(mosaic.getColumnCount()-1, col+1);
			for (int i = minrow; i <= maxrow; i++)
				for (int j = mincol; j <= maxcol; j++)
					paintSquare(i,j);
			break;
		case ERASE_3x3_TOOL:
			minrow = Math.max(0, row-1);
			maxrow = Math.min(mosaic.getRowCount()-1, row+1);
			mincol = Math.max(0, col-1);
			maxcol = Math.min(mosaic.getColumnCount()-1, col+1);
			for (int i = minrow; i <= maxrow; i++)
				for (int j = mincol; j <= maxcol; j++)
					eraseSquare(i,j);
			break;
		}
	}
	
	
	/**
	 * Fill the mosaic with squares of the current drawing color.
	 * (If symmetry is on, each square actually has its color set
	 * four times, so efficiency could be improved!)
	 */
	private void doFill() {
		mosaic.setAutopaint(false); // Turning off autopaint lets all the squares
		                            // be draws without inserting any delays;
		                            // when autopaint is on, there is a one
		                            // millisecond delay for each square drawn.
		for (int row = 0; row < mosaic.getRowCount(); row++)
			for (int col = 0; col < mosaic.getColumnCount(); col++)
				paintSquare(row,col);
		mosaic.setAutopaint(true);
	}
	

	/**
	 * Apply the current tool to the square that contains the mouse pointer.
	 * This method is called in response to both MousePressed and MouseDragged
	 * event.  The mouse handlers that called it are added to the mosaic
	 * in the start() method.
	 */
	private void doMouse(MouseEvent evt) {
		int row = mosaic.yCoordToRowNumber((int)evt.getY());
		int col = mosaic.xCoordToColumnNumber((int)evt.getX());
		if (row >= 0 && row < mosaic.getRowCount() && col >= 0 && col < mosaic.getColumnCount()) {
			   // (the test in this if statement will be false if the user drags the
			   //  mouse outside the canvas after pressing the mouse on the canvas)
			applyCurrentTool(row,col);
		}
	}

	
	/**
	 * This method is called when the user clicks the "Use Grouting"
	 * command in the Control menu.  The parameter tells whether
	 * the CheckMenuItem is checked.  The handler that calls this
	 * method is added to the menu item in the createMenuBar() method.
	 */
	public void doUseGrouting(boolean use) {
		if (use)
			mosaic.setGroutingColor(Color.GRAY);
		else
			mosaic.setGroutingColor(null);  // Turns grouting off.
	}
	
	
	/**
	 * Changes the current drawing color when the user clicks one of the
	 * colors in the Color menu.  A handler to call this method is added
	 * to each color menu item in the createMenuBar() method.  (Note that
	 * this method will be called even if the user picks the color that
	 * is already selected, but that is harmless.)
	 */
	private void doColorChoice( String colorName ) {
		if (colorName.equals("Red")) { 
			currentRed = 255;
			currentGreen = 0;
			currentBlue = 0;
		}
		else if (colorName.equals("Green")) {
			currentRed = 0;
			currentGreen = 180;
			currentBlue = 0;
		}
		else if (colorName.equals("Blue")) {
			currentRed = 0;
			currentGreen = 0;
			currentBlue = 255;
		}
		else if (colorName.equals("Cyan")) {
			currentRed = 0;
			currentGreen = 255;
			currentBlue = 255;
		}
		else if (colorName.equals("Magenta")) {
			currentRed = 255;
			currentGreen = 0;
			currentBlue = 255;
		}
		else if (colorName.equals("Yellow")) {
			currentRed = 255;
			currentGreen = 255;
			currentBlue = 0;
		}
		else if (colorName.equals("Gray")) {
			currentRed = 180;
			currentGreen = 180;
			currentBlue = 180;
		}
	}
	
	
	/**
	 * This method is called when the selected item in the
	 * Tools menu is changed for any reason.  The handler
	 * is added to the ToggleGroup that controls the menu
	 * items.  Apparently, it is called twice when the user
	 * changes the selection, once when the current item is
	 * deselected (with parameter value null) and once
	 * when the new item is selected.  The current tool is only
	 * changed when the parameter is non-null.
	 */
	private void doToolChoice( Toggle toggle ) {
		if (toggle == null)
			return;
		String toolName = ((RadioMenuItem)toggle).getText();
		if (toolName.equals("Draw"))
			currentTool = DRAW_TOOL;
		else if (toolName.equals("Erase"))
			currentTool = ERASE_TOOL;
		else if (toolName.equals("Draw 3x3"))
			currentTool = DRAW_3x3_TOOL;
		else if (toolName.equals("Erase 3x3"))
			currentTool = ERASE_3x3_TOOL;
	}
	

} // end MosaicDraw
