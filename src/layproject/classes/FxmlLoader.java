// classes.loader package
package layproject.classes;

// Importing required module, library, and package
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

// FxmlLoader class
public class FxmlLoader {
    
    // Set class variables
    private Pane view; 
    
    // Method to get view of the provided file URL
    public Pane getView(String file) throws IOException {
        view = FXMLLoader.load(getClass().getResource(file));
        return view;
    }
    
    public FXMLLoader loadStage(String file) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
        return loader;
    }
    
    // Method to show the stage of the provided file URL
    public void showStage(String file) throws IOException {
        Parent root = (Parent) loadStage(file).load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    // Method to show alert with the provided message
    public void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}