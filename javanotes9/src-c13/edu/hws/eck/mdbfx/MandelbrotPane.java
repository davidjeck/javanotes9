package edu.hws.eck.mdbfx;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Pos;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A MandelbrotPane contains a menu bar, a MandelbrotCanvas and a status bar.  The 
 * canvas computes and displays a visualization of the Mandelbrot Set.  The status bar
 * is a Label that is used to display information that the user might be interested
 * in.  A mouse listener is installed on the display that enables the user to
 * zoom in and out on the image.  Nothing is done to stop the user from zooming
 * in beyond the limited accuracy of numbers of type double -- when this happens,
 * the picture will first become "blocky" and with even further zooms will become
 * meaningless.
 * 
 * <p>Mouse actions on the display:
 * <ul>
 * <li>Moving or dragging the mouse shows image coordinates corresponding to mouse location.
 * <li>Clicking the mouse zooms in on or out from a point.  If shift is down when the
 * mouse was pressed, or if the right mouse button is used, then zoom out by a factor of 2;
 * otherwise, zoom in by a factor of 2.  If alt is down when the mouse is pressed, or if
 * the middle mouse button is used, then the point that was clicked is moved to the center
 * of the image; otherwise, it stays where it is and the rest of the picture moves toward
 * that point or away from it.
 * <li>Dragging the mouse will draw a box around a region of the image.  The box is forced
 * to have the same aspect ratio (shape) as the display.  The box is not drawn if it would
 * be too narrow.  If the box is there when the mouse is released, then the image is
 * zoomed.  With no modifier keys and using the left mouse button, the inside of the
 * box is zoomed to fill the entire display (this zooms in on the image, which is usually
 * what you want).  If the shift key is down, or if the right mouse button is used, 
 * then the entire display is shrunk down into the box (this zooms out).
 * </ul>
 */
public class MandelbrotPane extends BorderPane {

	/**
	 * Constant used to identify one of the types of color palettes that can
	 * be used for coloring the points in the image.  See setPaletteType()
	 * and setGradientPaletteType().
	 */
	public final static int PALETTE_SPECTRUM = 0;
	public final static int PALETTE_PALE_SPECTRUM = 1;
	public final static int PALETTE_GRAYSCALE = 2;
	public final static int PALETTE_CYCLIC_GRAYSCALE = 3;
	public final static int PALETTE_GRADIENT = 4;

	private MandelbrotCanvas display;        // The canvas where the image is drawn.
	private Canvas overlayCanvas;            // A mostly transparent overlay where zoom box is drawn.
	private GraphicsContext overlayGraphics; // Graphics context for drawing on the overlay canvas.
	private Label statusBar;                 // For displaying info; placed at the bottom of the window.
	private StackPane displayHolder;         // Holds the display and the overlay canvas.
	private ScrollPane scroller;             // Holds the displayHolder, but only when image size is fixed.
	private ObjectProperty<double[]> limits; // The xy-limits on the region displayed in the image.
											 //    (This holds xmin,xmax,ymin,ymax.)
	
	private Menus menubar;   // The menu bar from the top of the window.


	private int maxIterations;             // Current maximum number of iterations that will be used
										   //    in the Mandelbrot computation loop.  Pixels that require
										   //    more iterations are colored black.

	private int paletteType;               // Current palette type; one of the constants like PALETTE_SPECTRUM.
	private Color gradientPaletteColor1;   // If palette type is PALETTE_GRADIENT, this is the gradient start color.
	private Color gradientPaletteColor2;   // If palette type is PALETTE_GRADIENT, this is the gradient end color.
	private int paletteLength;             // The number of colors in the palette.
	private int[] palette;                 // The colors in the palette, expressed as ARGB color codes.

	private double xmin, xmax, ymin, ymax; // Ranges of xy values currently visible in the image.

	private double xmin_requested = -2.5;  // These are the values that were requested in the setLimits()
	private double xmax_requested = 1.1;   //   command.  They are adjusted in the checkAspect() method to
	private double ymin_requested = -1.35; //   fit the aspect ratio of the display, and the actual values
	private double ymax_requested = 1.35;  //   that are used for the image are stored in xmin, xmax, ymin,
										   //   and ymax.  If the image changes size, the REQUESTED values
										   //   are re-applied.  Initial values represent the defaults.

