
/**
 * This program shows a panel containing a button that jumps
 * to a random location when the user moves the move over it,
 * making it almost impossible to click (but you can cheat
 * by pressing the button, which is another way of triggering it).
 * 
 * The program demonstrates several things:  using a null layout,
 * responding to "mouseEntered" events, changing the position
 * of a button while the program is running, using a lambda
 * expression to define an ActionListener for the button,
 * using an anonymous subclass of MouseAdapter to respond to
 * mouseEntered events, and changing the text color and font
 * on a button.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BashfulButton extends JPanel {

	/**
	 * Main routine just opens a window that shows a BashfulButton panel.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Try to click the button!");
		BashfulButton content = new BashfulButton();
		window.setContentPane(content);
		window.pack();  // Use preferred size of content to set size of window.
		window.setResizable(false);  // User can't change the window's size.
		window.setLocation(100,100);
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		window.setVisible(true);
	}

	//-------------------------------------------------------------------------


	/**
	 * The constructor sets the layout manager for the panel to be null.
	 * It adds four components to the panel and sets their bounds explicitly.
	 * It is assumed that the panel has a fixed size of 520-by-420 pixels;
	 * it sets the preferred size of the panel to be that size.
	 */
	public BashfulButton() {

		setLayout(null);  // I will do the layout myself!

		setBackground(new Color(210,180,140));  // A tan background.

		setBorder( BorderFactory.createEtchedBorder() ); 

		setPreferredSize( new Dimension(600,400) );

		/* Create a button and place it in the center of the panel. */
		
		JButton button = new JButton("Click Me!");
		button.setForeground( Color.RED );
		button.setFont( new Font( "Serif", Font.BOLD, 18 ));
		add(button);
		button.setBounds( 225, 180, 150, 40);
		
		/* Respond to an ActionEvent from the button by showing
		   a message dialog. */
		
		button.addActionListener( evt -> 
				JOptionPane.showMessageDialog(this, "You Got Me!") );
	
		/* Respond to a mouseEntered event by moving the button to a
		   random location, with the x and y coordinates chosen to make
		   sure that the button lies entirely withing the panel. */
		
		button.addMouseListener( new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				int w = (int)(450*Math.random());
				int h = (int)(360*Math.random());
				button.setLocation(w, h);
			}
		});

	}


} // end class BashfulButton




