/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layproject.controller;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import layproject.classes.FxmlLoader;

/**
 * FXML Controller class
 *
 * @author Michael Christopher
 */
public class ManagerDashboardController implements Initializable {

    private final FxmlLoader loader = new FxmlLoader();
    
    @FXML
    private BorderPane dashboardManagerPane;
    @FXML
    private JFXButton membershipsBtn;
    @FXML
    private JFXButton homeBtn;
    @FXML
    private JFXButton menuBtn;
    @FXML
    private JFXButton staffBtn;
    @FXML
    private JFXButton staffCatBtn;
    @FXML
    private JFXButton historyBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    private void menuButtonCLicked(ActionEvent event) throws IOException {
        Pane view = loader.getView("/layproject/fxml/menuDashboard.fxml");    
        dashboardManagerPane.setCenter(view);
    }

    @FXML
    private void membershipsButtonClicked(ActionEvent event) throws IOException {
        Pane view = loader.getView("/layproject/fxml/membershipsDashboard.fxml");    
        dashboardManagerPane.setCenter(view);
    }

    @FXML
    private void staffButtonClicked(ActionEvent event) throws IOException{
        Pane view = loader.getView("/layproject/fxml/staffDashboard.fxml");    
        dashboardManagerPane.setCenter(view);
    }

    @FXML
    private void staffCatButtonClicked(ActionEvent event) throws IOException{
        Pane view = loader.getView("/layproject/fxml/staffCatDashboard.fxml");    
        dashboardManagerPane.setCenter(view);
    }
    
    @FXML
    private void homeBtnClicked(ActionEvent event) throws IOException{
        Stage stage = (Stage) homeBtn.getScene().getWindow();
        stage.close();    
        loader.showStage("/layproject/fxml/Login.fxml");
    }

    @FXML
    private void historyButtonClicked(ActionEvent event) throws IOException {
        Pane view = loader.getView("/layproject/fxml/invoiceOrderDashboard2.fxml");    
        dashboardManagerPane.setCenter(view);
    } 
}
