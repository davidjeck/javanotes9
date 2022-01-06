import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;


/**
 * Shows a pair of dice that are rolled when the user clicks a button
 * that appears below the dice.
 */
public class RollDiceWithButton extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //---------------------------------------------------------------------
    
   private int die1 = 4;  // The values shown on the dice.
   private int die2 = 3;
   
   private Canvas canvas; // The canvas where the dice are drawn
   
   private Button rollButton;  // The button that is clicked to roll the dice.
   
   private int frameNumber;  // When an animation is running, the number of
                             //    frames for which it has been running.  This
                             //    is used to end the animation after 60 frames.

   private AnimationTimer timer = new AnimationTimer() {
           // The timer is used to animation "rolling" of the dice.
           // In each frame, the dice values are randomized.  When
           // the number of frames reaches 60, the timer stops itself.
           // The rollButton is disabled while an animation is in
           // progress, so it has to be enabled when the animation stops.
       public void handle( long time ) {
           die1 = (int)(Math.random()*6) + 1;
           die2 = (int)(Math.random()*6) + 1;
           draw();
           frameNumber++;
           if (frameNumber == 60) {
               timer.stop();
               rollButton.setDisable(false);
           }
       }
   };
   
   
   /**
    *  The start() method sets up the GUI, using a BorderPane in which
    *  the canvas is the center component and the button is the bottom
    *  component.  An ActionEvent handler is added to the button to
    *  roll the dice when the button is clicked.
    */
   public void start(Stage stage) {
      
       canvas = new Canvas(100,100);
       draw();  // Draw the original dice.
       
       rollButton = new Button("Roll!");
       rollButton.setMaxWidth(1000);  // so button can grow to full width of window
       rollButton.setOnAction( e -> roll() ); // When clicked, roll the dice.
       
       BorderPane root = new BorderPane();
       root.setCenter(canvas);
       root.setBottom(rollButton);
       
       Scene scene = new Scene(root);
       stage.setScene(scene);
       stage.setTitle("Dice!");
       stage.setResizable(false);
       stage.show();
             
   } // end start()
   
   
   /**
    * Roll the dice by starting an animation that randomizes
    * the values on the dice in each frame.  The animation will
    * last for 60 frames, and the rollButton is disabled while
    * the animation is in progress.  This method is called
    * when the user clicks the roll button.
    */
   private void roll() {
       frameNumber = 0;
       rollButton.setDisable(true);
       timer.start(); // start an animation
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
    * The draw() method just draws the two dice and draws
    * a two-pixel wide blue border around the canvas.
    */
   private void draw() {
       GraphicsContext g = canvas.getGraphicsContext2D();
       g.setFill(Color.rgb(200,200,255));
       g.fillRect(0,0,100,100);
       g.setStroke( Color.BLUE );
       g.strokeRect(1,1,98,98);
       drawDie(g, die1, 10, 10);
       drawDie(g, die2, 55, 55);
   }
   
} // end class RollDiceWithButton
