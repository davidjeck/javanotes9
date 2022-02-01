import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

/**
 * This program displays 5 cards selected at random from a Deck.
 * It depends on the files Deck.java, Card.java, and cards.png.
 * There is a button that the user can click to redraw the
 * image using new random cards.
 */
public class RandomCards extends Application {

	private Canvas canvas;  // The canvas on which the strings are drawn.
	
	private Image cardImages;  // Contains images of all of the cards.
	                           // Cards are arranged in 5 rows and 13 columns.
	                           // Each of the first four rows contains the cards
	                           // from one suit, in numerical order.  The first
	                           // four rows contain clubs, diamonds, hearts, and
	                           // spades in that order.  The fifth row contains
	                           // two jokers and a face-down card.
	

	public static void main(String[] args) {
		launch();
	}
	

	public void start( Stage stage ) {
		
		cardImages = new Image("cards.png");
		
		canvas = new Canvas(5*79 + 120, 123 + 40);
		draw();  // draw content of canvas the first time.

		Button redraw = new Button("Deal Again!");
		redraw.setOnAction( e -> draw() );

		StackPane bottom = new StackPane(redraw);
		bottom.setStyle("-fx-background-color: gray; -fx-padding:5px;" + 
		                    " -fx-border-color:blue; -fx-border-width: 2px 0 0 0");
		BorderPane root = new BorderPane(canvas);
		root.setBottom(bottom);
		root.setStyle("-fx-border-color:blue; -fx-border-width: 2px; -fx-background-color: lightblue");
		
		stage.setScene( new Scene(root, Color.BLACK) );
		stage.setTitle("Random Cards");
		stage.setResizable(false);
		stage.show();

	}
	

	/**
	 * The draw() method is responsible for drawing the content of the canvas.
	 * It draws 5 cards in a row.  The first card has top left corner at (20,20),
	 * and there is a 20 pixel gap between each card and the next.
	 */
	private void draw() {
		
		GraphicsContext g = canvas.getGraphicsContext2D();
		
		Deck deck = new Deck();
		deck.shuffle();
		
		double sx,sy;  // top left corner of source rect for card in cardImages
		double dx,dy;  // top left corner of destination rect for card in the canvas
		
		for (int i = 0; i < 5; i++) {
			Card card = deck.dealCard();
			System.out.println(card); // for testing
			sx = 79 * (card.getValue()-1);
			sy = 123 * (3 - card.getSuit());
			dx = 20 + (79+20) * i;
			dy = 20;
			g.drawImage( cardImages, sx,sy,79,123, dx,dy,79,123 );
		}
		
	} // end draw()


}  // end class RandomCards
