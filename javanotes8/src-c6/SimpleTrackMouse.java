
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This program displays information about mouse events on a canvas, including the 
 * type of event, the position of the mouse, a list of modifier keys that were down 
 * when the event occurred, and an indication of which mouse button was pressed
 * or released, if any.  It also shows information about mouse events seen
 * by an event filter on the screen object; the screen gets to see most events before 
 * they are seen by the event target. 
 */
public class SimpleTrackMouse extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	// --------------------------------------------------------------------------------
	
	private Canvas canvas;  // The canvas that fills the window.
	                        // The program reports about mouse events for which the
	                        // canvas is the target.

	private StringBuilder eventInfo;  // Contains a string with information about the event.
	                                  // This string is drawn on the canvas.


	/**
	 * Set up a window containing just a canvas.  Install handlers for common
	 * mouse events on the canvas.  Also install an event filter for mouse
	 * events on the screen.  Information about mouse events will be displayed
	 * on the canvas.
	 */
	public void start(Stage stage) {
		
		eventInfo = new StringBuilder();
		
		/* Create the canvas, and set up the GUI */
		
		canvas = new Canvas(550,400);
		Pane root = new Pane(canvas);
		Scene scene = new Scene( root );
		stage.setScene(scene);
		stage.setTitle("Mouse Event Info");
		stage.setResizable(false);
		
		/* Draw an initial message on the canvas */
		
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.setFont( Font.font(18) );
		g.setFill(Color.WHITE);
		g.fillRect(0,0,550,400);
		g.setFill(Color.BLACK);
		g.fillText("WAITING FOR FIRST MOUSE EVENT", 50, 50);
		
		/* Install an event filter for all mouse events on the scene.  The
		 * filter just calls mouseEventOnScene(e) when an event occurs. */
		
		scene.addEventFilter(MouseEvent.ANY, e -> mouseEventOnScene(e) );
		
		/* Install event handlers for common mouse events on the canvas.
		 * I could have used a single event handler on the canvas, but this
		 * shows how to handle the individual types of event.  The response
		 * in each case is simply to call mouseEventOnCanvas() */
		
		canvas.setOnMousePressed( e -> mouseEventOnCanvas(e, "Mouse Pressed") );
		canvas.setOnMouseReleased( e -> mouseEventOnCanvas(e, "Mouse Released") );
		canvas.setOnMouseClicked( e -> mouseEventOnCanvas(e, "Mouse Clicked") );
		canvas.setOnMouseDragged( e -> mouseEventOnCanvas(e, "Mouse Dragged") );
		canvas.setOnMousePressed( e -> mouseEventOnCanvas(e, "Mouse Pressed") );
		canvas.setOnMouseMoved( e -> mouseEventOnCanvas(e, "Mouse Moved") );
		canvas.setOnMouseEntered( e -> mouseEventOnCanvas(e, "Mouse Entered") );
		canvas.setOnMouseExited( e -> mouseEventOnCanvas(e, "Mouse Exited") );
		
		stage.show();  // make the window visible
		
	} // end start()

	
	/**
	 * The draw() method is called from mouseEventOnCanvas() to show the
	 * information about the event on the canvas.  It simply draws the
	 * eventInfo string.
	 */
	private void draw() {
		GraphicsContext g = canvas.getGraphicsContext2D(); 
		g.setFill(Color.WHITE);
		g.fillRect( 0, 0, g.getCanvas().getWidth(), g.getCanvas().getHeight() );
		g.setFill(Color.BLACK);
		g.fillText( eventInfo.toString(), 40, 40 );
	}
	
	
	/**
	 * This is called by the event filter for mouse events that was installed
	 * on the screen.  It adds a note about the event to the eventInfo string
	 * but does not redraw the canvas.  The note will be part of the event
	 * info shown in the canvas after the next call to mouseEventOnCanvas().
	 */
	private void mouseEventOnScene(MouseEvent evt) {
		if (evt.getTarget() == canvas) {
		    eventInfo.append("MOUSE EVENT ON SCENE: " + evt.getEventType() + "\n\n");
		}
	}

	
	/**
	 * Adds information about a mouse event on the canvas to the eventInfo string,
	 * and displays that string on the canvas.  The eventInfo string is then cleared,
	 * except in the case of a Mouse Entered event (otherwise, the Mouse Entered
	 * event would always be immediately replaced by a Mouse Moved event before
	 * the user could have any chance of seeing it).
	 */
	private void mouseEventOnCanvas(MouseEvent evt, String eventType) {
		eventInfo.append(eventType + " on canvas at (");
		eventInfo.append( (int)evt.getX() + "," + (int)evt.getY() + ")\n");
		if (eventType.equals("Mouse Pressed") || eventType.equals("Mouse Released") 
				|| eventType.equals("Mouse Clicked")) {
			eventInfo.append( "Mouse button pressed or released: " + evt.getButton() + "\n");
		}
		if (eventType.equals("Mouse Clicked")) {
			eventInfo.append( "Click count: " + evt.getClickCount() + "\n" );
		}
		eventInfo.append("Modifier keys held down:  ");
		if (evt.isShiftDown())
			eventInfo.append("Shift  ");
		if (evt.isControlDown())
			eventInfo.append("Control  ");
		if (evt.isMetaDown())
			eventInfo.append("Meta  ");
		if (evt.isAltDown())
			eventInfo.append("Alt");
		eventInfo.append("\n");
		eventInfo.append("Mouse buttons held down:  ");
		if (evt.isPrimaryButtonDown())
			eventInfo.append("Primary  ");
		if (evt.isMiddleButtonDown())
			eventInfo.append("Middle  ");
		if (evt.isSecondaryButtonDown())
			eventInfo.append("Secondary  ");
		draw();
		if ( eventType.equals("Mouse Entered") ) {
			eventInfo.append("\n\n(Info not erased after Mouse Entered)\n\n\n");
		}
		else {
			eventInfo.setLength(0);
		}
	}


}  // end class SimpleTrackMouse

