
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  This file can be used to create very simple animations.  Just fill in
 *  the definition of drawFrame with the code to draw one frame of the 
 *  animation, and possibly change a few of the values in the rest of
 *  the program as noted below.
 *  
 *  (NOTE FOR LINUX USERS:  To get smooth animation, you might need
 *  to run this program using   java -Dsun.java2d.opengl=true SimpleAnimationStarter 
 *  or, alternatively, add System.setProperty("sun.java2d.opengl", "true");
 *  as the very first line of the main() routine in this program.)
 */
public class SimpleAnimationStarter extends JPanel implements ActionListener {

	/**
	 * Draws one frame of an animation. This subroutine should be called
	 * about 60 times per second.  It is responsible for redrawing the
	 * entire drawing area. The parameter g is used for drawing. The frameNumber 
	 * starts at zero and increases by 1 each time this subroutine is called.  
	 * The parameter elapsedSeconds gives the number of seconds since the animation
	 * was started.  By using frameNumber and/or elapsedSeconds in the drawing
	 * code, you can make a picture that changes over time.  That's an animation.
	 * The parameters width and height give the size of the drawing area, in pixels.  
	 * Note that the drawing area is automatically filled with the background color
	 * before this method is called.
	 */
	public void drawFrame(Graphics g, int frameNumber, double elapsedSeconds, int width, int height) {
		
		/* NOTE:  To get a different animation, just erase the contents of this 
		 * subroutine and substitute your own.  If you don't fill the picture
		 * with some other color, the background color will be white.  The sample
		 * code here just shows the frame number.
		 */
		
		g.drawString( "Frame number " + frameNumber, 40, 50 );
		g.drawString( String.format("Elapsed Time: %1.1f seconds", elapsedSeconds), 40, 80);

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
		SimpleAnimationStarter drawingArea = new SimpleAnimationStarter();
		
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
