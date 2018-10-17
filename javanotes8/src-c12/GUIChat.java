import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.io.*;
import java.net.*;


/**
 * Opens a window that can be used for a two-way network chat.
 * The window can "listen" for a connection request on a port
 * that is specified by the user.  It can request a connection
 * to another GUIChat window on a specified computer and port.
 * The window has an input box where the user can enter
 * messages to be sent over the connection.  A connection
 * can be closed by clicking a button in the window or by
 * closing the window.  To test the program, several
 * copies of the program can be run on the same computer.
 */
public class GUIChat extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	//--------------------------------------------------------------

	/**
	 * Possible states of the thread that handles the network connection.
	 */
	private enum ConnectionState { LISTENING, CONNECTING, CONNECTED, CLOSED }

	/**
	 * Default port number.  This is the initial content of input boxes in
	 * the window that specify the port number for the connection. 
	 */
	private static String defaultPort = "1501";

	/**
	 * Default host name.  This is the initial content of the input box that
	 * specifies the name of the computer to which a connection request
	 * will be sent.
	 */
	private static String defaultHost = "localhost";


	/**
	 * The thread that handles the connection; defined by a nested class.
	 */
	private volatile ConnectionHandler connection;

	/**
	 * Control buttons that appear in the window.
	 */
	private Button listenButton, connectButton, closeButton, 
				   clearButton, quitButton, saveButton, sendButton;

	/**
	 * Input boxes for connection information (port numbers and host names).
	 */
	private TextField listeningPortInput, remotePortInput, remoteHostInput;

	/**
	 * Input box for messages that will be sent to the other side of the
	 * network connection.
	 */
	private TextField messageInput;

	/**
	 * Contains a transcript of messages sent and received, along with
	 * information about the progress and state of the connection.
	 */
	private TextArea transcript;
	
	/**
	 * The program's window.
	 */
	private Stage window;
	
	
	/**
	 * Set up the GUI and event handling.
	 */
	public void start(Stage stage) {
		window = stage;
		
		listenButton = new Button("Listen on port:");
		listenButton.setOnAction( this::doAction );
		connectButton = new Button("Connect to:");
		connectButton.setOnAction( this::doAction );
		closeButton = new Button("Disconnect");
		closeButton.setOnAction( this::doAction );
		closeButton.setDisable(true);
		clearButton = new Button("Clear Transcript");
		clearButton.setOnAction( this::doAction );
		sendButton = new Button("Send");
		sendButton.setOnAction( this::doAction );
		sendButton.setDisable(true);
		sendButton.setDefaultButton(true);
		saveButton = new Button("Save Transcript");
		saveButton.setOnAction( this::doAction );
		quitButton = new Button("Quit");
		quitButton.setOnAction( this::doAction );
		messageInput = new TextField();
		messageInput.setOnAction( this::doAction );
		messageInput.setEditable(false);
		transcript = new TextArea();
		transcript.setPrefRowCount(20);
		transcript.setPrefColumnCount(60);
		transcript.setWrapText(true);
		transcript.setEditable(false);
		listeningPortInput = new TextField(defaultPort);
		listeningPortInput.setPrefColumnCount(5);
		remotePortInput = new TextField(defaultPort);
		remotePortInput.setPrefColumnCount(5);
		remoteHostInput = new TextField(defaultHost);
		remoteHostInput.setPrefColumnCount(18);
		
		HBox buttonBar = new HBox(5, quitButton, saveButton, clearButton, closeButton);
		buttonBar.setAlignment(Pos.CENTER);
		HBox connectBar = new HBox(5, listenButton, listeningPortInput, connectButton, 
				                      remoteHostInput, new Label("port:"), remotePortInput);
		connectBar.setAlignment(Pos.CENTER);
		VBox topPane = new VBox(8, connectBar, buttonBar);
		BorderPane inputBar = new BorderPane(messageInput);
		inputBar.setLeft( new Label("Your Message:"));
		inputBar.setRight(sendButton);
		BorderPane.setMargin(messageInput, new Insets(0,5,0,5));
		
		BorderPane root = new BorderPane(transcript);
		root.setTop(topPane);
		root.setBottom(inputBar);
		root.setStyle("-fx-border-color: #444; -fx-border-width: 3px");
		inputBar.setStyle("-fx-padding:5px; -fx-border-color: #444; -fx-border-width: 3px 0 0 0");
		topPane.setStyle("-fx-padding:5px; -fx-border-color: #444; -fx-border-width: 0 0 3px 0");

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Two-user Networked Chat");
		stage.setOnHidden( e -> {
			   // If a connection exists when the window is closed, close the connection.
			if (connection != null) 
				connection.close(); 
		});
		stage.show();

	} // end start()
	
	
	/**
	 * A little wrapper for showing an error alert.
	 */
	private void errorMessage(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR, message);
		alert.showAndWait();
	}

	/**
	 * Defines responses to buttons.  (In this program, I use one
	 * method to handle all the buttons; the source of the event
	 * can be used to determine which button was clicked.)
	 */
	private void doAction(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == listenButton) {
			if (connection == null || 
					connection.getConnectionState() == ConnectionState.CLOSED) {
				String portString = listeningPortInput.getText();
				int port;
				try {
					port = Integer.parseInt(portString);
					if (port < 0 || port > 65535)
						throw new NumberFormatException();
				}
				catch (NumberFormatException e) {
					errorMessage(portString + "is not a legal port number.");
					return;
				}
				connectButton.setDisable(true);
				listenButton.setDisable(true);
				closeButton.setDisable(false);
				connection = new ConnectionHandler(port);
			}
		}
		else if (source == connectButton) {
			if (connection == null || 
					connection.getConnectionState() == ConnectionState.CLOSED) {
				String portString = remotePortInput.getText();
				int port;
				try {
					port = Integer.parseInt(portString);
					if (port < 0 || port > 65535)
						throw new NumberFormatException();
				}
				catch (NumberFormatException e) {
					errorMessage(portString +"is not a legal port number.");
					return;
				}
				connectButton.setDisable(true);
				listenButton.setDisable(true);
				connection = new ConnectionHandler(remoteHostInput.getText(),port);
			}
		}
		else if (source == closeButton) {
			if (connection != null)
				connection.close();
		}
		else if (source == clearButton) {
			transcript.setText("");
		}
		else if (source == quitButton) {
			try {
				window.hide();
			}
			catch (SecurityException e) {
			}
		}
		else if (source == saveButton) {
			doSave();
		}
		else if (source == sendButton || source == messageInput) {
			if (connection != null && 
					connection.getConnectionState() == ConnectionState.CONNECTED) {
				connection.send(messageInput.getText());
				messageInput.selectAll();
				messageInput.requestFocus();
			}
		}
	}
	

	/**
	 * Save the contents of the transcript area to a file selected by the user.
	 */
	private void doSave() {
		FileChooser fileDialog = new FileChooser(); 
		fileDialog.setInitialFileName("transcript.txt");
		fileDialog.setInitialDirectory(new File(System.getProperty("user.home")));
		fileDialog.setTitle("Select File to be Saved");
		File selectedFile = fileDialog.showSaveDialog(window);
		if (selectedFile == null)
			return;  // User canceled or clicked the dialog's close box.
		PrintWriter out; 
		try {
			FileWriter stream = new FileWriter(selectedFile); 
			out = new PrintWriter( stream );
		}
		catch (Exception e) {
			errorMessage("Sorry, but an error occurred while\ntrying to open the file:\n" + e);
			return;
		}
		try {
			out.print(transcript.getText());  // Write text from the TextArea to the file.
			out.close();
			if (out.checkError())   // (need to check for errors in PrintWriter)
				throw new IOException("Error check failed.");
		}
		catch (Exception e) {
			errorMessage("Sorry, but an error occurred while\ntrying to write the text:\n" + e);
		}	
	}


	/**
	 * Add a line of text to the transcript area.
	 * @param message text to be added; a line feed is added at the end
	 */
	private void postMessage(String message) {
		Platform.runLater( () -> transcript.appendText(message + '\n') );
	}


	/**
	 * Defines the thread that handles the connection.  The thread is responsible
	 * for opening the connection and for receiving messages.  This class contains
	 * several methods that are called by the main class, and that are therefore
	 * executed in a different thread.  Note that by using a thread to open the
	 * connection, any blocking of the graphical user interface is avoided.  By
	 * using a thread for reading messages sent from the other side, the messages
	 * can be received and posted to the transcript asynchronously at the same
	 * time as the user is typing and sending messages.  All changes to the GUI
	 * that are made by this class are done using Platform.runLater().
	 */
	private class ConnectionHandler extends Thread {

		private volatile ConnectionState state;
		private String remoteHost;
		private int port;
		private ServerSocket listener;
		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;

		/**
		 * Listen for a connection on a specified port.  The constructor
		 * does not perform any network operations; it just sets some
		 * instance variables and starts the thread.  Note that the
		 * thread will only listen for one connection, and then will
		 * close its server socket.
		 */
		ConnectionHandler(int port) {
			state = ConnectionState.LISTENING;
			this.port = port;
			postMessage("\nLISTENING ON PORT " + port + "\n");
			try { setDaemon(true); }
			catch (Exception e) {}
			start();
		}

		/**
		 * Open a connection to specified computer and port.  The constructor
		 * does not perform any network operations; it just sets some
		 * instance variables and starts the thread.
		 */
		ConnectionHandler(String remoteHost, int port) {
			state = ConnectionState.CONNECTING;
			this.remoteHost = remoteHost;
			this.port = port;
			postMessage("\nCONNECTING TO " + remoteHost + " ON PORT " + port + "\n");
			try { setDaemon(true); }
			catch (Exception e) {}
			start();
		}

		/**
		 * Returns the current state of the connection.  
		 */
		synchronized ConnectionState getConnectionState() {
			return state;
		}

		/**
		 * Send a message to the other side of the connection, and post the
		 * message to the transcript.  This should only be called when the
		 * connection state is ConnectionState.CONNECTED; if it is called at
		 * other times, it is ignored.  (Although it is unlikely, it is
		 * possible for this method to block, if the system's buffer for
		 * outgoing data fills.)
		 */
		synchronized void send(String message) {
			if (state == ConnectionState.CONNECTED) {
				postMessage("SEND:  " + message);
				out.println(message);
				out.flush();
				if (out.checkError()) {
					postMessage("\nERROR OCCURRED WHILE TRYING TO SEND DATA.");
					close();
				}
			}
		}

		/**
		 * Close the connection. If the server socket is non-null, the
		 * server socket is closed, which will cause its accept() method to
		 * fail with an error.  If the socket is non-null, then the socket
		 * is closed, which will cause its input method to fail with an
		 * error.  (However, these errors will not be reported to the user.)
		 */
		synchronized void close() {
			state = ConnectionState.CLOSED;
			try {
				if (socket != null)
					socket.close();
				else if (listener != null)
					listener.close();
			}
			catch (IOException e) {
			}
		}

		/**
		 * This is called by the run() method when a message is received from
		 * the other side of the connection.  The message is posted to the
		 * transcript, but only if the connection state is CONNECTED.  (This
		 * is because a message might be received after the user has clicked
		 * the "Disconnect" button; that message should not be seen by the
		 * user.)
		 */
		synchronized private void received(String message) {
			if (state == ConnectionState.CONNECTED)
				postMessage("RECEIVE:  " + message);
		}

		/**
		 * This is called by the run() method when the connection has been
		 * successfully opened.  It enables the correct buttons, writes a
		 * message to the transcript, and sets the connected state to CONNECTED.
		 */
		synchronized private void connectionOpened() throws IOException {
			listener = null;
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			state = ConnectionState.CONNECTED;
			Platform.runLater( () -> { 
				closeButton.setDisable(false);
				sendButton.setDisable(false);
				messageInput.setEditable(true);
				messageInput.setText("");
				messageInput.requestFocus();
				postMessage("CONNECTION ESTABLISHED\n");
			});
		}

		/**
		 * This is called by the run() method when the connection is closed
		 * from the other side.  (This is detected when an end-of-stream is
		 * encountered on the input stream.)  It posts a message to the
		 * transcript and sets the connection state to CLOSED.
		 */
		synchronized private void connectionClosedFromOtherSide() {
			if (state == ConnectionState.CONNECTED) {
				postMessage("\nCONNECTION CLOSED FROM OTHER SIDE\n");
				state = ConnectionState.CLOSED;
			}
		}

		/**
		 * Called from the finally clause of the run() method to clean up
		 * after the network connection closes for any reason.
		 */
		private void cleanUp() {
			state = ConnectionState.CLOSED;
			Platform.runLater( () -> {
				listenButton.setDisable(false);
				connectButton.setDisable(false);
				closeButton.setDisable(true);
				sendButton.setDisable(true);
				messageInput.setEditable(false);
				postMessage("\n*** CONNECTION CLOSED ***\n");
			});
			if (socket != null && !socket.isClosed()) {
				// Make sure that the socket, if any, is closed.
				try {
					socket.close();
				}
				catch (IOException e) {
				}
			}
			socket = null;
			in = null;
			out = null;
			listener = null;
		}


		/**
		 * The run() method that is executed by the thread.  It opens a
		 * connection as a client or as a server (depending on which 
		 * constructor was used).
		 */
		public void run() {
			try {
				if (state == ConnectionState.LISTENING) {
						// Open a connection as a server.
					listener = new ServerSocket(port);
					socket = listener.accept();
					listener.close();
				}
				else if (state == ConnectionState.CONNECTING) {
						// Open a connection as a client.
					socket = new Socket(remoteHost,port);
				}
				connectionOpened();  // Set up to use the connection.
				while (state == ConnectionState.CONNECTED) {
						// Read one line of text from the other side of
						// the connection, and report it to the user.
					String input = in.readLine();
					if (input == null)
						connectionClosedFromOtherSide();
					else
						received(input);  // Report message to user.
				}
			}
			catch (Exception e) {
					// An error occurred.  Report it to the user, but not
					// if the connection has been closed (since the error
					// might be the expected error that is generated when
					// a socket is closed).
				if (state != ConnectionState.CLOSED)
					postMessage("\n\n ERROR:  " + e);
			}
			finally {  // Clean up before terminating the thread.
				cleanUp();
			}
		}

	} // end nested class ConnectionHandler

}
