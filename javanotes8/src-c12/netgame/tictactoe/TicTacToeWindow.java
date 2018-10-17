package netgame.tictactoe;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.io.IOException;
import netgame.common.*;

/**
 * This window represents one player in a two-player networked
 * game of TicTacToe.  The window is meant to be created by
 * the program netgame.tictactoe.Main.
 */
public class TicTacToeWindow extends Stage {
	
	/**
	 * The state of the game.  This state is a copy of the official
	 * state, which is stored on the server.  When the state changes,
	 * the state is sent as a message to this window.  (It is actually
	 * sent to the TicTacToeClient object that represents the connection
	 * to the server.)  When that happens, the state that was received
	 * in the message replaces the value of this variable, and the
	 * board and UI is updated to reflect the changed state.  This
	 * is done in the newState() method, which is called by the
	 * TicTacToeClient object.
	 */
	private TicTacToeGameState state;
	
	private volatile boolean connecting;  // Set to true until connection is established to hub.
	
	private Canvas board;     // A panel that displays the board.  The user
	                         // makes moves by clicking on this panel.

	private Label message;  // Displays messages to the user about the status of the game.
	
	private int myID;        // The ID number that identifies the player using this window.

	private TicTacToeClient connection;  // The Client object for sending and receiving 
	                                     // network messages.
	

	/**
	 * Creates, configures, and opens the window. Also creates a thread that attempts to
	 * open a connection to the server.
	 * @param hostName  the name or IP address of the host where the server is running.
	 * @param serverPortNumber  the port number on the server computer where 
	 *                            the Hub is listening for connections.
	 */
	public TicTacToeWindow(String hostName, int serverPortNumber) {

		connecting = true;
		
		message = new Label("Waiting for connection.");
		message.setFont( Font.font("Arial", FontWeight.BOLD, 16) );
		message.setPadding( new Insets(10));
		board = new Canvas(400,400);
		board.setOnMousePressed( e -> doMouseClick(e.getX(), e.getY()) );
		drawBoard();

		BorderPane content = new BorderPane(board);
		content.setBottom(message);
		BorderPane.setAlignment(message, Pos.CENTER);

		setScene( new Scene(content) );
		setTitle("Net TicTacToe");
		setResizable(false);
		setOnHidden( evt -> {
			    // When the user clicks the window's close box, this listener will
			    // send a disconnect message to the Hub and will end the program.
			    // The other player will then be notified that this player has disconnected.
			if (connection != null) {
				connection.disconnect();  // Send a disconnect message to the hub.
				try {
					Thread.sleep(333); // Wait one-third second to allow the message to be sent.
				}
				catch (InterruptedException e) {
				}
			}
			System.exit(0);  // In case connecting thread is still around, make sure it dies.
		});
		setX(100 + 50*Math.random());
		setY(100 + 50*Math.random());
		show();
		
		Thread connector = new Thread( () -> connect(hostName, serverPortNumber) );
		connector.start();
		
	} // end constructor
	
	
	/**
	 * This class defines the client object that handles communication with the Hub.
	 */
	private class TicTacToeClient extends Client {

		/**
		 * Connect to the hub at a specified host name and port number.
		 */
		public TicTacToeClient(String hubHostName,int hubPort) throws IOException {
			super(hubHostName, hubPort);
		}

		/**
		 * Responds to a message received from the Hub.  The only messages that
		 * are supported are TicTacToeGameState objects.  When one is received,
		 * the newState() method in the TicTacToeWindow class is called. That
		 * method is called using Platform.runLater() so that it will run on
		 * the JavaFX application thread.
		 */
		protected void messageReceived(Object message) {
			if (message instanceof TicTacToeGameState) {
				Platform.runLater( () -> newState( (TicTacToeGameState)message ) );
			}
		}

		/**
		 * If a shutdown message is received from the Hub, the user is notified
		 * and the program ends.
		 */
		protected void serverShutdown(String message) {
			Platform.runLater( () -> {
				Alert alert = new Alert(Alert.AlertType.INFORMATION,
							"Your opponent has disconnected.\nThe game is ended.");
				TicTacToeWindow.this.hide();
				alert.showAndWait();
				System.exit(0);
			});
		}
		
	} // end nested class TicTacToeClient
	
	

