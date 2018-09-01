import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

import java.util.ArrayList;

/**
 * A SillyStamper panel contains a List of icons, a canvas where 
 * the user can "stamp" images of the icons, and a few control buttons.
 * The user clicks an icon in the list to select it, then clicks on the 
 * canvas to place copies of the selected image.
 * 
 * This program *requires* the icon image files from the stamper_icons directory.
 * These icons are 32-by-32 pixels.
 * (The images were taken from a KDE desktop icon collection.)
 */
public class SillyStamper extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//--------------------------------------------------------------


	/**
	 * An object of type IconInfo stores the information needed to draw
	 * one icon image on the canvas.
	 */
	private static class IconInfo {
		int iconNumber;  // an index into the iconImages array
		int x, y;        // coords of the upper left corner of the image
		boolean big;     // should icon be scaled up to a larger (48-by48) size
	}

	/**
	 * Contains info for all the icons that have been placed on the
	 * canvas.  Might contain more than have actually been shown,
	 * because of the Undo command.  An icon that is removed from the
	 * canvas by an undo is not removed from this list.
	 */
	private ArrayList<IconInfo> icons = new ArrayList<IconInfo>();

	private int iconsShown;  // Number of icons shown in the display area.
	private int iconsPlaced; // Number of icons that have been placed.  Can be
							 //    greater than iconsShown, because of undo/redo.

	private ListView<ImageView> iconList;  // The ListView from which the user selects icons.

	private Button undoButton;  // A button for removing the most recently added image.
	private Button redoButton;  // A button for restoring the most recently removed image.

	private Canvas canvas;      // The canvas where the icons are displayed.

	private Image[] iconImages;  // The little images that can be "stamped".


	public void start(Stage stage) {
		
		canvas = new Canvas(400,300);
		canvas.setOnMousePressed( this::mousePressed );
		
		undoButton = new Button("Undo");
		undoButton.setOnAction( e -> {
			if (iconsShown > 0) {
				   // Decrement iconsShown, so one less icon will be drawn;
				   // the icon is still in the array, for the Redo command.
				iconsShown--;
				redoButton.setDisable(false);
				redraw();
			}
		});
		undoButton.setDisable(true);
		
		redoButton = new Button("Redo");
		redoButton.setOnAction( e -> {
			if (iconsShown < iconsPlaced) {
				   // Increment iconsShown, so one more icon will be shown.
				iconsShown++;
				if (iconsShown == iconsPlaced)
					redoButton.setDisable(true);
				undoButton.setDisable(false);
				redraw();
			}
		});
		redoButton.setDisable(true);

		iconList = createIconList();  // Create the scrolling list of icons.
		iconList.setStyle("-fx-border-color:#009; -fx-border-width: 0 0 0 6px");
		
		HBox bottom = new HBox(15,undoButton,redoButton);
		bottom.setStyle("-fx-padding:7px; -fx-border-color:#009; -fx-border-width:6px 0 0 0");
		bottom.setAlignment(Pos.CENTER);
		
		BorderPane root = new BorderPane(canvas);
		root.setStyle("-fx-border-color:#009; -fx-border-width:6px");
		root.setRight(iconList);
		root.setBottom(bottom);
		
		stage.setScene( new Scene(root) );
		stage.setResizable(false);
		stage.setTitle("Silly Stamper -- Shift-click for big icon");
		stage.show();
		redraw();

	} // end start()

	
    /**
     * Draw the entire content of the canvas, with a light blue background
     * and icons that the user has placed on the canvas.
     */
	public void redraw() {
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.setFill(Color.color(0.93,0.93,1));
		g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
		for (int i = 0; i < iconsShown; i++) {
			IconInfo info = icons.get(i);
			if (info.big)
				g.drawImage(iconImages[info.iconNumber], info.x, info.y, 48, 48);
			else
				g.drawImage(iconImages[info.iconNumber], info.x, info.y);
		}
	}

	/**
	 * When the user clicks the canvas, place a copy of the currently selected
	 * icon image at the point where the user clicked.  If the shift key is
	 * down the icon is "big", i.e. drawn at 48 pixels instead of the usual 32.
	 * Also, icons in the list that are no longer shown, because of "Undo"
	 * commands, are effectively discarded.
	 */
	public void mousePressed(MouseEvent e) { 
		IconInfo info  = new IconInfo();
		info.iconNumber = iconList.getSelectionModel().getSelectedIndex();
		info.big = e.isShiftDown();
		if (info.big) {  // icon image will be 48-by-48
			info.x = (int)(e.getX() - 24);  // Offset coords, so center of icon is at
			info.y = (int)(e.getY() - 24);  //         the point that was clicked.
		}
		else {  // icon image will be 32-by-32
			info.x = (int)(e.getX() - 16); 
			info.y = (int)(e.getY() - 16); 
		}
		if (iconsShown == icons.size())
			icons.add(info);
		else 
			icons.set(iconsShown, info);
		iconsShown++;
		iconsPlaced = iconsShown;
		redoButton.setDisable(true);
		undoButton.setDisable(false);
		redraw();
	}


	/**
	 * Create a ListView that contains all of the available icon images.  Initially,
	 * the first icon is selected.  The user can select a different icon by
	 * clicking its image in the list.  The items in the ListView are ImageView objects,
	 * not Image objects, since a List will not display Image objects correctly.
	 * This list is not editable and will not change after it is created.
	 */
	private ListView<ImageView> createIconList() {
		String[] iconNames = new String[] { // names of image resource file, in directory stamper_icons
				"icon5.png", "icon7.png", "icon8.png", "icon9.png", "icon10.png", "icon11.png", 
				"icon24.png", "icon25.png", "icon26.png", "icon31.png", "icon33.png", "icon34.png"
		};

		iconImages = new Image[iconNames.length];

		ListView<ImageView> list = new ListView<>();

		list.setPrefWidth(80);    // The default pref width is much too wide,
		list.setPrefHeight(100);  // The default pref height is 400, which is taller than the canvas,
		                          //    which would force the height of the BorderPane to be too big.

		for (int i = 0; i < iconNames.length; i++) {
			Image icon = new Image("stamper_icons/" + iconNames[i]);
			iconImages[i] = icon;
			list.getItems().add( new ImageView(icon) );
		}

		list.getSelectionModel().select(0);  // The first item in the list is currently selected.

		return list;
	}

} // end class SillyStamper

