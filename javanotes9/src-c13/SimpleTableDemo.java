
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Shows a Table that contains a list of the states of the United States
 * with their capital cities and population.  The entries in the table are 
 * not editable.
 */
public class SimpleTableDemo extends Application  {

	public static void main(String[] args) {
		launch(args);
	}
	//----------------------------------------------------------------------


	/**
	 * Just create a table and show it in the window.
	 */
	public void start(Stage stage) {
		
		/* Create a table where the data for a row is of type StateData, and
		 * add the items that hold the data for the rows.  The StateData class
		 * is defined below as a static nested class. */

		TableView<StateData> table = new TableView<>();
		for ( StateData item : stateData) {
			table.getItems().add(item);
		}

		/* Create a TableColumn object for each column.  A TablecColumn specifies
		 * a title for the column, and it has a "CellValueFactory" that is
		 * responsible for pulling the data for the column out of the objects
		 * that define the rows.  In this case, a standard CellValueFactory
		 * of type PropertyValueFactory is used; it simply calls a getter method
		 * for a named property.  For example, for the first column, the method
		 * getState() is called to get the state from each of the row objects. */
		
		TableColumn<StateData, String> stateCol = new TableColumn<>("State");
		stateCol.setCellValueFactory(new PropertyValueFactory<StateData, String>("state"));
		table.getColumns().add(stateCol);

		TableColumn<StateData, String> capitalCol = new TableColumn<>("Capital City");
		capitalCol.setCellValueFactory(new PropertyValueFactory<StateData, String>("capital"));
		table.getColumns().add(capitalCol);

		TableColumn<StateData, Integer> populationCol = new TableColumn<>("Population");
		populationCol.setCellValueFactory(new PropertyValueFactory<StateData, Integer>("population"));
		table.getColumns().add(populationCol);
		
		table.setPrefWidth(350);  // Table does not calculate a useful preferred width!
		
		StackPane root = new StackPane(table);  // Wrap table in a StackPane so I can have a border.
		root.setStyle("-fx-border-color:black; -fx-border-width:2");
		
		stage.setScene(new Scene(root));
		stage.setTitle("Trivial Table Demo");
		stage.show();
		
	}

	
	/**
	 * A table requires a class that defines the rows of the table.
	 * The class must be public and must have properties.  For a simple
	 * uneditable table, having a public getter method for each property
	 * that will appear in a table column is sufficient.
	 */
	public static class StateData {
		private String state;
		private String capital;
		private int population;
		public String getState() {
			return state;
		}
		public String getCapital() {
			return capital;
		}
		public int getPopulation() {
			return population;
		}
		public StateData(String s, String c, int p) {
			state = s;
			capital = c;
			population = p;
		}
	}

	/**
	 * An alphabetical list of the states of the United States and
	 * their capital cities and their populations in 2010.  Population 
	 * data is from http://www.theus50.com/fastfacts/population.php.
	 * The items from this array are added to the table as
	 * its items, so each row in the table will display data
	 * from one object from this array.
	 */
	private static StateData[] stateData = new StateData[] {
			new StateData( "Alabama", "Montgomery", 4779735 ),
			new StateData( "Alaska", "Juneau", 710231 ),
			new StateData( "Arizona", "Phoenix", 6329013 ),
			new StateData( "Arkansas", "Little Rock", 2915921 ),
			new StateData( "California", "Sacramento", 37253956 ),
			new StateData( "Colorado", "Denver", 5029196 ),
			new StateData( "Connecticut", "Hartford", 3574097 ),
			new StateData( "Delaware", "Dover", 897934 ),
			new StateData( "Florida", "Tallahassee", 18801311 ),
			new StateData( "Georgia", "Atlanta", 9687653 ),
			new StateData( "Hawaii", "Honolulu", 1360301 ),
			new StateData( "Idaho", "Boise", 1567582 ),
			new StateData( "Illinois", "Springfield", 12830632 ),
			new StateData( "Indiana", "Indianapolis", 6483800 ),
			new StateData( "Iowa", "Des Moines", 3046350 ),
			new StateData( "Kansas", "Topeka", 2853118 ),
			new StateData( "Kentucky", "Frankfort", 4339362 ),
			new StateData( "Louisiana", "Baton Rouge", 4533372 ),
			new StateData( "Maine", "Augusta", 1328361 ),
			new StateData( "Maryland", "Annapolis", 5773552 ),
			new StateData( "Massachusetts", "Boston", 6547629 ),
			new StateData( "Michigan", "Lansing", 9883635 ),
			new StateData( "Minnesota", "St. Paul", 5303925 ),
			new StateData( "Mississippi", "Jackson", 2967297 ),
			new StateData( "Missouri", "Jefferson City", 5988927 ),
			new StateData( "Montana", "Helena", 989415 ),
			new StateData( "Nebraska", "Lincoln", 1826341 ),
			new StateData( "Nevada", "Carson City", 2700551 ),
			new StateData( "New Hampshire", "Concord", 1316472 ),
			new StateData( "New Jersey", "Trenton", 8791894 ),
			new StateData( "New Mexico", "Santa Fe", 2059180 ),
			new StateData( "New York", "Albany", 19378104 ),
			new StateData( "North Carolina", "Raleigh", 9535475 ),
			new StateData( "North Dakota", "Bismarck", 672591 ),
			new StateData( "Ohio", "Columbus", 11536502 ),
			new StateData( "Oklahoma", "Oklahoma City", 3751354 ),
			new StateData( "Oregon", "Salem", 3831074 ),
			new StateData( "Pennsylvania", "Harrisburg", 12702379 ),
			new StateData( "Rhode Island", "Providence", 1052567 ),
			new StateData( "South Carolina", "Columbia", 4625364 ),
			new StateData( "South Dakota", "Pierre", 814180 ),
			new StateData( "Tennessee", "Nashville", 6346110 ),
			new StateData( "Texas", "Austin", 25145561 ),
			new StateData( "Utah", "Salt Lake City", 2763885 ),
			new StateData( "Vermont", "Montpelier", 625741 ),
			new StateData( "Virginia", "Richmond", 8001024 ),
			new StateData( "Washington", "Olympia", 6724540 ),
			new StateData( "West Virginia", "Charleston", 1852996 ),
			new StateData( "Wisconsin", "Madison", 5686986 ),
			new StateData( "Wyoming", "Cheyenne", 563626 )
	};

}
