import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This program demonstrates cyclic and oscillating animations.  For cyclic animation
 * repeats itself every N frames, for some value of N.  An oscillating animation also
 * repeats, but the repetition is "back-and-forth."  That is, the second half
 * of the repeated animation is the same as the first half played backwards.
 */
public class CyclicAndOscillatingMotionDemo extends JPanel implements ActionListener {

	public void drawFrame(Graphics g, int frameNumber, int width, int height) {

		
		/* Show cyclic motion at three speeds.  In each case, a square 
		 * moves across the drawing area from left to right, then jumps
		 * back to the start.
		 */

		int cyclicFrameNum;
		
		cyclicFrameNum = frameNumber % 300;  // Repeats every 300 frames
		g.setColor(Color.RED);
		g.fillRect( cyclicFrameNum, 0, 20, 20 );
		
		cyclicFrameNum = frameNumber % 150;  // Repeats every 150 frames
		g.setColor(Color.GREEN);
		g.fillRect( 2*cyclicFrameNum, 20, 20, 20 );
		
		cyclicFrameNum = frameNumber % 100;  // Repeats every 100 frames
		g.setColor(Color.BLUE);
		g.fillRect( 3*cyclicFrameNum, 40, 20, 20 );
		

		/* Show oscillating motion at three speeds.  In each case, a square 
		 * moves across the drawing area from left to right, then reverses
		 * direction to move from right to left back to its starting point.
		 */
		
		int oscillationFrameNum;
		
		oscillationFrameNum = frameNumber % 600;  // repeats every 600 frames
		if (oscillationFrameNum > 300)
			oscillationFrameNum = 600 - oscillationFrameNum; // after 300, the values go backwards back to 0
		g.setColor(Color.CYAN);
		g.fillRect( oscillationFrameNum, 60, 20, 20 );
		
		oscillationFrameNum = frameNumber % 300; // repeats every 300 frames
		if (oscillationFrameNum > 150)
			oscillationFrameNum = 300 - oscillationFrameNum; // after 150, the values go backwards back to 0
		g.setColor(Color.MAGENTA);
		g.fillRect( 2*oscillationFrameNum, 80, 20, 20 );
		
		oscillationFrameNum = frameNumber % 200; // repeats every 200 frames
		if (oscillationFrameNum > 100)
			oscillationFrameNum = 200 - oscillationFrameNum; // after 100, the values go backwards back to 0
		g.setColor(Color.YELLOW);
		g.fillRect( 3*oscillationFrameNum, 100, 20, 20 );
		
		
		/* Draw horizontal black lines across the window to separate the
		 * regions used by the six squares.  Also draw a box around the outside,
		 * mostly for the picture that I need for the web page!
		 */
		
		int y;
		g.setColor(Color.BLACK);
		for ( y = 20; y < 120; y = y + 20 )
			g.drawLine(0,y,320,y);
		g.drawRect(0,0,319,119);  // Why not (0,0,320,120)? This is a technicality.
		                          // If you use 320 and 120, the right and bottom edges
		                          // of the rect will actually be outside the drawing area.		
	}
	
	//------ Implementation details: DO NOT EXPECT TO UNDERSTAND THIS ------
	
	
	public static void main(String[] args) {
		
		JFrame window = new JFrame("Cyclic and Oscillating Motion");
		CyclicAndOscillatingMotionDemo drawingArea = new CyclicAndOscillatingMotionDemo();
		drawingArea.setBackground(Color.WHITE);
		window.setContentPane(drawingArea);
		drawingArea.setPreferredSize(new Dimension(320,120));  // size is 320 by 120
		window.pack();
		window.setLocation(100,50);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false); // The user can't change the size.
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
