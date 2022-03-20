
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class implements a simple card game.  The user sees a card 
 * and tries to predict whether the next card will be higher or 
 * lower.  Aces are the lowest-valued cards.  If the user makes
 * three correct predictions, the user wins.  If not, the
 * user loses.
 * 
 * A main() routine allows this class to be run as a program.
 * 
 * This class depends on several additional source code files:
 * Card.java, Hand.java, and Deck.java.
 */
public class HighLowGUI extends JPanel {


	/**
	 * A main routine allows this class to be run as an application.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("High/Low Card Game");
		HighLowGUI content = new HighLowGUI();
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(120,70);
		window.pack();
		window.setResizable(false);
		window.setVisible(true);
	}

	//---------------------------------------------------------------------

	/**
	 * The constructor lays out the panel.  A CardPanel occupies the CENTER 
	 * position of the panel (where CardPanel is a subclass of JPanel that is 
	 * defined below).  On the bottom is a panel that holds three buttons.
	 * Methods in the CardPanel are called in response to button clicks.
	 * Event handling for the ActionEvents from the buttons is set up using
	 * lambda expressions to define the ActionListeners.
	 */
	public HighLowGUI() {

		setBackground( new Color(130,50,40) );

		setLayout( new BorderLayout(3,3) );

		CardPanel board = new CardPanel();
		add(board, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground( new Color(220,200,180) );
		add(buttonPanel, BorderLayout.SOUTH);

		JButton higher = new JButton( "Higher" );
		higher.addActionListener( evt -> board.doHigher() );
		buttonPanel.add(higher);

		JButton lower = new JButton( "Lower" );
		lower.addActionListener( evt -> board.doLower() );
		buttonPanel.add(lower);

		JButton newGame = new JButton( "New Game" );
		newGame.addActionListener( evt -> board.doNewGame() );
		buttonPanel.add(newGame);

		setBorder(BorderFactory.createLineBorder( new Color(130,50,40), 3) );

	}  // end constructor



	/**
	 * A nested class that displays the cards and does all the work
	 * of keeping track of the state and responding to user events.
	 */
	private class CardPanel extends JPanel {

		Deck deck;       // A deck of cards to be used in the game.
		Hand hand;       // The cards that have been dealt.
		String message;  // A message drawn on the canvas, which changes
		                 //    to reflect the state of the game.

		boolean gameInProgress;  // Set to true when a game begins and to false
		                         //   when the game ends.

		Font bigFont;      // Font that will be used to display the message.
		Font smallFont;    // Font that will be used to draw the cards.

		/**
		 * Constructor creates fonts, sets the foreground and background
		 * colors and starts the first game.  It also sets a "preferred
		 * size" for the panel.  This size is respected when the program
		 * is run as an application, since the pack() method is used to
		 * set the size of the window.
		 */
		CardPanel() {
			setBackground( new Color(0,120,0) );
			setForeground( Color.GREEN );
			smallFont = new Font("SansSerif", Font.PLAIN, 12);
			bigFont = new Font("Serif", Font.BOLD, 14);
			setPreferredSize( new Dimension(370, 150));
			doNewGame();
		} // end constructor


		/**
		 * Called when the user clicks the "Higher" button.
		 * Check the user's prediction.  Game ends if user guessed
		 * wrong or if the user has made three correct predictions.
		 */
		void doHigher() {
			if (gameInProgress == false) {
					// If the game has ended, it was an error to click "Higher",
					// So set up an error message and abort processing.
				message = "Click \"New Game\" to start a new game!";
				repaint();
				return;
			}
			hand.addCard( deck.dealCard() );     // Deal a card to the hand.
			int cardCt = hand.getCardCount();
			Card thisCard = hand.getCard( cardCt - 1 );  // Card just dealt.
			Card prevCard = hand.getCard( cardCt - 2 );  // The previous card.
			if ( thisCard.getValue() < prevCard.getValue() ) {
				gameInProgress = false;
				message = "Too bad! You lose.";
			}
			else if ( thisCard.getValue() == prevCard.getValue() ) {
				gameInProgress = false;
				message = "Too bad!  You lose on ties.";
			}
			else if ( cardCt == 4) {
				gameInProgress = false;
				message = "You win!  You made three correct guesses.";
			}
			else {
				message = "Got it right!  Try for " + cardCt + ".";
			}
			repaint();
		} // end doHigher()
		

		/**
		 * Called when the user clicks the "Lower" button.
		 * Check the user's prediction.  Game ends if user guessed
		 * wrong or if the user has made three correct predictions.
		 */
		void doLower() {
			if (gameInProgress == false) {
					// If the game has ended, it was an error to click "Lower",
					// So set up an error message and abort processing.
				message = "Click \"New Game\" to start a new game!";
				repaint();
				return;
			}
			hand.addCard( deck.dealCard() );     // Deal a card to the hand.
			int cardCt = hand.getCardCount();
			Card thisCard = hand.getCard( cardCt - 1 );  // Card just dealt.
			Card prevCard = hand.getCard( cardCt - 2 );  // The previous card.
			if ( thisCard.getValue() > prevCard.getValue() ) {
				gameInProgress = false;
				message = "Too bad! You lose.";
			}
			else if ( thisCard.getValue() == prevCard.getValue() ) {
				gameInProgress = false;
				message = "Too bad!  You lose on ties.";
			}
			else if ( cardCt == 4) {
				gameInProgress = false;
				message = "You win!  You made three correct guesses.";
			}
			else {
				message = "Got it right!  Try for " + cardCt + ".";
			}
			repaint();
		} // end doLower()
		

		/**
		 * Called by the constructor, and called when
		 * the user clicks the "New Game" button.  Starts a new game.
		 */
		void doNewGame() {
			if (gameInProgress) {
					// If the current game is not over, it is an error to try
					// to start a new game.
				message = "You still have to finish this game!";
				repaint();
				return;
			}
			deck = new Deck();   // Create the deck and hand to use for this game.
			hand = new Hand();
			deck.shuffle();
			hand.addCard( deck.dealCard() );  // Deal the first card into the hand.
			message = "Is the next card higher or lower?";
			gameInProgress = true;
			repaint();
		} // end doNewGame()

		
		/**
		 * This method draws the message at the bottom of the
		 * panel, and it draws all of the dealt cards spread out
		 * across the canvas.  If the game is in progress, an extra
		 * card is drawn face down representing the card to be dealt next.
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setFont(bigFont);
			g.drawString(message,10,135);
			g.setFont(smallFont);
			int cardCt = hand.getCardCount();
			for (int i = 0; i < cardCt; i++)
				drawCard(g, hand.getCard(i), 10 + i * 90, 10);
			if (gameInProgress)
				drawCard(g, null, 10 + cardCt * 90, 10);
		} // end paintComponent()

		
		
		/**
		 * Draws a card as a 80 by 100 rectangle with upper left corner at (x,y).
		 * The card is drawn in the graphics context g.  If card is null, then
		 * a face-down card is drawn.  (The cards are rather primitive!)
		 */
		void drawCard(Graphics g, Card card, int x, int y) {
			if (card == null) {  
				// Draw a face-down card
				g.setColor(Color.BLUE);
				g.fillRect(x,y,80,100);
				g.setColor(Color.WHITE);
				g.drawRect(x+3,y+3,73,93);
				g.drawRect(x+4,y+4,71,91);
			}
			else {
				g.setColor(Color.WHITE);
				g.fillRect(x,y,80,100);
				g.setColor(Color.GRAY);
				g.drawRect(x,y,79,99);
				g.drawRect(x+1,y+1,77,97);
				if (card.getSuit() == Card.DIAMONDS || card.getSuit() == Card.HEARTS)
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLACK);
				g.drawString(card.getValueAsString(), x + 10, y + 30);
				g.drawString("of", x+ 10, y + 50);
				g.drawString(card.getSuitAsString(), x + 10, y + 70);
			}
		} // end drawCard()


	} // end nested class CardPanel


} // end class HighLowGUI
