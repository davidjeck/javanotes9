import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * In this program, the user enters numbers in a text field box.
 * After entering each number, the user presses return (or clicks
 * on a button).  Some statistics are displayed about all the
 * numbers that the user has entered.
 */
public class StatCalcGUI extends Application {
 
    public static void main(String[] args) {
        launch(args);
    }

    //---------------------------------------------------------------------
    
    private Label countLabel;    // A label for displaying the number of numbers.
    private Label sumLabel;      // A label for displaying the sum of the numbers.
    private Label meanLabel;     // A label for displaying the average.
    private Label standevLabel;  // A label for displaying the standard deviation.
    
    private Label message;  // A message at the top of the window.  It will
                            //   show an error message if the user's input is
                            //   not a legal number.  Otherwise, it just tells
                            //   the user to enter a number and press return.
    
    private Button enterButton;   // A button the user can press to enter a number.
                                  //    This is an alternative to pressing return.
    
    private Button clearButton;   // A button that clears all the data that the
                                  //    user has entered.
    
    private TextField numberInput;  // The input box where the user enters numbers.
    
    private StatCalc stats;  // An object that keeps track of the statistics
                             //   for all the numbers that have been entered.
    
 
    /**
     * Set up the GUI and event handling.
     */
    public void start(Stage stage) {
    
       stats = new StatCalc();
       
       numberInput = new TextField();
       numberInput.setPrefColumnCount(8);  // Makes the text box smaller than the default.
       
       enterButton = new Button("Enter");
       enterButton.setOnAction( e -> doEnter() );
       enterButton.setMaxSize(1000,1000);
       enterButton.setDefaultButton(true); // Pressing return will be equivalent to clicking this button.
       clearButton = new Button("Clear");
       clearButton.setOnAction( e -> doClear() );
       clearButton.setMaxSize(1000,1000);
       
       countLabel =   makeLabel(" Number of Entries:  0");
       sumLabel =     makeLabel(" Sum:                0.0");
       meanLabel =    makeLabel(" Average:            undefined");
       standevLabel = makeLabel(" Standard Deviation: undefined");
       
       message = new Label("Enter a number, press return:");
       message.setFont(Font.font(16));
       message.setTextFill(Color.WHITE);
       
       TilePane inputPanel = new TilePane(3,3,numberInput,enterButton,clearButton);
       inputPanel.setPrefColumns(3);
       TilePane root = new TilePane(3, 3, message, inputPanel, countLabel,
                                               sumLabel, meanLabel, standevLabel);
       root.setPrefColumns(1);
       root.setStyle("-fx-border-color:black; -fx-border-width:3; -fx-background-color:black");
       
       Scene scene = new Scene(root);
       stage.setScene(scene);
       stage.setTitle("Simple Calc GUI");
       stage.setResizable(false);
       stage.show();
       
    } // end start()

    
    /**
     * A utility routine for creating the labels that are used
     * for display.  This routine is used in the start() method.
     * @param text The text to show on the label.
     */
    private Label makeLabel(String text) {
       Label label = new Label(text);
       label.setMaxSize(1000,1000);
       label.setStyle("-fx-background-color:white; " +
                              "-fx-font-family: monospace; -fx-font-weight: bold");
       return label;
    }   
    
    
    /**
     * Clear all data, restoring the program to its original state.
     * This method is called when the user clicks the Clear button.
     */
    private void doClear() {
        stats = new StatCalc();
        numberInput.setText("");
        showData();
    }
    
    
    /**
     * Respond when the clicks the Enter button by getting a number from
     * the text input box, adding it to the StatCalc and updating the
     * four display labels.  It is possible that an error will occur,
     * in which case an error message is put into the label at the top
     * of the window.  (Because the Enter button has been set to be the
     * default button for the program, this mehod is also invoked when
     * the user presses return.)
     */
    private void doEnter() {
        double num;  // The user's number.
        try {
            num = Double.parseDouble(numberInput.getText());
        }
        catch (NumberFormatException e) {
                // The user's entry is not a legal number.  
                // Put an error message in the message label 
                // and return without entering a number.
            message.setText("\"" + numberInput.getText() + "\" is not a legal number.");
            numberInput.selectAll();
            numberInput.requestFocus();
            return;
        }
        stats.enter(num);
        showData();
    }
    
    
    /**
     *  Show the data from the StatCalc in the four output labels.
     */
    private void showData() {
        countLabel.setText(" Number of Entries:  " + stats.getCount());
        sumLabel.setText(" Sum:                " + stats.getSum());
        if (stats.getCount() == 0) {
               // Don't show any values for mean and standard deviation if
               // no numbers have been added to the data.
            meanLabel.setText(" Average:            undefined");
            standevLabel.setText(" Standard Deviation: undefined");
        }
        else {
            meanLabel.setText(" Average:            " + stats.getMean());
            standevLabel.setText(" Standard Deviation: " + stats.getStandardDeviation());
        }
        
        /* Set the message label back to its normal text, in case it has
          been showing an error message.  For the user's convenience,
          select the text in the TextField and give the input focus
          to the text field.  That way the user can just start typing
          the next number. */

        message.setText("Enter a number, click Enter:");
        numberInput.selectAll();
        numberInput.requestFocus();
    }

 
}  // end StatsCalcGUI
