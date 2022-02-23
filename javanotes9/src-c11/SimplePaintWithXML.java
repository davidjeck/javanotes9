

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.geometry.Point2D;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.*;
import java.util.ArrayList;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * SimplePaintWithXML is a drawing program in which the user can
 * sketch curves.  The user's work can be saved to a file which can later
 * be reopened and edited.  It is also possible to save the user's
 * picture as an image file.
 */
public class SimplePaintWithXML extends Application {

	
	public static void main(String[] args) {
		launch(args);
	}
	//--------------------------------------------------------------------


	/**
	 * An object of type CurveData represents the data required to redraw one
	 * of the curves that have been sketched by the user.
	 */
	private static class CurveData {
		Color color;  // The color of the curve.
		boolean symmetric;  // Are horizontal and vertical reflections also drawn?
		ArrayList<Point2D> points;  // The points on the curve.
	}


	private ArrayList<CurveData> curves;  // A list of all curves in the picture.
	
	private Stage window;     // The program's window, used to set the window title
	
	private Canvas canvas;       // The canvas on which curves are drawn.
	private GraphicsContext g;   // A graphics context for drawing on the canvas
	
	private Color backgroundColor;  // The current background color of the canvas

	private Color currentColor;   // When a curve is created, its color is taken
								  //     from this variable.  The value is changed
								  //     using commands in the "Color" menu.

	private boolean useSymmetry;  // When a curve is created, its "symmetric"
								  // property is copied from this variable.  Its
								  // value is set by the "Use Symmetry" command in
								  // the "Control" menu.
	private CheckMenuItem useSymmetryCheck;  // The checkbox controlling useSymmetry
	
	private File editFile;  // The file that is being edited.  Set when user opens
	                        // or saves a file.  Value is null if no file is being edited. 


	/**
	 *  Sets up the GUI with a canvas for drawing and a menu bar.
	 *  Also initializes global variables, and installs mouse event 
	 *  handlers to respond when the user drags the mouse on the canvas.
	 */
	public void start(Stage stage) {
		
		window = stage;
		
		currentColor = Color.BLACK;
		backgroundColor = Color.WHITE;
		curves = new ArrayList<>();
		
		canvas = new Canvas(600,600);
		g = canvas.getGraphicsContext2D();
		redraw();  // just fills canvas with background color
		Pane canvasHolder = new Pane(canvas); // for adding a border around the canvas
		canvasHolder.setStyle("-fx-border-color:darkgray; -fx-border-width:3px");
		canvas.relocate(3,3); // Since the holder is a Pane, we have to set the
		                      // canvas location manually, to allow for the
		                      // border.  Otherwise, canvas would be at (0,0).
		
		canvas.setOnMousePressed( e -> mousePressed(e) );
		canvas.setOnMouseDragged( e -> mouseDragged(e) );
		canvas.setOnMouseReleased( e -> mouseReleased(e) );
		
		BorderPane root = new BorderPane();
		root.setCenter( canvasHolder );
		root.setTop( createMenuBar() );
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Simple Paint: Untitled");
		stage.setResizable(false);
		stage.show();
		
	} // end start()
	

	/**
	 * Fills the panel with the current background color and draws all the
	 * curves that have been sketched by the user.  This is called when
	 * the picture has to be completely redrawn such as when the
	 * background color changes or when an Undo command is applied.
	 */
	private void redraw() {
		g.setFill(backgroundColor);
		g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
		for ( CurveData curve : curves) {
			g.setStroke(curve.color);
			for (int i = 1; i < curve.points.size(); i++) {
					// Draw a line segment from point number i-1 to point number i.
				double x1 = curve.points.get(i-1).getX();
				double y1 = curve.points.get(i-1).getY();
				double x2 = curve.points.get(i).getX();
				double y2 = curve.points.get(i).getY();
				drawSegment(curve.symmetric,x1,y1,x2,y2);
			}
		}
	} // end redraw()
	
	
	/**
	 * Strokes a line segment, using the current drawing color from (x1,y1) to (x2,y2).
	 * If symmetric is true, also draws the horizontal and vertical reflections
	 * of that segment.  This is called by redraw() and also when the mouse moves
	 * during a drag operation on the canvas.
	 */
	private void drawSegment(boolean symmetric, double x1, double y1, double x2, double y2) {
		g.strokeLine(x1,y1,x2,y2);
		if (symmetric) {
				// Also draw the horizontal and vertical reflections
				// of the line segment.
			double w = canvas.getWidth();
			double h = canvas.getHeight();
			g.strokeLine(w-x1,y1,w-x2,y2);
			g.strokeLine(x1,h-y1,x2,h-y2);
			g.strokeLine(w-x1,h-y1,w-x2,h-y2);
		}
	}


