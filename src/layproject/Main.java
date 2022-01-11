/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layproject;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Importing required module, libary, and package
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import layproject.classes.FxmlLoader;

// Main class extends Application
public class Main extends Application {
    
    private final FxmlLoader loader = new FxmlLoader();
    
    // Start the JavaFX application
    @Override
    public void start(Stage stage) throws Exception {
        loader.showStage("/layproject/fxml/Login.fxml");
    }

    public static void main(String[] args) {
        // Launch application
        launch(args);
    } 
}
