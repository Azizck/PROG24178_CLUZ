package mainwindow;

import java.io.File;
import mainwindow.Clothing.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
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
 * Edit Items controller User can open this window by double clicking on the
 * item in table view or by selecting item and clicking on the edit button
 * Handles add button on main window Each field is required before saving list
 * (image is optional) After saving, each list is saved as a clothing object in
 * observable list
 *
 * @version 1.0
 * @author Jingwei Sun, John Chen, Aziz Omar
 */
public class EditItemsController implements Initializable {

    //defined variables
    private ArrayList<Clothing> clothList;
    private Clothing c;
    private MainDocumentController mainController;

    private int indexOnEditing;
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
    private Label confirmation;
    @FXML
    private Button imgButton;
    @FXML
    private Button submitBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label totalValue;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initializes the variables
        mainController = MainDocumentController.getController();
        clothList = new ArrayList<>();

        //activates the type combo box on load
        typeCombo.getItems().setAll(Clothing.Type.values());

    }

    /**
     * Type combo box handling
     *
     * @param event Enables the combo box of other settings of clothing items
     */
    @FXML
    private void typeHandle(ActionEvent event) {

        //if dress is selected, then only girl sizes are available
        if (Clothing.Type.Dress == typeCombo.getSelectionModel().getSelectedItem()) {
            girlSize();

            //if skirt is selected then only girl sizes available
        } else if (Clothing.Type.Skirts == typeCombo.getSelectionModel().getSelectedItem()) {
            girlSize();
        } else if (Clothing.Type.Shorts == typeCombo.getSelectionModel().getSelectedItem()) {
            pantSize();
        } else if (Clothing.Type.Jeans == typeCombo.getSelectionModel().getSelectedItem()) {
            pantSize();
        } else if (Clothing.Type.Pants == typeCombo.getSelectionModel().getSelectedItem()) {
            pantSize();
        } else {
            //default sizes, all gender and all colors available for any other clothing types
            sizeCombo.getItems().setAll("XS", "S", "M", "L", "XL");
            genderCombo.getItems().setAll(Clothing.Gender.values());
            colorCombo.getItems().setAll(Clothing.Colors.values());
        }
    }

    /**
     * Selecting Pants type will set the values for size, color, gender
     */
    public void pantSize() {
        sizeCombo.getItems().setAll("28W", "30W", "32W", "34W", "36W");
        colorCombo.getItems().setAll(Clothing.Colors.values());
        genderCombo.getItems().setAll(Clothing.Gender.values());
    }

    /**
     * Selecting the Skirt or Dress type will set the values for size, color,
     * gender
     */
    public void girlSize() {
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

            //sets image location and stored as url
            url = file.toURI().toString();
            myImage = new Image(url);
            image.setImage(myImage);
            this.url = url;

        }
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
            if ((genderCombo.getSelectionModel().getSelectedItem() == null)
                    || (colorCombo.getSelectionModel().getSelectedItem() == null)
                    || (sizeCombo.getSelectionModel().getSelectedItem() == null)
                    || (idLabel.getText().length() <= 0)
                    || (priceLabel.getText().length() <= 0)
                    || (quantityLabel.getText().length() <= 0)) {
                confirmation.setText("Please fill in the fields");

            } else {

                if ((Integer.parseInt(idLabel.getText()) <= 0) || Double.parseDouble(priceLabel.getText()) < 0
                        || Integer.parseInt(quantityLabel.getText()) < 0) {
                    confirmation.setText("Please enter positive numbers");
                } //Product ID entered is above zero then iterate through the list to find matching IDs
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
                    //if selected product ID matches the editing ID then it is not a duplicate
                    if (Integer.parseInt(idLabel.getText()) == mainController.list.get(indexOnEditing).getProductId()) {
                        duplicate = false;
                    }
                    if (!duplicate) {
                        //creates a new clothing object if fields are all valid and call addProduct() 
                        setList();
                        mainController.update();
                        Stage stage = (Stage) cancelBtn.getScene().getWindow();
                        stage.close();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Saved");
                        alert.setHeaderText("");
                        alert.setContentText("Item Saved Sucessfully");
                        alert.showAndWait();
                    }

                }
            }

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
     * Current index being edited
     *
     * @param i sets the indexOnEditing to the passed index value
     * @return Current index value that is being edited
     */
    public int indexEdit(int i) {
        this.indexOnEditing = i;
        return indexOnEditing;
    }

    /**
     * Edit mode sets the object's new values and passes it to the main
     * controller's list
     */
    public void setList() {

        mainController.list.get(indexOnEditing).setColor(colorCombo.getSelectionModel().getSelectedItem());
        mainController.list.get(indexOnEditing).setType(typeCombo.getSelectionModel().getSelectedItem());
        mainController.list.get(indexOnEditing).setProductId(Integer.parseInt(idLabel.getText()));
        mainController.list.get(indexOnEditing).setGender(genderCombo.getSelectionModel().getSelectedItem());
        mainController.list.get(indexOnEditing).setSize(sizeCombo.getValue());
        mainController.list.get(indexOnEditing).setPrice(Double.parseDouble(priceLabel.getText()));
        mainController.list.get(indexOnEditing).setQuantity(Integer.parseInt(quantityLabel.getText()));

        //if an edited object wants to add a new image then set the url to the corresponding image location
        if (url != mainController.list.get(indexOnEditing).getURL()) {
            if (url.startsWith("file:/")) {
                url = url.substring(6, url.length());
                this.url = url;
                mainController.list.get(indexOnEditing).setURL(url);
            }
            //otherwise if the edited object will keep the same image
        } else if (url == mainController.list.get(indexOnEditing).getURL()) {
            mainController.list.get(indexOnEditing).setURL(mainController.list.get(indexOnEditing).getURL());
        }

    }

    /**
     * Editing window displays the placeholder of the object
     *
     * @param c passes the list's specific object for viewing and editing
     */
    public void editDisplay(ObservableList<Clothing> c) {

        //displays placeholder in the edit window
        typeCombo.getSelectionModel().select(mainController.list.get(indexOnEditing).getType());
        sizeCombo.getSelectionModel().select(mainController.list.get(indexOnEditing).getSize());
        genderCombo.getSelectionModel().select(mainController.list.get(indexOnEditing).getGender());
        colorCombo.getSelectionModel().select(mainController.list.get(indexOnEditing).getColor());
        idLabel.setText(mainController.list.get(indexOnEditing).getProductId() + "");
        quantityLabel.setText(mainController.list.get(indexOnEditing).getQuantity() + "");
        priceLabel.setText(mainController.list.get(indexOnEditing).getPrice() + "");

        //create an image based on the local storage location
        if (mainController.list.get(indexOnEditing).getURL() != null) {
            File file = new File(mainController.list.get(indexOnEditing).getURL());
            url = file.toURI().toString();
            myImage = new Image(url);
            image.setImage(myImage);
            this.url = url;

        }
        //displays the total value of the selected product
        double v = (mainController.list.get(indexOnEditing).getPrice() * mainController.list.get(indexOnEditing).getQuantity());
        totalValue.setText("$" + String.format("%.2f", v));
    }

    /**
     * Selecting a new image file
     */
    public void chooseImg() {

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

    /**
     * Sets the parent controller
     *
     * @param mainController
     */
    void setParentController(MainDocumentController mainController) {
        this.mainController = mainController;
    }

}
