
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Contains static methods for showing simple, unadorned dialog boxes
 * of several types.  The dialogs are based on the JavaFX Dialog class,
 * except for SimpleDialogs.prompt(), which uses the JavaFX TextInputDialog.
 * These dialogs do not have graphics (i.e. the icons that appear in
 * standard Alert dialogs).
 */
public class SimpleDialogs {
	
	/**
	 * For showing a dialog that contains an arbitrary node, with an
	 * "OK" and a "Cancel" button.  There is no header text in the dialog,
	 * only the content node and buttons.
	 * @param content the node that appears above the buttons in the dialog.
	 * @param title text to appear in the title bar of the dialog window.
	 *    The title text can be null.
	 * @return true if the user dismisses the dialog with OK, false if
	 *    the user cancels or closes the dialog in some other way.
	 */
	public static boolean custom(Node content, String title) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
		dialog.getDialogPane().setContent(content);
		Optional<ButtonType> result = dialog.showAndWait();
		return result.isPresent() && result.get() == ButtonType.OK;
	}
	
	/**
	 * Shows a dialog containing an arbitrary node as content.  The node is
	 * assumed to do some sort of input.  The dialog has "Cancel" and "OK buttons.
	 * The title of the dialog is "Input is Requested".
	 * @param inputNode  the content node for the dialog.
	 * @param headerText text that appears above the content node in the dialog;
	 *          can be null.
	 * @param testForErrorString a Supplier<String> that performs an error
	 *     check on the input in the content node.  This is called when the
	 *     user clicks "OK", before closing the dialog box. It must return a null
	 *     value if the input is good; otherwise, it must return an error
	 *     string.  When the value is non-null, the error string is shown
	 *     to the user, and the dialog does not close.  Cannot be null!
	 * @return true if the user dismisses the dialog with OK, and false otherwise.
	 *    If the value is true, then the input in the content node has passed
	 *    the error check.
	 */
	public static boolean vetoableInput(
			Node inputNode, String headerText, Supplier<String> testForErrorString) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Input is Requested");
		dialog.setHeaderText(headerText);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
		dialog.getDialogPane().setContent(inputNode);
		
	      // The following code is adapted with some changes from the JavaFX API docs.
		  // The idea is to consume a button click on the OK button if the state of
		  // the inputNode is not valid; doing so will stop the dialog from closing.
		Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
		okButton.addEventFilter(ActionEvent.ACTION, e -> {
		     if (testForErrorString.get() != null) {
		         e.consume();
		         message("Input is incorrect: " + testForErrorString.get());
		     }
		});
		
		Optional<ButtonType> result = dialog.showAndWait();
		return result.isPresent() && result.get() == ButtonType.OK;
	}
	
	/**
	 * Calls SimpleDialogs(text, "Message");
	 */
	public static void message(String text) {
		message(text,"Message");
	}

	/**
	 * Shows a simple dialog to the user containing a message and
	 * an OK button.
	 * @param text the message that appears in the dialog box.  Unlike the
	 *     messages for most of the other dialog boxes, this text is
	 *     automatically wrapped to a maximum width of 450 pixels.  There is
	 *     no need to include line feeds in the text.
	 * @param title text to appear in the title bar of the dialog;
	 *     can be null.
	 */
	public static void message(String text, String title) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setGraphic(null);
		dialog.setTitle(title);
		dialog.getDialogPane().setContent(makeText(text));
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().setPadding(new Insets(10,10,0,10));
		dialog.showAndWait();
	}
	
	/**
	 * Calls SimpleDialogs.prompt(promptText,"Request for Input",null)
	 */
	public static String prompt(String promptText) {
		return prompt(promptText,"Request for Input",null);
	}
	
	/**
	 * Calls SimpleDialogs.prompt(promptText,title,null);
	 */
	public static String prompt(String promptText, String title) {
		return prompt(promptText,title,null);
	}
	
	/**
	 * Shows an input dialog box where the user can enter one line of text.
	 * The dialog box has an "OK" button and a "Cancel" button.
	 * @param promptText the prompt (such as a question) that is displayed
	 *     in the dialog box above the text input box.  This is the
	 *     headerText for the dialog.  If it is to be displayed as more than
	 *     one line, it should contain \n characters to separate the lines.
	 *     Should not be null.
	 * @param title  text to appear in the title of the dialog box; can be null
	 * @param defaultValue if non-null, this is the initial text in the input box
	 * @return null if the user cancels the dialog, or the contents of the input
	 *     box if the user dismisses the dialog with the OK button.  Note that
	 *     the return value can be an empty string, if the user hits "OK" while
	 *     the input box is empty.
	 */
	public static String prompt(String promptText, String title, String defaultValue) {
		TextInputDialog dialog = new TextInputDialog(defaultValue);
		dialog.setTitle(title);
		dialog.setGraphic(null);
		dialog.setHeaderText(promptText);
		Optional<String> reply = dialog.showAndWait();
		if (reply.isPresent())
			return reply.get();
		else
			return null;
	}
	
	/**
	 * Calls SimpleDialogs.confirm(promptText, "Confirmation needed (or cancel)");
	 */
	public static String confirm(String promptText) {
		return confirm(promptText, "Confirmation needed (or cancel)");
	}
	
	/**
	 * Shows a dialog box with a message and three buttons: "yes", "no", and
	 * "cancel".  The only thing the user can do is click one of the buttons.
	 * @param message the prompt (such as a question) that is displayed
	 *     in the dialog box above the buttons.  This is the
	 *     contentText for the dialog.  (The headerText is null.)
	 *     If it is to be displayed as more than one line, it should 
	 *     contain \n characters to separate the lines.  Should not be null.
	 * @param title  text to appear in the title of the dialog box; can be null.
	 * @return "yes", "no", or "cancel".  The return value is always one of these
	 *     three strings, depending on which button the user clicks to dismiss
	 *     the dialog box.  If the user closes the dialog box in some other
	 *     way than clicking a button, the return value is "cancel".
	 *     The return value cannot be null.
	 */
	public static String confirm(String message, String title) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(null);
		dialog.setContentText(message);
		dialog.getDialogPane().getButtonTypes().addAll(
				ButtonType.CANCEL, ButtonType.NO, ButtonType.YES);
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.YES)
			return "yes";
		else if (result.isPresent() && result.get() == ButtonType.NO)
			return "no";
		else
			return "cancel";
	}
	
	/**
	 * Calls SimpleDialogs.colorChooser(initialColor, "Select a Color");
	 */
	public static Color colorChooser( Color initialColor ) {
		return colorChooser(initialColor, "Select a Color");
	}

	/**
	 * Shows a dialog box containing a simple color chooser pane that the user
	 * can manipulate to select a color.  The dialog box has an OK button and
	 * a "Cancel" button.
	 * @param initialColor the color that is initially selected in the dialog.
	 *     If the value is null, the initial color is black.
	 * @param headerText text to be shown in the dialog above the color chooser
	 *     pane.  Can be null.  For multi-line text, the \n character should
	 *     be included in the string to separate the lines.
	 * @return null if the user cancels the dialog, or the color that is selected
	 *    in the color chooser pane if the user dismisses the dialog box by
	 *    clicking the "OK" button.
	 */
	public static Color colorChooser( Color initialColor, String headerText ) {
		ColorChooserPane chooser = new ColorChooserPane(initialColor);
		
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Color Picker");
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
		dialog.getDialogPane().setContent(chooser);
		dialog.setHeaderText(headerText);
		
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK )
			return chooser.getColor();
		else
			return null;
	}
	
	
	// ---------------------- private implementation -------------------------------------------
	
	private final static int TEXT_WIDTH = 450;
	
	/**
	 * Makes a Text node to represent the text in a "message" dialog.
	 */
	private static Text makeText(String str) {
		Text text = new Text(str);
		text.setWrappingWidth(TEXT_WIDTH);
		text.setFont(Font.font(Font.getDefault().getSize()*1.2));
		return text;
	}
		
	/**
	 *  This component shows six sliders that the user can manipulate
	 *  to set the red, green, blue, hue, brightness, and saturation components
	 *  of a color.  A color patch shows the selected color, and there are
	 *  six labels that show the numerical values of all the components.
	 */
	private static class ColorChooserPane extends GridPane {

		private Slider hueSlider, brightnessSlider, saturationSlider,  // Sliders to control color components.
							redSlider, greenSlider, blueSlider;

		private Label hueLabel, brightnessLabel, saturationLabel,  // For displaying color component values.
							redLabel, greenLabel, blueLabel;

		private Pane colorPatch;  // Color patch for displaying the color.

		private Color currentColor;
		
		public ColorChooserPane(Color initialColor) {

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
			
			/* Lay out the components. */

			GridPane root = this;
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
			
			setColor(initialColor == null? Color.BLACK : initialColor);
		}
		
		
		public Color getColor() {
			return currentColor;
		}
		
		public void setColor(Color color) {
			if (color == null)
				return;
			hueSlider.setValue(color.getHue());
			brightnessSlider.setValue(color.getBrightness());
			saturationSlider.setValue(color.getSaturation());
			redSlider.setValue(color.getRed());
			greenSlider.setValue(color.getGreen());
			blueSlider.setValue(color.getBlue());
			String colorString = String.format("#%02x%02x%02x", (int)(255*color.getRed()),
					(int)(255*color.getGreen()), (int)(255*color.getBlue()) );
			colorPatch.setStyle("-fx-border-color:black; -fx-border-width:2px; -fx-background-color:" + colorString);
			hueLabel.setText(String.format(" Hue = %1.3f", color.getHue()));
			saturationLabel.setText(String.format(" Saturation = %1.3f", color.getSaturation()));
			brightnessLabel.setText(String.format(" Brightness = %1.3f", color.getBrightness()));
			redLabel.setText(String.format(" Red = %1.3f", color.getRed()));
			greenLabel.setText(String.format(" Green = %1.3f", color.getGreen()));
			blueLabel.setText(String.format(" Blue = %1.3f", color.getBlue()));
			currentColor = color;
		}
		

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
				        // only respond if it was set by user dragging the slider.
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
			currentColor = color;
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

}