	private Rectangle2D zoomBox;           // If non-null, then this rectangle is drawn on top of the
										   //   the image.  The image can be zoomed into or out of this box.
	                                       //   the zoom box can only exist during a mouse drag operation.
	

	/**
	 * Create a pane containing a MandelbrotCanvas, the label that
	 * is used as a status bar, and the menu bar.
	 */
	public MandelbrotPane() {
		setStyle("-fx-border-color:#333; -fx-border-width:1px");
		display = new MandelbrotCanvas(800,600);
		overlayCanvas = new Canvas(800,600);
		overlayGraphics = overlayCanvas.getGraphicsContext2D();
		displayHolder = new StackPane(display,overlayCanvas);
		displayHolder.setMinSize(100,100);
		displayHolder.setAlignment(Pos.TOP_LEFT);
		statusBar = new Label(I18n.tr("Idle"));
		statusBar.setStyle("-fx-padding:7px; -fx-background-color:white; "
				              + "-fx-border-color:#333; -fx-border-width:4px 0 0 0");
		statusBar.setMaxWidth(Double.POSITIVE_INFINITY);
		setCenter(displayHolder);
		setBottom(statusBar);

		limits = new SimpleObjectProperty<double[]>();  // Has to exist before creating menus.
		menubar = new Menus(this);
		setTop(menubar);
		
		overlayCanvas.setOnMousePressed( this::mousePressed );   // mouse interaction is with overlayCanvas
		overlayCanvas.setOnMouseDragged( this::mouseDragged );
		overlayCanvas.setOnMouseReleased( this::mouseReleased );
		overlayCanvas.setOnMouseMoved( this::mouseMoved );
		overlayCanvas.setOnMouseExited( this::mouseExited );
		
		paletteType = PALETTE_SPECTRUM;  // defaults for palette and maxIterations
		paletteLength = 0;
		maxIterations = 250;
		createPalette();
		checkAspect();
		limits.setValue(getLimits());
		
		/* Listen for changes to the size of displayHolder (which will be resized by this
		 * BorderPane when the window size changes).  In response, schedule a new compute
		 * job to occur in 300 milliseconds. */
		
		displayHolder.widthProperty().addListener( e -> startDelayedJob(300,true) );
		displayHolder.heightProperty().addListener( e -> startDelayedJob(300,true) );
		startDelayedJob(500, false);
	}
	
	
	/* ---------------------------------------------------------------------------------
	 * When image size changes, the image has to be recomputed.  As the user resizes the
	 * window, the width and height of the canvas can change many times. Rather than
	 * start a new computation each time their values change, there is a delay of
	 * 300 milliseconds before the new computation is started.  startDelayedJob()
	 * is also used in the constructor, to avoid starting the computation before the
	 * window has time to appear on the screen.
	 */
	
	private Timer resizeTimer = new Timer();  // For implementing the delay.
	private TimerTask resizeTask;
	
	/**
	 * Schedules a new computation to be started after a specified number of
	 * milliseconds.  If this method is called before the time has expired, the
	 * previously scheduled start is canceled, and a new start is scheduled with
	 * the given delay.  If the method is called many times quickly, only the
	 * last call will actually result in a new computation starting.  The method
	 * is called with resizeFirst = true, except when it is called from the
	 * constructor.
	 */
	private void startDelayedJob(int milliseconds, boolean resizeFirst) {
		synchronized(resizeTimer) {
			if (resizeTask != null)
				resizeTask.cancel();
			resizeTask = new TimerTask() {
				public void run() {
					synchronized(resizeTimer) {
						Platform.runLater( () -> {
							display.stopJob();
							if (resizeFirst) { 
								display.setWidth(displayHolder.getWidth());
								display.setHeight(displayHolder.getHeight());
								overlayCanvas.setWidth(displayHolder.getWidth());
								overlayCanvas.setHeight(displayHolder.getHeight());
								checkAspect();
								statusBar.setText( I18n.tr("status.imageSize",
										             (int)displayHolder.getWidth(),(int)displayHolder.getHeight()) );
							}
							display.startJob(maxIterations,palette,xmin,xmax,ymin,ymax);
							resizeTask = null;
						});
					}
				}
			};
			resizeTimer.schedule(resizeTask, milliseconds);
		}
	}
	
