
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This program shows an animation where 100 semi-transparent disks of
 * various sizes grow continually, disappearing before they get too big.
 * When a disk disappears, it is replaced by a new disk at another location.
 * This program uses class CircleInfo, defined in CircleInfo.java.
 */
public class GrowingCircleAnimation extends Application {
	
	private CircleInfo[] circleData; // holds the data for all 100 circles
	
	/**
	 *  Draw one frame of the animation.  If there is no disk data (which is
	 *  true for the first frame), 100 disks with random locations, colors,
	 *  and radii are created.  In each frame, all the disks grow by
	 *  one pixel per frame.  Disks sometimes disappear at random, or when
	 *  their radius reaches 100.  When a disk disappears, a new disk appears
	 *  with radius 1 and with a random location and color
	 */
	private void drawFrame(GraphicsContext g, int frameNumber, int width, int height) {
		g.setFill(Color.WHITE);
		g.fillRect(0,0,width,height);
		if (circleData == null) {  // create the array, if it doesn't exist
			circleData = new CircleInfo[100];
			for (int i = 0; i < circleData.length; i++) {
				circleData[i] = new CircleInfo( 
										(int)(width*Math.random()),
										(int)(height*Math.random()),
										(int)(100*Math.random()) );
			}
		}
		for (int i = 0; i < circleData.length; i++) {  // draw the filled circles
			circleData[i].radius++;
			circleData[i].draw(g);
			if (Math.random() < 0.005 || circleData[i].radius > 100) {
				    // replace circle number i with a new circle
				circleData[i] = new CircleInfo( 
						                (int)(width*Math.random()),
						                (int)(height*Math.random()),
						                1 );
			}
		}
	}
	
	
	//------ Implementation details: DO NOT EXPECT TO UNDERSTAND THIS -----

	
	public void start(Stage stage) {
		int width = 600;
		int height = 480;
		Canvas canvas = new Canvas(width,height);
		drawFrame(canvas.getGraphicsContext2D(), 0, width, height);
		BorderPane root = new BorderPane(canvas);
		root.setStyle("-fx-border-width: 4px; -fx-border-color: #444");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Growing Circles");
		stage.show();
		stage.setResizable(false);
		AnimationTimer anim = new AnimationTimer() {
			private int frameNum;
			long previousFrameTime;
			public void handle(long time) {
				if (time - previousFrameTime > 0.95e9/60) {
					// The if statement should not be necessary!  It's there because of
					// a bug that has shown up in some versions of Java on some computers.
					// The if statement throttles the frame rate to 60 per second, in case
					// JavaFX incorrectly fails to do that itself.  
					frameNum++;
					drawFrame(canvas.getGraphicsContext2D(), frameNum,  width, height);
					previousFrameTime = time;
				}
			}
		};
		anim.start();
	} 

	public static void main(String[] args) {
		launch();
	}

}
