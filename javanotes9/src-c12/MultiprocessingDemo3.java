
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

import java.util.concurrent.LinkedBlockingQueue;

/**
 * This demo program divides up a large computation into a fairly
 * large number of smaller tasks.  The computation is to compute
 * an image, and each task computes one row of pixels in the image.
 * 
 * A thread pool is created at the beginning of the program, with
 * one thread for each available processor.  The threads remove
 * tasks from a blocking queue and execute them.  The threads never
 * terminate (until the program ends).  To start a computation, tasks
 * are created and added to the blocking queue.  As soon as the 
 * first tasks are added to the queue, the threads "wake up" and
 * start working on them.
 * 
 * (The image is a small piece of the famous Mandelbrot set,
 *  which is used just because it takes some time to compute.  
 * There is no need to understand what the image means.)  
 */
public class MultiprocessingDemo3 extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//-----------------------------------------------------------------------------------

	private LinkedBlockingQueue<Runnable> taskQueue;  // The queue that holds individual tasks.

	private boolean jobInProgress; // Set to true when a job starts, false when it ends.

	private int jobNumber;  // Job number of the current computation job.
                            // A "job" is the computation of an entire image,
                            // whereas a "task" is one part of a job.
							// jobNumber is incremented after a job has completed.
							// Note that any left-over tasks from a job are ignored
							// when they finally complete.  This can happen when the
							// user aborts a computation, since if a thread is working
							// on a task when that happens, it will continue to work
							// on that task until the task completes, so the task can
							// complete after the job of which it is a part has been
							// aborted.  (Note:  The task itself could check the current
							// job number as it is running and terminate early if the
							// job number has changed.  This would make sense if the
							// task's computation were very long.)

	private int tasksRemaining; // How many tasks in the current job still remain to be done?
	    // (REMARK: jobInProgress, jobNumber, and tasksRemaining can all be modified by
	    //  more than one thread.  They do not need to be volatile because all access to
	    //  these variables is done in synchronized methods.)

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
		stage.setTitle("Multiprocessing Demo 3");
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
	 * This is called the first time the user clicks "Start" to 
	 * create the thread pool and the blocking queue of tasks that
	 * is used to send tasks to the thread pool.  The number of 
	 * threads is equal to the number of available processors.
	 * There is no need to keep any references to the thread
	 * objects.  They only have to be started here, and they
	 * will just keep running until the program ends.
	 */
	private void createThreadPool() {
		taskQueue = new LinkedBlockingQueue<Runnable>();
		int processors = Runtime.getRuntime().availableProcessors();
		for (int i = 0; i < processors; i++) {
			   // Threads will block while waiting for
			   // tasks to arrive in the queue.
			WorkerThread worker = new WorkerThread();
			worker.start();
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
		if (jobInProgress) {
			startButton.setText("Start Again");
			taskQueue.clear();
			jobNumber++;
			jobInProgress = false;
		}
		else {
			createThreadPool();
			
			startButton.setText("Abort"); // change name while computation is in progress
			g.setFill(Color.LIGHTGRAY);  // Fill canvas with gray
			g.fillRect(0,0,width,height);

			tasksRemaining = height;
			jobInProgress = true;

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
						               jobNumber, row, width, maxIterations, xmin, y, dx);
				taskQueue.add(task);
			}
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
		if (task.jobNumber != jobNumber) {
			    // The task is part of a previous job.  Ignore it.
			System.out.println("Dropping results from previous job."); // for testing
			return;
		}
		Platform.runLater( () -> drawOneRow(task.rowNumber, task.rgb) );
		tasksRemaining--;
		if (tasksRemaining == 0) { // all threads have finished
			Platform.runLater( () -> startButton.setText("StartAgain") );
			taskQueue.clear();
			jobNumber++;
			jobInProgress = false;
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
		int jobNumber;  // Which job is this task part of?
		int rowNumber;  // Which row of pixels does this task compute?
		double xmin;    // The x-value for the first pixel in the row.
		double y;       // The y-value for all the pixels in the row.
		double dx;      // The change in x-value from one pixel to the next.
		int maxIterations;  // The maximum count in the Mandelbrot algorithm.
		Color[] rgb;     // The colors computed for the pixels.
		MandelbrotTask( int jobNumber, int rowNumber, int width, 
				              int maxIterations, double xmin, double y, double dx) {
			this.jobNumber = jobNumber;
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


	/**
	 * This class defines the worker threads that make up the thread pool.
	 * A WorkerThread runs in a loop in which it retrieves a task from the 
	 * taskQueue and calls the run() method in that task.  Note that if
	 * the queue is empty, the thread blocks until a task becomes available
	 * in the queue.  The thread will run at a priority that is one less
	 * than the priority of the thread that calls the constructor.
	 * 
	 * A WorkerThread is designed to run in an infinite loop.  It will
	 * end only when the Java virtual machine exits. (This assumes that
	 * the tasks that are executed don't throw exceptions, which is true
	 * in this program.)  The constructor sets the thread to run as
	 * a daemon thread; the Java virtual machine will exit when the
	 * only threads are daemon threads. 
	 */
	private class WorkerThread extends Thread {
		WorkerThread() {
			try {
				setPriority( Thread.currentThread().getPriority() - 1);
			}
			catch (Exception e) {
			}
			try {
				setDaemon(true);
			}
			catch (Exception e) {
			}
		}
		public void run() {
			while (true) {
				try {
					Runnable task = taskQueue.take();
					task.run();
				}
				catch (InterruptedException e) {
				}
			}
		}
	}


} // end MultiprocessingDemo3
