
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This program shows an animation where 100 semi-transparent disks of
 * various sizes grow continually, disappearing before they get too big.
 * When a disk disappears, it is replaced by a new disk at another location.
 */
public class GrowingCircleAnimation extends JPanel implements ActionListener {
	
	private CircleInfo[] circleData; // holds the data for all 100 circles
	
	/**
	 *  Draw one frame of the animation.  If there is no disk data (which is
	 *  true for the first frame), 100 disks with random locations, colors,
	 *  and radii are created.  In each frame, all the disks grow by
	 *  one pixel per frame.  Disks sometimes disappear at random, or when
	 *  their radius reaches 100.  when a disk disappears, a new disk appears
	 *  with radius 1 and with a random location and color
	 */
	private void drawFrame(Graphics g, int frameNumber, int width, int height) {
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
			if (Math.random() < 0.01 || circleData[i].radius > 100) {
				    // replace circle number i with a new circle
				circleData[i] = new CircleInfo( 
						                (int)(width*Math.random()),
						                (int)(height*Math.random()),
						                1 );
			}
		}
		g.setColor(Color.BLACK);
		g.drawRect(0,0,width-1,height-1);  // Draw a frame (for the screenshot).
	}
	
	//------ Implementation details: DO NOT EXPECT TO UNDERSTAND THIS ------
	
	
	public static void main(String[] args) {
		JFrame window = new JFrame("Falling Circles");
		GrowingCircleAnimation drawingArea = new GrowingCircleAnimation();
		drawingArea.setBackground(Color.WHITE);
		window.setContentPane(drawingArea);
		drawingArea.setPreferredSize(new Dimension(600,480));
		window.pack();
		window.setLocation(100,50);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
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
