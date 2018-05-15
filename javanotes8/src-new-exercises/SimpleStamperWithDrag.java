import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A simple demonstration of MouseEvents.  Shapes are drawn
 * on a black background when the user clicks the panel.  If
 * the user Shift-clicks, the panel is cleared.  If the user
 * right-clicks the panel, a blue oval is drawn.  Otherwise,
 * when the user clicks, a red rectangle is drawn.  The contents of
 * the panel are not persistent.  For example, they might disappear 
 * if the panel is resized or is covered and uncovered.
 * This class has a main() routine to allow it to be run as an application.
 */
public class SimpleStamperWithDrag extends JPanel 
                               implements MouseListener, MouseMotionListener {

	public static void main(String[] args) {
		JFrame window = new JFrame("Simple Stamper");
		SimpleStamperWithDrag content = new SimpleStamperWithDrag();
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(120,70);
		window.setSize(400,300);
		window.setVisible(true);
	}

	// ----------------------------------------------------------------------

	/**
	 * This variable is set to true during a drag operation, unless the
	 * user was holding down the shift key when the mouse was first
	 * pressed (since in that case, the mouse gesture simply clears the
	 * panel and no figures should be drawn if the user drags the mouse).
	 */
	private boolean dragging;
	

	/**
	 * This constructor simply sets the background color of the panel to be black
	 * and sets the panel to listen for mouse events on itself.
	 */
	public SimpleStamperWithDrag() {
		setBackground(Color.WHITE);
		addMouseListener(this);
		addMouseMotionListener(this);
	}


	/**
	 *  Since this panel has been set to listen for mouse events on itself, 
	 *  this method will be called when the user clicks the mouse on the panel.
	 *  This method is part of the MouseListener interface.
	 */
	public void mousePressed(MouseEvent evt) {

		if ( evt.isShiftDown() ) {
				// The user was holding down the Shift key.  Just repaint the panel.
				// Since this class does not define a paintComponent() method, the 
				// method from the superclass, JPanel, is called.  That method simply
				// fills the panel with its background color, which is black.  The 
				// effect is to clear the panel.
			dragging = false;
			repaint();
			return;
		}

		dragging = true;

		int x = evt.getX();  // x-coordinate where user clicked.
		int y = evt.getY();  // y-coordinate where user clicked.

		Graphics g = getGraphics();  // Graphics context for drawing directly.
		                             // NOTE:  This is considered to be bad style!

		if ( evt.isMetaDown() ) {
				// User right-clicked at the point (x,y). Draw a blue oval centered 
				// at the point (x,y). (A black outline around the oval will make it 
				// more distinct when shapes overlap.)
			g.setColor(Color.BLUE);  // Blue interior.
			g.fillOval( x - 30, y - 15, 60, 30 );
			g.setColor(Color.BLACK); // Black outline.
			g.drawOval( x - 30, y - 15, 60, 30 );
		}
		else {
				// User left-clicked (or middle-clicked) at (x,y). 
				// Draw a red rectangle centered at (x,y).
			g.setColor(Color.RED);   // Red interior.
			g.fillRect( x - 30, y - 15, 60, 30 );
			g.setColor(Color.BLACK); // Black outline.
			g.drawRect( x - 30, y - 15, 60, 30 );
		}

		g.dispose();  // We are finished with the graphics context, so dispose of it.
		

	} // end mousePressed();


	/**
	 *  This method is called when the user drags the mouse.  If a the value of the
	 *  instance variable dragging is true, it will draw a rect or oval at the
	 *  current mouse position.
	 */
	public void mouseDragged(MouseEvent evt) {
		if ( dragging == false ) { 
			return;
		}
		int x = evt.getX();  // x-coordinate where user clicked.
		int y = evt.getY();  // y-coordinate where user clicked.
		Graphics g = getGraphics();  // Graphics context for drawing directly.
		                             // NOTE:  This is considered to be bad style!
		if ( evt.isMetaDown() ) {
			    // The user is using the right mouse button; draw an oval.
			g.setColor(Color.BLUE);  // Blue interior.
			g.fillOval( x - 30, y - 15, 60, 30 );
			g.setColor(Color.BLACK); // Black outline.
			g.drawOval( x - 30, y - 15, 60, 30 );
		}
		else {
			g.setColor(Color.RED);   // Red interior.
			g.fillRect( x - 30, y - 15, 60, 30 );
			g.setColor(Color.BLACK); // Black outline.
			g.drawRect( x - 30, y - 15, 60, 30 );
		}
		g.dispose();  // We are finished with the graphics context, so dispose of it.
	} // end mouseDragged();


	// The next four empty routines are required by the MouseListener interface.
	// They don't do anything in this class, so their definitions are empty.

	public void mouseEntered(MouseEvent evt) { }
	public void mouseExited(MouseEvent evt) { }
	public void mouseClicked(MouseEvent evt) { }
	public void mouseReleased(MouseEvent evt) { }

	// The next routines is required by the MouseMotionListener interface.

	public void mouseMoved(MouseEvent evt) { }

} // end class SimpleStamper