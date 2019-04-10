package mainwindow;

import java.io.File;
import mainwindow.Clothing.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Add Items controller Handles add button on main window Each field is required
 * before saving list (image is optional) After saving, each list is saved as a
 * clothing object in observable list
 *
 * @version 1.0
 * @author Jingwei Sun, John Chen, Aziz Omar
 */
public class AddItemsController implements Initializable {

    //variables to be used in the controller
    private Clothing c; //Clothing class
    private ObservableList<Clothing> list; //arraylist to hold all the clothing objects
    private MainDocumentController mainController;

    private int indexOnEditing; //tracks the current index of the array being edited
    private Label label;
    private Image myImage;
    private String url;

    @FXML
    private ComboBox<Type> typeCombo;
    @FXML
    private TextField idLabel;
    @FXML
    private ComboBox<Gender> genderCombo;
    @FXML
    private ComboBox<String> sizeCombo;
    @FXML
    private ComboBox<Colors> colorCombo;
    @FXML
    private TextField priceLabel;
    @FXML
    private TextField quantityLabel;
    @FXML
    private ImageView image;
    @FXML
    private Button submitBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label confirmation;
    @FXML
    private Button imgButton;

    /**
     * Initializes the controller variables
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //connect the MainDocumentController to be able to pass values/references
        mainController = MainDocumentController.getController();
        //populate the type combo box in the add items window on load
        typeCombo.getItems().setAll(Clothing.Type.values());
        //disable all other combo boxes until type is selected
        cbDisable(true);
    }

    /**
     * Type combo box handling
     *
     * @param event Enables the combo box of other settings of clothing items
     */
    @FXML
    private void typeHandle(ActionEvent event) {

        //if statement checks the type of clothing selected and displays the appropriate sizes
        if (Clothing.Type.Dress == typeCombo.getSelectionModel().getSelectedItem()) {
            girlSize();
        } else if (Clothing.Type.Skirts == typeCombo.getSelectionModel().getSelectedItem()) {
            girlSize();
        } else if (Clothing.Type.Shorts == typeCombo.getSelectionModel().getSelectedItem()) {
            pantSize();
        } else if (Clothing.Type.Jeans == typeCombo.getSelectionModel().getSelectedItem()) {
            pantSize();
        } else if (Clothing.Type.Pants == typeCombo.getSelectionModel().getSelectedItem()) {
            pantSize();
            //every other type of clothing will have these options to choose from
        } else {
            //enables all the combo boxes
            cbDisable(false);
            sizeCombo.getItems().setAll("XS", "S", "M", "L", "XL");
            genderCombo.getItems().setAll(Clothing.Gender.values());
            colorCombo.getItems().setAll(Clothing.Colors.values());
        }
    }

    /**
     * Selecting Pants type will set the values for size, color, gender
     */
    public void pantSize() {
        //enables all the combo boxes and preset the fields
        cbDisable(false);
        sizeCombo.getItems().setAll("28W", "30W", "32W", "34W", "36W");
        colorCombo.getItems().setAll(Clothing.Colors.values());
        genderCombo.getItems().setAll(Clothing.Gender.values());
    }

