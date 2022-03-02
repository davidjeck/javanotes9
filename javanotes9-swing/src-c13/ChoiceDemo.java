
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This program demonstrates making choices using radio groups
 * and comboboxes.  It also uses Actions, checkboxes, and menus.
 * A variety of techniques are demonstrated.  See the comments 
 * for many details.
 * 
 * The program shows a message whose text, foreground color, and
 * background color can be selected by the user.  Note that the
 * text can be set using both a check box and a menu item.  The foreground
 * color can be set using either a radio group or a combo box.  The
 * background color can be set using either a group of radio buttons
 * or a group of radio button menu items.
 */
public class ChoiceDemo extends JPanel {
	
	/**
	 * A main routine allows the class to be run as an application.
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("ChoiceDemo");
		ChoiceDemo content = new ChoiceDemo();
		frame.setContentPane(content);
		frame.setJMenuBar(content.menuBar);
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(100,70);
		frame.setVisible(true);
	}
	
	//------------------ Instance variables.  Some could be local in the constructor -----------
	
	private JLabel message;
	private Action messageAction;
	
	private JRadioButton redRadio, blueRadio, greenRadio, yellowRadio;
	private JComboBox<String> foregroundChoice;

	private Action[] backgroundActions;
	private String[] backgroundColorNames = { "Black", "Gray", "Light Gray", "White" };
	private Color[] backgroundcolors = {Color.BLACK, Color.GRAY, Color.LIGHT_GRAY, Color.WHITE};
	
	private JMenuBar menuBar;  // A menu bar to be used in the frame that contains this panel.

	//-------------------------------------------------------------------------------------------
	
	/**
	 * Everything is done in the constructor, including setting up the user interface
	 * and event handling and creating the menu bar.
	 */
	public ChoiceDemo() {
		
		// The panel shows a very large message together with some controls
		// for setting the text, foreground color and background color of the
		// message.  The message can say either "HELLO" or "GOODBYE".
		// First, create the message with default colors.
		
		message = new JLabel("GOODBYE", JLabel.CENTER);
		message.setOpaque(true);
		message.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		message.setFont( new Font("Serif", Font.BOLD, 72) );
		message.setForeground(Color.RED);
		message.setBackground(Color.BLACK);
		
		
		//----------------------------------------------------------------------------------
		// Create a radio group and a combo box to control the foreground color.
		// The color can be set to red, blue, green, or yellow.  The radio group
		// and the combo box have exactly the same functionality.  A problem is
		// keeping the radio group and the combo box in sync, so that both always
		// show the correct color selection.  
		
		ActionListener fgListener = new ActionListener() {
			    // This listener will be used as the action listener for each of the
			    // radio buttons in the foreground color group.  When the user selects
			    // one of those buttons, the listener changes the foreground color of
			    // the message to match the selection.  It ALSO changes the selection
			    // in the combo box, foregroundChoice, to match the selection in the
			    // radio group.  evt.getActionCommand() is the text from the button.
			public void actionPerformed(ActionEvent evt) {
				switch ( evt.getActionCommand() ) {
					case "Red" -> {
						message.setForeground(Color.RED);
						foregroundChoice.setSelectedIndex(0);
					}
					case "Blue" -> {
						message.setForeground(Color.BLUE);
						foregroundChoice.setSelectedIndex(1);
					}
					case "Green" -> {
						message.setForeground(Color.GREEN);
						foregroundChoice.setSelectedIndex(2);
					}
					case "Yellow" -> {
						message.setForeground(Color.YELLOW);
						foregroundChoice.setSelectedIndex(3);
					}
				}
			}
		};
		
		ButtonGroup colorGroup = new ButtonGroup(); // coordinates the buttons on the radio group
		
		redRadio = new JRadioButton("Red");       // Create first radio button.
		colorGroup.add(redRadio);                 // Add it to the button group.
		redRadio.addActionListener(fgListener);   // Add the actionListener to the radio button.
		
		blueRadio = new JRadioButton("Blue");     // Create second radio button.
		colorGroup.add(blueRadio);
		blueRadio.addActionListener(fgListener);
		
		greenRadio = new JRadioButton("Green");   // Create third radio button.
		colorGroup.add(greenRadio);
		greenRadio.addActionListener(fgListener);
		
		yellowRadio = new JRadioButton("Yellow"); // Create fourth radio button.
		colorGroup.add(yellowRadio);
		yellowRadio.addActionListener(fgListener);
		
		redRadio.setSelected(true);  // Need "Red" to be selected initially to match the message color.
		
		foregroundChoice = new JComboBox<String>();  // Create the combo box containing the same color options.
		foregroundChoice.addItem("Red");
		foregroundChoice.addItem("Blue");
		foregroundChoice.addItem("Green");
		foregroundChoice.addItem("Yellow");
		
		foregroundChoice.addActionListener( new ActionListener() {
			    // This is the listener for the combo box.  When the user selects one of the
			    // items in the combo box, this listener will set the foreground color
			    // of the message to match the selected color in the combo box.  It ALSO
			    // sets the corresponding radio button to be selected.
			public void actionPerformed(ActionEvent evt) {
				switch ( foregroundChoice.getSelectedIndex() ) {
					case 0 -> {
						message.setForeground(Color.RED);
						redRadio.setSelected(true);
					}
					case 1 -> {
						message.setForeground(Color.BLUE);
						blueRadio.setSelected(true);
					}
					case 2 -> {
						message.setForeground(Color.GREEN);
						greenRadio.setSelected(true);
					}
					case 3 -> {
						message.setForeground(Color.YELLOW);
						yellowRadio.setSelected(true);
					}
				}
			}
		});
		
		
		//---------------------------------------------------------------------------------------------
		// Create an Action that represents making a choice to set the message to
		// HELLO or to GOODBYE.  HELLO is used if the value of the actions SELECTED_KEY
		// property is true.  GOODBYE is used if it is false.  This action will be
		// used to create both a JCheckBox and a JCheckBoxMenuItem, and it will keep
		// the selected state of those two checkboxes in sync.
		messageAction = new AbstractAction("Use \"HELLO\"") {
			public void actionPerformed(ActionEvent evt) {
					// This method will be called when the user changes the state of either
					// the JCheckBox or of the JCheckBoxMenuItem.  It sets the text of the
					// message according to the value of the action's SELECTED _KEY property.
					// Note that a property value is actually an Object.  For a boolean
					// property value, the wrapper class Boolean is used.  Since the
					// return type of message.getValue() is Object, it has to be type-cast
					// to Boolean to use the value as a boolean value.
				boolean useHello = (Boolean)messageAction.getValue(Action.SELECTED_KEY);
				if (useHello)
					message.setText("HELLO");
				else
					message.setText("GOODBYE");
			}
		};
		messageAction.putValue(Action.SELECTED_KEY, false);  // Essential to set the value, to get the synchronization!
		JCheckBox messageCheckbox = new JCheckBox(messageAction);  // Create a checkbox from the action
		
		
		//-----------------------------------------------------------------------------------------
		// Create a radio button group to represent the choice of background colors.  In this case,
		// a set of Actions is created to represent the choices, and the radio buttons are created
		// from the actions.  Later, the same set of actions is used to create a group of 
		// JRadioButtonMenuItems for the menu bar.  The actions ensure that the JRadioButtons are
		// always in sync with the JRadioButtonMenuItems.  Only the JRadioButtons are in a
		// ButtonGroup -- the ButtonGroup makes sure that only one JRadioButton is selected;
		// since the JRadioButtonMenuItems are in sync with the JRadioButtons, the same is
		// automatically true for the menu items.
		//    Furthermore, the actions in this case have a value for their ACCELERATOR_KEY property.
		// The accelerator key is used as a shortcut for the menu items.  For example, a white 
		// background can be selected by hitting CONTROL-W.
		
		JRadioButton[] backgroundRadioButtons = new JRadioButton[backgroundColorNames.length];
		backgroundActions = new Action[backgroundColorNames.length];
		ButtonGroup backgroundGroup = new ButtonGroup();
		
		for ( int i = 0; i < backgroundColorNames.length; i++ ) {
			    // Create an Action that sets the background color of the message to background color number i.
			final Color color = backgroundcolors[i];  // need a final variable for use in anonymous class
			backgroundActions[i] = new AbstractAction(backgroundColorNames[i]) {
				public void actionPerformed(ActionEvent evt) {
					message.setBackground(color);
				}
			};
			
			backgroundActions[i].putValue(Action.SELECTED_KEY, false);  // Essential to set this to get synchronization!
			
			KeyStroke shortcut = KeyStroke.getKeyStroke("ctrl " + backgroundColorNames[i].charAt(0));
			      // Use the first letter of the color name, together with the control key, as the accelerator
			backgroundActions[i].putValue(Action.ACCELERATOR_KEY, shortcut);
			
			backgroundRadioButtons[i] = new JRadioButton(backgroundActions[i]); // create a radio button from the actions
			backgroundGroup.add(backgroundRadioButtons[i]);  // add the radio button to the radio group
			
		}
		backgroundActions[0].putValue(Action.SELECTED_KEY, true);  // We want the first item to be initially selected.
		
		
		//-------------------------------------------------------------------------------------------
		// Create a menu bar.  A Control menu contains a JCheckBoxMenuItem created from the same
		// Action that was used to create the JCheckBox.  It also contains a "Quit" command.
		// A BackgroundColor menu contains the JRadioButtonMenuItems for selecting background color.
		JMenu controlMenu = new JMenu("Control");
		controlMenu.add(new JCheckBoxMenuItem(messageAction));
		
		    // Add a "Quit" command with the modifier that is appropriate for the system
		    // on which the program is running.  This should be the meta (command or apple)
		    // key on a Mac and the control key on Windows or Linux.  Note that the color
		    // commands in the BackgroundColor menu use the control key on any computer.
		    // The next line gets a code for the correct modifier key.
		int shortcutMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
		KeyStroke quitKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Q, shortcutMask);
		JMenuItem quit = new JMenuItem("Quit");
		quit.setAccelerator(quitKeyStroke);
		quit.addActionListener( evt -> System.exit(0) );
		controlMenu.add(quit);
		
