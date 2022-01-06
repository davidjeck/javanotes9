import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This class demos a Canvas that is resized when the Pane that
 * contains it is resized.  (The Pane changes size when the user
 * changes the window size.)  This is done by binding the
 * width property of the Canvas to the width property of the
 * Pane and the height property of the Canvas to the height
 * property of the Pane.
 * 
 * The program shows an animation of red disks bouncing around 
 * inside the canvas.  When the canvas size increases, the balls 
 * spread out into the new space.  When the canvas size decreases, 
 * the balls become trapped in the smaller space.  This shows
 * that the canvas really does change size.
 */
public class CanvasResizeDemo extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//----------------------------------------------------------------

	private Canvas canvas;
	private GraphicsContext g;
	private BouncingBall[] balls;

	public void start(Stage stage) {
		
		canvas = new Canvas(640,480);
		g = canvas.getGraphicsContext2D();
		
		balls = new BouncingBall[50];
		for (int i = 0; i < 50; i++)
			balls[i] = new BouncingBall();
		
		/* When the user clicks or drags the mouse on the canvas,
		 * the velocities of all of the balls are changed so that
		 * they head towards the mouse position. */
		
		canvas.setOnMousePressed( e -> {
			double x = e.getX();
			double y = e.getY();
			for (BouncingBall b : balls)
				b.headTowards(x,y);
		});
		canvas.setOnMouseDragged( e -> {
			double x = e.getX();
			double y = e.getY();
			for (BouncingBall b : balls)
				b.headTowards(x,y);
		});
		
		Pane root = new Pane(canvas);
		stage.setScene( new Scene(root) );
		stage.setTitle("Resizable Canvas Demo");
		stage.setMinWidth(60);
		stage.setMinHeight(100);
		stage.show();
		
		
		/* Set up binding to keep canvas the same size as the Pane
		 * that contains it.  Note that this must be done after the 
		 * stage is shown, or canvas size will be set to zero. */
		
		canvas.widthProperty().bind(root.widthProperty()); 
		canvas.heightProperty().bind(root.heightProperty());
		
		
		/* Start a timer that will continually update the positions
		 * of the balls and redraw the canvas.  (There is no need
		 * to redraw the canvas when it changes size, since it
		 * will be redrawn by the timer in any case.) */
		
		AnimationTimer timer = new AnimationTimer() {
			long previousTime = 0;
			public void handle(long time) {
				    // Move all the balls, except no motion the
				    // first time that handle() is called.
				if (previousTime > 0) {
					double width = canvas.getWidth();
					double height = canvas.getHeight();
					for (BouncingBall b: balls) {
						b.move(width,height,(time - previousTime)/1.0e9);
					}
				}
				redraw();
				previousTime = time;
			}
		};
		timer.start();

	}
	
	/**
	 * Draw the canvas with the balls in their current positions.
	 */
	private void redraw() { 
		double width = canvas.getWidth();
		double height = canvas.getHeight();
		g.setFill(Color.WHITE);
		g.fillRect(0,0,width,height);
		g.setFill(Color.RED);
		for (BouncingBall b: balls) {
			g.fillOval(b.x-b.radius, b.y-b.radius, 2*b.radius, 2*b.radius);
		}
	}

	/**
	 * Represents a red disk that bounces around in the canvas.  The constructor
	 * makes a disk with radius 10 and center at the center of the canvas.  Note
	 * that the canvas must exist before the constructor is called.  The
	 * new disk is given a random velocity in the range 100 to 400 pixels per second.
	 */
	private class BouncingBall {
		double x,y;      // center of the disk
		double radius;   // radius of the disk
		double dx,dy;    // velocity in pixels per second
		BouncingBall() {
			x = canvas.getWidth()/2;
			y = canvas.getHeight()/2;
			this.radius = 10;
			double velocity = 100 + 300*Math.random();
			double angle = 2 * Math.PI * Math.random();
			dx = velocity*Math.cos(angle);
			dy = velocity*Math.sin(angle);
		}
		void move(double canvasWidth, double canvasHeight, double elapsedTimeInSeconds) {
			    // Move the ball by an amount equal to its velocity,
			    // multiplied by elapsed time since the previous frame.
			    // If it crosses an edge of the canvas, it is moved
			    // back into the canvas and its velocity is reversed.
			    // If it is outside the canvas because the canvas has
			    // shrunk, rather than because it crosses an edge,
			    // then it merely heads back in the direction of
			    // the canvas.
			double w = canvasWidth;
			double h = canvasHeight;
			x += dx * elapsedTimeInSeconds;
			y += dy * elapsedTimeInSeconds;
			if (x < radius) { 
					// bounce off left edge
				x = 2*radius - x;
				dx = Math.abs(dx);
			}
			else if (x - dx < w - radius && x > w - radius) { 
					// bounce off right edge
				x = 2 * (w - radius) - x;
				dx = -Math.abs(dx);
			}
			else if (x > w - radius) { 
					// Disk is outside the right edge but didn't move there.
					// Presumably this happened because canvas got smaller.
				dx = -Math.abs(dx);  // head back towards canvas.
			}
			if (y < radius) { 
				y = 2*radius - y;
				dy = Math.abs(dy);
			}
			else if (y - dy < h - radius && y > h - radius) { 
				y = 2 * (h - radius) - y;
				dy = -Math.abs(dy);
			}
			else if (y > h - radius) { 
				dy = -Math.abs(dy);
			}
		}
		void headTowards( double a, double b ) {
			    // Reset the direction in which the ball is moving so
			    // that its new velocity vector points in the direction
			    // from its current location to (a,b).  The speed is
			    // not changed, only the direction,
			if (Math.abs(a-x) < 1e-6 && Math.abs(b-y) < 1e-6)
				return;
			double velocity = Math.sqrt(dx*dx + dy*dy);
			double vecx = a - x;
			double vecy = b - y;
			double length = Math.sqrt(vecx*vecx + vecy*vecy);
			double dirx = vecx/length; // unit vector in direction from (x,y) to (a,b)
			double diry = vecy/length;
			dx = velocity * dirx;
			dy = velocity * diry;
		}
	}
	
} // end class CanvasResizeDemo
