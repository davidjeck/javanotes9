import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;


/**
 * A program that lets the user add squares to a canvas by clicking.
 * The center of a square is placed at the point where the user clicked.
 * Squares all have the same size (100-by-100).  They have random
 * colors with up to 50% transparency.  If the user shift-clicks
 * or right-clicks a square, the user can drag the square.  If
 * the user drags a square off the canvas, it is deleted from the
 * list of squares.
 */
public class DragLotsOfSquares extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //---------------------------------------------------------------------
    
    /**
     * An object of type SquareData contains the data necessary to
     * draw one square, that is, the color of the square and the
     * coordinates of its center.
     */
    private static class SquareData {
    	double x,y;  // Location of center of square.  The size is always 100-by-100.
    	Color color; // The color of the square
    }


    private ArrayList<SquareData> squares;  // Info for all squares in the picture.

    private Canvas canvas;  // The canvas where the sqaures are drawn.


    /**
     *  The start method sets up the GUI.  It adds mouse event handlers to
     *  the canvas to implement adding and dragging squares.
     */
    public void start(Stage stage) {
    	
    	squares = new ArrayList<SquareData>();

        canvas = new Canvas(640,480);
        draw(); // Will just fill canvas with background color.
        
        canvas.setOnMousePressed( e -> mousePressed(e) );
        canvas.setOnMouseDragged( e -> mouseDragged(e) );
        canvas.setOnMouseReleased( e -> mouseReleased(e) );
        
        Pane root = new Pane(canvas);
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Click to add a square. Right-click to drag.");
        stage.setResizable(false);
        stage.show();
    } 


    /**
     * Draw the canvas, showing all squares in their current positions.
     */
    private void draw() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.rgb(230,255,230)); // light green
        g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        g.setLineWidth(2);
        g.setStroke(Color.BLACK);
        for ( SquareData squareData: squares ) {
        	g.setFill( squareData.color );
        	g.fillRect( squareData.x - 50, squareData.y - 50, 100, 100);
        	g.strokeRect( squareData.x - 50, squareData.y - 50, 100, 100);
        }
    }

    
    //-----------------  Variables and methods for responding to drags -----------

    private boolean dragging;      // Set to true when a drag is in progress.

    private SquareData draggedSquare;  // When a drag is in progress, this is 
                                       // the square that is being dragged.

    private double offsetX, offsetY;  // Offset of mouse-click coordinates from the
                                      //   center of the square that is being dragged.

    /**
     * Respond when the user presses the mouse on the canvas.
     * A shift-click or right-click starts dragging the square
     * under the mouse, if any.  Other clicks will add a new
     * square with its center at the mouse position.  A drag
     * operation is begun only if the user shift-clicks or
     * right-clicks a square.
     */
    public void mousePressed(MouseEvent evt) { 

        if (dragging)  // Exit if a drag is already in progress.
            return;

        double x = evt.getX();  // Location where user clicked.
        double y = evt.getY();

        if (evt.isShiftDown() || evt.getButton() == MouseButton.SECONDARY) {  
        	// If user shift-clicked a square, start dragging it.
        
	        /* Find the square, if any, that contains (x,y).  If several squares
	         * contain (x,y), we want the one on top, which is the LAST one in
	         * the list that contains (x,y) -- so consider the squares in the
	         * reverse of their order in the list. */
	        
	        for (int i = squares.size() - 1; i >= 0; i--) {
	        	SquareData squareData = squares.get(i);
	        	double cx = squareData.x; // (cx,cy) is the center of the square
	        	double cy = squareData.y;
	        	if ( x >= cx - 50 && x <= cx + 50 && y >= cy - 50 && y <= cy + 50) {
	        		dragging = true;
	        		draggedSquare = squareData;
	        		offsetX = x - cx;
	        		offsetY = y - cy;
	        		break;  // stop as soon as we find  square containing (x,y)
	        	}
	        }
        }
        else { // Add a new square with center at (x,y)
        	SquareData squareData = new SquareData();
        	squareData.x = x;
        	squareData.y = y;
        	squareData.color = Color.color( 
        			Math.random(), Math.random(), Math.random(), 0.5 + 0.5*Math.random() );
        	squares.add( squareData );
        	draw();  // Redraw the whole picture to show the new square.
        	         //  (Could have just drawn it here instead!)
        }
    }
    

    /**
     * Dragging stops when user releases the mouse button.  If the user
     * has dragged the square completely off the canvas, then it is deleted
     * from the list of squares. (That will have no visible effect on the
     * picture, so the canvas is not redrawn.)
     */
    public void mouseReleased(MouseEvent evt) { 
    	if ( ! dragging )
    		return;
    	if (draggedSquare.x > canvas.getWidth() + 50
    			|| draggedSquare.x < -50
    			|| draggedSquare.y > canvas.getHeight() + 50
    			|| draggedSquare.y < -50) {
    		  // Square is completely off the canvas, so remove it!
    		squares.remove(draggedSquare);
    		  // For testing, to make sure square is actually deleted:
    		System.out.println("Removed square; list size = " + squares.size());
    	}
        dragging = false;  // drag operation has ended.
        draggedSquare = null;
    }
    

    /**
     * Respond when the user drags the mouse.  If a square is 
     * not being dragged, then exit. Otherwise, change the position
     * of the square that is being dragged to match the position
     * of the mouse.  Note that the center of the square is placed
     * in the same relative position with respect to the mouse that it
     * had when the user started dragging it.
     */
    public void mouseDragged(MouseEvent evt) { 
        if ( ! dragging )  
            return;
        double x = evt.getX();
        double y = evt.getY();
        draggedSquare.x = x - offsetX;
        draggedSquare.y = y - offsetY;
        draw();  // Redraw picture to show square in new positions.
    }

} // end class DragLotsOfSquares
