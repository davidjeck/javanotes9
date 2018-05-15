import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


/**
 * In this panel, the user types some text in a JTextArea and presses
 * a button.  The panel computes and displays the number of lines
 * in the text, the number of words in the text, and the number of
 * characters in the text.  A word is defined to be a sequence of
 * letters, except that an apostrophe with a letter on each side
 * of it is considered to be a letter.  (Thus "can't" is one word,
 * not two.)
 */
public class TextCounter extends JPanel {

	/**
	 * A main routine allows this class to be run as an application.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Text Counter");
		TextCounter content = new TextCounter();
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(120,70);
		window.setSize(300,350);
		window.setVisible(true);
	}

	//---------------------------------------------------------------------

	private JTextArea textInput;     // For the user's input text.

	private JLabel lineCountLabel;   // For displaying the number of lines.
	private JLabel wordCountLabel;   // For displaying the number of words.
	private JLabel charCountLabel;   // For displaying the number of chars.


	/**
	 * The constructor creates components and lays out the panel.
	 */ 
	public TextCounter() {

		setBackground(Color.DARK_GRAY);

		/* Create the text input area and make sure it has a
             white background. */

		textInput = new JTextArea();
		textInput.setBackground(Color.WHITE);

		/* Create a panel to hold the button and three display
             labels.  These will be laid out in a GridLayout with
             4 rows and 1 column. */

		JPanel south = new JPanel();
		south.setBackground(Color.DARK_GRAY);
		south.setLayout( new GridLayout(4,1,2,2) );

		/* Create the button and a listener to listen for
             clicks on the button, and add it to the panel. */

		JButton countButton = new JButton("Process the Text");
		countButton.addActionListener( new ActionListener() { 
			public void actionPerformed(ActionEvent evt) {
				processInput();
			}
		});
		south.add(countButton);

		/* Create each of the labels, set their colors, and
             add them to the panel. */

		lineCountLabel = new JLabel("  Number of lines:");
		lineCountLabel.setBackground(Color.WHITE);
		lineCountLabel.setForeground(Color.BLUE);
		lineCountLabel.setOpaque(true);
		south.add(lineCountLabel);

		wordCountLabel = new JLabel("  Number of words:");
		wordCountLabel.setBackground(Color.WHITE);
		wordCountLabel.setForeground(Color.BLUE);
		wordCountLabel.setOpaque(true);
		south.add(wordCountLabel);

		charCountLabel = new JLabel("  Number of chars:");
		charCountLabel.setBackground(Color.WHITE);
		charCountLabel.setForeground(Color.BLUE);
		charCountLabel.setOpaque(true);
		south.add(charCountLabel);

		/* Use a BorderLayout on the panel.  Although a BorderLayout
             is the default, I want one with a vertical gap of two
             pixels, to let the dark gray background color show through.
             Also add a gray border around the panel. */

		setLayout( new BorderLayout(2,2) );
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

		/* The text area is put into a JScrollPane to provide
             scroll bars for the TextArea, and the scroll pane is put in
             the Center position.  The panel that holds the button and
             labels is in the South position.  Note that the text area
             will be sized to fill the space that is left after the
             panel is assigned its preferred height. */

		JScrollPane scroller = new JScrollPane( textInput );
		add(scroller, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);


	} // end constructor


	/**
	 * This will be called by the action listener for the button when the user
	 * clicks the button.  It gets the text from the text area, counts the number
	 * of chars, words, and lines that it contains, and sets the labels to
	 * display the results.
	 */
	public void processInput() {

		String text;  // The user's input from the text area.

		int charCt, wordCt, lineCt;  // Char, word, and line counts.

		text = textInput.getText();

		charCt = text.length();  // The number of characters in the
		                         //    text is just its length.

		/* Compute the wordCt by counting the number of characters
              in the text that lie at the beginning of a word.  The
              beginning of a word is a letter such that the preceding
              character is not a letter.  This is complicated by two
              things:  If the letter is the first character in the
              text, then it is the beginning of a word.  If the letter
              is preceded by an apostrophe, and the apostrophe is
              preceded by a letter, than its not the first character
              in a word.
		 */

		wordCt = 0;
		for (int i = 0; i < charCt; i++) {
			boolean startOfWord;  // Is character i the start of a word?
			if ( Character.isLetter(text.charAt(i)) == false )
				startOfWord = false;  // No.  It's not a letter.
			else if (i == 0)
				startOfWord = true;   // Yes.  It's a letter at start of text.
			else if ( Character.isLetter(text.charAt(i-1)) )
				startOfWord = false;  // No.  It's a letter preceded by a letter.
			else if ( text.charAt(i-1) == '\'' && i > 1 
					&& Character.isLetter(text.charAt(i-2)) )
				startOfWord = false;  // No.  It's a continuation of a word
			                          //      after an apostrophe.
			else
				startOfWord = true;   // Yes.  It's a letter preceded by
			                          //       a non-letter.
			if (startOfWord)
				wordCt++;
		}

		/* The number of lines is just one plus the number of times the
              end of line character, '\n', occurs in the text. */

		lineCt = 1;
		for (int i = 0; i < charCt; i++) {
			if (text.charAt(i) == '\n')
				lineCt++;
		}

		/* Set the labels to display the data. */

		lineCountLabel.setText("  Number of Lines:  " + lineCt);
		wordCountLabel.setText("  Number of Words:  " + wordCt);
		charCountLabel.setText("  Number of Chars:  " + charCt);

	}  // end processInput()


} // end class TextCounter
