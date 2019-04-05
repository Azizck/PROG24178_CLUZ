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
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private ObservableList<Clothing> list;

    private String fileName;

    private AddItemsController addItemsController;
    private EditItemsController editItemsController;

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

    private static MainDocumentController controller;

    public static MainDocumentController getController() {
        return controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controller = this;
        // addItemsController.setParentController(this);

        //item = new ArrayList<>();
        //items = new TableView<Clothing>(); 
        inEdit = false;
        
        tableId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        tableType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tableSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        tableColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        tablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        //list = FXCollections.observableArrayList();
        int listSize = getList().size();
        items.setItems(getList());
        
        items.setOnMouseClicked(e -> {
            if (e.getClickCount() > 0) {
                
                select();
            }
        });
    }
    
    public void update() {
        items.refresh();
    }
    
    public void setList(ArrayList<Clothing> c) {
        //Clothing c = new Clothing(productId, type);
        ObservableList<Clothing> list = FXCollections.observableArrayList(getList());
        //items.getItems().clear();
        //items.getItems().setAll(list);
        list.addAll(c);
        items.setItems(list);
        
        this.list = list;
        //list.add(c);

    }
    
    public void updateList(ArrayList<Clothing> c) {
        //Clothing c = new Clothing(productId, type);
        ObservableList<Clothing> list = FXCollections.observableArrayList(getList());
        //items.getItems().clear();
        //items.getItems().setAll(list);
        items.setItems(list);
     
        //list.add(c);

    }

    public ObservableList<Clothing> getList() {

        ObservableList<Clothing> list = FXCollections.observableArrayList(
        
        new Clothing(1, Type.Dress, Gender.Boys, "XS", Colors.Orange, 23.33, 2),
           new Clothing(1, Type.Dress, Gender.Boys, "XS", Colors.Orange, 23.33, 2),
           new Clothing(1, Type.Dress, Gender.Boys, "XS", Colors.Orange, 23.33, 2),
           new Clothing(1, Type.Dress, Gender.Boys, "XS", Colors.Orange, 23.33, 2),
           new Clothing(1, Type.Dress, Gender.Boys, "XS", Colors.Orange, 23.33, 2),
           new Clothing(1, Type.Dress, Gender.Boys, "XS", Colors.Orange, 23.33, 2),  new Clothing(1, Type.Dress, Gender.Boys, "XS", Colors.Orange, 23.33, 2),
           new Clothing(1, Type.Dress, Gender.Boys, "XS", Colors.Orange, 23.33, 2),
           new Clothing(1, Type.Dress, Gender.Boys, "XS", Colors.Orange, 23.33, 2)
               
        );
        
        return list;
       
    }

    public void select() {

        if (!items.getSelectionModel().isEmpty()) {
            inEdit = true;
            Clothing selected = items.getSelectionModel().getSelectedItem();
            //indexOnEditing = items.getSelectionModel().getSelectedIndex();
            System.out.println(indexOnEditing);
            System.out.println(selected);
              
            this.selected = selected;

        }

    }

    @FXML
    private void addHandle(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("AddItems.fxml"));

            AddItemsController addItemsController = new FXMLLoader().getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Add Items");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println("Error" + e);
        }

    }

    @FXML
    private void editHandle(ActionEvent event) throws IOException {

        //items.getSelectionModel().clearSelection();
        try {
            if (!items.getSelectionModel().isEmpty()) {
                // Loading the modify part window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditItems.fxml"));
                Parent root = (Parent) loader.load();

                loader.<EditItemsController>getController().setParentController(this);
                EditItemsController editItemsController = loader.getController();
                editItemsController.edit(selected);
                editItemsController.indexEdit(indexOnEditing);
                Stage stage = new Stage();
                stage.setTitle("Edit Item");
                stage.setScene(new Scene(root));
                stage.show();

                /*

                Parent root = FXMLLoader.load(getClass().getResource("EditItems.fxml"));

                EditItemsController editItemsController = new FXMLLoader().getController();

                editItemsController.edit(selected);
                Scene scene = new Scene(root);

                Stage stage2 = new Stage();
                stage2.setTitle("Edit Item");
                stage2.setScene(scene);
                stage2.show();
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

}
