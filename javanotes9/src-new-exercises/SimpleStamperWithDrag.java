
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;


/**
 * A simple demonstration of MouseEvents.  Shapes are drawn
 * on a black background when the user clicks the canvas.  If
 * the user Shift-clicks, the canvas is cleared.  If the user
 * right-clicks the canvas, a blue oval is drawn.  Otherwise,
 * when the user clicks, a red rectangle is drawn.
 * Ovals and rects continue to be drawn as the user drags the mouse.
 */
public class SimpleStamperWithDrag extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    // ----------------------------------------------------------------------

    /**
     * This variable is set to true during a drag operation, unless the
     * user was holding down the shift key when the mouse was first
     * pressed (since in that case, the mouse gesture simply clears the
     * canvas and no figures should be drawn if the user drags the mouse).
     */
    private boolean dragging;
    
    /**
     * While dragging, prevShapeX and prevShapeY are the coordinates
     * at which the previous shape was drawn.  They are used to avoid 
     * drawing the next shape until the mouse has moved at least 5 pixels
     * horizontally or vertically.
     */
    private double prevShapeX, prevShapeY;
    
    /**
     * A graphics context for drawing on the canvas that fills the screen.
     */
    private GraphicsContext canvasGraphics;

    
    /**
     * This start() method sets up the GUI to show a canvas where the shapes
     * are drawn, and it installs mouse handlers on the canvas to draw shapes
     * as the user presses and drags the mouse.
     */
    public void start(Stage stage) {
        
        Canvas canvas = new Canvas(500,380);
        canvasGraphics = canvas.getGraphicsContext2D();
        canvasGraphics.setFill(Color.WHITE);
        canvasGraphics.fillRect(0,0,500,380);
        canvasGraphics.setStroke(Color.BLACK); // stroke color never changes
        
        canvas.setOnMousePressed( e -> mousePressed(e) );
        canvas.setOnMouseDragged( e -> mouseDragged(e) );
        
        BorderPane root = new BorderPane(canvas);
        root.setStyle("-fx-border-color: black; -fx-border-width: 2px");
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Mouse Drag Demo");
        stage.setResizable(false);
        stage.show();
        
    } // end start()


    /**
     *  This method will be called when the user clicks the mouse on the canvas.
     *  If the user right-clicked, it clears the canvas.  Otherwise, it draws
     *  a shape and starts a drag operation.
     */
    public void mousePressed(MouseEvent evt) {

        if ( evt.getButton() == MouseButton.SECONDARY ) {
                // The user right-clicked the canvas.  Fill the canvas with white
                // to erase its current contents.
            dragging = false;
            canvasGraphics.setFill(Color.WHITE);
            canvasGraphics.fillRect(0,0,500,380);
            return;
        }

        dragging = true;

        double x = evt.getX();  // x-coordinate where user clicked.
        double y = evt.getY();  // y-coordinate where user clicked.
        
        prevShapeX = x;  // Save coordinates where first shape is drawn.
        prevShapeY = y;

        if ( evt.isShiftDown() ) {
                // User was holding down the shift key. Draw a blue oval centered 
                // at the point (x,y). (A black outline around the oval will make it 
                // more distinct when shapes overlap.)
            canvasGraphics.setFill(Color.BLUE);  // Blue interior.
            canvasGraphics.fillOval( x - 30, y - 15, 60, 30 );
            canvasGraphics.strokeOval( x - 30, y - 15, 60, 30 );
        }
        else {
                // Draw a red rectangle centered at (x,y).
            canvasGraphics.setFill(Color.RED);   // Red interior.
            canvasGraphics.fillRect( x - 30, y - 15, 60, 30 );
            canvasGraphics.strokeRect( x - 30, y - 15, 60, 30 );
        }

    } // end mousePressed();


    /**
     *  This method is called when the user drags the mouse.  If a the value of the
     *  instance variable dragging is true, it will draw a rect or oval at the
     *  current mouse position.
     */
    public void mouseDragged(MouseEvent evt) {
        if ( dragging == false ) { 
            return;
        }
        
        double x = evt.getX();  // x-coordinate where user clicked.
        double y = evt.getY();  // y-coordinate where user clicked.
        
        if ( Math.abs(x - prevShapeX) < 5 && Math.abs(y - prevShapeY) < 5 ) {
                // The mouse has not moved at least 5 pixels horizontally
                // or vertically, so don't draw another shape yet.
            return;
        }
        
        prevShapeX = x;  // Save coords where the next shape is being drawn.
        prevShapeY = y;

        if (evt.isShiftDown() ) {
                // User was holding down the shift key. Draw a blue oval centered 
                // at the point (x,y). (A black outline around the oval will make it 
                // more distinct when shapes overlap.)
            canvasGraphics.setFill(Color.BLUE);  // Blue interior.
            canvasGraphics.fillOval( x - 30, y - 15, 60, 30 );
            canvasGraphics.strokeOval( x - 30, y - 15, 60, 30 );
        }
        else {
                // Draw a red rectangle centered at (x,y).
            canvasGraphics.setFill(Color.RED);   // Red interior.
            canvasGraphics.fillRect( x - 30, y - 15, 60, 30 );
            canvasGraphics.strokeRect( x - 30, y - 15, 60, 30 );
        }
        
    } // end mouseDragged();


} // end class SimpleStamperWithDrag
