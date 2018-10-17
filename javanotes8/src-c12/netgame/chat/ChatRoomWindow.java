package netgame.chat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;

import java.io.IOException;
import java.util.Optional;

import netgame.common.*;

/* This class is a demo of the "netgame" package.  It's not exactly a game, but
 * it uses the netgame infrastructure of Hub + Clients to send and receive
 * messages in the chat room.  The chat room server is just a netgame Hub.
 * A ChatRoomWindow has a subclass that represents a Client for that Hub.
 * You must run ChatRoomServer on a known computer.  Several copies of
 * ChatRoomWindow can then connect to that server.
 */

/**
 * This class represents a client for a "chat room" application.  The chat
 * room is hosted by a server running on some computer.  The user of this
 * program must know the host name (or IP address) of the computer that
 * hosts the chat room.  When this program is run, it asks for that
 * information.  Then, it opens a window that has an input box where the
 * user can enter messages to be sent to the chat room.  The message is 
 * sent when the user presses return in the input box or when the
 * user clicks a Send button.  There is also a text area that shows 
 * a transcript of all messages from participants in the chat room.
 * <p>Participants in the chat room are represented only by ID numbers
 * that are assigned to them by the server when they connect.
 */
public class ChatRoomWindow extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	//----------------------------------------------------------------------------
	
	private final static int PORT = 37829; // The ChatRoom port number; can't be 
	                                       // changed here unless the ChatRoomServer
	                                       // program is also changed.

	private TextField messageInput;   // For entering messages to be sent to the chat room.
	private Button sendButton;        // Sends the contents of the messageInput.
	private Button quitButton;        // Leaves the chat room cleanly, by sending a DisconnectMessage.
	
	private TextArea transcript;      // Contains all messages sent by chat room participant, as well
	                                  //    as a few additional status messages, 
	                                  //    such as when a new user arrives.
	
	private ChatClient connection;      // Represents the connection to the Hub; used to send messages;
	                                    // also receives and processes messages from the Hub.
	
	private volatile boolean connected; // This is true while the client is connected to the hub.
	
	
	/**
	 * Gets the host name (or IP address) of the chat room server from the
	 * user and then opens the main window.  The program ends when the user
	 * closes the window.
	 */
	public void start( Stage stage ) {
		
		TextInputDialog question = new TextInputDialog();
		question.setHeaderText("Enter the host name of the\ncomputer that hosts the chat room.");
		question.setContentText("Host Name:");
		Optional<String> response = question.showAndWait();
		if ( ! response.isPresent() )
			System.exit(0);
		String host = response.get().trim();
		if (host == null || host.trim().length() == 0)
			System.exit(0);

		transcript = new TextArea();
		transcript.setPrefRowCount(30);
		transcript.setPrefColumnCount(60);
		transcript.setWrapText(true);
		transcript.setEditable(false);

		sendButton = new Button("send");
		quitButton = new Button("quit");
		messageInput = new TextField();
		messageInput.setPrefColumnCount(40);
		sendButton.setOnAction( e -> doSend() );
		quitButton.setOnAction( e -> doQuit() );
		sendButton.setDefaultButton(true);
		sendButton.setDisable(true);
		messageInput.setEditable(false);
		messageInput.setDisable(true);
		
		HBox bottom = new HBox(8, new Label("YOU SAY:"), messageInput, sendButton, quitButton);
		HBox.setHgrow(messageInput, Priority.ALWAYS);
		HBox.setMargin(quitButton, new Insets(0,0,0,50));
		bottom.setPadding(new Insets(8));
		bottom.setStyle("-fx-border-color: black; -fx-border-width:2px");
		BorderPane root = new BorderPane(transcript);
		root.setBottom(bottom);
		
		stage.setScene( new Scene(root) );
		stage.setTitle("Networked Chat");
		stage.setResizable(false);
		stage.setOnHidden( e -> doQuit() );
		stage.show();
		
		new Thread() {
			    // This is a thread that opens the connection to the server.  Since
			    // that operation can block, it's not done directly in the constructor.
			    // Once the connection is established, the user interface elements are
			    // enabled so the user can send messages.  The Thread dies after
			    // the connection is established or after an error occurs.
			public void run() {
				try {
					addToTranscript("Connecting to " + host + " ...");
					connection = new ChatClient(host);
					connected = true;
					Platform.runLater( () -> {
						messageInput.setEditable(true);
						messageInput.setDisable(false);
						sendButton.setDisable(false);
						messageInput.requestFocus();
					});
				}
				catch (IOException e) {
					addToTranscript("Connection attempt failed.");
					addToTranscript("Error: " + e);
				}
			}
		}.start();

	}
	
	
	/**
	 * A ChatClient connects to the Hub and is used to send messages to
	 * and receive messages from a Hub.  Messages received from the
	 * Hub will be of type ForwardedMessage and will contain the
	 * ID number of the sender and the string that was sent by
	 * that user.
	 */
	private class ChatClient extends Client {

		/**
		 * Opens a connection to the chat room server on a specified computer.
		 */
		ChatClient(String host) throws IOException {
			super(host, PORT);
		}

		/**
		 * Responds when a message is received from the server.  It should be
		 * a ForwardedMessage representing something that one of the participants
		 * in the chat room is saying.  The message is simply added to the
		 * transcript, along with the ID number of the sender.
		 */
		protected void messageReceived(Object message) {
			if (message instanceof ForwardedMessage) {  // (no other message types are expected)
				ForwardedMessage bm = (ForwardedMessage)message;
				addToTranscript("#" + bm.senderID + " SAYS:  " + bm.message);
			}
		}

		/**
		 * Called when the connection to the client is shut down because of some
		 * error message.  (This will happen if the server program is terminated.)
		 */
		protected void connectionClosedByError(String message) {
			addToTranscript("Sorry, communication has shut down due to an error:\n     " + message);
			Platform.runLater( () -> {
				sendButton.setDisable(true);
				messageInput.setEditable(false);
				messageInput.setDisable(true);
				messageInput.setText("");
			});
			connected = false;
			connection = null;
		}

		/**
		 * Posts a message to the transcript when someone joins the chat room.
		 */
		protected void playerConnected(int newPlayerID) {
			addToTranscript("Someone new has joined the chat room, with ID number " + newPlayerID);
		}

		/**
		 * Posts a message to the transcript when someone leaves the chat room.
		 */
		protected void playerDisconnected(int departingPlayerID) {
			addToTranscript("The person with ID number " + departingPlayerID + " has left the chat room");
		}

	} // end nested class ChatClient

	
	
	
	
	/**
	 * Adds a string to the transcript area, followed by a blank line.
	 */
	private void addToTranscript(String message) {
		Platform.runLater( () ->	transcript.appendText(message + "\n\n") );
	}
	
	
	/**
	 * Called when the user clicks the Quit button or closes
	 * the window by clicking its close box. Called from the
	 * application thread.
	 */
	private void doQuit() {
		if (connected)
			connection.disconnect();  // Sends a DisconnectMessage to the server.
		try {
			Thread.sleep(500); // Time for DisconnectMessage to actually be sent.
		}
		catch (InterruptedException e) {
		}
		System.exit(0);
	}

	

	/** 
	 * Send the string entered by the user as a message
	 * to the Hub, using the ChatClient that handles communication
	 * for this ChatRoomWindow.  Note that the string is not added
	 * to the transcript here.  It will get added after the Hub
	 * receives the message and broadcasts it to all clients,
	 * including this one.  Called from the application thread.
	 */
	private void doSend() {
		String message = messageInput.getText();
		if (message.trim().length() == 0)
			return;
		connection.send(message);
		messageInput.selectAll();
		messageInput.requestFocus();
	}
	

}  // end class ChatRoomWindow
