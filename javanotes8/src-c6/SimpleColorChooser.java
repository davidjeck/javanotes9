import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 *  This program shows six sliders that the user can manipulate
 *  to set the red, green, blue, hue, brightness, and saturation components
 *  of a color.  A color patch shows the selected color, and there are
 *  six labels that show the numerical values of all the components.
 */
public class SimpleColorChooser extends Application {

	private Slider hueSlider, brightnessSlider, saturationSlider,  // Sliders to control color components.
						redSlider, greenSlider, blueSlider;

	private Label hueLabel, brightnessLabel, saturationLabel,  // For displaying color component values.
						redLabel, greenLabel, blueLabel;

	private Pane colorPatch;  // Color patch for displaying the color.

	public static void main(String[] args) {
		launch();
	}

	public void start(Stage stage) {

		/* Create Sliders with possible values from 0 to 1, or 0 to 360 for hue. */

		hueSlider = new Slider(0,360,0);
		saturationSlider = new Slider(0,1,1);
		brightnessSlider = new Slider(0,1,1);
		redSlider = new Slider(0,1,1);
		greenSlider = new Slider(0,1,0);
		blueSlider = new Slider(0,1,0);
		
		/* Set up listeners to respond when a slider value is changed. */
		
		hueSlider.valueProperty().addListener( e -> newColor(hueSlider) );
		saturationSlider.valueProperty().addListener( e -> newColor(saturationSlider) );
		brightnessSlider.valueProperty().addListener( e -> newColor(brightnessSlider) );
		redSlider.valueProperty().addListener( e -> newColor(redSlider) );
		greenSlider.valueProperty().addListener( e -> newColor(greenSlider) );
		blueSlider.valueProperty().addListener( e -> newColor(blueSlider) );

		/* Create Labels showing current RGB and HSB values. */

		hueLabel = makeText(String.format(" Hue = %1.3f", 0.0));
		saturationLabel = makeText(String.format(" Saturation = %1.3f", 1.0));
		brightnessLabel = makeText(String.format(" Brightness = %1.3f", 1.0));
		redLabel = makeText(String.format(" Red = %1.3f", 1.0));
		greenLabel = makeText(String.format(" Green = %1.3f", 0.0));
		blueLabel = makeText(String.format(" Blue = %1.3f", 0.0));

		/* Create an object to show the currently selected color. */
		
		colorPatch = new Pane();
		colorPatch.setStyle("-fx-background-color:red; -fx-border-color:black; -fx-border-width:2px");
		
		/* Lay out the components */

		GridPane root = new GridPane();
		ColumnConstraints c1 = new ColumnConstraints();
		c1.setPercentWidth(33);
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setPercentWidth(34);
		ColumnConstraints c3 = new ColumnConstraints();
		c3.setPercentWidth(33);
		root.getColumnConstraints().addAll(c1, c2, c3);
		
		root.add(hueSlider, 0, 0);
		root.add(saturationSlider, 0, 1);
		root.add(brightnessSlider, 0, 2);
		root.add(redSlider, 0, 3);
		root.add(greenSlider, 0, 4);
		root.add(blueSlider, 0, 5);
		root.add(hueLabel, 1, 0);
		root.add(saturationLabel, 1, 1);
		root.add(brightnessLabel, 1, 2);
		root.add(redLabel, 1, 3);
		root.add(greenLabel, 1, 4);
		root.add(blueLabel, 1, 5);
		root.add(colorPatch, 2, 0, 1, 6);  // occupies 6 rows!
		root.setStyle("-fx-padding:5px; -fx-border-color:darkblue; -fx-border-width:2px; -fx-background-color:#DDF");

		/* Create the scene and show the stage. */
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Simple Color Chooser");
		stage.setResizable(false);
		stage.show();

	} // end start();
	

	private Label makeText(String message) {
		   // Make a label to show a given message shown in bold, with some padding
		   // between the text and the border of the label.
		Label text = new Label(message);
		text.setStyle("-fx-padding: 6px 10px 6px 10px; -fx-font-weight:bold");
		return text;
	}
	
	
	private void newColor(Slider whichSlider) {
		    // Adjust the GUI to a new color value, when one of the sliders has changed.
		if ( ! whichSlider.isValueChanging() ) {
			return; // Don't respond to change if it was set programmatically;
			        // Only respond if it was set by user dragging the slider.
		}
		Color color;
		if (whichSlider == redSlider || whichSlider == greenSlider || whichSlider == blueSlider) {
			color = Color.color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
			hueSlider.setValue(color.getHue());
			brightnessSlider.setValue(color.getBrightness());
			saturationSlider.setValue(color.getSaturation());
		}
		else {
			color = Color.hsb(hueSlider.getValue(), saturationSlider.getValue(), brightnessSlider.getValue());
			redSlider.setValue(color.getRed());
			greenSlider.setValue(color.getGreen());
			blueSlider.setValue(color.getBlue());
		}
		String colorString = String.format("#%02x%02x%02x", (int)(255*color.getRed()),
				(int)(255*color.getGreen()), (int)(255*color.getBlue()) );
		colorPatch.setStyle("-fx-border-color:black; -fx-border-width:2px; -fx-background-color:" + colorString);
		hueLabel.setText(String.format(" Hue = %1.3f", color.getHue()));
		saturationLabel.setText(String.format(" Saturation = %1.3f", color.getSaturation()));
		brightnessLabel.setText(String.format(" Brightness = %1.3f", color.getBrightness()));
		redLabel.setText(String.format(" Red = %1.3f", color.getRed()));
		greenLabel.setText(String.format(" Green = %1.3f", color.getGreen()));
		blueLabel.setText(String.format(" Blue = %1.3f", color.getBlue()));
	}	


}  // end class SimpleColorChooser
