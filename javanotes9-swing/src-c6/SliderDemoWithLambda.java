
// A little program that demonstrates JSliders.  This is an alternative
// version of SliderDemo.java that uses lambda expressions to define
// the ChangeListeners are registered with the sliders.

import java.awt.*;
import javax.swing.*;

public class SliderDemoWithLambda extends JPanel  {

	/**
	 * A main routine allows this class to be run as an application.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Slider Demo");
		SliderDemoWithLambda content = new SliderDemoWithLambda();
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(120,70);
		window.setSize(350,200);
		window.setVisible(true);
	}

	//---------------------------------------------------------------------

	JSlider slider1, slider2, slider3;  // The sliders.

	JLabel label; // A label for reporting changes in the sliders' values.

	public SliderDemoWithLambda() {

		setLayout(new GridLayout(4,1));
		setBorder(BorderFactory.createCompoundBorder(
				       BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
				       BorderFactory.createEmptyBorder(8,8,8,8)));

		label = new JLabel("Try dragging the sliders!", JLabel.CENTER);
		add(label);

		slider1 = new JSlider(0,10,0);
		slider1.addChangeListener( 
				evt -> label.setText("Slider one changed to " + slider1.getValue()) );
		add(slider1);

		slider2 = new JSlider();
		slider2.addChangeListener( 
				evt -> label.setText("Slider two changed to " + slider2.getValue()) );
		slider2.setMajorTickSpacing(25);
		slider2.setMinorTickSpacing(5);
		slider2.setPaintTicks(true);
		add(slider2);

		slider3 = new JSlider(2000,2100,2014);
		slider3.addChangeListener( 
				evt -> label.setText("Slider three changed to " + slider3.getValue()) );
		slider3.setLabelTable(slider3.createStandardLabels(50));
		slider3.setPaintLabels(true);
		add(slider3);

	}  // end constructor

}
