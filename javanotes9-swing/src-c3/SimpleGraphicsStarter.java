
import java.awt.*;
import javax.swing.*;

/**
 *  This file can be used to draw simple pictures.  Just fill in
 *  the definition of drawPicture with the code that draws your picture.
 */
public class SimpleGraphicsStarter extends JPanel {

	/**
	 * Draws a picture.  The parameters width and height give the size 
	 * of the drawing area, in pixels.  The drawing area is automatically
	 * filled with the background color before this subroutine is called.
	 */
	public void drawPicture(Graphics g, int width, int height) {
				
		// As an example, draw a large number of colored disks.
		// To get a different picture, just erase all the code
		// inside drawPicture(), and substitute your own. 
		
		int centerX;     // The x-coord of the center of a disk.
		int centerY;     // The y-coord of the center of a disk.
		int colorChoice; // Used to select a random color.
		int count;       // Loop control variable for counting disks.
		
		for (count = 0; count < 500; count++) {
			
			colorChoice = (int)(4*Math.random());
			switch (colorChoice) {
				case 0 -> g.setColor(Color.RED);
				case 1 -> g.setColor(Color.GREEN);
				case 2 -> g.setColor(Color.BLUE);
				case 3 -> g.setColor(Color.YELLOW);
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
		JFrame window = new JFrame("Simple Graphics");
		
		/*
		 * NOTE: If you change the name of this class, you must change
		 * the name of the class in the next line to match!
		 */
		SimpleGraphicsStarter drawingArea = new SimpleGraphicsStarter();

		drawingArea.setBackground(Color.WHITE);  // Set background color for the picture.
		window.setContentPane(drawingArea);

		/* NOTE:  In the next line, the numbers 800 and 600 give the
		 * initial width and height of the drawing array.  You can change
		 * these numbers to get a different size.
		 */
		drawingArea.setPreferredSize(new Dimension(800,600));

		window.pack();
		window.setLocation(100,50);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		window.setVisible(true);

	} // end main

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// The following strange line will give a nicer, less jagged appearance
		// to lines and circles that are drawn using g.
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		drawPicture(g, getWidth(), getHeight());
	}

}