	//------------------- implement mouse dragging -------------------------------

	private CurveData currentCurve;  // During a drag, the curve that is being drawn
	private boolean dragging;  // Is a drag in progress?
	
	/**
	 * Called when the user presses the mouse on the canvas.  A new CurveData object
	 * is created to hold the points on the curve that the user is drawing.
	 * and the point where the mouse was pressed is added as the first point on
	 * the curve.  The color and symmetry property of the curve are taken from the
	 * current values of global variables currentColor and useSymmetry.  The
	 * new curve is not actually added to the list of curves until the mouse is
	 * released.
	 */
	private void mousePressed(MouseEvent evt) {
		if (dragging)
			return;
		dragging = true;
		currentCurve = new CurveData();
		currentCurve.color = currentColor;
		currentCurve.symmetric = useSymmetry;
		currentCurve.points = new ArrayList<>();
		currentCurve.points.add( new Point2D(evt.getX()+0.5, evt.getY()+0.5) );
		g.setStroke(currentColor); // set currentColor to be used for drawing this curve
	}
	
	/**
	 * Called when the mouse moves during a drag operation.  Adds a point to
	 * the curve and draws a line segment from the previous point to the current
	 * point.
	 */
	private void mouseDragged(MouseEvent evt) {
		if (!dragging)
			return;
		Point2D currentPoint = new Point2D( evt.getX()+0.5, evt.getY()+0.5 );
		Point2D prevPoint = currentCurve.points.get(currentCurve.points.size() - 1);
		currentCurve.points.add( currentPoint );
		drawSegment(useSymmetry, prevPoint.getX(), prevPoint.getY(), 
				            currentPoint.getX(), currentPoint.getY());
	}
	
	/**
	 * Called when the user releases the mouse.  The current curve is added to
	 * the list of curves, but only if the number of points is at least 2.
	 * (If there is only one point, it means that the user didn't move the
	 * mouse at all, and no curve was actually drawn.  In that case, the
	 * currentCurve object should simply be discarded.)
	 */
	private void mouseReleased(MouseEvent evt) {
		if (!dragging)
			return;
		dragging = false;
		if (currentCurve.points.size() > 1)
			curves.add(currentCurve);
		currentCurve = null;
	}
	

	//------------------------ implement menus -----------------------------
	
	private static final String[] colorNames = {  
		// List of available color names for the Color and BackgroudColor menus.
			"Black", "White", "Red", "Green", "Blue", 
			"Cyan", "Magenta", "Yellow", "Gray", "Brown", 
			"Purple", "Pink", "Orange"
	};
	
	private static final Color[] colors = {
		// List of Colors corresponding to the names in the colorNames array.
			Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, 
			Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.GRAY, Color.BROWN,
			Color.PURPLE, Color.PINK, Color.ORANGE
	};

