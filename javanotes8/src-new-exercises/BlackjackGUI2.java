import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * In this program, the user plays a game of Blackjack.  The
 * computer acts as the dealer.  The user plays by clicking
 * "Hit!" and "Stand!" buttons.  The user can place bets.
 * At the beginning of the game, the user is given $100.
 * 
 * This class defines a panel, but it also contains a main()
 * routine that makes it possible to run the program as a
 * stand-alone application. 
 *
 * This program depends on the following classes:  Card, Hand,
 * BlackjackHand, Deck.
 */
public class BlackjackGUI2 extends JPanel {
   
   /**
    * The main routine simply opens a window that shows a BlackjackGUI2.
    */
   public static void main(String[] args) {
      JFrame window = new JFrame("Blackjack");
      BlackjackGUI2 content = new BlackjackGUI2();
      window.setContentPane(content);
      window.pack();  // Set size of window to preferred size of its contents.
      window.setResizable(false);  // User can't change the window's size.
      Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
      window.setLocation( (screensize.width - window.getWidth())/2, 
            (screensize.height - window.getHeight())/2 );
      window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      window.setVisible(true);
   }
      

   private JButton hitButton;     // The three buttons that control the game.
   private JButton standButton;
   private JButton newGameButton;
   private JTextField betInput;  // Where the user inputs the amount of his bet.
   
   
   /**
    * The constructor lays out the panel.  A CardPanel occupies the CENTER 
    * position of the panel (where CardPanel is a subclass of JPanel that is 
    * defined below).  On the bottom is a panel that holds three buttons.  
    * The CardPanel listens for ActionEvents from the buttons and does all 
    * the real work of the program.
    */
   public BlackjackGUI2() {
      
      setBackground( new Color(130,50,40) );
      
      setLayout( new BorderLayout(3,3) );
      
      CardPanel board = new CardPanel();
      add(board, BorderLayout.CENTER);
      
      JPanel buttonPanel = new JPanel();
      buttonPanel.setBackground( new Color(220,200,180) );
      add(buttonPanel, BorderLayout.SOUTH);
      
      // NOTE: Declarations of hitButton, standButton, newGameButton were moved
      // out of the constructor.  Previously, they were local variables.
   
      hitButton = new JButton( "Hit!" );
      hitButton.setEnabled(false);
      hitButton.addActionListener(board);
      buttonPanel.add(hitButton);
      
      standButton = new JButton( "Stand!" );
      standButton.setEnabled(false);
      standButton.addActionListener(board);
      buttonPanel.add(standButton);
      
      newGameButton = new JButton( "New Game" );
      newGameButton.addActionListener(board);
      buttonPanel.add(newGameButton);
      
      buttonPanel.add(new JLabel("  Bet:", JLabel.RIGHT));
      
      betInput = new JTextField("10", 5);
      betInput.setMargin( new Insets(3,3,3,3) );
      buttonPanel.add(betInput);
      
      setBorder(BorderFactory.createLineBorder( new Color(130,50,40), 3) );
      
   }  // end constructor
   
   
   
   /**
    * A nested class that displays the game and does all the work
    * of keeping track of the state and responding to user events.
    */
   private class CardPanel extends JPanel implements ActionListener {
      
      Deck deck;         // A deck of cards to be used in the game.
      
      BlackjackHand dealerHand;   // Hand containing the dealer's cards.
      BlackjackHand playerHand;   // Hand containing the user's cards.
      
      String message;  // A message drawn on the canvas, which changes
                       //    to reflect the state of the game.
      
      boolean gameInProgress;  // Set to true when a game begins and to false
                               //   when the game ends.
      
      Font bigFont;      // Font that will be used to display the message.
      Font smallFont;    // Font that will be used to draw the cards.

