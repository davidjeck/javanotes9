
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * This program simply demonstrates using a TextField
 * and a TextArea.
 */
public class TextInputDemo extends Application {
	
	/* Define TextArea and TextField as instance variables
	 * so they can be used throughout the program.  Not needed
	 * in this program, but fairly typical in more complex 
	 * programs.
	 */
		
	private TextArea contentInput;
	private TextField titleInput;

	/**
	 * Start method builds the GUI and shows the window.
	 */
	public void start(Stage stage) {
		
		String title = "A Poem";  // Initial text for TextField.
		
		String content =  // Initial text for TextArea
				"So, naturalists observe, a flea\n"
					+ "     Has smaller fleas that on him prey;\n"
					+ "And these have smaller still to bite 'em;\n"
					+ "     And so proceed ad infinitum.\n\n"
					+ "                              --Jonathan Swift";
		
		/* Create and configure the TextField and TextArea */
		
		titleInput = new TextField(title);
		titleInput.setFont( Font.font(null, FontWeight.BOLD, 20) );
		titleInput.setPrefColumnCount(20);
		
		contentInput = new TextArea();
		contentInput.setText(content);
		contentInput.setFont( Font.font(16) );
		contentInput.setPrefRowCount(6);
		contentInput.setPrefColumnCount(30);
				
		/* A button that will clear the TextArea.  It also selects all of the
		 * text in the TextField and gives the focus to the TextField.  If the 
		 * user just starts typing at that point, the old text will be deleted
		 * and replaced with what the user types.
		 */
		
		Button clearButton = new Button("Clear TextArea");
		clearButton.setOnAction( e -> {
			contentInput.clear();
			titleInput.selectAll();
			titleInput.requestFocus();
		});

		/* Put the components in a BorderPane, which will be the root
		 * of the Scene.  A dark gray background on the BorderPane
		 * will show through in the margins around the TextArea and
		 * TextField, and in the extra space in the bottom section 
		 * around the button.
		 */
		
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #444444");
		root.setCenter(contentInput); 
		root.setTop(titleInput);
		root.setBottom(clearButton);
		BorderPane.setAlignment(clearButton, Pos.CENTER); // Put button in center, not at left.
		BorderPane.setMargin(contentInput, new Insets(2));  // Set margins around components
		BorderPane.setMargin(titleInput, new Insets(2,2,0,2));
		
		/* Add the scene to the window and make it visible. */
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Text Input Demo");
		stage.show();
		
	} // end start()

	//---------------------------------------------------------------------

	public static void main(String[] args) {
		launch(args);
	}

} // end class TextInputDemo
