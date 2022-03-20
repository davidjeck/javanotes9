
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.geometry.Insets;

/**
 *  A little program that demonstrates Sliders.
 */
public class SliderDemo extends Application {

	private Slider slider1, slider2, slider3;  // The sliders.

	private Label label; // A label for reporting changes in the sliders' values.

	public void start(Stage stage) {

		label = new Label("Try dragging the knobs on the sliders!");
		label.setFont( Font.font(18) );

		slider1 = new Slider(0,10,5);

		slider2 = new Slider();  // slider2 uses default values (0,100,0)
		slider2.setMajorTickUnit(25); // space between big tick marks, measured using slider values
		slider2.setMinorTickCount(5); // 5 small tick marks between big tick marks.
		slider2.setShowTickMarks(true);

		slider3 = new Slider(2000,2100,2022);
		slider3.setMajorTickUnit(50);  // determines how many labels are shown
		slider3.setMinorTickCount(49); // so there is a tick mark every 1 unit (ticks are not shown)
		slider3.setShowTickLabels(true);  // will show labels at 2000, 2050, 2100
		slider3.setSnapToTicks(true);  // after user finishes drag, value is snapped to a tick mark;
		                               // since there are minor tick marks at integer values, 
		                               //   the slider value is snapped to an integer.
		
		slider1.valueProperty().addListener( e -> sliderValueChanged(slider1) );
		slider2.valueProperty().addListener( e -> sliderValueChanged(slider2) );
		slider3.valueProperty().addListener( e -> sliderValueChanged(slider3) );
		
		VBox root = new VBox(12,label,slider1,slider2,slider3);
		root.setStyle("-fx-background-color:white; -fx-border-color: black");
		root.setPadding( new Insets(15) );
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Slider Demo");
		stage.show();

	}  // end start()
	

	/**
	 * This method is called by the handlers registered with the sliders when
	 * the value of the slider changes.  It is called repeatedly as the user
	 * drags the slider knob.  It can also be called when the drag ends, if 
	 * the slider's snapToTicks property is true.
	 * @param whichSlider tells which slider's value has changed
	 */
	private void sliderValueChanged(Slider whichSlider) {
		String str;
		if (whichSlider == slider1)
			str = String.format("First slider value is now %1.2f", slider1.getValue());
		else if (whichSlider == slider2)
			str = String.format("Second slider value is now %1.2f", slider2.getValue());
		else
			str = String.format("Third slider value is now %1.2f", slider3.getValue());
		label.setText(str);
	}
	
	//----------------------------------------------------------------------------
	
	public static void main(String[] args) {
		launch();
	}

}
