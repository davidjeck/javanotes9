import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

/**
 * This program lets the user draw filled polygons.
 * The user inputs a polygon by clicking a series of points.
 * The points are connected with lines from each point to the
 * next Clicking near the starting point (within 3 pixels) or
 * right-clicking will complete the polygon, so the user can 
 * begin a new one.  As soon as the user begins drawing a new 
 * polygon, the old one is discarded.
 */
public class SimplePolygons extends Application {
     
    public static void main(String[] args) {
        launch(args);
    }

    //---------------------------------------------------------------------

    private Canvas canvas;

    /* Variables for implementing polygon input. */

    private double[] xCoord, yCoord;    // Arrays containing the points of 
                                        //   the polygon.  Up to 500 points 
                                        //   are allowed.

    private int pointCt;  // The number of points that have been input.

    private boolean complete;   // Set to true when the polygon is complete.
                                // When this is false, only a series of lines are drawn.
                                // When it is true, a filled polygon is drawn.

    private final static Color POLYGON_COLOR = Color.RED;  
                                // Color that is used to draw the polygons.  


    /**
     * Set up the GUI, and install a mouse hander its data.
     */
    public void start (Stage stage) {
        
        xCoord = new double[500];  // create arrays to hold the polygon's points
        yCoord = new double[500];
        pointCt = 0;
        
        canvas = new Canvas(400,400);
        draw();
        canvas.setOnMousePressed( e -> mousePressed(e) );
        
        StackPane root = new StackPane(canvas);
        root.setStyle("-fx-border-color: black");
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Polygons");
        stage.setResizable(false);
        stage.show();
        
    }

    
    /**
     * Fill the canvas with white.  If the polygon is complete, draw it.
     * If not, draw the lines that the user has input so far.  (If only
     * one point has been input, it will still be visible as a small dot.)
     */
    private void draw() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.WHITE);
        g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        if (pointCt == 0)
            return;
        g.setLineWidth(2);
        g.setStroke(Color.BLACK);
        if (complete) { // draw a polygon
            g.setFill(POLYGON_COLOR);
            g.fillPolygon(xCoord, yCoord, pointCt);
            g.strokePolygon(xCoord, yCoord, pointCt);
        }
        else { // show the lines the user has drawn so far
            g.setFill(Color.BLACK);
            g.fillRect(xCoord[0]-2, yCoord[0]-2, 4, 4);  // small square marks first point
            for (int i = 0; i < pointCt - 1; i++) {
                g.strokeLine( xCoord[i], yCoord[i], xCoord[i+1], yCoord[i+1]);
            }
        }
    }


    /**
     * Processes a mouse click.
     */
    private void mousePressed(MouseEvent evt) { 

        if (complete) {
                // Start a new polygon at the point that was clicked.
            complete = false;
            xCoord[0] = evt.getX();
            yCoord[0] = evt.getY();
            pointCt = 1;
        }
        else if ( pointCt > 0 && pointCt > 0 && (Math.abs(xCoord[0] - evt.getX()) <= 3)
                && (Math.abs(yCoord[0] - evt.getY()) <= 3) ) {
                // User has clicked near the starting point.
                // The polygon is complete.
            complete = true;
        }
        else if (evt.getButton() == MouseButton.SECONDARY || pointCt == 500) {
                // The polygon is complete.
            complete = true;
        }
        else {
                // Add the point where the user clicked to the list of
                // points in the polygon, and draw a line between the
                // previous point and the current point.  A line can
                // only be drawn if there are at least two points.
            xCoord[pointCt] = evt.getX();
            yCoord[pointCt] = evt.getY();
            pointCt++;
        }
        draw();  // in all cases, redraw the picture.
    } // end mousePressed()


}  // end class SimplePolygons

