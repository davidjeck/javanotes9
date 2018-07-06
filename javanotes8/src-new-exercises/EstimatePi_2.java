
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

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
 * the actual value of pi.  These values are shown in three JLabels.
 * The computation is done by a separate thread that updates the
 * contents of the labels after every millionth trial.
 * 
 * In this version of the program, there is a "Run"/"Pause" button
 * that controls the computation thread.  Clicking the button once
 * starts the thread; clicking it again pauses it.  Initially, the
 * thread is paused.
 */
public class EstimatePi_2 extends Application {

    public static void main(String[] args) {
        launch();
    }
    //---------------------------------------------------------------------------

    private Label piEstimateLabel;  // A label for showing the current estimate of pi.
    private Label countLabel;       // A label for showing the number of trials.

    private Button runPauseButton;  // Button to control the thread.  Clicking this
                                    // button will pause the thread if it is running
                                    // and will restart it if it is paused.

    private ComputationThread runner;  // The thread that does the computation.

    private volatile boolean running;   // Control variable for signaling the thread to
                                        // run or pause.  Initially, this is false, so
                                        // the thread pauses as soon as it is created,
                                        // until the user clicks the "Run" button.


    /**
     * Set up GUI.  Create and start the computation thread (but it immediately
     * goes to sleep to wait for running to be set to true).
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

        runner = new ComputationThread();
        runner.start();

    } // end start()


    /**
     * This method responds to clicks on the button, by
     * toggling the value of the signal variable from true
     * to false or from false to true.  The text on the 
     * button is changed to match the state.  When
     * running is set to true, notify() is called to wake
     * up the thread.
     */
    public void doRunPause() {
        if (running) {
            runPauseButton.setText("Run");
            running = false;
        }
        else {
            runPauseButton.setText("Pause");
            synchronized(runner) {
                running = true;
                runner.notify(); 
            }
        }
    }


    /**
     *  This class defines the thread that does the computation.
     *  The thread runs in an infinite loop in which it performs
     *  batches of 1000000 trials and then updates the display labels.
     */
    private class ComputationThread extends Thread {
        final int BATCH_SIZE = 1000000;  // Number of trials between updates of the display.
        long trialCount;     // Total number of trials that have been performed.
        long inCircleCount;  // Number of trials in which x*x+y*y is less than 1.
        public ComputationThread() {
            setDaemon(true);
            setPriority(Thread.currentThread().getPriority() - 1);
        }
        public void run() {
            while (true) {
                synchronized(this) {
                    while ( ! running ) { // wait for running to be true
                        try {
                            wait();
                        }
                        catch (InterruptedException e) {
                        }
                    }
                }
                for (int i = 0; i < BATCH_SIZE; i++) {
                    double x = Math.random();
                    double y = Math.random();
                    trialCount++;
                    if (x*x + y*y < 1)
                        inCircleCount++;                        
                }
                double estimateForPi = 4 * ((double)inCircleCount / trialCount);
                Platform.runLater( () -> {
                    countLabel.setText(      " Number of Trials:   " + trialCount);
                    piEstimateLabel.setText( " Current Estimate:   " + estimateForPi);
                });
            }
        }
    }


} // end class EstimatePi_2
