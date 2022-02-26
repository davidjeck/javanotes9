import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This demo program divides up a large computation into a fairly
 * large number of smaller tasks.  The computation is to compute
 * an image, and each task computes one row of pixels in the image.
 * 
 * The functionality of this program is identical to MultiprocessingDemo3.
 * This version of the program uses an ExecutorService to execute the tasks.
 * When the user clicks "Start", a new ExecutorService is created and all
 * of the tasks that are part of computing the image are added to it.
 * If the user aborts a computation, the executor's shutDownNow() method
 * is called, which makes the executor drop any waiting tasks from its
 * queue.
 * 
 * (The image is a small piece of the famous Mandelbrot set,
 *  which is used just because it takes some time to compute.  
 * There is no need to understand what the image means.)  
 */
public class MultiprocessingDemo4 extends JPanel {

	/**
	 * This main routine just shows a panel of type MultiprocessingDemo3.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Multiprocessing Demo 4");
		MultiprocessingDemo4 content = new MultiprocessingDemo4();
		window.setContentPane(content);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (window.getWidth() > screenSize.width-100 || window.getHeight() > screenSize.height-100) {
			double scale1 = (double)(screenSize.width-100)/window.getWidth();
			double scale2 = (double)(screenSize.height-100)/window.getHeight();
			double scale = Math.min(scale1,scale2);
			window.setSize( (int)(scale*window.getWidth()), (int)(scale*window.getHeight()) );
		}
		window.setLocation( (screenSize.width - window.getWidth()) / 2,
				(screenSize.height - window.getHeight()) / 2 );
		window.setVisible(true);
	}

	private ExecutorService executor;   // The executor that executes the MandelbrotTasks.
									    // When a job is started, an executor is created to
									    // execute the tasks that make up that job.  (A job
									    // consists of computing a complete image; a task is
									    // computing one line of the image.)  The value of
									    // this variable is null when no job is in progress.

	private int tasksRemaining; // How many tasks in the current job still remain to be done?
							    // (Note: the variables executor and tasksRemaining can be
							    // modified by various threads.  They are not volatile because
							    // all access is done in synchronized methods.)

	private JButton startButton; // Button the user can click to start or abort the thread.
	private BufferedImage image; // Contains the image that is computed by this program.
	int[] palette;  // Holds a spectrum of RGB color values; used in computing pixel colors.


	/**
	 * The display is a JPanel that shows the image.  The part of the image that has
	 * not yet been computed is gray.  If the image has not yet been created, the
	 * entire display is filled with gray.
	 */
	private JPanel display = new JPanel() {
		protected void paintComponent(Graphics g) {
			if (image == null)
				super.paintComponent(g);  // fill with background color, gray
			else {
				/* Copy the image onto the display.  This is synchronized because
				 * there are several threads that compete for access to the image:
				 * the threads that compute the image and the thread that does the
				 * painting.  All synchronization is done on the main class object,
				 * referred to here as MultiprocessingDemo3.this.
				 */
				synchronized(MultiprocessingDemo4.this) {
					g.drawImage(image,0,0,null);
				}
			}
		}
	};


