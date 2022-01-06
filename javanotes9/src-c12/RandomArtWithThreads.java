
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A program that shows randomly generated "art".  When the user
 * clicks a "Start" button, a new random artwork is generated every
 * two seconds.
 * 
 * This program demonstrates using a thread for a very simple
 * animation.  The thread uses Platform.runLater() to redraw
 * the canvas on the JavaFX application thread.
 */
public class RandomArtWithThreads extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//-------------------------------------------------------------------


	private Canvas canvas;  // A panel where the random "art" is drawn

	private volatile boolean running;   // Set to true while thread is started;
										// It is set to false as a signal to
										// the thread that it should stop.

	private Runner runner;  // The thread that drives the animation.
	                        // Class Runner is a nested class, defined below.

	private Button startButton;  // A button that is used to start
								 // and stop the animation.

	
	public void start(Stage stage) {
		canvas = new Canvas(640,480);
		redraw();  // fill the canvas with white
		startButton = new Button("Start!");
		startButton.setOnAction( e -> doStartOrStop() );
		HBox bottom = new HBox(startButton);
		bottom.setStyle("-fx-padding: 6px; -fx-border-color: black; -fx-border-width: 3px 0 0 0");
		bottom.setAlignment(Pos.CENTER);
		BorderPane root = new BorderPane(canvas);
		root.setBottom(bottom);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Click Start to Make Random Art!");
		stage.setResizable(false);
		stage.show();
	}
	
	
	/**
	 * This class defines the threads that drive the animation.
	 */
	private class Runner extends Thread {
		public void run() {
			while (running) {
				Platform.runLater( () -> redraw() );
				try {
					Thread.sleep(2000);  // Wait two seconds between redraws.
				}
				catch (InterruptedException e) {
				}
			}
		}
	}


	/**
	 * If called when an animation is running, this method
	 * fills the canvas with a random work of "art".  If no thread
	 * is running, it just fills the canvas with white.
	 */
	private void redraw() {
		GraphicsContext g = canvas.getGraphicsContext2D();
		double width = canvas.getWidth();
		double height = canvas.getHeight();
		if ( ! running ) {
			g.setFill(Color.WHITE);
			g.fillRect( 0, 0, width, height );
			return;
		}
		Color randomGray = Color.hsb( 1, 0, Math.random() );
		g.setFill(randomGray);
		g.fillRect( 0, 0, width, height );

		int artType = (int)(3*Math.random());

		switch (artType) {
		case 0:
			g.setLineWidth(2);
			for (int i = 0; i < 500; i++) {
				int x1 = (int)(width * Math.random());
				int y1 = (int)(height * Math.random());
				int x2 = (int)(width * Math.random());
				int y2 = (int)(height * Math.random());
				Color randomHue = Color.hsb( 360*Math.random(), 1, 1);
				g.setStroke(randomHue);
				g.strokeLine(x1,y1,x2,y2);
			}
			break;
		case 1:
			for (int i = 0; i < 200; i++) {
				int centerX =  (int)(width * Math.random());
				int centerY = (int)(height * Math.random());
				Color randomHue = Color.hsb( 360*Math.random(), 1, 1);
				g.setStroke(randomHue);
				g.strokeOval(centerX - 50, centerY - 50, 100, 100);
			}
			break;
		default:
			g.setStroke(Color.BLACK);
			g.setLineWidth(4);
			for (int i = 0; i < 25; i++) {
				int centerX =  (int)(width * Math.random());
				int centerY = (int)(height * Math.random());
				int size = 30 + (int)(170*Math.random());
				Color randomColor =Color.color( Math.random(), Math.random(), Math.random() );
				g.setFill(randomColor);
				g.fillRect(centerX - size/2, centerY - size/2, size, size);
				g.strokeRect(centerX - size/2, centerY - size/2, size, size);
			}
			break;
		}
	}



	/**
	 * This method is called when the user clicks the Start button,
	 * If no thread is running, it sets the signaling variable, running, 
	 * to true and starts a new thread; it also changes
	 * the text on the Start button to "Stop". If the user clicks the button while
	 * a thread is running, then a signal is sent to the thread to terminate,
	 * by setting the value of the signaling variable, running, to false;
	 * and the method also sets the text on the Start button back to "Start."
	 */
	private void doStartOrStop() {
		if (running == false) { // start a thread
			startButton.setText("Stop");
			runner = new Runner();
			running = true;  // Set the signal before starting the thread!
			runner.start();
		}
		else { // stop the running thread
			
			startButton.setDisable(true);   // Disable button until thread exits,
											//   so user can't start another
											//   thread until after the current
											//   thread exits.

			/* Set the value of the signaling variable to false as a signal
			 * to the thread to terminate.
			 */

			running = false;
			redraw();  // Redraw the canvas, which will show only white since running = false.

			/* Wake the thread, in case it is sleeping, to get a more
			 * immediate reaction to the signal.
			 */

			runner.interrupt(); 

			/* Wait for the thread to stop before setting runner = null.
			 * One second should be plenty of time for this to happen, but
			 * in case something goes wrong, it's better not to wait forever.
			 */

			try {
				runner.join(1000);  // Wait for thread to stop.
			}
			catch (InterruptedException e) {
			}

			runner = null;

			startButton.setText("Start");
			startButton.setDisable(false);

		}
	}

} // end RandomArtWithThreads
