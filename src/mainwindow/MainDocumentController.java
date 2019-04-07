package mainwindow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import mainwindow.Clothing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    FilteredList filter;

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
    @FXML
    private Label uniqueProducts;
    @FXML
    private Label totalProducts;
    @FXML
    private Label totalValue;

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
            //single click
            if (e.getClickCount() == 1) {

                select();
            } //double click
            else if (e.getClickCount() == 2) {
                select();

                showEdit();

            } else {

            }
        });

        typeFilter.getItems().addAll(Type.values());
        genderFilter.getItems().addAll(Gender.values());
        colorFilter.getItems().addAll(Colors.values());

        items.setItems(list);
        filter = new FilteredList(list, e -> true);

        update();
    }

    public static MainDocumentController getController() {
        return controller;
    }

    public void update() {
        items.refresh();
        calculate();
    }

    public void calculate() {
        int q = 0;
        double v = 0;
        for (int i = 0; i < list.size(); i++) {
            q += list.get(i).getQuantity();
            v += (list.get(i).getPrice() * list.get(i).getQuantity());
        }
        uniqueProducts.setText("Unique Products: " + Integer.toString(list.size()));
        totalProducts.setText("Total Products: " + q);
        totalValue.setText("Total Value: $" + String.format("%.2f", v));

    }

    public void setList(Clothing c) {
        list.add(c);
        items.setItems(list);
        calculate();
        //items.getItems().setAll(list.add(c));
        //items.getItems().clear();
        //getList().add(c);

    }

    public ObservableList<Clothing> getList() {

        list.add(new Clothing(14, Type.Dress, Gender.Girls, "0", Colors.Orange, 53.33, 2));
        list.add(new Clothing(22, Type.Pants, Gender.Boys, "28W", Colors.Orange, 83.33, 2));
        list.add(new Clothing(34, Type.Outerwear, Gender.Girls, "M", Colors.Orange, 21.33, 2));
        list.add(new Clothing(41, Type.Shirts, Gender.Boys, "XS", Colors.Orange, 223.33, 2));
        list.add(new Clothing(5, Type.Dress, Gender.Female, "8", Colors.Orange, 2335.33, 2));

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
            System.out.println("===================");
  
            
            //System.out.println(list.get(5).getURL());
          
           

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
    private void editHandle(ActionEvent event) {
        showEdit();
    }

    private void showEdit() {

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

               
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please select a product to edit");
                alert.showAndWait();
            }

        } catch (IOException e) {
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
                update();
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
        // takes a selection and compares it with parameters in observableList using addListener
        typeFilter.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    // filters for all Clothing objects
                    filter.setPredicate((Predicate<? super Clothing>) (Clothing c) -> {
                        // when DRESS type selection is made, do something
                        if (Clothing.Type.Dress == typeFilter.getSelectionModel().getSelectedItem()) {
                            // search all items currently in ArrayList list
                            for (int i = 0; i < list.size(); i++) {
                                // if type of clothing object is DRESS then return true to be sorted
                                if (c.getType() == Type.Dress) {
                                    return true;
                                }
                            }
                        }
                        if (Clothing.Type.Shorts == typeFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getType() == Type.Shorts) {
                                    return true;
                                }
                            }
                        }
                        if (Clothing.Type.Skirts == typeFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getType() == Type.Skirts) {
                                    return true;
                                }
                            }
                        }
                        if (Clothing.Type.Shirts == typeFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getType() == Type.Shirts) {
                                    return true;
                                }
                            }
                        }
                        if (Clothing.Type.Outerwear == typeFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getType() == Type.Outerwear) {
                                    return true;
                                }
                            }
                        }
                        if (Clothing.Type.Jeans == typeFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getType() == Type.Jeans) {
                                    return true;
                                }
                            }
                        }
                        if (Clothing.Type.Sleepwear == typeFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getType() == Type.Sleepwear) {
                                    return true;
                                }
                            }
                        }
                        if (Clothing.Type.Sweaters == typeFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getType() == Type.Sweaters) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    });
                });
        // returns a new SortedList with FilteredList applied using the combobox parameters
        SortedList sort = new SortedList(filter);
        sort.comparatorProperty().bind(items.comparatorProperty());
        items.setItems(sort);
    }

    @FXML
    private void genderFilterHandle(ActionEvent event) {
        genderFilter.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    filter.setPredicate((Predicate<? super Clothing>) (Clothing c) -> {
                        if (Clothing.Gender.Male == genderFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getGender() == Gender.Male) {
                                    return true;
                                }
                            }

                        }
                        if (genderFilter.getSelectionModel().getSelectedItem() == Gender.Female) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getGender() == Gender.Female) {
                                    return true;
                                }
                            }

                        }
                        if (Clothing.Gender.Girls == genderFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getGender() == Gender.Girls) {
                                    return true;
                                }
                            }
                        }
                        if (Clothing.Gender.Boys == genderFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getGender() == Gender.Boys) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    });
                });
        SortedList sort = new SortedList(filter);
        sort.comparatorProperty().bind(items.comparatorProperty());
        items.setItems(sort);
    }

    @FXML
    private void sizeFilterHandle(ActionEvent event) {
//        if (Clothing.Size.XS == sizeFilter.getSelectionModel().getSelectedItem()){
//             
//         }
    }

    @FXML
    private void colorFilterHandle(ActionEvent event) {
        colorFilter.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    filter.setPredicate((Predicate<? super Clothing>) (Clothing c) -> {
                        if (Clothing.Colors.Red == colorFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getColor() == Colors.Red) {
                                    return true;
                                }
                            }
                        }
                        if (Clothing.Colors.Orange == colorFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getColor() == Colors.Orange) {
                                    return true;
                                }
                            }
                        }
                        if (Clothing.Colors.Yellow == colorFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getColor() == Colors.Yellow) {
                                    return true;
                                }
                            }
                        }
                        if (Clothing.Colors.Green == colorFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getColor() == Colors.Green) {
                                    return true;
                                }
                            }
                        }
                        if (Clothing.Colors.Blue == colorFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getColor() == Colors.Blue) {
                                    return true;
                                }
                            }
                        }
                        if (Clothing.Colors.White == colorFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getColor() == Colors.White) {
                                    return true;
                                }
                            }
                        }
                        if (Clothing.Colors.Black == colorFilter.getSelectionModel().getSelectedItem()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (c.getColor() == Colors.Black) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    });
                });
        SortedList sort = new SortedList(filter);
        sort.comparatorProperty().bind(items.comparatorProperty());
        items.setItems(sort);
    }

    @FXML
    private void saveHandle(ActionEvent event) {
        
        FileChooser fileChooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File(currentPath));
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Save as");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                PrintWriter writer = new PrintWriter(file);
                for (Clothing c : list) {
                    writer.write(c.getProductId() + ", " + c.getType() + ", "
                            + c.getGender() + ", " + c.getSize() + ", " + c.getColor()
                            + ", " + c.getPrice() + ", " + c.getQuantity() + ", " + c.getURL() + "\n");
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
        calculate();

    }

    private ObservableList<Clothing> open() {

        FileChooser fileChooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File(currentPath));
        
    
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Open File");

        ArrayList<Clothing> clist = new ArrayList<>();
        Clothing c = new Clothing();
        BufferedReader reader = null;

        try {
            File file = fileChooser.showOpenDialog(null);
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] names = line.split(", ");
                
                c = new Clothing(Integer.parseInt(names[0]),
                        Type.valueOf(names[1]), Gender.valueOf(names[2]),
                        names[3], Colors.valueOf(names[4]),
                        Double.parseDouble(names[5]),
                        Integer.parseInt(names[6]));
                        c.setURL(names[7]);
                        
               // clist.add(new Clothing(Integer.parseInt(names[0]),
                 //       Type.valueOf(names[1]), Gender.valueOf(names[2]),
                   //     names[3], Colors.valueOf(names[4]),
                     //   Double.parseDouble(names[5]),
                       // Integer.parseInt(names[6])));
                clist.add(c);
                //System.out.println(c);
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        ObservableList<Clothing> list = FXCollections.observableArrayList(clist);

        this.list = list;
        this.filter = new FilteredList(list, e -> true);
        return list;

    }

    @FXML
    private void resetHandle(ActionEvent event) {

        typeFilter.setValue(null);
        genderFilter.getSelectionModel().clearSelection();
        sizeFilter.setValue(null);
        colorFilter.setValue(null);
        searchField.setText("");
        items.setItems(list);
    }

    // search bar 
    @FXML
    private void search(KeyEvent event) {
        /*
        searchField.textProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue observable, Object oldVal, Object newVal) {
                searchKey((String)oldVal, (String)newVal);
            }
        });
         */

        // takes an input and compares it with parameters in observableList using addListener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super Clothing>) (Clothing c) -> {
                if (newValue.isEmpty() || newValue == null) {
                    return true;
                } else if (c.getSize().toLowerCase().contains(newValue)) {
                    return true;
                } else if (String.valueOf(c.getProductId()).toLowerCase().contains(newValue)) {
                    return true;
                } else if (String.valueOf(c.getPrice()).toLowerCase().contains(newValue)) {
                    return true;
                } else if (String.valueOf(c.getColor()).toLowerCase().contains(newValue)) {
                    return true;
                } else if (String.valueOf(c.getType()).toLowerCase().contains(newValue)) {
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

    public void searchKey(String oldVal, String newVal) {
        if (oldVal != null && (newVal.length() < oldVal.length())) {
            items.setItems(list);
        }
        newVal = newVal.toUpperCase();
        ObservableList<Clothing> subList = FXCollections.observableArrayList();
        for (Clothing e : items.getItems()) {
            Clothing searchText = (Clothing) e;
            if (searchText.getColor().toString().contains(newVal)) {
                subList.add(searchText);
            }
        }
        items.setItems(subList);

    }

}

/*
Need to implement:
filtering and searching
image viewing and editing
bug tests

 */
