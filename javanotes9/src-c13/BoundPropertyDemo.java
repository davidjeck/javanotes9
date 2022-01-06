import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.beans.binding.When;

/**
 * This program demonstrates bindings and bidirectional bindings
 * of JavaFX properties.  A window shows a large label.  The text
 * property of the label is bound to the text property of a text
 * field, so that any change to the text field automatically changes
 * the text in the label.  Color of the text is controlled by a
 * set of RadioButtons or, alternatively, by a set of RadioMenuItems.
 * The  selectedProperty of each radio button is bidirectionally
 * bound to the selectedProperty of the corresponding radio menu item,
 * so that they always have the same selected state.  Finally, the
 * background color of the label is bound, though an object of 
 * type javafx.beans.binding.When, to the selectedProperty of a
 * checkbox; when the checkbox is checked, the background is
 * pink, otherwise it is white.
 */
public class BoundPropertyDemo extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	//-----------------------------------------------------------------
	
	public void start(Stage stage) {
		
		/* Define the two possible Backgrounds for the label. */
		
		Background whiteBG = new Background(new BackgroundFill(Color.WHITE, null, null));
		Background colorBG = new Background(new BackgroundFill(Color.PINK, null, null));
		
		/* A label with huge text that can grow to fill the entire available area. */
		
		Label message = new Label("Hello World");
		message.setMaxWidth(Double.POSITIVE_INFINITY);
		message.setStyle("-fx-font: bold 72pt serif");
		message.setBackground(colorBG);
		
		/* The label is the center component of a BorderPane, which is the scene graph root. */
		
		BorderPane root = new BorderPane(message);
		root.setStyle("-fx-border-color:gray; -fx-border-width:5px");
		
		/* Define a textfield and bind the textProperty of the label to the
		 * textProperty of the textfield.  When the user types in the
		 * textfield, the text on the label is also changed. */
		
		TextField messageInput = new TextField("Hello World");
		message.textProperty().bind(messageInput.textProperty());
		
		/* Define a checkbox.  Use an object of type When to make an observable
		 * value of type Background that is equal to the pink background when the
		 * checkbox is checked and to the white background when the checkbox
		 * is not checked.  Bind the backgroundProperty() of the label to that
		 * observable value, so that the checkbox will control the label background. */
		
		CheckBox bgCheck = new CheckBox("Use pink background");
		message.backgroundProperty().bind( 
				new When(bgCheck.selectedProperty()).then(colorBG).otherwise(whiteBG));
		
		/* Define a slider and a label that will display the slider's value.  The
		 * label's textProperty is bound to a string property that is derived from
		 * the valueProperty of the slider using the asString(format) method. */
		
		Label sliderVal = new Label("10");
		Slider slider = new Slider();
		sliderVal.textProperty().bind( slider.valueProperty().asString("Slider Value: %1.2f") );

		/* Place some components in an HBox at the bottom of the BorderPane.  */
		
		HBox bottom = new HBox(10, new Label("Type here:"), messageInput, bgCheck, slider, sliderVal);
		bottom.setStyle("-fx-padding: 10px; -fx-border-color:gray; -fx-border-width: 5px 0 0 0");
		root.setBottom(bottom);
		
		/* Make a menubar with a Color menu containing a radiogroup for setting the color
		 * of the text in the label.  Make a corresponding group of radio buttons and put
		 * them in a VBox at the right of the BorderPane.  Bidirectionally bind the radio
		 * buttons to the radio menu items, to keep the selection in the menu in synch
		 * with the selection in the group of buttons.  Only the menu items are added
		 * to a ToggleGroup, to ensure that only one color is selected. */
		
		VBox right = new VBox(8);
		right.setStyle("-fx-padding: 10px; -fx-border-color:gray; -fx-border-width: 0 0 0 5px");
		Menu colorMenu = new Menu("Color");
		MenuBar menubar = new MenuBar(colorMenu);
		menubar.setStyle("-fx-border-color:gray; -fx-border-width: 0 0 5px 0");
		Color[] colors = { Color.BLACK, Color.RED, Color.GREEN, Color.BLUE };
		String[] colorNames = { "Black", "Red", "Green", "Blue" };
		ToggleGroup colorGroup = new ToggleGroup();
		for (int i = 0; i <colors.length; i++) {
			RadioButton button = new RadioButton(colorNames[i]);
			RadioMenuItem menuItem = new RadioMenuItem(colorNames[i]);
			button.selectedProperty().bindBidirectional(menuItem.selectedProperty());
			menuItem.setToggleGroup(colorGroup);
			    // Note how UserData is used to store the color object
			    // associated with the menu item, for later use. 
			menuItem.setUserData(colors[i]);
			right.getChildren().add(button);
			colorMenu.getItems().add(menuItem);
			if (i == 0)
				menuItem.setSelected(true);
		}
		colorGroup.selectedToggleProperty().addListener( e -> {
			        // Listen for changes to the selectedToggleProperty
			        // of the ToggleGroup, so that the color of the
			        // label can be set to match the selected menu item.
			Toggle t = colorGroup.getSelectedToggle();
			if (t != null) {
				    // t is the selected RadioMenuItem.  Get the color
				    // from its UserData, and use it to set the color
				    // of the text.
				Color c = (Color)t.getUserData();
				message.setTextFill(c);
			}
		});
		root.setRight(right);
		root.setTop(menubar);
		
		/* finish setup and show the window. */
		
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.setTitle("Bound Property Demo");
		stage.show();
		
	}
	
} // end class BoundPropertyDemo