	//--------------------------------- Methods for use by Main and Menus ----------------------
	
	public Menus getMenus() {
		return menubar;
	}
	
	/**
	 * This is called by Main.java when the window is closed.  It is important to cancel the
	 * resizeTimer, since otherwise, it would stop the Java Virtual Machine from exiting.
	 */
	public void closing() {
		resizeTimer.cancel();
	}
	
	public ObjectProperty<double[]> limitsProperty() {
		return limits;
	}
	
	/**
	 * Set the image size.  If width or height is less than or equal to zero,
	 * the size of the image will track the size of the pane.  Otherwise, the
	 * image size will be fixed; the image might not fill the window, and if it
	 * is too big for the window, scroll bars will appear.
	 */
	public void setImageSize( int width, int height ) { 
		if (width <= 0 || height <= 0) {
			if (scroller == null)
				return;
			scroller = null;
			setCenter(null);
			displayHolder = new StackPane(display,overlayCanvas);
			displayHolder.setMinSize(100,100);
			displayHolder.setAlignment(Pos.TOP_LEFT);
			displayHolder.widthProperty().addListener( e -> startDelayedJob(300,true) );
			displayHolder.heightProperty().addListener( e -> startDelayedJob(300,true) );
			setCenter(displayHolder);
		}
		else {
			if (scroller != null && width == (int)displayHolder.getWidth() 
					                   && height == (int)displayHolder.getHeight()) {
				return;
			}
			setCenter(null);
			display.setWidth(width);
			display.setHeight(height);
			overlayCanvas.setWidth(width);
			overlayCanvas.setHeight(height);
			displayHolder = new StackPane(display,overlayCanvas);
			scroller = new ScrollPane(displayHolder);
			setCenter(scroller);
			checkAspect();
			display.startJob(maxIterations, palette, xmin, xmax, ymin, ymax);
			statusBar.setText( I18n.tr("status.imageSize",width,height) );
		}
	}
	
	public void setMaxIterations( int iters ) {
		if (maxIterations == iters)
			return;
		maxIterations = iters;
		if (paletteLength == 0)
			createPalette();
		display.startJob(maxIterations, palette, xmin, xmax, ymin, ymax);
	}
	
	public int getMaxIterations() {
		return maxIterations;
	}
	
	public void setPaletteLength(int length) {
		if (length != 0 && length == paletteLength)
			return;
		paletteLength = length;
		createPalette();
		display.setPalette(palette);
	}
	
	public int getPaletteLength() {
		return paletteLength;
	}
	
	public void setPaletteType(int type) { // type != PALETTE_GRADIENT
		if (paletteType == type)
			return;
		paletteType = type;
		createPalette();
		display.setPalette(palette);
		gradientPaletteColor1 = null;
		gradientPaletteColor2 = null;
	}
	
	public void setGradientPalette(Color c1, Color c2) {
		if (paletteType == PALETTE_GRADIENT && gradientPaletteColor1.equals(c1)
				&& gradientPaletteColor2.equals(c2))
			return;
		paletteType = PALETTE_GRADIENT;
		gradientPaletteColor1 = c1;
		gradientPaletteColor2 = c2;
		createPalette();
		display.setPalette(palette);
	}
	
	public int getPaletteType() {
		return paletteType;
	}
	
	public Color getGradientPaletteColor1() { // will be null for other gradient types
		return gradientPaletteColor1;
	}
	
	public Color getGradientPaletteColor2() { // will be null for other gradient types
		return gradientPaletteColor2;
	}
	
