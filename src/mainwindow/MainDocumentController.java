/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainwindow;

import mainwindow.Clothing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author CS
 */
public class MainDocumentController implements Initializable {

    private ArrayList<Clothing> clothList;
    private Clothing selected;
    private boolean inEdit;
    private int indexOnEditing;


    ObservableList<Clothing> list = FXCollections.observableArrayList();
        
    private String fileName;

    private AddItemsController addItemsController;
    private EditItemsController editItemsController;

    private static MainDocumentController controller;

    
    private Label label;
    @FXML
    private TableView<Clothing> items;
    @FXML
    private TableColumn<Clothing, Integer> tableId;
    @FXML
    private TableColumn<Clothing, Type> tableType;
    @FXML
    private Button addBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Button removeBtn;
    @FXML
    private TableColumn<Clothing, Gender> tableGender;
    @FXML
    private TableColumn<Clothing, String> tableSize;
    @FXML
    private TableColumn<Clothing, Colors> tableColor;
    @FXML
    private TableColumn<Clothing, Double> tablePrice;
    @FXML
    private TableColumn<Clothing, Integer> tableQuantity;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Type> typeFilter;
    @FXML
    private ComboBox<Gender> genderFilter;
    @FXML
    private ComboBox<Size> sizeFilter;
    @FXML
    private ComboBox<Colors> colorFilter;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controller = this;
        indexOnEditing = 0;
        inEdit = false;
        
        tableId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        tableType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tableSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        tableColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        tablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        
        //display table with entries on load
        items.setItems(getList());
        
        items.setOnMouseClicked(e -> {
            if (e.getClickCount() > 0) {
                
                select();
            }
        });
        
        typeFilter.getItems().addAll(Type.values());
        genderFilter.getItems().addAll(Gender.values());
//        sizeFilter.getItems().addAll(Size.values());
        colorFilter.getItems().addAll(Colors.values());
    }
    
    public static MainDocumentController getController() {
        return controller;
    }

    
    public void update() {
        items.refresh();
    }
    
    public void setList(Clothing c) {
        
        items.getItems().clear();
        getList().add(c);
      
    }
    
  
    
    public ObservableList<Clothing> getList() {

        list.add(new Clothing(1, Type.Dress, Gender.Girls, "XS", Colors.Orange, 23.33, 2));
        list.add(new Clothing(2, Type.Jackets, Gender.Boys, "XS", Colors.Orange, 23.33, 2));
        list.add(new Clothing(3, Type.Dress, Gender.Girls, "XS", Colors.Orange, 23.33, 2));
        list.add(new Clothing(4, Type.Jackets, Gender.Boys, "XS", Colors.Orange, 23.33, 2));
        list.add(new Clothing(5, Type.Dress, Gender.Girls, "XS", Colors.Orange, 23.33, 2));
        
        return list;
       
    }

    public void select() {

        if (!items.getSelectionModel().isEmpty()) {
            inEdit = true;
            Clothing selected = items.getSelectionModel().getSelectedItem();
            indexOnEditing = items.getSelectionModel().getSelectedIndex();
            this.indexOnEditing = indexOnEditing;
            this.selected = selected;
            
            //for testing purposes
            System.out.println("index is" + indexOnEditing);
            System.out.println(selected);
            System.out.println("===========");
            System.out.println("List size is: " + list.size());
               
         
        }

    }

    @FXML
    private void addHandle(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("AddItems.fxml"));

            AddItemsController addItemsController = new FXMLLoader().getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Items");
            stage.setScene(scene);
            
                stage.show();
                
        } catch (Exception e) {
            System.out.println("Error" + e);
        }

    }

    @FXML
    private void editHandle(ActionEvent event) throws IOException {

        try {
            if (!items.getSelectionModel().isEmpty()) {
                // Loading the modify part window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditItems.fxml"));
                Parent root = (Parent) loader.load();

                loader.<EditItemsController>getController().setParentController(this);
                EditItemsController editItemsController = loader.getController();
                
                editItemsController.indexEdit(indexOnEditing);
          
                editItemsController.editDisplay(list);
                
                Stage stage = new Stage(); 
                //keeps the new window focused
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Edit Item");
                stage.setScene(new Scene(root));                
                stage.show();
                
                /*
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("something.fxml");  
                anna = loader.getController();
                
                 */
                
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please select a product to edit");
                alert.showAndWait();
            }

        } catch (Exception e) {
            System.out.println("Error" + e);
        }
    }

    @FXML
    private void removeHandle(ActionEvent event) {
        
        if (!items.getSelectionModel().isEmpty()) {

            inEdit = true;
            Clothing selected = items.getSelectionModel().getSelectedItem();
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setContentText("Confirm if you would like to delete?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                items.getItems().remove(selected);

            }
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a row to delete");

            alert.showAndWait();
        }
    }

    @FXML
    private void searchHandle(ActionEvent event) {
    }

    @FXML
    private void typeFilterHandle(ActionEvent event) {
         if (Clothing.Type.Dress == typeFilter.getSelectionModel().getSelectedItem()){
             
         }
         if (Clothing.Type.Shorts == typeFilter.getSelectionModel().getSelectedItem()){
             
         }
         if (Clothing.Type.Jackets == typeFilter.getSelectionModel().getSelectedItem()){
             
         }
         if (Clothing.Type.Shirts == typeFilter.getSelectionModel().getSelectedItem()){
             
         }
    }

    @FXML
    private void genderFilterHandle(ActionEvent event) {
        if (Clothing.Gender.Male == genderFilter.getSelectionModel().getSelectedItem()){
             
         }
        if (Clothing.Gender.Female == genderFilter.getSelectionModel().getSelectedItem()){
             
         }
        if (Clothing.Gender.Girls == genderFilter.getSelectionModel().getSelectedItem()){
             
         }
        if (Clothing.Gender.Boys == genderFilter.getSelectionModel().getSelectedItem()){
             
         }
    }

    @FXML
    private void sizeFilterHandle(ActionEvent event) {
//        if (Clothing.Size.XS == sizeFilter.getSelectionModel().getSelectedItem()){
//             
//         }
    }

    @FXML
    private void colorFilterHandle(ActionEvent event) {
        if (Clothing.Colors.Red == colorFilter.getSelectionModel().getSelectedItem()){
             
         }
        if (Clothing.Colors.Orange == colorFilter.getSelectionModel().getSelectedItem()){
             
         }
        if (Clothing.Colors.Yellow == colorFilter.getSelectionModel().getSelectedItem()){
             
         }
        if (Clothing.Colors.Green == colorFilter.getSelectionModel().getSelectedItem()){
             
         }
        if (Clothing.Colors.Blue == colorFilter.getSelectionModel().getSelectedItem()){
             
         }
        if (Clothing.Colors.White == colorFilter.getSelectionModel().getSelectedItem()){
             
         }
        if (Clothing.Colors.Black == colorFilter.getSelectionModel().getSelectedItem()){
             
         }
    }

}
