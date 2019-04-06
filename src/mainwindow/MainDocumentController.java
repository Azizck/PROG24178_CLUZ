package mainwindow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import mainwindow.Clothing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This project is developed for a clothing retailer whose needs are to manage
 * inventory on a day-to-day basis. The required functionalities are adding,
 * editing, removing items while giving users the freedom to select clothing
 * types accordingly.
 *
 * April 5th, 2019
 *
 * @author Jingwei Sun, John Chen, Aziz Omar
 */
public class MainDocumentController implements Initializable {

    private ArrayList<Clothing> clothList;
    private Clothing selected;
    private boolean inEdit;
    private int indexOnEditing;

    ObservableList<Clothing> list = FXCollections.observableArrayList();
    FilteredList filter = new FilteredList(list, e -> true);

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
    private ComboBox<String> sizeFilter;
    @FXML
    private ComboBox<Colors> colorFilter;
    @FXML
    private Button saveFile;
    @FXML
    private Button openFile;
    @FXML
    private Button resetBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        items.setItems(getList());

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
        items.setOnMouseClicked(e -> {
            if (e.getClickCount() > 0) {

                select();
            }
        });

        typeFilter.getItems().addAll(Type.values());
        genderFilter.getItems().addAll(Gender.values());
        colorFilter.getItems().addAll(Colors.values());

        items.setItems(list);

    }

    public static MainDocumentController getController() {
        return controller;
    }

    public void update() {
        items.refresh();
    }

    public void setList(Clothing c) {
        list.add(c);
        items.setItems(list);
        //items.getItems().setAll(list.add(c));
        //items.getItems().clear();
        //getList().add(c);

    }

    public ObservableList<Clothing> getList() {

        list.add(new Clothing(14, Type.Dress, Gender.Girls, "XS", Colors.Orange, 53.33, 2));
        list.add(new Clothing(22, Type.Pants, Gender.Boys, "XS", Colors.Orange, 83.33, 2));
        list.add(new Clothing(34, Type.Dress, Gender.Girls, "XS", Colors.Orange, 21.33, 2));
        list.add(new Clothing(41, Type.Shorts, Gender.Boys, "XS", Colors.Orange, 223.33, 2));
        list.add(new Clothing(5, Type.Dress, Gender.Girls, "XS", Colors.Orange, 2335.33, 2));

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

    public void pantSize() {
        sizeFilter.getItems().setAll("28W", "30W", "32W", "34W", "36W");
        colorFilter.getItems().setAll(Clothing.Colors.values());
        genderFilter.getItems().setAll(Clothing.Gender.values());
    }

    public void girlSize() {
        sizeFilter.getItems().setAll("0", "2", "4", "6", "8", "10", "12");
        colorFilter.getItems().setAll(Clothing.Colors.values());
        genderFilter.getItems().setAll(Clothing.Gender.Girls, Clothing.Gender.Female);
    }

    @FXML
    private void searchHandle(ActionEvent event) {

    }

    @FXML
    private void typeFilterHandle(ActionEvent event) {

        if (Clothing.Type.Dress == typeFilter.getSelectionModel().getSelectedItem()) {
            girlSize();

        } else if (Clothing.Type.Skirts == typeFilter.getSelectionModel().getSelectedItem()) {
            girlSize();
        } else if (Clothing.Type.Shorts == typeFilter.getSelectionModel().getSelectedItem()) {
            pantSize();
        } else if (Clothing.Type.Jeans == typeFilter.getSelectionModel().getSelectedItem()) {
            pantSize();
        } else if (Clothing.Type.Pants == typeFilter.getSelectionModel().getSelectedItem()) {
            pantSize();
        } else {
            sizeFilter.getItems().setAll("XS", "S", "M", "L", "XL");
            genderFilter.getItems().setAll(Clothing.Gender.values());
            colorFilter.getItems().setAll(Clothing.Colors.values());
        }
        if (Clothing.Type.Dress == typeFilter.getSelectionModel().getSelectedItem()) {

        }
        if (Clothing.Type.Shorts == typeFilter.getSelectionModel().getSelectedItem()) {

        }
        if (Clothing.Type.Skirts == typeFilter.getSelectionModel().getSelectedItem()) {

        }
        if (Clothing.Type.Shirts == typeFilter.getSelectionModel().getSelectedItem()) {

        }
    }

    @FXML
    private void genderFilterHandle(ActionEvent event) {
        if (Clothing.Gender.Male == genderFilter.getSelectionModel().getSelectedItem()) {

        }
        if (Clothing.Gender.Female == genderFilter.getSelectionModel().getSelectedItem()) {

        }
        if (Clothing.Gender.Girls == genderFilter.getSelectionModel().getSelectedItem()) {

        }
        if (Clothing.Gender.Boys == genderFilter.getSelectionModel().getSelectedItem()) {

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
        if (Clothing.Colors.Red == colorFilter.getSelectionModel().getSelectedItem()) {

        }
        if (Clothing.Colors.Orange == colorFilter.getSelectionModel().getSelectedItem()) {

        }
        if (Clothing.Colors.Yellow == colorFilter.getSelectionModel().getSelectedItem()) {

        }
        if (Clothing.Colors.Green == colorFilter.getSelectionModel().getSelectedItem()) {

        }
        if (Clothing.Colors.Blue == colorFilter.getSelectionModel().getSelectedItem()) {

        }
        if (Clothing.Colors.White == colorFilter.getSelectionModel().getSelectedItem()) {

        }
        if (Clothing.Colors.Black == colorFilter.getSelectionModel().getSelectedItem()) {

        }
    }

    @FXML
    private void saveHandle(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Save as");
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                PrintWriter writer = new PrintWriter(file);
                for (Clothing c : list) {
                    writer.write(c.getProductId() + ", " + c.getType() + ", "
                            + c.getGender() + ", " + c.getSize() + ", " + c.getColor()
                            + ", " + c.getPrice() + ", " + c.getQuantity() + "\n");
                }

                writer.close();
            } catch (IOException ex) {
                System.out.println("Error: " + ex);
            }
        }
    }

    @FXML
    private void openFileHandle(ActionEvent event) {
        items.setItems(open());

    }

    private ObservableList<Clothing> open() {

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Open File");

        ArrayList<Clothing> c = new ArrayList<>();
        BufferedReader reader = null;

        try {
            File file = fileChooser.showOpenDialog(stage);
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] names = line.split(", ");
                c.add(new Clothing(Integer.parseInt(names[0]),
                        Type.valueOf(names[1]), Gender.valueOf(names[2]),
                        names[3], Colors.valueOf(names[4]),
                        Double.parseDouble(names[5]),
                        Integer.parseInt(names[6])));
                //System.out.println(c);
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        ObservableList<Clothing> list = FXCollections.observableArrayList(c);

        this.list = list;
        return list;

    }

    @FXML
    private void resetHandle(ActionEvent event) {
    }
    // search bar 
    @FXML
    private void search(KeyEvent event) {
        // takes an input and compares it with parameters in observableList using addListener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super Clothing>) (Clothing c) -> {
                if (newValue.isEmpty() || newValue == null) {
                    return true;
                }else if (c.getSize().toUpperCase().contains(newValue)) {
                    return true;
                }
                return false;
            });
        });
        // compares new sorted list with filtered list using SortedList 
        SortedList sort = new SortedList(filter);
        sort.comparatorProperty().bind(items.comparatorProperty());
        items.setItems(sort);
    }

}


/*
Need to implement:
filtering and searching
image viewing and editing
bug tests

*/
