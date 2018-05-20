
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *  This file can be used to create very simple animations.  Just fill in
 *  the definition of drawFrame with the code to draw one frame of the 
 *  animation, and possibly change a few of the values in the rest of
 *  the program as noted below.
 */
public class CyclicAndOscillatingMotionDemo extends Application {

	/**
	 * Draws one frame of an animation. This subroutine should be called
	 * about 60 times per second.  It is responsible for redrawing the
	 * entire drawing area. The parameter g is used for drawing. The frameNumber 
	 * starts at zero and increases by 1 each time this subroutine is called.  
	 * The parameter elapsedSeconds gives the number of seconds since the animation
	 * was started.  By using frameNumber and/or elapsedSeconds in the drawing
	 * code, you can make a picture that changes over time.  That's an animation.
	 * The parameters width and height give the size of the drawing area, in pixels.  
	 */
	public void drawFrame(GraphicsContext g, int frameNumber, double elapsedSeconds, int width, int height) {


		g.setFill(Color.WHITE);
		g.fillRect(0, 0, width, height); // First, fill the entire image with a background color!

        /* Show cyclic motion at three speeds.  In each case, a square 
         * moves across the drawing area from left to right, then jumps
         * back to the start.
         */

        int cyclicFrameNum;
        
        cyclicFrameNum = frameNumber % 300;  // Repeats every 300 frames
        g.setFill(Color.RED);
        g.fillRect( cyclicFrameNum, 0, 20, 20 );
        
        cyclicFrameNum = frameNumber % 150;  // Repeats every 150 frames
        g.setFill(Color.GREEN);
        g.fillRect( 2*cyclicFrameNum, 20, 20, 20 );
        
        cyclicFrameNum = frameNumber % 100;  // Repeats every 100 frames
        g.setFill(Color.BLUE);
        g.fillRect( 3*cyclicFrameNum, 40, 20, 20 );
        

        /* Show oscillating motion at three speeds.  In each case, a square 
         * moves across the drawing area from left to right, then reverses
         * direction to move from right to left back to its starting point.
         */
        
        int oscillationFrameNum;
        
        oscillationFrameNum = frameNumber % 600;  // repeats every 600 frames
        if (oscillationFrameNum > 300)
            oscillationFrameNum = 600 - oscillationFrameNum; // after 300, the values go backwards back to 0
        g.setFill(Color.CYAN);
        g.fillRect( oscillationFrameNum, 60, 20, 20 );
        
        oscillationFrameNum = frameNumber % 300; // repeats every 300 frames
        if (oscillationFrameNum > 150)
            oscillationFrameNum = 300 - oscillationFrameNum; // after 150, the values go backwards back to 0
        g.setFill(Color.MAGENTA);
        g.fillRect( 2*oscillationFrameNum, 80, 20, 20 );
        
        oscillationFrameNum = frameNumber % 200; // repeats every 200 frames
        if (oscillationFrameNum > 100)
            oscillationFrameNum = 200 - oscillationFrameNum; // after 100, the values go backwards back to 0
        g.setFill(Color.YELLOW);
        g.fillRect( 3*oscillationFrameNum, 100, 20, 20 );
        
        
        /* Draw horizontal black lines across the window to separate the
         * regions used by the six squares.  Also draw a box around the outside,
         * mostly for the picture that I need for the web page!
         */
        
        int y;
        g.setStroke(Color.BLACK);
        for ( y = 20; y < 120; y = y + 20 )
            g.strokeLine(0,y+0.5,320,y+0.5);
 	}

	//------ Implementation details: DO NOT EXPECT TO UNDERSTAND THIS ------


	private int frameNum;
	private long startTime;

	public void start(Stage stage) {
		int width = 320;   // The width of the image.  You can modify this value!
		int height = 120;  // The height of the image. You can modify this value!
		Canvas canvas = new Canvas(width,height);
		drawFrame(canvas.getGraphicsContext2D(), 0, 0, width, height);
		BorderPane root = new BorderPane(canvas);
		root.setStyle("-fx-border-width: 4px; -fx-border-color: #444");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Motion demo"); // STRING APPEARS IN WINDOW TITLEBAR!
		stage.show();
		stage.setResizable(false);
		AnimationTimer anim = new AnimationTimer() {
			public void handle(long now) {
				if (startTime < 0)
					startTime = now;
				frameNum++;
				drawFrame(canvas.getGraphicsContext2D(), frameNum, (now-startTime)/1e9, width, height);
			}
		};
		startTime = -1;
		anim.start();
	} 

	public static void main(String[] args) {
		launch();
	}

} // end CyclicAndOscillatingMotionDemo
