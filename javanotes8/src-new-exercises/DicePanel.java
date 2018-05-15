import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Shows a pair of dice that are rolled when the user clicks on the
 * applet.  It is assumed that the panel is 100-by-100 pixels.
 */
public class DicePanel extends JPanel {
 
	/**
	 * A main routine allows this class to be run as an application.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Dice");
		DicePanel content = new DicePanel();
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(120,70);
		window.pack();
		window.setVisible(true);
	}

	//---------------------------------------------------------------------


	private int die1 = 4;  // The values shown on the dice.
    private int die2 = 3;
 
    /**
     *  The constructor adds a mouse listener to the panel.  The listener
     *  will roll the dice when the user clicks the panel.  Also, the
     *  background color and the preferred size of the panel are set.
     */
    public DicePanel() {
       setPreferredSize( new Dimension(100,100) );
       setBackground( new Color(200,200,255) );  // light blue
       addMouseListener( new MouseAdapter() {
           public void mousePressed(MouseEvent evt) {
               roll();
           }
       });
    }
    
    /**
     * Draw a die with upper left corner at (x,y).  The die is
     * 35 by 35 pixels in size.  The val parameter gives the
     * value showing on the die (that is, the number of dots).
     */
    private void drawDie(Graphics g, int val, int x, int y) {
       g.setColor(Color.white);
       g.fillRect(x, y, 35, 35);
       g.setColor(Color.black);
       g.drawRect(x, y, 34, 34);
       if (val > 1)  // upper left dot
          g.fillOval(x+3, y+3, 9, 9);
       if (val > 3)  // upper right dot
          g.fillOval(x+23, y+3, 9, 9);
       if (val == 6) // middle left dot
          g.fillOval(x+3, y+13, 9, 9);
       if (val % 2 == 1) // middle dot (for odd-numbered val's)
          g.fillOval(x+13, y+13, 9, 9);
       if (val == 6) // middle right dot
          g.fillOval(x+23, y+13, 9, 9);
       if (val > 3)  // bottom left dot
          g.fillOval(x+3, y+23, 9, 9);
       if (val > 1)  // bottom right dot
          g.fillOval(x+23, y+23, 9,9);
    }
 
 
    /**
     * Roll the dice by randomizing their values.  Tell the
     * system to repaint the applet, to show the new values.
     */
    void roll() {
       die1 = (int)(Math.random()*6) + 1;
       die2 = (int)(Math.random()*6) + 1;
       repaint();
    }
    
    
    /**
     * The paintComponent method just draws the two dice and draws
     * a one-pixel wide blue border around the panel.  Antialiasing
     * is turned on to make the ovals look nicer.
     */
    public void paintComponent(Graphics g) {
       super.paintComponent(g);  // fill with background color.
       Graphics2D g2 = (Graphics2D)g;
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                   RenderingHints.VALUE_ANTIALIAS_ON);
       g.setColor( Color.BLUE );
       g.drawRect(0,0,99,99);
       g.drawRect(1,1,97,97);
       drawDie(g, die1, 10, 10);
       drawDie(g, die2, 55, 55);
    }
 
} // end class DicePanel