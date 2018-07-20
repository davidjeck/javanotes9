
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;

/**
 * Shows a StopWatchLabel.  The user can click to start a timer
 * and click again to stop it.  The elapsed time is displayed.
 */
public class TestStopWatch extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//------------------------------------------------------------
	
	public void start(Stage stage) {
		
		StopWatchLabel stopWatch = new StopWatchLabel();
		stopWatch.setStyle("-fx-font: bold 30pt serif; -fx-background-color:#ffffee;"
				+ "-fx-border-color:#008; -fx-border-width:3px; -fx-padding:20px;"
				+ "-fx-text-fill: #008");
		stopWatch.setMaxSize(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY);
		stopWatch.setAlignment(Pos.CENTER);
		
		stage.setScene( new Scene(new StackPane(stopWatch)) );
		stage.setTitle("StopWatchLabel Demo");
		stage.show();
		
	}
	
}
