/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainwindow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author John
 */
public class LoginController implements Initializable {

    @FXML
    private VBox Vbox;
    @FXML
    private Label title;
    @FXML
    private Label userlbl;
    @FXML
    private TextField username;
    @FXML
    private Label passwdlbl;
    @FXML
    private TextField password;
    @FXML
    private Button loginBtn;
    @FXML
    private Button signupBtn;

    private static Parent root;

    public static Parent getRoot() {
        return root;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void userTextField(ActionEvent event) {
    }

    @FXML
    private void passwdFieldHandler(ActionEvent event) {
    }

    @FXML
    private void loginBtnHandler(ActionEvent event) throws IOException {
        if (username.getText().equals("admin") && password.getText().equals("admin")) {
            Parent newRoot = FXMLLoader.load(getClass().getResource("MainDocument.fxml"));
            root = ((Node) event.getSource()).getScene().getRoot();
            ((Node) event.getSource()).getScene().setRoot(newRoot);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Login Error");
            alert.setContentText("Username or Password Incorrect");
            alert.showAndWait();
        }
    }

    @FXML
    private void signupBtnHandler(ActionEvent event) {
    }

}
