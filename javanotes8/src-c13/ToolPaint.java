
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

import javafx.scene.image.WritableImage;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.SnapshotParameters;
import javafx.geometry.Rectangle2D;

/**
 * A simple paint program that lets the user paint with several
 * different tools, including a "smudge" tool that uses fairly
 * sophisticated pixel manipulation.  The program also demonstrates
 * using a transparent "overlay" canvas to implement some of the
 * tools.  The window for this program is not resizable.
 * The program also requires the file SimpleDialogs.java.
 */
public class ToolPaint extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private Canvas canvas; // The canvas on which the image is drawn.
	private GraphicsContext canvasGraphics;  // The graphics context for the canvas.

	private Canvas overlay; // A transparent canvas that lies on top of
							// the image canvas, used for temporarily
							// drawing some shapes during a mouse drag.
	private GraphicsContext overlayGraphics;  // Graphics context for overlay.


	/**
	 * The possible drawing tools in this program.  (The CURVE tool allows the
	 * user to sketch a free-hand curve, while the LINE tool draws a line
	 * between two points.  The SMUDGE tool lets the user "spread paint around"
	 * with the mouse.  The ERASE tool erases with a 10-by-10 pixel rectangle.)
	 */
	private enum Tool { LINE, RECT, OVAL, FILLED_RECT, FILLED_OVAL, CURVE, SMUDGE, ERASE }

	private Tool currentTool = Tool.CURVE;  // The current drawing tool.

	private Color currentColor = Color.BLACK;  // The current drawing color.

	private Color backgroundColor = Color.WHITE;  // The current background color.

	/* Some variables that are used during dragging. */
	private boolean dragging;     // is a drag in progress?
	private int startX, startY;   // start point of drag
	private int prevX, prevY;     // previous mouse location during a drag
	private int currentX, currentY;  // current mouse position during a drag

	/* Some variables used to implement the smudge tool. */
	private double[][] smudgeRed, smudgeGreen, smudgeBlue;
	private WritableImage pixels; // a 9-by-9 block of pixels from the canvas  
	private PixelReader pixelReader;  // for reading colors from pixels
	private SnapshotParameters snapshotParams;  // used for snapshotting the canvas
	private PixelWriter pixelWriter;  // for writing pixels to the canvas

	private Stage window;  // The program's window.
	
	/**
	 * Create the canvas and the overlayCanvas, and set up mouse handling,
	 * configure and show the window.
	 */
	public void start(Stage stage) {
		
		window = stage;

		int width = 800;   // size of canvas; can be changed here
		int height = 600;
		
		canvas = new Canvas(width,height);
		canvasGraphics = canvas.getGraphicsContext2D();
		canvasGraphics.setFill(backgroundColor);
		canvasGraphics.fillRect(0,0,width,height);
		overlay = new Canvas(width,height);
		overlayGraphics = overlay.getGraphicsContext2D();
		overlay.setOnMousePressed( e -> mousePressed(e) );
		overlay.setOnMouseDragged( e -> mouseDragged(e) );
		overlay.setOnMouseReleased( e -> mouseReleased(e) );
		canvasGraphics.setLineWidth(2);
		overlayGraphics.setLineWidth(2);
		
		StackPane canvasHolder = new StackPane(canvas,overlay);
		BorderPane root = new BorderPane(canvasHolder);
		root.setTop( makeMenuBar() );
		
		stage.setScene( new Scene(root) );
		stage.setTitle("A Simple Paint Program");
		stage.setResizable(false);
		stage.show();

	} // end start()


	/**
	 * A utility method to draw the current shape in a given graphics context.
	 * It draws the correct shape for the current tool in a rectangle whose
	 * corners are given by the starting position of the mouse and the current
	 * position of the mouse.  This method is not used when the current tool 
	 * is Tool.CURVE or Tool.ERASE, or Tool.SMUDGE.  For other tools, it is
	 * used to draw the shape to the overlay canvas during a drag operation;
	 * then, when the drag ends, it is used to draw the shape to the main
	 * canvas.  The shape is defined by the tool and by the two points
	 * (startX,startY) and (currentX,currentY).
	 */
	private void putCurrentShape(GraphicsContext g) {
		switch (currentTool) {
		case LINE:
			g.strokeLine(startX, startY, currentX, currentY);
			break;
		case OVAL:
			putOval(g,false,startX, startY, currentX, currentY);
			break;
		case RECT:
			putRect(g,false,startX, startY, currentX, currentY);
			break;
		case FILLED_OVAL:
			putOval(g,true,startX, startY, currentX, currentY);
			break;
		case FILLED_RECT:
			putRect(g,true,startX, startY, currentX, currentY);
			break;
		default:  // not called in other cases
			break;
		}
	}


	/**
	 * Draws a filled or unfilled rectangle with corners at the points (x1,y1)
	 * and (x2,y2).  Nothing is drawn if x1 == x2 or y1 == y2.
	 * (This method translates from an opposite-corners definition of the rectangle
	 * to the upper-left-corner-width-and-height definition used for drawing.)
	 * @param g the graphics context where the rectangle is drawn
	 * @param filled tells whether to draw a filled or unfilled rectangle.
	 */
	private void putRect(GraphicsContext g, boolean filled, int x1, int y1, int x2, int y2) {
		if (x1 == x2 || y1 == y2)
			return;
		int x = Math.min(x1,x2);    // get upper left corner, (x,y)
		int y = Math.min(y1,y2);
		int w = Math.abs(x1 - x2);  // get width and height
		int h = Math.abs(y1 - y2);
		if (filled)
			g.fillRect(x,y,w,h);
		else
			g.strokeRect(x,y,w,h);
	}


	/**
	 * Draws a filled or unfilled oval in the rectangle with corners at the 
	 * points (x1,y1) and (x2,y2).  Nothing is drawn if x1 == x2 or y1 == y2.
	 * @param g the graphics context where the oval is drawn
	 * @param filled tells whether to draw a filled or unfilled oval.
	 */
	private void putOval(GraphicsContext g, boolean filled, int x1, int y1, int x2, int y2) {
		if (x1 == x2 || y1 == y2)
			return;
		int x = Math.min(x1,x2);    // get upper left corner, (x,y)
		int y = Math.min(y1,y2);
		int w = Math.abs(x1 - x2);  // get width and height
		int h = Math.abs(y1 - y2);
		if (filled)
			g.fillOval(x,y,w,h);
		else
			g.strokeOval(x,y,w,h);
	}

	
	/**
	 * Creates a menu bar for use with this program, with "Color"
	 * and "Tool" menus.
	 */
	private MenuBar makeMenuBar() {
		MenuBar menubar = new MenuBar();
		Menu fileMenu = new Menu("File");
		Menu colorMenu = new Menu("Color");
		Menu toolMenu = new Menu("Tool");
		menubar.getMenus().add(fileMenu);
		menubar.getMenus().add(colorMenu);
		menubar.getMenus().add(toolMenu);
		
		MenuItem openImage = new MenuItem("Load Image...");
		openImage.setOnAction( e -> doOpenImage() );
		fileMenu.getItems().add(openImage);
		MenuItem saveImage = new MenuItem("Save PNG Image...");
		saveImage.setOnAction( e -> doSaveImage() );
		fileMenu.getItems().add(saveImage);
		fileMenu.getItems().add( new SeparatorMenuItem() );
		MenuItem quit = new MenuItem("Quit");
		quit.setOnAction( e -> System.exit(0) );
		fileMenu.getItems().add(quit);
		
		/* Color choices are given by RadioMenuItems, controlled by
		 * a ToggleGroup.  Each choice corresponds to a standard color,
		 * except for a "Custom Drawing Color" item that calls up
		 * a color choice dialog box. */
		
		Color[] colors = { // Standard colors available in the menu.
				Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, 
				Color.BLUE, Color.YELLOW, Color.CYAN, Color.PURPLE, Color.GRAY};
		String[] colorNames = { // Names for the colors, used to construct menu items.
				"Black", "White", "Red", "Green", 
				"Blue", "Yellow", "Cyan", "Purple", "Gray"};
		
		ToggleGroup colorGroup = new ToggleGroup();
		for (int i = 0; i < colors.length; i++) {
			RadioMenuItem item = new RadioMenuItem("Draw with " + colorNames[i]);
			item.setUserData(colors[i]); // Stash the actual color in the menu item's UserData
			item.setToggleGroup(colorGroup);
			colorMenu.getItems().add(item);
			if (i == 0)
				item.setSelected(true);  // Initially selected color is black.
		}
		RadioMenuItem customColor = new RadioMenuItem("Custom Drawing Color...");
		customColor.setToggleGroup(colorGroup);
		customColor.setOnAction( e -> {
			   // For the custom color selection, use a dialog box to get an 
			   // arbitrary color from the user.  This has to be done in an
			   // ActionEvent handler, since it needs to happen even if the user selects
			   // Custom Drawing Color when it is already selected.  (In that case,
			   // the selected toggle does not change.
			customColor.setSelected(true);
			Color c = SimpleDialogs.colorChooser(currentColor, "Select a Color to Use For Drawing");
			if (c != null)  // c is null if user cancels the dialog
				currentColor = c;
		});
		colorMenu.getItems().add(customColor);
		colorGroup.selectedToggleProperty().addListener( e -> {
			   // Sets the color, when one of the standard colors is selected.
			   // This does not handle the Custom Color option.  Note that
			   // when the user chooses a new radio menu item, the selected toggle
			   // first changes to null as the old menu item is deselected, then
			   // changes to the newly selected menu item.
			Toggle t = colorGroup.getSelectedToggle();  // the selected RadioMenuItem
			if (t != null && t != customColor) {
				   // The color associated with this menu item was stashed
				   // in the UserData of the menu item, t.
				currentColor = (Color)t.getUserData();
			}
		});
		colorMenu.getItems().add( new SeparatorMenuItem() );
		MenuItem clear = new MenuItem("Clear to Background Color");
		clear.setOnAction( e -> {  // Fill main canvas with current background color.
			canvasGraphics.setFill(backgroundColor);
			canvasGraphics.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
		});
		colorMenu.getItems().add(clear);
		MenuItem fill = new MenuItem("Fill with Drawing Color");
		fill.setOnAction( e -> {  // Fill main canvas with current drawing color, but
			                      // don't change the background color.  (The erase
			                      // tool will still erase to the (old) background color.)
			canvasGraphics.setFill(currentColor);
			canvasGraphics.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
		});
		colorMenu.getItems().add(fill);
		colorMenu.getItems().add(new SeparatorMenuItem());
		MenuItem setBG = new MenuItem("Fill and Set Background...");
		setBG.setOnAction( e -> {
			     // User can select a new background color from a dialog box.  If the
			     // dialog box is not canceled, the selected color becomes the background
			     // color, and the canvas is filled with that background color.
			Color c = SimpleDialogs.colorChooser(backgroundColor, "Select a Background Color");
			if (c != null) {
				backgroundColor = c;
				canvasGraphics.setFill(c);
				canvasGraphics.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
			}
		});
		colorMenu.getItems().add(setBG);
		
		/* The User selects a drawing tool from the tool menu.  The menu contains
		 * an entry for each available tool.  Tools are represented by RadioMenuItems,
		 * controlled by a ToggleGroup. */
		
		Tool[] tools = { // The available tools in the order they appear in the menu.
				Tool.CURVE, Tool.LINE, Tool.RECT, Tool.OVAL, Tool.FILLED_RECT, 
				Tool.FILLED_OVAL, Tool.SMUDGE, Tool.ERASE };
		String[] toolNames = { // Names for the tools, used as text in the menu items.
				"Curve", "Line", "Rectangle", "Oval", "Filled Rectangle", 
				"Filled Oval", "Smudge", "Erase", };
		
		ToggleGroup toolGroup = new ToggleGroup();
		for (int i = 0; i < tools.length; i++) {
			RadioMenuItem item = new RadioMenuItem(toolNames[i]);
			item.setUserData(tools[i]);  // Stash the actual tool in the menu items' UserData
			item.setToggleGroup(toolGroup);
			toolMenu.getItems().add(item);
			if (i == 0)
				item.setSelected(true);  // Curve tool is initially selected
			if (i == 0 || i == 5)  // Separators before and after the shape tools.
				toolMenu.getItems().add(new SeparatorMenuItem() );
		}
		toolGroup.selectedToggleProperty().addListener( e -> {
			Toggle t = toolGroup.getSelectedToggle();  // The selected RadioMenuItem
			if (t != null)
				currentTool = (Tool)t.getUserData(); // the actual tool was stashed in the UserData.
		});

		return menubar;
		
	} // end makeMenuBar

	
	/**
	 * When the ERASE or SMUDGE tools are used and the mouse jumps
	 * from (x1,y1) to (x2,y2), the tool has to be applied to a
	 * line of pixel positions between the two points in order to
	 * be sure to cover the entire line that the mouse moves along.
	 */
	private void applyToolAlongLine(int x1, int y1, int x2, int y2) {
		int w = (int)canvas.getWidth();   // (for SMUDGE only)
		int h = (int)canvas.getHeight();  // (for SMUDGE only)
		int dist = Math.max(Math.abs(x2-x1),Math.abs(y2-y1));
			// dist is the number of points along the line from
			// (x1,y1) to (x2,y2) at which the tool will be applied.
		double dx = (double)(x2-x1)/dist;
		double dy = (double)(y2-y1)/dist;
		for (int d = 1; d <= dist; d++) {
				// Apply the tool at one of the points (x,y) along the
				// line from (x1,y1) to (x2,y2).
			int x = (int)Math.round(x1 + dx*d);
			int y = (int)Math.round(y1 + dy*d);
			if (currentTool == Tool.ERASE) {
					// Erase a 10-by-10 block of pixels around (x,y)
				canvasGraphics.fillRect(x-5,y-5,10,10);
			}
			else { 
					// For the SMUDGE tool, blend some of the color from
					// the smudgeRed, smudgeGreen, and smudgeBlue arrays
					// into the pixels in a 9-by-9 block around (x,y), and
					// vice versa.  The effect is to smear out the color
					// of pixels that are visited by the tool.
				snapshotParams.setViewport(new Rectangle2D(x-4,y-4,9,9));
				canvas.snapshot(snapshotParams, pixels);
				for (int j = 0; j < 9; j++) {
					int c = x - 4 + j;
					for (int i = 0; i < 9; i++) {
						int r = y - 4 + i;
						if ( r >= 0 && r < h && c >= 0 && c < w && smudgeRed[i][j] != -1) {
							Color oldColor = pixelReader.getColor(j,i);
							double newRed = (oldColor.getRed()*0.8 + smudgeRed[i][j]*0.2);
							double newGreen = (oldColor.getGreen()*0.8 + smudgeGreen[i][j]*0.2);
							double newBlue = (oldColor.getBlue()*0.8 + smudgeBlue[i][j]*0.2);
							pixelWriter.setColor(c, r,Color.color(newRed,newGreen,newBlue));
							smudgeRed[i][j] = oldColor.getRed()*0.2 + smudgeRed[i][j]*0.8;
							smudgeGreen[i][j] = oldColor.getGreen()*0.2 + smudgeGreen[i][j]*0.8;
							smudgeBlue[i][j] = oldColor.getBlue()*0.2 + smudgeBlue[i][j]*0.8;
						}
					}
				}
			}
		}
	} // end applyToolAlongLine


	/**
	 * Start a drag operation. 
	 */
	private void mousePressed(MouseEvent evt) {
		startX = prevX = currentX = (int)evt.getX();
		startY = prevY = currentY = (int)evt.getY();
		dragging = true;
		canvasGraphics.setStroke(currentColor);  // Make sure we are drawing with the right color.
		canvasGraphics.setFill(currentColor);
		overlayGraphics.setStroke(currentColor);
		overlayGraphics.setFill(currentColor);
		if (currentTool == Tool.ERASE) {
				// Erase a 10-by-10 block around the starting mouse position.
			canvasGraphics.setFill(backgroundColor);  // Change the color when using erase.
			canvasGraphics.fillRect(startX-5,startY-5,10,10);
		}
		else if (currentTool == Tool.SMUDGE) {
				// Record the colors in a 9-by-9 block of pixels around the
				// starting mouse position into the arrays smudgeRed, 
				// smudgeGreen, and smudgeBlue.  These arrays hold the
				// red, green, and blue components of the colors.
			if (smudgeRed == null) {
					// Create all variables needed for smudge, if not already done.
				pixels = new WritableImage(9,9);  
				pixelReader = pixels.getPixelReader();
				snapshotParams = new SnapshotParameters();
				smudgeRed = new double[9][9]; 
				smudgeGreen = new double[9][9];
				smudgeBlue = new double[9][9];
				pixelWriter = canvasGraphics.getPixelWriter();
			}
			snapshotParams.setViewport(new Rectangle2D(startX-4,startY-4,9,9));
			canvas.snapshot(snapshotParams, pixels);
			int h = (int)canvas.getHeight();
			int w = (int)canvas.getWidth();
			for (int j = 0; j < 9; j++) {  // row in the snapshot
				int r = startY + j - 4;  // the corresponding row in the canvas
				for (int i = 0; i < 9; i++) {  // column in the snapshot
					int c = startX + i - 4;  // the corresponding column in canvas
					if (r < 0 || r >= h || c < 0 || c >= w) {
						    // The point (i,j) is outside the canvas.
							// A -1 in the smudgeRed array indicates that the
							// corresponding pixel was outside the canvas.
						smudgeRed[j][i] = -1;
					}
					else {
						Color color = pixelReader.getColor(i, j); // get color from snapshot
						smudgeRed[j][i] = color.getRed();
						smudgeGreen[j][i] = color.getGreen();
						smudgeBlue[j][i] = color.getBlue();
					}
				}
			}
		}
	}

	
	/**
	 * Continue a drag operation when the user drags the mouse.
	 * For the CURVE tool, a line is drawn from the previous mouse
	 * position to the current mouse position in the main canvas.
	 * For shape tools like LINE and FILLED_RECT, the shape is drawn
	 * to the overlay canvas after first clearing the overlay canvas.  
	 * For the SMUDGE and ERASE tools, the tool is applied along a 
	 * line from the previous mouse position to the current position,
	 * on the main canvas.
	 */
	private void mouseDragged(MouseEvent evt) {
		if (!dragging)
			return;
		currentX = (int)evt.getX();
		currentY = (int)evt.getY();
		if (currentTool == Tool.CURVE) {
			canvasGraphics.strokeLine(prevX,prevY,currentX,currentY);
		}
		else if (currentTool == Tool.ERASE || currentTool == Tool.SMUDGE) {
			applyToolAlongLine(prevX,prevY,currentX,currentY);
		}
		else  {  // tool is a shape that has to be drawn to overlay canvas
			overlayGraphics.clearRect(0,0,overlay.getWidth(),overlay.getHeight());
			putCurrentShape(overlayGraphics);
		}
		prevX = currentX;
		prevY = currentY;
	}

	
	/**
	 * Finish a mouse drag operation.  Nothing is done unless the current tool
	 * is a shape tool.  For shape tools, the user's shape is drawn to the
	 * main canvas, making it a permanent part of the picture, and
	 * the overlay canvas, which was used for the shape during dragging,
	 * is cleared.
	 */
	private void mouseReleased(MouseEvent evt) {
		dragging = false;
		if (currentTool != Tool.CURVE && 
				currentTool != Tool.ERASE && currentTool != Tool.SMUDGE) {
			putCurrentShape(canvasGraphics);
			overlayGraphics.clearRect(0,0,overlay.getWidth(),overlay.getHeight());
		}
	}
	
	
	/**
	 * Reads an image from a file and draws it to the canvas,
	 * scaling it so it fills the canvas.
	 */
	private void doOpenImage() {
		FileChooser fileDialog = new FileChooser(); 
		fileDialog.setInitialFileName("");
		fileDialog.setInitialDirectory(
				               new File( System.getProperty("user.home") ) );
		fileDialog.setTitle("Select Image File to Load");
		File selectedFile = fileDialog.showOpenDialog(window);
		if ( selectedFile == null )
			return;  // User did not select a file.
		Image image = new Image("file:" + selectedFile);
		if (image.isError()) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR,
				    "Sorry, an error occurred while\ntrying to load the file:\n"
					     + image.getException().getMessage());
			errorAlert.showAndWait();
			return;
		}
		canvasGraphics.drawImage(image,0,0,canvas.getWidth(),canvas.getHeight());
	}

	
	/**
	 * Saves the user's sketch as an image file in PNG format.
	 */
	private void doSaveImage() {
		FileChooser fileDialog = new FileChooser(); 
		fileDialog.setInitialFileName("imagefile.png");
		fileDialog.setInitialDirectory(
				         new File( System.getProperty("user.home") ) );
		fileDialog.setTitle("Select File to Save. Name MUST end with .png!");
		File selectedFile = fileDialog.showSaveDialog(window);
		if ( selectedFile == null )
			return;  // User did not select a file.
		try {
			Image canvasImage = canvas.snapshot(null,null);
			BufferedImage image = SwingFXUtils.fromFXImage(canvasImage,null);
		    String filename = selectedFile.getName().toLowerCase();
		    if ( ! filename.endsWith(".png")) {
		    	throw new Exception("The file name must end with \".png\".");
		    }
			boolean hasFormat = ImageIO.write(image,"PNG",selectedFile);
			if ( ! hasFormat ) { // (this should never happen)
				throw new Exception( "PNG format not available.");
			}
		}
		catch (Exception e) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR,
				   "Sorry, an error occurred while\ntrying to save the image:\n"
					     + e.getMessage());
			errorAlert.showAndWait();
		}	
	}
	


} // end class ToolPaint

