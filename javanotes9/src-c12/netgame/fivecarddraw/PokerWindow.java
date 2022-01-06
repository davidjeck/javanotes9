package netgame.fivecarddraw;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import netgame.common.*;

import java.io.IOException;
import java.util.Optional;


/**
 * A window for one player in a two-player networked game of
 * Five Card Draw Poker.  The window is created by the main 
 * program, netgame.fivecarddraw.Main.
 * <p>When a PokerWindow is opened, it establishes a
 * a connection to a PokerHub at a specified host and port.
 * Once two players have connected to the hub, the hub will
 * manage a game of poker between the two players.  Each
 * player is given $1000 to start.  One player acts a 
 * "dealer", and the dealer clicks a "DEAL" button to start
 * the game.  (The role of dealer alternates between players.)
 * After the cards are dealt, there is a first round of
 * betting.  The non-dealer player bets first.  When either
 * player matches the other's bet, the betting round ends and
 * each player has a chance to discard some cards and draw
 * new ones.  Then there is a second round of betting.  When
 * that round ends, the players' hands are compared, and the
 * winner is announced.  (Players also have the choice of 
 * folding, instead of betting, which will end the game
 * immediately.)  Note that the player's amount of money
 * can become negative, and nothing is done when that
 * happens.  The game continues until one or the other
 * player quits.  The other player is notified so that
 * the second player's program can also be terminated.
 * <p>For details of the communication protocol that
 * is used between the PokerWindows and the PokerHub,
 * see the comments on the PokerHub class.
 */
public class PokerWindow extends Stage {
	
	
	private PokerClient connection;   // Handles communication with the PokerHub; used to send messages to the hub.

	private PokerGameState state;     // Represents the state of the game, as seen by this player.  The state is
	                                  //   received as a message from the hub whenever the state changes.  This
	                                  //   variable changes only in the newState() method.
	
	private boolean[] discard;        // When the player is discarding cards, this array tells which cards the
	                                  //   player wants to discard.  discard[i] is true if the player is discarding 
	                                  //   the i-th card in the hand.
	
	private PokerCard[] opponentHand; // The opponent's hand.  This variable is null during the playing of a
	                                  //   hand.  It becomes non-null if the opponent's hand is sent to this
	                                  //   player at the end of one hand of poker.
	
	private Canvas canvas;          // The canvas where the game is displayed, defined by the inner class, Display.

	private Image cardImages;         // An image holding pictures of all the cards.  The Image is loaded
	                                  // as a resource by the PokerWindow constructor from a resource file
	                                  // "netgame/fivecarddraw/cards.png."   (The program will be non-functional
	                                  // if that resource file is not there.)
	
	
	private Button dealButton;   // User interface components shown along the right side of the window.
	private Button drawButton;
	private Button betButton;
	private Button callButton;
	private Button passButton;
	private Button foldButton;
	private Button quitButton;	
	private TextField betInput;
	
