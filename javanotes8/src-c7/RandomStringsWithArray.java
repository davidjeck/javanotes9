import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This panel displays several copies of a message.  The color and 
 * position of each message is selected at random.  The font
 * of each message is randomly chosen from among five possible
 * fonts.  The messages are displayed on a black background.
 * The program uses an array to store the information needed
 * to draw the strings.
 * 
 * This class also contains a main() routine so that it can be run
 * as a program.
 */
public class RandomStringsWithArray extends JPanel {

	/**
	 * This main routine makes it possible to run RandomStringsWithArray
	 * as a program.  When it is run, it creates a window that contains a
	 * RandomStringsWithArray as its content pane.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Hello World");
		RandomStringsWithArray content = new RandomStringsWithArray("Hello!");
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(120,70);
		window.setSize(350,250);
		window.setVisible(true);
	}

	
	//----------------------------------------------------------------------------
	
	/**
	 * An object of this type holds the position, color, and font
	 * of one copy of the string.
	 */
	private static class StringData {
	   int x, y;     // The coordinates of the left end of baseline of string.
	   Color color;  // The color in which the string is drawn.
	   Font font;    // The font that is used to draw the string.
	}
	
	private final int MESSAGE_COUNT = 25;
	
	private String message;  // The message to be displayed.  This can be set in
	                         // the constructor.  If no value is provided in the
	                         // constructor, then the string "Java!" is used.

	private Font[] fonts; // An array to hold the five fonts that are available.
	
	private StringData[] stringData;  // Holds the data for drawing all the strings.

	/**
	 * Default constructor creates a panel that displays the message "Java!".
	 */
	public RandomStringsWithArray() {
		this(null);  // Call the other constructor, with parameter null.
	}

	/**
	 * Constructor creates a panel to display 25 copies of a specified message.
	 * @param messageString The message to be displayed.  If this is null,
	 * then the default message "Java!" is displayed.
	 */
	public RandomStringsWithArray(String messageString) {

		message = messageString;
		if (message == null)
			message = "Java!";

		fonts = new Font[5];  // Create the array to hold the five fonts.
		 
		fonts[0] = new Font("Serif", Font.BOLD, 14);
		fonts[1] = new Font("SansSerif", Font.BOLD + Font.ITALIC, 24);
		fonts[2] = new Font("Monospaced", Font.PLAIN, 20);
		fonts[3] = new Font("Dialog", Font.PLAIN, 30);
		fonts[4] = new Font("Serif", Font.ITALIC, 36);
		
		setBackground(Color.BLACK);
		
		addMouseListener( new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				   // In response to a mouse click, create new data for the strings,
				   // and call repaint to apply the change to what is shown on screen.
				createStringData();
				repaint();
			}
		});
		
	}
	
	/**
	 * Creates the stringData array with random data that will be used to draw
	 * the strings.  This method will be called from paintComponent to create the
	 * first set of data.  It is also called by a MouseListener in response 
	 * to a mouse click on the panel, so that the click will result in a
	 * new set of data.  Note:  This method cannot be called from the constructor
	 * since the width and height of the panel are not known when the constructor
	 * is called, and the width and height are used in computing the coordinates
	 * for the positions of the strings.
	 */
	private void createStringData() {
		int width = getWidth();
		int height = getHeight();
		stringData = new StringData[MESSAGE_COUNT];
		for (int i = 0; i < MESSAGE_COUNT; i++) {
			    // Create an object to represent the data for string number i,
			    // and fill it with random values.
			stringData[i] = new StringData();
			int fontIndex = (int)(Math.random() * 5);
			stringData[i].font = fonts[fontIndex]; // one of 5 fonts, selected at random
			float hue = (float)Math.random();
			stringData[i].color = Color.getHSBColor(hue, 1.0F, 1.0F);  // random color
			stringData[i].x = -50 + (int)(Math.random()*(width+40));  // random x-coord
			stringData[i].y = (int)(Math.random()*(height+20));  // random y-coord
		}
	}

	/**
	 * The paintComponent method is responsible for drawing the content of the panel.
	 * It draws 25 copies of the message string, using a random color, font, and
	 * position for each string.
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);  // Call the paintComponent method from the 
		                          // superclass, JPanel.  This simply fills the 
		                          // entire panel with the background color, black.
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, 
                                RenderingHints.VALUE_ANTIALIAS_ON );
		
		if ( stringData == null ) {
			    // If the data for the strings has not already been created, do it now.
			    // This will only be the case the first time paintComponent() is called.
			createStringData();
		}

		for (int i = 0; i < MESSAGE_COUNT; i++) {
			g.setFont( stringData[i].font );
			g.setColor( stringData[i].color );
			g.drawString( message, stringData[i].x, stringData[i].y );

		} // end for

	} // end paintComponent()


}  // end class RandomStringsWithArray
