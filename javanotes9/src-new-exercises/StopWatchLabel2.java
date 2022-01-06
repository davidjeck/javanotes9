
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;

/**
 * A custom component that acts as a simple stop-watch.  When the user clicks
 * on it, this component starts timing.  When the user clicks again,
 * it displays the time between the two clicks.  Clicking a third time
 * starts another timer, etc.  While it is timing, the label just
 * displays the number of seconds that have passed since the stop
 * watch was started.
 */
public class StopWatchLabel2 extends Label {

	private long startTime;   // Start time of timer.
	//   (Time is measured in milliseconds.)

	private boolean running;  // True when the timer is running.

	private AnimationTimer timer;  // Used to update the timer to show
	// the number of seconds that have passed.

	/**
	 * Constructor sets initial text on the label to
	 * "Click to start timer." and sets up a mouse event
	 * handler so the label can respond to clicks.
	 */
	public StopWatchLabel2() {
		super("  Click to start timer.  ");
		setOnMousePressed( e -> setRunning( !running ) );
	}


	/**
	 * Tells whether the stopwatch is currently running.
	 */
	public boolean isRunning() {
		return running;
	}


	/**
	 * Sets the stop stopwatch to be running or stopped, and changes the text 
	 * that is shown on the label.  (This method should be called on the JavaFX
	 * application thread.)
	 * @param running says whether the stopwatch should be running; if this
	 *    is equal to the current state, nothing is done.
	 */
	public void setRunning( boolean running ) {
		if (this.running == running)
			return;
		this.running = running;
		if (running == true) {
			// Record the time and start the timer.
			startTime = System.currentTimeMillis();
			if (timer == null) {
				timer = new AnimationTimer() {
					public void handle(long now) {
						long elapsedTime = System.currentTimeMillis() - startTime;
						String text = String.format(
								"%3.1f seconds elapsed", elapsedTime/1000.0);
						setText(text);
					}
				};
			}			
			setText("   0.0 seconds elapsed");
			timer.start();
		}
		else {
			// Stop the timer.  Compute the elapsed time since the
			// stop was started and display it.
			running = false;
			timer.stop();
			long endTime = System.currentTimeMillis();
			double seconds = (endTime - startTime) / 1000.0;
			setText( String.format("Time: %1.3f seconds", seconds) );
		}
	}

} // end StopWatchLabel
