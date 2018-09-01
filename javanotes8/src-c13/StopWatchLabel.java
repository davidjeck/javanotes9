
import javafx.scene.control.Label;

/**
 * A custom component that acts as a simple stop-watch.  When the user clicks
 * on it, this component starts timing.  When the user clicks again,
 * it displays the time between the two clicks.  Clicking a third time
 * starts another timer, etc.  While it is timing, the label just
 * displays the message "Timing....".
 */
public class StopWatchLabel extends Label {

	private long startTime;   // Start time of timer.
							  //   (Time is measured in milliseconds.)

	private boolean running;  // True when the timer is running.

	/**
	 * Constructor sets initial text on the label to
	 * "Click to start timer." and sets up a mouse event
	 * handler so the label can respond to clicks.
	 */
	public StopWatchLabel() {
		super("  Click to start timer.  ");
		setOnMousePressed( e -> setRunning( !running ) );
	}


	/**
	 * Tells whether the timer is currently running.
	 */
	public boolean isRunning() {
		return running;
	}


	/**
	 * Sets the timer to be running or stopped, and changes the text that
	 * is shown on the label.  (This method should be called on the JavaFX
	 * application thread.)
	 * @param running says whether the timer should be running; if this
	 *    is equal to the current state, nothing is done.
	 */
	public void setRunning( boolean running ) {
		if (this.running == running)
			return;
		this.running = running;
		if (running == true) {
				// Record the time and start the timer.
			startTime = System.currentTimeMillis();  
			setText("Timing....");
		}
		else {
				// Stop the timer.  Compute the elapsed time since the
				// timer was started and display it.
			long endTime = System.currentTimeMillis();
			double seconds = (endTime - startTime) / 1000.0;
			setText( String.format("Time: %1.3f seconds", seconds) );
		}
	}

} // end StopWatchLabel
