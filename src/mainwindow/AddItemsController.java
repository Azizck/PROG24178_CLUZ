/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainwindow;

import java.io.File;
import java.io.IOException;
import mainwindow.Clothing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
 *
 * @author CS
 */
public class AddItemsController implements Initializable {

    private ArrayList<Clothing> clothList;
    private Clothing c;
    private ObservableList<Clothing> list;

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
    private Button submitBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label confirmation;
    @FXML
    private Button imgButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainController = MainDocumentController.getController();
        clothList = new ArrayList<>();
        typeCombo.getItems().setAll(Clothing.Type.values());
        cbDisable(true);
    }

    @FXML
    private void typeHandle(ActionEvent event) {

        if (Clothing.Type.Dress == typeCombo.getSelectionModel().getSelectedItem()) {
            cbDisable(false);
            sizeCombo.getItems().setAll("0", "2", "4", "6", "8", "10", "12");
            colorCombo.getItems().setAll(Clothing.Colors.values());
            genderCombo.getItems().setAll(Clothing.Gender.Girls, Clothing.Gender.Female);
        } else if (Clothing.Type.Shorts == typeCombo.getSelectionModel().getSelectedItem()) {
            cbDisable(false);
            sizeCombo.getItems().setAll("28W", "30W", "32W", "34W", "36W");
            colorCombo.getItems().setAll(Clothing.Colors.values());
            genderCombo.getItems().setAll(Clothing.Gender.values());
        } else {
            cbDisable(false);
            sizeCombo.getItems().setAll("XS", "S", "M", "L", "XL");
            genderCombo.getItems().setAll(Clothing.Gender.values());
            colorCombo.getItems().setAll(Clothing.Colors.values());
        }

    }

    @FXML
    private void genderHandle(ActionEvent event) {
    }

    @FXML
    private void submitHandle(ActionEvent event) {

        if (typeCombo.getSelectionModel().isEmpty() || colorCombo.getSelectionModel().isEmpty()
                || genderCombo.getSelectionModel().isEmpty()
                || priceLabel.getText().isEmpty() || quantityLabel.getText().isEmpty()
                || sizeCombo.getSelectionModel().isEmpty()) {
            confirmation.setText("Please complete the fields");

        } else {
            Clothing c = new Clothing();
            addProduct(c);
        }

    }

    @FXML
    private void cancelHandle(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    private void showHandle(ActionEvent event
    ) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mainController.getList().toString().replace("[", "").replace("]", "").replace(",", ""), ButtonType.OK);
        alert.setHeaderText("Products");
        alert.setTitle("Product List");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
        alert.show();
    }

    public void cbDisable(boolean b) {
        sizeCombo.setDisable(b);
        colorCombo.setDisable(b);
        genderCombo.setDisable(b);
    }

    public void clear() {

        typeCombo.getSelectionModel().clearSelection();
        idLabel.setText("");
        genderCombo.getSelectionModel().clearSelection();
        sizeCombo.getSelectionModel().clearSelection();
        colorCombo.getSelectionModel().clearSelection();
        priceLabel.setText("");
        quantityLabel.setText("");
        cbDisable(false);

    }

    //method to add the product given the product type
    public void addProduct(Clothing c) {

        c.setProductType(typeCombo.getValue());
        c.setProductId(Integer.parseInt(idLabel.getText()));
        c.setSize(sizeCombo.getValue());
        c.setColor(colorCombo.getValue());
        c.setGender(genderCombo.getValue());
        c.setPrice(Double.parseDouble(priceLabel.getText()));
        c.setQuantity(Integer.parseInt(quantityLabel.getText()));

        this.c = c;

        //adds cloth object to ArrayList clothList
        clothList.add(c);
        mainController.setList(c);

        confirmation.setText("Item Added Successfully");

        clear();

    }

    public Clothing getProduct() {
        return c;
    }

    @FXML
    private void imageHandle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            Image myImage = new Image(file.toURI().toString());
            image.setImage(myImage);
        }
    }

    /*
    public void setParentController(MainDocumentController mainController) {
    this.mainController = mainController;
}
     */
}
