import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.image.Image;
import javafx.geometry.Pos;

/**
 * This program demonstrates Transforms.  Transforms controlled by sliders
 * are applied to a canvas in the order scale, shear, rotate, translate.
 * There is also a final translation that moves the origin to the center
 * of the canvas, so that the transformations specified by the sliders 
 * are applied with the origin in the center.  That is, the center of
 * the canvas is the center for scaling, shear, and rotation; those
 * transformations leave the center fixed.  This program requires an
 * image resource file named face-smile.png.
 */
public class TransformDemo extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//----------------------------------------------------------------------------


	private Canvas canvas;  // The canvas where the figures are drawn.

	private Slider scaleXSlider;  // Controls scaling in horizontal direction.
	private Slider scaleYSlider;  // Controls scaling in vertical direction.
	private Slider shearSlider;  // Controls horizontal shear.
	private Slider rotateSlider;  // Controls angle of rotation.
	private Slider translateXSlider;  // Controls horizontal translation.
	private Slider translateYSlider;  // Controls vertical translation.
	
	private Button resetButton;  // Resets all sliders to default value.
	                             // Transformation is the identity.
	
	private LinearGradient gradient;  // A linear gradient paint.
	
	private Image smiley;  // A small smiley face (from Gnome desktop)
	
	
	/**
	 * Set up GUI and event handling.
	 */
	public void start(Stage stage) {

		smiley = new Image("face-smile.png");
		gradient = new LinearGradient(0,0,50,20,false,CycleMethod.REFLECT,
				new Stop(0,Color.BLACK), new Stop(1,Color.LIGHTGRAY));
		
		scaleXSlider = new Slider(-2,2,1);
		scaleXSlider.setBlockIncrement(0.1);
		   // (Note:  The block increment is the amount by which the value
		   //  will change, if the user hits a left/right arrow key while
		   //  the slider has the input focus.)
		scaleYSlider = new Slider(-2,2,1);
		scaleYSlider.setBlockIncrement(0.1);
		shearSlider = new Slider(-1,1,0);
		shearSlider.setBlockIncrement(0.05);
		rotateSlider = new Slider(-180,180,0);
		translateXSlider = new Slider(-200,200,0);
		translateYSlider = new Slider(-150,150,0);
		
		resetButton = new Button("Reset Tranforms");
		resetButton.setOnAction( e -> {
			scaleXSlider.setValue(1);
			scaleYSlider.setValue(1);
			shearSlider.setValue(0);
			rotateSlider.setValue(0);
			translateXSlider.setValue(0);
			translateYSlider.setValue(0);
		});
		
		VBox bottom = new VBox(10,
				makeInput("X-Scale:    ", scaleXSlider),
				makeInput("Y-Scale:    ", scaleYSlider),
				makeInput("X-Shear:    ", shearSlider),
				makeInput("Rotate:     ", rotateSlider),
				makeInput("X-Translate:", translateXSlider),
				makeInput("Y-Translate:", translateYSlider),
				resetButton
			);
		bottom.setStyle("-fx-padding: 10px; -fx-border-color: #444; -fx-border-width: 4px 0 0 0");
		bottom.setAlignment(Pos.CENTER);
		
		canvas = new Canvas(800,600);
		drawCanvas();  // (must be done after making sliders)
		
		BorderPane root = new BorderPane(canvas);
		root.setStyle("-fx-border-color: #444; -fx-border-width: 4px");
		root.setBottom(bottom);

		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.setTitle("Transform Demo");
		stage.show();
	}
	
	
	/**
	 * Creates an HBox containing a slider and a label that displays the
	 * value of that slider.  An event handler is added to the slider to
	 * redraw the canvas when the slider value changes.
	 */
	private HBox makeInput(String text, Slider slider) {
		Label label = new Label();
		label.setStyle("-fx-font: 14pt monospace");
		label.textProperty().bind(slider.valueProperty().asString(text + "%8.2f"));
		slider.setPrefWidth(300);
		slider.valueProperty().addListener( e -> drawCanvas() );
		HBox box = new HBox(10,slider,label);
		box.setAlignment(Pos.CENTER);
		return box;
	}

	
	/**
	 *  Fill the canvas with white, then draw some stuff, transformed
	 *  by the transforms specified by the sliders.
	 */
	private void drawCanvas() {
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.save();  // Save the current state, before applying transforms.
		g.setFill(Color.WHITE);
		g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
		
		g.translate( canvas.getWidth()/2, canvas.getHeight()/2 );  // move origin to center
		
		/*  Apply transforms specified by the sliders, with the origin as the center
		 *  point for scaling, rotation and shear. */
		
		g.translate( translateXSlider.getValue(), translateYSlider.getValue() );
		g.rotate( rotateSlider.getValue() );
		g.transform( new Affine(1, shearSlider.getValue(), 0, 0, 1, 0) ); // a shear transform
		g.scale( scaleXSlider.getValue(), scaleYSlider.getValue());
		
		/* Draw some objects, subject to the transforms. */
		
		g.setFont(Font.font(48));
		g.setFill(Color.BLACK);
		g.fillText("Hello World",0,0);
		g.setLineWidth(5);
		g.setStroke(Color.RED);
		g.strokeRect(-200,-150,150,100);
		g.setFill(gradient);
		g.fillOval(-200,50,150,100);
		g.setLineWidth(2);
		g.setStroke(Color.BLACK);
		g.strokeOval(-200,50,150,100);
		g.setFill(Color.GREEN);
		g.fillRoundRect(20, 20, 200, 100, 20, 20);
		g.setFill(Color.BLUE);
		g.fillRect(50,-175,50,100);
		g.drawImage(smiley,-100,-20);
		g.restore();  // Restore the previous, untransformed state.
	}

	

} // end class TransformDemo

