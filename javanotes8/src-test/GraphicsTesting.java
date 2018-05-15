import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GraphicsTesting extends JPanel {
	
	public static void main(String[] args) {
		JFrame window = new JFrame();
		GraphicsTesting content = new GraphicsTesting();
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(120,70);
		window.pack();
		window.setVisible(true);
	}
	
	GraphicsTesting() {
		ClassLoader cl = getClass().getClassLoader();
		Image foo = Toolkit.getDefaultToolkit().createImage(cl.getResource("java.jpeg"));
		JButton javaButton = new JButton( "<html><u>Now</u> is the time for<br>" +
                "a cup of <font color=red>coffee</font>." );
		javaButton.setIcon(new ImageIcon(foo));
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		add(javaButton);
		add(new JButton("foo"));
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}
//	
//	public void paintComponent(Graphics g) {
//	    super.paintComponent(g);
//	    Graphics2D g2 = (Graphics2D)g;
//	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//	    g.setColor(Color.RED);
//	    g.fillOval(10,10,80,80);
//	    g.setColor(Color.GREEN);
//	    g.fillRect(30,30,40,40);
//	}


}
