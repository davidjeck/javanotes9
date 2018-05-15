
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  When run as a program, this class opens a window on the screen that
 *  shows a large number of colored disks.  The positions of the disks
 *  are selected at random, and the color is randomly selected from
 *  red, green, or blue.  A black outline is drawn around each disk.
 *  The picture changes every three seconds.
 */
public class RandomCircles extends JPanel implements ActionListener {

	/**
	 * Draws 500 disks with random colors and locations.
	 * Each disk has a radius of 50 pixels.  This subroutine is
	 * called every three seconds, giving a new set of disks.
	 */
	public void drawFrame(Graphics g, int frameNumber, int width, int height) {
		
		int centerX;     // The x-coord of the center of a disk.
		int centerY;     // The y-coord of the center of a disk.
		int colorChoice; // Used to select a random color.
		int count;       // Loop control variable for counting disks.
		
		for (count = 0; count < 500; count++) {
			
			colorChoice = (int)(3*Math.random());
			switch (colorChoice) {
			case 0:
				g.setColor(Color.RED);
				break;
			case 1:
				g.setColor(Color.GREEN);
				break;
			case 2:
				g.setColor(Color.BLUE);
				break;
			}
			
			centerX = (int)(width*Math.random());
			centerY = (int)(height*Math.random());
			
			g.fillOval( centerX - 50, centerY - 50, 100, 100 );
			g.setColor(Color.BLACK);
			g.drawOval( centerX - 50, centerY - 50, 100, 100 );
			
		}
	}
	
	//------ Implementation details: DO NOT EXPECT TO UNDERSTAND THIS ------
	
	
	public static void main(String[] args) {
		
		/* NOTE:  The string in the following statement goes in the title bar
		 * of the window.
		 */
		JFrame window = new JFrame("Random Disks");
		
		/*
		 * NOTE: If you change the name of this class, you must change
		 * the name of the class in the next line to match!
		 */
		RandomCircles drawingArea = new RandomCircles();

		drawingArea.setBackground(Color.WHITE);
		window.setContentPane(drawingArea);

		/* NOTE:  In the next line, the numbers 500 and 500 give the
		 * initial width and height of the drawing array.  You can change
		 * these numbers to get a different size.
		 */
		drawingArea.setPreferredSize(new Dimension(500,500));

		window.pack();
		window.setLocation(100,50);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		/*
		 * Note:  In the following line, you can change false to
		 * true.  This will prevent the user from resizing the window,
		 * so you can be sure that the size of the drawing area will
		 * not change.  It can be easier to draw the frames if you know
		 * the size.
		 */
		window.setResizable(false);
		
		/* NOTE:  In the next line, the number 3000 gives the time between
		 * calls to drawFrame().  The time is given in milliseconds, where
		 * one second equals 1000 milliseconds.  You can change the number
		 * to control the animation speed.
		 */
		Timer frameTimer = new Timer(3000,drawingArea);

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
