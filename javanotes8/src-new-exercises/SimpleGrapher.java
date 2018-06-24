import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;


/**
   The SimpleGrapher program can draw graphs of functions input by the
   user.  The user enters the definition of the function in a text
   input box.  When the user presses return or clicks the "Enter" button,
   the function is graphed.  (Unless the definition contains an error.  
   In that case, an error message is displayed.)

   The graph is drawn on a canvas which represents the region of the
   (x,y)-plane given by  -5 <= x <= 5  and  -5 <= y <= 5.  Any part of
   the graph that lies outside this region is not shown.  The graph
   is drawn by plotting 301 points and joining them with lines.  This
   does not handle discontinuous functions properly.
   
   An example function is graphed when the program starts.

   This program requires the class Expr, which is defined in by a 
   separate file, Expr.java.  That file contains a full description 
   of the syntax of legal function definitions, but the program
   understands operators +, -, *, /, and ^ (where ^ represents
   exponentiation), as well as common mathematical functions 
   such as sin(x) and ln(x).
 */

public class SimpleGrapher extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	//---------------------------------------------------------------------------------

	private GraphCanvas graph;  // The Canvas that will display the graph.
	                            // GraphCanvas is a subclass of Canvas that
	                            // is defined as a static nested class.

	private TextField functionInput;  // A text input box where the user enters
									  // the definition of the function.

	private Label message;  // A label for displaying messages to the user,
							// including error messages when the function
							// definition is illegal.

	/**
	 * Set up the GUI with a large canvas in the center where the functions
	 * are graphed, a label at the top for displaying messages, and
	 * an input box for the function below the canvas.  Also adds an Enter
	 * button with an ActionEvent handler that graphs the function.  The
	 * button is set to be the default button in the window, so that the
	 * user can also graph the function by pressing return.
	 */
	public void start(Stage stage) {
		
		/* Create the components and set up event handling.  The
		 * canvas is given an initial function to draw, and the
		 * textfield is initialized to show the definition of
		 * that function. */

		graph = new GraphCanvas( new Expr("sin(x)*3 + cos(5*x)") );
		
		message = new Label(" Enter a function and click Enter or press return");

		functionInput = new TextField("sin(x)*3 + cos(5*x)");
		
		Button graphIt = new Button("Enter");
		graphIt.setDefaultButton(true);

		graphIt.setOnAction( evt -> {
					// Get the user's function definition from the box and use it
					// to create a new object of type Expr.  Tell the GraphPanel to 
					// graph this function.  If the  definition is illegal, an 
					// IllegalArgumentException is  thrown by the Expr constructor.
					// If this happens, the graph is cleared and an error message 
					// is displayed in the message label.
				Expr function;  // The user's function.
				try {
					String def = functionInput.getText();
					function = new Expr(def);
					graph.setFunction(function);
					message.setText(" Enter a function and click Enter or press return.");
				}
				catch (IllegalArgumentException e) {
					graph.clearFunction();
					message.setText(e.getMessage());
				}
				functionInput.selectAll();
				functionInput.requestFocus();  // Let's user start typing in input box.
			} );
		
		/* Create the layout. */
		
		HBox bottom = new HBox(8, new Label("f(x) ="), functionInput, graphIt);
		
		BorderPane root = new BorderPane();
		root.setCenter(graph);
		root.setTop(message);
		root.setBottom(bottom);
		
		/* Tweak the components to make the program more attractive.  Add borders
		 * around the entire root pane and between the canvas and the top and bottom
		 * components.  Adding some padding around the components in the bottom HBox. */
		
		root.setStyle("-fx-border-color:gray; -fx-border-width:4px");
		message.setTextFill(Color.RED);  // User red text for the message.
		message.setStyle("-fx-background-color:white; -fx-padding:7px; "
                + "-fx-border-color:gray; -fx-border-width:0 0 4px 0");
		message.setMaxWidth(10000);  // Required to make the label (and its border)
		                             // extend the full width of the window.
		bottom.setStyle("-fx-border-color:gray; -fx-border-width:4px 0 0 0; -fx-padding:8px");
		HBox.setHgrow(functionInput, Priority.ALWAYS); // Allows functionInput to grow
		                                               // to fill the available space.
		
		/* Finish setting up the window and make it visible. */
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("A Simple Function Grapher");
		stage.show();

	}  // end start()


	//   -------------------------- Nested class ----------------------------

	private static class GraphCanvas extends Canvas {

		// A object of this class can display the graph of a function
		// on the region of the (x,y)-plane given by -5 <= x <= 5 and
		// -5 <= y <= 5.  The graph is drawn very simply, by plotting
		// 301 points and connecting them with line segments.


		Expr func;  // The definition of the function that is to be graphed.
					// If the value is null, no graph is drawn.


		GraphCanvas(Expr firstFunction) {
			super(600,600);
			func = firstFunction;
			draw();  // Draw the canvas at startup.
		}


		public void setFunction(Expr exp) {
				// Set the canvas to graph the function whose definition is
				// given by the function exp.
			func = exp;
			draw();
		}


		public void clearFunction() {
				// Set the canvas to draw no graph at all.
			func = null;
			draw();
		}


		public void draw() {
				// Fill the canvas with white, then draw a set of axes
				// and the graph of the function.  Or, if func is null, 
				// display a message that there is no function to be graphed.
			GraphicsContext g = getGraphicsContext2D();
			g.setFill(Color.WHITE);
			g.fillRect(0,0,getWidth(),getHeight());
			
			if (func == null) {
				g.setFill( Color.RED );
				g.fillText("No function is available.", 30, 40);
			}
			else {
				g.setFill( Color.PURPLE );
				g.fillText("y = " + func, 5, 15);
				drawAxes(g);
				drawFunction(g);
			}
		}


		void drawAxes(GraphicsContext g) {
				// Draw horizontal and vertical axes in the middle of the
				// canvas.  A 5-pixel border is left at the ends of the axes.
			double width = getWidth();
			double height = getHeight();
			g.setStroke(Color.BLUE);
			g.setLineWidth(2);
			g.strokeLine(5, height/2, width-5, height/2);
			g.strokeLine(width/2, 5, width/2, height-5);
		}


		void drawFunction(GraphicsContext g) {
				// Draw the graph of the function defined by the instance 
				// variable func.  Just plot 301 points with lines 
				// between them. s

			double x, y;          // A point on the graph.  y is f(x).
			double prevx, prevy;  // The previous point on the graph.

			double dx;  // Difference between the x-values of consecutive 
						// points on the graph.

			dx  = 10.0 / 300;

			g.setStroke(Color.RED);
			g.setLineWidth(1);

			/* Compute the first point. */

			x = -5;
			y = func.value(x);

			/* Compute each of the other 300 points, and draw a line segment
               between each consecutive pair of points.  Note that if the
               function is undefined at one of the points in a pair, then 
               the line segment is not drawn.  */

			for (int i = 1; i <= 300; i++) {

				prevx = x;           // Save the coords of the previous point.
				prevy = y;

				x += dx;            // Get the coords of the next point.

				y = func.value(x);

				if ( (! Double.isNaN(y)) && (! Double.isNaN(prevy)) ) {
					   // Draw a line segment between the two points.
					putLine(g, prevx, prevy, x, y);
				}

			}  // end for

		}  // end drawFunction()


		void putLine(GraphicsContext g, double x1, double y1, 
				double x2, double y2) {
					// Draw a line segment from the point (x1,y1) to (x2,y2).
					// These values must be scaled to convert from coordinates
			        // that go from -5 to 5 to the coordinates that are needed
			        // for drawing on the canvas, which go from 0 to 600.
					// coordinates of the corresponding pixels.

			if (Math.abs(y1) > 10000 || Math.abs(y2) > 10000) {
					// Only draw lines for reasonable y-values.
					// This should not be necessary, but I'm not sure
				    // how GraphicsContext will handle very large values.
				return;
			}

			double a1, b1;   // Pixel coordinates corresponding to (x1,y1).
			double a2, b2;   // Pixel coordinates corresponding to (x2,y2).

			double width = getWidth();     // Width of the canvas (600).
			double height = getHeight();   // Height of the canvas (600).

			a1 = (int)( (x1 + 5) / 10 * width );
			b1 = (int)( (5 - y1) / 10 * height );
			a2 = (int)( (x2 + 5) / 10 * width );
			b2 = (int)( (5 - y2) / 10 * height );

			g.strokeLine(a1,b1,a2,b2);
			
		}  // end putLine()

	}  // end nested class GraphCanvas


} // end class SimpleGrapher