	/**
	 * Sets all params back to their default values and starts a new computation.
	 */
	public void defaults() {
		paletteType = PALETTE_SPECTRUM;
		maxIterations = 250;
		paletteLength = 0;
		setLimits(-2.5,1.2,-1.35,1.35);
		createPalette();
		display.startJob(maxIterations,palette,xmin,xmax,ymin,ymax);
	}
	
	/**
	 * Sets all params to specified values and starts a new computation.
	 * No error checking is done.  This is used in Menus.java to implement
	 * loading from an XML params file.
	 */
	public void setParams(int maxIterations, int paletteType, Color c1, Color c2, 
			                                         int paletteLength, double[] limits) {
		this.maxIterations = maxIterations;
		this.paletteType = paletteType;
		this.gradientPaletteColor1 = c1;
		this.gradientPaletteColor2 = c2;
		this.paletteLength = paletteLength;
		setLimits(limits[0],limits[1],limits[2],limits[3]);
		checkAspect();
		createPalette();
		display.startJob(maxIterations,palette,xmin,xmax,ymin,ymax);
	}
	
	/**
	 * Change the limits on the xy-region shown in the image, possibly
	 * readjusting them to match the aspect ratio of the display, and
	 * start a new computation.
	 */
	public void setLimits( double xmin, double xmax, double ymin, double ymax ) {
		xmin_requested = xmin;
		xmax_requested = xmax;
		ymin_requested = ymin;
		ymax_requested = ymax;
		checkAspect();
		limits.setValue(getLimits());
		display.startJob(maxIterations,palette,this.xmin,this.xmax,this.ymin,this.ymax);
	}
	
	public double[] getLimits() {
		return new double[] { xmin, xmax, ymin, ymax };
	}

    /**
     * When the xy-limits were originally set, or when they are changed with setLimits,
     * allDefaults, or setParams, the requested mins and maxes are saved before the
     * values are adjusted to match the aspect ratio of the display.  This method
     * returns the originally requested limits.  When the limits are changed by
     * zooming, the requested limits will be the same as the actual limits.
     */
	public double[] getRequestedLimits() {
		return new double[] { xmin_requested, xmax_requested, ymin_requested, ymax_requested };
	}
	
	public MandelbrotCanvas getDisplay() {
		return display;
	}

	//---------------------------- The rest of the file is private implementation detail ----------

	/**
	 * Adjusts the xy limits to fit the aspect ratio of the display.  If the shape of
	 * the requested region in the plane does not match the shape of the display,
	 * then either the range of x values or the range of y values will be increased
	 * to make the shapes match.  Note that the full requested ranges are always shown.
	 * There just might be some extra parts of the plane visible on the top and bottom
	 * or sides.
	 */
	private void checkAspect() {
		xmin = xmin_requested;
		xmax = xmax_requested;
		if (xmax < xmin) {
			double temp = xmin;
			xmin = xmax;
			xmax = temp;
		}
		ymin = ymin_requested;
		ymax = ymax_requested;
		if (ymax < ymin) {
			double temp = ymax;
			ymax = ymin;
			ymin = temp;
		}
		double width = xmax - xmin;
		double height = ymax - ymin;
		double aspect = width/height;
		double windowAspect = (double)display.getWidth()/(double)display.getHeight();
		if (aspect < windowAspect) {
			double newWidth = width*windowAspect/aspect;
			double center = (xmax + xmin)/2;
			xmax = center + newWidth/2;
			xmin = center - newWidth/2;
		}
		else if (aspect > windowAspect) {
			double newHeight = height*aspect/windowAspect;
			double center = (ymax+ymin)/2;
			ymax = center + newHeight/2;
			ymin = center - newHeight/2;
		}
	}
	
