
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

/**
 *  A very simple drawing program that lets the user add shapes to a drawing
 *  area and drag them around.  An abstract Shape class is used to represent
 *  shapes in general, with subclasses to represent particular kinds of shape.
 *  (These are implemented as nested classes inside the main class.)  This
 *  program is an illustration of class hierarchy, inheritance, polymorphism,
 *  and abstract classes.  Note that this program will fail if you add more
 *  than 500 shapes, since it uses an array of length 500 to store the shapes.
 *  (Note: This program draws on a JavaFX canvas, which is not the best way
 *  to draw shapes in JavaFX.  In fact, JavaFX has its own shape classes that
 *  could be used more naturally.)
 */
public class ShapeDraw extends Application {

	private Shape[] shapes = new Shape[500];  // Contains shapes the user has drawn.
	private int shapeCount = 0; // Number of shapes that the user has drawn.
	private Canvas canvas; // The drawing area where the user draws.
	private Color currentColor = Color.RED;  // Color to be used for new shapes.

	/**
	 * A main routine that simply runs this application.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	//--------------------- Methods for creating the GUI -------------------------
	
	/**
	 * This method is required for any JavaFX Application.  It adds content to
	 * the window (given by the parameter, stage) and shows the window.
	 */
	public void start(Stage stage) {
		canvas = makeCanvas();
		paintCanvas();
		StackPane canvasHolder = new StackPane(canvas);
		canvasHolder.setStyle("-fx-border-width: 2px; -fx-border-color: #444");
		BorderPane root = new BorderPane(canvasHolder);
		root.setStyle("-fx-border-width: 1px; -fx-border-color: black");
		root.setBottom(makeToolPanel(canvas));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Click buttons to add shapes; drag shapes with your mouse"); 
		stage.setResizable(false);
		stage.show();
	}

	private Canvas makeCanvas() {
		    // Creates a canvas, and add mouse listeners to implement dragging.
		    // The listeners are given by methods that are defined below.
		Canvas canvas = new Canvas(800,600);
		canvas.setOnMousePressed( this::mousePressed );
		canvas.setOnMouseReleased( this::mouseReleased );
		canvas.setOnMouseDragged( this::mouseDragged );
		return canvas;
	}

