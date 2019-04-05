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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //mainController = MainDocumentController.getController();
        // TODO
    }

    @FXML
    private void userTextField(ActionEvent event) {
    }

    @FXML
    private void passwdFieldHandler(ActionEvent event) {
    }

    @FXML
    private void loginBtnHandler(ActionEvent event) {
        //if (username.getText().equals("admin") && password.getText().equals("admin")) {
            /*
            Parent newRoot = FXMLLoader.load(getClass().getResource("MainDocument.fxml"));
            root = ((Node) event.getSource()).getScene().getRoot();
            ((Node) event.getSource()).getScene().setRoot(newRoot);
*/
            Stage login = (Stage)loginBtn.getScene().getWindow();
            login.close();
            
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MainDocument.fxml"));

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setMinWidth(865);
            stage.setMinHeight(525);
            stage.setTitle("Cluz");
            stage.setScene(scene);
            stage.show();
                
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Login Error");
            alert.setContentText("Username or Password Incorrect");
            alert.showAndWait();
       }
       */
    }

    @FXML
    private void signupBtnHandler(ActionEvent event) {
    }

}