    //method that displays dress size if dress/skirt is selected, only provides female/girl as gender choices
    /**
     * Selecting the Skirt or Dress type will set the values for size, color,
     * gender
     */
    public void girlSize() {
        //enable all the combo boxes and preset the fields
        cbDisable(false);
        sizeCombo.getItems().setAll("0", "2", "4", "6", "8", "10", "12");
        colorCombo.getItems().setAll(Clothing.Colors.values());
        genderCombo.getItems().setAll(Clothing.Gender.Girls, Clothing.Gender.Female);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void genderHandle(ActionEvent event) {
    }

    /**
     * Submitting the object to the ObservableList which then displays in the
     * table
     *
     * @param event Data validation of entries before object reference can be
     * passed
     */
    @FXML
    private void submitHandle(ActionEvent event) {

        //checks if the fields are completed, prompt if not
        try {
            if (typeCombo.getSelectionModel().isEmpty() || colorCombo.getSelectionModel().isEmpty()
                    || genderCombo.getSelectionModel().isEmpty()
                    || priceLabel.getText().isEmpty() || quantityLabel.getText().isEmpty()
                    || sizeCombo.getSelectionModel().isEmpty() || idLabel.getText().isEmpty()) {
                confirmation.setText("Please fill in the fields");
            } else {
                //if all fields are filled then the ID, price, and quantity must be greater than 0 otherwise display message
                if ((Integer.parseInt(idLabel.getText()) <= 0) || Double.parseDouble(priceLabel.getText()) < 0
                        || Integer.parseInt(quantityLabel.getText()) < 0) {
                    confirmation.setText("Please enter positive numbers");
                } //if product ID is above zero then iterate through the list to find matching IDs
                else if (Integer.parseInt(idLabel.getText()) > 0) {
                    boolean duplicate = false;
                    for (int i = 0; i < mainController.list.size(); i++) {

                        //if a match was found then display message and set duplicate to true
                        if (Integer.parseInt(idLabel.getText()) == mainController.list.get(i).getProductId()) {
                            confirmation.setText("Please enter an unique ID");
                            duplicate = true;
                            //if duplicate then true
                        }
                    }
                    //if all fields are valid then a new object of clothing is created
                    if (!duplicate) {
                        Clothing c = new Clothing();
                        addProduct(c); //passes the object reference to the method

                    }
                }
            }
            //display error if anything outside of numbers are entered
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("Please enter valid numbers");
            alert.showAndWait();
            System.out.println(e);
        }
    }

    /**
     * Closing the current stage
     *
     * @param event Cancels the current window
     */
    @FXML
    private void cancelHandle(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Enables or disables the combo boxes
     *
     * @param b Passes true or false values to control the combo boxes
     */
    public void cbDisable(boolean b) {
        sizeCombo.setDisable(b);
        colorCombo.setDisable(b);
        genderCombo.setDisable(b);
    }

    /**
     * Clears and resets the input fields upon submitting a product item
     */
    public void clear() {

        typeCombo.getSelectionModel().clearSelection();
        idLabel.setText("");
        genderCombo.getSelectionModel().clearSelection();
        sizeCombo.getSelectionModel().clearSelection();
        colorCombo.getSelectionModel().clearSelection();
        priceLabel.setText("");
        quantityLabel.setText("");
        image.setImage(null);
        cbDisable(false);
    }

    /**
     * Sets an object with selected values and passes it to the main controller
     * list
     *
     * @param c
     */
    public void addProduct(Clothing c) {

        c.setType(typeCombo.getValue());
        c.setProductId(Integer.parseInt(idLabel.getText()));
        c.setSize(sizeCombo.getValue());
        c.setColor(colorCombo.getValue());
        c.setGender(genderCombo.getValue());
        c.setPrice(Double.parseDouble(priceLabel.getText()));
        c.setQuantity(Integer.parseInt(quantityLabel.getText()));

        //if image does exist then store the local url
        if (image.getImage() != null) {
            if (url.startsWith("file:/")) {
                url = url.substring(6, url.length());

                this.url = url;
                c.setURL(url);
            }
        }

        //passes the object to the main controller's setList method
        mainController.setList(c);
        //displays a success message
        confirmation.setText("Item Added Successfully");
        //clear all the input fields
        clear();
    }

    /**
     * Adds image to the clothing item
     *
     * @param event Chooses the image location upon clicking
     */
    @FXML
    private void imageHandle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File(currentPath));

        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        // increased efficiency in storing img path. Now as string value
        if (file != null) {

            url = file.toURI().toString();
            myImage = new Image(url);
            image.setImage(myImage);
            this.url = url;

        }
    }

}
