
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
 *  shows colored disks.  New disks are continually added until the
 *  window is closed by the user.  The positions of the disks
 *  are selected at random, and the color is randomly selected from
 *  red, green, blue, or yellow.  A black outline is drawn around each 
 *  disk to make it more visible.
 */
public class RandomCircles extends Application {

	/**
	 * Each time this subroutine is called, it draws one random disk.
	 * It is called about 60 times per second.  Since this subroutine
	 * does not clear the window before drawing a disk, the disk is
	 * added to what is already in the window.  So, disks are continuously
	 * added to the window at a rate of about sixty disks per second.
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
			private int frameNum;
			private long startTime = -1;
			private long previousTime;
			public void handle(long now) {
				if (startTime < 0) {
					startTime = previousTime = now;
					drawFrame(canvas.getGraphicsContext2D(), 0, 0, width, height);
				}
				else if (now - previousTime > 0.95e9/60) {
					   // The test in the else-if is to guard against a bug that has shown
					   // up in some versions of JavaFX on some computers.  The bug allows
					   // the handle() method to be called many times more than the 60 times
					   // per second that is specified in the JavaFX documentation.
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

} // end RandomCircles
