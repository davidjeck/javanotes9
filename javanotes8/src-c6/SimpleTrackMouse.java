
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * A SimpleTrackMousePanel is a panel that displays information about mouse
 * events on the panel, including the type of event, the position of the mouse,
 * a list of modifier keys that were down when the event occurred, and an indication
 * of which mouse button was involved, if any.
 */
public class SimpleTrackMouse extends JPanel {

	public static void main(String[] args) {
		JFrame window = new JFrame("Click Me to Redraw");
		SimpleTrackMouse content = new SimpleTrackMouse();
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(120,70);
		window.setSize(450,350);
		window.setVisible(true);
	}

	// --------------------------------------------------------------------------------

	private String eventType = null;     // If non-null, gives the type of the most recent mouse event.
	private String modifierKeys = "";    // If non-empty, gives special keys that are held down.
	private String button = "";          // Information about which mouse button was used.
	private int mouseX, mouseY;          // Position of mouse (at most recent mouse event).


	/**
	 * Constructor creates a mouse listener object and sets it to listen for
	 * mouse events and mouse motion events on the panel.
	 */
	public SimpleTrackMouse() { 
			// Set background color and arrange for the panel to listen for mouse events.
		setBackground(Color.WHITE);
		MouseHandler listener = new MouseHandler();
		addMouseListener(listener);        // Register mouse listener.
		addMouseMotionListener(listener);  // Register mouse motion listener.
	}


	/**
	 * Records information about a mouse event on the panel.  This method is called
	 * by the mouse handler object whenever a mouse event occurs.
	 * @param evt the MouseEvent object for the event.
	 * @param eventType a description of the type of event, such as "mousePressed".
	 */
	private void setInfo(MouseEvent evt, String eventType) {
		this.eventType = eventType;
		mouseX = evt.getX();
		mouseY = evt.getY();
		modifierKeys = "";
		if (evt.isShiftDown())
			modifierKeys += "Shift  ";
		if (evt.isControlDown())
			modifierKeys += "Control  ";
		if (evt.isMetaDown())
			modifierKeys += "Meta  ";
		if (evt.isAltDown())
			modifierKeys += "Alt";
		switch ( evt.getButton() ) {
		case MouseEvent.BUTTON1:
			button = "Left";
			break;
		case MouseEvent.BUTTON2:
			button = "Middle";
			break;
		case MouseEvent.BUTTON3:
			button = "Right";
			break;
		default:
			button = "";
		}
		repaint();
	}


	/**
	 * The paintComponent() method displays information about the most recent
	 * mouse event on the panel (as set by the setInfo() method).
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);  // Fills panel with background color.

		if (eventType == null) {
				// If eventType is null, no mouse event has yet occurred 
				// on the panel, so don't display any information.
			return;
		}

		g.setColor(Color.RED);  // Display information about the mouse event.
		g.drawString("Mouse event type:  " + eventType, 6, 18);
		if (modifierKeys.length() > 0)
			g.drawString("Modifier keys:  " + modifierKeys, 6, 38);
		else
			g.drawString("Modifier keys:  None", 6, 38);
		if (button.length() > 0)
			g.drawString("Button used:  " + button, 6, 58);
		g.setColor(Color.BLACK);
		g.drawString("(" + mouseX + "," + mouseY + ")", mouseX, mouseY);

	}  


	/**
	 * An object belonging to class MouseHandler listens for mouse events
	 * on the panel.  (Listening is set up in the constructor for the
	 * SimpleTrackMousePanel class.)  When a mouse event occurs, the listener
	 * simply calls the setInfo() method in the SimpleMouseTrackPanel class
	 * with information about the mouse event that has occurred.
	 */
	private class MouseHandler implements MouseListener, MouseMotionListener {

		public void mousePressed(MouseEvent evt) {
			setInfo(evt, "mousePressed");
		}

		public void mouseReleased(MouseEvent evt) {
			setInfo(evt, "mouseReleased");
		}

		public void mouseClicked(MouseEvent evt) {
			setInfo(evt, "mouseClicked");
		}

		public void mouseEntered(MouseEvent evt) {
			setInfo(evt, "mouseEntered");
		}

		public void mouseExited(MouseEvent evt) {
			setInfo(evt, "mouseExited");
		}

		public void mouseMoved(MouseEvent evt) {
			setInfo(evt, "mouseMoved");
		}

		public void mouseDragged(MouseEvent evt) {
			setInfo(evt, "mouseDragged");
		}

	}  // end nested class MouseHandler

}  // end of class SimpleMouseTracker

