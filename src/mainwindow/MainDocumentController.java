package mainwindow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import mainwindow.Clothing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Paths;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This project is developed for a clothing retailer whose needs are to manage
 * inventory on a day-to-day basis. The required functionalities are adding,
 * editing, removing items while giving users the freedom to select clothing
 * types accordingly.
 *
 *
 * @version 1.0
 * @author Jingwei Sun, John Chen, Aziz Omar
 */
public class MainDocumentController implements Initializable {

	
	double x,y;
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
	@FXML
	private VBox sLeftSideBar;

    /**
     * Initializes the variables upon starting the application.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //initializes the variables
        controller = this;
        indexOnEditing = 0;
        inEdit = false;

        //sets the table columns
        tableId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        tableType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tableSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        tableColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        tablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        //TableView calls the getList method and populates the table on start
        items.setItems(getList());

        //initializes the filter
        filter = new FilteredList(list, e -> true);

        //if the table is clicked
        items.setOnMouseClicked(e -> {
            //single click
            if (e.getClickCount() == 1) {
                //calls the select method
                select();
            } //double click
            else if (e.getClickCount() == 2) {
                //calls select and showEdit methods
                select();
                showEdit();
            } else {
                //empty
            }
        });

        //default display of combo box filters
        typeFilter.getItems().addAll(Type.values());
        genderFilter.getItems().addAll(Gender.values());
        colorFilter.getItems().addAll(Colors.values());

        //items.setItems(list);
        //populates the size filter combo box on start
        sizeFilter.getItems().addAll("XS", "S", "M", "L", "XL", "0", "2", "4", "6",
                "8", "10", "12", "28W", "30W", "32W", "34W", "36W");

        //refreshes the table and calculates 
        update();
    }

    /**
     * Returns the MainDocumentController
     *
     * @return returns the main controller
     */
    public static MainDocumentController getController() {
        return controller;
    }

    /**
     * Updates the table and calculates inventory statistics
     */
    public void update() {
        items.refresh();
        calculate();
    }

    /**
     * Calculates the inventory's statistics
     */
    public void calculate() {
        int q = 0;
        double v = 0;
        //for loop to calculate total price and total quantity of items in inventory
        for (int i = 0; i < list.size(); i++) {
            q += list.get(i).getQuantity();
            v += (list.get(i).getPrice() * list.get(i).getQuantity());
        }
        //sets the informational values
        uniqueProducts.setText("Unique Products: " + Integer.toString(list.size()));
        totalProducts.setText("Total Products: " + q);
        totalValue.setText("Total Value: $" + String.format("%.2f", v));

    }

    /**
     * Sets the list of items using Clothing objects passed from add or edit
     * controllers
     *
     * @param c
     */
    public void setList(Clothing c) {
        list.add(c);
        items.setItems(list);
        calculate();
    }

    /**
     * Creates the current list of items using the local List.txt file
     *
     * @return the current list of clothing items from a text file
     */
    public ObservableList<Clothing> getList() {

        //create new Clothing objects and an ArrayList
        ArrayList<Clothing> clist = new ArrayList<>();
        Clothing c = new Clothing();
        BufferedReader reader = null;

        //read from a text file in local folder and add each line of object to an ArrayList
        try {
            File file = new File("List.txt");
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] names = line.split(", ");

                c = new Clothing(Integer.parseInt(names[0]),
                        Type.valueOf(names[1]), Gender.valueOf(names[2]),
                        names[3], Colors.valueOf(names[4]),
                        Double.parseDouble(names[5]),
                        Integer.parseInt(names[6]));
                if (names[7] != null) {
                c.setURL(names[7]);
                }
                clist.add(c);
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        //add the ArrayList to the ObservableList
        ObservableList<Clothing> list = FXCollections.observableArrayList(clist);

        //sets the current list to the new list and return the list
        this.list = list;
        this.filter = new FilteredList(list, e -> true);
        return list;
    }

    /**
     * Select method that activates inEdit variable and collects index from
     * object
     */
    public void select() {

        //if the clicked item is not empty then set edit to true
        if (!items.getSelectionModel().isEmpty()) {
            inEdit = true;
            Clothing selected = items.getSelectionModel().getSelectedItem();
            indexOnEditing = items.getSelectionModel().getSelectedIndex();
            this.indexOnEditing = indexOnEditing;
            this.selected = selected;
        }
    }

