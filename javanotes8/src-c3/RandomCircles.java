
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 *  When run as a program, this class opens a window on the screen that
 *  shows a large number of colored disks.  The positions of the disks
 *  are selected at random, and the color is randomly selected from
 *  red, green, or blue.  A black outline is drawn around each disk.
 *  The picture changes every three seconds.
 */
public class RandomCircles extends Application {

	/**
	 * Draws 500 disks with random colors and locations.
	 * Each disk has a radius of 50 pixels.  This subroutine is
	 * called every three seconds, giving a new set of disks.
	 * Since the drawing area is not erased first, the disk drawn by
	 * this subroutine is added to the image rather than replacing it.
	 */
	public void drawFrame(GraphicsContext g, int frameNumber, double elapsedSeconds, int width, int height) {

		int centerX;     // The x-coord of the center of a disk.
		int centerY;     // The y-coord of the center of a disk.
		int colorChoice; // Used to select a random color.

		centerX = (int)(width*Math.random());
		centerY = (int)(height*Math.random());

		colorChoice = (int)(4*Math.random());
		switch (colorChoice) {
		case 0:
			g.setFill(Color.RED);
			break;
		case 1:
			g.setFill(Color.GREEN);
			break;
		case 2:
			g.setFill(Color.BLUE);
			break;
		case 3:
			g.setFill(Color.YELLOW);
			break;
		}

		g.fillOval( centerX - 50, centerY - 50, 100, 100 );
		g.setStroke(Color.BLACK);
		g.strokeOval( centerX - 50, centerY - 50, 100, 100 );

	}

	//------ Implementation details: DO NOT EXPECT TO UNDERSTAND THIS ------

	private int frameNum;
	private long startTime;

	public void start(Stage stage) {
		int width = 800;   // The width of the image.  You can modify this value!
		int height = 600;  // The height of the image. You can modify this value!
		Canvas canvas = new Canvas(width,height);
		drawFrame(canvas.getGraphicsContext2D(), 0, 0, width, height);
		BorderPane root = new BorderPane(canvas);
		root.setStyle("-fx-border-width: 4px; -fx-border-color: #444");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
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

} // end RandomCircles
