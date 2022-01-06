import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Pos;

/**
 * This program demonstrates ImagePattern and LinearGradient paints.
 * A polygon is drawn that is filled with a paint selected by the user.
 * The user can also drag the vertices of the polygon.
 * This program requires image resource files named face-smile.png
 * and tile.png.
 */
public class PaintDemo extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//----------------------------------------------------------------------------


	private Canvas canvas;  // The canvas where the polygon is drawn.

	private Paint paint;  // The paint that is used to fill the polygon in the canvas.

	private Image smiley, tile;  // Images for ImagePaint.

	private RadioButton gradientButton1, gradientButton2;  // Select the type of paint.
	private RadioButton patternButton1, patternButton2;

	private ToggleGroup paintStyleButtonGroup;  // Toggle group controlling the radio buttons.

	private double gradientAngle = 45, gradientWidth = 50;  // Settings that affect the paint.
	private double patternOffset = 0, patternScale = 100;

	private Slider slider1, slider2;  // Sliders that control the settings; which 
									  // setting is affected depends on current paint type.

	private Label label1 = new Label("  Gradient Angle:");  // Labels change, depending
	private Label label2 = new Label("  Gradient Width:");  //   on type of paint.

	private double[] xcoord = {100, 420, 300, 470, 230, 40};  // Coords for vertices of polygon.
	private double[] ycoord = {100, 30, 240, 260, 480, 300};

	private int draggedPoint = -1;  // When a vertex is being dragged, this is the index
	                                // of the vertex coords in the above arrays.  The value
	                                // -1 means no vertex is being dragged.


	/**
	 * Set up GUI and event handling.
	 */
	public void start(Stage stage) {

		smiley = new Image("face-smile.png");
		tile = new Image("tile.png");

		canvas = new Canvas(500,500);
		slider1 = new Slider(0,360,gradientAngle);
		slider2 = new Slider(10,300,gradientWidth);
		gradientButton1 = new RadioButton("Black/Gray Gradient");
		gradientButton2 = new RadioButton("Red/Green/Blue Gradient");
		patternButton1 = new RadioButton("Smiley Face Pattern");
		patternButton2 = new RadioButton("Tile Pattern");
		gradientButton1.setSelected(true);

		paintStyleButtonGroup = new ToggleGroup();
		gradientButton1.setToggleGroup(paintStyleButtonGroup);
		gradientButton2.setToggleGroup(paintStyleButtonGroup);
		patternButton1.setToggleGroup(paintStyleButtonGroup);
		patternButton2.setToggleGroup(paintStyleButtonGroup);
		
		canvas.setOnMousePressed( this::mousePressed );
		canvas.setOnMouseDragged( this::mouseDragged );
		paintStyleButtonGroup.selectedToggleProperty().addListener( e -> {
			if (paintStyleButtonGroup.getSelectedToggle() != null)
				paintStyleChanged();
		});
		slider1.valueProperty().addListener( e -> {
			if (slider1.isValueChanging())
				setPaint();
		});
		slider2.valueProperty().addListener( e -> {
			if (slider2.isValueChanging())
				setPaint();
		});

		setPaint();
		
		BorderPane root = new BorderPane(canvas);
		root.setStyle("-fx-border-color: #444; -fx-border-width: 4");

		TilePane bottom = new TilePane(12,12);
		bottom.setStyle("-fx-padding: 12px; -fx-border-color: #444; -fx-border-width:3px 0 0 0");
		bottom.setTileAlignment(Pos.CENTER_LEFT);
		bottom.setAlignment(Pos.CENTER);
		bottom.setPrefColumns(2);
		bottom.getChildren().addAll(label1, slider1, label2, slider2,
				         gradientButton1, patternButton1, gradientButton2, patternButton2);
		root.setBottom(bottom);
		
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.setTitle("PaintDemo -- Drag the Vertices");
		stage.show();
	}

	
	/**
	 *  Fill the canvas with white, then draw the polygon filled with the
	 *  current fillPaint.  Draw small squares at the polygon vertices;
	 *  the user can drag these squares to move the vertices.
	 */
	private void drawCanvas() {
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.setFill(Color.WHITE);
		g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
		g.setFill(paint);
		g.fillPolygon(xcoord, ycoord, 6);
		g.setStroke(Color.BLACK);
		g.setLineWidth(2);
		g.strokePolygon(xcoord, ycoord, 6);
		g.setFill(Color.BLACK);
		for (int i = 0; i < 6; i++)
			g.fillRect(xcoord[i] - 4, ycoord[i] - 4, 9, 9);
	}

	
	/**
	 * Called when the user presses the mouse on the canvas.  Searches the
	 * polygon vertices to find one near the mouse position.  If one is
	 * found, the user can drag it.
	 */
	private void mousePressed(MouseEvent e) {
		draggedPoint = -1;
		for (int i = 0; i < 6; i++) {
			if (Math.abs(xcoord[i] - e.getX()) < 5 && Math.abs(ycoord[i] - e.getY()) < 5) {
				draggedPoint = i;
				break;
			}
		}
	}

	
	/**
	 * Called when the mouse is dragged on the canvas.  If a vertex is being
	 * dragged, move it to the current mouse position and redraw the canvas.
	 */
	private void mouseDragged(MouseEvent e) {
		if (draggedPoint < 0)
			return;
		double x = Math.max(0, Math.min(e.getX(),canvas.getWidth()));
		double y = Math.max(0, Math.min(e.getY(),canvas.getHeight()));
		xcoord[draggedPoint] = x;
		ycoord[draggedPoint] = y;
		drawCanvas();
	}


	/**
	 * Responds when a user clicks on the radio button to select a new paint style.
	 * This method sets up the labels and sliders to correspond to the kind of 
	 * paint that has been selected.  Calls setPaint() to apply the paint to the
	 * canvas.
	 */
	private void paintStyleChanged() {
		Toggle currentButton = paintStyleButtonGroup.getSelectedToggle();
		if (currentButton == gradientButton1 || currentButton == gradientButton2) {
			label1.setText("  Gradient Angle:");
			label2.setText("  Gradient Width:");
			slider1.setMin(0);
			slider1.setMax(360);
			slider1.setValue(gradientAngle);
			slider2.setMin(20);
			slider2.setMax(200);
			slider2.setValue(gradientWidth);
			setPaint();
		}
		else if (currentButton == patternButton1 || currentButton == patternButton2){
			label1.setText("  Image Offset:");
			label2.setText("  Image Scale:");
			slider1.setMin(0);
			slider1.setMax(100);
			slider1.setValue(patternOffset);
			slider2.setMin(30);
			slider2.setMax(200);
			slider2.setValue(patternScale);
			setPaint();
		}
	}


	/**
	 * Called when the type of paint or the values on the sliders are changed.
	 * Creates the new Paint and redraws the canvas to show the change.
	 */
	private void setPaint() {
		Toggle currentButton = paintStyleButtonGroup.getSelectedToggle(); // (can be null)
		if (currentButton == gradientButton1 || currentButton == gradientButton2) {
			gradientAngle = slider1.getValue();
			gradientWidth = slider2.getValue();
			double x1 = canvas.getWidth()/2;
			double y1 = canvas.getHeight()/2;
			double x2 = x1 + gradientWidth * Math.cos(gradientAngle/180.0 * Math.PI);
			double y2 = y1 + gradientWidth * Math.sin(gradientAngle/180.0 * Math.PI);
			if (currentButton == gradientButton1)  {
				paint = new LinearGradient(x1,y1,x2,y2,false,CycleMethod.REFLECT,
						new Stop(0,Color.BLACK), new Stop(1,Color.LIGHTGRAY));
			}
			else {
				paint = new LinearGradient(x1,y1,x2,y2,false,CycleMethod.REFLECT,
						new Stop(0,Color.RED), new Stop(0.5,Color.GREEN), new Stop(1,Color.BLUE));
			}
			drawCanvas();
		}
		else if (currentButton == patternButton1 || currentButton == patternButton2){
			patternOffset = slider1.getValue();
			patternScale = slider2.getValue();
			Image texture;
			if (currentButton == patternButton1)
				texture = smiley;
			else
				texture = tile;
			double width = texture.getWidth() * patternScale / 100;
			double height = texture.getHeight() * patternScale / 100;
			double offsetX = width * patternOffset / 100;
			double offsetY = height * patternOffset / 100;
			paint = new ImagePattern(texture,offsetX,offsetY,width,height,false);
			drawCanvas();
		}
	}



} // end class PaintDemo

