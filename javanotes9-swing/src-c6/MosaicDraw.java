import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * A MosaicDraw is a JFrame that shows a MosaicCanvas as its content pane.  The mosaic
 * is made up of rows and columns of squares.  The user can click-and-drag
 * the mouse to color the squares.  A menu bar with some options is shown
 * at the top of the window.  The window will be centered on the screen,
 * but will not be visible initially.
 * 
 * This class also contains a main program, so that it can be run as a
 * stand-alone application.  The main program creates a MosaicDrawFrame and
 * makes it visible on the screen.  It also sets the program to end when
 * the user closes the window.
 * 
 * Note that this class depends on MosaicCanvas.java and MosaicDrawController.java.
 */
public class MosaicDraw extends JFrame {

	public static void main(String[] args) {
		JFrame window = new MosaicDraw();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	public MosaicDraw() {
		super("Mosaic Draw");
		MosaicDrawController controller = new MosaicDrawController();
		setContentPane(controller.getMosaicCanvas());
		setJMenuBar(controller.getMenuBar());
		pack();
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation( (screensize.width - getWidth())/2, (screensize.height - getHeight())/2);
	}

}
