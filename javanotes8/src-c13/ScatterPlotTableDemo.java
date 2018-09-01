
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;
import javafx.scene.transform.Affine;
import javafx.scene.input.MouseEvent;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;


/**
 * Demonstrates the use of an editable table.  The program lets the 
 * user enter (x,y) coordinates of some points, and it draws a simple
 * scatter plot of all the points in the table for which both the
 * x and the y coordinate are defined.  The user can also click the 
 * canvas to add a new point.
 */
public class ScatterPlotTableDemo extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//-----------------------------------------------------------------------


	private TableView<Point> table;        // The table where the points are input.
	private Canvas canvas;                 // The canvas where the scatter plot is drawn.
	private ObservableList<Point> points;  // The points from the table.  Each element of
	                                       //   this list corresponds to a row in the table.
	
	private Affine canvasTransform;  // The transform from pixel coords to the coords
	                                 //   that were used for drawing the canvas;  this
	                                 //   is computed in redrawDisplay() and is used 
	                                 //   in canvasClicked() to transform the mouse coords.
	
	/**
	 * Set up the GUI and events.
	 */
	public void start(Stage stage) {
	
		/* Create the table, and get the ObservableList of rows from the table.
		 * The listener for the list redraws the canvas whenever a point is added
		 * to the list or deleted from the list (but it's not called if a point
		 * that is already in the list is modified). */
		
		table = new TableView<Point>();
		points = table.getItems();
		
		for (int i = 0; i < 5; i++) { // add 5 random points to the table
			points.add( new Point(5*Math.random(), 5*Math.random()) );
		}
		points.addListener( (Observable e) -> redrawDisplay() );
		
		/* Configure the table and set up a listener on the editingCellProperty of
		 * the table.  This property is the cell that is currently being edited,
		 * or is null when no cell is being edited.  When the value changes to
		 * null, it means that the user has just finished editing a cell.  Since
		 * the value in the cell might have been changed, the canvas should be
		 * redrawn. */
		
		table.setPrefSize(225,100);
		table.setEditable(true);
		table.editingCellProperty().addListener( (o,oldVal,newVal) -> {
			if (newVal == null) {
				redrawDisplay();
			}
		});
		
		/* A StringConverter for use in the table columns, for converting between
		 * the real numbers in the list of points and their string representation
		 * in the table. */
		
		StringConverter<Double> myConverter = new StringConverter<Double>() {
			    // This custom string converter will convert a bad input string to
			    // Double.NaN, instead of just failing.  And it will display an NaN 
			    // value as "Bad Value" and an empty string value as zero.
			public Double fromString(String s) {
				if (s == null || s.trim().length() == 0)
					return 0.0;
				try {
					return Double.parseDouble(s);
				}
				catch (NumberFormatException e) {
					return Double.NaN;
				}
			}
			public String toString(Double n) {
				if (n == null || n.isNaN())
					return "Bad Value";
				return String.format("%1.4g", n.doubleValue());
			}
		};
	
		/* Configure the table columns, one to show the x coords and one to
		 * show the y coords of points from the table.  Turn off sorting and
		 * resizing of table by the user. */
		
		TableColumn<Point, Double> xColumn = new TableColumn<>("X Coord");
		xColumn.setCellValueFactory( new PropertyValueFactory<Point, Double>("x") );
		xColumn.setCellFactory( TextFieldTableCell.forTableColumn(myConverter) );
		xColumn.setSortable(false);
		xColumn.setResizable(false);
		xColumn.setPrefWidth(100);
		table.getColumns().add(xColumn);
		
		TableColumn<Point, Double> yColumn = new TableColumn<>("Y Coord");
		yColumn.setCellValueFactory( new PropertyValueFactory<Point, Double>("y") );
		yColumn.setCellFactory( TextFieldTableCell.forTableColumn(myConverter) );
		yColumn.setSortable(false);
		yColumn.setResizable(false);
		yColumn.setPrefWidth(100);
		table.getColumns().add(yColumn);
		
		/* Create buttons for adding and deleting points.  These are shown
		 * in the UI below the table. */
		
		Button deleteButton = new Button("Delete Selected");
		deleteButton.setOnAction( e -> {
			int selected = table.getSelectionModel().getSelectedIndex();
			if (selected >= 0)
				points.remove( table.getSelectionModel().getSelectedIndex());
		});
		deleteButton.setMaxWidth(Double.POSITIVE_INFINITY);
		deleteButton.disableProperty().bind(
				table.getSelectionModel().selectedIndexProperty().isEqualTo(-1));
		Button addButton = new Button("Add Random Point");
		addButton.setOnAction( e -> {  // add another random point to the table
			points.add( new Point(5*Math.random(), 5*Math.random()) );
			table.scrollTo(points.size()-1); // make sure new point is visible
			table.getSelectionModel().select(points.size()-1);
		});
		addButton.setMaxWidth(Double.POSITIVE_INFINITY);
		VBox buttons = new VBox(addButton,deleteButton);
		buttons.setStyle("-fx-border-color:black; -fx-border-width: 2px");
		
		BorderPane tableHolder = new BorderPane(table);
		tableHolder.setBottom(buttons);
		
		/* Create the canvas and install a mouse event handler on it. */
		
		canvas = new Canvas(400,400);
		canvas.setOnMousePressed( e -> canvasClicked(e) );
		redrawDisplay();
		
		/* Finish setting up the GUI */
		
		HBox root = new HBox(tableHolder, canvas);
		
		stage.setScene( new Scene(root) );
		stage.setTitle("Editable Table Demo");
		stage.setResizable(false);
		stage.show();
				
	} // end start()


	/**
	 * An object of type Point represents one row in the table, 
	 * which displays the x and y coordinates of a point in its
	 * two columns.  This class follows the pattern that editable
	 * table columns should be represented by observable properties
	 * of the objects that define the rows.  Note that getter and
	 * setter methods are usually provided for such properties, but
	 * they are not required for the table and are not included here.
	 * (If we wanted the canvas to be redrawn in response to 
	 * arbitrary changes in Points, we would need to add listeners
	 * to the x and y properties of every point.  That is not done
	 * here, since the only way points will change is if the user
	 * edits them, and the program redraws the canvas in that case
	 * using a listener on the editingCell property of the table.)
	 */
	public static class Point {
		private DoubleProperty x, y;
		public Point(double xVal, double yVal) {
			x = new SimpleDoubleProperty(this,"x",xVal);
			y = new SimpleDoubleProperty(this,"y",yVal);
		}
		public DoubleProperty xProperty() {
			return x;
		}
		public DoubleProperty yProperty() {
			return y;
		}
	}
	
	
	/**
	 * When the canvas is clicked, add a new point at the mouse location.  The
	 * coordinates of the point have to be transformed from the usual pixel
	 * coordinates to the coordinate system that is used for points in the
	 * table.  That coordinate system was determined when the canvas was
	 * drawn, and the canvasTransform was saved at that time.
	 */
	private void canvasClicked(MouseEvent e) {
		Point2D transformedPoint = canvasTransform.transform(e.getX(),e.getY());
		Point pt = new Point( transformedPoint.getX(), transformedPoint.getY());
		points.add(pt);
		table.scrollTo(points.size() - 1);
	}

	
	/**
	 * Draws the canvas where a scatter plot of the points
	 * in the table is shown.  The range of values shown in the plot
	 * is adjusted to make sure that all the points are visible.
	 * Note that only points for which both coordinates are
	 * defined (i.e., not Double.NaN) are drawn.
	 */
	private void redrawDisplay() {
		if (canvas == null || points == null)
			return;
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.setFill(Color.WHITE);
		g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

		g.save();
		
		double min = 0;  // Minimum of the range of values displayed.
		double max = 5;     // Maximum of the range of value displayed.
		for (Point pt: points) {
			double x = pt.xProperty().get();  // (Return type of getValue() is Object.)
			double y = pt.yProperty().get();
			if ( !Double.isNaN(x) && !Double.isNaN(y)) {
				if (x < min)
					min = x - 0.5;
				if (x > max)
					max = x + 0.5;
				if (y < min)
					min = y - 0.5;
				if (y > max)
					max = y + 0.5;
			}
		}
		min -= 0.5;
		max += 0.25;

		/* Apply a translation so that the drawing coordinates on the display
		 * correspond to the range of values that I want to show. */

		g.translate(canvas.getWidth()/2,canvas.getHeight()/2);  // (in fact, canvas is square)
		g.scale(canvas.getWidth()/(max-min), -canvas.getHeight()/(max-min));
		g.translate(-(max+min)/2, -(max+min)/2);
		try {
			canvasTransform = g.getTransform().createInverse();
		}
		catch (Exception e) {
		}

		/* I want to be able to draw lines that are a certain number of pixels
		 * long.  Unfortunately, the unit of length is no longer equal to the
		 * size of a pixel, so I have to figure out how big a pixel is in the
		 * new coordinates.  Also, horizontal and vertical size can be different. */

		double pixelSize = (max-min)/canvas.getWidth();  // Size of a pixel in new coords.

		g.setLineWidth(2*pixelSize); // actual line width is two pixels

		/* Draw x and y axes with tick marks to mark the integers (but don't draw
		 * the tick marks if there would be more than 100 of them. */

		g.setStroke(Color.BLUE);
		g.strokeLine(min,0,max,0);
		g.strokeLine(0,min,0,max);
		if (max - min < 100) {
			int tick = (int)min;
			while (tick <= max) {
				g.strokeLine(tick,0,tick,3*pixelSize);
				g.strokeLine(0,tick,3*pixelSize,tick);
				tick++;
			}
		}

		/* Draw a small crosshair at each point from the table. */

		g.setStroke(Color.RED);
		for (Point pt : points) {
			double x = pt.xProperty().get();
			double y = pt.yProperty().get();
			if ( !Double.isNaN(x) && !Double.isNaN(y)) {
				g.strokeLine(x-3*pixelSize,y,x+3*pixelSize,y);
				g.strokeLine(x,y-3*pixelSize,x,y+3*pixelSize);
			}
		}
		
		g.restore();
		
		/* Draw a border around the edge of the canvas. */
		
		g.setStroke(Color.GRAY);
		g.setLineWidth(4);
		g.strokeRect(2,2,canvas.getWidth()-4,canvas.getHeight()-4);

	} // end redrawDisplay()

	
} // end ScatterPlotTableDemo
