
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.function.Supplier;


/**
 * This program tests the dialog boxes that are implemented in
 * SimpleDialogs.java.  There are six buttons that the user can
 * click to show dialog boxes of various kinds.
 */
public class TestDialogs extends Application {
	
	public static void main(String[] args) {
		launch();
	}
	//--------------------------------------------------------------------
	
	public void start(Stage stage) {
		
		Label message = new Label("Click any button to show a dialog box.");
		message.setMaxWidth(Double.POSITIVE_INFINITY);
		message.setFont(Font.font(20));
		message.setAlignment(Pos.CENTER);
		message.setPadding(new Insets(10));
		message.setStyle("-fx-background-color:#333");
		message.setTextFill(Color.WHITE);
		
		TilePane buttons = new TilePane();
		buttons.setPrefColumns(2);
		
		buttons.getChildren().add( makeButton("SimpleDialogs.message(m)", e -> {
			message.setText("Showing message dialog");
			SimpleDialogs.message("This dialog shows a message ot the user "
					+ "with an OK button.  The user can dismiss the dialog "
					+ "by clicking the button or by clicking the window's "
					+ "close box.  The text in this dialog is automatically "
					+ "wrapped so that line breaks in the parameter are "
					+ "not needed.");
			message.setText("You closed the message dialog.");
		} ) );
		
		buttons.getChildren().add( makeButton("SimpleDialogs.message(m,t)", e -> {
			message.setText("Showing message dialog with title");
			SimpleDialogs.message("This dialog uses a title, which appears "
					+ "in the title bar of the window, above this message.", 
					"This Is The Title!");
			message.setText("You closed the message dialog.");
		} ) );
		
		buttons.getChildren().add( makeButton("SimpleDialogs.colorChooser(c)", e -> {
			message.setText("Showing color dialog");
			Color c = SimpleDialogs.colorChooser( Color.PURPLE );
			if (c == null)
				message.setText("You canceled the color chooser.");
			else
				message.setText( String.format("You picked r=%1.2f, g=%1.2f, b=%1.2f",
						c.getRed(), c.getGreen(), c.getBlue()) );
		} ) );
		
		buttons.getChildren().add( makeButton("SimpleDialogs.prompt()", e -> {
			message.setText("Showing input dialog");
			String input = SimpleDialogs.prompt("Please input your reply\n"
					+ "in the text input box below.", 
					"Using a TextInputDialog", "default value");
			if (input == null)
				message.setText("You canceled the prompt dialog.");
			else
				message.setText("You replied \"" + input + "\".");
		} ) );
		
		buttons.getChildren().add( makeButton("SimpleDialogs.confirm()", e -> {
			message.setText("Showing confirm dialog");
			String answer = SimpleDialogs.confirm("This dialog is asking a YES/NO question\n"
					+ "but there is also a possibility of canceling.\n"
					+ "What do you say?");
			message.setText("You said \"" + answer + "\"");
		} ) );
		
		buttons.getChildren().add( makeButton("SimpleDialogs.vetoableInput()", e -> {
			     // For a vetoable dialog, the user can't close the dialog with "OK"
			     // unless the input is legal.  However, the user can always cancel.
			message.setText("Showing input dialog with error checking");
			boolean ok = SimpleDialogs.vetoableInput(
					makeInputForCustomDialog(),
					"Please input the minimum and maximum limits\n"
						+ "on your desired range of values.", 
					testInput);
			if (ok) {
				double a = Double.parseDouble(min.getText().trim());
				double b = Double.parseDouble(max.getText().trim());
				message.setText(String.format("Your input range was %1.4g to %1.4g", a, b));
			}
			else {
				message.setText("You canceled the input dialog.");
			}
		} ) );
		
		BorderPane root = new BorderPane(buttons);
		root.setBottom(message);
		
		stage.setScene( new Scene(root) );
		stage.setTitle("Test SimpleDialogs");
		stage.setResizable(false);
		stage.show();
	}
	
	
	/**
	 * Make one of the buttons for the dialog box.
	 * @param text the text that will be shown on the button
	 * @param onClick the ActionEvent handler for the button
	 */
	private Button makeButton(String text, EventHandler<ActionEvent> onClick) {
		Button btn = new Button(text);
		btn.setPrefSize(300,50);
		btn.setOnAction(onClick);
		btn.setFont(Font.font(16));
		return btn;
	}
	
	/* The remaining items are used for the "vetoable input" dialog.
	 * The dialog shows a pair of input boxes where the user enters
	 * two numbers, min and max.  The dialog will only accept legal
	 * numbers where max is greater than min.  
	 */
	
	private TextField min, max;  // input boxes for the dialog
	
	/**
	 * This object tests the input in the dialog box to see
	 * whether it is legal.  The return value is null if the
	 * input is legal.  If not, the return value is an error
	 * message string that will be shown to the user.
	 */
	private Supplier<String> testInput = () -> {
		double a,b;
		if (min.getText().trim().length() == 0) {
			min.requestFocus();
			return "you must enter a value for min.";
		}
		if (max.getText().trim().length() == 0) {
			max.requestFocus();
			return "you must enter a value for max.";
		}
		try {
			a = Double.parseDouble(min.getText().trim());
		}
		catch (NumberFormatException e) {
			min.selectAll();
			min.requestFocus();
			return "min is not a legal number.";
		}
		try {
			b = Double.parseDouble(max.getText().trim());
		}
		catch (NumberFormatException e) {
			max.selectAll();
			max.requestFocus();
			return "max is not a legal number.";
		}
		if (b <= a) {
			min.selectAll();
			min.requestFocus();
			return "max must be greater than min.";
		}
		return null;
	};

	/**
	 * Makes the content node for the dialog box.
	 */
	private HBox makeInputForCustomDialog() {
		min = new TextField();
		min.setPrefColumnCount(8);
		max = new TextField();
		max.setPrefColumnCount(8);
		HBox box = new HBox(8,
				new Label("min = "), min,
				new Label("max = "), max);
		box.setPadding(new Insets(8));
		return box;
	}

} // end class TestDialogs