	/**
	 * Constructor creates a panel to hold the display, with a "Start" button 
	 * and a pop-up menu for selecting the number of threads below it.
	 */
	public MultiprocessingDemo4() {
		display.setPreferredSize(new Dimension(1600,1200));
		display.setBackground(Color.LIGHT_GRAY);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		setLayout(new BorderLayout());
		add(display, BorderLayout.CENTER);
		JPanel bottom = new JPanel();
		startButton = new JButton("Start");
		bottom.add(startButton);
		bottom.setBackground(Color.WHITE);
		add(bottom,BorderLayout.SOUTH);
		palette = new int[256];
		for (int i = 0; i < 256; i++)
			palette[i] = Color.getHSBColor(i/255F, 1, 1).getRGB();
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (executor != null)
					stop();
				else
					start();
			}
		});
	}


	/**
	 * This method is called when the user clicks the Start button,
	 * while no computation is in progress.  It clears the image
	 * and sets up the computation of a new image.  The first time
	 * that it is called, it is also responsible for creating the
	 * image and the thread pool.
	 */
	synchronized private void start() {
		startButton.setText("Abort"); // change name while computation is in progress
		int width = display.getWidth() + 2;
		int height = display.getHeight() + 2;
		if (image == null) { // create the image and the thread pool
			image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		}
		Graphics g = image.getGraphics();  // fill image with gray
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0,0,width,height);
		g.dispose();
		display.repaint();

		int processors =  Runtime.getRuntime().availableProcessors();
		executor = Executors.newFixedThreadPool(processors);
		
		tasksRemaining = height;

		double xmin = -1.6744096740931858;
		double xmax = -1.674409674093473;
		double ymin = 4.716540768697223E-5;
		double ymax = 4.716540790246652E-5;
		int maxIterations = 10000;
		double dx = (xmax-xmin)/(width-1);
		double dy = (ymax-ymin)/(height-1);
		for (int row = 0; row < height; row++) { // Add tasks for current job to job queue.
			double y = ymax - row*dy;
			MandelbrotTask task = new MandelbrotTask(
					               executor, row, width, maxIterations, xmin, y, dx);
			executor.execute(task);
		}
		executor.shutdown();  // Will shut down after completing submitted tasks.
	}

	/**
	 * This method is called when the user clicks the button while
	 * a thread is running.  It is also called by the taskFinished()
	 * method when all the tasks that make up a job have been
	 * completed.  The responsibility of this method is to
	 * finish the current job by incrementing the jobNumber and
	 * discarding any tasks from the current job that are still in
	 * the queue (in case the job is ending because the user has
	 * aborted it before it finished).
	 */
	synchronized private void stop() {
		startButton.setText("Start Again");
		executor.shutdownNow(); // Drop any remaining jobs.
		executor = null;  // signals that now no job is progress
	}


	/**
	 * This method is called by each thread when it terminates.  We keep track
	 * of the number of threads that have terminated, so that when they have
	 * all finished, we can put the program into the correct state, such as
	 * changing the name of the button to "Start Again" and re-enabling the
	 * pop-up menu.
	 */
	synchronized private void taskFinished(MandelbrotTask task) {
		if (task.myExecutor != executor) {
			System.out.println("Dropping results from previous job."); // for testing
			return;
		}
		tasksRemaining--;
		image.setRGB(0,task.rowNumber, task.width, 1, task.rgb, 0, task.width);
		display.repaint(0,task.rowNumber,task.width,1); // Repaint just the newly computed row.
		if (tasksRemaining == 0) { // all threads have finished
			stop();
		}
	}


	/**
	 * An object of type MandelbrotTask represents the task of computing one row
	 * of pixels in an image of the Mandelbrot set.  The task has a run() method
	 * that does the actual computation.  It also calls the taskFinished() method
	 * before terminating.
	 */
	private class MandelbrotTask implements Runnable {
		ExecutorService myExecutor; // Which Executor will execute this task?
							        // This is used in taskFinished to avoid
							        // processing the result from a task that is
							        // part of a previous job.
		int rowNumber;  // Which row of pixels does this task compute?
		double xmin;    // The x-value for the first pixel in the row.
		double y;       // The y-value for all the pixels in the row.
		double dx;      // The change in x-value from one pixel to the next.
		int width;      // The number of pixels in the row.
		int maxIterations;  // The maximum count in the Mandelbrot algorithm.
		int[] rgb;      // The pixel colors computed by this task;
		MandelbrotTask( ExecutorService executor, int rowNumber, int width, 
	              int maxIterations, double xmin, double y, double dx) {
			this.myExecutor = executor;
			this.rowNumber = rowNumber;
			this.maxIterations = maxIterations;
			this.xmin = xmin;
			this.y = y;
			this.dx = dx;
			this.width = width;
		}
		public void run() {
			rgb= new int[width];     // The colors computed for the pixels.
			for (int i = 0; i < rgb.length; i++) {
				double x = xmin + i * dx;
				int count = 0;
				double xx = x;
				double yy = y;
				while (count < maxIterations && (xx*xx + yy*yy) < 4) {
					count++;
					double newxx = xx*xx - yy*yy + x;
					yy = 2*xx*yy + y;
					xx = newxx; 
				}
				if (count == maxIterations)
					rgb[i] = 0;
				else
					rgb[i] = palette[count % 256];
			}
			taskFinished(this);
		}
	}

}
