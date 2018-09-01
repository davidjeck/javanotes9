package edu.hws.eck.mdbfx;

import javafx.scene.control.Dialog;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.layout.TilePane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import java.util.Optional;


/**
 * This class represents a dialog box where the user can enter new values
 * for xmin, xmax, ymin, and ymax.  These values specify the ranges of
 * x and y values that are shown in the Mandelbrot image.
 */
public class SetLimitsDialog extends Dialog<ButtonType> {
	
	/**
	 * This static convenience method shows a dialog of type SetLimitsDialog,
	 * waits for the user to input a response or to dismiss the dialog, and
	 * returns the user's response (or null if the user cancels).  Note that
	 * if you use this method, then you don't have to do anything else with
	 * this class.
	 * @param oldLimitStrings an array of 4 strings that are used as the
	 *   initial content of the input boxes for xmin, xmax, ymin, ymax (in 
	 *   that order). (Note that I pass strings rather than doubles because
	 *   I had nicely formatted strings available in the class that calls this
	 *   method.)
	 * @return null, if the user cancels, or an array of four doubles, representing
	 *   the new values for xmin, xmax, ymin, and ymax.  It is guaranteed that
	 *   xmin is strictly less than xmax and ymin is strictly less than ymax.
	 *   (The return value will also be null when the user clicks OK
	 *   without ever editing the initial values in the input boxes.  Only
	 *   CHANGED values are returned.)
	 */
	static double[] showDialog(String[] oldLimitStrings) {
		SetLimitsDialog dialog = new SetLimitsDialog(oldLimitStrings);
		Optional<ButtonType> response = dialog.showAndWait();
		double[] values = dialog.getInputsIfChanged();
		if (response.isPresent())
			return values;
		else
			return null;
	}
	
	private double[] inputValues;  // Will contain the user's input after user clicks OK.
	boolean changed;  // Will be set to true if the user actually edited the input boxes.

	private TextField[] inputBoxes;
	private String[] oldLimitStrings;
	
	private static final String[] names = { "limitsdialog.xmin", "limitsdialog.xmax", 
		                                    "limitsdialog.ymin", "limitsdialog.ymax" };
	
	/**
	 * Constructor builds the dialog box and adds a listener to the OK button.
	 * Does not make the dialog visible on the screen. 
	 * @param oldLimitStrings initial content of input boxes.  Must be an array 
	 *     of length (at least) four.
	 */
	public SetLimitsDialog(String[] oldLimitStrings) {
		this.oldLimitStrings = oldLimitStrings;
		setTitle(I18n.tr("limitsdialog.title"));

		TilePane input = new TilePane(10,10);
		input.setPrefColumns(2);
		inputBoxes = new TextField[4];
		for (int i = 0; i < 4; i++) {
			Label label = new Label(I18n.tr(names[i])+":");
			inputBoxes[i] = new TextField(oldLimitStrings[i]);
			input.getChildren().addAll( label, inputBoxes[i]);
		}
		input.setAlignment(Pos.CENTER);
		VBox content = new VBox(15, 
				new Label(I18n.tr("limitsdialog.question")),
				input);
		content.setPadding(new Insets(15));
		getDialogPane().setContent(content);

		getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
		Button okButton = (Button)getDialogPane().lookupButton(ButtonType.OK);
		Button cancelButton = (Button)getDialogPane().lookupButton(ButtonType.CANCEL);
		okButton.setText(I18n.tr("button.ok"));
		cancelButton.setText(I18n.tr("button.cancel"));
		okButton.addEventFilter( ActionEvent.ACTION, e -> {
			if ( checkInput() == false ) {
				e.consume();
			}
		} );
	}
	
	/**
	 * This is called when the user clicks the OK button, to get the user's responses
	 * and make sure they are legal.  If not, the user is informed of the error and
	 * the dialog will stay on the screen.
	 * @return true if the input is legal.
	 */
	private boolean checkInput() {
		changed = false;
		inputValues = null;
		String[] inputStrings = new String[4];
		for (int i = 0; i < 4; i++) {
			inputStrings[i] = inputBoxes[i].getText(); 
			if (!inputStrings[i].equals(oldLimitStrings[i]))
				changed = true;  // At least one of the input strings has been modified.
		}
		double[] values = new double[4];
		for (int i = 0; i < 4; i++) {
			try {
				values[i] = Double.parseDouble(inputStrings[i]);
			}
			catch (NumberFormatException e) {
				error( I18n.tr( "limitsdialog.error.NAN", inputStrings[i], I18n.tr(names[i]) ) );
				inputBoxes[i].selectAll();
				inputBoxes[i].requestFocus();
				return false;
			}
		}
		if (values[1] <= values[0]) {
			error(I18n.tr("limitsdialog.error.xValuesOutOfOrder"));
			inputBoxes[1].selectAll();
			inputBoxes[1].requestFocus();
			return false;
		}
		if (values[3] <= values[2]) {
			error(I18n.tr("limitsdialog.error.yValuesOutOfOrder"));
			inputBoxes[3].selectAll();
			inputBoxes[3].requestFocus();
			return false;
		}
		inputValues = values;
		return true;
	}
	
	/**
	 * Can be called after the dialog box is closed to get the user's inputs.
	 * @return an array containing the four values from the input box, if the 
	 *   user dismissed the dialog box with the OK button, or null if the user
	 *   canceled.
	 */
	public double[] getInputs() {
		return inputValues;
	}

	/**
	 * Can be called after the dialog box is closed to get the user's inputs.
	 * (This is provided because the double values that are returned might not be
	 * exactly the same as the original double values, even if the user has
	 * not edited the values at all.  This is true because of round-off error
	 * when doubles are converted to string form.  So, you can't check whether
	 * the user edited the inputs just by checking whether the new values are
	 * the same as the original values.  (This would probably be called a rather
	 * minor point by most people.))
	 * @return an array containing the four values from the input box, if the 
	 *   user dismissed the dialog box with the OK button AND the user changed
	 *   the initial values before clicking OK, or null if the user canceled OR
	 *   if the user clicked OK but did not change the values.
	 */
	public double[] getInputsIfChanged() {
		if (changed)
			return inputValues;
		else
			return null;
	}

	
	/**
	 * Utility method to show an error alert.
	 */
	private void error(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR, message);
		alert.setTitle(I18n.tr("imagesizedialog.error.title"));
		alert.setHeaderText(null);
		alert.showAndWait();
	}

}  // end class SetLimitsDialog