	/**
	 * Builds the array that holds the palette colors, based on current settings.
	 */
	private void createPalette() {
		if (paletteLength == 0)
			palette = new int[maxIterations+1];
		else
			palette = new int[paletteLength];
		for (int i = 0; i < palette.length; i++) {
			double fraction = ((double)i)/(palette.length);
			Color color;
			switch (paletteType) {
			case PALETTE_GRADIENT:
				double r1 = gradientPaletteColor1.getRed();
				double r2 = gradientPaletteColor2.getRed();
				double r = Math.max(0,Math.min(1,r2*fraction + r1*(1-fraction)));
				double g1 = gradientPaletteColor1.getGreen();
				double g2 = gradientPaletteColor2.getGreen();
				double g = Math.max(0,Math.min(1,g2*fraction + g1*(1-fraction)));
				double b1 = gradientPaletteColor1.getBlue();
				double b2 = gradientPaletteColor2.getBlue();
				double b = Math.max(0,Math.min(1,b2*fraction + b1*(1-fraction)));
				color = Color.color(r,g,b);
				break;
			case PALETTE_SPECTRUM:
				color = Color.hsb(360*fraction, 1, 1);
				break;
			case PALETTE_PALE_SPECTRUM:
				color = Color.hsb(360*fraction, 0.6F, 1);
				break;
			case PALETTE_GRAYSCALE:
				color = Color.gray(0.9*fraction);
				break;
			default: // PALETTE_CYCLIC_GRAYSCALE
				if (fraction < 0.5)
					color = Color.gray(Math.max(0,Math.min(1,2*fraction)));
				else
					color = Color.gray(Math.max(0,Math.min(1,2*(1-fraction))));
				break;
			}
			int argb = 0xFF;
			argb = argb << 8 | (int)(color.getRed()*255);
			argb = argb << 8 | (int)(color.getGreen()*255);
			argb = argb << 8 | (int)(color.getBlue()*255);
			palette[i] = argb;
		}
	}
	
	/**
	 * Zoom in on or out from a point in the image.
	 * @param x x-coordinate of the point at the center of the zoom
	 * @param y y-coordinate of the point at the center of the zoom
	 * @param factor magnification or shrinking factor.  If factor is
	 *   greater than 1, zoom out.  If factor is less than 1, zoom in.
	 *   For example, factor=0.5 shrinks the x,y ranges in the image
	 *   to half their previous size.
	 * @param movePointToCenter if true, then the image point at pixel
	 *   position (x,y) is moved to the center pixel of the image after
	 *   the zoom; if false, the point is not moved so that the pixel
	 *   at (x,y) represents the same point after the zoom as before
	 *   and all the other points move towards or away from that one.
	 */
	public void zoom(double x, double y, double factor, boolean movePointToCenter) {
		double newWidth = factor*(xmax-xmin);
		double newHeight = factor*(ymax-ymin);
		double centerX = xmin + ((double)x)/display.getWidth()*(xmax-xmin);
		double centerY = ymax - ((double)y)/display.getHeight()*(ymax-ymin);
		if (movePointToCenter) {
			xmin = centerX-newWidth/2;
			xmax = centerX+newWidth/2;
			ymin = centerY-newHeight/2;
			ymax = centerY+newHeight/2;
		}
		else {
			double newXmin = centerX - newWidth*(centerX-xmin)/(xmax-xmin);
			double newYmin = centerY - newHeight*(centerY-ymin)/(ymax-ymin);
			xmin = newXmin;
			xmax = xmin + newWidth;
			ymin = newYmin;
			ymax = ymin + newHeight;
		}
		xmin_requested = xmin;
		xmax_requested = xmax;
		ymin_requested = ymin;
		ymax_requested = ymax;
		limits.setValue(getLimits());
		display.startJob(maxIterations, palette, xmin, xmax, ymin, ymax); 
	}

	/**
	 * Used to draw a rectangle around a portion of the image.  If the parameter is null,
	 * then nothing is drawn (and the rectangle that was there before, if any is removed).
	 * Otherwise, the specified rectangle will be drawn on top of the image.
	 */
	private void setZoomBox(Rectangle2D rect) {
		if (zoomBox != null)
			overlayGraphics.clearRect( zoomBox.getMinX() - 5, zoomBox.getMinY() - 5, 
					                       zoomBox.getWidth() + 10, zoomBox.getWidth() + 10);
		zoomBox = rect;
		if (zoomBox != null) {
			overlayGraphics.setStroke(Color.WHITE);	
			overlayGraphics.setLineWidth(4);
			overlayGraphics.strokeRect(zoomBox.getMinX(),zoomBox.getMinY(),
					                       zoomBox.getWidth(),zoomBox.getHeight());
			overlayGraphics.setStroke(Color.BLACK);	
			overlayGraphics.setLineWidth(2);
			overlayGraphics.strokeRect(zoomBox.getMinX(),zoomBox.getMinY(),
					                       zoomBox.getWidth(),zoomBox.getHeight());
		}
	}