	/**
	 * Creates a menu bar for use with this panel.  It contains
	 * four menus: "File", "Control", "Color", and "BackgroundColor".
	 */
	public MenuBar createMenuBar() {

		/* Create the menu bar object */

		MenuBar menuBar = new MenuBar();

		/* Create the menus and add them to the menu bar. */

		Menu fileMenu = new Menu("File");
		Menu controlMenu = new Menu("Control");
		Menu colorMenu = new Menu("Color");
		Menu bgColorMenu = new Menu("BackgroundColor");
		menuBar.getMenus().addAll(fileMenu,controlMenu,colorMenu,bgColorMenu);

		/* Add commands to the "File" menu.  It contains commands
		 * for saving and for opening a file.  It also contains
		 * a command for saving the user's picture as a PNG file and
		 * a command for quitting the program.
		 */

		MenuItem newCommand = new MenuItem("New");
		fileMenu.getItems().add(newCommand);
		newCommand.setOnAction( e -> doNew() );
		fileMenu.getItems().add( new SeparatorMenuItem() );
		MenuItem saveText = new MenuItem("Save (XML Format)...");
		fileMenu.getItems().add(saveText);
		saveText.setOnAction( e ->  doSaveAsText() );
		MenuItem openText = new MenuItem("Open (XML Format)...");
		fileMenu.getItems().add(openText);
		openText.setOnAction( e -> doOpenAsText() );
		fileMenu.getItems().add( new SeparatorMenuItem() );
		MenuItem saveImage = new MenuItem("Save Image...");
		fileMenu.getItems().add(saveImage);
		saveImage.setOnAction( e -> doSaveImage() );
		fileMenu.getItems().add( new SeparatorMenuItem() );
		MenuItem quitCommand = new MenuItem("Quit");
		fileMenu.getItems().add(quitCommand);
		quitCommand.setOnAction( e -> System.exit(0) );

		/* Add commands to the "Control" menu.  It contains an Undo
		 * command that will remove the most recently drawn curve
		 * from the list of curves; a "Clear" command that removes
		 * all the curves that have been drawn; and a "Use Symmetry"
		 * checkbox that determines whether symmetry should be used. */

		MenuItem undo = new MenuItem("Undo");
		undo.setOnAction( e -> {
				if (curves.size() > 0) {
					curves.remove( curves.size() - 1);
					redraw();  // Redraw without the curve that has been removed.
				}
		});
		
		MenuItem clear = new MenuItem("Clear");
		clear.setOnAction( e -> {
				curves = new ArrayList<CurveData>();
				redraw();  // Redraw with no curves shown.
		});
		
		useSymmetryCheck = new CheckMenuItem("Use Symmetry");
		useSymmetryCheck.setOnAction( e -> useSymmetry = useSymmetryCheck.isSelected() );
		
		controlMenu.getItems().addAll(undo,clear,useSymmetryCheck);

		/* Add commands to the "Color" menu.  The menu contains commands for
		 * setting the current drawing color.  When the user chooses one of these
		 * commands, it has no immediate effect on the drawing.  It just sets
		 * the color that will be used for future drawing.  */

		ToggleGroup colorGroup = new ToggleGroup();
		for (int i = 0; i < colorNames.length; i++) {
			RadioMenuItem item = new RadioMenuItem(colorNames[i]);
			colorMenu.getItems().add(item);
			item.setUserData(Integer.valueOf(i));
			item.setToggleGroup(colorGroup);
			if (i == 0) {
				item.setSelected(true);
			}
		}
		colorGroup.selectedToggleProperty().addListener( (e,oldVal,newVal) -> {
			if (newVal != null) {
				   // When the user selects a new RadioMenuItem from the group,
				   // the selectedToggle property changes twice, once to null,
				   // then to the newly selected RadioMenuItem.
				   //    The "userData" property of a Node is a place where
				   // a program can stash data associated with the node
				   // that will be needed later in the program.  It can be
				   // any object.  Here, I use it to stash the color number
				   // associated with the RadioMenuItem so I know which
				   // color to use.  The value is an Integer, which is
				   // automatically "unboxed" to an int when used here as
				   // an array index.
				currentColor = colors[ (Integer)newVal.getUserData() ];
			}
		});

		/* Add commands to the "BackgroundColor" menu.  The menu contains commands
		 * for setting the background color of the panel.  When the user chooses
		 * one of these commands, the panel is immediately redrawn with the new
		 * background color.  Any curves that have been drawn are still there. */

		ToggleGroup bgGroup = new ToggleGroup();
		for (int i = 0; i < colorNames.length; i++) {
			RadioMenuItem item = new RadioMenuItem(colorNames[i]);
			bgColorMenu.getItems().add(item);
			item.setUserData(Integer.valueOf(i));
			item.setToggleGroup(bgGroup);
			if (i == 1) {
				item.setSelected(true);
			}
		}
		bgGroup.selectedToggleProperty().addListener( (e,oldVal,newVal) -> {
			if (newVal != null) {
				backgroundColor = colors[ (Integer)newVal.getUserData() ];
				redraw(); // picture has to be redrawn with new background color
			}
		});

		/* Return the menu bar that has been constructed. */

		return menuBar;

	} // end createMenuBar


