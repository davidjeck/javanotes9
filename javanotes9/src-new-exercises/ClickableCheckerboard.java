import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

/**  
 *  This program draws a red-and-black checkerboard.
 *  It is assumed that the size of the canvas is 400
 *  by 400 pixels.  When the user clicks a square, that
 *  square is selected, unless it is already selected.
 *  When the user clicks the selected square, it is
 *  unselected.  If there is a selected square, it is
 *  highlighted with a cyan border.
 */
public class ClickableCheckerboard extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //-------------------------------------------------------------------

    private Canvas canvas;  // Where the checkerboard is drawn

    private int selectedRow; // Row and column of selected square.  If no
    private int selectedCol; //      square is selected, selectedRow is -1.

    /**
     * Constructor.  Set selectedRow to -1 to indicate that
     * no square is selected.  And set the board object
     * to listen for mouse events on itself.
     */
    public void start(Stage stage) {
        
        selectedRow = -1;  // To start, no square is selected!
        
        canvas = new Canvas(400,400);
        draw();
        
        canvas.setOnMousePressed(e -> mousePressed(e));
        
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Click Me!");
        stage.show();        
    }
    

    /**
     * Draw the checkerboard and highlight selected square, if any.
     */
    private void draw() {

        int row;   // Row number, from 0 to 7
        int col;   // Column number, from 0 to 7
        int x,y;   // Top-left corner of square
        
        GraphicsContext g = canvas.getGraphicsContext2D();

        for ( row = 0;  row < 8;  row++ ) {

            for ( col = 0;  col < 8;  col++) {
                x = col * 50;
                y = row * 50;
                if ( (row % 2) == (col % 2) )
                    g.setFill(Color.RED);
                else
                    g.setFill(Color.BLACK);
                g.fillRect(x, y, 50, 50);
            } 

        } // end for row

        if (selectedRow >= 0) {
                // Since there is a selected square, draw a cyan
                // border around it.  (If selectedRow < 0, then
                // no square is selected and no border is drawn.)
            g.setStroke(Color.CYAN);
            g.setLineWidth(3);
            y = selectedRow * 50;
            x = selectedCol * 50;
            g.strokeRect(x+1.5, y+1.5, 47, 47);
        }

    }  // end paint()
    

    /**
     * When the user clicks on the canvas, figure out which
     * row and column the click was in and change the
     * selected square accordingly.
     */
    private void mousePressed(MouseEvent evt) {

        int col = (int)(evt.getX() / 50);   // Column where user clicked.
        int row = (int)(evt.getY() / 50);   // Row where user clicked.

        if (selectedRow == row && selectedCol == col) {
                // User clicked on the currently selected square.
                // Turn off the selection by setting selectedRow to -1.
            selectedRow = -1;
        }
        else {
                // Change the selection to the square the user clicked on.
            selectedRow = row;
            selectedCol = col;
        }
        draw();

    }  // end mousePressed()

} // end ClickableCheckerboard
