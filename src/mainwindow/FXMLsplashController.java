/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainwindow;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Splash effects controller class
 *
 * @author Aziz
 */
public class FXMLsplashController implements Initializable {

	@FXML
	private AnchorPane splashScene;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// an object of PauseTransation with duration of two seconds
		PauseTransition delay = new PauseTransition(Duration.seconds(2));
		// set the delay on event to be called swapScenes when the duration time finishes
		delay.setOnFinished(event2
			//call swap screen to swap the scenes
			-> swapScenes()
		);
		
		delay.play();

	}

	//method replaces the splash screen scene with the main window screen 
	public void swapScenes() {

		try {
			// get the stage of the current screen 
			Stage login = (Stage)splashScene.getScene().getWindow();
			//load the main screen 	
			Parent root = FXMLLoader.load(getClass().getResource("MainDocument.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add("mainStyle.css");


                        //login.setMinHeight(560);
                        //login.setMinWidth(400);
                        // swap the scenes 
			login.setScene(scene);
                         login.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
}