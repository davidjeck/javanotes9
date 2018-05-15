
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  When run as a program, this class opens a window on the screen that
 *  shows a set of nested rectangles that seems to be moving infinitely 
 *  inward towards the center.  The animation continues until the user
 *  closes the window.
 */
public class MovingRects extends JPanel implements ActionListener {

	/**
	 * Draws a set of nested rectangles. This subroutine is called 50 times per
	 * second and is responsible for redrawing the entire drawing area.  The
	 * parameter g is used for drawing. The frameNumber starts at zero and
	 * increases by 1 each time this subroutine is called.  The parameters width
	 * and height give the size of the drawing area, in pixels.  
	 * The sizes and positions of the rectangles that are drawn depend
	 * on the frame number, giving the illusion of motion.
	 */
	public void drawFrame(Graphics g, int frameNumber, int width, int height) {
		
		/* NOTE:  To get a different animation, just erase the contents of this 
		 * subroutine and substitute your own.  If you don't fill the picture
		 * with some other color, the background color will be white.
		 */

		int inset; // Gap between edges of drawing area and the outer rectangle.

		int rectWidth, rectHeight;   // The size of one of the rectangles.

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0,0,width,height);  // Fill drawing area with light gray.

		g.setColor(Color.BLACK);  // Draw the rectangles in black.

		inset = frameNumber % 15;

		rectWidth = width - 2*inset;
		rectHeight = height - 2*inset;

		while (rectWidth >= 0 && rectHeight >= 0) {
			g.drawRect(inset, inset, rectWidth, rectHeight);
			inset += 15;       // rectangles are 15 pixels apart
			rectWidth -= 30;
			rectHeight -= 30;
		}
	}
	
	//------ Implementation details: DO NOT EXPECT TO UNDERSTAND THIS ------
	
	
	public static void main(String[] args) {
		
		/* NOTE:  The string in the following statement goes in the title bar
		 * of the window.
		 */
		JFrame window = new JFrame("Infinite motion");

		/*
		 * NOTE: If you change the name of this class, you must change
		 * the name of the class in the next line to match!
		 */
		MovingRects drawingArea = new MovingRects();
		drawingArea.setBackground(Color.WHITE);
		window.setContentPane(drawingArea);

		/* NOTE:  In the next line, the numbers 600 and 450 give the
		 * initial width and height of the drawing array.  You can change
		 * these numbers to get a different size.
		 */
		drawingArea.setPreferredSize(new Dimension(600,450));

		window.pack();
		window.setLocation(100,50);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		/*
		 * Note:  In the following line, you can change true to
		 * false.  This will prevent the user from resizing the window,
		 * so you can be sure that the size of the drawing area will
		 * not change.  It can be easier to draw the frames if you know
		 * the size.
		 */
		window.setResizable(true);
		
		/* NOTE:  In the next line, the number 20 gives the time between
		 * calls to drawFrame().  The time is given in milliseconds, where
		 * one second equals 1000 milliseconds.  You can increase this number
		 * to get a slower animation.  You can decrease it somewhat to get a
		 * faster animation, but the speed is limited by the time it takes
		 * for the computer to draw each frame. 
		 */
		Timer frameTimer = new Timer(20,drawingArea);

		window.setVisible(true);
		frameTimer.start();

	} // end main

	private int frameNum;
	
	public void actionPerformed(ActionEvent evt) {
		frameNum++;
		repaint();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawFrame(g, frameNum, getWidth(), getHeight());
	}

}
