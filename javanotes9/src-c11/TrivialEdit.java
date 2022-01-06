import java.io.*;
import java.util.Scanner;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;


/**
 *  This program lets the user edit short text files in a window.  When a
 *  file is being edited, the name of the file is displayed in the window's
 *  title bar.  A "File" menu provides the following commands:
 *  
 *          New  -- Clears all text from the window.
 *          Open -- Lets the user select a file and loads up to 100
 *                  lines of text from that file into the window.  The
 *                  previous contents of the window are lost.
 *          Save -- Lets the user specify an output file and saves
 *                  the contents of the window in that file.
 *          Quit -- Closes the window and ends the program.
 *
 *  This very simple program is not meant for serious text editing.  In
 *  particular, a limit of 10000 characters is put on the size of the 
 *  files that it can read.
 */
public class TrivialEdit extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//-----------------------------------------------------------------------


	private TextArea text;   // Holds the text that is displayed in the window.

	private File editFile;  // The file, if any that is currently being edited.
	                        // If non-null, this file was selected by the user
	                        // in a file open or file save dialog.

	private Stage mainWindow;  // The program window, used for changing the window title.

	/**
	 * Open a window, with a TextArea where the user can
	 * edit some text and with a menu bar for file operations.
	 */
	public void start(Stage stage) {
		mainWindow = stage;
		
		text = new TextArea();
		text.setPrefColumnCount(50);
		text.setPrefRowCount(25);
		
		BorderPane root = new BorderPane( text );
		root.setTop( makeMenuBar() );
		
		stage.setTitle("TrivialEdit: Untitled");
		stage.setScene( new Scene(root) );
		stage.show();
	}


	/**
	 * Create and return a menu bar containing a single menu, the
	 * File menu.  This menu contains four commands, New, Open, Save,
	 * and Quit.
	 */
	private MenuBar makeMenuBar() {
		Menu fileMenu = new Menu("File");

		MenuItem newCmd = new MenuItem("New");
		newCmd.setOnAction( e -> doNew() );
		fileMenu.getItems().add(newCmd);

		MenuItem openCmd = new MenuItem("Open...");
		openCmd.setOnAction( e -> doOpen() );
		fileMenu.getItems().add(openCmd);

		MenuItem saveCmd = new MenuItem("Save...");
		saveCmd.setOnAction( e -> doSave() );
		fileMenu.getItems().add(saveCmd);

		fileMenu.getItems().add( new SeparatorMenuItem() );

		MenuItem quitCmd = new MenuItem("Quit");
		quitCmd.setOnAction( e -> doQuit() );
		fileMenu.getItems().add(quitCmd);

		MenuBar bar = new MenuBar(fileMenu);
		return bar;

	} // end makeMenuBar()


	/**
	 * Carry out the "New" command from the File menu by clearing all 
	 * the text from the TextArea.  Also sets the title bar of the
	 * window to read "TrivialEdit: Untitled".
	 */
	private void doNew() {
		text.setText("");
		editFile = null;
		mainWindow.setTitle("TrivialEdit: Untitled");
	}


	/**
	 *  Carry out the Save command by letting the user specify an output file 
	 *  and writing the text from the TextArea to that file.
	 */
	private void doSave() {
		FileChooser fileDialog = new FileChooser(); 
		if (editFile == null) {
			   // No file is being edited.  Set file name in dialog to "filename.txt"
			   // and set the directory in the dialog to the user's home directory.
			fileDialog.setInitialFileName("filename.txt");
			fileDialog.setInitialDirectory( new File( System.getProperty("user.home")));
		}
		else {
			   // Get the file name and directory for the dialog from
			   // the file that is currently being edited.
			fileDialog.setInitialFileName(editFile.getName());
			fileDialog.setInitialDirectory(editFile.getParentFile());
		}
		fileDialog.setTitle("Select File to be Saved");
		File selectedFile = fileDialog.showSaveDialog(mainWindow);
		if ( selectedFile == null )
			return;  // User did not select a file.
		// Note: User has selected a file AND if the file exists has
		//    confirmed that it is OK to erase the exiting file.
		PrintWriter out; 
		try {
			FileWriter stream = new FileWriter(selectedFile); 
			out = new PrintWriter( stream );
		}
		catch (Exception e) {
			   // Most likely to occur if the user doesn't have permission to write the file.
			Alert errorAlert = new Alert(Alert.AlertType.ERROR,
					"Sorry, but an error occurred while\ntrying to open the file for output.");
			errorAlert.showAndWait();
			return;
		}
		try {
			out.print(text.getText());  // Write text from the TextArea to the file.
			out.flush(); // (Probably not needed; it's probably done by out.close();
			out.close();
			if (out.checkError())   // (need to check for errors in PrintWriter)
				throw new IOException("Error check failed.");
			editFile = selectedFile;
			mainWindow.setTitle("TrivialEdit: " + editFile.getName());
		}
		catch (Exception e) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR,
					"Sorry, but an error occurred while\ntrying to write data to the file.");
			errorAlert.showAndWait();
		}	
	}


	/**
	 * Carry out the Open command by letting the user specify a file to be opened 
	 * and reading up to 10000 characters from that file.  If the file is read 
	 * successfully and is not too long, then the text from the file replaces the 
	 * text in the TextArea.
	 */
	public void doOpen() {
		FileChooser fileDialog = new FileChooser();
		fileDialog.setTitle("Select File to be Opened");
		fileDialog.setInitialFileName(null);  // No file is initially selected.
		if (editFile == null)
			fileDialog.setInitialDirectory(new File(System.getProperty("user.home")));
		else
			fileDialog.setInitialDirectory(editFile.getParentFile());
		File selectedFile = fileDialog.showOpenDialog(mainWindow);
		if (selectedFile == null)
			return;  // User canceled.
		Scanner in;
		try {
			in = new Scanner( selectedFile );
		}
		catch (FileNotFoundException e) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR,
					"Sorry, but an error occurred\nwhile trying to open the file.");
			errorAlert.showAndWait();
			return;
		}
		try {
			StringBuilder input = new StringBuilder();
			while (in.hasNextLine()) {
				String lineFromFile = in.nextLine();
				if (lineFromFile == null)
					break;  // End-of-file has been reached.
				input.append(lineFromFile);
				input.append('\n');
				if (input.length() > 10000) {
					Alert errorAlert = new Alert(Alert.AlertType.ERROR,
							"Sorry, but an error occurred while\ntrying to read the data:\n" +
									"Input file is too large for this program.");
					errorAlert.showAndWait();
					return;
				}
			}
			text.setText(input.toString());
			editFile = selectedFile;
			mainWindow.setTitle("TrivialEdit: " + editFile.getName());
		}
		catch (Exception e) {
			Alert errorAlert = new Alert(Alert.AlertType.ERROR,
					"Sorry, but an error occurred while\ntrying to read the data.");
			errorAlert.showAndWait();
		}
		finally {
			in.close();
		}
	}


	/**
	 * Carry out the Quit command by exiting the program.
	 */
	private void doQuit() {
		System.exit(0);
	}


} // end class TrivialEdit
