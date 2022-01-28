
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This program shows an animation where 100 semi-transparent disks of
 * various sizes grow continually, disappearing before they get too big.
 * When a disk disappears, it is replaced by a new disk at another location.
 * This program uses class CircleInfo, defined in CircleInfo.java.
 *  
 *  (NOTE FOR LINUX USERS:  To get smooth animation, you might need
 *  to run this program using   java -Dsun.java2d.opengl=true SimpleAnimationStarter 
 *  or, alternatively, add System.setProperty("sun.java2d.opengl", "true");
 *  as the very first line of the main() routine in this program.)
 */
public class GrowingCircleAnimation extends JPanel implements ActionListener {

	private CircleInfo[] circleData; // holds the data for all 100 circles
	
	/**
	 *  Draw one frame of the animation.  If there is no disk data (which is
	 *  true for the first frame), 100 disks with random locations, colors,
	 *  and radii are created.  In each frame, all the disks grow by
	 *  one pixel per frame.  Disks sometimes disappear at random, or when
	 *  their radius reaches 100.  When a disk disappears, a new disk appears
	 *  with radius 1 and with a random location and color
	 */
	public void drawFrame(Graphics g, int frameNumber, double elapsedSeconds, int width, int height) {
		g.setColor(Color.WHITE);
		g.fillRect(0,0,width,height);
		if (circleData == null) {  // create the array, if it doesn't exist
			circleData = new CircleInfo[100];
			for (int i = 0; i < circleData.length; i++) {
				circleData[i] = new CircleInfo( 
										(int)(width*Math.random()),
										(int)(height*Math.random()),
										(int)(100*Math.random()) );
			}
		}
		for (int i = 0; i < circleData.length; i++) {  // draw the filled circles
			circleData[i].radius++;
			circleData[i].draw(g);
			if (Math.random() < 0.005 || circleData[i].radius > 100) {
				    // replace circle number i with a new circle
				circleData[i] = new CircleInfo( 
						                (int)(width*Math.random()),
						                (int)(height*Math.random()),
						                1 );
			}
		}
	}
	
	//------ Implementation details: DO NOT EXPECT TO UNDERSTAND THIS ------
	
	
	public static void main(String[] args) {
		
		/* NOTE:  The string in the following statement goes in the title bar
		 * of the window.
		 */
		JFrame window = new JFrame("Growing Circles");
		
		/*
		 * NOTE: If you change the name of this class, you must change
		 * the name of the class in the next line to match!
		 */
		GrowingCircleAnimation drawingArea = new GrowingCircleAnimation();
		
		drawingArea.setBackground(Color.WHITE);
		window.setContentPane(drawingArea);

		/* NOTE:  In the next line, the numbers 600 and 480 give the
		 * initial width and height of the drawing array.  You can change
		 * these numbers to get a different size.
		 */
		drawingArea.setPreferredSize(new Dimension(600,480));

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
