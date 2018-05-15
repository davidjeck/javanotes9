import javax.swing.*;    // make the Swing GUI classes available
import java.awt.*;       // used for Color and GridLayout classes
import java.awt.event.*; // used for ActionEvent and  ActionListener classes

/**
 * This simple program demonstrates several GUI components that are available in the
 * Swing GUI library.  The program shows a window containing a button, a text input
 * box, a combo box (pop-up menu), and a text area.  The text area is used for
 * a "transcript" that records interactions of the user with the other components.
 */
public class GUIDemo extends JPanel implements ActionListener {

	/**
	 * This main routine allows this class to be run as an application.  The main
	 * routine simply creates a window containing a panel of type GUIDemo.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("GUI Demo");
		window.setContentPane( new GUIDemo() );
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocation(150,100);
		window.setVisible(true);
	}

	//-----------------------------------------------------------------------------------

	private JTextArea transcript; // a message will be posted to this text area
								  // each time an event is generated by some
								  // some user action
	
	private JComboBox<String> combobox; // The pop-up menu.

	/**
	 * This constructor adds several GUI components to the panel and sets
	 * itself up to listen for action events from some of them.
	 */
	public GUIDemo() {

		setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
		setBackground(Color.WHITE);

		setLayout(new GridLayout(1, 2, 3, 3));
			// I will put the transcript area in the right half of the
			// panel. The left half will be occupied by a grid of
			// four lines. Each line contains a component and
			// a label for that component.

		transcript = new JTextArea();
		transcript.setEditable(false);
		transcript.setMargin(new Insets(4, 4, 4, 4));
		JPanel left = new JPanel();
		left.setLayout(new GridLayout(4, 2, 3, 10));
		left.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		add(left);
		add(new JScrollPane(transcript));

		JLabel lab = new JLabel("Push Button:   ", JLabel.RIGHT);
		left.add(lab);
		JButton b = new JButton("Click Me!");
		b.addActionListener(this);
		left.add(b);

		lab = new JLabel("Checkbox:   ", JLabel.RIGHT);
		left.add(lab);
		JCheckBox c = new JCheckBox("Click me!");
		c.addActionListener(this);
		left.add(c);

		lab = new JLabel("Text Field:   ", JLabel.RIGHT);
		left.add(lab);
		JTextField t = new JTextField("Type here!");
		t.addActionListener(this);
		left.add(t);

		lab = new JLabel("Pop-up Menu:   ", JLabel.RIGHT);
		left.add(lab);
		combobox = new JComboBox<String>();
		combobox.addItem("First Option");
		combobox.addItem("Second Option");
		combobox.addItem("Third Option");
		combobox.addItem("Fourth Option");
		combobox.addActionListener(this);
		left.add(combobox);

	}

	private void post(String message) { // add a line to the transcript
		transcript.append(message + "\n\n");
	}

	/**
	 * Respond to an ActionEvent from one of the GUI components in the panel.
	 * In each case, a message about the event is posted to the transcript.
	 * (This method is part of the ActionListener interface.)
	 */
	public void actionPerformed(ActionEvent evt) {
		Object target = evt.getSource(); // which component produced this event?
		if (target instanceof JButton) {
			post("Button was clicked.");
		} else if (target instanceof JTextField) {
			post("Pressed return in TextField\nwith contents:\n    "
					+ evt.getActionCommand());
		} else if (target instanceof JCheckBox) {
			if (((JCheckBox) target).isSelected())
				post("Checkbox was turned on.");
			else
				post("Checkbox was turned off.");
		} else if (target == combobox) {
			Object item = combobox.getSelectedItem();
			post("Item \"" + item + "\" selected\nfrom pop-up menu.");
		}
	}

} // end class GUIDemo
