
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This program displays 25 copies of a message, and it runs
 * an animation in which the strings move around on the screen.
 * The color, position, and velocity of each message is selected 
 * at random when the program first starts, and there is a button that
 * the user can click to reinitialize all the random values.  The
 * RandomeStringsWithArray class defines the JPanel that is used
 * for drawing and that occupies the entire content of the program
 * window.
 *  
 *  (NOTE FOR LINUX USERS:  To get smooth animation, you might need
 *  to run this program using   java -Dsun.java2d.opengl=true SimpleAnimationStarter 
 *  or, alternatively, add System.setProperty("sun.java2d.opengl", "true");
 *  as the very first line of the main() routine in this program.)
 */
public class RandomStringsWithArray extends JPanel {

	/**
	 * This main routine makes it possible to run RandomStringsWithArray
	 * as a program.  When it is run, it creates a window that contains a
	 * RandomStringsWithArray as its content pane.
	 */
	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");
		JFrame window = new JFrame("Moving Strings");
		RandomStringsWithArray content = new RandomStringsWithArray();
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(120,70);
		window.pack();
		window.setVisible(true);
	}

	//----------------------------------------------
	
	private final static String MESSAGE = "Hello Java"; 

	private final static Font[] fonts = new Font[] { // The five fonts
			new Font("Serif", Font.BOLD, 20),
			new Font("Monospace", Font.BOLD | Font.ITALIC, 28),
			new Font("SansSerif", Font.PLAIN, 32),
			new Font("Dialog", Font.ITALIC, 40),
			new Font("Serif",  Font.BOLD | Font.ITALIC, 60)
		};
	
	private StringData[] stringData;  // Holds all information needed
	                                  // to draw the strings.
	
	private long previousFrameTime; // When previous frame of animation was drawn.
	
	private static class StringData {  // Info needed to draw one string.
		double x,y;    // location of the string
		double dx,dy;  // velocity of the string, in pixels per second
		Color color;   // color of the string
		Font font;     // the font that is used to draw the string
	}
	
	

	/**
	 * Constructor creates a panel representing a large display area,
	 * where random strings are drawn.  Starts an animation that shows
	 * the string moving around in the panel.  Adds a mouse listener
	 * so that when the user clicks the panel, the data for the
	 * strings will be recreated with new random values.
	 */
	public RandomStringsWithArray() {

		setPreferredSize(new Dimension(500,300));
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
		
		addMouseListener ( new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				createStringData();
			}
		} );
		
		Timer timer = new Timer(17, e -> {
				// The timer will run continually.  In each frame, all the strings
				// will be moved, and the panel will be redrawn.
			long time = System.currentTimeMillis();
			if (previousFrameTime > 0 && stringData != null) {
					// Time since previous call to handle is (time - previousTime),
					// in milliseconds.  Dividing by 1000 converts milliseconds to seconds.
					// The first time handle() is called, previousTime is 0 and the
					// update is not done.  (The test that stringData is not null
					// is required, since this event handler can be called before
					// the stringData has been created.)
				updateStringData( (time - previousFrameTime)/1000.0 );
			}
			repaint();
			previousFrameTime = time;
		} );
		timer.setInitialDelay(250);
		timer.start();
	}
	
	
	/**
	 * Creates an array of 25 StringData objects and fills it with
	 * randomly generated data for each of the 25 strings.  This is
	 * called in the start() method and when the user clicks the
	 * "Restart!" button.
	 */
	private void createStringData() {
		stringData = new StringData[25];
		for (int i = 0; i < 25; i++) {
			stringData[i] = new StringData();
			stringData[i].x = getWidth() * Math.random();
			stringData[i].y = getHeight() * Math.random();
			stringData[i].dx = 50 + 150*Math.random(); // 50 to 200 pixels per second
			if (Math.random() < 0.5) // 50% chance that dx is negative
				stringData[i].dx = -stringData[i].dx;
			stringData[i].dy = 50 + 150*Math.random();
			if (Math.random() < 0.5) // 50% chance that dy is negative
				stringData[i].dy = -stringData[i].dy;
			stringData[i].color = Color.getHSBColor( (float)Math.random(), 1, 1 );
			stringData[i].font = fonts[ (int)(5*Math.random()) ];
		}
	}

	
	/**
	 * Update the data for the 25 strings by moving each string, where the
	 * distance moved depends on the velocity.  If a string moves too far
	 * off the panel, move it to the opposite side of the panel.
	 * (To make sure a string has moved all the way off the panel to the
	 * left, wait until data.x reaches -400 before moving it to the
	 * right of the panel. 
	 * @param deltaTimeInSeconds time that has elapsed since the previous
	 *          call to updateStringData, measured in seconds.
	 */
	private void updateStringData(double deltaTimeInSeconds) {
		for ( StringData data : stringData ) {
			data.x += data.dx * deltaTimeInSeconds;
			data.y += data.dy * deltaTimeInSeconds;
			if (data.x < -400)
				data.x = getWidth();
			if (data.x > getWidth()+10)
				data.x = -400;
			if (data.y < -10)
				data.y = getHeight() + 70;
			if (data.y > getHeight() + 80)
				data.y = -10;
		}
	}

	/**
	 * This is the method that draws the content of the panel,
	 * showing 25 random strings.
	 */
	protected void paintComponent(Graphics g) {
		if (stringData == null) {
			// Creates string data the first time the panel is drawn.
			createStringData(); // 
		}
		super.paintComponent(g);
		int width = getWidth();
		int height = getHeight();

		g.setColor( Color.WHITE );  // fill with white background
		g.fillRect(0, 0, width, height);

		for ( StringData data : stringData ) {
			// Draw one string, using the properties in one of the
			// StringData objects from the array.
			g.setColor( data.color );
			g.setFont( data.font);
			g.drawString(MESSAGE, (int)data.x, (int)data.y);

		} // end for
	}

}  // end class RandomStringsWithArray
