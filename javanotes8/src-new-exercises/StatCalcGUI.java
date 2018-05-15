import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * In this panel, the user enters numbers in a text field box.
 * After entering each number, the user presses return (or clicks
 * on a button).  Some statistics are displayed about all the
 * numbers that the user has entered.
 */
public class StatCalcGUI extends JPanel implements ActionListener {

	/**
	 * A main routine allows this class to be run as an application.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Stat Calc");
		StatCalcGUI content = new StatCalcGUI();
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(120,70);
		window.setSize(350,200);
		window.setVisible(true);
	}

	//---------------------------------------------------------------------

	final static Color labelBG = new Color(240,225,200);  // For creating labels
	final static Color labelFG = new Color(180,0,0);
	final static Font labelFont = new Font("Monospaced", Font.PLAIN, 12);

	private JLabel countLabel;    // A label for displaying the number of numbers.
	private JLabel sumLabel;      // A label for displaying the sum of the numbers.
	private JLabel meanLabel;     // A label for displaying the average.
	private JLabel standevLabel;  // A label for displaying the standard deviation.

	private JLabel message;  // A message at the top of the applet.  It will
	//   show an error message if the user's input is
	//   not a legal number.  Otherwise, it just tells
	//   the user to enter a number and press return.

	private JButton enterButton;   // A button the user can press to enter a number.
	//    This is an alternative to pressing return.
	private JButton clearButton;   // A button that clears all the data that the
	//    user has entered.

	private JTextField numberInput;  // The input box where the user enters numbers.

	private StatCalc stats;  // An object that keeps track of the statistics
	//   for all the numbers that have been entered.

	/**
	 * The constructor creates the objects used by the panel.  The panel
	 * will listen for action events from the buttons and from the text
	 * field.  (A JTextField generates an ActionEvent when the user presses 
	 * return while typing in the text field.)
	 */
	public StatCalcGUI() {

		stats = new StatCalc();

		numberInput = new JTextField();
		numberInput.setBackground(Color.WHITE);
		numberInput.addActionListener(this);

		enterButton = new JButton("Enter");
		enterButton.addActionListener(this);

		clearButton = new JButton("Clear");
		clearButton.addActionListener(this);

		JPanel inputPanel = new JPanel();  // A panel that will hold the
		//   JTextField and JButtons.
		inputPanel.setLayout( new GridLayout(1,3) );
		inputPanel.add(numberInput);
		inputPanel.add(enterButton);
		inputPanel.add(clearButton);

		countLabel =   makeLabel(" Number of Entries:  0");
		sumLabel =     makeLabel(" Sum:                0.0");
		meanLabel =    makeLabel(" Average:            undefined");
		standevLabel = makeLabel(" Standard Deviation: undefined");

		message = new JLabel("Enter a number, press return:",
				JLabel.CENTER);
		message.setBackground(labelBG);
		message.setForeground(Color.BLUE);
		message.setOpaque(true);
		message.setFont(new Font("SansSerif", Font.BOLD, 12));

		/* Use a GridLayout with 6 rows and 1 column, and add all the
          components that have been created to the applet. */

		setBackground(Color.BLUE);
		setLayout( new GridLayout(6,1,2,2) );
		add(message);
		add(inputPanel);
		add(countLabel);
		add(sumLabel);
		add(meanLabel);
		add(standevLabel);

		/* Add a blue border around the panel. */

		setBorder( BorderFactory.createLineBorder(Color.BLUE, 2) );

	} // end constructor


	/**
	 * A utility routine for creating the labels that are used
	 * for display.  This routine is called by the constructor.
	 * @param text The text to show on the label.
	 */
	private JLabel makeLabel(String text) {
		JLabel label = new JLabel(text);
		label.setBackground(labelBG);
		label.setForeground(labelFG);
		label.setFont(labelFont);
		label.setOpaque(true);
		return label;
	}   


	/**
	 * This is called when the user clicks one of the buttons or
	 * presses return in the input box.  The response to clicking
	 * on the Enter button is the same as the response to pressing
	 * return in the JTextField.
	 */
	public void actionPerformed(ActionEvent evt) {

		Object source = evt.getSource();  // Object that generated 
		//   the action event.

		if (source == clearButton) {
			// Handle the clear button by starting with a new,
			// empty StatCalc object and resetting the display
			// labels to show no data entered.  The TextField
			// is also made empty.
			stats = new StatCalc();
			countLabel.setText(" Number of Entries:  0");
			sumLabel.setText(" Sum:                0.0");
			meanLabel.setText(" Average:            undefined");
			standevLabel.setText(" Standard Deviation: undefined");
			numberInput.setText("");
		}
		else if (source == enterButton || source == numberInput) {
			// Get the user's number, enter it into the StatCalc
			// object, and set the display on the display labels
			// to reflect the new data.
			double num;  // The user's number.
			try {
				num = Double.parseDouble(numberInput.getText());
			}
			catch (NumberFormatException e) {
				// The user's entry is not a legal number.  
				// Put an error message in the message label 
				// and return without entering a number.
				message.setText("\"" + numberInput.getText() + 
						"\" is not a legal number.");
				numberInput.selectAll();
				numberInput.requestFocus();
				return;
			}
			stats.enter(num);
			countLabel.setText(" Number of Entries:  " + stats.getCount());
			sumLabel.setText(" Sum:                " + stats.getSum());
			meanLabel.setText(" Average:            " + stats.getMean());
			standevLabel.setText(" Standard Deviation: " 
					+ stats.getStandardDeviation());
		}

		/* Set the message label back to its normal text, in case it has
          been showing an error message.  For the user's convenience,
          select the text in the TextField and give the input focus
          to the text field.  That way the user can just start typing
          the next number. */

		message.setText("Enter a number, press return:");
		numberInput.selectAll();
		numberInput.requestFocus();

	}  // end ActionPerformed

}  // end StatsApplet