	/**
	 * Clear the current image and restore the initial state for
	 * current color, background color, and symmetry.
	 */
	private void doNew() {
		curves = new ArrayList<CurveData>();
		backgroundColor = Color.WHITE;
		useSymmetry = false;
		useSymmetryCheck.setSelected(false);
		currentColor = Color.BLACK;
		window.setTitle("SimplePaint: Untitled");
		editFile = null;
		redraw();
	}


	/**
	 * Save the user's image to a file in human-readable text format.
	 * Files created by this method can be read back into the program
	 * using the doOpenAsText() method.
	 */
	private void doSaveAsText() {
		FileChooser fileDialog = new FileChooser(); 
		if (editFile == null) {
			// No file is being edited.  Set file name in dialog to "filename.txt"
			// and set the directory in the dialog to the user's home directory.
			fileDialog.setInitialFileName("filename.xml");
			fileDialog.setInitialDirectory( new File( System.getProperty("user.home")));
		}
		else {
			// Get the file name and directory for the dialog from
			// the file that is currently being edited.
			fileDialog.setInitialFileName(editFile.getName());
			fileDialog.setInitialDirectory(editFile.getParentFile());
		}
		fileDialog.setTitle("Select File to be Saved");
		File selectedFile = fileDialog.showSaveDialog(window);
		if ( selectedFile == null )
			return;  // User did not select a file.
		// Note: User has selected a file AND if the file exists has
		//    confirmed that it is OK to erase the exiting file.
		PrintWriter out; 
		try {
			FileWriter stream = new FileWriter(selectedFile); 
			out = new PrintWriter( stream );
		}
		catch (Exception e) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR,
					"Sorry, but an error occurred\nwhile trying to open the file\nfor writing.");
			errorAlert.showAndWait();
			return;
		}
		try {
			out.println("<?xml version=\"1.0\"?>");
			out.println("<simplepaint version=\"1.0\">");
			out.println("   <background red='" + backgroundColor.getRed() + "' green='" +
					backgroundColor.getGreen() + "' blue='" + backgroundColor.getBlue() + "'/>");
			for (CurveData c : curves) {
				out.println("   <curve>");
				out.println("      <color red='" + c.color.getRed() + "' green='" +
						c.color.getGreen() + "' blue='" + c.color.getBlue() + "'/>");
				out.println("      <symmetric>" + c.symmetric + "</symmetric>");
				for (Point2D pt : c.points)
					out.println("      <point x='" + pt.getX() + "' y='" + pt.getY() + "'/>");
				out.println("   </curve>");
			}
			out.println("</simplepaint>");
			out.flush();
			out.close();
			editFile = selectedFile;
			window.setTitle("SimplePaint: " + editFile.getName());
			if (out.checkError())
				throw new IOException("Output error.");
			editFile = selectedFile;
			window.setTitle("SimplePaint: " + editFile.getName());
		}
		catch (Exception e) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR,
					"Sorry, but an error occurred while\ntrying to write data to the file:");
			errorAlert.showAndWait();
		}
	}

	/**
	 * Read image data from a file into the drawing area.  The format
	 * of the file must be the same as that used in the doSaveAsText()
	 * method.
	 */
	private void doOpenAsText() {
		FileChooser fileDialog = new FileChooser();
		fileDialog.setTitle("Select File to be Opened");
		fileDialog.setInitialFileName(null);  // No file is initially selected.
		if (editFile == null)
			fileDialog.setInitialDirectory(new File(System.getProperty("user.home")));
		else
			fileDialog.setInitialDirectory(editFile.getParentFile());
		File selectedFile = fileDialog.showOpenDialog(window);
		if (selectedFile == null)
			return;  // User canceled.
        Document xmldoc;
        try {
            DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xmldoc = docReader.parse(selectedFile);
        }
		catch (Exception e) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR,
					"Sorry, but an error occurred\nwhile trying to open the file:\n" + e);
			errorAlert.showAndWait();
			return;
		}
		try {
			Color newBackground = Color.WHITE;
			ArrayList<CurveData> newCurves = new ArrayList<>();
			Element rootElement = xmldoc.getDocumentElement();
			if ( ! rootElement.getNodeName().equals("simplepaint") )
				throw new Exception("File is not a SimplePaint file.");
			String version = rootElement.getAttribute("version");
			try {
				double versionNumber = Double.parseDouble(version);
				if (versionNumber > 1.0)
					throw new Exception("File requires a newer version of SimplePaint.");
			}
			catch (NumberFormatException e) {
			}
			NodeList nodes = rootElement.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i) instanceof Element) {
					Element element = (Element)nodes.item(i);
					if (element.getTagName().equals("background")) {
						double r = Double.parseDouble(element.getAttribute("red"));
						double g = Double.parseDouble(element.getAttribute("green"));
						double b = Double.parseDouble(element.getAttribute("blue"));
						newBackground = Color.color(r,g,b);
					}
					else if (element.getTagName().equals("curve")) {
						CurveData curve = new CurveData();
						curve.color = Color.BLACK;
						curve.points = new ArrayList<>();
						newCurves.add(curve);
						NodeList curveNodes = element.getChildNodes();
						for (int j = 0; j < curveNodes.getLength(); j++) {
							if (curveNodes.item(j) instanceof Element) {
								Element curveElement = (Element)curveNodes.item(j);
								if (curveElement.getTagName().equals("color")) {
									double r = Double.parseDouble(curveElement.getAttribute("red"));
									double g = Double.parseDouble(curveElement.getAttribute("green"));
									double b = Double.parseDouble(curveElement.getAttribute("blue"));
									curve.color = Color.color(r,g,b);
								}
								else if (curveElement.getTagName().equals("point")) {
									double x = Double.parseDouble(curveElement.getAttribute("x"));
									double y = Double.parseDouble(curveElement.getAttribute("y"));
									curve.points.add(new Point2D(x,y));
								}
								else if (curveElement.getTagName().equals("symmetric")) {
									String content = curveElement.getTextContent();
									if (content.equals("true"))
										curve.symmetric = true;
								}
							}
						}
					}
				}			
			}
			backgroundColor = newBackground;
			curves = newCurves;
			redraw();
			editFile = selectedFile;
			window.setTitle("SimplePaint: " + editFile.getName());
		}
		catch (Exception e) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR,
					"Sorry, but an error occurred while\ntrying to read the data:\n" + e);
			errorAlert.showAndWait();
		}	
	}


	/**
	 * Saves the user's sketch as an image file in PNG format.
	 */
	private void doSaveImage() {
		FileChooser fileDialog = new FileChooser(); 
		fileDialog.setInitialFileName("imagefile.png");
		if (editFile == null) {
			fileDialog.setInitialDirectory( new File( System.getProperty("user.home")));
		}
		else {
			fileDialog.setInitialDirectory(editFile.getParentFile());
		}
		fileDialog.setTitle("Select File to be Saved");
		File selectedFile = fileDialog.showSaveDialog(window);
		if ( selectedFile == null )
			return;  // User did not select a file.
		try {
			Image canvasImage = canvas.snapshot(null,null);
			BufferedImage image = SwingFXUtils.fromFXImage(canvasImage,null);
			boolean hasPNG = ImageIO.write(image,"PNG",selectedFile);
			if ( ! hasPNG )
				throw new Exception("PNG format not available.");
		}
		catch (Exception e) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR,
					"Sorry, but an error occurred while\ntrying to save the image:\n"
					     + e.getMessage());
			errorAlert.showAndWait();
		}	
	}

} // end SimplePaintWithXML


