
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  A very simple drawing program that lets the user add shapes to a drawing
 *  area and drag them around.  An abstract Shape class is used to represent
 *  shapes in general, with subclasses to represent particular kinds of shape.
 *  (These are implemented as nested classes inside the main class.)  This
 *  program is an illustration of class hierarchy, inheritance, polymorphism,
 *  and abstract classes.  (Note that this program will fail if you add more
 *  than 500 shapes, since it uses an array of length 500 to store the shapes.)
 */
public class ShapeDraw extends JPanel {
	
	/**
	 * A main routine that allows this class to be run as an application.
	 * It simply opens a window displaying a ShapeDraw panel.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Really Simple ShapeDraw");
		window.setContentPane( new ShapeDraw() );
		window.setSize(500,400);
		
		window.setLocation(150,100);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	

	/**
	 * Set up the GUI, with a drawing area plus a row of controls below the
	 * drawing area.  The controls include three buttons which are used
	 * to add shapes to the drawing area and a menu that is used to select
	 * the color of the shape when it is created.  The constructor also
	 * sets up listeners to handle events from the controls.
	 */
	public ShapeDraw() {  

		setBackground(Color.LIGHT_GRAY);

		DrawingArea canvas = new DrawingArea();  // create the canvas

		colorChoice = new JComboBox<String>();  // color choice menu
		colorChoice.addItem("Red");
		colorChoice.addItem("Green");
		colorChoice.addItem("Blue");
		colorChoice.addItem("Cyan");
		colorChoice.addItem("Magenta");
		colorChoice.addItem("Yellow");
		colorChoice.addItem("Black");
		colorChoice.addItem("White");
		colorChoice.addActionListener(canvas);

		JButton rectButton = new JButton("Rect");    // buttons for adding shapes
		rectButton.addActionListener(canvas);

		JButton ovalButton = new JButton("Oval");
		ovalButton.addActionListener(canvas);

		JButton roundRectButton = new JButton("RoundRect");
		roundRectButton.addActionListener(canvas);

		JPanel bottom = new JPanel();   // a Panel to hold the control buttons
		bottom.setLayout(new GridLayout(1,4,3,3));
		bottom.add(rectButton);
		bottom.add(ovalButton);
		bottom.add(roundRectButton);
		bottom.add(colorChoice);

		setLayout(new BorderLayout(3,3));
		add(canvas, BorderLayout.CENTER);              // add canvas and controls to the panel
		add(bottom, BorderLayout.SOUTH);

	} // end constructor

	JComboBox<String> colorChoice;  // The color selection menu

	//---- Nested class definitions ---
	//
	// The remainder of the ShapeDraw class consists of static nested class definitions.
	// These are just like regular classes, except that they are defined inside
	// another class (and hence have full names, when used outside this class, such
	// as ShapeDraw.ShapeCanvas).

	/**
	 * This nested class defines the drawing area.
	 */
	class DrawingArea extends JPanel
	                 implements ActionListener, MouseListener, MouseMotionListener {

		// This class represents a "canvas" that can display colored shapes and
		// let the user drag them around.  It uses an off-screen images to 
		// make the dragging look as smooth as possible.

		Shape[] shapes = new Shape[500]; // holds a list of up to 500 shapes
		int shapeCount = 0;  // the actual number of shapes
		Color currentColor = Color.RED;  // current color; when a shape is created, this is its color


		DrawingArea() {
			   // Constructor: set background color to white set up listeners to respond to mouse actions
			setBackground(Color.WHITE);
			addMouseListener(this);
			addMouseMotionListener(this);
		}   

		public void paintComponent(Graphics g) {
			   // In the paint method, all the shapes in ArrayList are
			   // copied onto the canvas.
			g.setColor(Color.WHITE);
			g.fillRect(0,0,getSize().width,getSize().height);
			for (int i = 0; i < shapeCount; i++) {
				Shape s = shapes[i];
				s.draw(g);
			}
			g.setColor(Color.BLACK);  // draw a black border around the edge of the drawing area
			g.drawRect(0,0,getWidth()-1,getHeight()-1);
		}   

		public void actionPerformed(ActionEvent evt) {
			   // Called to respond to action events.  The three shape-adding
			   // buttons have been set up to send action events to this canvas.
			   // Respond by adding the appropriate shape to the canvas.
			if (evt.getSource() == colorChoice) {
				switch ( colorChoice.getSelectedIndex() ) {
				case 0: currentColor = Color.RED;     break;
				case 1: currentColor = Color.GREEN;   break;
				case 2: currentColor = Color.BLUE;    break;
				case 3: currentColor = Color.CYAN;    break;
				case 4: currentColor = Color.MAGENTA; break;
				case 5: currentColor = Color.YELLOW;  break;
				case 6: currentColor = Color.BLACK;   break;
				case 7: currentColor = Color.WHITE;   break;
				}
			}
			else {
				String command = evt.getActionCommand();
				if (command.equals("Rect"))
					addShape(new RectShape());
				else if (command.equals("Oval"))
					addShape(new OvalShape());
				else if (command.equals("RoundRect"))
					addShape(new RoundRectShape());
			}
		}

		void addShape(Shape shape) {
				// Add the shape to the canvas, and set its size/position and color.
				// The shape is added at the top-left corner, with size 80-by-50.
				// Then redraw the canvas to show the newly added shape.
			shape.setColor(currentColor);
			shape.reshape(3,3,80,50);
			shapes[shapeCount] = shape;
			shapeCount++;
			repaint();
		}


		// -------------------- This rest of this class implements dragging ----------------------

		Shape shapeBeingDragged = null;  // This is null unless a shape is being dragged.
										 // A non-null value is used as a signal that dragging
										 // is in progress, as well as indicating which shape
										 // is being dragged.

		int prevDragX;  // During dragging, these record the x and y coordinates of the
		int prevDragY;  //    previous position of the mouse.

		public void mousePressed(MouseEvent evt) {
				// User has pressed the mouse.  Find the shape that the user has clicked on, if
				// any.  If there is a shape at the position when the mouse was clicked, then
				// start dragging it.  If the user was holding down the shift key, then bring
				// the dragged shape to the front, in front of all the other shapes.
			int x = evt.getX();  // x-coordinate of point where mouse was clicked
			int y = evt.getY();  // y-coordinate of point 
			for ( int i = shapeCount - 1; i >= 0; i-- ) {  // check shapes from front to back
				Shape s = shapes[i];
				if (s.containsPoint(x,y)) {
					shapeBeingDragged = s;
					prevDragX = x;
					prevDragY = y;
					if (evt.isShiftDown()) { // s should be moved on top of all the other shapes
						for (int j = i; j < shapeCount-1; j++) {
								// move the shapes following s down in the list
							shapes[j] = shapes[j+1];
						}
						shapes[shapeCount-1] = s;  // put s at the end of the list
						repaint();  // repaint canvas to show s in front of other shapes
					}
					return;
				}
			}
		}

		public void mouseDragged(MouseEvent evt) {
				// User has moved the mouse.  Move the dragged shape by the same amount.
			int x = evt.getX();
			int y = evt.getY();
			if (shapeBeingDragged != null) {
				shapeBeingDragged.moveBy(x - prevDragX, y - prevDragY);
				prevDragX = x;
				prevDragY = y;
				repaint();      // redraw canvas to show shape in new position
			}
		}

		public void mouseReleased(MouseEvent evt) {
				// User has released the mouse.  Move the dragged shape, then set
				// shapeBeingDragged to null to indicate that dragging is over.
			int x = evt.getX();
			int y = evt.getY();
			if (shapeBeingDragged != null) {
				shapeBeingDragged.moveBy(x - prevDragX, y - prevDragY);
				shapeBeingDragged = null;
				repaint();
			}
		}

		public void mouseEntered(MouseEvent evt) { }   // Other methods required for MouseListener and 
		public void mouseExited(MouseEvent evt) { }    //              MouseMotionListener interfaces.
		public void mouseMoved(MouseEvent evt) { }
		public void mouseClicked(MouseEvent evt) { }

	}  // end class DrawingArea

	

