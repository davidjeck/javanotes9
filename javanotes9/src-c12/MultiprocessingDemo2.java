
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

import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * This demo program divides up a large computation into a fairly
 * large number of smaller tasks.  The computation is to compute
 * an image, and each task computes one row of pixels in the image.
 * The tasks are placed into a thread-safe queue.  Several "worker" 
 * threads remove tasks from the queue and carry them out.  When
 * all the tasks have completed, the worker threads terminate.
 * The number of worker threads is specified by the user.
 * (The image is a small piece of the famous Mandelbrot set,
 * which is used just because it takes some time to compute.  
 * There is no need to understand what the image means.)  
 */
public class MultiprocessingDemo2 extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//---------------------------------------------------------------------
	

	private WorkerThread[] workers;  // the threads that compute the image

	private ConcurrentLinkedQueue<Runnable> taskQueue;  // holds individual tasks

	private volatile int threadsRunning; // how many threads are still running?
	
	private volatile boolean running;  // used to signal the thread to abort

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
		threadCountSelect.getItems().add("Use 9 threads.");
		threadCountSelect.getItems().add("Use 10 threads.");
		threadCountSelect.getItems().add("Use 20 threads.");
		threadCountSelect.getSelectionModel().select(1);
		HBox bottom = new HBox(8,startButton,threadCountSelect);
		bottom.setStyle("-fx-padding: 6px; -fx-border-color:black; -fx-border-width: 2px 0 0 0");
		bottom.setAlignment(Pos.CENTER);
		BorderPane root = new BorderPane(canvas);
		root.setBottom(bottom);
		root.setStyle("-fx-border-color:black; -fx-border-width: 2px");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Multiprocessing Demo 2");
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
	 * This method is called when the user clicks the button,
	 * If no computation is in progress, then it starts as many new
	 * threads as the user has specified.  It creates one 
	 * MandelbrotTask object for each row of the image and places
	 * all the tasks into a queue.  The threads will remove tasks
	 * from the queue to process them.  The threads are run at lower
	 * priority than the event-handling thread, in order to keep the
	 * GUI responsive. 
	 *    If this method is called when a computation is in progress,
	 * it sets the value of the signal variable, running to false,
	 * as a signal to the threads that they should terminate.
	 */
	private void doStartOrStop() {
		if (running) {
			startButton.setDisable(true);  // will be re-enabled when all threads finish
			running = false;
		}
		else {
			startButton.setText("Abort"); // change name while computation is in progress
			threadCountSelect.setDisable(true); // will be re-enabled when all threads finish
			g.setFill(Color.LIGHTGRAY);  // fill canvas with gray
			g.fillRect(0,0,width,height);
	
			taskQueue = new ConcurrentLinkedQueue<Runnable>();
	
			double xmin = -1.6744096740931858;
			double xmax = -1.674409674093473;
			double ymin = 4.716540768697223E-5;
			double ymax = 4.716540790246652E-5;
			int maxIterations = 10000;
			double dx = (xmax-xmin)/(width-1);
			double dy = (ymax-ymin)/(height-1);
			for (int row = 0; row < height; row++) {
				double y = ymax - row*dy;
				MandelbrotTask task = new MandelbrotTask(row, width, maxIterations, xmin, y, dx);
				taskQueue.add(task);  // Tasks must be added to the queue before threads are started!
			}
	
			int threadCount = threadCountSelect.getSelectionModel().getSelectedIndex() + 1;
			if (threadCount == 11)
				threadCount = 20;
			workers = new WorkerThread[threadCount];
			running = true;  // Set the signal variable before starting the threads!
			threadsRunning = threadCount;  // Records how many of the threads are still running
			for (int i = 0; i < threadCount; i++) {
				workers[i] = new WorkerThread();
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
	 * An object of type MandelbrotTask represents the task of computing one row
	 * of pixels in an image of the Mandelbrot set.  The task has a run() method
	 * that does the actual computation and also applies the colors that it has
	 * computed to the image on the screen.
	 */
	private class MandelbrotTask implements Runnable {
		int rowNumber;  // Which row of pixels does this task compute?
		double xmin;    // The x-value for the first pixel in the row.
		double y;       // The y-value for all the pixels in the row.
		double dx;      // The change in x-value from one pixel to the next.
		int width;      // The number of pixels in the row.
		int maxIterations;  // The maximum count in the Mandelbrot algorithm.
		MandelbrotTask( int rowNumber, int width, int maxIterations, double xmin, double y, double dx) {
			this.rowNumber = rowNumber;
			this.maxIterations = maxIterations;
			this.xmin = xmin;
			this.y = y;
			this.dx = dx;
			this.width = width;
		}
		public void run() {
			Color[] rgb = new Color[width];     // The colors computed for the pixels.
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
			Platform.runLater( () -> drawOneRow(rowNumber, rgb) );
		}
	}


	/**
	 * This class defines the worker threads that carry out the tasks. 
	 * A WorkerThread runs in a loop in which it retrieves a task from the 
	 * taskQueue and calls the run() method in that task.  The thread 
	 * terminates when the queue is empty.  (Note that for this to work 
	 * properly, all the tasks must be placed into the queue before the
	 * thread is started.  If the queue is empty when the thread starts,
	 * the thread will simply exit immediately.)  The thread also terminates
	 * if the signal variable, running, is set to false.  Just before it
	 * terminates, the thread calls the threadFinished() method.
	 */
	private class WorkerThread extends Thread {
		public void run() {
			try {
				while (running) {
					Runnable task = taskQueue.poll();
					if (task == null)
						break;
					task.run();
				}
			}
			finally {
				threadFinished();
			}
		}
	}


}
