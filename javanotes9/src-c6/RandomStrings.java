import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

/**
 * This program displays 25 copies of a message.  The color and 
 * position of each message is selected at random.  The font
 * of each message is randomly chosen from among five possible
 * fonts.  The messages are displayed on a white background.
 * There is a button that the user can click to redraw the
 * image using new random values.
 */
public class RandomStrings extends Application {

	private final static String MESSAGE = "Hello JavaFX"; 

	private Font font1, font2, font3, font4, font5;  // The five fonts.
	
	private Canvas canvas;  // The canvas on which the strings are drawn.
	

	public static void main(String[] args) {
		launch();
	}
	

	public void start( Stage stage ) {

		font1 = Font.font("Times New Roman", FontWeight.BOLD, 20);
		font2 = Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 28);
		font3 = Font.font("Verdana", 32);
		font4 = Font.font(40);
		font5 = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 60);
		
		canvas = new Canvas(500,300);
		draw();  // draw content of canvas the first time.

		Button redraw = new Button("Redraw!");
		redraw.setOnAction( e -> draw() );

		StackPane bottom = new StackPane(redraw);
		bottom.setStyle("-fx-background-color: gray; -fx-padding:5px;" + 
		                    " -fx-border-color:black; -fx-border-width: 2px 0 0 0");
		BorderPane root = new BorderPane(canvas);
		root.setBottom(bottom);
		root.setStyle("-fx-border-color:black; -fx-border-width: 2px");
		
		stage.setScene( new Scene(root, Color.BLACK) );
		stage.setTitle("Random Strings");
		stage.setResizable(false);
		stage.show();
	}
	

	/**
	 * The draw() method is responsible for drawing the content of the canvas.
	 * It draws 25 copies of the message string, using a random color, font, and
	 * position for each string.
	 */
	private void draw() {

		GraphicsContext g = canvas.getGraphicsContext2D();
		
		double width = canvas.getWidth();
		double height = canvas.getHeight();
		
		g.setFill( Color.WHITE );  // fill with white background
		g.fillRect(0, 0, width, height);

		for (int i = 0; i < 25; i++) {

			// Draw one string.  First, set the font to be one of the five
			// available fonts, at random.  

			int fontNum = (int)(5*Math.random()) + 1;
			switch (fontNum) {
				case 1 -> g.setFont(font1);
				case 2 -> g.setFont(font2);
				case 3 -> g.setFont(font3);
				case 4 -> g.setFont(font4);
				case 5 -> g.setFont(font5);
			} // end switch

			// Set the color to a bright, saturated color, with random hue.

			double hue = 360*Math.random();
			g.setFill( Color.hsb(hue, 1.0, 1.0) );

			// Select the position of the string, at random.

			double x,y;
			x = -50 + Math.random()*(width+40);
			y = Math.random()*(height+20);

			// Draw the message.

			g.fillText(MESSAGE,x,y);
			
			// Also stroke the outline of the strings with black
			
			g.setStroke(Color.BLACK);
			g.strokeText(MESSAGE,x,y);

		} // end for

	} // end draw()


}  // end class RandomStrings
