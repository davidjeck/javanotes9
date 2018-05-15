
// A little program that demonstrates JSliders.

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

public class SliderDemo extends JPanel implements ChangeListener {

	/**
	 * A main routine allows this class to be run as an application.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Slider Demo");
		SliderDemo content = new SliderDemo();
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(120,70);
		window.setSize(350,200);
		window.setVisible(true);
	}

	//---------------------------------------------------------------------

	JSlider slider1, slider2, slider3;  // The sliders.

	JLabel label; // A label for reporting changes in the sliders' values.

	public SliderDemo() {

		setLayout(new GridLayout(4,1));
		setBorder(BorderFactory.createCompoundBorder(
				       BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
				       BorderFactory.createEmptyBorder(8,8,8,8)));

		label = new JLabel("Try dragging the sliders!", JLabel.CENTER);
		add(label);

		slider1 = new JSlider(0,10,0);
		slider1.addChangeListener(this);
		add(slider1);

		slider2 = new JSlider();
		slider2.addChangeListener(this);
		slider2.setMajorTickSpacing(25);
		slider2.setMinorTickSpacing(5);
		slider2.setPaintTicks(true);
		add(slider2);

		slider3 = new JSlider(2000,2100,2014);
		slider3.addChangeListener(this);
		slider3.setLabelTable(slider3.createStandardLabels(50));
		slider3.setPaintLabels(true);
		add(slider3);

	}  // end constructor

	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() == slider1)
			label.setText("Slider one changed to " + slider1.getValue());
		else if (evt.getSource() == slider2)
			label.setText("Slider two changed to " + slider2.getValue());
		else if (evt.getSource() == slider3)
			label.setText("Slider three changed to " + slider3.getValue());
	}

}
