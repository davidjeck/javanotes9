
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
	 * Draws a set of nested rectangles. This subroutine is called about 60 times
	 * second.  The drawing area has already been cleared to the background color. 
	 * The parameter g is used for drawing. The frameNumber starts at zero and
	 * increases by 1 each time this subroutine is called.  The parameters width
	 * and height give the size of the drawing area, in pixels.  
	 * The sizes and positions of the rectangles that are drawn depend
	 * on the frame number, giving the illusion of motion.
	 */
	public void drawFrame(Graphics g, int frameNumber, double elapsedSeconds, int width, int height) {
		
		int inset; // Gap between edges of drawing area and the outer rectangle.

		int rectWidth, rectHeight;   // The size of one of the rectangles.

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
		JFrame window = new JFrame("Simple Animation");
		
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
		drawingArea.setPreferredSize(new Dimension(800,600));

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
		
		/* NOTE:  In the next line, the number 17 gives the time between
		 * calls to drawFrame().  The time is given in milliseconds, where
		 * one second equals 1000 milliseconds.  You can increase this number
		 * to get a slower animation.  You can decrease it somewhat to get a
		 * faster animation, but the speed is limited by the time it takes
		 * for the computer to draw each frame. 
		 */
		Timer frameTimer = new Timer(17,drawingArea);
		frameTimer.setInitialDelay(500);

		window.setVisible(true);
		frameTimer.start();

	} // end main

	private int frameNum;
	private long elapsedTime;
	private long startTime = -1;
	
	public void actionPerformed(ActionEvent evt) {
		repaint();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		frameNum++;
		if (startTime == -1) {
			elapsedTime = 0;
			startTime = System.currentTimeMillis();
		}
		else {
			elapsedTime = System.currentTimeMillis() - startTime;
		}
		drawFrame(g, frameNum, elapsedTime/1000.0, getWidth(), getHeight());
	}

}
