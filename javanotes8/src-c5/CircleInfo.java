import java.awt.Color;
import java.awt.Graphics;

/**
 * A simple class that holds the size, color, and location of a colored disk,
 * with a method for drawing the circle in a graphics context.  The circle
 * is drawn as a filled oval, with a black outline.
 */
public class CircleInfo {
	
    public int radius;     // The radius of the circle.
    public int x,y;       // The location of the center of the circle.
    public Color color;   // The color of the circle.
    
    /**
     * Create a CircleInfo with a given location and radius and with a
     * randomly selected, semi-transparent color.
     * @param centerX   The x coordinate of the center.
     * @param centerY   The y coordinate of the center.
     * @param rad       The radius of the circle.
     */
    public CircleInfo( int centerX, int centerY, int rad ) {
		x = centerX;
		y = centerY;
    	radius = rad;
		int red = (int)(255*Math.random());
		int green = (int)(255*Math.random());
		int blue = (int)(255*Math.random());
		color = new Color(red,green,blue, 100);
    }
    
    /**
     * Draw the disk in graphics context g, with a black outline.
     */
    public void draw( Graphics g ) {
    	g.setColor( color );
    	g.fillOval( x - radius, y - radius, 2*radius, 2*radius );
    	g.setColor( Color.BLACK );
    	g.drawOval( x - radius, y - radius, 2*radius, 2*radius );
    }
}
