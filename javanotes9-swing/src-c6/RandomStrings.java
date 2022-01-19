
import javax.swing.JFrame;

/**
 * A program that shows a RandomStringsPanel as its content pane.
 */
public class RandomStrings {
	
	public static void main(String[] args) {
		JFrame window = new JFrame("Java!");
		RandomStringsPanel content = new RandomStringsPanel();
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(120,70);
		window.setSize(350,250);
		window.setVisible(true);
	}

}

