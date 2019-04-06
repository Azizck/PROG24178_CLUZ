/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainwindow;

import mainwindow.Clothing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
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

       if (Clothing.Type.Dress == typeCombo.getSelectionModel().getSelectedItem()) {

            sizeCombo.getItems().setAll("0", "2", "4", "6", "8", "10", "12");
            colorCombo.getItems().setAll(Clothing.Colors.values());
            genderCombo.getItems().setAll(Clothing.Gender.Girls, Clothing.Gender.Female);
        } else if (Clothing.Type.Shorts == typeCombo.getSelectionModel().getSelectedItem()) {

            sizeCombo.getItems().setAll("28W", "30W", "32W", "34W", "36W");
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
       
            setList();
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
    
    
    public void setList() {
               
        mainController.list.get(indexOnEditing).setColor(colorCombo.getSelectionModel().getSelectedItem());      
        mainController.list.get(indexOnEditing).setType(typeCombo.getSelectionModel().getSelectedItem());
        mainController.list.get(indexOnEditing).setProductId(Integer.parseInt(idLabel.getText()));
        mainController.list.get(indexOnEditing).setGender(genderCombo.getSelectionModel().getSelectedItem());
        mainController.list.get(indexOnEditing).setSize(sizeCombo.getValue());
        mainController.list.get(indexOnEditing).setPrice(Double.parseDouble(priceLabel.getText()));
        mainController.list.get(indexOnEditing).setQuantity(Integer.parseInt(quantityLabel.getText()));
        
  
    }
    public void editDisplay(ObservableList<Clothing> c) {
        
        //displays placeholder in the edit window
        typeCombo.getSelectionModel().select(mainController.list.get(indexOnEditing).getType());
        sizeCombo.getSelectionModel().select(mainController.list.get(indexOnEditing).getSize());
        genderCombo.getSelectionModel().select(mainController.list.get(indexOnEditing).getGender());
        colorCombo.getSelectionModel().select(mainController.list.get(indexOnEditing).getColor());
        idLabel.setText(mainController.list.get(indexOnEditing).getProductId()+"");
        quantityLabel.setText(mainController.list.get(indexOnEditing).getQuantity()+"");
        priceLabel.setText(mainController.list.get(indexOnEditing).getPrice()+"");
       
       
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