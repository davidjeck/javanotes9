
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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
 * In this version of the program, the computation thread runs
 * continually from the time the program is started until it
 * ends.  It is run at a reduced priority so that it does not
 * interfere with the GUI thread.
 */
public class EstimatePi_1 extends Application {

    public static void main(String[] args) {
        launch();
    }
    //---------------------------------------------------------------------------
    
    private Label piEstimateLabel;  // A label for showing the current estimate of pi.
    private Label countLabel;       // A label for showing the number of trials.
    
    private ComputationThread runner;  // The thread that does the computation.
    
    
    /**
     * Set up the GUI.  Create and start the computation thread.
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
        
        VBox root = new VBox(piLabel, piEstimateLabel, countLabel);
        root.setStyle("-fx-border-color: blue; -fx-border-width:2px");
        stage.setScene(new Scene(root));
        stage.setTitle("Estimating Pi");
        stage.setResizable(false);
        stage.show();
        
        runner = new ComputationThread();
        runner.start();
        
    } // end start()

    
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
    
    
} // end class EstimatePi_1