	/**
	 * If a rectangle has been specified using the drawZoomBox, this method will zoom the
	 * image into or out of the box.  The rectangle is then discarded.  If there is no
	 * zoom box, nothing is done.
	 * @param zoomOut if false, the part of the image inside the zoom rectangle is magnified
	 *    to fill the entire image; if true, the entire image is shrunk down to fit inside
	 *    the zoom box and new parts of the picture become visible.
	 */
	private void applyZoom(boolean zoomOut) {
		if (zoomBox == null)
			return;
		double x1, x2, y1, y2;  // coordinates of corners of zoombox
		double cx, cy;   // coordinates of center of zoombox
		double newWidth, newHeight;
		x1 = xmin + zoomBox.getMinX()/display.getWidth()*(xmax-xmin);
		x2 = xmin + zoomBox.getMaxX()/display.getWidth()*(xmax-xmin);
		y1 = ymax - zoomBox.getMaxY()/display.getHeight()*(ymax-ymin);
		y2 = ymax - zoomBox.getMinY()/display.getHeight()*(ymax-ymin);
		cx = (x1+x2)/2;
		cy = (y1+y2)/2;
		if (zoomOut) {  // (some heavy math)
			double newXmin = xmin + (xmin-x1)/(x2-x1)*(xmax-xmin);
			double newXmax = xmin + (xmax-x1)/(x2-x1)*(xmax-xmin);
			double newYmin = ymin + (ymin-y1)/(y2-y1)*(ymax-ymin);
			double newYmax = ymin + (ymax-y1)/(y2-y1)*(ymax-ymin);
			display.startJob(maxIterations,palette,newXmin,newXmax,newYmin,newYmax);
			xmin = newXmin;
			xmax = newXmax;
			ymin = newYmin;
			ymax = newYmax;
		}
		else {
			newWidth = x2 - x1;
			newHeight = y2 - y1;
			xmin = cx-newWidth/2;
			xmax = cx+newWidth/2;
			ymin = cy-newHeight/2;
			ymax = cy+newHeight/2;
			display.startJob(maxIterations,palette,xmin,xmax,ymin,ymax); 
		}
		xmin_requested = xmin;
		xmax_requested = xmax;
		ymin_requested = ymin;
		ymax_requested = ymax;
		limits.setValue(getLimits());
		setZoomBox(null);
	}
	
	/**
	 * Display the coordinates of the image point that corresponds
	 * to pixel coordinates (x,y).
	 */
	private void doShowCoordsInStatusBar(double x, double y) {
		double width = display.getWidth();
		double height = display.getHeight();
		double xCoord = xmin + x/width*(xmax-xmin);
		double yCoord = ymax - y/height*(ymax-ymin);
		
		// The next 10 lines try to avoid more digits after the decimal
		// points than makes sense.  If it succeeds the coordinates
		// that are shown should differ only in their last few digits.
		double diff = xmax - xmin;
		int scale = 4;
		if (diff > 0) {
			while (diff < 1) {
				scale++;
				diff *= 10;
			}
		}
		String xStr = String.format("%1." + scale + "f", xCoord);
		String yStr = String.format("%1." + scale + "f", yCoord);
		statusBar.setText(I18n.tr("status.mouseCoords",xStr,yStr));
	}
	
	
	/*----------------------------------------------------------------------------------
	 * Defines the listeners that respond to user mouse actions on the display.
	 * Note that the (x,y) coordinates for the events refer to the display, since
	 * the listeners are registered to respond to events on the overlay canvas, which
	 * has the same coords as the display.  Mouse drags and clicks are used for zooming 
	 * the image.  Dragging and mouse motion also show the current mouse coordinates 
	 * in the status bar.
	 */
	
