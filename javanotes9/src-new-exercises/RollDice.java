import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Shows a pair of dice that are rolled when the user clicks on the
 * program.  It is assumed that the canvas is 100-by-100 pixels.
 */
public class RollDice extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //---------------------------------------------------------------------

    private int die1 = 4;  // The values shown on the dice.
    private int die2 = 3;

    private Canvas canvas;  // The canvas on which the dice are drawn.
    

    /**
     *  The start() method sets up the GUI and installs a mouse listener
     *  on the canvas where the dice are to be drawn.
     */
    public void start(Stage stage) {
        
        canvas = new Canvas(100,100);
        draw();  // Draw the original dice.
        
        canvas.setOnMousePressed( e -> roll() );
        
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dice!");
        stage.setResizable(false);
        stage.show();
        
    }
    

    /**
     * Draw a die with upper left corner at (x,y).  The die is
     * 35 by 35 pixels in size.  The val parameter gives the
     * value showing on the die (that is, the number of dots).
     */
    private void drawDie(GraphicsContext g, int val, int x, int y) {
        g.setFill(Color.WHITE);
        g.fillRect(x, y, 35, 35);
        g.setStroke(Color.BLACK);
        g.strokeRect(x+0.5, y+0.5, 34, 34);
        g.setFill(Color.BLACK);
        if (val > 1)  // upper left dot
            g.fillOval(x+3, y+3, 9, 9);
        if (val > 3)  // upper right dot
            g.fillOval(x+23, y+3, 9, 9);
        if (val == 6) // middle left dot
            g.fillOval(x+3, y+13, 9, 9);
        if (val % 2 == 1) // middle dot (for odd-numbered val's)
            g.fillOval(x+13, y+13, 9, 9);
        if (val == 6) // middle right dot
            g.fillOval(x+23, y+13, 9, 9);
        if (val > 3)  // bottom left dot
            g.fillOval(x+3, y+23, 9, 9);
        if (val > 1)  // bottom right dot
            g.fillOval(x+23, y+23, 9,9);
    }


    /**
     * Roll the dice by randomizing their values.  Tell the
     * system to repaint the canvas, to show the new values.
     */
    private void roll() {
        die1 = (int)(Math.random()*6) + 1;
        die2 = (int)(Math.random()*6) + 1;
        draw();
    }


    /**
     * The draw() method just draws the two dice and draws
     * a two-pixel wide blue border around the canvas.
     */
    private void draw() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.rgb(200,200,255));
        g.fillRect(0,0,100,100);
        g.setStroke( Color.BLUE );
        g.setLineWidth(2);
        g.strokeRect(1,1,98,98);
        drawDie(g, die1, 10, 10);
        drawDie(g, die2, 55, 55);
    }

} // end class RollDice
