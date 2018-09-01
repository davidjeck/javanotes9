package edu.hws.eck.mdbfx;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.nio.IntBuffer;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A canvas that can display a Mandelbrot set.  A call to startJob() will tell it
 * what portion of the set to display and will provide the maximum number of iterations
 * to use and a palette for coloring the pixels.  Computations are done by background
 * threads.  The current computation, if any, can be aborted by calling stopJob().
 * All methods in this class should be called on the JavaFX application thread.
 */
public class MandelbrotCanvas extends Canvas {
	
	/* A PIXEL_FORMAT is needed for the PixelWriter method that writes multiple pixels. */
	private static PixelFormat<IntBuffer> PIXEL_FORMAT = PixelFormat.getIntArgbPreInstance();

	/* The value of this property is set to true when a computation is in progress. */
	private SimpleBooleanProperty working = new SimpleBooleanProperty(false);
	
	private int[] palette;   // The palette of colors used to color the pixels.
	                         // This is set in startJob, which is only called from MandebrotPane.
	                         // The palette holds colors represented as ints in AGBR format.
	                         
	private int[][] iterationCounts;     // The iteration counts for any rows that have been computed;
	                                     // iterationCounts[i] contains the counts for row number i.
	                                     // All rows are empty at the start of a computation.
	                                     // These are saved so that new palettes can be applied
	                                     // without recomputing the iteration counts.
	                         
	private volatile int currentJobNum;  // This is incremented when a job is stopped.  Results from tasks in
	                                     // a job that are completed after the job is stopped are discarded.
	
	private int tasksRemainingInJob;     // This is decremented by a MandelbrotTask when it completes.
	                                     // When it reaches 0, stopJob() is called.
	
	private LinkedBlockingQueue<MandelbrotTask> taskQueue; // For sending tasks to worker threads.
	
	private GraphicsContext g;       // Graphics context for this canvas.
	private PixelWriter pixelWriter; // PixelWriter for setting pixel colors in this canvas.
	
	
	/**
	 * Create the canvas with a given width and height.  The constructor
	 * creates the thread pool of worker threads that do the computations.
	 * The canvas is initially fully transparent.
	 */
	public MandelbrotCanvas(int width, int height) {
		super(width,height);
		g = getGraphicsContext2D();
		taskQueue = new LinkedBlockingQueue<>();
		int processors = Runtime.getRuntime().availableProcessors();
		for (int i = 0; i < processors; i++) {
			new WorkerThread().start();
		}
	}
	
	
	/**
	 * Returns an observable boolean property whose value is true when a
	 * computation is in progress in this canvas.
	 */
	public BooleanProperty workingProperty() {
		return working;
	}
	
	
	/**
	 * Change the palette that is used to color pixels.  The palette will be
	 * applied immediately to any pixels that have already been computed,
	 * and if a computation is in progress, it will also be used when new
	 * pixels are computed.
	 */
	public void setPalette( int[] palette ) {
		this.palette = palette;
		if (iterationCounts != null) {
			int width = (int)getWidth();
			int[] colors = new int[width];
			for (int row = 0; row < iterationCounts.length; row++) {
				if (iterationCounts[row] != null) {
					int[] counts = iterationCounts[row];
					for (int i = 0; i < width; i++) {
						colors[i] = counts[i] == -1? 0xFF000000 : palette[counts[i] % palette.length];
					}
					pixelWriter.setPixels(0, row, width, 1, PIXEL_FORMAT, colors, 0, width);
				}
			}
		}
	}
		
	
	/**
	 * Start a computation to compute colors for pixels in the region bounded by
	 * xmin, xmax, ymin, and ymax.  The computation does up to maxIterations per
	 * pixel; if the computation does not end before that limit is reached,
	 * the pixel will be black.  The palette is used to color pixels.
	 */
	public void startJob(int maxIterations, int[] palette,
	                               double xmin, double xmax, double ymin, double ymax) {
		stopJob();
		working.set(true);
		this.palette = palette;
		g.clearRect(0,0,getWidth(),getHeight());
		pixelWriter = g.getPixelWriter();
		tasksRemainingInJob = (int)getHeight();
		iterationCounts = new int[tasksRemainingInJob][];
		int count = (int)getWidth();
		double dx = (xmax - xmin) / (count-1);
		double dy = (ymax - ymin) / (tasksRemainingInJob - 1);
		double x = xmin + dx/2;
		double y = ymax - dy/2;
		for (int i = 0; i < tasksRemainingInJob; i++) {
			    // Create one task for each row of the image,
			    // and add the tasks to the task queue.
			MandelbrotTask task = new MandelbrotTask();
			task.count = count;
			task.jobNumber = currentJobNum;
			task.rowNumber = i;
			task.maxIterations = maxIterations;
			task.xmin = x;
			task.dx = dx;
			task.y = y;
			y -= dy;
			taskQueue.add(task);
		}
	}
	
	
	/**
	 * Terminates the current computation, if there is one.
	 */
	public void stopJob() {
		taskQueue.clear();
		currentJobNum++; // stop tasks from previous jobs from being processed
		working.set(false);
	}
		
	
	/**
	 * A MandelbrotTask will compute the iteration counts for one
	 * row of pixels in the mandelbrot image, and it will apply the
	 * corresponding colors to pixels in that row.  (But if the
	 * jobNumber recorded in this task is not equal to the
	 * currentJobNumber in the canvas, results are just discarded.)
	 */
	private class MandelbrotTask implements Runnable {
		double xmin; // x-value at left edge of canvas
		double dx;   // x-increment going from one pixel to the next
		double y;    // y-value for this row of pixels
		int count;   // number of pixels in this row
		int maxIterations;  // maximum number of iterations to compute
		int rowNumber;  // which row of pixels does this task work on
		int jobNumber;  // which job is this task a part of
		public void run() {
			int[] counts = new int[count];
			for (int i = 0; i < count; i++) {
				double x0 = xmin + i * dx;
				double y0 = y;
				double a = x0;
				double b = y0;
				int ct = 0;
				while (a*a + b*b < 4.1) {  // The mandelbrot iteration
					ct++;
					if (ct > maxIterations) {
						ct = -1;
						break;
					}
					double newa = a*a - b*b + x0;
					b = 2*a*b + y0;
					a = newa;
				}
				counts[i] = ct;
				if (jobNumber != currentJobNum) {
					   // The canvas has moved on to another job.
					return;
				}
			}
			Platform.runLater( () -> {
				    // Apply data to pixels in the canvas.  This must be done
				    // on the JavaFX application thread.
				if (jobNumber == currentJobNum) {
					int[] colors = new int[count];
					for (int i = 0; i < count; i++) {
						colors[i] = counts[i] == -1? 0xFF000000 : palette[counts[i] % palette.length];
					}
					pixelWriter.setPixels(0, rowNumber, count, 1, PIXEL_FORMAT, colors, 0, count);
					iterationCounts[rowNumber] = counts;
					tasksRemainingInJob--;
					if (tasksRemainingInJob <= 0)
						stopJob();
				}
			} );
		}
	}
	
	
	/**
	 * This class defines the worker threads that make up the thread pool.
	 * A WorkerThread runs in a loop in which it retrieves a task from the 
	 * taskQueue and calls the run() method in that task.  Note that if
	 * the queue is empty, the thread blocks until a task becomes available
	 * in the queue.  The thread will run at a priority the is one less
	 * than the priority of the thread that calls the constructor.
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

}