	// ------- Nested class definitions for the abstract Shape class and three -----
	// -------------------- concrete subclasses of Shape. --------------------------


	static abstract class Shape {

			// A class representing shapes that can be displayed on a ShapeCanvas.
			// The subclasses of this class represent particular types of shapes.
			// When a shape is first constructed, it has height and width zero
			// and a default color of white.

		int left, top;      // Position of top left corner of rectangle that bounds this shape.
		int width, height;  // Size of the bounding rectangle.
		Color color = Color.white;  // Color of this shape.

		void reshape(int left, int top, int width, int height) {
				// Set the position and size of this shape.
			this.left = left;
			this.top = top;
			this.width = width;
			this.height = height;
		}

		void moveBy(int dx, int dy) {
				// Move the shape by dx pixels horizontally and dy pixels vertically
				// (by changing the position of the top-left corner of the shape).
			left += dx;
			top += dy;
		}

		void setColor(Color color) {
				// Set the color of this shape
			this.color = color;
		}

		boolean containsPoint(int x, int y) {
				// Check whether the shape contains the point (x,y).
				// By default, this just checks whether (x,y) is inside the
				// rectangle that bounds the shape.  This method should be
				// overridden by a subclass if the default behavior is not
				// appropriate for the subclass.
			if (x >= left && x < left+width && y >= top && y < top+height)
				return true;
			else
				return false;
		}

		abstract void draw(Graphics g);  
			// Draw the shape in the graphics context g.
			// This must be overriden in any concrete subclass.

	}  // end of class Shape



	static class RectShape extends Shape {
			// This class represents rectangle shapes.
		void draw(Graphics g) {
			g.setColor(color);
			g.fillRect(left,top,width,height);
			g.setColor(Color.black);
			g.drawRect(left,top,width,height);
		}
	}


	static class OvalShape extends Shape {
			// This class represents oval shapes.
		void draw(Graphics g) {
			g.setColor(color);
			g.fillOval(left,top,width,height);
			g.setColor(Color.black);
			g.drawOval(left,top,width,height);
		}
		boolean containsPoint(int x, int y) {
				// Check whether (x,y) is inside this oval, using the
				// mathematical equation of an ellipse.  This replaces the
			    // definition of containsPoint that was inherited from the
			 	// Shape class.
			double rx = width/2.0;   // horizontal radius of ellipse
			double ry = height/2.0;  // vertical radius of ellipse 
			double cx = left + rx;   // x-coord of center of ellipse
			double cy = top + ry;    // y-coord of center of ellipse
			if ( (ry*(x-cx))*(ry*(x-cx)) + (rx*(y-cy))*(rx*(y-cy)) <= rx*rx*ry*ry )
				return true;
			else
				return false;
		}
	}


	static class RoundRectShape extends Shape {
			// This class represents rectangle shapes with rounded corners.
			// (Note that it uses the inherited version of the 
			// containsPoint(x,y) method, even though that is not perfectly
			// accurate when (x,y) is near one of the corners.)
		void draw(Graphics g) {
			g.setColor(color);
			g.fillRoundRect(left,top,width,height,width/3,height/3);
			g.setColor(Color.black);
			g.drawRoundRect(left,top,width,height,width/3,height/3);
		}
	}


}  // end class ShapeDraw

