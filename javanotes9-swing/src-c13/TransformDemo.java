import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates Transforms.  Transforms controlled by sliders
 * are applied to an image in the order scale, shear, rotate, translate.
 * There is also a final translation that moves the origin to the center
 * of the panel, so that the transformations specified by the sliders 
 * are applied with the origin in the center.  That is, the center of
 * the canvas is the center for scaling, shear, and rotation; those
 * transformations leave the center fixed.  This program requires an
 * image resource file named face-smile.png.
 */
public class TransformDemo extends JPanel {

	public static void main(String[] args) {
		JFrame window = new JFrame("TransformDemo");
		TransformDemo content = new TransformDemo();
		window.setContentPane(content);
		window.pack();  
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation( (screenSize.width - window.getWidth())/2,
				(screenSize.height - window.getHeight())/2 );
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		window.setVisible(true);
	}
	//----------------------------------------------------------------------------


	private Display canvas;  // The JPanel where the figures are drawn.

	private JSlider scaleXSlider;  // Controls scaling in horizontal direction.
	private JSlider scaleYSlider;  // Controls scaling in vertical direction.
	private JSlider shearSlider;  // Controls horizontal shear.
	private JSlider rotateSlider;  // Controls angle of rotation.
	private JSlider translateXSlider;  // Controls horizontal translation.
	private JSlider translateYSlider;  // Controls vertical translation.
	
	private JButton resetButton;  // Resets all sliders to default value.
	                             // Transformation is the identity.
	
	private GradientPaint gradient;  // A linear gradient paint.
	
	private Image smiley;  // A small smiley face (from Gnome desktop)
	

	/**
	 * Defines the drawing area where the transformed shapes are drawn.
	 */
	private class Display extends JPanel {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.translate( canvas.getWidth()/2, canvas.getHeight()/2 );  // move origin to center
			
			/*  Apply transforms specified by the sliders, with the origin as the center
			 *  point for scaling, rotation and shear. */
			
			g2.translate( translateXSlider.getValue(), translateYSlider.getValue() );
			g2.rotate( rotateSlider.getValue()*Math.PI/180 );
			g2.shear(shearSlider.getValue()/100.0, 0);
			g2.scale( scaleXSlider.getValue()/100.0, scaleYSlider.getValue()/100.0);
			
			/* Draw some objects, subject to the transforms. */
			
			g2.setFont(new Font("Serif", Font.BOLD, 48));
			g2.setColor(Color.BLACK);
			g2.drawString("Hello World",0,0);
			g2.setStroke( new BasicStroke(5) );
			g2.setColor(Color.RED);
			g2.drawRect(-200,-150,150,100);
			g2.setPaint(gradient);
			g2.fillOval(-200,50,150,100);
			g2.setStroke( new BasicStroke(2) );
			g2.setColor(Color.BLACK);
			g2.drawOval(-200,50,150,100);
			g2.setColor(Color.GREEN);
			g2.fillRoundRect(20, 20, 200, 100, 20, 20);
			g2.setColor(Color.BLUE);
			g2.fillRect(50,-175,50,100);
			g2.drawImage(smiley,-100,-20,null);
		}
	}
	
	
	/**
	 * Constructor sets up GUI.
	 */
	public TransformDemo() {

		ClassLoader cl = getClass().getClassLoader();
		smiley = Toolkit.getDefaultToolkit().createImage(cl.getResource("face-smile.png"));
		gradient = new GradientPaint(0,0,Color.BLACK,50,20,Color.LIGHT_GRAY, true);
		scaleXSlider = new JSlider(-200,200,100);
		scaleYSlider = new JSlider(-200,200,100);
		shearSlider = new JSlider(-100,100,0);
		rotateSlider = new JSlider(-180,180,0);
		translateXSlider = new JSlider(-200,200,0);
		translateYSlider = new JSlider(-150,150,0);
		
		resetButton = new JButton("Reset Tranforms");
		resetButton.addActionListener( e -> {
			scaleXSlider.setValue(100);
			scaleYSlider.setValue(100);
			shearSlider.setValue(0);
			rotateSlider.setValue(0);
			translateXSlider.setValue(0);
			translateYSlider.setValue(0);
		});
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridLayout(7,1,5,5));
		bottom.add(makeInput("X-Scale:    ", scaleXSlider, true));
		bottom.add(makeInput("Y-Scale:    ", scaleYSlider, true));
		bottom.add(makeInput("X-Shear:    ", shearSlider, true));
		bottom.add(makeInput("Rotate:     ", rotateSlider, false));
		bottom.add(makeInput("X-Translate:", translateXSlider, false));
		bottom.add(makeInput("Y-Translate:", translateYSlider, false));
		JPanel buttonHolder = new JPanel();
		buttonHolder.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonHolder.add(resetButton);
		bottom.add(buttonHolder);
		
		canvas = new Display();
		canvas.setBackground(Color.WHITE);
		canvas.setPreferredSize(new Dimension(800,600));
		
		setLayout(new BorderLayout(10,10));
		add(canvas, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
	}
	
	
	/**
	 * Creates a panel containing a slider and a label that displays the
	 * value of that slider.  An event handler is added to the slider to
	 * redraw the canvas when the slider value changes.  If the third
	 * parameter is true, then the value from the slider is divided by 100.
	 */
	private JPanel makeInput(String text, JSlider slider, boolean scale) {
		JLabel label = new JLabel(String.format("%s %8.2f", text,
				scale? slider.getValue()/100.0 : slider.getValue()) );
		label.setFont(new Font("Monospaced", Font.BOLD, 14));
		label.setOpaque(true);
		slider.addChangeListener( e -> canvas.repaint() );
		slider.addChangeListener( e -> 
		         label.setText( String.format("%s %8.2f", text,
  				          scale? slider.getValue()/100.0 : slider.getValue()) ));
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(5,5));
		panel.add(slider, BorderLayout.CENTER);
		panel.add(label, BorderLayout.EAST);
		return panel;
	}



} // end class TransformDemo

