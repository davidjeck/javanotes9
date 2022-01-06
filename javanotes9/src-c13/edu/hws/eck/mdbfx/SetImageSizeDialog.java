package edu.hws.eck.mdbfx;

import javafx.scene.control.Dialog;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import java.util.Optional;


/**
 * This class defines a custom dialog box that asks the user to enter
 * a new size for the Mandelbrot image.  The dialog box has two input
 * boxes where the user can enter integers.  It has a "Image Size Tracks
 * Window Size" checkbox; if the box is checked, then the inputSize
 * returned by the image will be (0,0), which will enable size tracking
 * in MandelbrotPane.
 */
public class SetImageSizeDialog extends Dialog<ButtonType> {

	/**
	 * This static convenience method will show a dialog of type
	 * SetImageSizeDialog.  It will not return until the user dismisses
	 * the dialog, by clicking "OK" or "Cancel" or by clicking the
	 * dialog's close box.  The user is forced to either enter legal
	 * inputs or to cancel the dialog.  Legal input values are integers
	 * in the range 10 to 5000.  NOTE:  If you use this method, you
	 * don't have to worry about anything else in the class.
	 * @param oldSize if non-null, then the values of oldSize[0] and
	 *    oldSize[1] are put in the input boxes.  If null, the initial
	 *    values in the input boxes are set to 800 and 600.
	 * @return null, if the user cancels the dialog or clicks OK without changing
	 *    the values in the input boxes; if the user changes the values and clicks
	 *    OK, then the return value is an array of two double values representing
	 *    the user's inputs, or containing (0,0) if the user checked the
	 *    "Image Size Tracks Window Size" box.
	 */
	public static int[] showDialog(int[] oldSize) {
		SetImageSizeDialog dialog = new SetImageSizeDialog(oldSize);
		Optional<ButtonType> response = dialog.showAndWait();
		if (response.isPresent() && dialog.inputSize != null &&
				(dialog.inputSize[0] != oldSize[0] || dialog.inputSize[1] != oldSize[1]) ) {
			return dialog.inputSize;
		}
		else {
			return null;
		}
	}

	private int[] inputSize;  // This will be set to the user's input,
	                          // when the user clicks OK and the input
	                          // is legal.

	private TextField widthInput;
	private TextField heightInput;
	private CheckBox trackWindowSize;


	/**
	 * This constructor creates the dialog's user interface and sets up
	 * a listener to detect clicks on the OK button.
	 *     When the user clicks OK, the checkbox and input boxes are checked.
	 * If the input is legal, the dialog box is closed; if not, an error
	 * message is displayed and the dialog stays up.  Note that this constructor
	 * does not make the dialog box visible.
	 * @param frame the parent of this dialog box, which is blocked until
	 *    the dialog box is closed.
	 * @param oldSize initial values for the input boxes. Must be either null
	 *     or an array of length at least two.  An array of length 0 or 1
	 *     will cause an exception. 
	 */
	public SetImageSizeDialog(int[] oldSize) {
		setTitle(I18n.tr("imagesizedialog.title")); 
		if (oldSize == null)
			oldSize = new int[] { 800, 600 };
		widthInput = new TextField("" + oldSize[0]);
		widthInput.setPrefColumnCount(5);
		heightInput = new TextField("" + oldSize[1]);
		heightInput.setPrefColumnCount(5);
		trackWindowSize = new CheckBox(I18n.tr("imagesizedialog.trackWindowSize"));
		trackWindowSize.setSelected(false);

		HBox input = new HBox( 8,
				new Label(I18n.tr("imagesizedialog.widthequals")),
				widthInput,
				new Rectangle(20,0),  // acts as a horizontal strut
				new Label(I18n.tr("imagesizedialog.heightequals")),
				heightInput );
		input.setAlignment(Pos.CENTER);
		input.disableProperty().bind(trackWindowSize.selectedProperty());

		VBox content = new VBox(15,
				new Label(I18n.tr("imagesizedialog.question")),
				trackWindowSize,
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
	 * After the dialog box has closed, this can be called to get
	 * the user's response.
	 * @return null, if dialog was canceled, or an array containing
	 *   the width/height entered by the user if not.
	 */
	public int[] getInputSize() {
		return inputSize;
	}

	/**
	 * This method is called when the user clicks OK.  It gets the
	 * user's input and checks whether it is legal.
	 * @return true if the input is legal, false if not.
	 */
	private boolean checkInput() {
		if (trackWindowSize.isSelected()) {
			inputSize = new int[] { 0, 0 };
			return true;
		}
		inputSize = null;
		int width, height;
		try {
			width = Integer.parseInt(widthInput.getText());
		}
		catch (NumberFormatException e) {
			error(I18n.tr("imagesizedialog.error.widthnotanumber", widthInput.getText()));
			widthInput.selectAll();
			widthInput.requestFocus();
			return false;
		}
		try {
			height = Integer.parseInt(heightInput.getText());
		}
		catch (NumberFormatException e) {
			error(I18n.tr("imagesizedialog.error.heightnotanumber", heightInput.getText())); 
			heightInput.selectAll();
			heightInput.requestFocus();
			return false;
		}
		if (width < 10 || width > 5000) {
			error(I18n.tr("imagesizedialog.error.badwidth", ""+width)); 
			widthInput.selectAll();
			widthInput.requestFocus();
			return false;
		}
		else if (height < 10 || height > 5000) {
			error(I18n.tr("imagesizedialog.error.badheight", ""+width)); 
			heightInput.selectAll();
			heightInput.requestFocus();
			return false;
		}
		inputSize = new int[] { width, height };
		return true;
	}

	/**
	 * Utility method to show an error alert.
	 */
	private void error(String message) { 
		Alert alert = new Alert(Alert.AlertType.ERROR, message);
		alert.setTitle(I18n.tr("imagesizedialog.error.title"));
		alert.setContentText(message);
		alert.showAndWait();
	}

} // end class SetImageSizeDialog
