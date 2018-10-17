
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

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * This demo program divides up a large computation into a fairly
 * large number of smaller tasks.  The computation is to compute
 * an image, and each task computes one row of pixels in the image.
 * 
 * The functionality of this program is identical to MultiprocessingDemo3.
 * This version of the program uses an ExecutorService to execute the tasks.
 * When the user clicks "Start", a new ExecutorService is created and all
 * of the tasks that are part of computing the image are added to it.
 * If the user aborts a computation, the executor's shutDownNow() method
 * is called, which makes the executor drop any waiting tasks from its
 * queue.
 * 
 * (The image is a small piece of the famous Mandelbrot set,
 * which is used just because it takes some time to compute.  
 * There is no need to understand what the image means.)  
 */
public class MultiprocessingDemo4 extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//-----------------------------------------------------------------------------------

	private ExecutorService executor;  // The executor that executes the MandelbrotTasks.
	                                   // When a job is started, an executor is created to
	                                   // execute the tasks that make up that job.  (A job
	                                   // consists of computing a complete image; a task is
	                                   // computing one line of the image.)  The value of
	                                   // this variable is null when no job is in progress.

	private int tasksRemaining; // How many tasks in the current job still remain to be done?
	                            // (Note: the variables executor and tasksRemaining can be
	                            // modified by various threads.  They are not volatile because
	                            // all access is done in synchronized methods.)

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
		stage.setTitle("Multiprocessing Demo 4");
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
	 * This method is called when the user clicks the Start button.
	 * If no computation is in progress, it clears the image
	 * and sets up the computation of a new image.  The first time
	 * that it is called, it is also responsible for creating the
	 * the thread pool.
	 */
	synchronized private void doStartOrStop() {
		if (executor != null) { // a job is in progress
			startButton.setText("Start Again");
			executor.shutdownNow(); // Drop any remaining jobs.
			executor = null;  // signals that now no job is progress
		}
		else {  // start a new job
			int processors =  Runtime.getRuntime().availableProcessors();
			executor = Executors.newFixedThreadPool(processors);
			
			startButton.setText("Abort"); // change name while computation is in progress
			g.setFill(Color.LIGHTGRAY);  // fill canvas with gray
			g.fillRect(0,0,width,height);

			tasksRemaining = height;

			double xmin = -1.6744096740931858;
			double xmax = -1.674409674093473;
			double ymin = 4.716540768697223E-5;
			double ymax = 4.716540790246652E-5;
			int maxIterations = 10000;
			double dx = (xmax-xmin)/(width-1);
			double dy = (ymax-ymin)/(height-1);
			for (int row = 0; row < height; row++) { // Add tasks for current job to job queue.
				double y = ymax - row*dy;
				MandelbrotTask task = new MandelbrotTask(
						               executor, row, width, maxIterations, xmin, y, dx);
				executor.execute(task);
			}
			executor.shutdown();  // Will shut down after completing submitted tasks.
		}
	}
	

	/**
	 * This method is called by each thread when it terminates.  We keep track
	 * of the number of threads that have terminated, so that when they have
	 * all finished, we can put the program into the correct state, such as
	 * changing the name of the button to "Start Again" and re-enabling the
	 * pop-up menu.  This method is responsible for drawing the row of
	 * pixels computed by the task to the canvas.  It only does that if
	 * the task is part of the current job, not a previous, aborted job.
	 */
	synchronized private void taskFinished(MandelbrotTask task) {
		if (task.myExecutor != executor) {
			    // The task is part of a previous job.  Ignore it.
			    // (executor in this case is probably null, but could be
			    // an executor running the next job.  In either case, this
			    // task is from a previous job.)
			System.out.println("Dropping results from previous job."); // for testing
			return;
		}
		Platform.runLater( () -> drawOneRow(task.rowNumber, task.rgb) );
		tasksRemaining--;
		if (tasksRemaining == 0) { // all threads have finished
			Platform.runLater( () -> startButton.setText("StartAgain") );
			executor = null;  // signals that now no job is in progress
		}
	}


	/**
	 * An object of type MandelbrotTask represents the task of computing one row
	 * of pixels in an image of the Mandelbrot set.  The task has a run() method
	 * that does the actual computation.  It also calls the taskFinished() method
	 * before terminating.  It does not draw the row of pixels to the canvas,
	 * because it is possible that the task completes after a job has been
	 * aborted.  In that case, the data should be discarded.
	 */
	private class MandelbrotTask implements Runnable {
		ExecutorService myExecutor;  // Which Executor will execute this task?
		                             // This is used in taskFinished to avoid
		                             // processing the result from a task that is
		                             // part of a previous job.
		int rowNumber;  // Which row of pixels does this task compute?
		double xmin;    // The x-value for the first pixel in the row.
		double y;       // The y-value for all the pixels in the row.
		double dx;      // The change in x-value from one pixel to the next.
		int maxIterations;  // The maximum count in the Mandelbrot algorithm.
		Color[] rgb;     // The colors computed for the pixels.
		MandelbrotTask( ExecutorService executor, int rowNumber, int width, 
				              int maxIterations, double xmin, double y, double dx) {
			this.myExecutor = executor;
			this.rowNumber = rowNumber;
			this.maxIterations = maxIterations;
			this.xmin = xmin;
			this.y = y;
			this.dx = dx;
			rgb = new Color[width];
		}
		public void run() {
			for (int i = 0; i < rgb.length; i++) {
				double x = xmin + i * dx;
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
					rgb[i] = Color.BLACK;
				else
					rgb[i] = palette[count % 256];
			}
			taskFinished(this);
		}
	}


} // end MultiprocessingDemo4
