
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 * This demo program uses several threads to compute an image "in the background".
 * It is a modification of the BackgroundComputationDemo, which used only one thread.
 * 
 * As rows of pixels in the image are computed, they are copied to the
 * screen.  (The image is a small piece of the famous Mandelbrot set, which
 * is used just because it takes some time to compute.  There is no need
 * to understand what the image means.)  The user starts the computation by
 * clicking a "Start" button.  A pop-up menu allows the user to select the
 * number of threads to be used.  The specified number of threads is created 
 * and each thread is assigned a region in the image.  The threads are run
 * at lower priority, which will make sure that the GUI thread will get a
 * chance to run to repaint the display as necessary.
 */
public class MultiprocessingDemo1 extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//---------------------------------------------------------------------
	
	
	private Runner[] workers;  // the threads that compute the image
	
	private volatile boolean running;  // used to signal the thread to abort
	
	private volatile int threadsRunning; // how many threads are still running?
	
	private Button startButton; // button the user can click to start or abort the thread
	
	private ComboBox<String> threadCountSelect;  // for specifying the number of threads to be used
	
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
		threadCountSelect = new ComboBox<String>();
		threadCountSelect.setEditable(false);
		threadCountSelect.getItems().add("Use 1 thread.");
		threadCountSelect.getItems().add("Use 2 threads.");
		threadCountSelect.getItems().add("Use 3 threads.");
		threadCountSelect.getItems().add("Use 4 threads.");
		threadCountSelect.getItems().add("Use 5 threads.");
		threadCountSelect.getItems().add("Use 6 threads.");
		threadCountSelect.getItems().add("Use 7 threads.");
		threadCountSelect.getItems().add("Use 8 threads.");
		threadCountSelect.getSelectionModel().select(1);
		HBox bottom = new HBox(8,startButton,threadCountSelect);
		bottom.setStyle("-fx-padding: 6px; -fx-border-color:black; -fx-border-width: 2px 0 0 0");
		bottom.setAlignment(Pos.CENTER);
		BorderPane root = new BorderPane(canvas);
		root.setBottom(bottom);
		root.setStyle("-fx-border-color:black; -fx-border-width: 2px");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Multiprocessing Demo 1");
		stage.setResizable(false);
		stage.show();
	}
	
	
	/**
	 * This method is called from the computation threads when one row of pixels needs
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
	 * This method is called when the user clicks the button.  If
	 * no computation is currently running, it starts as many new
	 * threads as the user has specified, and assigns a different part
	 * of the image to each thread.  The threads are run at lower
	 * priority than the event-handling thread, in order to keep the
	 * GUI responsive.  If a computation is in progress when this
	 * method is called, running is set to false as a signal to stop
	 * all of the threads.  
	 */
	private void doStartOrStop() {
		if (running) {
			startButton.setDisable(true); // will be re-enabled when all threads have stopped
			   // (prevent user from trying to stop threads that are already stopping)
			running = false;  // signal the threads to stop
		}
		else {
			startButton.setText("Abort"); // change name while computation is in progress
			threadCountSelect.setDisable(true); // will be re-enabled when all threads finish
			g.setFill(Color.LIGHTGRAY);  // fill canvas with gray
			g.fillRect(0,0,width,height);
			int threadCount = threadCountSelect.getSelectionModel().getSelectedIndex() + 1;
			workers = new Runner[threadCount];
			int rowsPerThread;  // How many rows of pixels should each thread compute?
			rowsPerThread  = height / threadCount;
			running = true;  // Set the signal before starting the threads!
			threadsRunning = threadCount;  // Records how many of the threads are still running
			for (int i = 0; i < threadCount; i++) {
				int startRow;  // first row computed by thread number i
				int endRow;    // last row computed by thread number i
				   // Create and start a thread to compute the rows of the image from
				   // startRow to endRow.  Note that we have to make sure that
				   // the endRow for the last thread is the bottom row of the image.
				startRow = rowsPerThread*i;
				if (i == threadCount-1)
					endRow = height-1;
				else
					endRow = rowsPerThread*(i+1) - 1;
				workers[i] = new Runner(startRow, endRow);
				try {
					workers[i].setPriority( Thread.currentThread().getPriority() - 1 );
				}
				catch (Exception e) {
				}
				workers[i].start();
			}
		}
	}
		
	
	/**
	 * This method is called by each thread when it terminates.  We keep track
	 * of the number of threads that have terminated, so that when they have
	 * all finished, we can put the program into the correct state, such as
	 * changing the name of the button to "Start Again" and re-enabling the
	 * pop-up menu.
	 */
	synchronized private void threadFinished() {
		threadsRunning--;
		if (threadsRunning == 0) { // all threads have finished
			Platform.runLater( () -> {
				   // Make sure state is correct when threads end.
				startButton.setText("Start Again");
				startButton.setDisable(false);
				threadCountSelect.setDisable(false);
			});
			running = false; // Make sure running is false after the thread ends.
			workers = null;
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
		int startRow, endRow;
		Runner(int startRow, int endRow) {
			this.startRow = startRow;
			this.endRow = endRow;
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
				for (int row = startRow; row <= endRow; row++) {  // Compute one row of pixels.
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
				threadFinished(); // Make sure this is called when the thread finishes for any reason.
			}
		}
	}

} // end MultiprocessingDemo1
