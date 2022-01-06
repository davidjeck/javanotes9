package netgame.tictactoe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.io.IOException;
import netgame.common.*;


/**
 *  A main class for the network TicTacToe game.  This program
 *  shows a primary window where the user can choose to be a server or
 *  a client.  If the user chooses to be a server, then a TicTacToeHub
 *  is created to manage the game; the game will not start until a
 *  second player has connected as a client.  To act as a client,
 *  the user must know the host name or IP address of the computer
 *  and the port number where the server is waiting for a connection.
 *  When run as a client, this program does not create a hub;
 *  rather, it connects to the hub that was created by the server.
 *  In either case, a TicTacToeWindow is created where the game will 
 *  be played.
 */
public class Main extends Application {

	private static final int DEFAULT_PORT = 45017;
	
	public static void main(String[] args) {
		launch(args);
	}
	//------------------------------------------------------------------------------
	
	private Stage window;  // The first window that shows on the screen, with connection controls.
	
	private Label message;
	private TextField listeningPortInput;
	private TextField hostInput;
	private TextField connectPortInput;
	
	public void start(Stage stage) {
		
		window = stage;
		
		Button okButton = new Button("OK");
		okButton.setDefaultButton(true);
		Button cancelButton = new Button("Cancel");
		cancelButton.setCancelButton(true);
		
		message = new Label("Welcome to Networked TicTacToe!");
		message.setFont(Font.font("Times New Roman", FontWeight.BOLD, 24));
		
		listeningPortInput = new TextField("" + DEFAULT_PORT);
		listeningPortInput.setPrefColumnCount(5);
		hostInput = new TextField();
		hostInput.setPrefColumnCount(30);
		connectPortInput = new TextField("" + DEFAULT_PORT);
		connectPortInput.setPrefColumnCount(5);
		
		RadioButton selectServerMode = new RadioButton("Start a new game");
		selectServerMode.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		RadioButton selectClientMode = new RadioButton("Connect to existing game");
		selectClientMode.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		
		ToggleGroup group = new ToggleGroup();
		selectServerMode.setToggleGroup(group);
		selectClientMode.setToggleGroup(group);
		
		Label listenPortLabel = new Label("Listen On Port: ");
		listenPortLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		Label hostLabel = new Label("Computer: ");
		hostLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		Label connectPortLabel = new Label("Port Number: ");
		connectPortLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		
		HBox row2 = new HBox(listenPortLabel,listeningPortInput);
		HBox row4 = new HBox(hostLabel,hostInput);
		HBox row5 = new HBox(connectPortLabel,connectPortInput);
		
		VBox inputs = new VBox(15,message,selectServerMode,row2,selectClientMode,row4,row5);
		VBox.setMargin(row2, new Insets(0,0,0,50));
		VBox.setMargin(row4, new Insets(0,0,0,50));
		VBox.setMargin(row5, new Insets(0,0,0,50));
		inputs.setStyle("-fx-padding:20px; -fx-border-color:black; -fx-border-width:2px");
		HBox bottom = new HBox(8,cancelButton,okButton);
		bottom.setPadding(new Insets(10,0,0,0));
		bottom.setAlignment(Pos.CENTER);
		BorderPane root = new BorderPane();
		root.setCenter(inputs);
		root.setBottom(bottom);
		root.setPadding( new Insets(15,15,10,15) );
	
		stage.setScene( new Scene(root) );
		stage.setTitle("Net TicTacToe");
		stage.setResizable(false);
		
		cancelButton.setOnAction( e -> Platform.exit() );
		okButton.setOnAction( e -> doOK( selectServerMode.isSelected() ) );

		selectServerMode.setOnAction( e -> {
			listeningPortInput.setDisable(false);
			hostInput.setDisable(true);
			connectPortInput.setDisable(true);
			listeningPortInput.setEditable(true);
			hostInput.setEditable(false);
			connectPortInput.setEditable(false);
		});
		selectClientMode.setOnAction( e -> {
			listeningPortInput.setDisable(true);
			hostInput.setDisable(false);
			connectPortInput.setDisable(false);
			listeningPortInput.setEditable(false);
			hostInput.setEditable(true);
			connectPortInput.setEditable(true);
		});
		
		selectServerMode.setSelected(true);
		hostInput.setDisable(true);
		connectPortInput.setDisable(true);
		hostInput.setEditable(false);
		connectPortInput.setEditable(false);
		
		stage.show();
		
	} // end start()

	
	private void errorMessage(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR, message);
		alert.showAndWait();
	}
	
	
	private void doOK( boolean openAsServer) {
		// If the user has choosen to run as the server, then a TicTacToeGameHub (server) 
		// is created and after that a TicTacToeWindow is created that connects to the new
		// server running on  localhost.  In that case, the game will wait for a second 
		// connection. 
	    //    If the user chooses to connect to an existing server, then only
		// a TicTacToeWindow is created, which will connect to the specified
		// host where the server is running.
		
		if (openAsServer) {
			int port;
			try {
				port = Integer.parseInt(listeningPortInput.getText().trim());
				if (port <= 0)
					throw new Exception();
			}
			catch (Exception e) {
				errorMessage("The value in the \"Listen on port\" box\nis not a legal positive integer!");
				message.setText("Illegal port number.  Please try again!");
				listeningPortInput.selectAll();
				listeningPortInput.requestFocus();
				return;
			}
			Hub hub;
			try {
				hub = new TicTacToeGameHub(port);
			}
			catch (Exception e) {
				errorMessage("Sorry, could not listen on port number " + port);
				message.setText("Please try a different port number!");
				listeningPortInput.selectAll();
				listeningPortInput.requestFocus();
				return;
			}
			new TicTacToeWindow("localhost", port);
			window.hide();
		}
		else {
			String host;
			int port;
			host = hostInput.getText().trim();
			if (host.length() == 0) {
				errorMessage("You must enter the name or IP address\nof the computer that is hosting the game.");
				message.setText("You must enter a computer name!");
				hostInput.requestFocus();
				return;
			}
			try {
				port = Integer.parseInt(connectPortInput.getText().trim());
				if (port <= 0)
					throw new Exception();
			}
			catch (Exception e) {
				errorMessage("The value in the \"Port Number\" box\nis not a legal positive integer!");
				message.setText("Illegal port number.  Please try again!");
				connectPortInput.selectAll();
				connectPortInput.requestFocus();
				return;
			}
			new TicTacToeWindow(host,port);
			window.hide();
		}
		
	} // end doOK
			
		
} // end TicTacToe Main
