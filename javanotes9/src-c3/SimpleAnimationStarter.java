
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
public class SimpleAnimationStarter extends Application {

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

		/* NOTE:  To get a different animation, just erase the contents of this 
		 * subroutine and substitute your own. 
		 */

		g.setFill(Color.WHITE);
		g.fillRect(0, 0, width, height); // First, fill the entire image with a background color!

		g.setFill(Color.BLACK);
		g.fillText( "Frame number " + frameNumber, 40, 50 );
		g.fillText( String.format("Elapsed Time: %1.1f seconds", elapsedSeconds), 40, 80);

	}

	//------ Implementation details: DO NOT EXPECT TO UNDERSTAND THIS ------


	public void start(Stage stage) {
		int width = 800;   // The width of the image.  You can modify this value!
		int height = 600;  // The height of the image. You can modify this value!
		Canvas canvas = new Canvas(width,height);
		drawFrame(canvas.getGraphicsContext2D(), 0, 0, width, height);
		BorderPane root = new BorderPane(canvas);
		root.setStyle("-fx-border-width: 4px; -fx-border-color: #444");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Simple Animation"); // STRING APPEARS IN WINDOW TITLEBAR!
		stage.show();
		stage.setResizable(false);
		AnimationTimer anim = new AnimationTimer() {
			private int frameNum;
			private long startTime = -1;
			private long previousTime;
			public void handle(long now) {
				if (startTime < 0) {
					startTime = previousTime = now;
					drawFrame(canvas.getGraphicsContext2D(), 0, 0, width, height);
				}
				else if (now - previousTime > 0.95e9/60) {
					   // The test in the else-if is to make sure that drawFrame() is
					   // called about once every 1/60 second.  It is required since
					   // handle() can be called by the system more often than that.
					frameNum++;
					drawFrame(canvas.getGraphicsContext2D(), frameNum, (now-startTime)/1e9, width, height);
					previousTime = now;
				}
			}
		};
		anim.start();
	} 

	public static void main(String[] args) {
		launch();
	}
	
} // end SimpleAnimationStarter
