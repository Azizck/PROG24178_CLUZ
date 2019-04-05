/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainwindow;

import java.io.File;
import mainwindow.Clothing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author CS
 */
public class EditItemsController implements Initializable {

    private ArrayList<Clothing> clothList;
    private Clothing c;

    private MainDocumentController mainController;

    private boolean inEditing;
    private int indexOnEditing;
    private String fileName;
    private int index;
    private Label label;

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
    private Button showBtn;
    @FXML
    private Button submitBtn;
    @FXML
    private Button cancelBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainController = MainDocumentController.getController();
        clothList = new ArrayList<>();
        inEditing = false;

        typeCombo.getItems().setAll(Clothing.Type.values());
    }

    @FXML
    private void typeHandle(ActionEvent event) {

        //Clothing.Type.valueOf("Dress");
        //for (int i = 0; i < typeCombo.getItems().size(); i++) {
        // if (Clothing.Type.values().equals(typeCombo.getSelectionModel().getSelectedItem())) {
        //if (typeCombo.getSelectionModel().getSelectedItem().equals(Clothing.Type.values())) {
        if (Clothing.Type.Dress == typeCombo.getSelectionModel().getSelectedItem()) {

            sizeCombo.getItems().setAll("0", "2", "4", "6", "8", "10");
            colorCombo.getItems().setAll(Clothing.Colors.values());
            genderCombo.getItems().setAll(Clothing.Gender.Girls, Clothing.Gender.Female);
        } else if (Clothing.Type.Shorts == typeCombo.getSelectionModel().getSelectedItem()) {

            sizeCombo.getItems().setAll("30W", "32W", "34W", "36W");
            colorCombo.getItems().setAll(Clothing.Colors.values());
            genderCombo.getItems().setAll(Clothing.Gender.values());
        } else {

            sizeCombo.getItems().setAll("XS", "S", "M", "L", "XL");
            genderCombo.getItems().setAll(Clothing.Gender.values());
            colorCombo.getItems().setAll(Clothing.Colors.values());
        }

    }

    @FXML
    private void genderHandle(ActionEvent event) {
    }

    @FXML
    private void imageHandle(ActionEvent event) {
//        FileChooser fileChooser = new FileChooser();
//        File file = fileChooser.showOpenDialog(null);
//        if (file != null) {
//            Image myImage = new Image(file.toURI().toString());
//            image.setImage(myImage);
    }

    @FXML
    private void showHandle(ActionEvent event
    ) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, clothList.toString().replace("[", "").replace("]", "").replace(",", ""), ButtonType.OK);
        alert.setHeaderText("Products");
        alert.setTitle("Product List");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
        alert.show();
    }

    @FXML
    private void submitHandle(ActionEvent event) {
        
        
            c.setProductType(typeCombo.getValue());
            c.setProductId(Integer.parseInt(idLabel.getText()));
            c.setSize(sizeCombo.getValue());
            c.setColor(colorCombo.getValue());
            c.setGender(genderCombo.getValue());
            c.setPrice(Double.parseDouble(priceLabel.getText()));
            c.setQuantity(Integer.parseInt(quantityLabel.getText()));

            clothList.add(indexOnEditing, c);
            mainController.updateList(clothList);
            //mainController.setList(clothList);

            confirmation.setText("Item Added Successfully");
            mainController.update();

        
    }

    @FXML
    private void cancelHandle(ActionEvent event) {
        Stage stage = (Stage)cancelBtn.getScene().getWindow();
        stage.close();
    }

    public int indexEdit(int i){
        this.indexOnEditing = i;
        return indexOnEditing;
    }
    public void edit(Clothing c) {
        //typeCombo.setSelectionModel(c.getType());
        typeCombo.getSelectionModel().getSelectedItem();
        idLabel.setText(c.getProductId() + "");
        genderCombo.getItems().indexOf(c);
        priceLabel.setText(c.getPrice() + "");
        //c.getPrice(Double.parseDouble(priceLabel.getText()));
        quantityLabel.setText(c.getQuantity() + "");
     
        this.c = c;
        
       
    }

    public void clear() {

        typeCombo.getSelectionModel().clearSelection();
        //typeCombo.valueProperty().set(null);
        idLabel.setText("");
        genderCombo.getSelectionModel().clearSelection();
        sizeCombo.getSelectionModel().clearSelection();
        colorCombo.getSelectionModel().clearSelection();
        priceLabel.setText("");
        quantityLabel.setText("");

    }

    void setParentController(MainDocumentController mainController) {
        this.mainController = mainController;
    }

}
