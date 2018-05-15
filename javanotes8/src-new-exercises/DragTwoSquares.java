import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * A panel showing a red square and a blue square that the user
 * can drag with the mouse.   The user can drag the squares off
 * the panel and drop them.  There is no way of getting them back.
 */
public class DragTwoSquares extends JPanel {


	/**
	 * A main routine allows this class to be run as an application.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Drag Either Square");
		DragTwoSquares content = new DragTwoSquares();
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(120,70);
		window.setSize(400,300);
		window.setVisible(true);
	}

	//---------------------------------------------------------------------


	private int x1, y1;   // Coords of top-left corner of the red square.
	private int x2, y2;   // Coords of top-left corner of the blue square.


	/**
	 *  The constructor places the two squares in their initial positions and
	 *  sets up listening for mouse events and mouse motion events.
	 */
	public DragTwoSquares() {

		x1 = 10;  // Set up initial positions of the squares.
		y1 = 10;
		x2 = 50;
		y2 = 10;

		setBackground(Color.LIGHT_GRAY);  // Set up appearance of the panel
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1) );

		Dragger listener = new Dragger();  // Listening object, belonging to a nested
		//     class that is defined below.

		addMouseListener(listener);        // Set up listening.
		addMouseMotionListener(listener);

	} 


	/**
	 * paintComponent just draws the two squares in their current positions.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);  // Fill with background color.
		g.setColor(Color.RED);
		g.fillRect(x1, y1, 30, 30);
		g.setColor(Color.BLUE);
		g.fillRect(x2, y2, 30, 30);
	}


	/**
	 *  This private class is used to define the listener that listens
	 *  for mouse events and mouse motion events on the panel.
	 */
	private class Dragger implements MouseListener, MouseMotionListener {

		/* Some variables used during dragging */

		boolean dragging;      // Set to true when a drag is in progress.

		boolean dragRedSquare; // True if red square is being dragged, false
							   //    if blue square is being dragged.

		int offsetX, offsetY;  // Offset of mouse-click coordinates from the
							   //   top-left corner of the square that was
							   //   clicked.

		/**
		 * Respond when the user presses the mouse on the panel.
		 * Check which square the user clicked, if any, and start
		 * dragging that square.
		 */
		public void mousePressed(MouseEvent evt) { 

			if (dragging)  // Exit if a drag is already in progress.
				return;

			int x = evt.getX();  // Location where user clicked.
			int y = evt.getY();

			if (x >= x2 && x < x2+30 && y >= y2 && y < y2+30) {
					// It's the blue square (which should be checked first,
					// since it's drawn on top of the red square.)
				dragging = true;
				dragRedSquare = false;
				offsetX = x - x2;  // Distance from corner of square to (x,y).
				offsetY = y - y2;
			}
			else if (x >= x1 && x < x1+30 && y >= y1 && y < y1+30) {
					// It's the red square.
				dragging = true;
				dragRedSquare = true;
				offsetX = x - x1;  // Distance from corner of square to (x,y).
				offsetY = y - y1;
			}

		}

		/**
		 * Dragging stops when user releases the mouse button.
		 */
		public void mouseReleased(MouseEvent evt) { 
			dragging = false;
		}

		/**
		 * Respond when the user drags the mouse.  If a square is 
		 * not being dragged, then exit. Otherwise, change the position
		 * of the square that is being dragged to match the position
		 * of the mouse.  Note that the corner of the square is placed
		 * in the same relative position with respect to the mouse that i
		 * had when the user started dragging it.
		 */
		public void mouseDragged(MouseEvent evt) { 

			if (dragging == false)  
				return;
			int x = evt.getX();
			int y = evt.getY();
			if (dragRedSquare) {  // Move the red square.
				x1 = x - offsetX;
				y1 = y - offsetY;
			}
			else {   // Move the blue square.
				x2 = x - offsetX;
				y2 = y - offsetY;
			}
			repaint();  // (Calls the repaint() method in the DragTwoSquaresPanel class.)
		}


		public void mouseMoved(MouseEvent evt) { }  // empty methods required by interfaces.
		public void mouseClicked(MouseEvent evt) { }
		public void mouseEntered(MouseEvent evt) { }
		public void mouseExited(MouseEvent evt) { }

	} // end nested class Dragger


} // end class DragTwoSquaresPanel