	private double startX, startY; // Location of mousePressed event.

	private boolean dragging;   // True if a drag operation is in progress.

	private boolean zoomOut;    // True if the action will be a zoom out rather than
								// a zoom in.  This is set to true if the shift key
								// or meta key is down for the mousePressed action.
								// (Also true if the right-mouse button is used.)

	private boolean moved;      // During a drag operation, this becomes true if
								// the mouse actually moves at least a few pixels.
								// If so, the mouse action is interpreted as a
								// click rather than a drag.

	private boolean movePointToCenter;  // True if the click point for a click
										// operation should be moved to the center
										// of the image; false if it should not
										// be moved.  This is set in the mousePressed
										// routine to be true if the alt/option key
										// is down (or the middle mouse button is used).

	private void mousePressed(MouseEvent evt) {
		    // Start a mouse drag or click operation.
		doShowCoordsInStatusBar(startX,startY);
		startX = evt.getX();
		startY = evt.getY();
		zoomOut = evt.isShiftDown() || evt.getButton() == MouseButton.SECONDARY;
		dragging = true;
		moved = false;
		movePointToCenter = evt.isAltDown() || evt.getButton() == MouseButton.MIDDLE;
	}


	private void mouseReleased(MouseEvent evt) {
		    //Apply the zoom box to zoom in or out, if there is one,
		    // or, if the mouse has not moved, just zoom in or out by
		    // a factor of 2, with the current mouse location as center
		    // of scaling.
		if (!dragging)
			return;
		if (moved)  // If moved is true, this is a drag operation, otherwise, a click.
			applyZoom(zoomOut);  // zoom into or out of zoom rect that user has drawn
		else if (zoomOut)
			zoom(startX,startY,2,movePointToCenter); // zoom out from point
		else
			zoom(startX,startY,0.5,movePointToCenter); // zoom in on point
		dragging = false;
	}

	private void mouseDragged(MouseEvent evt) {
		    // respond to mouse drag by maybe drawing a zoom box
		double x = evt.getX();
		double y = evt.getY();
		doShowCoordsInStatusBar(x,y);
		if (!dragging)
			return;
		double width = Math.abs(x-startX);
		double height = Math.abs(y-startY);
		if (Math.abs(width) < 3 || Math.abs(height) < 3) {
				// Too close to start point to have a zoom box.
		    setZoomBox(null);
			return;
		}
		moved = true;   // Mouse has moved more than 2 pixels away from start position.
						// During a draw operation, a zoom box is drawn with one corner at the
						// mouse's starting position.  When the mouse is released, the image
						// is zoomed out from this box or into this box.
						// The next 6 lines adjust the shape of the zoom box so that it matches
						// the shape of the window.  This is so that zooming will use the same
						// magnification factor in both directions.
		double aspect = (double)width/height;
		double imageAspect = display.getWidth()/display.getHeight();
		if (aspect < imageAspect)
			width = (width*imageAspect/aspect);
		else if (aspect > imageAspect)
			height = (height*aspect/imageAspect);
		
		// The next 9 lines compute the upper left corner of the rectangle,
		// so that it has one corner at the start position of the mouse;
		// width and height represent the size of the zoom rect.
		double x1,y1;
		if (x < startX)
			x1 = startX - width;
		else
			x1 = startX;
		if (y < startY)
			y1 = startY - height;
		else
			y1 = startY;
		Rectangle2D rect = new Rectangle2D(x1,y1,width,height);
		setZoomBox(rect);
	}

	private void mouseMoved(MouseEvent evt) { 
		    // When mouse moves, show coords of its location in the status bar.
		doShowCoordsInStatusBar(evt.getX(),evt.getY());
	}

	private void mouseExited(MouseEvent evt) { 
			// When mouse moves out of the display, get rid of the coordinate
			// display in the status bar.
		statusBar.setText(I18n.tr("Idle"));
	}



} // end class MandelbrotPane
