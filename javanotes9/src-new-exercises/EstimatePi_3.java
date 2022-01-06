
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.animation.AnimationTimer;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;
import java.util.Random;

/**
 * This program uses a probabilistic technique to estimate the
 * value of the mathematical constant pi.  The technique is to
 * choose random numbers x and y in the range 0 to 1, and to
 * compute x*x + y*y.  The probability that x*x + y*y is less than
 * 1 is pi/4.  If many trials are performed, and the number of
 * trials in which x*x+y*y is less than 1 is divided by the total
 * number of trials, the result is an approximation for pi/4.
 * Multiplying this by 4 gives an approximation for pi.
 * 
 * The program shows the estimate produced by this procedure, along
 * with the number of trials that have been done and, for comparison,
 * the actual value of pi.  These values are shown in three Labels.
 * The computation is done by a separate thread that updates the
 * contents of the labels after every millionth trial.
 * 
 * In this version of the program, the computation thread runs
 * continually from the time the program is started until it
 * ends.  It is run at a reduced priority so that it does not
 * interfere with the GUI thread.
 */
public class EstimatePi_3 extends Application {

    public static void main(String[] args) {
        launch();
    }
    //---------------------------------------------------------------------------

    private final static int BATCH_SIZE = 1000000;  // This is the number of trials
                                                    // in a batch.  A computation thread runs this
                                                    // many trials in a fast for loop, without checking
                                                    // the value of running and without reporting its
                                                    // results.  After the for loop, the thread puts
                                                    // the result from that batch into the queue.  It
                                                    // then checks the value of running and will pause
                                                    // if running is false.

    private long totalTrialCount;    // Total number of trials considered so far.
    private long totalInCircleCount; // Number of those trials for which x*x+y*y < 1.

    private Label piEstimateLabel;  // A label for showing the current estimate of pi.
    private Label countLabel;       // A label for showing the number of trials.

    private Button runPauseButton;  // Button to control the threads.  Clicking this
                                    // button will pause the threads if they are running
                                    // and will restart it if it is paused.

    private volatile boolean running;   // Control variable for signaling the threads to
                                        // run or pause.  Initially, this is false, so
                                        // the thread pauses as soon as it is created,
                                        // until the user clicks the "Run" button.


    private LinkedBlockingQueue<Integer> resultsQueue;  // Results from the computation
                                                        // threads are placed into this queue.  Every number
                                                        // in the queue represents the results from running
                                                        // a batch of trials, of size BATCH_SIZE.  The number
                                                        // in the queue is the number of trials in that batch
                                                        // that resulted in x*x+y*y being less than 1.  (Note
                                                        // that I use a blocking queue rather than a 
                                                        // ConcurrentLinkedQueue only because the blocking
                                                        // queue has a convenient drainTo() method for getting
                                                        // all the items out of the queue at once, with correct
                                                        // synchronization.)

    private AnimationTimer resultsTimer;    // While the computation is running, this timer is
                                            // also running.  Every 1/60 second, it grabs the
                                            // results from the queue and applies them to the
                                            // display labels.  (Note that some results can be
                                            // left in the queue while the timer and threads are
                                            // paused.  This seems harmless.)


