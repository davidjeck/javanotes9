import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;

import java.io.StringReader;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

/**
 * A demonstration of simple XML parsing and processing using the Document Object
 * Model.  XML can be entered into one text area (at the top).  When the user
 * clicks "Parse XML Input", the input is parsed to give a DOM representation
 * of the XML.  If this succeeds, the DOM representation is traversed and information
 * is output about the nodes that are encountered.  Only Element, Text, and
 * Attribute nodes are understood by this program.  If the input is not well-formed
 * XML, an error message is output.  Output goes to a second text area in the
 * middle of the panel.  Initially, the input box contains a sample XML document.
 * 
 * This class has a main() routine, so it can be run as a stand-alone application.
 */
public class XMLDemo extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	//----------------------------------------------------------

	private final static String initialInput = 
			        "<?xml version=\"1.0\"?>\n" +
					"<simplepaint version=\"1.0\">\n" +
					"   <background red='1' green='0.6' blue='0.2'/>\n" +
					"   <curve>\n" +
					"      <color red='0' green='0' blue='1'/>\n" +
					"      <symmetric>false</symmetric>\n" +
					"      <point x='83' y='96'/>\n" +
					"      <point x='116' y='149'/>\n" +
					"      <point x='159' y='215'/>\n" +
					"      <point x='216' y='294'/>\n" +
					"      <point x='264' y='359'/>\n" +
					"      <point x='309' y='418'/>\n" +
					"      <point x='371' y='499'/>\n" +
					"      <point x='400' y='543'/>\n" +
					"   </curve>\n" +
					"   <curve>\n" +
					"      <color red='1' green='1' blue='1'/>\n" +
					"      <symmetric>true</symmetric>\n" +
					"      <point x='54' y='305'/>\n" +
					"      <point x='79' y='289'/>\n" +
					"      <point x='128' y='262'/>\n" +
					"      <point x='190' y='236'/>\n" +
					"      <point x='253' y='209'/>\n" +
					"      <point x='341' y='158'/>\n" +
					"   </curve>\n" +
					"</simplepaint>\n";


	private TextArea input;
	private TextArea output;

	public void start(Stage stage) {
		
		input = new TextArea(initialInput);
		input.setPrefRowCount(15);
		
		output = new TextArea();
		output.setPrefRowCount(15);
		
		Button parseButton = new Button("Parse XML Input");
		parseButton.setOnAction( e -> doParse() );
		Button clearButton = new Button("Clear");
		clearButton.setOnAction( e -> {
			input.setText("");
			output.setText("");
			input.requestFocus();
		} );
		Button restoreButton = new Button("Restore Initial Input");
		restoreButton.setOnAction( e -> {
			input.setText(initialInput);
			output.setText("");
			input.requestFocus();
		});
		
		HBox buttons = new HBox(5, parseButton, clearButton, restoreButton);
		buttons.setAlignment(Pos.CENTER);
		
		VBox root = new VBox(5, input, buttons, output);
		VBox.setVgrow(input, Priority.ALWAYS);
		VBox.setVgrow(output, Priority.ALWAYS);
		root.setStyle("-fx-border-width:5px; -fx-border-color: gray; -fx-background-color:gray");
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("XML Demo");
		stage.show();
	}


	/**
	 * Attempt to parse the text from the input box as an XML document.
	 * If an error occurs, display an error message.  If not, display
	 * information about the nodes in the DOM representation of the document.
	 */
	private void doParse() {
		output.setText("");
		String data = input.getText();
		if (data.trim().length() == 0) {
			output.setText("ERROR:  There is no text in the input box!\n");
			return;
		}
		Document xmldoc;
		try {
			DocumentBuilder docReader 
			= DocumentBuilderFactory.newInstance().newDocumentBuilder();
			xmldoc = docReader.parse(new InputSource(new StringReader(data)));
		}
		catch (Exception e) {
			output.setText("ERROR:  The input is not well-formed XML.\n\n" + e);
			return;
		}
		output.appendText("Input has been parsed successfully.\n");
		output.appendText("Nodes in the DOM representation:\n\n");
		Element root = xmldoc.getDocumentElement();
		listNodes(root,"",1);
	}


	/**
	 * Display information about the Element root, its attributes, and any nested
	 * element and text nodes.  This is a recursive routine.
	 * @param node the Element whose name, attributes, and content will be displayed
	 * @param indent spaces added at the beginning of each line of output.  This string
	 *    of spaces grows with each nested recursive call to this method.
	 * @param level increases by 1 with each nested recursive call to this method.
	 *    Gives the level of nesting of node within the root element of the document.
	 */
	private void listNodes(Element node, String indent, int level) {
		output.appendText(indent + level + ". Element named:  " + node.getTagName() + '\n');
		NamedNodeMap attributes = node.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Attr attribute = (Attr)attributes.item(i);
			output.appendText(indent + "         with attribute named: " + attribute.getName()
					+ ",  value:  " + attribute.getValue() + '\n');
		}
		indent += "   ";
		level++;
		String prefix = indent + level + ". ";
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			int nodeType = child.getNodeType();
			if (nodeType == Node.ELEMENT_NODE)
				listNodes( (Element)child, indent, level);
			else if (nodeType == Node.TEXT_NODE) {
				String text = child.getTextContent();
				text = text.trim();
				if (text.length() > 0)
					output.appendText(prefix + "Text node containing:  " + text + '\n');
			}
			else 
				output.appendText(prefix + "(Some other type of node.)\n");
		}
	}

}
