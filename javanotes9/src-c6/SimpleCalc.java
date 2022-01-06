
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * In this program, the user can type in two real numbers.  The
 * user can click on buttons labeled +, - , *, and / to perform
 * basic arithmetic operations on the numbers.  When the user
 * clicks on a button the answer is displayed. 
 */
public class SimpleCalc extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	//-------------------------------------------------------------------------


	private TextField xInput, yInput;  // Input boxes for the numbers.

	private Label answer;  // Label for displaying the answer, or an 
						   //    error message if appropriate.


	public void start(Stage stage) {

		/* Create TextFields for the user's input, initially containing "0" */

		xInput = new TextField("0");
		yInput = new TextField("0");

		/* Create HBoxes to hold the input boxes and labels "x =" and "y =".
		 * Set the input boxes to grow to fill the available space. */

		HBox xPane = new HBox( new Label(" x = "), xInput );
		HBox yPane = new HBox( new Label(" y = "), yInput );

		/* Create the four buttons and an HBox to hold them. */

		Button plus = new Button("+");
		plus.setOnAction( e -> doOperation('+') );

		Button minus = new Button("-");
		minus.setOnAction( e -> doOperation('-') );

		Button times = new Button("*");
		times.setOnAction( e -> doOperation('*') );

		Button divide = new Button("/");
		divide.setOnAction( e -> doOperation('/') );
		
		HBox buttonPane = new HBox( plus, minus, times, divide );
		
		/* The four buttons need to be tweaked so that they will fill
		 * the entire buttonPane.  This can be done by giving each button
		 * a large maximum width and setting an hgrow constraint
		 * for the button. */
		
		HBox.setHgrow(plus, Priority.ALWAYS);
		plus.setMaxWidth(Double.POSITIVE_INFINITY);
		HBox.setHgrow(minus, Priority.ALWAYS);
		minus.setMaxWidth(Double.POSITIVE_INFINITY);
		HBox.setHgrow(times, Priority.ALWAYS);
		times.setMaxWidth(Double.POSITIVE_INFINITY);
		HBox.setHgrow(divide, Priority.ALWAYS);
		divide.setMaxWidth(Double.POSITIVE_INFINITY);

		/* Create the label for displaying the answer in red. 
		 * Use a bold font. */

		answer = new Label("x + y = 0");
		answer.setTextFill(Color.RED);
		answer.setStyle("-fx-font-weight:bold");
		answer.setAlignment(Pos.CENTER);
		answer.setMaxWidth(Double.POSITIVE_INFINITY);

		/* Create a VBox to hold all the other components.  There is
		 * a 10-pixel gap between components, and padding around the edges. */
		
		VBox root = new VBox( 10, xPane, yPane, buttonPane, answer );
		root.setPadding( new Insets( 8,3,8,3 ) );
		root.setStyle("-fx-border-color:black; -fx-border-width:2px");

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("SimpleCalc");
		stage.setResizable(false);
		stage.show();
		
	}  // end start();


	/**
	 * When the user clicks a button, get the numbers from the input boxes 
	 * and perform the operation indicated by the button.  Put the result in
	 * the answer label.  If an error occurs, an error message is put in the label.
	 */
	private void doOperation( char op ) {

		double x, y;  // The numbers from the input boxes.

		/* Get a number from the xInput TextField.  Use 
			 xInput.getText() to get its contents as a String.
			 Convert this String to a double.  The try...catch
			 statement will check for errors in the String.  If 
			 the string is not a legal number, the error message
			 "Illegal data for x." is put into the answer and
			 this method ends. */

		try {
			String xStr = xInput.getText();
			x = Double.parseDouble(xStr);
		}
		catch (NumberFormatException e) {
				// The string xStr is not a legal number.
			answer.setText("Illegal data for x.");
			xInput.requestFocus();
			xInput.selectAll();
			return;
		}

		/* Get a number from yInput in the same way. */

		try {
			String yStr = yInput.getText();
			y = Double.parseDouble(yStr);
		}
		catch (NumberFormatException e) {
			answer.setText("Illegal data for y.");
			yInput.requestFocus();
			yInput.selectAll();
			return;
		}

		/* Perform the operation based on the action command
			 from the button.  Note that division by zero produces
			 an error message. */

		if (op == '+')
			answer.setText( "x + y = " + (x+y) );
		else if (op == '-')
			answer.setText( "x - y = " + (x-y) );
		else if (op == '*')
			answer.setText( "x * y = " + (x*y) );
		else if (op == '/') {
			if (y == 0) {
				answer.setText("Can't divide by zero!");
				yInput.requestFocus();
				yInput.selectAll();
			}
			else {
				answer.setText( "x / y = " + (x/y) );
			}
		}

	} // end doOperation()


}  // end class SimpleCalc
