import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * A RandomArtPanel draws random pictures which might be taken to have
 * some vague resemblance to abstract art.  A new picture is produced every
 * four seconds.  There are three types of pictures:  random lines,
 * random circles, and random 3D rects.  The figures are drawn in
 * random colors on a background that is a random shade of gray.  The
 * data for a given piece of art is stored in a data structure so
 * that the picture can be redrawn if necessary.  The data is created
 * in response to the action event from a timer.
 */
public class RandomArt2 extends JPanel {

	/**
	 * A main routine to make it possible to run this program as an application.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Random Art ??");
		RandomArt2 content = new RandomArt2();
		window.setContentPane(content);
		window.setSize(400,400);
		window.setLocation(100,100);
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		window.setVisible(true);
	}

	//---------------------------------------------------------------------
	/**
	 * The data for the currently displayed picture (unless it is null).
	 */
	private ArtData artData;


	/**
	 * The constructor creates a timer with a delay time of four seconds
	 * (4000 milliseconds), and with a RepaintAction object as its
	 * ActionListener.  It also starts the timer running.  The
	 * RepaintAction class is a nested class, defined below.
	 */
	public RandomArt2() {
		RepaintAction action = new RepaintAction();
		Timer timer = new Timer(4000, action);
		timer.start();
	}


	/**
	 * The paintComponent() method fills the panel with a random shade of
	 * gray and then draws one of three types of random "art".  The data for
	 * the picture is in the variable artData (if this variable is null,
	 * then an artData object is created here).
	 */
	public void paintComponent(Graphics g) {

		if (artData == null)  // If no artdata has yet been created, create it.
			createArtData();

		// Note:  Since the next two lines fill the entire panel, there is
		// no need to call super.paintComponent(g), since any drawing
		// that it does will only be covered up anyway.

		g.setColor(artData.backgroundColor); // Fill with the art's background color.
		g.fillRect( 0, 0, getWidth(), getHeight() );

		artData.draw(g);  // Draw the art.

	} // end paintComponent()


	/**
	 * Creates an object belonging to one of the three subclasses of
	 * ArtData, and assigns that object to the instance variable, artData.
	 * The subclass to use (that is, the type of art) is chosen at random.
	 */
	private void createArtData() {
		switch ( (int)(3*Math.random()) ) {
		case 0:
			artData = new LineArtData();
			break;
		case 1:
			artData = new CircleArtData();
			break;
		case 2:
			artData = new SquareArtData();
			break;
		}
	}


	/**
	 * An abstract class that represents the data for a random work
	 * of "art".  Different concrete subclasses of this class represent
	 * different types of art.  This class contains a background
	 * color which is a random shade of gray, selected when the object
	 * is constructed.
	 */
	private abstract class ArtData {
		Color backgroundColor;  // The background color for the art.
		ArtData() {  // Constructor sets background color to be a random shade of gray.
			int x = (int)(256*Math.random());
			backgroundColor = new Color( x, x, x );
		}
		abstract void draw(Graphics g);  // Draw the picture.
	}


	/**
	 * Stores data for a picture that contains 500 random lines drawn in
	 * different random colors.
	 */
	private class LineArtData extends ArtData {

		Color[] color;         // color[i] is the color of line number i
		int[] x1, y1, x2, y2;  // line i goes from (x1[i],y1[i]) to (x2[i],y2[i]).
		LineArtData() {  // Constructor creates arrays and fills then randomly.
			color = new Color[500];
			x1 = new int[500];
			y1 = new int[500];
			x2 = new int[500];
			y2 = new int[500];
			for (int i = 0; i < 500; i++) {
				x1[i] = (int)(getWidth() * Math.random());
				y1[i] = (int)(getHeight() * Math.random());
				x2[i] = (int)(getWidth() * Math.random());
				y2[i] = (int)(getHeight() * Math.random());
				color[i] = Color.getHSBColor( (float)Math.random(), 1.0F, 1.0F);
			}
		}
		void draw(Graphics g) {  // Draw the picture.
			for (int i = 0; i < 500; i++) {
				g.setColor(color[i]);
				g.drawLine( x1[i], y1[i], x2[i], y2[i] );
			}
		}
	}


	/**
	 * Stores data for a picture that contains 200 circles with 
	 * radius 50, with random centers, and drawn in random colors.
	 */
	private class CircleArtData extends ArtData {
		class OneCircle {
			Color color;  // the color of the th circle
			int centerX;  // center of circle is at (centerX, centerY)
			int centerY;
		}
		ArrayList<OneCircle> circles;
		CircleArtData() {  // Constructor creates arrays and fills then randomly.
			circles = new ArrayList<OneCircle>();
			for (int i = 0; i < 200; i++) {
				OneCircle c = new OneCircle();
				c.centerX =  (int)(getWidth() * Math.random());
				c.centerY = (int)(getHeight() * Math.random());
				c.color = Color.getHSBColor( (float)Math.random(), 1.0F, 1.0F);
				circles.add(c);
			}
		}
		void draw(Graphics g) {  // Draw the picture.
			for (OneCircle circle : circles) {
				g.setColor(circle.color);
				g.drawOval(circle.centerX - 50, circle.centerY - 50, 100, 100);
			}
		}
	}


	/**
	 * Stores data for a picture that contains 25 filled squares with 
	 * random sizes and  with random centers, and drawn in random colors.
	 */
	private class SquareArtData extends ArtData {
		class OneSquare {
			Color color;  // the color of the square
			int centerX;  // the center of square is (centerX, centerY)
			int centerY; 
			int size;     // the length of a side of the square
		}
		ArrayList<OneSquare> squares = new ArrayList<OneSquare>();
		SquareArtData() {  // Constructor creates arrays and fills then randomly.
			for (int i = 0; i < 25; i++) {
				OneSquare s = new OneSquare();
				s.centerX =  (int)(getWidth() * Math.random());
				s.centerY = (int)(getHeight() * Math.random());
				s.size = 30 + (int)(170*Math.random());
				s.color = new Color( (int)(256*Math.random()), 
						(int)(256*Math.random()), (int)(256*Math.random()) );
				squares.add(s);
			}
		}
		void draw(Graphics g) {  // Draw the picture.
			for ( OneSquare square : squares ) {
				g.setColor(square.color);
				g.fill3DRect(square.centerX - square.size/2, square.centerY - square.size/2, 
						square.size, square.size, true);
			}
		}
	}


	/**
	 * A RepaintAction object creates a new artData object and calls the repaint 
	 * method of this panel each time its actionPerformed() method is called.  
	 * An object of this type is used as an action listener for a Timer that 
	 * generates an ActionEvent every four seconds.  The result is a new work of
	 * art every four seconds.
	 */
	private class RepaintAction implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			createArtData();
			repaint();
		}
	}


} // end class RandomArtPanel2

