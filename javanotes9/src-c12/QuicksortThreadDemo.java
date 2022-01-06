
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
 * A program that can show a demonstration of Quicksort in action.
 * The items to be sorted are the hues of a set of vertical bars.
 * When sorted, the bars form a spectrum from red to violet.
 * Initially, the bars are sorted.  There is a Start button.  When
 * the user clicks this button, the order of the bars is randomized
 * and then Quicksort is applied.  During the sort, a black bar
 * marks the location of an "empty" space in the array.  
 * The user can abort the sort by clicking the button again.
 * 
 * The main point of this program is to demonstrate threads, with
 * very simple inter-thread communication. The recursive Quicksort
 * algorithm is run in a separate thread. All shanges to the canvas
 * by that thread are made using Platform.runLater().  The abort 
 * operation is implemented by setting the value of a volatile variable 
 * that is checked periodically by the thread.  When the user aborts
 * the sort before it finishes, the value of the variable changes;
 * the thread sees the change and exits.
 */
public class QuicksortThreadDemo extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//-----------------------------------------------------------------

	private final static int ARRAY_SIZE = 100;  // The number of colored bars;
	                                            // canvas width will be 6*ARRAY_SIZE.

	private int[] hue = new int[ARRAY_SIZE];  // The array that will be sorted.
	private Color[] palette = new Color[ARRAY_SIZE]; // Colors in spectral order.
	private Canvas canvas;      // The panel that displays the colored bars.
	private GraphicsContext g;  // A graphics context for drawing on the canvas.
	private Button startButton; // The button that starts and stops the demo.

	private Runner runner; // The thread that runs the recursion.
	private volatile boolean running;   // Set to true while recursion is running;
										// this is set to false as a signal to the
										// thread to abort.


	/**
	 * Set up the GUI and event-handling.  Also fills the palette array
	 * with colors in spectral order.
	 */
	public void start(Stage stage) {
		for (int i = 0; i < ARRAY_SIZE; i++) {
			palette[i] = Color.hsb((310.0*i)/ARRAY_SIZE, 1, 1);
		}
		canvas = new Canvas(6+6*ARRAY_SIZE, 206);
		g = canvas.getGraphicsContext2D();
		drawSorted();  // initial drawing of canvas, with sorted colors.
		startButton = new Button("Start!");
		startButton.setOnAction( e -> doStartOrStop() );
		HBox bottom = new HBox(startButton);
		bottom.setStyle("-fx-padding: 6px");
		bottom.setAlignment(Pos.CENTER);
		BorderPane root = new BorderPane(canvas);
		root.setBottom(bottom);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Demo: Quicksort in a Thread");
		stage.setResizable(false);
		stage.show();
	}


	/**
	 * When the user aborts the recursion before it finishes, an exception of
	 * this type is thrown to end the recursion cleanly.
	 */
	private class ThreadTerminationException extends RuntimeException {
	}

	
	/**
	 * Redraws the entire canvas, with colors in sorted order.  This method
	 * is ALWAYS called on the application thread.  It is called in the
	 * start() method to draw the initial contents of the canvas, and it
	 * is called when the animation thread exits, to make sure that
	 * the colors are shown in sorted order at that time.
	 */
	private void drawSorted() {
		g.setFill(Color.GRAY);
		g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
		double barWidth = (double)(canvas.getWidth() - 6) / palette.length;
		double h = canvas.getHeight() - 6;
		for (int i = 0; i < palette.length; i++) {
			int x1 = 3 + (int)(i*barWidth + 0.49);
			int x2 = 3 + (int)((i+1)*barWidth + 0.49);
			int w = x2 - x1;
			g.setFill( palette[i] );
			g.fillRect(x1,3,w,h);
		}
	}
	
	
	/**
	 * Change one of the values in the hue array, and redraw the corresponding
	 * vertical line on the canvas in the new color.  This method is ALWAYS
	 * called on the animation thread, not the application thread.  It uses
	 * Platform.runLater() to draw the line that needs to change color on the canvas.
	 * @param index  the index of the element in the hue array that is changed
	 * @param colorNumber  the new value for the element in the hue array.
	 *             If the value is -1, the new color is black; otherwise,
	 *             colorNumber is an index into the palette array.
	 */
	private void setHue( int index, int colorNumber ) {
		hue[index] = colorNumber;
		Platform.runLater( () -> {
			double barWidth = (double)(canvas.getWidth() - 6) / palette.length;
			double h = canvas.getHeight() - 6;
			int x1 = 3 + (int)(index*barWidth + 0.49);
			int x2 = 3 + (int)((index+1)*barWidth + 0.49);
			int w = x2 - x1;
			if (colorNumber == -1)
				g.setFill(Color.BLACK);
			else
				g.setFill(palette[colorNumber]);
			g.fillRect(x1,3,w,h);
		});
	}	
	

	/**
	 * This method is called when the user clicks the Start button,
	 * If no thread is running, it starts a new thread, after setting
	 * the signaling variable, running, to true; it also changes the text
	 * on the Start button to "Finish". If the user clicks the button while
	 * a thread is running, then a signal is sent to the thread to terminate,
	 * by setting the value of the signaling variable, running, to false.
	 * Note that the thread changes the text on the button back
	 * to "Start" before it terminates.
	 */
	private void doStartOrStop() {
		if (running == false) { // start a thread
			startButton.setText("Finish");
			runner = new Runner();
			running = true;  // Set the signal before starting the thread!
			runner.start();
		}
		else { // stop the running thread

			/* Set the value of the signaling variable to false as a signal
			 * to the thread to terminate.  When this is seen in the
			 * recursive algorithm, it will throw a ThreadTerminationException
			 * to terminate the thread.
			 */

			running = false; 

			/* Wake the thread, in case it is sleeping, to get a more
			 * immediate reaction to the signal.
			 */

			runner.interrupt(); 

		}
	}



	/**
	 * This method is called frequently by the thread that is running
	 * the recursion, in order to insert delays.  The delay
	 * will give the system a chance to update the display, and it
	 * gives the user a chance to see what is going on in the sort.
	 * Since this method is called regularly while the recursion is in 
	 * progress, it is also used as a convenient place to check the value
	 * of the signaling variable, running.  If the value of running has
	 * been set to false, this method throws an exception of type
	 * ThreadTerminationException.  This exception will cause all active
	 * levels of the recursion to be terminated.  It is caught in the
	 * run() method of the thread.
	 * @param millis  The number of milliseconds to sleep.
	 */
	private void delay(int millis) {
		if (! running)
			throw new ThreadTerminationException();
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
		}
		if (! running)
			throw new ThreadTerminationException();
	}


	/**
	 * The basic non-recursive QuickSortStep algorithm, which
	 * uses hue[lo] as a "pivot" and rearranges elements of the 
	 * hue array from positions lo through hi so that
	 * the pivot value is in its correct location, with smaller
	 * items to the left and bigger items to the right.  The
	 * position of the pivot is returned.  In this version,
	 * we conceptually remove the pivot from the array, leaving
	 * an empty space.  The space is marked by a -1, and it moves
	 * around as the algorithm proceeds.  It is shown as a black
	 * bar in the display. Every time a change is made, the
	 * delay() method is called to insert a 1/10 second delay
	 * to let the user see the change.  All changes to the hue
	 * array are made by calling setHue(), which also changes the
	 * color of the corresponding line on the canvas.
	 */
	private int quickSortStep(int lo, int hi) {
		int pivot = hue[lo];  // Save pivot item.
		setHue( lo, -1);  // Mark location lo as empty.
		delay(100);
		while (true) {
			while (hi > lo  && hue[hi] > pivot)
				hi--;
			if (hi == lo)
				break;
			setHue(lo,hue[hi]); // Move hue[hi] into empty space.
			setHue(hi,-1);      // Mark location hi as empty.
			delay(100);
			while (lo < hi && hue[lo] < pivot)
				lo++;
			if (hi == lo)
				break;
			setHue(hi,hue[lo]);  // Move hue[lo] into empty space.
			setHue(lo, -1);      // Mark location lo as empty.
			delay(100);
		}
		setHue(lo,pivot);  // Move pivot item into the empty space.
		delay(100);
		return lo;
	}


	/**
	 * The recursive quickSort algorithm, for sorting the hue
	 * array from positions lo through hi into increasing order.
	 * Most of the actual work is done in quickSortStep().
	 * This method is called by the animation thread as  
	 * quickSort(0,hue.length-1)  to sort the entire array.
	 */
	private void quickSort(int lo, int hi) {
		if (hi <= lo)
			return;
		int mid = quickSortStep(lo, hi);
		quickSort(lo, mid-1);
		quickSort(mid+1, hi);
	}


	/**
	 * This class defines the thread that runs the recursive
	 * QuickSort algorithm.  The thread begins by randomizing the
	 * hue array.  It then calls quickSort() to sort the entire array.
	 * If quickSort() is aborted by a ThreadTerminationException,
	 * which would be caused by the user clicking the Finish button,
	 * then the thread will restore the array to sorted order before
	 * terminating, so that whether or not the quickSort is aborted,
	 * the array ends up sorted.  In any case, in the end, it 
	 * resets the text on the button to "Start".
	 */
	private class Runner extends Thread {
		Runner() {
			    // The constructor sets this thread to be a Daemon thread.
			    // Otherwise, the thread will keep the Java Virtual Machine
			    // from exiting when the window is closed.
			setDaemon(true);
		}
		public void run() {
			for (int i = 0; i < hue.length; i++) {
				   // fill hue array with indices in order
				hue[i] = i;
			}
			for (int i = hue.length-1; i > 0; i--) { 
				   // Randomize the order of the hues.
				int r = (int)((i+1)*Math.random());
				int temp = hue[r];
				hue[r] = hue[i];
				   // The last assignment that needs to be done in this
				   // loop is hue[i] = temp.  The value of hue[i] will
				   // not change after this, so the assignment is done
				   // by calling setHue(i,temp) which will change
				   // the value in the array and also use Platform.runLater()
				   // to change the color of the i-th line in the canvas.
				setHue(i,temp);
			}
			try {
				delay(1000);  // Wait one second before starting the sort.
				quickSort(0,hue.length-1);  // Sort the whole array.
			}
			catch (ThreadTerminationException e) { // User aborted quickSort.
					// Put the colors back into sorted order.  The drawSorted()
					// method draws all of the color bars in sorted order.
				Platform.runLater( () -> drawSorted() );
			}
			finally {
				running = false;  // make sure running is false; this is only
				                  //   really necessary if the thread terminated
				                  //   normally
				Platform.runLater( () -> startButton.setText("Start") );
			}
		}
	}

} // end QuicksortThreadDemo
