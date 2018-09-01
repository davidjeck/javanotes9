package edu.hws.eck.mdbfx;

import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Optional;
import org.w3c.dom.*;
import java.io.*;
import java.net.URL;


/**
 * This class defines a MenuBar for use with a MandelbrotPane.  This is a large
 * and complex class because it includes many nested classes and event handlers that
 * do the work of carrying out menu commands.  However, most of the complexity is in
 * the private part of the class, and the class has only a small public interface.
 */
public class Menus extends MenuBar {

	/**
	 * This is the list of example files for the Examples menu.  The items in the
	 * menu are the strings from this array.  For a string str in the array,
	 * a resource file name is constructed as:  "edu/hws/edu/mdbfx/examples" + str + ".mdb".
	 * The resulting names must be names of resource files that are accessible 
	 * to the program.  The files should be settings files for the Mandelbrot
	 * Viewer program.  When the user selects an item from the Examples menu,
	 * the corresponding file is loaded and applied to the display.
	 */
	private static final String[] SETTINGS_FILE_LIST =  {
			"settings1", "settings2", "settings3", "settings4", "settings5", 
			"settings6", "settings7", "settings8", "settings9", "settings10",
			"settings11", "settings12"
	};

	/**
	 * Constructor creates the menu bar containing commands that apply to a MandelbrotPane.
	 * The menu bar contains File, MaxIterations, Palette, PaletteLength, and Example menus.
	 * @param owner  the MandelbrotPane that will be managed by this menu bar.  This
	 *   variable is saved for later access to the MandelbrotPane.
	 */
	public Menus(MandelbrotPane owner) {
		this.owner = owner;
		
		paletteManager = new PaletteManager();              // Defines the Palette menu.
		paletteLengthManager = new PaletteLengthManager();  // Defines the PaletteLength menu.
		maxIterationsManager = new MaxIterationsManager();  // Defines the MaxIterations menu.

		// Create menus and add them to the menu bar.

		Menu fileMenu = new Menu(I18n.tr("menu.file"));
		Menu controlMenu = new Menu(I18n.tr("menu.control"));
		Menu maxIterationsMenu = new Menu(I18n.tr("menu.maxIterations"));
		Menu paletteMenu = new Menu(I18n.tr("menu.palette"));
		Menu paletteLengthMenu = new Menu(I18n.tr("menu.paletteLength"));
		Menu exampleMenu = new Menu(I18n.tr("menu.examples"));
		getMenus().addAll(fileMenu,controlMenu,maxIterationsMenu,paletteMenu,
				                                   paletteLengthMenu,exampleMenu);

		// Add items to the File menu.

		MenuItem saveParams = new MenuItem(I18n.tr("command.save"));
		saveParams.setOnAction( e -> doSaveParams() );
		saveParams.setAccelerator(KeyCombination.valueOf("shortcut+S"));
		MenuItem openParams = new MenuItem(I18n.tr("command.open"));
		openParams.setOnAction( e -> doOpenParams() );
		openParams.setAccelerator(KeyCombination.valueOf("shortcut+O"));
		MenuItem saveImage = new MenuItem(I18n.tr("command.saveImage"));
		saveImage.setOnAction( e -> doSaveImage() );
		saveImage.setAccelerator(KeyCombination.valueOf("shortcut+shift+S"));
		MenuItem close = new MenuItem(I18n.tr("command.quit"));
		close.setOnAction( e -> Platform.exit() );
		close.setAccelerator(KeyCombination.valueOf("shortcut+Q"));
		fileMenu.getItems().addAll(saveParams,openParams, 
				new SeparatorMenuItem(), saveImage, new SeparatorMenuItem(), close);

		// Add items to the Control menu.

		MenuItem allDefaults = new MenuItem(I18n.tr("command.restoreAllDefaults"));
		allDefaults.setOnAction( e -> doAllDefaults() );
		allDefaults.setAccelerator(KeyCombination.valueOf("shortcut+shift+R"));
		MenuItem defaultLimits = new MenuItem(I18n.tr("command.defaultLimits"));
		defaultLimits.setOnAction( e -> doDefaultLimits() );
		defaultLimits.setAccelerator(KeyCombination.valueOf("shortcut+R"));
		MenuItem undoChangeOfLimits = new MenuItem(I18n.tr("Restore Previous Limits"));
		undoChangeOfLimits.setOnAction( e -> doUndoChangeOfLimits() );
		undoChangeOfLimits.setAccelerator(KeyCombination.valueOf("shortcut+U"));
		undoChangeOfLimits.setDisable(true);		
		MenuItem showLimits = new MenuItem(I18n.tr("command.showLimits"));
		showLimits.setOnAction( e -> doShowLimits() );
		showLimits.setAccelerator(KeyCombination.valueOf("shortcut+L"));
		MenuItem setLimits = new MenuItem(I18n.tr("command.enterLimits"));
		setLimits.setOnAction( e -> doSetLimits() );
		setLimits.setAccelerator(KeyCombination.valueOf("shortcut+shift+L"));
		MenuItem setImageSize = new MenuItem(I18n.tr("command.enterImageSize"));
		setImageSize.setOnAction( e -> doSetImageSize() );
		setImageSize.setAccelerator(KeyCombination.valueOf("shortcut+I"));
		controlMenu.getItems().addAll( allDefaults, new SeparatorMenuItem(), defaultLimits, undoChangeOfLimits, 
				showLimits, setLimits, new SeparatorMenuItem(), setImageSize);

		// Add items to the other three menus.  These are created by the "manager" objects.
		// and by the fillExampleMenu() method.

		paletteMenu.getItems().addAll(paletteManager.items);
		paletteLengthMenu.getItems().addAll(paletteLengthManager.items);
		maxIterationsMenu.getItems().addAll(maxIterationsManager.items);

		fillExampleMenu(exampleMenu);

		// Some commands are disabled when a computation is in progress in the display.
		
		saveImage.disableProperty().bind(owner.getDisplay().workingProperty());
		saveParams.disableProperty().bind(owner.getDisplay().workingProperty());
		openParams.disableProperty().bind(owner.getDisplay().workingProperty());
		setLimits.disableProperty().bind(owner.getDisplay().workingProperty());
		setImageSize.disableProperty().bind(owner.getDisplay().workingProperty());

		owner.limitsProperty().addListener( (o,oldVal,newVal) -> {
			    // Save old value of limitsProperty for use in "Restore Previous Limits".
			previousLimits = oldVal;
			undoChangeOfLimits.setDisable( previousLimits == null );
		});

	} // end constructor