	/**
	 * When the window is created, this method is called in a separate
	 * thread to make the connection to the server.  If an error
	 * occurs, the program is terminated.
	 */
	private void connect(String hostName, int serverPortNumber) {
		TicTacToeClient c;
		int id;
		try {
			c = new TicTacToeClient(hostName, serverPortNumber);
			id = c.getID();
			Platform.runLater( () -> {
				connecting = false;
				connection = c;
				myID = id;
				drawBoard();
				message.setText("Waiting for two players to connect.");
			});
		}
		catch (Exception e) {
			Platform.runLater( () -> {
				Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sorry, could not connect to\n"
						+ hostName + " on port " + serverPortNumber + "\nShutting down.");
				alert.showAndWait();
				System.exit(0);
			});
		}
	}
		
	
	private void drawBoard() {
		GraphicsContext g = board.getGraphicsContext2D();
		g.setFill(Color.WHITE);
		g.fillRect(0,0,board.getWidth(),board.getHeight());
		g.setStroke(Color.BLACK);
		g.setLineWidth(6);
		g.strokeRect(0,0,board.getWidth(),board.getHeight());
		g.setFill(Color.BLACK);
		if (connecting) {
			g.fillText("Connecting...", 20, 35);
			return;
		}
		if (state == null || state.board == null) {
			g.fillText("Starting up.", 20, 35);
			return;
		}
		g.setLineWidth(10);
		g.setStroke(Color.BLACK);
		g.strokeLine(150,50,150,350);
		g.strokeLine(250,50,250,350);
		g.strokeLine(50,150,350,150);
		g.strokeLine(50,250,350,250);
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if (state.board[row][col] == 'X') {
					g.setStroke(Color.RED);
					g.strokeLine(70+col*100, 70+row*100, 130+col*100, 130+row*100);
					g.strokeLine(70+col*100, 130+row*100, 130+col*100, 70+row*100);
				}
				else if (state.board[row][col] == 'O') {
					g.setStroke(Color.BLUE);
					g.strokeOval(65+col*100,65+row*100, 70, 70);
				}
			}
		}
	}


	/**
	 * This method is called when the user clicks the tictactoe board.  If the
	 * click represents a legal move at a legal time, then a message is sent
	 * to the Hub to inform it of the move.  The Hub will change the game
	 * state and send the new state to both players.  It is very important that
	 * the game clients do not change the game state directly, since the
	 * "official" game state is maintained by the Hub.  Doing things this
	 * way guarantees that both players see the same board.
	 */
	private void doMouseClick(double x, double y) {
		if (state == null || state.board == null)
			return;
		if (!state.gameInProgress) {
				// After a game ends, the winning player -- or either
				// player in the event of a tie -- can start a new
				// game by clicking the board.  When this happens,
			    // the String "newgame" is sent as a message to Hub.
			if (state.gameEndedInTie || myID == state.winner)
			   connection.send("newgame");  // Start a new game.
			return;
		}
		if (myID !=state.currentPlayer) {
			return;  // it's not this player's turn.
		}
		int row = (int)((y-50) / 100);
		int col = (int)((x-50) / 100);
		if (row >= 0 && row < 3 && col >= 0 && col < 3 && state.board[row][col] == ' ' ) {
			   // User has clicked an empty square.  Send the move to the Hub
			   // as an array of two ints containing the row number and column
			   // number of the square where the user clicked.
			connection.send( new int[] { row, col } );
		}
	}
	
	
	/**
	 * This method is called when a new game state is received from the hub.
	 * It stores the new state in the instance variable that represents the
	 * game state and updates the user interface to reflect the state.
	 * Note that this method is called on the application thread (using
	 * Platform.runLater()) to avoid synchronization problems.
	 */
	private void newState(TicTacToeGameState state) {
		if ( state.playerDisconnected ) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION,
					                   "Your opponent has disconnected.\nThe game is ended.");
			alert.showAndWait();
			System.exit(0);
		}
		this.state = state;
		drawBoard();
		if ( state.board == null ) {
			return;  // haven't started yet -- waiting for 2nd player
		}
		else if ( ! state.gameInProgress ) {
			setTitle("Game Over");
			if ( state.gameEndedInTie )
				message.setText("Game ended in tie. Click to start again.");
			else if (myID == state.winner)
				message.setText("You won!  Click to start a new game.");
			else
				message.setText("You lost.  Waiting for new game...");
		}
		else {
			if (myID == state.playerPlayingX)
				setTitle("You are playing X's");
			else
				setTitle("You are playing O's");
			if (myID == state.currentPlayer)
				message.setText("Your move");
			else
				message.setText("Waiting for opponent's move");
		}
	}

} // end TicTacToeWindow