	private String message = "";                     // text that is displayed on the canvas
	private String messageFromServer = "";
	private String money = "", opponentsMoney = "", pot = "";
	
	
	/**
	 * The constructor sets up the window and makes it visible on the screen.  
	 * It starts a thread that will open a connection to a PokerHub.
	 * The window will become operational when the game starts, or it will be closed
	 * and the program terminated if the connection attempt fails.
	 * @param hubHostName the host name or IP address where the PokerHub is listening.
	 * @param hubPort the port number where the PokerHub is listening.
	 */
	public PokerWindow(final String hubHostName, final int hubPort) {

		cardImages = new Image("netgame/fivecarddraw/cards.png");
		messageFromServer  = "WAITING FOR CONNECTION";
		
		canvas = new Canvas(550,575);
		drawBoard();
		canvas.setOnMousePressed( evt -> doClick(evt.getX(),evt.getY()) );
		
		betInput = new TextField();
		betInput.setEditable(false);
		betInput.setPrefColumnCount(5);
		VBox.setMargin(betInput,new Insets(0,10,0,15));

		VBox controls = new VBox();
		EventHandler<ActionEvent> listener = this::doAction;
		dealButton = makeButton("DEAL",listener,controls);
		drawButton = makeButton("DRAW",listener,controls);
		betButton = makeButton("BET:",listener,controls);
		controls.getChildren().add(betInput);
		passButton = makeButton("PASS",listener,controls);
		callButton = makeButton("CALL",listener,controls);
		foldButton = makeButton("FOLD",listener,controls);
		quitButton = makeButton("QUIT",listener,controls);
		quitButton.setDisable(false);
		
		BorderPane root = new BorderPane(canvas);
		root.setRight(controls);
		
		setScene(new Scene(root));
		setOnHiding( e -> doQuit() );
		setTitle("NetPoker");
		setResizable(false);
		setX(200);
		setY(100);
		show();
		
		new Thread( () -> connect(hubHostName,hubPort) ).start();
		
	} // end start()
	
	
	/**
	 * Utility routine used by the constructor to make a button, add it
	 * to the VBox, and add a listener for ActionEvents. All buttons will
	 * have the same size, 95x40.
	 */
	private Button makeButton(String text, EventHandler<ActionEvent> listener, VBox box) {
		Button button = new Button(text);
		button.setDisable(true);
		button.setPrefSize(95,40);
		button.setFont(Font.font(null, FontWeight.BOLD, 15));
		button.setOnAction(listener);
		box.getChildren().add(button);
		VBox.setMargin(button, new Insets(30,10,0,10));
		return button;
	}
	
	
	/**
	 * When the window is created, this method is called in a separate
	 * thread to make the connection to the server.  If an error
	 * occurs, the program is terminated.
	 */
	private void connect(String hostName, int serverPortNumber) {
		PokerClient c;
		try {
			c = new PokerClient(hostName, serverPortNumber);
			int id = c.getID();
			Platform.runLater( () -> {
				connection = c;
				if (id == 1) {
					   // This is Player #1.  Still have to wait for second player to
					   // connect.  Change the message display to reflect that fact.
					messageFromServer  = "Waiting for an opponent to connect...";
					drawBoard();
				}
			});
		}
		catch (Exception e) {
			Platform.runLater( () -> {
				Alert alert = new Alert(Alert.AlertType.INFORMATION, 
						"Sorry, could not connect to\n"
						+ hostName + " on port " + serverPortNumber 
						+ "\nShutting down.");
				alert.showAndWait();
				System.exit(0);
			});
		}
	}
		

	
	
	// ---------------------- Private inner classes -----------------------------------
	
	
	/**
	 * A PokerClient is a netgame client that handles communication
	 * with the PokerHub.  It is used by the PokerWindow class to
	 * send messages to the hub.  When messages are received from
	 * the hub, it takes an appropriate action.
	 */
	private class PokerClient extends Client {

		/**
		 * Connect to a PokerHub at a specified hostname and port number.
		 */
		public PokerClient(String hubHostName, int hubPort) throws IOException {
			super(hubHostName, hubPort);
		}

		/**
		 * This method is called when a message from the hub is received 
		 * by this client.  If the message is of type PokerGameState,
		 * then the newState() method in the PokerWindow class is called
		 * to handle the change in the state of the game.  If the message
		 * is of type String, it represents a message that is to be
		 * displayed to the user; the string is displayed as
		 * messageFromServer.  If the message is of type PokerCard[],
		 * then it is the opponent's hand.  That hand is sent when the
		 * game has ended and the player gets to see the opponent's hand.
		 * <p>Note that this method is called from a separate thread, not
		 * from the GUI event thread.  In order to avoid synchronization
		 * issues, this method uses Platform.runLater() to carry 
		 * out its task in the GUI event thread.
		 */
		protected void messageReceived(final Object message) {
			Platform.runLater( () -> {
				if (message instanceof PokerGameState)
					newState( (PokerGameState)message );
				else if (message instanceof String) {
					messageFromServer  =  (String)message ;
					drawBoard();
				}
				else if (message instanceof PokerCard[]) {
					opponentHand = (PokerCard[])message;
				    drawBoard();
				}
			});
		}

		/**
		 * This method is called when the hub shuts down.  That is a signal
		 * that the opposing player has quit the game.  The user is informed
		 * of this, and the program is terminated.
		 */
		protected void serverShutdown(String message) {
			Platform.runLater( () -> {
				showMessage("Your opponent has quit.\nThe game is over.");
				System.exit(0);
			});
		}
		
	} // end nested class PokerClient
	
	private static Font font16 = Font.font(16);  //fonts for use in drawBoard
	private static Font font24 = Font.font(24); 
	private static Font font38 = Font.font(38); 

