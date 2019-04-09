/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainwindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Login Controller class
 *
 * @author John
 */
public class LoginController implements Initializable {


    private Image image;
	@FXML
	private VBox Vbox;
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
	private ImageView logo;


	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
        Image image = new Image("file:logo.png");
         logo.setImage(image);
    
	}

	@FXML
	private void userTextField(ActionEvent event) {
	}

	@FXML
	private void passwdFieldHandler(ActionEvent event) {
	}

	@FXML
	private void loginBtnHandler(ActionEvent event) {
		if (username.getText().equals("admin") && password.getText().equals("admin")) {
		
         
		Stage login = (Stage) loginBtn.getScene().getWindow();
		login.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("FXMLsplash.fxml"));

			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			
			stage.setTitle("Cluz");
			stage.setScene(scene);
                         //stage.initStyle(StageStyle.UNDECORATED);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Login Error");
            alert.setContentText("Username or Password Incorrect");
            alert.showAndWait();
       }
		 
	}
}
