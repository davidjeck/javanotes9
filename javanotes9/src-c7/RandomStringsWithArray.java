import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;

/**
 * This program displays 25 copies of a message, and it runs
 * an animation in which the strings move around on the screen.
 * The color, position, and velocity of each message is selected 
 * at random when the program first starts, and there is a button that
 * the user can click to reinitialize all the random values.
 */
public class RandomStringsWithArray extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	//----------------------------------------------
	
	private final static String MESSAGE = "Hello JavaFX"; 

	private final static Font[] fonts = new Font[] {
			Font.font("Times New Roman", FontWeight.BOLD, 20),
			Font.font("Arial", FontWeight.BOLD, FontPosture .ITALIC, 28),
			Font.font("Verdana", 32),
			Font.font(40),
			Font.font("Times New Roman", FontWeight.BOLD, FontPosture .ITALIC, 60)
	};

	private Canvas canvas;  // The canvas on which the strings are drawn.
	
	private StringData[] stringData;  // Holds all information needed
	                                  // to draw the strings.

	private static class StringData {  // Info needed to draw one string.
		double x,y;    // location of the string
		double dx,dy;  // velocity of the string, in pixels per second
		Color color;   // color of the string
		Font font;     // the font that is used to draw the string
	}
	

	public void start( Stage stage ) {

		canvas = new Canvas(500,300);
		createStringData();
		draw();  // draw content of canvas the first time.

		Button redraw = new Button("Restart!");
		redraw.setOnAction( e -> createStringData() );
		     // When the button is clicked, the string data is re-initialized.
		     // There is no need to call draw() because the animation that
		     // runs continually will redraw the canvas in the next frame.

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
		
		AnimationTimer timer = new AnimationTimer() {
			   // The timer will run continually.  In each frame, all the strings
			   // will be moved, and the canvas will be redrawn.
			long previousTime;
			public void handle(long time) {
				if (previousTime > 0) {
					   // Time since previous call to handle is (time - previousTime),
					   // in nanoseconds.  Dividing by 1e9 converts nanoseconds to seconds.
					   // The first time handle() is called, previousTime is 0 and the
					   // update is not done.
					updateStringData( (time - previousTime)/1e9 );
				}
				draw();
				previousTime = time;
			}
		};
		timer.start();
	}
	
	
	/**
	 * Creates an array of 25 StringData objects and fills it with
	 * randomly generated data for each of the 25 strings.  This is
	 * called in the start() method and when the user clicks the
	 * "Restart!" button.
	 */
	private void createStringData() {
		stringData = new StringData[25];
		for (int i = 0; i < 25; i++) {
			stringData[i] = new StringData();
			stringData[i].x = canvas.getWidth() * Math.random();
			stringData[i].y = canvas.getHeight() * Math.random();
			stringData[i].dx = 50 + 150*Math.random(); // 50 to 200 pixels per second
			if (Math.random() < 0.5) // 50% chance that dx is negative
				stringData[i].dx = -stringData[i].dx;
			stringData[i].dy = 50 + 150*Math.random();
			if (Math.random() < 0.5) // 50% chance that dy is negative
				stringData[i].dy = -stringData[i].dy;
			stringData[i].color = Color.hsb( 360*Math.random(), 1.0, 1.0 );
			stringData[i].font = fonts[ (int)(5*Math.random()) ];
		}
	}

	
	/**
	 * Update the data for the 25 strings by moving each string, where the
	 * distance moved depends on the velocity.  If a string moves too far
	 * off the canvas, move it to the opposite side of the canvas.
	 * (To make sure a string has moved all the way off the canvas to the
	 * left, wait until data.x reaches -400 before moving it to the
	 * right of the canvas. 
	 * @param deltaTimeInSeconds time that has elapsed since the previous
	 *          call to updateStringData, measured in seconds.
	 */
	private void updateStringData(double deltaTimeInSeconds) {
		for ( StringData data : stringData ) {
			data.x += data.dx * deltaTimeInSeconds;
			data.y += data.dy * deltaTimeInSeconds;
			if (data.x < -400)
				data.x = canvas.getWidth();
			if (data.x > canvas.getWidth()+10)
				data.x = -400;
			if (data.y < -10)
				data.y = canvas.getHeight() + 70;
			if (data.y > canvas.getHeight() + 80)
				data.y = -10;
		}
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

		for ( StringData data : stringData ) {

			// Draw one string, using the properties in one of the
			// StringData objects from the array.

			g.setFill( data.color );
			g.setFont( data.font);
			g.fillText(MESSAGE, data.x, data.y);
			g.setStroke(Color.BLACK);
			g.strokeText(MESSAGE, data.x, data.y);

		} // end for

	} // end draw()


}  // end class RandomStringsWithArray
