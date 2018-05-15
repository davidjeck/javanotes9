import java.awt.*;

import javax.swing.*;

/**
 * This program simply demonstrates using a JTextArea in a JScrollPane.
 */
public class FlowLayoutDemo extends JPanel {
	

	/**
	 * A main routine allows this class to be run as an application.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Slider Demo");
		FlowLayoutDemo content = new FlowLayoutDemo();
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(120,70);
		window.setSize(550,120);
		window.setVisible(true);
	}

	//---------------------------------------------------------------------

	public FlowLayoutDemo() {

		setBorder(BorderFactory.createCompoundBorder(
			       BorderFactory.createLineBorder(Color.BLACK, 1),
			       BorderFactory.createEmptyBorder(5,5,5,5)));
		
		JButton b1, b2, b3, b4, b5;
		b1 = new JButton("Button Number 1");
		b2 = new JButton("Button Number 2");
		b3 = new JButton("Button Number 3");
		b4 = new JButton("Button Number 4");
		b5 = new JButton("Button Number 5");
		add(b1);
		add(b2);
		add(b3);
		add(b4);
		add(b5);
		
	}

}
