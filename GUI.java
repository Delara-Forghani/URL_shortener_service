package ceit.aut.ac.ir;

import javax.swing.*;
import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces
import java.io.IOException;


public class GUI extends JFrame implements ActionListener {
    private Label lblSearch;    // Declare a Label component
    private TextField textField; // Declare a TextField component
    private TextField ansField; // Declare a TextField component
    private Button btnSub;   // Declare a Button component
    private urlShortener urlShort;
    private writeToFile writer;

    // Constructor to setup GUI components and event handlers
    public GUI() {

        // "super" Frame, which is a Container, sets its layout to FlowLayout to arrange
        // the components from left-to-right, and flow to next row from top-to-bottom.
        setLayout(new FlowLayout());

        //Terminating the program when pressing cross button
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        lblSearch = new Label("Enter the Original URL: ");  // construct the Label component
        add(lblSearch);                    // "super" Frame container adds Label component

        textField = new TextField(20); // construct the TextField component with initial text
        textField.setEditable(true);  //set to read and write
        add(textField, BorderLayout.CENTER); // "super" Frame container adds TextField component

        btnSub = new Button("Submit");   // construct the Button component
        add(btnSub, BorderLayout.LINE_END);   // "super" Frame container adds Button component

        lblSearch = new Label("Shortened URl: ");  // construct the Label component
        add(lblSearch);  // "super" Frame container adds Label component

        ansField = new TextField(20); // construct the TextField component with initial text
        ansField.setEditable(false);  // set to read-only
        add(ansField, BorderLayout.CENTER); // "super" Frame container adds TextField component

        // "btnSub" is the source object that fires an ActionEvent when clicked.
        btnSub.addActionListener(this);


        setTitle("URL Shortener");  // "super" Frame sets its title
        setSize(500, 200);  // "super" Frame sets its initial window size


        setVisible(true); // "super" Frame shows

    }


    public static void main(String[] args) throws IOException {
        // Invoke the constructor to setup the GUI, by allocating an instance
        GUI app = new GUI();
        app.writer = new writeToFile("url_storage.txt");
        app.urlShort = new urlShortener(8);

    }

    // ActionEvent handler - Called back upon button-click.
    @Override
    public void actionPerformed(ActionEvent evt) {
        String url = textField.getText();
        textField.setText("");
        // construct a shorten url for the entered url
        String shortenUrl = urlShort.getShortUrl(url);
        if (!urlShort.getExist()) {
        //add the shorten url to the file
            writer.writeToFile(shortenUrl, url);
        //show the shorten url was generated in a text field
            ansField.setText(shortenUrl);

        } else {
            ansField.setText(shortenUrl);
        }

    }
}