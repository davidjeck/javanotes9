import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.URL;

/**
 * A short demo program that can show several different cursors, including 
 * a custom cursor that is created from an image resource.  A main() routine 
 * allows this class to be run as a stand-alone application.  desktop.)
 */
public class CursorDemo extends JPanel {
	
	/**
	 * The main routine simply opens a window that shows a CursorDemo panel.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("CursorDemo");
		CursorDemo content = new CursorDemo();
		window.setContentPane(content);
		window.pack();  
		window.setResizable(false); 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation( (screenSize.width - window.getWidth())/2,
				(screenSize.height - window.getHeight())/2 );
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		window.setVisible(true);
	}
	
	
	public CursorDemo() {
		setLayout(new GridLayout(4,1,3,3));
		setBackground(Color.GRAY);
		setBorder(BorderFactory.createLineBorder(Color.GRAY,3));
		JButton button;
		ButtonHandler listener = new ButtonHandler();
		button = new JButton("Use Crosshair Cursor");
		button.addActionListener(listener);
		add(button);
		button = new JButton("Use Wait Cursor");
		button.addActionListener(listener);
		add(button);
		button = new JButton("Use Move Cursor");
		button.addActionListener(listener);
		add(button);
		button = new JButton("Use Custom Cursor");
		button.addActionListener(listener);
		add(button);
	}
	
	
	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			String command = evt.getActionCommand();
			if (command.equals("Use Wait Cursor"))
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			else if (command.equals("Use Move Cursor"))
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			else if (command.equals("Use Crosshair Cursor"))
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			else if (command.equals("Use Custom Cursor"))
				useCustomCursor("TinySmiley.png");
		}
	}
	

	/**
	 * Sets the current cursor for this component to be a custom
	 * cursor defined by an image resource.  If the image resource
	 * can't be found, then the component's cursor is not changed.
	 * (For this demo, the hotspot of the cursor is always set to
	 * be the point (7,7).)
	 */
	private void useCustomCursor(String imageResourceName) {
		ClassLoader cl = getClass().getClassLoader();
		URL resourceURL = cl.getResource(imageResourceName);
		if (resourceURL != null) {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Image image = toolkit.createImage(resourceURL);
			Point hotSpot = new Point(7,7);
			Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "smiley");
			setCursor(cursor);
		}
	}

}