	private HBox makeToolPanel(Canvas canvas) {
		    // Make a pane containing the buttons that are used to add shapes
		    // and the pop-up menu for selecting the current color.
		Button ovalButton = new Button("Add an Oval");
		ovalButton.setOnAction( (e) -> addShape( new OvalShape() ) );
		Button rectButton = new Button("Add a Rect");
		rectButton.setOnAction( (e) -> addShape( new RectShape() ) );
		Button roundRectButton = new Button("Add a RoundRect");
		roundRectButton.setOnAction( (e) -> addShape( new RoundRectShape() ) );
		ComboBox<String> combobox = new ComboBox<>();
		combobox.setEditable(false);
		Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, 
				Color.MAGENTA, Color.YELLOW, Color.BLACK, Color.WHITE };
		String[] colorNames = { "Red", "Green", "Blue", "Cyan", 
				"Magenta", "Yellow", "Black", "White" };
		combobox.getItems().addAll(colorNames);
		combobox.setValue("Red");
		combobox.setOnAction( 
				e -> currentColor = colors[combobox.getSelectionModel().getSelectedIndex()] );		
		HBox tools = new HBox(10);
		tools.getChildren().add(ovalButton);
		tools.getChildren().add(rectButton);
		tools.getChildren().add(roundRectButton);
		tools.getChildren().add(combobox);
		tools.setStyle("-fx-border-width: 5px; -fx-border-color: transparent; -fx-background-color: lightgray");
		return tools;
	}

	private void paintCanvas() {
			// Redraw the shapes.  The entire list of shapes
			// is redrawn whenever the user adds a new shape
			// or moves an existing shape.
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.setFill(Color.WHITE); // Fill with white background.
		g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
		for (int i = 0; i < shapeCount; i++) {
			Shape s = shapes[i];
			s.draw(g);
		}
	}

	private void addShape(Shape shape) {
			// Add the shape to the canvas, and set its size/position and color.
			// The shape is added at the top-left corner, with size 80-by-50.
			// Then redraw the canvas to show the newly added shape.  This method
		    // is used in the event listeners for the buttons in makeToolsPanel().
		shape.setColor(currentColor);
		shape.reshape(10,10,150,100);
		shapes[shapeCount] = shape;
		shapeCount++;
		paintCanvas();
	}

	
	// ------------ This part of the class defines methods to implement dragging -----------
	// -------------- These methods are added to the canvas as event listeners -------------

	private Shape shapeBeingDragged = null;  // This is null unless a shape is being dragged.
	                                         // A non-null value is used as a signal that dragging
	                                         // is in progress, as well as indicating which shape
	                                         // is being dragged.

	private int prevDragX;  // During dragging, these record the x and y coordinates of the
	private int prevDragY;  //    previous position of the mouse.

	private void mousePressed(MouseEvent evt) {
			// User has pressed the mouse.  Find the shape that the user has clicked on, if
			// any.  If there is a shape at the position when the mouse was clicked, then
			// start dragging it.  If the user was holding down the shift key, then bring
			// the dragged shape to the front, in front of all the other shapes.
		int x = (int)evt.getX();  // x-coordinate of point where mouse was clicked
		int y = (int)evt.getY();  // y-coordinate of point 
		for ( int i = shapeCount - 1; i >= 0; i-- ) {  // check shapes from front to back
			Shape s = shapes[i];
			if (s.containsPoint(x,y)) {
				shapeBeingDragged = s;
				prevDragX = x;
				prevDragY = y;
				if (evt.isShiftDown()) { // s should be moved on top of all the other shapes
					for (int j = i; j < shapeCount-1; j++) {
						    // move the shapes following s down in the list
						shapes[j] = shapes[j+1];
					}
					shapes[shapeCount-1] = s;  // put s at the end of the list
					paintCanvas();  // repaint canvas to show s in front of other shapes
				}
				return;
			}
		}
	}

	private void mouseDragged(MouseEvent evt) {
			// User has moved the mouse.  Move the dragged shape by the same amount.
		int x = (int)evt.getX();
		int y = (int)evt.getY();
		if (shapeBeingDragged != null) {
			shapeBeingDragged.moveBy(x - prevDragX, y - prevDragY);
			prevDragX = x;
			prevDragY = y;
			paintCanvas();      // redraw canvas to show shape in new position
		}
	}

	private void mouseReleased(MouseEvent evt) {
			// User has released the mouse.  Move the dragged shape, then set
			// shapeBeingDragged to null to indicate that dragging is over.
		shapeBeingDragged = null;
	}


	
	// ------- Nested class definitions for the abstract Shape class and three -----
	// -------------------- concrete subclasses of Shape. --------------------------

	static abstract class Shape {

			// A class representing shapes that can be displayed on a ShapeCanvas.
			// The subclasses of this class represent particular types of shapes.
			// When a shape is first constructed, it has height and width zero
			// and a default color of white.

		int left, top;      // Position of top left corner of rectangle that bounds this shape.
		int width, height;  // Size of the bounding rectangle.
		Color color = Color.WHITE;  // Color of this shape.

		void reshape(int left, int top, int width, int height) {
			// Set the position and size of this shape.
			this.left = left;
			this.top = top;
			this.width = width;
			this.height = height;
		}

		void moveBy(int dx, int dy) {
				// Move the shape by dx pixels horizontally and dy pixels vertically
				// (by changing the position of the top-left corner of the shape).
			left += dx;
			top += dy;
		}

		void setColor(Color color) {
				// Set the color of this shape
			this.color = color;
		}

		boolean containsPoint(int x, int y) {
				// Check whether the shape contains the point (x,y).
				// By default, this just checks whether (x,y) is inside the
				// rectangle that bounds the shape.  This method should be
				// overridden by a subclass if the default behavior is not
				// appropriate for the subclass.
			if (x >= left && x < left+width && y >= top && y < top+height)
				return true;
			else
				return false;
		}

		abstract void draw(GraphicsContext g);  
			// Draw the shape in the graphics context g.
			// This must be overriden in any concrete subclass.

	}  // end of class Shape



	static class RectShape extends Shape {
			// This class represents rectangle shapes.
		void draw(GraphicsContext g) {
			g.setFill(color);
			g.fillRect(left,top,width,height);
			g.setStroke(Color.BLACK);
			g.strokeRect(left,top,width,height);
		}
	}


	static class OvalShape extends Shape {
			// This class represents oval shapes.
		void draw(GraphicsContext g) {
			g.setFill(color);
			g.fillOval(left,top,width,height);
			g.setStroke(Color.BLACK);
			g.strokeOval(left,top,width,height);
		}
		boolean containsPoint(int x, int y) {
				// Check whether (x,y) is inside this oval, using the
				// mathematical equation of an ellipse.  This replaces the
				// definition of containsPoint that was inherited from the
				// Shape class.
			double rx = width/2.0;   // horizontal radius of ellipse
			double ry = height/2.0;  // vertical radius of ellipse 
			double cx = left + rx;   // x-coord of center of ellipse
			double cy = top + ry;    // y-coord of center of ellipse
			if ( (ry*(x-cx))*(ry*(x-cx)) + (rx*(y-cy))*(rx*(y-cy)) <= rx*rx*ry*ry )
				return true;
			else
				return false;
		}
	}


	static class RoundRectShape extends Shape {
			// This class represents rectangle shapes with rounded corners.
			// (Note that it uses the inherited version of the 
			// containsPoint(x,y) method, even though that is not perfectly
			// accurate when (x,y) is near one of the corners.)
		void draw(GraphicsContext g) {
			g.setFill(color);
			g.fillRoundRect(left,top,width,height,width/3,height/3);
			g.setStroke(Color.BLACK);
			g.strokeRoundRect(left,top,width,height,width/3,height/3);
		}
	}


}  // end class ShapeDraw