    /**
     * Sets up the GUI.  Creates computation threads and starts them.
     * Creates the AnimationTimer but does not start it.
     */
    public void start(Stage stage) {

        countLabel =      new Label(" Number of Trials:   0");
        piEstimateLabel = new Label(" Current Estimate:   (none)");
        Label piLabel =  new Label(" Actual value of pi: " + Math.PI + "  ");
        String style = "-fx-font: bold 18pt monospaced; -fx-padding: 8px; "
                + "-fx-border-color: blue; -fx-border-width:2px";
        countLabel.setStyle(style);
        piEstimateLabel.setStyle(style);
        piLabel.setStyle(style);
        countLabel.setMaxWidth(10000);
        piEstimateLabel.setMaxWidth(10000);
        piLabel.setMaxWidth(10000);

        runPauseButton = new Button("Run");
        runPauseButton.setOnAction( e -> doRunPause() );

        VBox labels = new VBox(piLabel, piEstimateLabel, countLabel);
        labels.setStyle("-fx-border-color: blue; -fx-border-width:2px");

        BorderPane root = new BorderPane(labels);
        root.setBottom(runPauseButton);
        BorderPane.setAlignment(runPauseButton, Pos.CENTER);
        BorderPane.setMargin(runPauseButton, new Insets(10));


        stage.setScene(new Scene(root));
        stage.setTitle("Estimating Pi");
        stage.setResizable(false);
        stage.show();

        resultsTimer = new AnimationTimer() { // (must be created before threads, since
            public void handle(long time) {   //    the threads use it for synchronization)
                grabResults();
            }
        };

        resultsQueue = new LinkedBlockingQueue<Integer>();
        int threadCount = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < threadCount; i++) {
            ComputationThread runner = new ComputationThread();
            runner.start();
        }

    } // end start


    /**
     * This method responds to clicks on the button, by
     * toggling the value of the signal variable from true
     * to false or from false to true.  The text on the 
     * button is changed to match the state. The timer
     * is also stopped or started.
     */
    public void doRunPause() {
        if (running) {
            resultsTimer.stop();
            runPauseButton.setText("Run");
            running = false;
        }
        else {
            runPauseButton.setText("Pause");
            resultsTimer.start();
            synchronized(resultsTimer) { 
                    // IMPORTANT: Synchronization is now on the ApplicationTimer!
                running = true;
                resultsTimer.notifyAll(); 
                    // IMPORTANT: Use notifyAll(), not notify(), to wake ALL computation threads
            }
        }
    }


    /**
     * This method is called by the timer, every 1/60 second while the
     * computation is running.  It grabs the entire contents of the
     * queue that is used to send results from the threads to
     * this method.  Each value in the queue represents the number of
     * trials, out of a batch of size BATCH_SIZE, in which x*x+y*Y was
     * less than 1.  This method updates the total number of trials that 
     * have been performed and the total number of trials for which
     * x*x+y*y was less than 1.  It then updates the display labels
     * with the new data.
     */
    private void grabResults() {
        ArrayList<Integer> results = new ArrayList<Integer>();
        resultsQueue.drainTo(results);  // Get entire contents of queue.
                    // Using this method avoids having to synchronize
                    // the entire process of removing items from the
                    // queue one at a time.  (And doing that without
                    // synchronization would introduce a race condition.)
        for (int inCircleCount : results) {
            totalTrialCount += BATCH_SIZE;
            totalInCircleCount += inCircleCount;
        }
        double estimateOfPi = 4 * ((double)totalInCircleCount / totalTrialCount);
        countLabel.setText(      " Number of Trials:   " + totalTrialCount);
        piEstimateLabel.setText( " Current Estimate:   " + estimateOfPi);
        // System.out.println("Got " + results.size() + " results.");  // for testing
    }


    /**
     *  This class defines the thread that does the computation.
     *  The thread runs in an infinite loop in which it performs
     *  batches of 1000000 trials and places the result in the queue.
     *  Just after it starts and between batches, the thread tests
     *  the value of the signal variable, running.  If this variable
     *  is false, then the thread sleeps until the value of running
     *  is set to true.  Note that this method creates and uses its
     *  own object of type Random to generate random numbers. (Because
     *  access to Math.random() has to be synchronized, using it
     *  in multiple threads slowed things down immensely.)
     *  Synchronization in this thread, as in the rest of the program,
     *  is on the ApplicationTimer object, which is referred to here as
     *  "EstimatePi_3.this".  The previous version used the thread
     *  object for synchronization, but in this version there can
     *  be multiple threads, so it seemed more natural to use something else
     */
    private class ComputationThread extends Thread {
        public ComputationThread() {
            setDaemon(true);
            setPriority(Thread.currentThread().getPriority() - 1);
        }
        public void run() {
            Random myRandom = new Random();
            while (true) {
                synchronized(resultsTimer) { 
                    while ( ! running ) {
                        
                        try {
                            resultsTimer.wait();
                        }
                        catch (InterruptedException e) {
                        }
                    }
                }
                int inCircleCount = 0;
                for (int i = 0; i < BATCH_SIZE; i++) {
                    double x = myRandom.nextDouble();
                    double y = myRandom.nextDouble();
                    if (x*x + y*y < 1)
                        inCircleCount++;                        
                }
                resultsQueue.add(inCircleCount);
            }
        }
    }

} // end class EstimatePi_3