		JMenu colorMenu = new JMenu("BackgroundColor");
		for (Action action : backgroundActions)
			colorMenu.add(new JRadioButtonMenuItem(action));
		menuBar = new JMenuBar();
		menuBar.add(controlMenu);
		menuBar.add(colorMenu);
		
		
		//----------------------------------------------------------------------------------------------
		// Set up the user interface, which uses four panels in addition to the main panel...
		
		setBackground(Color.LIGHT_GRAY);
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		JPanel left = new JPanel();
		JPanel right = new JPanel();
		JPanel center = new JPanel();
		JPanel bottom = new JPanel();
		
		setLayout(new BorderLayout(5,5));
		add(left, BorderLayout.WEST);
		add(right, BorderLayout.EAST);
		add(center, BorderLayout.CENTER);
		left.setLayout(new GridLayout(4,1));
		right.setLayout(new GridLayout(4,1));
		center.setBackground(Color.LIGHT_GRAY);
		center.setLayout(new BorderLayout(5,5));
		center.add(message,BorderLayout.CENTER);
		center.add(bottom,BorderLayout.SOUTH);
		bottom.setBackground(Color.LIGHT_GRAY);
		bottom.setLayout(new GridLayout(1,2,5,5));

		left.add(redRadio);  // left panel holds the foreground color radio buttons
		left.add(blueRadio);
		left.add(greenRadio);
		left.add(yellowRadio);

		for (JRadioButton rb : backgroundRadioButtons)
			right.add(rb);   // right panel holds the background color radio buttons

		bottom.add(foregroundChoice);  // bottom panel holds the combo box and checkbox
		bottom.add(messageCheckbox);

	} // end constructor
	
	

} // end ChoiceDemo
