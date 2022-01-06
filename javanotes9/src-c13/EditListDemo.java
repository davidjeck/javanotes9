import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;


/**
 * This program shows two editable ListViews.  One contains strings
 * and one contains integers.  The user can edit list entries,
 * delete entries, and add new entries.  There are labels that
 * display the selected index and the selected item in each list.
 */
public class EditListDemo extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//--------------------------------------------------------------


	public void start(Stage stage) {

		HBox root = new HBox(20, makeNamePane(), makeNumberPane() );
		root.setPadding(new Insets(10));
		root.setStyle("-fx-border-color:black; -fx-border-width:2px");
		
		stage.setScene( new Scene(root) );
		//stage.setResizable(false);
		stage.setTitle("Editable List Demo");
		stage.show();

	} // end start()



	/**
	 * Make the pane for the left side of the window.  It contains a ListView<String>,
	 * with labels that show the selected index and selected item, a button for
	 * deleting the selected item, and a button and text input box that can be
	 * used to add an item at the end of the list.
	 */
	private BorderPane makeNamePane() {
		
		/* The items in a ListView<String> are stored in an ObservableList<String>.
		 * One way to add items is to make an observable list and pass it as
		 * a parameter to the ListView constructor. */
		
		ObservableList<String> names = FXCollections.observableArrayList(
				"Fred", "Wilma", "Betty", "Barney", "Jane", "John", "Joe", "Judy");
		ListView<String> listView = new ListView<String>(names);
		
		/* For the items to be editable, listView must be made editable, and a 
		 * "cell factory" must be installed that will make editable cells for
		 * display in the list.  For editing strings, the cell factory can
		 * be created by the factory method TextFieldListCell.forListView(). */
		
		listView.setEditable(true);
		listView.setCellFactory(TextFieldListCell.forListView());
		
		/* Make a BordePane, with a title "My Favorite Names" in the top position. */
		
		BorderPane namePane = new BorderPane(listView);
		Label top = new Label("My Favorite Names");
		top.setPadding( new Insets(10) );
		top.setFont( Font.font(20) );
		top.setTextFill(Color.YELLOW);
		top.setMaxWidth(Double.POSITIVE_INFINITY);
		top.setAlignment(Pos.CENTER);
		top.setStyle("-fx-background-color: black");
		namePane.setTop(top);
		
		/* Create the bottom node for the BorderPane. It contains two labels whose
		 * text property is taken from properties of the ListView's SelectionModel.
		 * It contains a Button that can be used to delete the selected item; this
		 * button's disable property is bound to a boolean property derived from
		 * the selection model.  And it contains a TextField where the user can
		 * enter a new item for the list.  When the text field has focus, an
		 * associated "Add" button becomes the default button for the window, so
		 * the user can add the item to the list just by pressing return while
		 * typing in the input box.
		 */
		
		Label selectedIndexLabel = new Label();
		selectedIndexLabel.textProperty().bind(
				listView.getSelectionModel().selectedIndexProperty().asString("Selected Index: %d") );
		
		Label selectedNameLabel = new Label();
		selectedNameLabel.textProperty().bind(
				listView.getSelectionModel().selectedItemProperty().asString("SelectedItem: %s") );
		
		Button deleteNameButton = new Button("Delete Selected Item");
		deleteNameButton.setMaxWidth(Double.POSITIVE_INFINITY);
		deleteNameButton.disableProperty().bind( 
				listView.getSelectionModel().selectedIndexProperty().isEqualTo(-1) );
		deleteNameButton.setOnAction( e -> {
			int index = listView.getSelectionModel().getSelectedIndex();
			if (index >= 0)
				names.remove(index);
		});
		
		TextField addNameInput = new TextField();
		addNameInput.setPrefColumnCount(10);
		Button addNameButton = new Button("Add: ");
		addNameButton.setOnAction( e -> {
			String name = addNameInput.getText().trim();
			if (name.length() > 0) {
				names.add(name);
				addNameInput.selectAll();
				listView.scrollTo(names.size() - 1);  // make sure item is visible at the bottom of the list
			}
		});
		addNameButton.defaultButtonProperty().bind( addNameInput.focusedProperty() );
		HBox addNameHolder = new HBox(5,addNameButton,addNameInput);
		
		VBox nameBot = new VBox(12, selectedIndexLabel, selectedNameLabel, deleteNameButton, addNameHolder );
		nameBot.setPadding(new Insets(10));
		namePane.setBottom(nameBot);
		
		return namePane;
		
	} // end makeNumberPane()
	
	
	/**
	 * Make the pane for the right side of the window.  It contains a ListView<Integer>,
	 * with labels that show the selected index and selected item, a button for
	 * deleting the selected item, and a button and text input box that can be
	 * used to add an item at the end of the list.
	 */
	private BorderPane makeNumberPane() {
		
		/* This method is similar to makeNamePane(), except for the way that initial
		 * items are added to the list, and the use of a custom StringConverter.
		 * (Also, it works with listView.getItems() directly, instead of having a
		 * name for that list.) */
		
		ListView<Integer> listView = new ListView<>(); // start with an empty list.
		listView.setEditable(true);

		int[] primes = { 2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97 };
		for (int i = 0; i < primes.length; i++) { // add items to the ObservableList of items.
			listView.getItems().add(primes[i]);
		}
		
// We could use a standard IntegerStringConverter to convert items in the list to and from
// their string representation.  However, I don't like the way it handles bad input, so
// I will make my own.  To use a standard converter, use the command in the next line:
//		listView.setCellFactory(TextFieldListCell.forListView( new IntegerStringConverter() ));
	
		StringConverter<Integer> myConverter = new StringConverter<Integer>() {
			    // This custom string converter will convert a bad input string to
			    // null, instead of just failing.  And it will display a null value
			    // as "Bad Value" and an empty string value as 0.
			public Integer fromString(String s) {
				if (s == null || s.trim().length() == 0)
					return 0;
				try {
					return Integer.parseInt(s);
				}
				catch (NumberFormatException e) {
					return null;
				}
			}
			public String toString(Integer n) {
				if (n == null)
					return "Bad Value";
				return n.toString();
			}
		};
		
		listView.setCellFactory( TextFieldListCell.forListView( myConverter ));
		
		BorderPane numberPane = new BorderPane(listView);
		Label top = new Label("My Favorite Numbers");
		top.setPadding( new Insets(10) );
		top.setFont( Font.font(20) );
		top.setTextFill(Color.YELLOW);
		top.setMaxWidth(Double.POSITIVE_INFINITY);
		top.setAlignment(Pos.CENTER);
		top.setStyle("-fx-background-color: black");
		numberPane.setTop(top);
		
		Label selectedIndexLabel = new Label();
		selectedIndexLabel.textProperty().bind(
				listView.getSelectionModel().selectedIndexProperty().asString("Selected Index: %d") );
		
		Label selectedNumberLabel = new Label();
		selectedNumberLabel.textProperty().bind(
				listView.getSelectionModel().selectedItemProperty().asString("SelectedItem: %s") );
		
		Button deleteNumberButton = new Button("Delete Selected Item");
		deleteNumberButton.setMaxWidth(Double.POSITIVE_INFINITY);
		deleteNumberButton.disableProperty().bind( 
				listView.getSelectionModel().selectedIndexProperty().isEqualTo(-1) );
		deleteNumberButton.setOnAction( e -> {
			int index = listView.getSelectionModel().getSelectedIndex();
			if (index >= 0)
				listView.getItems().remove(index);
		});
		
		TextField addNumberInput = new TextField();
		addNumberInput.setPrefColumnCount(10);
		Button addNumberButton = new Button("Add: ");
		addNumberButton.setOnAction( e -> {
			String name = addNumberInput.getText().trim();
			if (name.length() > 0) {
				listView.getItems().add(Integer.parseInt(name));
				addNumberInput.selectAll();
				listView.scrollTo(listView.getItems().size() - 1);
			}
		});
		addNumberButton.defaultButtonProperty().bind( addNumberInput.focusedProperty() );
		HBox addNameHolder = new HBox(5,addNumberButton,addNumberInput);
		
		VBox nameBot = new VBox(12, selectedIndexLabel, selectedNumberLabel, deleteNumberButton, addNameHolder );
		nameBot.setPadding(new Insets(10));
		numberPane.setBottom(nameBot);
		
		return numberPane;
		
	} // end makeNumberPane()



} // end class EditListDemo