	/**
	 * If one of the save or open commands has been used to save or load a file, then
	 * fileDialogProperty will be the directory that contained the saved or opened file.
	 * This method returns an absolute path name for that selected directory.  It is used
	 * by Main.java to find out the selected directory when the program ends.
	 * The directory is saved in user preferences and is restored the next time
	 * the program is run.
	 */
	public String getSelectedDirectoryInFileChooser() {
		if (fileDialogDirectory == null)
			return null;
		else 
			return fileDialogDirectory.getAbsolutePath();
	}


	/**
	 * This sets the selected directory for the file dialog.  This method
	 * is called by Main.java when the program starts, to restore the directory
	 * that was saved the last time the program was run (by the same user).
	 * @param path absolute path name to the directory; if this is
	 *   not the path name of an actual directory, then the property
	 *   is not set.
	 */
	public void setSelectedDirectoryInFileChooser(String path) {
		File dir = new File(path);
		if (dir.isDirectory()) {
			fileDialogDirectory = dir;
		}
	}


	/**
	 * Produces an XML representation of the current settings.  This is used by the
	 * doOpenParams() method to restore the setting of the program based on the contents
	 * of an XML file.  It is also used for the Examples menu.  Currently, the image size 
	 * is NOT adjusted to the value in the file; the same picture that was saved is shown, 
	 * but possibly at a different size.  No changes are made if an error occurs while
	 * processing the file.  An exception is thrown in that case.
	 */
	public void retrieveSettingsFromXML(Document xmlDoc) {
		Element docElement = xmlDoc.getDocumentElement();
		String docName = docElement.getTagName();
		if (! docName.equalsIgnoreCase("mandelbrot_settings"))
			throw new IllegalArgumentException(I18n.tr("xml.error.wrongType",docName));
		String version = docElement.getAttribute("version");
		if ( ! version.equalsIgnoreCase("edu.hws.eck.mdb/1.0"))
			throw new IllegalArgumentException(I18n.tr("xml.error.wrongSettingsVersion"));
		NodeList nodes = docElement.getChildNodes();
		int ct = nodes.getLength();
		
		/* Default values will be used if no value is found in the file, */
		int paletteItemNum = 0;
		int paletteType = MandelbrotPane.PALETTE_SPECTRUM;
		Color c1 = null, c2 = null;  // for gradient palettes
		int paletteLength = 0;
		int maxIterations = 250;
		double[] limits = new double[] { -2.5,1.1,-1.35,1.35 };
		
		for (int i = 0; i < ct; i++) {
			Node node = nodes.item(i);
			if (node instanceof Element) {
				String name = ((Element)node).getTagName();
				String value = ((Element)node).getAttribute("value");
				try {
					if (name.equalsIgnoreCase("palettetype")) {
						Object[] paletteData = paletteManager.getValueFromString(value);
						paletteItemNum = (Integer)paletteData[0];
						paletteType = (Integer)paletteData[1];
						if (paletteType == MandelbrotPane.PALETTE_GRADIENT) {
							c1 = (Color)paletteData[2];
							c2 = (Color)paletteData[3];
						}
					}
					else if (name.equalsIgnoreCase("palettelength"))
						paletteLength = paletteLengthManager.getValueFromString(value);
					else if (name.equalsIgnoreCase("maxiterations"))
						maxIterations = maxIterationsManager.getValueFromString(value);
					else if (name.equalsIgnoreCase("limits")) {
						String[] limitStrings = value.split(",");
						double xmin = Double.parseDouble(limitStrings[0]);
						double xmax = Double.parseDouble(limitStrings[1]);
						double ymin = Double.parseDouble(limitStrings[2]);
						double ymax = Double.parseDouble(limitStrings[3]);
						if (xmin >= xmax || ymin >= ymax)
							throw new IllegalArgumentException();
						limits = new double[] { xmin, xmax, ymin, ymax };
					}
				}
				catch (Exception e) {
					throw new IllegalArgumentException(I18n.tr("xml.error.illegalSettingsValue",name,value));
				}
			}
		}
		owner.setParams(maxIterations,paletteType,c1,c2,paletteLength,limits);
		paletteManager.setItemNum(paletteItemNum);
		paletteLengthManager.setValue(paletteLength);
		maxIterationsManager.setValue(maxIterations);
	}


