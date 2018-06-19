import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * In this program, the user plays a game of Blackjack.  The
 * computer acts as the dealer.  The user plays by clicking
 * "Hit!" and "Stand!" buttons.    The user can place bets.
 * At the beginning of the game, the user is given $100.
 *
 * This program depends on the following classes:  Card, Hand,
 * BlackjackHand, Deck.  It also requires the image resource
 * file cards.png.
 */
public class BlackjackGUI2 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //---------------------------------------------------------------------

    private Deck deck;         // A deck of cards to be used in the game.

    private BlackjackHand dealerHand;   // Hand containing the dealer's cards.
    private BlackjackHand playerHand;   // Hand containing the user's cards.

    private Button hitButton, standButton, newGameButton;

    private TextField betInput;  // An input box for the user's bet amount.

    private String message; // A message drawn on the canvas, which changes
                            //    to reflect the state of the game.

    private boolean gameInProgress; // Set to true when a game begins and to false
                                    //   when the game ends.

    private Canvas board;     // The canvas were cards and messages are displayed.

    private Image cardImages;  // The image that contains all the cards in a deck.

    private int usersMoney = 100;  // How much money the user currently has.

    private int betAmount;  // The amount the use bet on the current game,
                            //      when a game is in progress.

    /**
     * The start() method() sets up the GUI and event handling.
     */
    public void start(Stage stage) {

        cardImages = new Image("cards.png");

        board = new Canvas(515, 415); 
            // space for 5 cards across and 2 cards down, 
            // with 20-pixel spaces between cards,
            // plus space for messages

        hitButton = new Button( "Hit!" );
        hitButton.setOnAction( e -> doHit() );
        standButton = new Button( "Stand!" );
        standButton.setOnAction( e -> doStand() );
        newGameButton = new Button( "New Game" );
        newGameButton.setOnAction( e -> doNewGame() );

        betInput = new TextField("10");
        betInput.setPrefColumnCount(5);

        HBox buttonBar = new HBox(6, hitButton, standButton, newGameButton,
                new Label(" Your bet:"), betInput);
        buttonBar.setStyle("-fx-border-color: darkred; -fx-border-width: 3px 0 0 0;" 
                + "-fx-padding: 8px; -fx-background-color:beige");
        buttonBar.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-border-color: darkred; -fx-border-width: 3px");
        root.setCenter(board);
        root.setBottom(buttonBar);

        setGameInProgress(false);
        drawBoard();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Blackjack");
        stage.setResizable(false);
        stage.show();

    }  // end start()


    /**
     * This method is called whenever the value of the gameInProgress
     * property has to be changed.  In addition to setting the value
     * of the gameInProgress variable, it also enables and disables
     * the buttons and text input box to reflect the state of the game.
     * @param inProgress The new value of gameInProgress.
     */
    private void setGameInProgress( boolean inProgress ) {
        gameInProgress = inProgress;
        if (gameInProgress) {
            hitButton.setDisable(false);
            standButton.setDisable(false);
            newGameButton.setDisable(true);
            betInput.setEditable(false);
            hitButton.requestFocus();
        }
        else {
            hitButton.setDisable(true);
            standButton.setDisable(true);
            newGameButton.setDisable(false);
            betInput.setEditable(true);
            newGameButton.requestFocus();
        }
    }


    /**
     * This method is called when the user clicks the "Hit!" button.  First 
     * check that a game is actually in progress.  If not, give  an error 
     * message and exit.  Otherwise, give the user a card.  The game can end 
     * at this point if the user goes over 21 or if the user has taken 5 cards 
     * without going over 21.
     */
    void doHit() {
        if (gameInProgress == false) {
            message = "Click \"New Game\" to start a new game.";
            drawBoard();
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
        drawBoard();
    }


    /**
     * This method is called when the user clicks the "Stand!" button.
     * Check whether a game is actually in progress.  If it is, the game 
     * ends.  The dealer takes cards until either the dealer has 5 cards 
     * or more than 16 points.  Then the  winner of the game is determined. 
     */
    void doStand() {
        if (gameInProgress == false) {
            message = "Click \"New Game\" to start a new game.";
            drawBoard();
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
        drawBoard();
    }


    /**
     * Called by the constructor, and called by doNewGame().  Start a new game.  
     * Deal two cards to each player.  The game might end right then  if one 
     * of the players had blackjack.  Otherwise, gameInProgress is set to true 
     * and the game begins.
     */
    void doNewGame() {
        if (gameInProgress) {
                // If the current game is not over, it is an error to try
                // to start a new game.
            message = "You still have to finish this game!";
            drawBoard();
            return;
        }
        if (usersMoney == 0) { // User is broke; give the user another $100.
            usersMoney = 100;
        }
        try {  // get the amount of the user's bet
            betAmount = Integer.parseInt(betInput.getText());
        }
        catch (NumberFormatException e) {
            message = "Bet amount must be an integer!";
            betInput.requestFocus();
            betInput.selectAll();
            drawBoard();
            return;
        }
        if (betAmount > usersMoney) {
            message = "The bet amount can't be more than you have!";
            betInput.requestFocus();
            betInput.selectAll();
            drawBoard();
            return;
        }
        if (betAmount <= 0) {
            message = "The bet has to be a positive number";
            betInput.requestFocus();
            betInput.selectAll();
            drawBoard();
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
        drawBoard();
    }  // end newGame();


    /**
     * The drawBoard() method shows the message at the bottom of the
     * canvas, and it draws all of the dealt cards spread out
     * across the canvas.  If the first game has not started, it shows
     * a welcome message instead of the cards.
     */
    public void drawBoard() {

        GraphicsContext g = board.getGraphicsContext2D();
        g.setFill( Color.DARKGREEN);
        g.fillRect(0,0,board.getWidth(),board.getHeight());

        g.setFont( Font.font(16) );

        // Draw a message telling how much money the user has.

        g.setFill(Color.YELLOW);
        if (usersMoney > 0) {
            g.fillText("You have $" + usersMoney, 20, board.getHeight() - 45);
        }
        else {
            g.fillText("YOU ARE BROKE!  (I will give you another $100.)", 
                    20, board.getHeight() - 45 );
        }

        g.setFill( Color.rgb(220,255,220) );

        if (dealerHand == null) {
                // The first game has not yet started.
                // Draw a welcome message and return.
            g.setFont( Font.font(30) );
            g.fillText("Welcome to Blackjack!\nPlace your bet and\nclick \"New Game\".", 40,80);
            return;  
        }

        // Draw the message at the bottom of the canvas.

        g.fillText(message, 20, board.getHeight() - 20);

        // Draw labels for the two sets of cards.

        g.fillText("Dealer's Cards:", 20, 27);
        g.fillText("Your Cards:", 20, 190);

        // Draw dealer's cards.  Draw first card face down if
        // the game is still in progress,  It will be revealed
        // when the game ends.

        if (gameInProgress)
            drawCard(g, null, 20, 40);
        else
            drawCard(g, dealerHand.getCard(0), 20, 40);
        for (int i = 1; i < dealerHand.getCardCount(); i++)
            drawCard(g, dealerHand.getCard(i), 20 + i * 99, 40);

        // Draw the user's cards.

        for (int i = 0; i < playerHand.getCardCount(); i++)
            drawCard(g, playerHand.getCard(i), 20 + i * 99, 206);

    }  // end drawBoard();


    /**
     * Draws a card with top-left corner at (x,y).  If card is null,
     * then a face-down card is drawn.  The cards images are from 
     * the file cards.png; this program will fail without it.
     */
    private void drawCard(GraphicsContext g, Card card, int x, int y) {
        int cardRow, cardCol;
        if (card == null) {  
            cardRow = 4;   // row and column of a face down card
            cardCol = 2;
        }
        else {
            cardRow = 3 - card.getSuit();
            cardCol = card.getValue() - 1;
        }
        double sx,sy;  // top left corner of source rect for card in cardImages
        sx = 79 * cardCol;
        sy = 123 * cardRow;
        g.drawImage( cardImages, sx,sy,79,123, x,y,79,123 );
    } // end drawCard()


} // end class BlackjackGUI2
