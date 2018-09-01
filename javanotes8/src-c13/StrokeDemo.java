import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.TilePane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * This program demonstrates the effect of using different stroke properties 
 * when stroking lines and rectangles.  Fifteen small canvases are shown, in three
 * rows and five columns.  Each canvas uses different stroke properties.  The
 * line widths are 1, 2, 5, 10, or 15, depending on the column.  The first row
 * uses the default values for LineCap and LineJoin.  The second row
 * uses StrokeLineCap.ROUND and StrokeLineJoin.ROUND.  The third row
 * uses StrokeLineCap.BUTT and StrokeLineJoin.BEVEL.  In addition the
 * third row uses a dash pattern.
 */
public class StrokeDemo extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//---------------------------------------------------------------------


	private Display[] displays;    // The 15 canvasses.

	private double startX, startY; // Record the starting point of a drag operation.


	/**
	 * Each of the 15 canvases in the program is an object of type Display.
	 * Canvas size is 150-by-150.  The constructor specifies the properties
	 * of strokes that are drawn on the canvas.
	 */
	private class Display extends Canvas {
		GraphicsContext g;  // graphics context for drawing on this canvas
		boolean offset;  // for a line of width 1, coordinates will be offset by 0.5
		Display(StrokeLineCap cap, StrokeLineJoin join, double lineWidth, double[] dashPattern) {
			super(150,150);
			g = getGraphicsContext2D();
			g.setLineCap(cap);
			g.setLineJoin(join);
			g.setLineWidth(lineWidth);
			if (dashPattern != null)
				g.setLineDashes(dashPattern);
			g.setFill(Color.WHITE);
			g.setStroke(Color.BLACK);
			offset = lineWidth == 1;
			draw(true,20,20,125,135);
		}
		void draw( boolean drawLine, double x1, double y1, double x2, double y2) {
			    // Draw a line from (x1,y1) to (x2,y2), or stroke a rectangle
			    // with corners at (x1,y1) and (x2,y2).
			if (offset) {
				x1 += 0.5;
				x2 += 0.5;
				y1 += 0.5;
				y2 += 0.5;
			}
			g.fillRect(0,0,150,150);
			if (drawLine)
				g.strokeLine(x1,y1,x2,y2);
			else {
				double x = Math.min(x1,x2);
				double y = Math.min(y1,y2);
				double w = Math.abs(x1 - x2);
				double h = Math.abs(y1 - y2);
				g.strokeRect(x,y,w,h);
			}
		}
	} // end class Display


	/**
	 * The start method creates and lays out the 15 display canvasses in a TilePane
	 * with 3 rows and 5 columns.  Each canvas is created using different stroke properties.
	 */
	public void start(Stage stage) {

		TilePane root = new TilePane(5,5);
		root.setPrefColumns(5);
		root.setStyle("-fx-background-color:#008; -fx-border-color: #008; -fx-border-width: 5");

		double[] dashPattern = { 7, 7 };

		displays = new Display[] {

				new Display(StrokeLineCap.SQUARE, StrokeLineJoin.MITER, 1, null),
				new Display(StrokeLineCap.SQUARE, StrokeLineJoin.MITER, 2, null),
				new Display(StrokeLineCap.SQUARE, StrokeLineJoin.MITER, 5, null),
				new Display(StrokeLineCap.SQUARE, StrokeLineJoin.MITER, 10, null),
				new Display(StrokeLineCap.SQUARE, StrokeLineJoin.MITER, 20, null),

				new Display(StrokeLineCap.ROUND, StrokeLineJoin.ROUND, 1, null),
				new Display(StrokeLineCap.ROUND, StrokeLineJoin.ROUND, 2, null),
				new Display(StrokeLineCap.ROUND, StrokeLineJoin.ROUND, 5, null),
				new Display(StrokeLineCap.ROUND, StrokeLineJoin.ROUND, 10, null),
				new Display(StrokeLineCap.ROUND, StrokeLineJoin.ROUND, 20, null),

				new Display(StrokeLineCap.BUTT, StrokeLineJoin.BEVEL, 1, dashPattern),
				new Display(StrokeLineCap.BUTT, StrokeLineJoin.BEVEL, 2, dashPattern),
				new Display(StrokeLineCap.BUTT, StrokeLineJoin.BEVEL, 5, dashPattern),
				new Display(StrokeLineCap.BUTT, StrokeLineJoin.BEVEL, 10, dashPattern),
				new Display(StrokeLineCap.BUTT, StrokeLineJoin.BEVEL, 20, dashPattern)
		};

		for (Display canvas : displays) {
			root.getChildren().add(canvas);
			canvas.setOnMousePressed( this::mousePressed );
			canvas.setOnMouseDragged( this::mouseDragged );
		}
		
		stage.setScene( new Scene(root) );
		stage.setTitle("Left- or Right-Click and Drag on Any Canvas");
		stage.setResizable(false);
		stage.show();
		
	} 


	/**
	 * Called when the user presses the mouse on ANY one of the 15 canvasses.
	 * Draws a line or rectangle on ALL canvasses, using the same point for
	 * both endpoints of the line or for corners of the rectangle.  This will
	 * show something in some canvasses, depending on the lineCap and lineJoin
	 * used in the canvas.
	 */
	private void mousePressed(MouseEvent e) {
		double x = e.getX();
		double y = e.getY();
		boolean drawLine = e.getButton() == MouseButton.SECONDARY; 
		for (Display canvas : displays) {
			canvas.draw(drawLine,x,y,x,y);
		}
		startX = x;
		startY = y;
	}
	

	/**
	 * Called when the user drags the mouse on any of the 15 canvasses.  Draws
	 * a line or rectangle on all canvasses, using the start point of the drag
	 * and the current position of the mouse.
	 */
	public void mouseDragged(MouseEvent e) {
		double x = e.getX();
		double y = e.getY();
		boolean drawLine = e.getButton() == MouseButton.SECONDARY; 
		for (Display canvas : displays) {
			canvas.draw(drawLine,startX,startY,x,y);
		}
	}


} // end StrokeDemo

