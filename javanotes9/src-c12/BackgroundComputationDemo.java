
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Screen;
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
 * This demo program uses a thread to compute an image "in the background".
 * As rows of pixels in the image are computed, they are copied to the
 * screen.  (The image is a small piece of the famous Mandelbrot set, which
 * is used just because it takes some time to compute.  There is no need
 * to understand what the image means.)  The user starts the computation by
 * clicking a "Start" button.  A separate thread is created and is run at
 * a lower priority, which will make sure that the GUI thread will get a
 * chance to run to repaint the display as necessary.  All changes to the
 * GUI from the animation thread are made by calling Platform.runLater().
 */
public class BackgroundComputationDemo extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//-----------------------------------------------------------------------------

	private Runner runner;  // the thread that computes the image

	private volatile boolean running;  // used to signal the thread to abort

	private Button startButton; // button the user can click to start or abort the thread

	private Canvas canvas;      // the canvas where the image is displayed
	private GraphicsContext g;  // the graphics context for drawing on the canvas
	
	private Color[] palette;    // the color palette, containing the colors of the spectrum

	int width, height;          // the size of the canvas

	
	/**
	 * Set up the GUI and event handling.  The canvas will be 1200-by-1000 pixels,
	 * if that fits comfortably on the screen; otherwise, size will be reduced to fit.
	 * This method also makes the color palette, containing colors in spectral order.
	 */
	public void start(Stage stage) {
		
		palette = new Color[256];
		for (int i = 0; i < 256; i++)
			palette[i] = Color.hsb(360*(i/256.0), 1, 1);
		
		int screenWidth = (int)Screen.getPrimary().getVisualBounds().getWidth();
		int screenHeight = (int)Screen.getPrimary().getVisualBounds().getHeight();
		width = Math.min(1200,screenWidth - 50);
		height = Math.min(1000, screenHeight - 120);
		
		canvas = new Canvas(width,height);
		g = canvas.getGraphicsContext2D();
		g.setFill(Color.LIGHTGRAY);
		g.fillRect(0,0,width,height);
		startButton = new Button("Start!");
		startButton.setOnAction( e -> doStartOrStop() );
		HBox bottom = new HBox(startButton);
		bottom.setStyle("-fx-padding: 6px; -fx-border-color:black; -fx-border-width: 2px 0 0 0");
		bottom.setAlignment(Pos.CENTER);
		BorderPane root = new BorderPane(canvas);
		root.setBottom(bottom);
		root.setStyle("-fx-border-color:black; -fx-border-width: 2px");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Demo: Background Computation in a Thread");
		stage.setResizable(false);
		stage.show();
	}
	
	
	/**
	 * This method is called from the animation thread when one row of pixels needs
	 * to be added to the image.
	 * @param rowNumber the row of pixels whose colors are to be set
	 * @param colorArray an array of colors, one for each pixel
	 */
	private void drawOneRow( int rowNumber, Color[] colorArray ) {
		for (int i = 0; i < width; i++) {
			   // Color an individual pixel by filling in a 1-by-1 pixel
			   // rectangle.  Not the most efficient way to do this, but
			   // good enough for this demo.
			g.setFill(colorArray[i]);
			g.fillRect(i,rowNumber,1,1);
		}
	}


	/**
	 * This method is called when the user clicks the Start button.
	 * If no thread is running, it sets the signaling variable, running,
	 * to true and creates and starts a new thread. Note that
	 * the thread is responsible for changing the text on the button.
	 * Note that the priority of the thread is set to be one less
	 * than the priority of the thread that calls this method, that
	 * is of the JavaFX application thread.  This means that the application
	 * thread is run in preference to the computation thread.  When there is an
	 * event to be handled, such as updating the display or reacting to a
	 * button click, the event-handling thread should wake up immediately
	 * to handle the event.
	 */
	private void doStartOrStop() {
		if (running == false) { // create a thread and start it
			startButton.setDisable(true);  // will be re-enabled by the thread
			g.setFill(Color.LIGHTGRAY);
			g.fillRect(0,0,width,height);
			runner = new Runner();
			try {
				runner.setPriority( Thread.currentThread().getPriority() - 1 );
			}
			catch (Exception e) {
			}
			running = true;  // Set the signal before starting the thread!
			runner.start();
		}
		else {  // stop the thread
			startButton.setDisable(true);  // will be re-enabled by the thread
			running = false;
			runner = null;
		}
	}


	/**
	 * This class defines the thread that does the computation.  The
	 * run method computes the image one pixel at a time.  After computing
	 * the colors for each row of pixels, the colors are copied into the
	 * image, and the part of the display that shows that row is repainted.
	 * All modifications to the GUI are made using Platform.runLater().
	 * (Since the thread runs in the background, at lower priority than
	 * the event-handling thread, the event-handling thread wakes up
	 * immediately to repaint the display.)
	 */
	private class Runner extends Thread {
		double xmin, xmax, ymin, ymax;
		int maxIterations;
		Runner() {
			xmin = -1.6744096740931858;
			xmax = -1.674409674093473;
			ymin = 4.716540768697223E-5;
			ymax = 4.716540790246652E-5;
			maxIterations = 10000;
		}
		public void run() {
			try {
				Platform.runLater( () -> startButton.setDisable(false) );
				Platform.runLater( () -> startButton.setText("Abort!") );
				double x, y;
				double dx, dy;
				dx = (xmax-xmin)/(width-1);
				dy = (ymax-ymin)/(height-1);
				for (int row = 0; row < height; row++) {  // Compute one row of pixels.
					final Color[] rgb = new Color[width];
					y = ymax - dy*row;
					for (int col = 0; col < width; col++) {
						x = xmin + dx*col;
						int count = 0;
						double xx = x;
						double yy = y;
						while (count < maxIterations && (xx*xx + yy*yy) < 4) {
							count++;
							double newxx = xx*xx - yy*yy + x;
							yy = 2*xx*yy + y;
							xx = newxx; 
						}
						if (count == maxIterations)
							rgb[col] = Color.BLACK;
						else
							rgb[col] = palette[count%palette.length];
						if (! running) {  // Check for the signal to abort the computation.
							return;
						}
					}
					final int rowNum = row;
					Platform.runLater( () -> drawOneRow(rowNum,rgb) );
				}
			}
			finally {
				 // Make sure the state is correct after the thread ends for any reason.
				Platform.runLater( () -> startButton.setText("Start Again") );
				Platform.runLater( () -> startButton.setDisable(false) );
				running = false;
				runner = null;
			}
		}
	}

}
