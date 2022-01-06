import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * In this program, the user plays a game of Blackjack.  The
 * computer acts as the dealer.  The user plays by clicking
 * "Hit!" and "Stand!" buttons.
 *
 * This program depends on the following classes:  Card, Hand,
 * BlackjackHand, Deck.  It also requires the image resource
 * file cards.png.
 */
public class BlackjackGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //---------------------------------------------------------------------

    private Deck deck;         // A deck of cards to be used in the game.

    private BlackjackHand dealerHand;   // Hand containing the dealer's cards.
    private BlackjackHand playerHand;   // Hand containing the user's cards.

    private String message; // A message drawn on the canvas, which changes
                            //    to reflect the state of the game.

    private boolean gameInProgress; // Set to true when a game begins and to false
                                    //   when the game ends.

    private Canvas board;     // The canvas were cards and messages are displayed.
    
    private Image cardImages;  // The image that contains all the cards in a deck.
    
    
    /**
     * The start() method() sets up the GUI and event handling.
     */
    public void start(Stage stage) {

        cardImages = new Image("cards.png");

        board = new Canvas(515, 390); 
                         // space for 5 cards across and 2 cards down, 
                         // with 20-pixel spaces between cards,
                         // plus space for messages

        Button hitButton = new Button( "Hit!" );
        hitButton.setOnAction( e -> doHit() );
        Button standButton = new Button( "Stand!" );
        standButton.setOnAction( e -> doStand() );
        Button newGameButton = new Button( "New Game" );
        newGameButton.setOnAction( e -> doNewGame() );

        HBox buttonBar = new HBox(6,hitButton,standButton,newGameButton);
        buttonBar.setStyle("-fx-border-color: darkred; -fx-border-width: 3px 0 0 0;" 
                + "-fx-padding: 8px; -fx-background-color:beige");
        buttonBar.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-border-color: darkred; -fx-border-width: 3px");
        root.setCenter(board);
        root.setBottom(buttonBar);
        
        doNewGame(); // Start the first game.
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Blackjack");
        stage.setResizable(false);
        stage.show();

    }  // end start()

    
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
            message = "You've busted!  Sorry, you lose.";
            gameInProgress = false;
        }
        else if (playerHand.getCardCount() == 5) {
            message = "You win by taking 5 cards without going over 21.";
            gameInProgress = false;
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
        gameInProgress = false;
        while (dealerHand.getBlackjackValue() <= 16 && dealerHand.getCardCount() < 5)
            dealerHand.addCard( deck.dealCard() );
        if (dealerHand.getBlackjackValue() > 21)
            message = "You win!  Dealer has busted with " + dealerHand.getBlackjackValue() + ".";
        else if (dealerHand.getCardCount() == 5)
            message = "Sorry, you lose.  Dealer took 5 cards without going over 21.";
        else if (dealerHand.getBlackjackValue() > playerHand.getBlackjackValue())
            message = "Sorry, you lose, " + dealerHand.getBlackjackValue()
                                 + " to " + playerHand.getBlackjackValue() + ".";
        else if (dealerHand.getBlackjackValue() == playerHand.getBlackjackValue())
            message = "Sorry, you lose.  Dealer wins on a tie.";
        else
            message = "You win, " + playerHand.getBlackjackValue()
                                     + " to " + dealerHand.getBlackjackValue() + "!";
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
            gameInProgress = false;
        }
        else if (playerHand.getBlackjackValue() == 21) {
            message = "You win!  You have Blackjack.";
            gameInProgress = false;
        }
        else {
            message = "You have " + playerHand.getBlackjackValue() + ".  Hit or stand?";
            gameInProgress = true;
        }
        drawBoard();
    }  // end newGame();


    /**
     * The drawBoard() method shows the message at the bottom of the
     * canvas, and it draws all of the dealt cards spread out
     * across the canvas.
     */
    public void drawBoard() {


        GraphicsContext g = board.getGraphicsContext2D();
        g.setFill( Color.DARKGREEN);
        g.fillRect(0,0,board.getWidth(),board.getHeight());
        
        g.setFont( Font.font(16) );
        g.setFill( Color.rgb(220,255,220) );
        
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


} // end class BlackjackGUI