    /**
     *
     * @param event Clicking Add opens a new Add Item window
     */
    @FXML
    private void addHandle(ActionEvent event) {

        //creates a new Add item window upon clicking Add
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AddItems.fxml"));

            AddItemsController addItemsController = new FXMLLoader().getController();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Items");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println("Error" + e);
        }

    }

    /**
     *
     * @param event Calls the showEdit() method
     */
    @FXML
    private void editHandle(ActionEvent event) {
        //calls the showEdit method, displays Edit item window
        showEdit();
    }

    /**
     * Opens a new Edit window
     */
    public void showEdit() {

        try {

            if (!items.getSelectionModel().isEmpty()) {
                // Loading the modify part window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditItems.fxml"));
                Parent root = (Parent) loader.load();

                loader.<EditItemsController>getController().setParentController(this);
                EditItemsController editItemsController = loader.getController();
                //passes the object index to the Edit controller
                editItemsController.indexEdit(indexOnEditing);
                //passes the current list of objects
                editItemsController.editDisplay(list);

                Stage stage = new Stage();
                //keeps the new window focused
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Edit Item");
                Scene scene = new Scene(root);
                stage.setScene(scene);
                scene.getStylesheets().add(getClass().getResource("style_1.css").toExternalForm());
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

    /**
     *
     * @param event Clicking Remove deletes the selected row of object
     */
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
                //gets the selected item and removes it, updates the table values
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

    @FXML
    private void searchHandle(ActionEvent event) {

    }

    /**
     *
     * @param event Type filters for the clothing item list
     */
    @FXML
    private void typeFilterHandle(ActionEvent event) {

        try {
            // takes a selection and compares it with parameters in observableList using addListener
            //  typeFilter.getSelectionModel().selectedItemProperty()
            //         .addListener((ObservableValue<? extends Type> observable, Type oldValue, Type newValue) -> {
            // filters for all Clothing objects
            filter.setPredicate((Predicate<? super Clothing>) (Clothing c) -> {
                if (null != typeFilter.getSelectionModel().getSelectedItem()) // when DRESS type selection is made, do something
                {
                    // disables all other comboboxes when one is selected
                    genderFilter.setDisable(true);
                    sizeFilter.setDisable(true);
                    colorFilter.setDisable(true);

                    switch (typeFilter.getSelectionModel().getSelectedItem()) {
                        case Dress:
                            // search all items currently in ArrayList list
                            for (Clothing list1 : list) {
                                // if type of clothing object is DRESS then return true to be sorted
                                if (c.getType() == Type.Dress) {
                                    return true;
                                }
                            }
                            break;

                        case Shorts:
                            for (Clothing list1 : list) {
                                if (c.getType() == Type.Shorts) {
                                    return true;
                                }
                            }
                            break;

                        case Skirts:
                            for (Clothing list1 : list) {
                                if (c.getType() == Type.Skirts) {
                                    return true;
                                }
                            }
                            break;

                        case Shirts:
                            for (Clothing list1 : list) {
                                if (c.getType() == Type.Shirts) {
                                    return true;
                                }
                            }
                            break;

                        case Outerwear:
                            for (Clothing list1 : list) {
                                if (c.getType() == Type.Outerwear) {
                                    return true;
                                }
                            }
                            break;

                        case Jeans:
                            for (Clothing list1 : list) {
                                if (c.getType() == Type.Jeans) {
                                    return true;
                                }
                            }
                            break;

                        case Sleepwear:
                            for (Clothing list1 : list) {
                                if (c.getType() == Type.Sleepwear) {
                                    return true;
                                }
                            }
                            break;
                        case Pants:
                            for (Clothing list1 : list) {
                                if (c.getType() == Type.Pants) {
                                    return true;
                                }
                            }
                            break;

                        case Sweaters:
                            for (Clothing list1 : list) {
                                if (c.getType() == Type.Sweaters) {
                                    return true;
                                }
                            }
                            break;

                        default:
                            break;
                    }
                }
                return false;
            });
            //********** });
            // returns a new SortedList with FilteredList applied using the combobox parameters
            SortedList sort = new SortedList(filter);
            sort.comparatorProperty().bind(items.comparatorProperty());
            items.setItems(sort);
            // clears filters after each selection

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     *
     * @param event Gender filter for the clothing item list
     */
    @FXML
    private void genderFilterHandle(ActionEvent event) {
        try {
            // genderFilter.getSelectionModel().selectedItemProperty()
            //         .addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super Clothing>) (Clothing c) -> {
                if (null != genderFilter.getSelectionModel().getSelectedItem()) {
                    typeFilter.setDisable(true);
                    sizeFilter.setDisable(true);
                    colorFilter.setDisable(true);

                    switch (genderFilter.getSelectionModel().getSelectedItem()) {
                        case Male:
                            for (Clothing list1 : list) {
                                if (c.getGender() == Gender.Male) {
                                    return true;
                                }
                            }
                            break;

                        case Female:
                            for (Clothing list1 : list) {
                                if (c.getGender() == Gender.Female) {
                                    return true;
                                }
                            }
                            break;

                        case Girls:
                            for (Clothing list1 : list) {
                                if (c.getGender() == Gender.Girls) {
                                    return true;
                                }
                            }
                            break;

                        case Boys:
                            for (Clothing list1 : list) {
                                if (c.getGender() == Gender.Boys) {
                                    return true;
                                }
                            }
                            break;

                        default:
                            break;
                    }
                }
                return false;
            });
            // });
            SortedList sort = new SortedList(filter);
            sort.comparatorProperty().bind(items.comparatorProperty());
            items.setItems(sort);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     *
     * @param event Size filter for clothing item list
     */
    @FXML
    private void sizeFilterHandle(ActionEvent event) {
        try {
            //sizeFilter.getSelectionModel().selectedItemProperty()
            //      .addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super Clothing>) (Clothing c) -> {
                if (null != sizeFilter.getSelectionModel().getSelectedItem()) {
                    typeFilter.setDisable(true);
                    genderFilter.setDisable(true);
                    colorFilter.setDisable(true);

                    switch (sizeFilter.getSelectionModel().getSelectedItem()) {
                        // sizes are compared using strings instead of enums 
                        case "XS":
                            for (Clothing list1 : list) {
                                if ("XS".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "S":
                            for (Clothing list1 : list) {
                                if ("S".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "M":
                            for (Clothing list1 : list) {
                                if ("M".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "L":
                            for (Clothing list1 : list) {
                                if ("L".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "XL":
                            for (Clothing list1 : list) {
                                if ("XL".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "0":
                            for (Clothing list1 : list) {
                                if ("0".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "2":
                            for (Clothing list1 : list) {
                                if ("2".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "4":
                            for (Clothing list1 : list) {
                                if ("4".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "6":
                            for (Clothing list1 : list) {
                                if ("6".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "8":
                            for (Clothing list1 : list) {
                                if ("8".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "10":
                            for (Clothing list1 : list) {
                                if ("10".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "12":
                            for (Clothing list1 : list) {
                                if ("12".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "28W":
                            for (Clothing list1 : list) {
                                if ("28W".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "30W":
                            for (Clothing list1 : list) {
                                if ("30W".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "32W":
                            for (Clothing list1 : list) {
                                if ("32W".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "34W":
                            for (Clothing list1 : list) {
                                if ("34W".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        case "36W":
                            for (Clothing list1 : list) {
                                if ("36W".equals(c.getSize())) {
                                    return true;
                                }
                            }
                            break;

                        default:
                            break;
                    }
                }
                return false;
            });
            //  });
            SortedList sort = new SortedList(filter);
            sort.comparatorProperty().bind(items.comparatorProperty());
            items.setItems(sort);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     *
     * @param event Color filter for the clothing item list
     */
    @FXML
    private void colorFilterHandle(ActionEvent event) {
        try {
            //  colorFilter.getSelectionModel().selectedItemProperty()
            //        .addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super Clothing>) (Clothing c) -> {
                if (null != colorFilter.getSelectionModel().getSelectedItem()) {
                    typeFilter.setDisable(true);
                    genderFilter.setDisable(true);
                    sizeFilter.setDisable(true);

                    switch (colorFilter.getSelectionModel().getSelectedItem()) {
                        case Red:
                            for (Clothing list1 : list) {
                                if (c.getColor() == Colors.Red) {
                                    return true;
                                }
                            }
                            break;

                        case Orange:
                            for (Clothing list1 : list) {
                                if (c.getColor() == Colors.Orange) {
                                    return true;
                                }
                            }
                            break;

                        case Yellow:
                            for (Clothing list1 : list) {
                                if (c.getColor() == Colors.Yellow) {
                                    return true;
                                }
                            }
                            break;

                        case Green:
                            for (Clothing list1 : list) {
                                if (c.getColor() == Colors.Green) {
                                    return true;
                                }
                            }
                            break;

                        case Blue:
                            for (Clothing list1 : list) {
                                if (c.getColor() == Colors.Blue) {
                                    return true;
                                }
                            }
                            break;

                        case White:
                            for (Clothing list1 : list) {
                                if (c.getColor() == Colors.White) {
                                    return true;
                                }
                            }
                            break;

                        case Black:
                            for (Clothing list1 : list) {
                                if (c.getColor() == Colors.Black) {
                                    return true;
                                }
                            }
                            break;

                        default:
                            break;
                    }
                }
                return false;
            });
            // });
            SortedList sort = new SortedList(filter);
            sort.comparatorProperty().bind(items.comparatorProperty());
            items.setItems(sort);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     *
     * @param event Saves the list of items as a text file in local storage
     */
    @FXML
    private void saveHandle(ActionEvent event) {

        //initializes a filechooser to allow user to choose where to save the text file
        FileChooser fileChooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File(currentPath));

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Save as");
        File file = fileChooser.showSaveDialog(null);

        //if file exists then print the object one row at a time
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

    /**
     *
     * @param event Opening the file sets the table list as the text file opened
     * then calculates the values
     */
    @FXML
    private void openFileHandle(ActionEvent event) {
        items.setItems(open());
        calculate();

    }

    /**
     *
     * @return Open method to read a file from a selected text file
     */
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
                if (names[7] != null) {
                c.setURL(names[7]);
                }
                clist.add(c);
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        ObservableList<Clothing> list = FXCollections.observableArrayList(clist);

        this.list = list;
        this.filter = new FilteredList(list, e -> true);
        return list;

    }

    /**
     *
     * @param event Resets the filter and search bar
     */
    @FXML
    private void resetHandle(ActionEvent event) {
        setPrompt();
        typeFilter.setDisable(false);
        genderFilter.setDisable(false);
        sizeFilter.setDisable(false);
        colorFilter.setDisable(false);

        items.setItems(list);
    }

    public void setPrompt() {
        typeFilter.setPromptText("Type");
        genderFilter.setPromptText("Gender");
        sizeFilter.setPromptText("Size");
        colorFilter.setPromptText("Color");
        searchField.setText("");
    }

    /**
     *
     * @param event Search function for the search bar
     */
    @FXML
    private void search(KeyEvent event) {

        // takes an input and compares it with parameters in observableList using addListener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super Clothing>) (Clothing c) -> {
                if (newValue.isEmpty() || newValue == null) {
                    return true;
                } else if (c.getSize().toLowerCase().contains(newValue)) {
                    return true;
                } else if (String.valueOf(c.getProductId()).toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                } else if (String.valueOf(c.getPrice()).toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                } else if (String.valueOf(c.getColor()).toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                } else if (String.valueOf(c.getType()).toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                } else if (String.valueOf(c.getGender()).toLowerCase().contains(newValue.toLowerCase())) {
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

	@FXML
	private void mainDragged(MouseEvent event) {
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		stage.setX(event.getScreenX()- x);
		stage.setY(event.getScreenY()- y);
	}

	@FXML
	private void mainPressed(MouseEvent event) {
		event.getSceneX();
		event.getSceneY();
	}


	@FXML
	private void min(MouseEvent event) {
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		stage.setIconified(true);
	}

	@FXML
	private void max(MouseEvent event) {
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		stage.setFullScreenExitHint(" ");
		
		if(stage.isFullScreen())
		stage.setFullScreen(false);
		else
		stage.setFullScreen(true);

	}
	@FXML
	private void btnClose(MouseEvent event) {
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}

}