	private void drawBoard() {
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.setFill(Color.BEIGE);
		g.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
		g.setStroke(Color.DARKRED);
		g.setLineWidth(8);
		g.strokeRect(0,0,canvas.getWidth(),canvas.getHeight());
		
		g.setFill(Color.GREEN);
		g.setFont(font24);
		g.fillText(opponentsMoney,100,40);
		g.fillText(money,100,550);
		g.setFont(font38);
		g.fillText(pot,150,300);
		g.setFill(Color.DARKRED);
		g.setFont(font16);
		g.fillText(message,30,355);
		g.fillText(messageFromServer,30,230);
		
		if (state == null) {
			   // Still waiting for connections.  Don't draw anything.
			return;
		}
		
		if (state.hand == null) {
			   // This happens only while waiting for the first hand to be dealt.
			   // Draw outlines of the card locations for this player's hand.
			g.setStroke(Color.DARKRED);
			g.setLineWidth(2);
			for (int x = 25; x < 500; x += 105)
				g.strokeRect(x,380,80,124);
		}
		else {
			   // Draw the cards in this player's hand.
			for (int i = 0; i < 5; i++) {
				if (discard != null && discard[i])
					drawCard(g,null,25+i*105,380);
				else
					drawCard(g,state.hand[i],25+i*105,380);
			}
		}
		if (state.hand == null) {
			   // This happens only while waiting for the first hand to be dealt.
			   // Draw outlines of the card locations for the opponent's hand.
			g.setStroke(Color.DARKRED);
			g.setLineWidth(2);
			for (int x = 25; x < 500; x += 105)
				g.strokeRect(x,70,80,124);
		}
		else if (opponentHand == null) {
			   // The opponent's hand exists but is unknown.  Draw it as face-down cards.
			for (int i = 0; i < 5; i++)
				drawCard(g,null,25+i*105,70);
		}
		else {
			   // The opponent's hand is known.  Draw the cards.
			for (int i = 0; i < 5; i++)
				drawCard(g,opponentHand[i],25+i*105,70);
		}
	}	

	
	private void showMessage(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
		alert.showAndWait();
	}
	

	
	/**
	 * A class to define the action event handler that responds when the user clicks
	 * a button.  The button that was clicked is given by evt.getSource().  Note that 
	 * once an action is taken, the buttons that were enabled are disabled,
	 * to prevent the user from generating extra messages while the hub is
	 * processing the user's action.
	 */
		public void doAction(ActionEvent evt) {
			Object src = evt.getSource();
			if (src == quitButton) {  // end the program
				doQuit();
			}
			else if (src == dealButton) { 
				    // send "deal" as a message to the hub, which will start the next hand of the game
				dealButton.setDisable(true);
				connection.send("deal");
			}
			else if (src == foldButton) { 
				   // send "fold" as a message to the hub, which will end the game because this user folded
				foldButton.setDisable(true);
				betButton.setDisable(true);
				passButton.setDisable(true);
				callButton.setDisable(true);
				betInput.setEditable(false);
				betInput.setText("");
				connection.send("fold");
			}
			else if (src == passButton) { 
				   // send the integer 0 as a message, indicating that the user places no bet;
				   // this is only possible for the first bet in a betting round
				foldButton.setDisable(true);
				betButton.setDisable(true);
				passButton.setDisable(true);
				callButton.setDisable(true);
				betInput.setEditable(false);
				betInput.setText("");
				connection.send(Integer.valueOf(0));
			}
			else if (src == callButton) { 
				   // send an integer equal to the minimum possible bet as a message to the hub;
				   // this means "see" in the first betting round and "call" in the second, and
				   // it will end the current betting round.
				foldButton.setDisable(true);
				betButton.setDisable(true);
				passButton.setDisable(true);
				callButton.setDisable(true);
				betInput.setEditable(false);
				betInput.setText("");
				connection.send(Integer.valueOf(state.amountToSee));
			}
			else if (src == drawButton) {
				   // Send the list of cards that the user wants to discard as a message to
				   // the hub.  The cards are recorded in the discard array.
				int ct = 0;
				for (int i = 0; i < 5; i++) {  // Count the number of discarded cards.
					if (discard[i])
						ct++;
				}
				if (ct == 0) {
					Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
							"Are you sure you want to draw NO cards?\n"
							   +"If not, click 'No', and select\n" 
							   + "the cards that you want to discard.",
							   ButtonType.NO, ButtonType.YES);
					Optional<ButtonType> resp = alert.showAndWait();
					if (! resp.isPresent() || resp.get() == ButtonType.NO)
						return;
				}
				int[] cardNums = new int[ct];
				int j = 0;
				for (int i = 0; i < 5; i++) {  // Put indices of discarded cards into an array to be sent to the hub. 
					if (discard[i])
						cardNums[j++] = i;
				}
				discard = null;  // We are finished with the discard array.
				drawButton.setDisable(true);
				connection.send(cardNums);
			}
			else if (src == betButton) {
				   // User wants to place a bet.  Check that the bet is legal.  If it is,
				   // send the bet amount as a message to the hub.
				int amount;
				try {
					amount = Integer.parseInt(betInput.getText().trim());
					if (amount <= 0)
						throw new NumberFormatException();
				}
				catch (NumberFormatException e) {
					showMessage("The bet amount must be\na legal positive integer.");
					betInput.selectAll();
					betInput.requestFocus();
					return;
				}
				if ( (state.status == PokerGameState.RAISE_SEE_OR_FOLD_ROUND_1 ||
						state.status == PokerGameState.RAISE_CALL_OR_FOLD_ROUND_2)
						   && amount < state.amountToSee) {
					showMessage("Your bet must be at least " + state.amountToSee
					           + "\n to match your opponent's bet.");
					betInput.selectAll();
					betInput.requestFocus();
					return;
				}
				betInput.setEditable(false);
				betInput.setText("");
				connection.send(Integer.valueOf(amount));
			}
		}
	
	
	/**
	 * This method is called when a new PokerGameState is received from the PokerHub.
	 * It changes the GUI and the window's state to match the new game state.  The
	 * new state is also stored in the instance variable named state.
	 */
	private void newState(PokerGameState state) {
		
		this.state = state;
		
		// Set the enabled status of the buttons to enable actions that are appropriate in the current state.
		
		dealButton.setDisable(state.status != PokerGameState.DEAL);
		drawButton.setDisable(state.status != PokerGameState.DRAW);
		betButton.setDisable(state.status != PokerGameState.BET_OR_FOLD
				        && state.status != PokerGameState.RAISE_SEE_OR_FOLD_ROUND_1
				        && state.status != PokerGameState.RAISE_CALL_OR_FOLD_ROUND_2);
		foldButton.setDisable(state.status != PokerGameState.BET_OR_FOLD
		                && state.status != PokerGameState.RAISE_SEE_OR_FOLD_ROUND_1
				        && state.status != PokerGameState.RAISE_CALL_OR_FOLD_ROUND_2);
		passButton.setDisable(state.status != PokerGameState.BET_OR_FOLD);
		callButton.setDisable(state.status != PokerGameState.RAISE_SEE_OR_FOLD_ROUND_1
		                && state.status != PokerGameState.RAISE_CALL_OR_FOLD_ROUND_2);
		
		// Set the name of callButton to "CALL" during the second round of
		// betting and to "SEE" at other times.
		
		if (state.status == PokerGameState.RAISE_CALL_OR_FOLD_ROUND_2)
			callButton.setText("CALL");
		else
			callButton.setText("SEE");
		
		// When it's time for this player to make a bet, enable the betInput text field,
		// set its content to be the minimum possible bet plus $10, and select the
		// text input so that the user can simply type the bet.  The
		// betInput is not editable except when it's time for the user to place a bet.
		// Once the user places the bet (or sees, calls, passes, or folds), the
		// betInput is once again made empty and uneditable.
		
		if (state.status == PokerGameState.RAISE_SEE_OR_FOLD_ROUND_1 || 
				state.status == PokerGameState.RAISE_CALL_OR_FOLD_ROUND_2 ||
				state.status == PokerGameState.BET_OR_FOLD) {
			if ( ! betInput.isEditable() ) {
				int suggestedBet;
				if ( state.status == PokerGameState.BET_OR_FOLD)
					suggestedBet = 10;
				else
					suggestedBet = state.amountToSee + 10;
				betInput.setText("" + suggestedBet);
				betInput.setEditable(true);
				betInput.selectAll();
				betInput.requestFocus();
			}
		}
		
		// Show the current amounts of the user's money, the opponent's money, and the pot.
		
		money  = "You have $ " + state.money;
		opponentsMoney  = "Your opponent has $ " + state.opponentMoney;
		if (state.status != PokerGameState.DEAL && state.status != PokerGameState.WAIT_FOR_DEAL)
			opponentHand = null;
		pot  = "Pot:  $ " + state.pot;
		
		// If it's time for the user to select cards to be discarded, create the
		// discard array that will record which cards have been selected to be discarded.
		
		if (state.status == PokerGameState.DRAW && discard == null) {
			discard = new boolean[5];
		}
		
		// Set the message to show instructions to the user that are appropriate for the state.
		
		switch (state.status) {
		case PokerGameState.DEAL:
			message  = "Click the DEAL button to start the game.";
			break;
		case PokerGameState.DRAW:
			message  = "Click the cards you want to discard, then click DRAW.";
			break;
		case PokerGameState.BET_OR_FOLD:
			message  = "Place your BET, PASS, or FOLD.";
			break;
		case PokerGameState.RAISE_SEE_OR_FOLD_ROUND_1:
			message  = "Place your BET, SEE, or FOLD.";
			break;
		case PokerGameState.RAISE_CALL_OR_FOLD_ROUND_2:
			message  = "Place your BET, CALL, or FOLD.";
			break;
		case PokerGameState.WAIT_FOR_BET:
			message  = "Waiting for opponent to bet.";
			break;
		case PokerGameState.WAIT_FOR_DEAL:
			message  = "Waiting for opponent to deal.";
			break;
		case PokerGameState.WAIT_FOR_DRAW:
			message  = "Waiting for opponent to draw.";
			break;
		}
		
		drawBoard();

	} // end newState()
	
		
	
	/**
	 * This method is called when the user clicks the display at the
	 * point (x,y).  Clicks are ignored except when the user is 
	 * selecting cards to discard (that is when state.status is
	 * PokerGameState.DRAW).  While discarding, if the user clicks
	 * a card, that card is turned over.
	 */
	private void doClick(double x, double y) {
		if (state == null || state.status != PokerGameState.DRAW)
			return;
		for (int i = 0; i < 5; i++) {
			if (y > 380 && y < 503 && x > 25+i*105 && x < 104+i*105) {
				   // Clicked on card number i.  Toggle the value in
				   // discard[i].  If the card is face up, it will be
				   // selected for discarding and will now appear face down.
				   // If the card is already selected for discarding, 
				   // it will be de-selected.
				discard[i] = !discard[i];
				drawBoard();
				break;
			}
		}
	}
	
	
	/**
	 * This method is called when the user clicks the "QUIT"
	 * button or closed the window.  The client disconnects
	 * from the server before terminating the program.  
	 * This will be seen by the Hub, which will inform the 
	 * other player's program (if any), so that that program 
	 * can also terminate.
	 */
	private void doQuit() {
		hide(); // Close the window.
		if (connection != null) {
			connection.disconnect();
			try { // Time for the disconnect message to be sent.
				Thread.sleep(500);
			}
			catch (InterruptedException e) {
			}
		}
		System.exit(0);
	}
	

	/**
	 * Draws a card in a 79x123 pixel rectangle with its
	 * upper left corner at a specified point (x,y).  Drawing the card 
	 * requires the resource file "netgame/fivecarddraw/cards.png".
	 * @param g The non-null graphics context used for drawing the card.
	 * @param card The card that is to be drawn.  If the value is null, then a
	 *     face-down card is drawn.
	 * @param x the x-coord of the upper left corner of the card
	 * @param y the y-coord of the upper left corner of the card
	 */
	public void drawCard(GraphicsContext g, PokerCard card, int x, int y) {
		int cx;    // x-coord of upper left corner of the card inside cardsImage
		int cy;    // y-coord of upper left corner of the card inside cardsImage
		if (card == null) {
			cy = 4*123;   // coords for a face-down card.
			cx = 2*79;
		}
		else {
			if (card.getValue() == PokerCard.ACE)
				cx = 0;
			else
				cx = (card.getValue()-1)*79;
			switch (card.getSuit()) {
			case PokerCard.CLUBS:    
				cy = 0; 
				break;
			case PokerCard.DIAMONDS: 
				cy = 123; 
				break;
			case PokerCard.HEARTS:   
				cy = 2*123; 
				break;
			default:  // spades   
				cy = 3*123; 
				break;
			}
		}
		g.drawImage( cardImages, cx,cy,79,123, x,y,79,123 );
	}
	
	
} // end class PokerWindow