      int usersMoney;    // The amount of money that the user currently has.
      int betAmount;     // The bet amount, read from betInput when game starts.

      
      /**
       * The constructor creates the fonts and starts the first game.
       * It also sets a preferred size of 460-by-330 for the panel.
       * The paintComponent() method assumes that this is in fact the
       * size of the panel (although it can be a little taller with
       * no bad effect).
       */
      CardPanel() {
         setPreferredSize( new Dimension(460,330) );
         setBackground( new Color(0,120,0) );
         smallFont = new Font("SansSerif", Font.PLAIN, 12);
         bigFont = new Font("Serif", Font.BOLD, 14);
         usersMoney = 100;
         message = "Make your bet and hit \"New Game\".";
      }
      
      
      /**
       * This method is called whenever the value of the gameInProgress
       * property has to be changed.  In addition to setting the value
       * of the gameInProgress variable, it also enables and disables
       * the buttons and text input box to reflect the state of the
       * game.
       * @param inProgress The new value of gameInProgress.
       */
      private void setGameInProgress( boolean inProgress ) {
         gameInProgress = inProgress;
         if (gameInProgress) {
            hitButton.setEnabled(true);
            standButton.setEnabled(true);
            newGameButton.setEnabled(false);
            betInput.setEditable(false);
         }
         else {
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            newGameButton.setEnabled(true);
            betInput.setEditable(true);
         }
      }
      
      
      /**
       * This is called when the user wants to start a new game.  It tries to
       * read the amount of the user's bet from the betInput text field.  If an error
       * occurs, the message in the panel is changed to inform the user of the error.
       * @return true if the bet is read without error, or false if an error occurs
       */
      private boolean checkBet() {
         int amount;
         try {
            amount = Integer.parseInt( betInput.getText() );
         }
         catch (NumberFormatException e) {
            message = "The bet amount must be a legal positive integer.";
            repaint();
            return false;
         }
         if (amount <= 0) {
            message = "The bet amount must be a positive integer.";
            repaint();
            return false;
         }
         if (amount > usersMoney) {
            message = "You can't bet more money than you have!";
            repaint();
            return false;
         }
         betAmount = amount;
         return true;
      }
   
      
      /**
       * Respond when the user clicks on a button by calling the appropriate 
       * method.  Note that the buttons are created and listening is set
       * up in the constructor of the BlackjackPanel class.
       */
      public void actionPerformed(ActionEvent evt) {
         String command = evt.getActionCommand();
         if (command.equals("Hit!"))
            doHit();
         else if (command.equals("Stand!"))
            doStand();
         else if (command.equals("New Game"))
            doNewGame();
      }
      
      
      /**
       * This method is called when the user clicks the "Hit!" button.  First 
       * check that a game is actually in progress.  If not, give  an error 
       * message and exit.  Otherwise, give the user a card.  The game can end 
       * at this point if the user goes over 21 or if the user has taken 5 cards 
       * without going over 21.
       */
      void doHit() {
         if (gameInProgress == false) {  // Should not be possible!
            message = "Click \"New Game\" to start a new game.";
            repaint();
            return;
         }
         playerHand.addCard( deck.dealCard() );
         if ( playerHand.getBlackjackValue() > 21 ) {
            usersMoney = usersMoney - betAmount;
            message = "You've busted!  Sorry, you lose.";
            setGameInProgress(false);
         }
         else if (playerHand.getCardCount() == 5) {
            usersMoney = usersMoney + betAmount;
            message = "You win by taking 5 cards without going over 21.";
            setGameInProgress(false);
         }
         else {
            message = "You have " + playerHand.getBlackjackValue() + ".  Hit or Stand?";
         }
         repaint();
      }
      
      
      /**
       * This method is called when the user clicks the "Stand!" button.
       * Check whether a game is actually in progress.  If it is, the game 
       * ends.  The dealer takes cards until either the dealer has 5 cards 
       * or more than 16 points.  Then the  winner of the game is determined. 
       */
      void doStand() {
         if (gameInProgress == false) {  // Should not be possible!
            message = "Click \"New Game\" to start a new game.";
            repaint();
            return;
         }
         setGameInProgress(false);
         while (dealerHand.getBlackjackValue() <= 16 && dealerHand.getCardCount() < 5)
            dealerHand.addCard( deck.dealCard() );
         if (dealerHand.getBlackjackValue() > 21) {
            usersMoney = usersMoney + betAmount;  
            message = "You win!  Dealer has busted with " + dealerHand.getBlackjackValue() + ".";
         }
         else if (dealerHand.getCardCount() == 5) {
            usersMoney = usersMoney - betAmount; 
            message = "Sorry, you lose.  Dealer took 5 cards without going over 21.";
         }
         else if (dealerHand.getBlackjackValue() > playerHand.getBlackjackValue()) {
            usersMoney = usersMoney - betAmount;
            message = "Sorry, you lose, " + dealerHand.getBlackjackValue()
               +    " to " + playerHand.getBlackjackValue() + ".";
         }
         else if (dealerHand.getBlackjackValue() == playerHand.getBlackjackValue()) {
            usersMoney = usersMoney - betAmount; 
            message = "Sorry, you lose.  Dealer wins on a tie.";
         }
         else {
            usersMoney = usersMoney + betAmount; 
            message = "You win, " + playerHand.getBlackjackValue()
                  + " to " + dealerHand.getBlackjackValue() + "!";
         }
         repaint();
      }
      
      
      /**
       * Called by the constructor, and called by actionPerformed() if  the 
       * user clicks the "New Game" button.  Start a new game.  Deal two cards 
       * to each player.  The game might end right then  if one of the players 
       * had blackjack.  Otherwise, gameInProgress is set to true and the game 
       * begins.
       */
      void doNewGame() {
         if (gameInProgress) {
               // If the current game is not over, it is an error to try
               // to start a new game.  This shouldn't be possible because
               // the new game button is disabled while a game is in progress,
               // but it doesn't hurt anything to check anyway.
            message = "You still have to finish this game!";
            repaint();
            return;
         }
         if (usersMoney == 0) {
               // The user has run out of money; give the user another $100.
            usersMoney = 100;
         }
         if ( ! checkBet() ) {
               // The user's bet was not legal, so we can't start a game.
               // The checkBet method has already given an error message.
            return;
         }
         deck = new Deck();   // Create the deck and hands to use for this game.
         dealerHand = new BlackjackHand();
         playerHand = new BlackjackHand();
         deck.shuffle();
         dealerHand.addCard( deck.dealCard() );  // Deal two cards to each player.
         dealerHand.addCard( deck.dealCard() );
         playerHand.addCard( deck.dealCard() );
         playerHand.addCard( deck.dealCard() );
         if (dealerHand.getBlackjackValue() == 21) {
            message = "Sorry, you lose.  Dealer has Blackjack.";
            usersMoney = usersMoney - betAmount; 
            setGameInProgress(false);
         }
         else if (playerHand.getBlackjackValue() == 21) {
            message = "You win!  You have Blackjack.";
            usersMoney = usersMoney + betAmount;  
            setGameInProgress(false);
         }
         else {
            message = "You have " + playerHand.getBlackjackValue() + ".  Hit or stand?";
            setGameInProgress(true);
         }
         repaint();
      }  // end newGame();
      
      
      /**
       * The paint method shows the message at the bottom of the
       * canvas, and it draws all of the dealt cards spread out
       * across the canvas.
       */
      public void paintComponent(Graphics g) {
         
         super.paintComponent(g); // fill with background color.
         
         g.setFont(bigFont);
         g.setColor(Color.GREEN);
         g.drawString(message, 10, getHeight() - 10);
         
         // Draw a message telling how much money the user has.
         
         g.setColor(Color.YELLOW);
         if (usersMoney > 0)
            g.drawString("You have $" + usersMoney, 10, getHeight() - 35);
         else
            g.drawString("YOU ARE BROKE!  (I will give you another $100.)", 
                  10, getHeight() - 32 );
         
         if (dealerHand == null)
            return;  // the first game has not yet started.
         
         // Draw labels for the two sets of cards.
         
         g.setColor(Color.GREEN);
         g.drawString("Dealer's Cards:", 10, 23);
         g.drawString("Your Cards:", 10, 153);
         
         // Draw dealer's cards.  Draw first card face down if
         // the game is still in progress,  It will be revealed
         // when the game ends.
         
         g.setFont(smallFont);
         if (gameInProgress)
            drawCard(g, null, 10, 30);
         else
            drawCard(g, dealerHand.getCard(0), 10, 30);
         for (int i = 1; i < dealerHand.getCardCount(); i++)
            drawCard(g, dealerHand.getCard(i), 10 + i * 90, 30);
         
         // Draw the user's cards.
         
         for (int i = 0; i < playerHand.getCardCount(); i++)
            drawCard(g, playerHand.getCard(i), 10 + i * 90, 160);
         
      }  // end paintComponent();
      
      
      /**
       * Draws a card as a 80 by 100 rectangle with upper left corner at (x,y).
       * The card is drawn in the graphics context g.  If card is null, then
       * a face-down card is drawn.  (The cards are rather primitive!)
       */
      void drawCard(Graphics g, Card card, int x, int y) {
         if (card == null) {  
               // Draw a face-down card
            g.setColor(Color.blue);
            g.fillRect(x,y,80,100);
            g.setColor(Color.white);
            g.drawRect(x+3,y+3,73,93);
            g.drawRect(x+4,y+4,71,91);
         }
         else {
            g.setColor(Color.white);
            g.fillRect(x,y,80,100);
            g.setColor(Color.gray);
            g.drawRect(x,y,79,99);
            g.drawRect(x+1,y+1,77,97);
            if (card.getSuit() == Card.DIAMONDS || card.getSuit() == Card.HEARTS)
               g.setColor(Color.red);
            else
               g.setColor(Color.black);
            g.drawString(card.getValueAsString(), x + 10, y + 30);
            g.drawString("of", x+ 10, y + 50);
            g.drawString(card.getSuitAsString(), x + 10, y + 70);
         }
      }  // end drawCard()
      
      
   } // end nested class CardPanel
   
   
} // end class BlackjackGUI2