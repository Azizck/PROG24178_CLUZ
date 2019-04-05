/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainwindow;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**
 *
 * @author CS
 */
public class MainWindow extends Application {
    
    /*
    //Static global variable for the controller (where MyController is the name of your controller class

static MainDocumentController mainDocumentController;

@Override
public void start(Stage stage) throws Exception {

    //Set up instance instead of using static load() method
    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDocument.fxml"));
    Parent root = loader.load();

    //Now we have access to getController() through the instance... don't forget the type cast
    mainDocumentController = (MainDocumentController)loader.getController();

    Scene scene = new Scene(root);

    stage.setScene(scene);
    stage.show();
}
    */
    
    @Override
    public void start(Stage stage) throws Exception {
        try {
        Parent root = FXMLLoader.load(getClass().getResource("MainDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Cluz");
        stage.show();
               
        
     } catch (IOException e) {
        Logger logger = Logger.getLogger(getClass().getName());
        logger.log(Level.SEVERE, "Failed to create new Window.", e);
    }
        
    } 
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