	/**
	 * This is used by the Save Params action to create an XML representation of 
	 * the current settings.  (It is public but is not currently used outside this class.)
	 */
	public String currentSettingsAsXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version='1.0'?>\n");
		buffer.append("<mandelbrot_settings version='edu.hws.eck.mdb/1.0'>\n");
		double[] limits = owner.getRequestedLimits();
		String limitString = limits[0] + "," + limits[1] + "," + limits[2] + "," + limits[3];
		buffer.append("<limits value='"+ limitString + "'/>\n");
		String sizeString = owner.getDisplay().getWidth() + "," + owner.getDisplay().getHeight();
		buffer.append("<imagesize value='"+ sizeString + "'/>\n");
		buffer.append("<maxiterations value='" + maxIterationsManager.valueAsString() + "'/>\n");
		buffer.append("<palettetype value='" + paletteManager.valueAsString() + "'/>\n");
		buffer.append("<palettelength value='" + paletteLengthManager.valueAsString() + "'/>\n");
		buffer.append("</mandelbrot_settings>\n");
		return buffer.toString();
	}


	//------------------ Everything after this point is private --------------------------


	private MandelbrotPane owner;  // From the parameter to the constructor.

	private PaletteManager paletteManager;              // Manages Palette menu; defined by nested class below.
	private PaletteLengthManager paletteLengthManager;  // Manages PaletteLength menu; defined by nested class below.
	private MaxIterationsManager maxIterationsManager;  // Manages MaxIterations menu; defined by nested class below.

	private File fileDialogDirectory; // Save selected directory from fileDialog.
	private double[] previousLimits;  // For the Restore Previous Limits command.



	/**
	 * A little utility method that makes strings out of the xy-limits on the display,
	 * where the lengths of the string is adjusted depending on the distance between
	 * xmax and xmin.  The idea is to try to avoid more digits after the decimal
	 * points than makes sense.  If it succeeds, the coordinates that are shown for xmin
	 * and xmax should differ only in their last four or five digits and the same should
	 * also be true for ymin and ymax.
	 * @return An array of 4 strings representing the values of xmin, xmax, ymin, ymax.
	 */
	private String[] makeScaledLimitStrings() {
		double[] limits = owner.getLimits();
		double xmin = limits[0];
		double xmax = limits[1];
		double ymin = limits[2];
		double ymax = limits[3];
		double diff = xmax - xmin;
		if (diff == 0)
			return new String[] { ""+xmin, ""+xmax, ""+ymin, ""+ymax };
		int scale = 4;
		if (diff > 0) {
			while (diff < 1) {
				scale++;
				diff *= 10;
			}
		}
		String fmt = "%1." + scale + "f";
		String[] str = new String[4];
		str[0] = String.format(fmt,xmin);
		str[1] = String.format(fmt,xmax);
		str[2] = String.format(fmt,ymin);
		str[3] = String.format(fmt,ymax);
		return str;
	}


	/**
	 * Save an XML file representing the current settings of the program.
	 * The XML file is in the format that is loaded by doOpenParams.
	 */
	private void doSaveParams() {
		FileChooser fileDialog = new FileChooser();
		fileDialog.setInitialFileName(I18n.tr("files.saveparams.defaultFileName"));
		if (fileDialogDirectory == null)
			fileDialog.setInitialDirectory(new File(System.getProperty("user.home")));
		else
			fileDialog.setInitialDirectory(fileDialogDirectory);
		fileDialog.setTitle(I18n.tr("files.saveparams.title"));
		File selectedFile = fileDialog.showSaveDialog(owner.getScene().getWindow());
		if (selectedFile == null)
			return;
		PrintWriter out; 
		try {
			FileOutputStream stream = new FileOutputStream(selectedFile); 
			out = new PrintWriter( stream );
		}
		catch (Exception e) {
			error(I18n.tr("files.saveparams.error.cannotOpen", 
					selectedFile.getName(), e.toString()));
			return;
		}
		try {
			out.print(currentSettingsAsXML());
			out.close();
			try {
				File dir = selectedFile.getParentFile();
				if (dir.isDirectory())
					fileDialogDirectory = dir;
			}
			catch (Exception e) {
			}
		}
		catch (Exception e) {
			error(I18n.tr("files.saveparams.error.cannotWrite", 
					selectedFile.getName(), e.toString()));
		}   
	}


	/**
	 * Open an XML file and, if it is successfully parsed, restore the
	 * image to the settings that were stored in the file.
	 */
	private void doOpenParams() {
		FileChooser fileDialog = new FileChooser();
		fileDialog.setTitle(I18n.tr("files.openparams.title"));
		if (fileDialogDirectory == null)
			fileDialog.setInitialDirectory(new File(System.getProperty("user.home")));
		else
			fileDialog.setInitialDirectory(fileDialogDirectory);
		File selectedFile = fileDialog.showOpenDialog(owner.getScene().getWindow());
		if (selectedFile == null)
			return;  // User canceled or clicked the dialog's close box.
		Document xmldoc;
		try {
			DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			xmldoc = docReader.parse(selectedFile);
		}
		catch (Exception e) {
			error(I18n.tr("files.openparams.error.notXML", selectedFile.getName(), e.toString()));
			return;
		}
		try {
			retrieveSettingsFromXML(xmldoc);
			try {
				File dir = selectedFile.getParentFile();
				if (dir.isDirectory())
					fileDialogDirectory = dir;
			}
			catch (Exception e) {
			}
		}
		catch (Exception e) {
			error(I18n.tr("files.openparams.error.notParamsFile", selectedFile.getName(), e.getMessage()));
		}   
	}


	/**
	 * Save the current image as a PNG file.  Only the PNG file format is available.
	 */
	public void doSaveImage() {
		FileChooser fileDialog = new FileChooser();
		fileDialog.setInitialFileName(I18n.tr("files.saveimage.defaultFileName")); 
		if (fileDialogDirectory == null)
			fileDialog.setInitialDirectory(new File(System.getProperty("user.home")));
		else
			fileDialog.setInitialDirectory(fileDialogDirectory);
		fileDialog.setTitle(I18n.tr("files.saveimage.title"));
		File selectedFile = fileDialog.showSaveDialog(owner.getScene().getWindow());
		if ( selectedFile == null )
			return;  // User did not select a file.
		try {
			Image canvasImage = owner.getDisplay().snapshot(null,null);
			BufferedImage image = SwingFXUtils.fromFXImage(canvasImage,null);
			String filename = selectedFile.getName().toLowerCase();
			if ( ! filename.endsWith(".png")) {
				error(I18n.tr("file.saveimage.pngOnly"));
				return;
			}
			boolean hasFormat = ImageIO.write(image,"PNG",selectedFile);
			if ( ! hasFormat ) { // (this should never happen)
				error(I18n.tr("files.saveimage.noPNG"));
			}
			try {
				File dir = selectedFile.getParentFile();
				if (dir.isDirectory())
					fileDialogDirectory = dir;
			}
			catch (Exception e) {
			}
		}
		catch (Exception e) {
			error(I18n.tr("files.saveimage.cantwrite", selectedFile.getName(), e.toString()));
		}   
	}


	/**
	 * Restores limits to their default values, showing the entire Mandelbrot Set.
	 */
	private void doDefaultLimits() {
		owner.setLimits(-2.5,1.1,-1.35,1.35);
	}


	/**
	 * Restores default limits, palette, and maxIterations.
	 */
	private void doAllDefaults() {
		owner.defaults();
		paletteManager.setItemNum(0);      // Change menus to match default settings
		paletteLengthManager.setValue(0);
		maxIterationsManager.setValue(250);
	}


	/**
	 * Restores previous xy-limits on MandelbrotPanel.  The previous limits are
	 * obtained from an event that is emitted by an observable property of the display whenever
	 * the limits change.  The change event handler stores the old limits in the previousLimits
	 * instance variable.
	 */
	private void doUndoChangeOfLimits() {
		if (previousLimits != null)
			owner.setLimits(previousLimits[0],previousLimits[1],
					previousLimits[2],previousLimits[3]);
	}


	/**
	 * Puts up a message alert that contains the current range of xy-values
	 * that is shown in the MandelbrotPane.
	 */
	private void doShowLimits() {
		String[] limits = makeScaledLimitStrings();
		Alert alert = new Alert(Alert.AlertType.INFORMATION, 
				I18n.tr("dialog.showLimits",limits[0],limits[1],limits[2],limits[3]));
		alert.setHeaderText(null);
		alert.setGraphic(null);
		alert.showAndWait();
	}


	/**
	 * Puts up a dialog box of type SetImageSizeDialog (another class defined in
	 * this package).  The dialog box lets the user enter new values for the
	 * width and height of the image.  If the user does not cancel, then the
	 * new width and height are applied to the image.  The dialog box ensures
	 * that the returned values, if any, are legal
	 */
	private void doSetImageSize() {
		int oldWidth = (int)owner.getDisplay().getWidth();
		int oldHeight = (int)owner.getDisplay().getHeight();
		int[] newSize = SetImageSizeDialog.showDialog(new int[] {oldWidth, oldHeight});
		if (newSize == null) // user canceled the dialog
			return;
		owner.setImageSize(newSize[0], newSize[1]);
	}


	/**
	 * Puts up a dialog box of type SetLimitsDialog (another class defined in
	 * this package).  The dialog box lets the user enter new values for xmin,
	 * xmax, ymin, and ymax (the limits of the range of xy-values shown in the
	 * MandelbrotPane).  If the user does not cancel, the new limits are
	 * applied to the display.
	 */
	private void doSetLimits() {
		String[] limits = makeScaledLimitStrings();
		double[] newLimits = SetLimitsDialog.showDialog(limits);
		if (newLimits != null) // User canceled the dialog
			owner.setLimits(newLimits[0],newLimits[1],newLimits[2],newLimits[3]);
	}

	
	/**
	 * Adds names of settings files to the examples menu.  See the global variable
	 * SETTINGS_FILE_LIST for more info.  Installs event handlers on each menu
	 * item to load the corresponding settings file.
	 */
	private void fillExampleMenu(Menu menu) { 
		for (int i = 0; i < SETTINGS_FILE_LIST.length; i++) {
			final String str = SETTINGS_FILE_LIST[i];
			MenuItem item = new MenuItem(str);
			item.setOnAction( e -> loadExampleFile(str) );
			menu.getItems().add(item);
		}
	}

	/**
	 * Reads settings from an XML resource file.
	 */
	private void loadExampleFile(String resourceName) {  // Tries to load one of the examples.
		resourceName = "edu/hws/eck/mdbfx/examples/" + resourceName + ".mdb";
		ClassLoader cl = getClass().getClassLoader();
		URL resourceURL = cl.getResource(resourceName);
		if (resourceURL != null) {
			try {
				InputStream stream = resourceURL.openStream();
				DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document xmldoc = docReader.parse(stream);
				retrieveSettingsFromXML(xmldoc);
			}
			catch (Exception e) {
				error("Internal Error.  Couldn't load example\n" + e);
			}
		}
		else {
			error("Internal Error.  Couldn't find file.");
		}
	}

	
	/**
	 * Show an error alert with the given message.
	 */
	private void error(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR, message);
		alert.setHeaderText(null);
		alert.showAndWait();
	}


	/**
	 * Defines the object that manages the Palette menu.
	 */
	private class PaletteManager {

		RadioMenuItem[] items;  // Array contains all the items that are in the Palette menu.
		int selectedItem = 0;  // Index in the items array of the item that is currently selected.
		private String[] valueStrings = {"Spectrum","PaleSpectrum","Grayscale","CyclicGrayscale",
				"BlackToRed","RedToCyan","OrangeToBlue"};  // Names for commands in XML settings file.

		PaletteManager() {
				// Constructor creates the items and adds them to a ToggleGroup.  Also this
				// object adds itself as an ActionListener to each item so it can carry out
				// the command when the user selects one of the items.
			items = new RadioMenuItem[8];
			items[0] = new RadioMenuItem(I18n.tr("command.palette.spectrum"));
			items[1] = new RadioMenuItem(I18n.tr("command.palette.paleSpectrum"));
			items[2] = new RadioMenuItem(I18n.tr("command.palette.grayscale"));
			items[3] = new RadioMenuItem(I18n.tr("command.palette.cyclicGrayscale"));
			items[4] = new RadioMenuItem(I18n.tr("command.palette.gradientBlackToRed"));
			items[5] = new RadioMenuItem(I18n.tr("command.palette.gradientRedToCyan"));
			items[6] = new RadioMenuItem(I18n.tr("command.palette.gradientOrangeToBlue"));
			items[7] = new RadioMenuItem(I18n.tr("command.palette.customGradient"));
			ToggleGroup grp = new ToggleGroup();
			for (int i = 0; i < items.length; i++) {
				items[i].setToggleGroup(grp);
				items[i].setOnAction( e -> applySelection() );
			}
			items[selectedItem].setSelected(true);
		}

		String valueAsString() {
				// Converts the setting of this menu to a string that can be saved
				// in an XML file.  This is used by the currentSettingAsXML() method,
				// which is used in turn by the SaveParams command.
			if (selectedItem < valueStrings.length)
				return valueStrings[selectedItem];
			else {
				Color c1 = owner.getGradientPaletteColor1();
				Color c2 = owner.getGradientPaletteColor2();
				if (c1 == null || c2 == null)
					return valueStrings[0];  // Should not happen!
				return "Custom/" + c1.getRed() + "," + c1.getGreen() + "," + c1.getBlue() + "/"
				+ c2.getRed() + "," + c2.getGreen() + "," + c2.getBlue();
			}
		}
		
		void setItemNum(int itemNum) {
			    // Select specifed item number in the menu.
			selectedItem = itemNum;
			items[itemNum].setSelected(true);
		}

		Object[] getValueFromString(String str) {
				// Takes a string from an XML file (which originally came from the
				// previous method when the file was saved) and restores the setting
				// represented by that string.  This is called by the retrieveSettingsFromXML()
				// method, which is called in turn by the Open Params command.
			for (int i = 0; i < 6; i++) {
				if (valueStrings[i].equalsIgnoreCase(str)) {
					switch(i) {
					case 0: return new Object[] { 0, MandelbrotPane.PALETTE_SPECTRUM };
					case 1: return new Object[] { 1, MandelbrotPane.PALETTE_PALE_SPECTRUM };
					case 2: return new Object[] { 2, MandelbrotPane.PALETTE_GRAYSCALE };
					case 3: return new Object[] { 3, MandelbrotPane.PALETTE_CYCLIC_GRAYSCALE };
					case 4: return new Object[] { 4, MandelbrotPane.PALETTE_GRADIENT, Color.BLACK, Color.RED };
					case 5: return new Object[] { 5, MandelbrotPane.PALETTE_GRADIENT, Color.RED, Color.CYAN };
					case 6: return new Object[] { 6, MandelbrotPane.PALETTE_GRADIENT, Color.rgb(255,130,20), Color.BLUE };
					}
				}
			}
			String[] tokens = str.split("[/,]");
			if ( ! tokens[0].equalsIgnoreCase("custom"))
				throw new IllegalArgumentException();
			Color c1 = Color.color( Double.parseDouble(tokens[1]), 
					Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]) );
			Color c2 =Color.color( Double.parseDouble(tokens[4]), 
					Double.parseDouble(tokens[5]), Double.parseDouble(tokens[6]) );
			return new Object[] { 7, MandelbrotPane.PALETTE_GRADIENT, c1, c2 };
		}

		private void applySelection() {
				// Sets the palette in the MandelbrotPane to match the
				// currently selected item in the menu.
			if (items[0].isSelected()) {
				owner.setPaletteType(MandelbrotPane.PALETTE_SPECTRUM);
				selectedItem = 0;
			}
			else if (items[1].isSelected()) {
				owner.setPaletteType(MandelbrotPane.PALETTE_PALE_SPECTRUM);
				selectedItem = 1;
			}
			else if (items[2].isSelected()) {
				owner.setPaletteType(MandelbrotPane.PALETTE_GRAYSCALE);
				selectedItem = 2;
			}
			else if (items[3].isSelected()) {
				owner.setPaletteType(MandelbrotPane.PALETTE_CYCLIC_GRAYSCALE);
				selectedItem = 3;
			}
			else if (items[4].isSelected()) {
				owner.setGradientPalette(Color.BLACK, Color.RED);
				selectedItem = 4;
			}
			else if (items[5].isSelected()) {
				owner.setGradientPalette(Color.RED, Color.CYAN);
				selectedItem = 5;
			}
			else if (items[6].isSelected()) {
				owner.setGradientPalette( Color.rgb(255,130,20), Color.rgb(0,0,255));
				selectedItem = 6;
			}
			else {  // The setting is for a custom gradient.  NOTE that this case never occurs when
					// this method is called from the setValueFromString() method; it only occurs
					// when called from actionPerformed() in response to a user action.  The
					// command is "Custom gradient", and the response is to show two Color
					// dialog boxes where the user can pick the start and end color for the
					// gradient.  Note that if the user CANCELS, then the state of the
					// MandelbrotPane is not changed, and the menu must be reset to show
					// the selection that was in place before the user action so that the
					// menu will properly reflect the state of the display.  This is the
					// main reason why I keep the current selectedItem in an instance variable.
				Color c1 = Color.GREEN;
				Color c2 = Color.YELLOW;
				if (owner.getPaletteType() == MandelbrotPane.PALETTE_GRADIENT) {
						// If the display is already using a gradient palette, then the colors
						// from that gradient will be used as the initially selected colors in
						// the color chooser dialog boxes.
					c1 = owner.getGradientPaletteColor1();
					c2 = owner.getGradientPaletteColor2();
				}
				c1 = colorChooser(c1, I18n.tr("dialog.selectGradient1"));
				if (c1 == null) {
					items[selectedItem].setSelected(true);  // Restore previous selection in menu.
					return;                                 // (The menu selection has been changed by the user
				}                                           // action, but the value of selectedItem has not changed.)
				c2 = colorChooser(c2, I18n.tr("dialog.selectGradient2"));
				if (c2 == null) {
					items[selectedItem].setSelected(true);
					return;
				}
				owner.setGradientPalette(c1, c2);
				selectedItem = 7;
			}
		}
	} // end nested class PaletteManager


	/**
	 * Defines the object that manages the PaletteLength menu.  Similar in
	 * structure to PaletteManager; see above.
	 */
	private class PaletteLengthManager {
		int[] standardLengths = { 25, 50, 100, 250, 500, 1000, 2000, 5000, 10000 };
		int selectedItem = 0;
		RadioMenuItem[] items;
		PaletteLengthManager() {
			items = new RadioMenuItem[ 2 + standardLengths.length ];
			items[0] = new RadioMenuItem(I18n.tr("command.palette.lengthTracksMaxIterations"));
			for (int i = 0; i < standardLengths.length; i++)
				items[i+1] = new RadioMenuItem(
						I18n.tr("command.palette.length", ""+standardLengths[i]));
			items[items.length-1] = new RadioMenuItem(I18n.tr("command.palette.customLength"));
			ToggleGroup grp = new ToggleGroup();
			for (int i = 0; i < items.length; i++) {
				items[i].setToggleGroup(grp);
				items[i].setOnAction(e -> itemSelected());
			}
			items[selectedItem].setSelected(true);
		}
		String valueAsString() {
			return "" + owner.getPaletteLength();
		}
		void setValue(int value) {
			if (value == 0) {
				selectedItem = 0;
				items[0].setSelected(true);
				return;
			}
			for (int i = 0; i < standardLengths.length; i++) {
				if (value == standardLengths[i]) {
					selectedItem = i+1;
					items[i+1].setSelected(true);
					return;
				}
			}
			items[items.length-1].setSelected(true);
			selectedItem = items.length - 1;
		}
		int getValueFromString(String str) {
			int length = Integer.parseInt(str);
			if (length == 0) {
				return 0;
			}
			if (length < 2 || length > 500000)
				throw new IllegalArgumentException();
			return length;
		}
		public void itemSelected() {
			if (items[0].isSelected()) {
				owner.setPaletteLength(0);
				selectedItem = 0;
			}
			else if (items[items.length-1].isSelected()) {
				TextInputDialog dialog = new TextInputDialog();
				dialog.setHeaderText(I18n.tr("command.palette.customLengthQuestion",
						owner.getPaletteLength()));
				Optional<String> resp = dialog.showAndWait();
				if (!resp.isPresent() || resp.get() == null || resp.get().trim().length() == 0) {
					items[selectedItem].setSelected(true);
					return;
				}
				try {
					int length = Integer.parseInt(resp.get().trim());
					if (length < 2)
						throw new NumberFormatException();
					if (length > 500000)
						throw new NumberFormatException();
					owner.setPaletteLength(length);
					selectedItem = items.length - 1;
				}
				catch (NumberFormatException e) {
					error(I18n.tr("command.palette.customLengthError",resp.get().trim()));
					items[selectedItem].setSelected(true);
					return;
				}
			}
			else {
				for (int i = 0; i < standardLengths.length; i++) {
					if (items[i+1].isSelected()) {
						owner.setPaletteLength(standardLengths[i]);
						selectedItem = i+1;
						break;
					}
				}
			}
		}
	} // end nested class PaletteLengthManager


	/**
	 * Defines the object that manages the MaxIterations menu.  Similar in
	 * structure to PaletteManager; see above.
	 */
	private class MaxIterationsManager{
		int[] standardValues = { 50, 100, 250, 500, 1000, 2000, 5000, 20000, 50000, 100000 };
		int selectedItem = 2;
		RadioMenuItem[] items;
		MaxIterationsManager() {
			items = new RadioMenuItem[ 1 + standardValues.length ];
			for (int i = 0; i < standardValues.length; i++)
				items[i] = new RadioMenuItem(
						I18n.tr("command.maxiterations", ""+standardValues[i]));
			items[items.length-1] = new RadioMenuItem(I18n.tr("command.maxiterations.custom"));
			ToggleGroup grp = new ToggleGroup();
			for (int i = 0; i < items.length; i++) {
				items[i].setToggleGroup(grp);
				items[i].setOnAction( e -> itemSelected() );
			}
			items[selectedItem].setSelected(true);
		}
		String valueAsString() {
			return "" + owner.getMaxIterations();
		}
		void setValue(int value) {
			for (int i = 0; i < standardValues.length; i++) {
				if (value == standardValues[i]) {
					selectedItem = i;
					items[i].setSelected(true);
					return;
				}
			}
			items[items.length-1].setSelected(true);
			selectedItem = items.length - 1;
		}
		int getValueFromString(String str) {
			int length = Integer.parseInt(str);
			if (length < 2 || length > 500000)
				throw new IllegalArgumentException();
			return length;
		}
		public void itemSelected() {
			if (items[items.length-1].isSelected()) {
				TextInputDialog dialog = new TextInputDialog();
				dialog.setHeaderText(I18n.tr("command.maxiterations.customQuestion",owner.getMaxIterations()));
				Optional<String> resp = dialog.showAndWait();
				if (!resp.isPresent() || resp.get() == null || resp.get().trim().length() == 0) {
					items[selectedItem].setSelected(true);
					return;
				}
				try {
					int value = Integer.parseInt(resp.get().trim());
					if (value < 2)
						throw new NumberFormatException();
					if (value > 500000)
						throw new NumberFormatException();
					owner.setMaxIterations(value);
					selectedItem = items.length - 1;
				}
				catch (NumberFormatException e) {
					error(I18n.tr("command.maxiterations.customError",resp.get().trim()));
					items[selectedItem].setSelected(true);
					return;
				}
			}
			else {
				for (int i = 0; i < standardValues.length; i++) {
					if (items[i].isSelected()) {
						owner.setMaxIterations(standardValues[i]);
						selectedItem = i;
						break;
					}
				}
			}
		}
	} // end nested class MaxIterationsManager


	//---------- implementing a ColorChooser dialog box (from SimpleDialogs.java) ------------------------

	/**
	 *  This component shows six sliders that the user can manipulate
	 *  to set the red, green, blue, hue, brightness, and saturation components
	 *  of a color.  A color patch shows the selected color, and there are
	 *  six labels that show the numerical values of all the components.
	 */
	private static class ColorChooserPane extends GridPane {

		private Slider hueSlider, brightnessSlider, saturationSlider,  // Sliders to control color components.
		redSlider, greenSlider, blueSlider;

		private Label hueLabel, brightnessLabel, saturationLabel,  // For displaying color component values.
		redLabel, greenLabel, blueLabel;

		private Pane colorPatch;  // Color patch for displaying the color.

		private Color currentColor;

		public ColorChooserPane(Color initialColor) {

			/* Create Sliders with possible values from 0 to 1, or 0 to 360 for hue. */

			hueSlider = new Slider(0,360,0);
			saturationSlider = new Slider(0,1,1);
			brightnessSlider = new Slider(0,1,1);
			redSlider = new Slider(0,1,1);
			greenSlider = new Slider(0,1,0);
			blueSlider = new Slider(0,1,0);

			/* Set up listeners to respond when a slider value is changed. */

			hueSlider.valueProperty().addListener( e -> newColor(hueSlider) );
			saturationSlider.valueProperty().addListener( e -> newColor(saturationSlider) );
			brightnessSlider.valueProperty().addListener( e -> newColor(brightnessSlider) );
			redSlider.valueProperty().addListener( e -> newColor(redSlider) );
			greenSlider.valueProperty().addListener( e -> newColor(greenSlider) );
			blueSlider.valueProperty().addListener( e -> newColor(blueSlider) );

			/* Create Labels showing current RGB and HSB values. */

			hueLabel = makeText(String.format(" Hue = %1.3f", 0.0));
			saturationLabel = makeText(String.format(" Saturation = %1.3f", 1.0));
			brightnessLabel = makeText(String.format(" Brightness = %1.3f", 1.0));
			redLabel = makeText(String.format(" Red = %1.3f", 1.0));
			greenLabel = makeText(String.format(" Green = %1.3f", 1.0));
			blueLabel = makeText(String.format(" Blue = %1.3f", 1.0));

			/* Create an object to show the currently selected color. */

			colorPatch = new Pane();
			colorPatch.setStyle("-fx-background-color:red; -fx-border-color:black; -fx-border-width:2px");

			/* Lay out the components */

			GridPane root = this;
			ColumnConstraints c1 = new ColumnConstraints();
			c1.setPercentWidth(33);
			ColumnConstraints c2 = new ColumnConstraints();
			c2.setPercentWidth(34);
			ColumnConstraints c3 = new ColumnConstraints();
			c3.setPercentWidth(33);
			root.getColumnConstraints().addAll(c1, c2, c3);

			root.add(hueSlider, 0, 0);
			root.add(saturationSlider, 0, 1);
			root.add(brightnessSlider, 0, 2);
			root.add(redSlider, 0, 3);
			root.add(greenSlider, 0, 4);
			root.add(blueSlider, 0, 5);
			root.add(hueLabel, 1, 0);
			root.add(saturationLabel, 1, 1);
			root.add(brightnessLabel, 1, 2);
			root.add(redLabel, 1, 3);
			root.add(greenLabel, 1, 4);
			root.add(blueLabel, 1, 5);
			root.add(colorPatch, 2, 0, 1, 6);  // occupies 6 rows!
			root.setStyle("-fx-padding:5px; -fx-border-color:darkblue; -fx-border-width:2px; -fx-background-color:#DDF");

			setColor(initialColor == null? Color.BLACK : initialColor);
		}


		public Color getColor() {
			return currentColor;
		}

		public void setColor(Color color) {
			if (color == null)
				return;
			hueSlider.setValue(color.getHue());
			brightnessSlider.setValue(color.getBrightness());
			saturationSlider.setValue(color.getSaturation());
			redSlider.setValue(color.getRed());
			greenSlider.setValue(color.getGreen());
			blueSlider.setValue(color.getBlue());
			String colorString = String.format("#%02x%02x%02x", (int)(255*color.getRed()),
					(int)(255*color.getGreen()), (int)(255*color.getBlue()) );
			colorPatch.setStyle("-fx-border-color:black; -fx-border-width:2px; -fx-background-color:" + colorString);
			hueLabel.setText(String.format(I18n.tr("dialog.hue") + " = %1.3f", color.getHue()));
			saturationLabel.setText(String.format(I18n.tr("dialog.saturation") + " = %1.3f", color.getSaturation()));
			brightnessLabel.setText(String.format(I18n.tr("dialog.brightness") + " = %1.3f", color.getBrightness()));
			redLabel.setText(String.format(I18n.tr("dialog.red") + " = %1.3f", color.getRed()));
			greenLabel.setText(String.format(I18n.tr("dialog.green") + " = %1.3f", color.getGreen()));
			blueLabel.setText(String.format(I18n.tr("dialog.blue") + " = %1.3f", color.getBlue()));
			currentColor = color;
		}


		private Label makeText(String message) {
				// Make a label to show a given message shown in bold, with some padding
				// between the text and the border of the label.
			Label text = new Label(message);
			text.setStyle(" -fx-padding: 6px 10px 6px 10px; -fx-font-weight:bold");
			return text;
		}


		private void newColor(Slider whichSlider) {
				// Adjust the GUI to a new color value, when one of the sliders has changed.
			if ( ! whichSlider.isValueChanging() ) {
				return; // Don't respond to change if it was set programmatically;
						// only respond if it was set by user dragging the slider.
			}
			Color color;
			if (whichSlider == redSlider || whichSlider == greenSlider || whichSlider == blueSlider) {
				color = Color.color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
				hueSlider.setValue(color.getHue());
				brightnessSlider.setValue(color.getBrightness());
				saturationSlider.setValue(color.getSaturation());
			}
			else {
				color = Color.hsb(hueSlider.getValue(), saturationSlider.getValue(), brightnessSlider.getValue());
				redSlider.setValue(color.getRed());
				greenSlider.setValue(color.getGreen());
				blueSlider.setValue(color.getBlue());
			}
			currentColor = color;
			String colorString = String.format("#%02x%02x%02x", (int)(255*color.getRed()),
					(int)(255*color.getGreen()), (int)(255*color.getBlue()) );
			colorPatch.setStyle("-fx-border-color:black; -fx-border-width:2px; -fx-background-color:" + colorString);
			hueLabel.setText(String.format(I18n.tr("dialog.hue") + " = %1.3f", color.getHue()));
			saturationLabel.setText(String.format(I18n.tr("dialog.saturation") + " = %1.3f", color.getSaturation()));
			brightnessLabel.setText(String.format(I18n.tr("dialog.brightness") + " = %1.3f", color.getBrightness()));
			redLabel.setText(String.format(I18n.tr("dialog.red") + " = %1.3f", color.getRed()));
			greenLabel.setText(String.format(I18n.tr("dialog.green") + " = %1.3f", color.getGreen()));
			blueLabel.setText(String.format(I18n.tr("dialog.blue") + " = %1.3f", color.getBlue()));
		}	


	}  // end class SimpleColorChooser

	/**
	 * Shows a dialog box containing a simple color chooser pane that the user
	 * can manipulate to select a color.  The dialog box has an OK button and
	 * a "Cancel" button.
	 * @param initialColor the color that is initially selected in the dialog.
	 *     If the value is null, the initial color is black.
	 * @param headerText Text to be shown in the dialog above the color chooser
	 *     pane.  Can be null.  For multi-line text, the \n character should
	 *     be included in the string to separate the lines.
	 * @return null if the user cancels the dialog, or the color that is selected
	 *    in the color chooser pane if the user dismisses the dialog box by
	 *    clicking the "OK" button.
	 */
	private static Color colorChooser( Color initialColor, String headerText ) {
		ColorChooserPane chooser = new ColorChooserPane(initialColor);

		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle(I18n.tr("dialog.colorpicker"));
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
		dialog.getDialogPane().setContent(chooser);
		dialog.setHeaderText(headerText);

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK )
			return chooser.getColor();
		else
			return null;
	}


} // end class Menus
