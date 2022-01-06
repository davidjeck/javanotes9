import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Insets;

/**
 * This simple program demonstrates several GUI components that are available in the
 * Java FX library.  The program shows a window containing a button, a text input
 * box, a choice box (pop-up menu), and a text area.  The text area is used for
 * a "transcript" that records interactions of the user with the other components.
 */
public class GUIDemo extends Application {

	/**
	 * This main routine allows this class to be run as an application.  This is 
	 * required for running Java FX programs in some versions of Java, but should
	 * not be necessary in more recent versions.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	//-----------------------------------------------------------------------------------


	/**
	 * This constructor adds several GUI components to the panel and sets
	 * itself up to listen for action events from some of them.
	 */
	public void start(Stage stage) {
		
		GridPane root = new GridPane();
			// I will put the transcript area in the right half of the
			// pane. The left half will be occupied by a grid of 4 rows
			// and two columns. Each row contains a component and
			// a label for that component.
		
		root.setHgap(8);
		root.setVgap(10);
		root.setPadding(new Insets(5,5,5,5));

		TextArea transcript = new TextArea();
		transcript.setEditable(false);
		transcript.setPrefRowCount(7);
		transcript.setPrefColumnCount(20);
		root.add(transcript, 2, 0, 1, 4);

		Label lab = new Label("Push Button:");
		root.add(lab, 0, 0);
		Button b = new Button("Click Me!");
		b.setOnAction( e -> transcript.appendText("Button was clicked\n\n") );
		root.add(b, 1, 0);

		lab = new Label("Checkbox:");
		root.add(lab, 0, 1);
		CheckBox c = new CheckBox("Click me!");
		c.setOnAction( e -> transcript.appendText("Checkbox was toggled\n\n") );
		root.add(c, 1, 1);

		lab = new Label("Text Field:");
		root.add(lab, 0, 2);
		TextField t = new TextField("Type here!");
		t.setPrefColumnCount(10);
		t.setOnAction( e -> transcript.appendText(
				              "Pressed return in TextField\nwith contents:  " + t.getText() + "\n\n") );
		root.add(t, 1, 2);

		lab = new Label("Pop-up Menu:");
		root.add(lab, 0, 3);
		ComboBox<String> combobox = new ComboBox<>();
		combobox.getItems().addAll("First Option", "Second Option", "Third Option", "Fourth Option");
		combobox.setValue("First Option");
		combobox.setOnAction( e -> transcript.appendText("Selected " + 
		                               combobox.getValue() + " from menu\n\n") );
		root.add(combobox, 1, 3);
		root.setStyle("-fx-border-width: 3px; -fx-border-color: darkblue");
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("GUIDemo");
		stage.show();
		stage.setResizable(false);
	}


} // end class GUIDemo
