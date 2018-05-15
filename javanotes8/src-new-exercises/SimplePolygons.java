import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * This program lets the user draw colored polygons.
 * The user inputs a polygon by clicking a series of points.
 * The points are connected with lines from each point to the
 * next Clicking near the starting point (within 3 pixels) or
 * right-clicking (or Command-clicking) will complete the
 * polygon, so the user can begin a new one.  As soon as the
 * user begins drawing a new polygon, the old one is discarded.
 */
public class SimplePolygons extends JPanel implements MouseListener {

	/**
	 * A main() routine to allow this program to be run as an application.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("SimplePolygons");
		SimplePolygons content = new SimplePolygons();
		window.setContentPane(content);
		window.pack();
		window.setLocation(100,100);
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		window.setResizable(false);  
		window.setVisible(true);
	}


	/* Variables for implementing polygon input. */

	private int[] xCoord, yCoord;   // Arrays containing the points of 
									//   the polygon.  Up to 500 points 
									//   are allowed.

	private int pointCt;  // The number of points that have been input.

	private boolean complete;   // Set to true when the polygon is complete.
								// When this is false, only a series of lines are drawn.
								// When it is true, a filled polygon is drawn.

	private final static Color polygonColor = Color.RED;  
	// Color that is used to draw the polygons.  


	/**
	 * Initialize the panel and its data; add a black border and set
	 * the panel to listen for mouse events on itself.  Also sets
	 * the preferred size of the panel to be 300-by-300.
	 */
	public SimplePolygons() {
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
		setPreferredSize( new Dimension(300,300) );
		addMouseListener(this);
		xCoord = new int[500];
		yCoord = new int[500];
		pointCt = 0;
		complete = false;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (pointCt == 0)
			return;
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke( new BasicStroke(2) );
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (pointCt == 1) {
			g.fillRect(xCoord[0], yCoord[0], 2, 2);
		}
		else if (complete) { // draw a polygon
			g.setColor(polygonColor);
			g.fillPolygon(xCoord, yCoord, pointCt);
			g.setColor(Color.BLACK);
			g.drawPolygon(xCoord, yCoord, pointCt);
		}
		else { // draw a series of lines
			for (int i = 0; i < pointCt - 1; i++)
				g.drawLine( xCoord[i], yCoord[i], xCoord[i+1], yCoord[i+1]);
		}
	}


	/**
	 * Processes a mouse click.
	 */
	public void mousePressed(MouseEvent evt) { 

		if (complete) {
				// Start a new polygon at the point that was clicked.
			complete = false;
			xCoord[0] = evt.getX();
			yCoord[0] = evt.getY();
			pointCt = 1;
		}
		else if ( pointCt > 1 && pointCt > 0 && (Math.abs(xCoord[0] - evt.getX()) <= 3)
				&& (Math.abs(yCoord[0] - evt.getY()) <= 3) ) {
				// User has clicked near the starting point.
				// The polygon is complete.
			complete = true;
		}
		else if (evt.isMetaDown() || pointCt == 500) {
			    // The polygon is complete.
			complete = true;
		}
		else {
				// Add the point where the user clicked to the list of
				// points in the polygon, and draw a line between the
				// previous point and the current point.  A line can
				// only be drawn if there are at least two points.
			xCoord[pointCt] = evt.getX();
			yCoord[pointCt] = evt.getY();
			pointCt++;
		}
		repaint();  // in all cases, repaint.
	} // end mousePressed()

	public void mouseReleased(MouseEvent evt) { }
	public void mouseClicked(MouseEvent evt) { }
	public void mouseEntered(MouseEvent evt) { }
	public void mouseExited(MouseEvent evt) { }


}  // end class SimplePolygons

